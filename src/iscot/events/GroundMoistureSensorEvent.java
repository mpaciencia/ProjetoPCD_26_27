package iscot.events;

import iscot.enums.Areas;

public class GroundMoistureSensorEvent extends SensorEvent {

    private static final long serialVersionUID = 1L;
    private final double groundMoisture;

    public GroundMoistureSensorEvent(Areas area, double groundMoisture) {
        super(area);
        this.groundMoisture = groundMoisture;
    }

    public double getGroundMoisture() {
        return groundMoisture;
    }

    @Override
    public String toString(){
        return "GroundMoistureSensorEvent{" + "area = " + getArea() + ", groundMoisture = " + groundMoisture + "%}";
    }

}
