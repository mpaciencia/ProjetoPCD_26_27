package iscot.client.actuators;

import iscot.client.ClientArgs;
import iscot.client.ServerConnection;
import iscot.client.gui.ACIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.order.AreaTemperatureOrderEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ACActuator {

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
            System.out.println("AC registado com ID: " + connection.getDeviceId());
        } catch (IOException e) {
            System.err.println("Error ao ligar ou registar o servidor: " + e.getMessage());
            System.exit(1);
            return;
        }

        JFrame janela = new JFrame("Ar condicionado em " + area);
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
        AreaTemperatureOrderEvent ordem = new AreaTemperatureOrderEvent(
                        area, AirConditioningMode.Frio, 22);

        arCondicionado.setTemperatura(ordem.getTemperatura());
        arCondicionado.setModo(ordem.getModo());

        estado.setText("Modo: " + ordem.getModo()
                        + " — " + ordem.getTemperatura() + " °C");
        historico.addElement("Ar condicionado em modo " + ordem.getModo()
                        + " - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.println(ordem);

        janela.setVisible(true);

        // TODO: receive and handle messages from server
    }
}
