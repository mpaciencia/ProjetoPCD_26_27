package iscot.events.sensor;

import iscot.enums.Areas;

public class LightSensorEvent extends SensorEvent {

    private static final long serialVersionUID = 1L;
    private final double light;

    public LightSensorEvent(Areas area, double light) {
        super(area);
        if (!Double.isFinite(light) || light < 0 || light > 100) {
            throw new IllegalArgumentException("A luminosidade deve estar entre 0 e 100.");
        }
        this.light = light;
    }

    public double getLight() {
        return light;
    }

    @Override
    public String toString(){
        return "LightSensorEvent{" + "area = " + getArea() + ", light = " + light + '}';
    }
}
