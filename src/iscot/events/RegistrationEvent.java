package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {

    private static final long serialVersionUID = 1L;

    public RegistrationEvent(Areas area, Type clientType) {
        super(clientType, area);
    }
}
