package nodoku;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class Nodoku {
	private int anchoGrilla;
	private int largoGrilla;
	private int valorMaximoCelda;
	private int[] sumasEsperadasPorFila;
	private int[] sumasEsperadasPorColumna;
	private int[][] grilla;
	
	private long tiempoDeInicio;
	private long tiempoDeCompletacion;
	private Ranking ranking;
	
	private final int VALOR_MAXIMO_DEFECTO = 4;
	
	public Nodoku(int tamanio) {
		/* Crea un tablero de Nodoku cuadrado de tamanio x tamanio. */
		nuevoJuego(tamanio);
		
		ranking = new Ranking();
	}
	
	public void nuevoJuego(int tamanio) {
		inicializarValores(tamanio);
		generarJuego();
	}

	private void inicializarValores(int tamanio) {
		anchoGrilla = tamanio;
		largoGrilla = tamanio;
		valorMaximoCelda = VALOR_MAXIMO_DEFECTO;
		grilla = new int[tamanio][tamanio];
		
		Date tiempo = new Date();
		tiempoDeInicio = tiempo.getTime();
	}
	
	private void generarJuego()
	{
		/* Genera las listas de sumas esperadas de modo que el juego tenga solución. */
		Random rand = new Random();
		
		sumasEsperadasPorColumna = new int[anchoGrilla];
		sumasEsperadasPorFila = new int[largoGrilla];
		
		for(int y=0; y<anchoGrilla; y++) {
			for(int x=0; x<largoGrilla; x++) {
				int numeroCelda = rand.nextInt(1, valorMaximoCelda + 1);
				
				sumasEsperadasPorColumna[x] += numeroCelda;
				sumasEsperadasPorFila[y] += numeroCelda;
			}
		}
	}

	public void cambiarValorGrilla(int valor, int x, int y) {
		grilla[y][x] = valor;
	}
	
	public int getValorMaximoCelda()
	{
		return valorMaximoCelda;
	}
	
	public int[] getSumasEsperadasPorFila() {
		return sumasEsperadasPorFila;
	}

	public int[] getSumasEsperadasPorColumna() {
		return sumasEsperadasPorColumna;
	}
	
	public boolean filaEstaResuelta(int y) {
		int suma = 0;
		for (int x=0; x<grilla[0].length; x++) {
			int valor = grilla[y][x];
			if (valor==0) {
				return false;
			}
			suma += valor;
		}
		return suma == sumasEsperadasPorFila[y];
	}
	
	public boolean columnaEstaResuelta(int x) {
		int suma = 0;
		for (int y=0; y<grilla.length; y++) {
			int valor = grilla[y][x];
			if (valor==0) {
				return false;
			}
			suma += valor;
		}
		return suma == sumasEsperadasPorColumna[x];
	}
	
	public void agregarAlRanking(String nombre) {
		ranking.agregarJugador(tiempoDeCompletacion, nombre);
	}
	
	public void guardarRanking() {
		// Llamado cuando se cierra la aplicación para guardar el ranking.
		ranking.guardarRanking();
	}
	
	public boolean chequearJuegoResuelto()
	{
		boolean juegoResuelto = true;
		for (int x = 0; x < anchoGrilla; x++)
		{
			juegoResuelto = juegoResuelto && 
			columnaEstaResuelta(x);
		}
		for (int y = 0; y < largoGrilla; y++)
		{
			juegoResuelto = juegoResuelto && 
			filaEstaResuelta(y);
		}
		
		if (juegoResuelto) {
			manejarJuegoResuelto();
		}
		
		return juegoResuelto;
	}
	
	private void manejarJuegoResuelto() {
		Date tiempo = new Date();
		tiempoDeCompletacion = tiempo.getTime() - tiempoDeInicio;
	}
	
	public List<String[]> getTop10Ranking(){
		return ranking.getTop10Ranking();
	}
	
	public Long getTiempoDeCompletacion() {
		return tiempoDeCompletacion;
	}
}

