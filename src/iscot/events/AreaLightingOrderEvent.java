package iscot.events;
import iscot.enums.Areas;
public class AreaLightingOrderEvent extends HighLevelOrderEvent{
    
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
}
