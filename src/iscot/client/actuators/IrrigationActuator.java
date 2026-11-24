package iscot.client.actuators;

import iscot.client.ClientArgs;
import iscot.client.gui.IrrigationIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.order.AreaIrrigationOrderEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class IrrigationActuator {

    public static void main(String[] args) {
        final ClientArgs config;

        try {
            config = ClientArgs.parseOrExit(args, Type.ACTUATOR);
        } catch (IllegalArgumentException e) {
            return;
        }

        Areas area = config.getArea();

        JFrame janela = new JFrame("Rega em " + area);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        IrrigationIcon rega = new IrrigationIcon();
        JLabel estado = new JLabel("", SwingConstants.CENTER);
        
        DefaultListModel<String> historico = new DefaultListModel<>();
        JList<String> listaHistorico = new JList<>(historico);
        JScrollPane historicoScroll = new JScrollPane(listaHistorico);
        historicoScroll.setBorder(BorderFactory.createTitledBorder("Histórico"));

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(rega, BorderLayout.CENTER);
        
        JPanel painelEstado = new JPanel(new BorderLayout());
        painelEstado.add(estado, BorderLayout.NORTH);
        painelEstado.add(historicoScroll, BorderLayout.CENTER);
        painel.add(painelEstado, BorderLayout.SOUTH);

        janela.setContentPane(painel);
        janela.pack();
        janela.setLocationRelativeTo(null);

        // Ordem de exemplo: regar a zona 1 durante 1 minuto.
        AreaIrrigationOrderEvent ordem = new AreaIrrigationOrderEvent(area, 1, 1, true);

        rega.setZona(ordem.getZona());
        rega.setDuracao(ordem.getDuracao());
        rega.setAtiva(ordem.isActive());

        estado.setText("Zona " + ordem.getZona()
                        + " — rega ativa durante "
                        + ordem.getDuracao() + " min");
        historico.addElement("Rega ativa na zona " + ordem.getZona()
                        + " - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.println(ordem);

        janela.setVisible(true);

        // TODO: receive and handle messages from server
    }
}
