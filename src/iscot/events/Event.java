package iscot.events;

import java.io.Serializable;
import java.util.Objects;

import iscot.enums.Areas;

public abstract class Event implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final Areas area;

    public Event(Areas area) {
        this.area = Objects.requireNonNull(area, "A área é obrigatória.");
    }

    public Areas getArea() {
        return area;
    }
}
