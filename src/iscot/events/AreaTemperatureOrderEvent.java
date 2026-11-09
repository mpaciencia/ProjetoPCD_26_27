package iscot.events;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent{
    private final AirConditioningMode modo;
    private final double temperatura;

    public AreaTemperatureOrderEvent(Areas area, AirConditioningMode modo, double temperatura){
        super(area);
        if(modo==null){
            throw new IllegalArgumentException("O modo não pode ser nulo");
        }
        if(!Double.isFinite(temperatura) || temperatura < 16 || temperatura > 30){
            throw new IllegalArgumentException("A temperatura deve estar entre 16 e 30 ºC");
        }
        this.modo = modo;
        this.temperatura = temperatura;
    }

    public AirConditioningMode getModo() {
        return modo;
    }

    public double getTemperatura() {
        return temperatura;
    }
}
