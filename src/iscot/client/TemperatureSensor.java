package iscot.client;

import iscot.client.gui.SensorControl;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.sensor.TemperatureSensorEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TemperatureSensor {

    public static void main(String[] args) {
        final ClientArgs config;

        try {
            config = ClientArgs.parseOrExit(args, Type.SENSOR);
        } catch (IllegalArgumentException e) {
            return;
        }

        Areas area = config.getArea();

        final ServerConnection connection;
        try {
            connection = new ServerConnection(config);
            System.out.println("Sensor registado com ID: " + connection.getDeviceId());
        } catch (IOException e) {
            System.err.println("Erro ao ligar ou registar o servidor: " + e.getMessage());
            System.exit(1);
            return;
        }

        JFrame janela = new JFrame("Temperatura em " + area);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    connection.close();
                } catch (IOException ex) {
                    System.err.println("Erro ao fechar o servidor: " + ex.getMessage());
                }
            }
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.setLayout(new BorderLayout());
        SensorControl sensorControl= new SensorControl( "🌡", "Temperatura - "+area, "°C",
                16, 30, 1, 22,
                valor -> {
                    TemperatureSensorEvent evento = new TemperatureSensorEvent(area, valor);
                    try {
                        connection.sendEvent(evento);
                        System.out.println("Evento enviado: " + evento);
                    } catch (IOException ex) {
                        System.err.println("Erro ao fechar o servidor: " + ex.getMessage());

                        JOptionPane.showMessageDialog(janela, "A ligação ao servidor foi perdida", "Erro de ligação", JOptionPane.ERROR_MESSAGE);

                        try {
                            connection.close();
                        } catch (IOException closeError) {
                            System.err.println("Erro ao fechar o servidor: " + closeError.getMessage());
                        }
                        janela.dispose();
                    }
                });

        painel.add(sensorControl,BorderLayout.CENTER);
        janela.setContentPane(painel);
        janela.setLocation(600, 400);
        janela.setMinimumSize(janela.getSize());
        janela.pack();
        janela.setVisible(true);

    }
}
