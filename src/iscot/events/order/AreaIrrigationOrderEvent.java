package iscot.events.order;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent{
    private static final long serialVersionUID = 1L;
    private final int zona;
    private final int duracao;
    private final boolean active;

    public AreaIrrigationOrderEvent(Areas area, int zona, int duracao, boolean active){
        super(area);

        if(zona < 1 || zona > 3){
            throw new IllegalArgumentException("A zona deve ser 1,2 ou 3.");
        }
        if(duracao < 0){
            throw new IllegalArgumentException("A duração não pode ser negativa.");
        }

        this.zona = zona;
        this.duracao = duracao;
        this.active = active;
    }

    public int getZona() {
        return zona;
    }

    public int getDuracao() {
        return duracao;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "AreaIrrigationOrderEvent{area = " + getArea() + ", zona = " + zona
                + ", duracao = " + duracao + " min, ativa = " + active + "}";
    }
}
