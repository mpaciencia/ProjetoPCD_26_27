package iscot.client;

import iscot.events.Event;
import iscot.events.RegistrationEvent;
import iscot.protocol.RegistrationResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {

    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final String deviceId;

    public ServerConnection(ClientArgs config) throws IOException {
        socket = new Socket(config.getHost(), config.getPort());

        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(socket.getInputStream());

        RegistrationEvent registration = config.toRegistration();
        output.writeObject(registration);
        output.flush();

        try {
            Object response = input.readObject();

            if(!(response instanceof RegistrationResponse)) {
                throw new IOException("O servidor enviou uma resposta de registo inválido");
            }

            RegistrationResponse registrationResponse =  (RegistrationResponse) response;

            if(!registrationResponse.isAceite()) {
                throw new IOException("Registo recusado pelo servidor: " + registrationResponse.getMensagem());
            }

            deviceId = registrationResponse.getDeviceId();

            if(deviceId == null || deviceId.isBlank()) {
                throw new IOException("O servidor aceitou o registo sem avisar um deviceId");
            }

        } catch (ClassNotFoundException e) {
            close();
            throw new IOException("Não foi possível interpretar a resposta do servidor", e);
        } catch (IOException e) {
            close();
            throw e;
        }

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void sendEvent(Event event) throws IOException {
        output.writeObject(event);
        output.flush();
        output.reset();
    }

    public void close() throws IOException {
        try {
            input.close();
        } finally {
            try {
                output.close();
            } finally {
                socket.close();
            }
        }
    }

}
