package interfaz;

import nodoku.Nodoku;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.Toolkit;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class InterfazNodoku {
	Nodoku juego;
	JFrame ventanaPrincipal;
	private JTextField casilleros[][];
	private Label sumasEsperadasPorFila[];
	private Label sumasEsperadasPorColumna[];
	private boolean filasResueltas[];
	private boolean columnasResueltas[];
	
	VentanaGanador ventanaGanador;
	RegistroRanking registroRanking;
	
	int ultimoTamanio;
	int ultimoAncho;
	int ultimoAlto;
	
	private final Color COLOR_CORRECTO = Color.green;
	private final Color COLOR_DEFAULT = Color.white;
	private final Color COLOR_TEXTO = Color.black;
	private final Color COLOR_TEXTO_CORRECTO = new Color(10,221,8);
	private final int TAMANIO_FACIL = 4;
	private final int TAMANIO_MEDIO = 6;
	private final int TAMANIO_DIFICIL = 8;
	int tamanioPersonalizado;
	private final int ALTO_VENTANA_FACIL = 310;
	private final int ANCHO_VENTANA_FACIL = 260;
	private final int ALTO_VENTANA_MEDIO = 410;
	private final int ANCHO_VENTANA_MEDIO = 360;
	private final int ALTO_VENTANA_DIFICIL = 510;
	private final int ANCHO_VENTANA_DIFICIL = 460;

	/**
		Lanza la aplicación
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					InterfazNodoku window = new InterfazNodoku();
					window.ventanaPrincipal.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
		Crea la aplicación
	 */
	public InterfazNodoku()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
		initialize();
	}

	/*
	 	Inicializar contenido de la ventana
	 */
	private void initialize()
	{	
		// Crea la ventana del juego	
		ventanaPrincipal = ventanaNueva();
		ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Acción que se ejecutará antes de cerrar la ventana.
		ventanaPrincipal.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        salir();
		    }
		});
		
		
		// Al arrancar por primera vez, lo hace en modo fácil ************	
		nuevoJuego(TAMANIO_FACIL, ANCHO_VENTANA_FACIL, ALTO_VENTANA_FACIL);
		
		// Crea barra menú ***********************************************
		JMenuBar barraMenu = new JMenuBar();
		ventanaPrincipal.setJMenuBar(barraMenu);
		
		// Crea  menú desplegable Nuevo **********************************
		barraMenu.add(crearMenuDesplegable());
	}
	
	void nuevoJuego(int tamanio, int ancho, int alto)
	{	
		limpiarVentana();
		
		ultimoTamanio = tamanio;
		ultimoAncho = ancho;
		ultimoAlto = alto;
		
		if (juego != null) {
			juego.nuevoJuego(tamanio);
		}
		else {
			juego = new Nodoku(tamanio);
		}
		
		filasResueltas = new boolean[tamanio];
		columnasResueltas = new boolean[tamanio];
		centrarVentana(ventanaPrincipal, ancho, alto);
		crearCasilleros(tamanio);
		mostrarValoresEsperados();
		
		if (ventanaGanador != null) {
			ventanaGanador.dispose();
		}
		if (registroRanking != null) {
			registroRanking.dispose();
		}
		
		registroRanking = new RegistroRanking(this);
		ventanaGanador = new VentanaGanador(this);
	}
	
	void nuevoJuego() 
	{
		nuevoJuego(ultimoTamanio, ultimoAncho, ultimoAlto);
	}
	
	private JMenu crearMenuDesplegable() {
		/* Crea un menu deplegable con opciones. */
		
		JMenu mnNuevo = new JMenu("Nuevo juego");
		
		crearBotonParaMenu(mnNuevo, "Fácil", 
				TAMANIO_FACIL, ANCHO_VENTANA_FACIL, ALTO_VENTANA_FACIL);
		crearBotonParaMenu(mnNuevo, "Medio",
				TAMANIO_MEDIO, ANCHO_VENTANA_MEDIO, ALTO_VENTANA_MEDIO);
		crearBotonParaMenu(mnNuevo, "Díficil",
				TAMANIO_DIFICIL, ANCHO_VENTANA_DIFICIL, ALTO_VENTANA_DIFICIL);
		
		crearMenuItemTamanioPersonalizado(mnNuevo);
		
		return mnNuevo;
	}
	
	private void crearBotonParaMenu(JMenu menu, String nombre, int tamanio, int ancho, int alto) {
		JMenuItem nuevoBotonMenu = new JMenuItem(nombre);
		
		nuevoBotonMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				nuevoJuego(tamanio, ancho, alto);
			}
		});
		
		menu.add(nuevoBotonMenu);
	}
	
	private void crearMenuItemTamanioPersonalizado(JMenu menu) {
		JMenuItem mnNuevoItemPersonalizado = new JMenuItem("Personalizado");
		
		InterfazNodoku interfaz = this;
		mnNuevoItemPersonalizado.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ventanaPrincipal.setVisible(false);
				new VentanaTamanioPersonalizado(interfaz);
			}
		});
		menu.add(mnNuevoItemPersonalizado);
	}
	
	void salir() {
		juego.guardarRanking();
		System.exit(0);
	}

	static void centrarVentana(JFrame ventana, int ancho, int alto)
	{
		int ancho_pantalla = getAnchoPantalla();
		int alto_pantalla  = getAltoPantalla();
		int x = (ancho_pantalla - ancho)/2;
		int y = (alto_pantalla - alto)/2;
		ventana.setBounds(x, y, ancho, alto);
	}
	
	private void crearCasilleros(int tamanio_grilla)
	{	
		try {
			MaskFormatter formato;
			formato = new MaskFormatter("#"); // sólo un dígito
			String numerosValidos = digitosValidos();
			formato.setValidCharacters(numerosValidos);	
			casilleros = new JFormattedTextField[tamanio_grilla][tamanio_grilla];
	
			for (int x = 0; x < tamanio_grilla; x++)
			{
				for (int y = 0; y < tamanio_grilla; y++)
				{
					casilleros[y][x] = crearCelda(x, y, formato);
					ventanaPrincipal.getContentPane().add(casilleros[y][x]);
				}
			}
		}
		catch (Exception e){}
	}
	
	private void cambiarValorGrilla(int valor, int x, int y) {
		juego.cambiarValorGrilla(valor, x, y);
		if (juego.filaEstaResuelta(y)) {
			filasResueltas[y] = true;
			setColorFila(y, true);
		} else {
			filasResueltas[y] = false;
			setColorFila(y, false);
		}
		if (juego.columnaEstaResuelta(x)) {
			columnasResueltas[x] = true;
			setColorColumna(x, true);
		} else {
			columnasResueltas[x] = false;
			setColorColumna(x, false);
		}
		
		if (juego.chequearJuegoResuelto())
		{
			manejarVictoria();
		}
	}
	
	private void manejarVictoria() {
		ventanaGanador.setTiempo(juego.getTiempoDeCompletacion());
		ventanaGanador.setVisible(true);
		ventanaPrincipal.setEnabled(false);
	}
	
	private void setColorFila(int y, boolean sumaCorrecta)
	{
		for (int x = 0; x < casilleros[y].length; x++)
		{
			if (sumaCorrecta) {
				casilleros[y][x].setBackground(COLOR_CORRECTO);
				sumasEsperadasPorFila[y].setForeground(COLOR_TEXTO_CORRECTO);
			} else {
				if (!columnasResueltas[x])
				{
					casilleros[y][x].setBackground(COLOR_DEFAULT);
					sumasEsperadasPorFila[y].setForeground(COLOR_TEXTO);
				}
			}
		}
	}
	
	private void setColorColumna(int x, boolean sumaCorrecta)
	{
	 	for (int y = 0; y < ultimoTamanio; y++)
		{
			if (sumaCorrecta)
			{
				casilleros[y][x].setBackground(COLOR_CORRECTO);
				sumasEsperadasPorColumna[x].setForeground(COLOR_TEXTO_CORRECTO);
			} else {
				if (!filasResueltas[y])
				{
					casilleros[y][x].setBackground(COLOR_DEFAULT);
					sumasEsperadasPorColumna[x].setForeground(COLOR_TEXTO);
				}
			}
		}
	}


	private JFormattedTextField crearCelda(int x, int y, MaskFormatter formato) {
		JFormattedTextField celda = new JFormattedTextField(formato);
		celda.setHorizontalAlignment(JTextField.CENTER);
		celda.setBounds(x*50, y*50, 47, 47);
		celda.setFont(new Font("Arial", Font.PLAIN, 17));
		agregarDocumentListener(celda, x, y);
		
		return celda;
	}
	
	private void agregarDocumentListener(JTextField celda, int x, int y) {
		celda.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				cambiarValorGrilla(0, x, y);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				// Llamado cada vez que el contenido de una celda cambia.
				if (celda.getText().equals(" ")) {
					cambiarValorGrilla(0, x, y);
				}
				else {
					int valor = Integer.parseInt(celda.getText());
					cambiarValorGrilla(valor, x, y);
				}
			}
		});
	}
	
	public void mostrarValoresEsperados()
	{
		int columnas[] = juego.getSumasEsperadasPorColumna();
		int filas[] = juego.getSumasEsperadasPorFila();
		sumasEsperadasPorColumna = new Label[ultimoTamanio];
		sumasEsperadasPorFila = new Label[ultimoTamanio];
		
		for (int i = 0; i < ultimoTamanio; i ++)
		{
			sumasEsperadasPorFila[i] =
			agregarSumaEsperada(Integer.toString(filas[i]),	ultimoTamanio * 50, i * 50);
			sumasEsperadasPorColumna[i] = 
			agregarSumaEsperada(Integer.toString(columnas[i]), i * 50, ultimoTamanio * 50);
		}
	}
	
	private Label agregarSumaEsperada(String valor, int x, int y)
	{	
		Label nuevaSuma = new Label(valor);
		nuevaSuma.setAlignment(1);
		nuevaSuma.setFont(new Font("Arial", Font.PLAIN, 17));
		nuevaSuma.setBounds(x, y, 50, 50);
		ventanaPrincipal.getContentPane().add(nuevaSuma);	
		return nuevaSuma;
	}
	
	private JFrame ventanaNueva()
	{
		JFrame nueva = new JFrame();
		nueva.getContentPane().setLayout(null);
		nueva.setResizable(false); // cambio de tamaño no permitido
		return nueva;
	}
	
	private String digitosValidos()
	{
		StringBuilder cadena = new StringBuilder();
		for (int i = 1; i <= juego.getValorMaximoCelda(); i++)
		{
			cadena.append(i);
		}
		return cadena.toString();
	}
	
	private void limpiarVentana() // Borra pantalla del juego anterior
	{
		if (casilleros != null) {			
			borrarCasilleros();
			borrarConsigna();
		}
	}
	
	private void borrarCasilleros()
	{
		for (int x = 0; x < ultimoTamanio; x++)
		{
			for (int y = 0; y < ultimoTamanio; y++)
			{
				ventanaPrincipal.getContentPane().remove(casilleros[y][x]);
			}
		}	
	}
	
	private void borrarConsigna()
	{
		for (int i = 0; i < ultimoTamanio; i ++)
		{
			ventanaPrincipal.getContentPane().remove(sumasEsperadasPorColumna[i]);
			ventanaPrincipal.getContentPane().remove(sumasEsperadasPorFila[i]);
		}		
	}
	
	static int getAnchoPantalla()
	{
		// Lee el ancho de la resolución de la pantalla del dispositivo
		Dimension resolucionPantalla = Toolkit.getDefaultToolkit().getScreenSize();
		return resolucionPantalla.width;
	}	
	
	static int getAltoPantalla()
	{
		// Lee el ancho de la resolución de la pantalla del dispositivo
		Dimension resolucionPantalla = Toolkit.getDefaultToolkit().getScreenSize();
		return resolucionPantalla.height;
	}	
}
