package iscot.events.order;
import iscot.enums.Areas;
public class AreaLightingOrderEvent extends HighLevelOrderEvent{
    private static final long serialVersionUID = 1L;
    private final boolean ligada;
    private final int intensidade;

    public AreaLightingOrderEvent(Areas area, boolean ligada, int intensidade){
        super(area);
        if(intensidade < 0 || intensidade > 100){
            throw new IllegalArgumentException("A intensidade deve estar entre 0 e 100.");
        }
        this.ligada = ligada;
        this.intensidade = intensidade;
    }

    public boolean isLigada() {
        return ligada;
    }

    public int getIntensidade() {
        return intensidade;
    }

    @Override
    public String toString() {
        return "AreaLightingOrderEvent{area = " + getArea() + ", ligada = " + ligada
                + ", intensidade = " + intensidade + "%}";
    }
}
