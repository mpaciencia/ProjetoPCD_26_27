package iscot.client.actuators;

import iscot.client.gui.ACIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.events.order.AreaTemperatureOrderEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ACActuator {

    public static void main(String[] args) {
        Areas area = args.length >= 3
                ? Areas.valueOf(args[2])
                : args.length >= 1
                ? Areas.valueOf(args[0])
                : Areas.LivingRoom;

            JFrame janela = new JFrame("Ar condicionado em " + area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ACIcon arCondicionado = new ACIcon();
            JLabel estado = new JLabel("", SwingConstants.CENTER);
            DefaultListModel<String> historico = new DefaultListModel<>();
            JList<String> listaHistorico = new JList<>(historico);
            JScrollPane historicoScroll = new JScrollPane(listaHistorico);
            historicoScroll.setBorder(BorderFactory.createTitledBorder("Histórico"));

            JPanel painel = new JPanel(new BorderLayout());
            painel.add(arCondicionado, BorderLayout.CENTER);
            JPanel painelEstado = new JPanel(new BorderLayout());
            painelEstado.add(estado, BorderLayout.NORTH);
            painelEstado.add(historicoScroll, BorderLayout.CENTER);
            painel.add(painelEstado, BorderLayout.SOUTH);

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
            historico.addElement("Ar condicionado em modo " + ordem.getModo()
                    + " - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            System.out.println(ordem);

            janela.setVisible(true);
    }
}
