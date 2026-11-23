package iscot.events.order;

import iscot.enums.Areas;

public class AreaVentilationOrderEvent extends HighLevelOrderEvent {

    private static final long serialVersionUID = 1L;
    private final boolean ligado;

    public AreaVentilationOrderEvent(Areas area, boolean ligado){
        super(area);
        this.ligado = ligado;
    }

    public boolean isLigado(){
        return ligado;
    }

    @Override
    public String toString() {
        return "AreaVentilationOrderEvent{area = " + getArea() + ", ligado = " + ligado + "}";
    }
}
