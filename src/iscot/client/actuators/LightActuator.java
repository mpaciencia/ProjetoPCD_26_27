package iscot.client.actuators;

import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.events.order.AreaLightingOrderEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LightActuator {

    public static void main(String[] args) {
        Areas area = args.length >= 3
                ? Areas.valueOf(args[2])
                : args.length >= 1
                ? Areas.valueOf(args[0])
                : Areas.LivingRoom;

            JFrame janela = new JFrame("Luz em " + area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            LightIcon lampada = new LightIcon();
            JLabel estado = new JLabel("Estado: desligada", SwingConstants.CENTER);
            DefaultListModel<String> historico = new DefaultListModel<>();
            JList<String> listaHistorico = new JList<>(historico);
            JScrollPane historicoScroll = new JScrollPane(listaHistorico);
            historicoScroll.setBorder(BorderFactory.createTitledBorder("Histórico"));

            JPanel painel = new JPanel(new BorderLayout());
            painel.add(lampada, BorderLayout.CENTER);
            JPanel painelEstado = new JPanel(new BorderLayout());
            painelEstado.add(estado, BorderLayout.NORTH);
            painelEstado.add(historicoScroll, BorderLayout.CENTER);
            painel.add(painelEstado, BorderLayout.SOUTH);

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
            historico.addElement("Lâmpada ligada - "
                    + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            System.out.println(ordem);

            janela.setVisible(true);
    }
}
