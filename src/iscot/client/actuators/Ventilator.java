package iscot.client.actuators;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
		Areas area = args.length >= 3
				? Areas.valueOf(args[2])
				: args.length >= 1
				? Areas.valueOf(args[0])
				: Areas.LivingRoom;
			// TODO connect to server
			
			// TODO register with the server
			
			JFrame janela = new JFrame("Ventilador em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			VentilatorIcon ventilator = new VentilatorIcon();
			painel.add(ventilator,BorderLayout.CENTER);
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
			//ordem de evento para a fase 3
			final boolean[] ligado = {false};

			ventilator.setLigado(ligado[0]);

			Timer timer = new Timer(5000, e -> {
				ligado[0] = !ligado[0];

				AreaVentilationOrderEvent ordem = new AreaVentilationOrderEvent(area, ligado[0]);

				ventilator.setLigado(ordem.isLigado());

				historico.addElement("Ventilador " + (ordem.isLigado() ? "ligado" : "desligado")
						+ " - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				System.out.println(ordem);
			});
			timer.start();
	}
}
