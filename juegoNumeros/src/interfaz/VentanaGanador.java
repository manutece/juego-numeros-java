package interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class VentanaGanador extends JFrame {
	private static final long serialVersionUID = 4351125225212137569L;
	private JPanel contentPane;
	private JLabel tiempoLabel;
	private JTable tablaRanking;
	private DefaultTableModel modeloRanking;
	private InterfazNodoku interfaz;
	private boolean registrado;

	private final int NOMBRE_JUGADOR = 0;
	private final int TIEMPO_MS = 1;
	
	/**
	 * Create the frame.
	 */
	public VentanaGanador(InterfazNodoku interfaz) {		
		inicializarVentana(interfaz);
		
		crearLabelGanaste();
		
		crearLabelsTiempo();
		
		crearTablaRanking();
		crearLabelRanking();
		
		crearBotonNuevoJuego(interfaz);
		crearBotonTerminarJuego(interfaz);
		crearBotonRegistrarseAlRanking(interfaz);
		
		this.setVisible(false);
		this.setAlwaysOnTop(true);
	}

	private void inicializarVentana(InterfazNodoku interfaz) {
		this.interfaz = interfaz;
		setResizable(false);
		registrado = false;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		agregarAccionAlCerrarVentana();
		setBounds(centroHorizontalSegunAncho(450) , 
				centroVerticalSegunAlto(400) , 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
	}

	private void agregarAccionAlCerrarVentana() {
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        nuevoJuego();
		    }
		});
	}

	private void crearLabelGanaste() {
		JLabel ganasteLabel = new JLabel("GANASTE!");
		ganasteLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		ganasteLabel.setBounds(134, 11, 147, 46);
		contentPane.add(ganasteLabel);
	}

	private void crearLabelsTiempo() {
		JLabel tuTiempoLabel = new JLabel("Tu tiempo:");
		tuTiempoLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tuTiempoLabel.setBounds(10, 69, 79, 20);
		contentPane.add(tuTiempoLabel);
		
		tiempoLabel = new JLabel("00:00:00");
		tiempoLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tiempoLabel.setBounds(85, 69, 58, 20);
		contentPane.add(tiempoLabel);
	}

	private void crearTablaRanking() {
		JScrollPane scrollRanking = new JScrollPane();
		scrollRanking.setBounds(10, 105, 400, 186);
		contentPane.add(scrollRanking);
		
		inicializarTablaRanking();
		
		scrollRanking.setViewportView(tablaRanking);
	}

	private void crearLabelRanking() {
		JLabel rankingLabel = new JLabel("Ranking");
		rankingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rankingLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rankingLabel.setBounds(163, 81, 74, 19);
		contentPane.add(rankingLabel);
	}

	private void crearBotonNuevoJuego(InterfazNodoku interfaz) {
		JButton btnNuevoJuego = new JButton("Nuevo Juego");
		btnNuevoJuego.setBounds(85, 300, 114, 23);
		btnNuevoJuego.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nuevoJuego();
			}

		});
		contentPane.add(btnNuevoJuego);
	}
	
	private void nuevoJuego() {
		interfaz.ventanaPrincipal.setEnabled(true);
		interfaz.nuevoJuego();
	}

	private void crearBotonTerminarJuego(InterfazNodoku interfaz) {
		JButton btnTerminarJuego = new JButton("Terminar Juego");
		btnTerminarJuego.setBounds(209, 300, 114, 23);
		btnTerminarJuego.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				interfaz.salir();
			}
		});
		contentPane.add(btnTerminarJuego);
	}

	private void crearBotonRegistrarseAlRanking(InterfazNodoku interfaz) {
		JButton btnRegistrarseAlRanking = new JButton("Registrarse al Ranking");
		btnRegistrarseAlRanking.setBounds(263, 69, 147, 23);
		btnRegistrarseAlRanking.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!registrado) {					
					interfaz.ventanaGanador.setEnabled(false);
					interfaz.registroRanking.setVisible(true);
				}
			}
		});
		contentPane.add(btnRegistrarseAlRanking);
	}

	int centroHorizontalSegunAncho(int ancho)
	{
		return (InterfazNodoku.getAnchoPantalla() - ancho) / 2;
	}
	
	int centroVerticalSegunAlto(int alto)
	{
		return (InterfazNodoku.getAltoPantalla() - alto) / 2;
	}

	public void setTiempo(Long ms) {
		tiempoLabel.setText(milisegundosAhms(ms));
	}

	public void registroExitoso() {
		registrado = true;
	}
	
	private void inicializarTablaRanking() {
		tablaRanking = new JTable();
		tablaRanking.setBounds(10, 105, 400, 120);
		
		modeloRanking = new DefaultTableModel();
		modeloRanking.addColumn("Jugador");
		modeloRanking.addColumn("Tiempo");
		
		int posicion = 1;
		for (String[] parJugadorTiempo : interfaz.juego.getTop10Ranking()) {
			modeloRanking.addRow(new String[] 
					{String.format("%dº. %s", posicion, parJugadorTiempo[NOMBRE_JUGADOR]), 
							milisegundosAhms(Long.parseLong(parJugadorTiempo[TIEMPO_MS]))});
			posicion++;
		}
		
		tablaRanking.setModel(modeloRanking);
	}
	
	private static String milisegundosAhms(Long ms) {
		Long segundos = ms / 1000;
		Long minutos = segundos / 60; segundos = segundos % 60;
		Long horas = minutos / 60; minutos = minutos % 60;
		
		return String.format("%02d:%02d:%02d", horas, minutos, segundos);
	}
}
