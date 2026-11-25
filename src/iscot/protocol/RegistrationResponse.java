package iscot.protocol;

import java.io.Serializable;

public class RegistrationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean aceite;
    private final String deviceId;
    private final String mensagem;

    public RegistrationResponse(
            boolean aceite,
            String deviceId,
            String mensagem) {
        this.aceite = aceite;
        this.deviceId = deviceId;
        this.mensagem = mensagem;
    }

    public boolean isAceite() {
        return aceite;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMensagem() {
        return mensagem;
    }
}