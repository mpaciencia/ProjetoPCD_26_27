package iscot.events;

import java.io.Serializable;
import java.util.Objects;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Event implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final Type type;
    private final Areas area;

    protected Event(Type type, Areas area) {
        this.area = Objects.requireNonNull(area, "A área é obrigatória.");
        this.type = Objects.requireNonNull(type, "O tipo é obrigatório.");
    }
    public Type getType(){
        return type;
    }
    public Areas getArea() {
        return area;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + type + ", area=" + area + "]";
    }
    
}
