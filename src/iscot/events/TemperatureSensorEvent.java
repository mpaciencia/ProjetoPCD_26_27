package iscot.events;

import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {

    private static final long serialVersionUID = 1L;
    private double temperature;

    public TemperatureSensorEvent(Areas area, double temperature) {
        super(area);
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString(){
        return "TemperatureSensorEvent{" + "area = " + getArea() + ", temperature = " + temperature + '}';
    }
}
