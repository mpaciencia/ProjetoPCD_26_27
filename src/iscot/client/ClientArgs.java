package iscot.client;

import java.util.Arrays;
import java.util.Locale;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.RegistrationEvent;

public class ClientArgs {
    private final String host;
    private final int port;
    private final Areas area;
    private final Type type;

    private ClientArgs(String host, int port, Areas area, Type type) {
        this.host = host;
        this.port = port;
        this.area = area;
        this.type = type;
    }

    public static ClientArgs parseOrExit(String[] args, Type expectedType) {
        try {
            return parse(args, expectedType);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println(
                    "Uso: java <classe> IP PORT AREA TIPO"
                            + " (TIPO = " + expectedType + ")"
            );
            throw new IllegalArgumentException("Argumentos inválidos.", e);
        }
    }

    private static ClientArgs parse(String[] args, Type expectedType) {
        if (args == null || args.length != 4) {
            throw new IllegalArgumentException(
                    "São necessários 4 argumentos: IP PORT AREA TIPO."
            );
        }

        String host = args[0].trim();
        if (host.isEmpty()) {
            throw new IllegalArgumentException(
                    "O endereço do servidor está vazio."
            );
        }

        final int port;
        try {
            port = Integer.parseInt(args[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Porto inválido: " + args[1]
            );
        }

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException(
                    "O porto deve estar entre 1 e 65535."
            );
        }

        final Areas area;
        try {
            area = Areas.valueOf(args[2].trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Área inválida. Valores aceites: "
                            + Arrays.toString(Areas.values())
            );
        }

        final Type type;
        try {
            type = Type.valueOf(args[3].trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo inválido. Valores aceites: SENSOR, DECIDER, ACTUATOR."
            );
        }

        if (type == Type.DWC) {
            throw new IllegalArgumentException("O tipo DWC não é aceite.");
        }

        if (type != expectedType) {
            throw new IllegalArgumentException(
                    "Esta aplicação exige o tipo " + expectedType
                            + ", mas foi indicado " + type + "."
            );
        }

        return new ClientArgs(host, port, area, type);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Areas getArea() {
        return area;
    }

    public Type getType() {
        return type;
    }

    public RegistrationEvent toRegistration() {
        return new RegistrationEvent(area, type);
    }
}