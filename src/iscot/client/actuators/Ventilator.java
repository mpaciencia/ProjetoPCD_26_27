package iscot.client.actuators;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.Timer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import iscot.client.ClientArgs;
import iscot.client.ServerConnection;
import iscot.client.gui.LightIcon;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.order.AreaVentilationOrderEvent;

public class Ventilator {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
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
			System.out.println("Ventilador registado com ID: " + connection.getDeviceId());
		} catch (IOException e) {
			System.err.println("Error ao ligar ou registar o servidor: " + e.getMessage());
			System.exit(1);
			return;
		}

		JFrame janela = new JFrame("Ventilador em " + area);
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

		// Criar os componentes gráficos
		JPanel painel = new JPanel();
		painel.setLayout(new BorderLayout());
		VentilatorIcon ventilator = new VentilatorIcon();
		painel.add(ventilator, BorderLayout.CENTER);
		DefaultListModel<String> historico = new DefaultListModel<>();
		JList<String> listaHistorico = new JList<>(historico);
		JScrollPane historicoScroll = new JScrollPane(listaHistorico);
		historicoScroll.setBorder(BorderFactory.createTitledBorder("Histórico"));
		painel.add(historicoScroll, BorderLayout.SOUTH);
		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.setMinimumSize(janela.getSize());
		janela.pack();
		janela.setVisible(true);

		// TODO: receive and handle messages from server
	}
}
