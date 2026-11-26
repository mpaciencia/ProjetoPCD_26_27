package iscot.client;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.sensor.HumiditySensorEvent;


public class HumiditySensor {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		ClientArgs config;

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
			System.err.println("Erro ao ligar ou registar o sensor: " + e.getMessage());
			System.exit(1);
			return;
		}
			
			
		JFrame janela = new JFrame("Humidade em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		janela.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
                    connection.close();
                } catch (IOException ex) {
					System.err.println("Erro ao fechar ligação: " + ex.getMessage());
				}
			}
		});

		// Criar os componentes gráficos
		JPanel painel = new JPanel();
		painel.setLayout(new BorderLayout());
		SensorControl sensorControl = new SensorControl("💧", "Humidade - " + area, "%",
				0, 100, 1, 50,
				valor -> {
					HumiditySensorEvent evento = new HumiditySensorEvent(area, valor);
					try {
                        connection.sendEvent(evento);
						System.out.println("Evento enviado: " + evento);
                    } catch (IOException ex) {
						System.err.println("Erro ao enviar: " + ex.getMessage());

						JOptionPane.showMessageDialog(janela, "A ligação ao servidor foi perdida", "erro de ligação", JOptionPane.ERROR_MESSAGE);

						try {
                            connection.close();
                        } catch (IOException closeError) {
							System.err.println("Erro ao fechar ligação: " +  closeError.getMessage());
						}
						janela.dispose();
					}
				});
		painel.add(sensorControl, BorderLayout.CENTER);
		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.setMinimumSize(janela.getSize());
		janela.pack();
		janela.setVisible(true);
			
		
	}

}
