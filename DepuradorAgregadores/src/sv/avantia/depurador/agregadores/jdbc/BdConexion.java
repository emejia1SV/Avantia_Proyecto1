package sv.avantia.depurador.agregadores.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BdConexion {

	private static Connection connection = null;
	private static ResourceBundle properties = ResourceBundle
			.getBundle("sv.avantia.depurador.agregadores.propiedades.parametrosSistema");

	/**
	 * Metodo que carga el driver especifico
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @return void
	 **/

	private static void loadDriver() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (Exception e) {
			errorHandler("Error al cargar el driver para la Base de Datos Oracle ", e);
		}
	}

	/**
	 * Metodo que carga la conexion desde unos parametros que estan dentro de un
	 * archivo de propiedades
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @return void
	 **/

	private static void loadConnection() {
		try {
			connection = DriverManager.getConnection(getFormatedUrl(),
					properties.getString("jdbc.usuario"),
					properties.getString("jdbc.contrasenia"));
		} catch (SQLException e) {
			errorHandler("Error al querer conectarse a la Base de Datos "
					+ getFormatedUrl(), e);
		}
	}

	/**
	 * Metodo que muestra en consola los errores que pudieran a sucitar dentro
	 * de esta clase singleton
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @param {String} Message
	 * @param {Exception} e
	 * @return void
	 **/

	private static void errorHandler(String message, Exception e) {
		System.out.println(message);
		if (e != null)
			System.out.println(e.getMessage());
	}

	/**
	 * Metodo que retorna el formato de la URL para la conexion a la base de
	 * datos
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @return {String}
	 **/

	private static String getFormatedUrl() {
		return properties.getString("jdbc.url")
				+ properties.getString("jdbc.ip") + ":"
				+ properties.getString("jdbc.puerto") + ":"
				+ properties.getString("jdbc.db");
	}

	/**
	 * Metodo que retorna la instanca de la clase singleton
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @return {Connection} connection
	 **/

	public static Connection getConnection() {
		if (connection == null) {
			loadDriver();
			loadConnection();
		}
		return connection;
	}

	/**
	 * Metodo para poder cerrar la conexion a la base de datos
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @return void
	 **/

	public static void closeConnection() {
		if (connection == null) {
			errorHandler("No hay ninguna conexion abierta en este momento para ser cerrada", null);
		} else {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				errorHandler("Error al tratar de cerrar la conexion a la Base de Datos", e);
			}
		}
	}
}