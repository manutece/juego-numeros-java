package interfaz;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class VentanaTamanioPersonalizado extends JFrame {
	
	private static final long serialVersionUID = 4351125225212137569L;
	private int anchoVentana;
	private JSlider deslizable;
	private InterfazNodoku interfaz;	
	private final int MIN_CELDAS = 2;
	private final int MAX_CELDAS = 12;
	
	public VentanaTamanioPersonalizado(InterfazNodoku interfaz) {
		super();
		this.interfaz = interfaz;
		
		inicializarVentana();
		
		crearMensajeTamanioCuadricula();
		crearDeslizable();
		
		crearBotonAceptar();
	}
	
	private void inicializarVentana() {
		getContentPane().setLayout(null);
		setResizable(false); // cambio de tamaño no permitido
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		anchoVentana = 70 + (32 * (MAX_CELDAS - MIN_CELDAS));
		InterfazNodoku.centrarVentana(this, anchoVentana, 190);
		
		setVisible(true);
	}
	
	private void crearMensajeTamanioCuadricula() {
		JLabel mensaje = new JLabel();
		mensaje.setText("Tamaño de la cuadrícula:");
		mensaje.setFont(new Font("Arial", Font.PLAIN, 13));
		mensaje.setBounds((anchoVentana - 150) / 2, 15, 150, 25);
		getContentPane().add(mensaje);
	}
	
	private void crearDeslizable() {
		Dictionary<Integer, JLabel> tablaDeLabels = new Hashtable<>();
		
		for (int i = MIN_CELDAS; i <= MAX_CELDAS; i++)
		{
			tablaDeLabels.put(i, new JLabel(String.valueOf(i)));
		}
		
		deslizable = new JSlider(MIN_CELDAS, MAX_CELDAS);
		deslizable.setBounds(35, 40, 30 * (MAX_CELDAS - MIN_CELDAS), 50);
		deslizable.setMajorTickSpacing(1);
		deslizable.setPaintTicks(true);
		deslizable.setLabelTable(tablaDeLabels);
		deslizable.setPaintLabels(true);
		deslizable.setSnapToTicks(true);
		deslizable.setValue(interfaz.ultimoTamanio);
		getContentPane().add(deslizable);
		deslizable.setVisible(true);		
	}
	
	private void crearBotonAceptar() {
		JButton aceptar = new JButton();
		aceptar.setBounds((anchoVentana - 105) / 2, 105, 80, 30);
		aceptar.setText("Aceptar");
		this.add(aceptar);
		aceptar.setVisible(true);
		
		aceptar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	
				interfaz.tamanioPersonalizado = deslizable.getValue();
				interfaz.nuevoJuego(interfaz.tamanioPersonalizado, 
						interfaz.tamanioPersonalizado * 50 + 60,
						interfaz.tamanioPersonalizado * 50 + 110);
				interfaz.ventanaPrincipal.setVisible(true);
				dispose();
			}
		});
	}
}
