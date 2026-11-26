package iscot.server;

import iscot.enums.Type;
import iscot.events.Event;
import iscot.events.RegistrationEvent;
import iscot.protocol.RegistrationResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

public class IscoTServer {
    private static final int DEFAULT_PORT = 12345;
    private static final int REGISTRATION_TIMEOUT_MS = 10_000;

    private final Map<String, ClientRecord> clients = new LinkedHashMap<>();

    private int nextDeviceNumber = 0;

    public static void main(String[] args){
        int port;

        try {
            port = parsePort(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println("Uso: java iscot.server.IscoTServer [PORT]");
            return;
        }

        new IscoTServer().start(port);
    }

    private static int parsePort(String[] args) {
        if(args.length > 1){
            throw new IllegalArgumentException("Indica zero ou um porto");
        }
        if(args.length == 0){
            return DEFAULT_PORT;
        }

        final int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O porto tem de ser um número");
        }

        if(port < 1 || port > 65535) {
            throw new IllegalArgumentException("O porto tem de estar entre 1 e 65535");
        }

        return port;
    }

    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("IscoT à escuta na porta " + port);

            while (true){
                Socket socket = serverSocket.accept();
                Thread clienThread = new Thread(
                        () -> handleClient(socket),
                        "iscot-client-" + socket.getRemoteSocketAddress()
                );
                // inicia uma nova thread que executa handleClient, assim a thread principal 
                // volta ao ciclo e chama outra vez accpet -> o servidor pode aceitar outros clientes
                // enquanto a thread do primeiro cliente espera por dados
                clienThread.start();
            }
        } catch (IOException e) {
            System.err.println("Não foi possível iniciar o servidor: " + e.getMessage());
        }
    }

    public void handleClient(Socket socket){
        String deviceId = null;

        try(socket) {
            socket.setSoTimeout(REGISTRATION_TIMEOUT_MS);
            /*
            * Esta ordem tem de coincidir com a do cliente:
            * criar ObjectOutputStream e fazer flush antes de criar
            * ObjectInputStream. 
            */
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        
            Object firstObject = input.readObject();

            if (!(firstObject instanceof RegistrationEvent)){
                sendResponse(output, new RegistrationResponse(false, null, "O primeiro objeto enviado tem de ser um registo"));
                return;
            }
            // servidor obtem os dados do registo
            RegistrationEvent registration = (RegistrationEvent) firstObject;
            Type type = registration.getType();

            if (type == null || registration.getArea() == null) {
                sendResponse(output, new  RegistrationResponse(false, null, "O registo tem tipo ou área inválida. "));
                return;
                }
            if (!isAcceptedType(type)) {
                sendResponse(output, new RegistrationResponse(false, null, "Tipo de cliente não aceite: " + type));
                return;
            }
            // atribuir o id e guardar o cliente
            deviceId = registerClient(socket, registration);

            sendResponse(output, new RegistrationResponse(true, deviceId, "Registo aceite."));

            System.out.println("Cliente registado: " + deviceId + " | área=" + registration.getArea());

            // O timeout aplica-se apenas à espera pelo registo.
            // desativar o timeout de leitura, a thread pode esperar
            // indefinidamente por novos objetos ou pelo fecho da ligacao
            socket.setSoTimeout(0);

            /* Na fase 4 não processamos eventos. Esta leitura mantém a
            * thread atenta ao fecho da ligação pelo cliente. A receção e 
            * validação de eventos entra na fase 5. 
            */
            while (true) {
                // readObject bloqueia enquanto o cliente nao envia nada, 
                // ciclo nao fica a consumir CPU infinita
                Object object = input.readObject();

                if(!(object instanceof Event)) {
                    System.err.println("Objeto inesperado de " + deviceId + ": " + object.getClass().getName());
                } else {
                    System.out.println("Evento recebido de " + deviceId + "(processamento previsto para a fase 5).");
                }
           }
        } catch (EOFException | SocketException e) {
            // fecho normal ou perda da ligação.
        } catch (ClassNotFoundException e ) {
            System.err.println("Classe recebida desconhecida: " + e.getMessage());
        } catch (IOException e){
            System.err.println("Erro na ligação " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        } finally {
            // finally executa quer o tratamento termine normalmente, quer ocorra excecao
            if (deviceId != null) {
                removeClient(deviceId);
                System.out.println("Cliente desligado: " + deviceId);
            }
        }
    }

    private boolean isAcceptedType(Type type) {
        return type == Type.SENSOR
                || type == Type.DECIDER
                || type == Type.ACTUATOR;
    }
    private synchronized String registerClient(Socket socket, RegistrationEvent registration){

        nextDeviceNumber++;
        String deviceId = registration.getType() + "-" + nextDeviceNumber;

        clients.put(deviceId, new ClientRecord(deviceId, registration.getType(), registration.getArea(), socket));
        
        return deviceId;
    }

    private synchronized void removeClient(String deviceId){
        clients.remove(deviceId);
    }
    // escreve o objeto, envia os bytes pendentes e limpa a a cache interna do stream
    private void sendResponse(ObjectOutputStream output, RegistrationResponse response) throws IOException {
        output.writeObject(response);
        output.flush();
        output.reset();
    }
    // guarda os dados associados a um cliente
    private static final class ClientRecord {
        private final String deviceId;
        private final Type type;
        private final iscot.enums.Areas area;
        private final Socket socket;

        private ClientRecord(String deviceId, Type type, iscot.enums.Areas area, Socket socket) {
            this.deviceId = deviceId;
            this.type = type;
            this.area = area;
            this.socket = socket;
        }
    }
}


/* Resumo do percurso
Servidor abre a porta
        ↓
accept() espera por uma ligação
        ↓
cria uma thread dedicada ao cliente
        ↓
a thread recebe e valida RegistrationEvent
        ↓
atribui ID e guarda ClientRecord
        ↓
envia RegistrationResponse
        ↓
fica à espera de dados ou do fecho
        ↓
remove o cliente e fecha o socket
*/