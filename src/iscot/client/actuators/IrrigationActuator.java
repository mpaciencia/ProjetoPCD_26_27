package iscot.client.actuators;

import iscot.client.gui.IrrigationIcon;
import iscot.enums.Areas;
import iscot.events.AreaIrrigationOrderEvent;

import javax.swing.*;
import java.awt.*;

public class IrrigationActuator {

    public static void main(String[] args) {
        Areas area = args.length >= 1
                ? Areas.valueOf(args[0])
                : Areas.Garden;

            JFrame janela = new JFrame("Rega em " + area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            IrrigationIcon rega = new IrrigationIcon();
            JLabel estado = new JLabel("", SwingConstants.CENTER);

            JPanel painel = new JPanel(new BorderLayout());
            painel.add(rega, BorderLayout.CENTER);
            painel.add(estado, BorderLayout.SOUTH);

            janela.setContentPane(painel);
            janela.pack();
            janela.setLocationRelativeTo(null);

            // Ordem de exemplo: regar a zona 1 durante 10 minutos.
            AreaIrrigationOrderEvent ordem =
                    new AreaIrrigationOrderEvent(area, 1, 10, true);

            rega.setZona(ordem.getZona());
            rega.setDuracao(ordem.getDuracao());
            rega.setAtiva(ordem.isAtiva());

            estado.setText("Zona " + ordem.getZona()
                    + " — rega ativa durante "
                    + ordem.getDuracao() + " min");

            System.out.println(ordem);

            janela.setVisible(true);
    }
}
