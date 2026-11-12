package iscot.events;

import java.util.Objects;
import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {

    private static final long serialVersionUID = 1L;

    private final Type type;

    public RegistrationEvent(Areas area, Type type) {
        super(area);
        this.type = Objects.requireNonNull(type, "O tipo de cliente é obrigatório.");
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RegistrationEvent{" + "area=" + getArea() + ", type=" + type + '}';
    }
}
