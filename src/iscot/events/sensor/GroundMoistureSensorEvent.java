package iscot.events.sensor;

import iscot.enums.Areas;

public class GroundMoistureSensorEvent extends SensorEvent {

    private static final long serialVersionUID = 1L;
    private final double groundMoisture;

    public GroundMoistureSensorEvent(Areas area, double groundMoisture) {
        super(area);
        if (!Double.isFinite(groundMoisture) || groundMoisture < 0 || groundMoisture > 100) {
            throw new IllegalArgumentException("A humidade do solo deve estar entre 0 e 100.");
        }
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
