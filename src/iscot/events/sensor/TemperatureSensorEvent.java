package iscot.events.sensor;

import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {

    private static final long serialVersionUID = 1L;
    private final double temperature;

    public TemperatureSensorEvent(Areas area, double temperature) {
        super(area);
        if (!Double.isFinite(temperature) || temperature < 16 || temperature > 30) {
            throw new IllegalArgumentException("A temperatura deve estar entre 16 e 30 °C.");
        }
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
