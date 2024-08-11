package nodoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Ranking {
	private File archivoRankings;
	private Map<Long, String> jugadoresPorTiempo;
	private List<Long> mejoresTiempos;
	
	public Ranking() {
		archivoRankings = new File("rankings.txt");
		
		boolean archivoCreado;
		try {
			archivoCreado = archivoRankings.createNewFile();
			jugadoresPorTiempo = new HashMap<Long, String>();
			mejoresTiempos = new LinkedList<Long>();
			if (!archivoCreado) {
				// Ya existe un archivo con los rankings.
				leerRankings();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void agregarJugador(Long tiempo, String nombre) {
		jugadoresPorTiempo.put(tiempo, nombre);
		mejoresTiempos.add(tiempo);
	}
	
	public void guardarRanking() {
		try {
			FileWriter fw = new FileWriter("rankings.txt");
			
			mejoresTiempos.sort(null);
			StringBuilder rankingAGuardar = new StringBuilder();
			for (int i=0; i<10 && i<mejoresTiempos.size(); i++) {
				// Solo guardamos los mejores 10 tiempos.
				Long tiempoDeCompletacion = mejoresTiempos.get(i);
				rankingAGuardar.append(tiempoDeCompletacion.toString() 
						+ "," + jugadoresPorTiempo.get(tiempoDeCompletacion) 
						+ "\n");
			}
			fw.write(rankingAGuardar.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public List<String[]> getTop10Ranking() {
		LinkedList<String[]> paresJugadorTiempo = new LinkedList<String[]>();
		
		mejoresTiempos.sort(null);
		for (int i=0; i<10 && i<mejoresTiempos.size(); i++) {
			Long tiempoDeCompletacion = mejoresTiempos.get(i);
			String[] par = {jugadoresPorTiempo.get(tiempoDeCompletacion),
								tiempoDeCompletacion.toString()};
			paresJugadorTiempo.add(par);
		}
		
		return paresJugadorTiempo;
	}

	private void leerRankings() throws IOException {
		/* Los rankings se guardan en un archivo donde cada línea sigue el 
		 * siguiente formato:
		 * <tiempo_de_completacion_en_milisegundos>,<nombre_jugador>*/
		Scanner s = new Scanner(archivoRankings);
		
		while (s.hasNextLine()) {
			String data = s.nextLine();
			Jugador j = new Jugador(data.split(","));
			agregarJugador(j.tiempoDeCompletación, j.nombre);
		}
		s.close();
	}
}

class Jugador {
	Long tiempoDeCompletación;
	String nombre;
	
	int TIEMPO = 0;
	int NOMBRE = 1;
	
	public Jugador(String[] tiempoYJugador) {
		tiempoDeCompletación = Long.parseLong(tiempoYJugador[TIEMPO]);
		nombre = tiempoYJugador[NOMBRE];
	}
}
