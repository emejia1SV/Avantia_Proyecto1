package sv.avantia.depurador.agregadores.generarArchivos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;

public class CrearArchivoTxt {

	/**
	 * metodo que generara el texto que se le escribira dentro del archivo
	 * 
	 * @author Emejia - Avantia consultores
	 * @throws Exception
	 * */
/*	private void generarTxt() throws Exception {
		StringBuilder buffer = new StringBuilder();
		String telefono = "00000000";
		String telefonoI = "00000000";
		for (int i = 0; i < 1000000; i++) {
			telefono = telefono+i;
			buffer.append(telefono.substring(telefono.length()-8, telefono.length()));
			buffer.append("\n");
			telefono= telefonoI;
		}
		
		createTXT(buffer);
	}*/

	/**
	 * metodo que me grava el archivo en la ubicacion indicada 
	 * 
	 * @author Emejia - Avantia consultores
	 * @param {String} url 
	 * @param {StringBuilder} buffer - Es el texto que se escribira dentro del archivo
	 * @throws Exception
	 * */
	public void createTXT(String url, StringBuilder buffer) throws Exception {
		try {
			Calendar cal = Calendar.getInstance();
			
			String urlCompleta = url + cal.get(Calendar.DAY_OF_MONTH) + cal.get(Calendar.MONTH) + cal.get(Calendar.YEAR) +".txt";
			BufferedWriter out = new BufferedWriter(new FileWriter(urlCompleta));
			out.write(buffer.toString());
			out.close();
		} catch (Exception e) {
			System.err.println("Error al crear el archivo txt");
		}
	}
}
