package iscot.events.sensor;

import iscot.enums.Areas;
import iscot.events.Event;
import iscot.enums.Type;

public abstract class SensorEvent extends Event {
    
    protected SensorEvent(Areas area){
        super(Type.SENSOR, area);
    }
}