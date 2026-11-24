package iscot.client;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.RegistrationEvent;
import iscot.events.sensor.GroundMoistureSensorEvent;

import javax.swing.*;
import java.awt.*;

public class GroundMoistureSensor {

    public static void main(String[] args) {
        ClientArgs config;

        try {
            config = ClientArgs.parseOrExit(args, Type.SENSOR);
        } catch (IllegalArgumentException e) {
            return;
        }

        Areas area = config.getArea();
        // TODO connect to the server

		// TODO register with the server


        JFrame janela = new JFrame("Humidade do Solo em " + area);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setLayout(new BorderLayout());
        SensorControl sensorControl= new SensorControl( "🌱", "Humidade do Solo - "+area, "%",
                0, 100, 1, 50,
                valor -> {
                    GroundMoistureSensorEvent evento = new GroundMoistureSensorEvent(area, valor);
                    System.out.println(evento);
                });
        painel.add(sensorControl,BorderLayout.CENTER);
        janela.setContentPane(painel);
        janela.setLocation(600, 400);
        janela.setMinimumSize(janela.getSize());
        janela.pack();
        janela.setVisible(true);
    }
}
