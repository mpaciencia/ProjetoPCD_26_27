package iscot.client;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.GroundMoistureSensorEvent;
import iscot.events.RegistrationEvent;

import javax.swing.*;
import java.awt.*;

public class GroundMoistureSensor {

    public static void main(String[] args) {
        if(args.length != 3) {
            System.err.println("Invalid arguments!");
            return;
        }

        Areas area = Areas.valueOf(args[2]);

        // Nesta fase o registo é apenas representado localmente.
        RegistrationEvent registo = new RegistrationEvent(area, Type.SENSOR);
        System.out.println(registo);

        JFrame janela = new JFrame("Humidade do Solo em " + area);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setLayout(new BorderLayout());
        SensorControl sensorControl= new SensorControl( "☀", "Humidade do Solo - "+area, "%",
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
