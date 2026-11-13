package iscot.client.actuators;

import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.events.AreaLightingOrderEvent;

import javax.swing.*;
import java.awt.*;

public class LightActuator {

    public static void main(String[] args) {
        Areas area = args.length >= 1
                ? Areas.valueOf(args[0])
                : Areas.LivingRoom;

            JFrame janela = new JFrame("Luz em " + area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            LightIcon lampada = new LightIcon();
            JLabel estado = new JLabel("Estado: desligada", SwingConstants.CENTER);

            JPanel painel = new JPanel(new BorderLayout());
            painel.add(lampada, BorderLayout.CENTER);
            painel.add(estado, BorderLayout.SOUTH);

            janela.setContentPane(painel);
            janela.pack();
            janela.setLocationRelativeTo(null);

            // Ordem de exemplo para a fase 3.
            AreaLightingOrderEvent ordem =
                    new AreaLightingOrderEvent(area, true, 80);

            lampada.setIntensidade(ordem.getIntensidade());
            lampada.setLigada(ordem.isLigada());

            estado.setText("Estado: ligada — intensidade "
                    + ordem.getIntensidade() + "%");

            System.out.println(ordem);

            janela.setVisible(true);
    }
}
