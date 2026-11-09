package iscot.events;

import iscot.enums.Areas;

public abstract class SensorEvent extends Event {
    
    public SensorEvent(Areas area){
        super(area);
    }
}
