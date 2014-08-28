package sv.avantia.depurador.agregadores.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BdEjecucion {

	/**
	 * Consultar a la Base de Datos
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * 
	 * @param query
	 * */
	public ResultSet consultar(String query) throws SQLException {
		if(query == null)
			throw new SQLException("La sentencia SQL de ejecución no puede estar vacía ni ser nula");
		
		Statement stmt = null;
		try {
			stmt = BdConexion.getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			return stmt.executeQuery(query);
		} catch (SQLException e) {
			throw e;
		}
	}
}