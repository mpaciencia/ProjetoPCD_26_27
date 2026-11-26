package iscot.client.actuators;

import iscot.client.ClientArgs;
import iscot.client.ServerConnection;
import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.order.AreaLightingOrderEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LightActuator {

    public static void main(String[] args) {
        final ClientArgs config;

        try {
            config = ClientArgs.parseOrExit(args, Type.ACTUATOR);
        } catch (IllegalArgumentException e) {
            return;
        }

        Areas area = config.getArea();

        final ServerConnection connection;

        try {
            connection = new ServerConnection(config);
            System.out.println("Luz registado com ID: " + connection.getDeviceId());
        } catch (IOException e) {
            System.err.println("Error ao ligar ou registar o servidor: " + e.getMessage());
            System.exit(1);
            return;
        }

        JFrame janela = new JFrame("Luz em " + area);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    connection.close();
                } catch (IOException ex) {
                    System.err.println("Erro ao fechar a ligação: " + ex.getMessage());
                }
            }
        });

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
        AreaLightingOrderEvent ordem = new AreaLightingOrderEvent(area, true, 80);

        lampada.setIntensidade(ordem.getIntensidade());
        lampada.setLigada(ordem.isLigada());

        estado.setText("Estado: ligada — intensidade "
                        + ordem.getIntensidade() + "%");
        historico.addElement("Lâmpada ligada - "
                        + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.println(ordem);

        janela.setVisible(true);

        // TODO: receive and handle messages from server
    }
}
