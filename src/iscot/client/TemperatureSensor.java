package iscot.client;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.events.HumiditySensorEvent;
import iscot.events.TemperatureSensorEvent;

import javax.swing.*;
import java.awt.*;

public class TemperatureSensor {

    public static void main(String[] args) {
        if(args.length != 3){
            System.err.println("Invalid arguments!");
            return;
        }

        Areas area = Areas.valueOf(args[2]);

        JFrame janela = new JFrame("Temperatura em " + area);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setLayout(new BorderLayout());
        SensorControl sensorControl= new SensorControl( "☀", "Temperatura - "+area, "°C",
                16, 30, 1, 22,
                valor -> {
                    TemperatureSensorEvent evento = new TemperatureSensorEvent(area, valor);
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
