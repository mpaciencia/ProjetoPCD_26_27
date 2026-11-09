package iscot.events;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent{
    private final int zona;
    private final int duracao;
    private final boolean ativa;

    public AreaIrrigationOrderEvent(Areas area, int zona, int duracao, boolean ativa){
        super(area);

        if(zona < 1 || zona > 3){
            throw new IllegalArgumentException("A zona deve ser 1,2 ou 3.");
        }
        if(duracao < 0){
            throw new IllegalArgumentException("A duração não pode ser negativa.");
        }

        this.zona = zona;
        this.duracao = duracao;
        this.ativa = ativa;
    }

    public int getZona() {
        return zona;
    }

    public int getDuracao() {
        return duracao;
    }

    public boolean isAtiva() {
        return ativa;
    }
}
