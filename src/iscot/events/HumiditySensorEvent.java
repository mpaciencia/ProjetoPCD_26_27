package iscot.events;

import iscot.enums.Areas;

public class HumiditySensorEvent extends SensorEvent{

    private final double humidity;

    public HumiditySensorEvent(Areas area, double humidity){
        super(area);

        if(!Double.isFinite(humidity) || humidity < 0 || humidity > 100){
            throw new IllegalArgumentException("A humidade deve estar entre 0 e 100%.");
        }

        this.humidity = humidity;
    }

    public double getHumidity(){
        return humidity;
    }
}
