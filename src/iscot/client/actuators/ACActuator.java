package iscot.client.actuators;

import iscot.client.gui.ACIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.events.AreaTemperatureOrderEvent;

import javax.swing.*;
import java.awt.*;

public class ACActuator {

    public static void main(String[] args) {
        Areas area = args.length >= 1
                ? Areas.valueOf(args[0])
                : Areas.LivingRoom;

            JFrame janela = new JFrame("Ar condicionado em " + area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ACIcon arCondicionado = new ACIcon();
            JLabel estado = new JLabel("", SwingConstants.CENTER);

            JPanel painel = new JPanel(new BorderLayout());
            painel.add(arCondicionado, BorderLayout.CENTER);
            painel.add(estado, BorderLayout.SOUTH);

            janela.setContentPane(painel);
            janela.pack();
            janela.setLocationRelativeTo(null);

            // Ordem de exemplo: arrefecer até 22 °C.
            AreaTemperatureOrderEvent ordem =
                    new AreaTemperatureOrderEvent(
                            area, AirConditioningMode.Frio, 22);

            arCondicionado.setTemperatura(ordem.getTemperatura());
            arCondicionado.setModo(ordem.getModo());

            estado.setText("Modo: " + ordem.getModo()
                    + " — " + ordem.getTemperatura() + " °C");

            System.out.println(ordem);

            janela.setVisible(true);
    }
}
