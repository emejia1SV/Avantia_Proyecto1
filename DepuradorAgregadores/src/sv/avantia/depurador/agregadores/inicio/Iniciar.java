package sv.avantia.depurador.agregadores.inicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import sv.avantia.depurador.agregadores.generarArchivos.ConexionSFTP;
import sv.avantia.depurador.agregadores.generarArchivos.CrearArchivoTxt;
import sv.avantia.depurador.agregadores.hilo.ConsultaAgregadorPorHilo;
import sv.avantia.depurador.agregadores.jdbc.BdEjecucion;
import sv.avantia.depurador.agregadores.objetos.Agregadores;
import sv.avantia.depurador.agregadores.objetos.Metodos;
import sv.avantia.depurador.agregadores.objetos.Parametros;
import sv.avantia.depurador.agregadores.objetos.Servicios;
import sv.avantia.depurador.agregadores.utileria.Log4jInit;
import sv.avantia.depurador.agregadores.ws.cliente.Cliente;

import com.cladonia.xml.webservice.wsdl.OperationInfo;
import com.cladonia.xml.webservice.wsdl.ServiceInfo;
import com.cladonia.xml.webservice.wsdl.WSDLException;

/**
 * Clase que inicializara el flujo a seguir para la depuracion de los
 * Agregadores de servicios moviles
 * 
 * @author Edwin Mejia - Avantia Consultores
 * 
 * */
public class Iniciar {

	/* Get actual class name to be printed on */
	public static Logger logger = Logger.getLogger("avantiaLogger");
	
	static{
		Log4jInit.init();
	}
	
	/**
	 * Carga de archivo de propiedades
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * */
	private static ResourceBundle properties = ResourceBundle.getBundle("sv.avantia.depurador.agregadores.propiedades.parametrosSistema");

	/**
	 * Metodo que inicializara todo el flujo del JAR ejecutable
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			flujoEjecutado();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo en el se inicia la ejecucion de la logica del negocio para este
	 * sistema de depurador de agregadores
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @return void
	 * */
	private static void flujoEjecutado(){
		long 	  					init 			= System.currentTimeMillis();
		ResultSet 					rset 			= null;
		Integer   					n1   			= Integer.valueOf(properties.getString("numero.intervaloInicial"));
		Integer   					n2   			= Integer.valueOf(properties.getString("numero.intervaloFinal"));
		Calendar  					cal  			= Calendar.getInstance();
		StringBuilder 				texto			= new StringBuilder();
		BdEjecucion 				ejecuciones 	= new BdEjecucion();
		CrearArchivoTxt 			archivoTXT 		= new CrearArchivoTxt();
 		
		try {			
			while (true) {
				rset = ejecuciones.consultar(properties.getString("query.numeros")
						.replaceAll("n1", n1.toString())
						.replaceAll("n2", n2.toString()));

				// si ya no hay mas numeros terminamos
				if (!existenMasNumeros(rset))
					break;

				rset.close();

				n1 = n1	+ Integer.valueOf(properties.getString("numero.intervaloFinal"));
				n2 = n2	+ Integer.valueOf(properties.getString("numero.intervaloFinal"));
			}
		} catch (SQLException e) {
			System.out.println("Error procesando los numeros");
			System.err.println(e.getMessage());
		} catch (WSDLException e) {
			System.out.println("Error procesando los wsdl");
			System.err.println(e.getMessage());
		}

		texto.append(cal.getTime());
		texto.append("\n");
		texto.append(System.currentTimeMillis() - init);
		texto.append(" Segundos");
		try {
			archivoTXT.createTXT(properties.getString("file.url"), texto);
		} catch (Exception e1) {
			System.err.println("error al crear el archivo de texto");
		}
		
		try {
			ConexionSFTP sftp = new ConexionSFTP();
			sftp.moveFileToDir(null, "");
		} catch (Exception e) {
			// TODO: handle exception
		}

		n1 = null;
		n2 = null;
		texto = null;
		archivoTXT = null;
		ejecuciones = null;

		System.out.println("finish");
	}

	/**
	 * Verificamos si existen mas numeros para seguir iterando
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @param rset
	 * @throws SQLException
	 * @throws WSDLException 
	 * */
	private static boolean existenMasNumeros(ResultSet rset) throws SQLException, WSDLException {
		if (rset != null) {
			if (rset.last()) {
				if (rset.getRow() > 0) {
					consultaAgregadores();
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}	

	/**
	 * Cada vez que se obtiene una serie de números de telefono se debe iterar
	 * la parametrización para generar los clientes
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @return void
	 * @throws SQLException 
	 * @throws WSDLException 
	 * */
	private static void consultaAgregadores() throws SQLException, WSDLException {
		try {
			ResultSet[] insumos= obtenerInsumos();
			for (Agregadores agregador : obtenerAgregadores()) {
				if(agregador.getNombre_agregador().equals("SMT")){
					System.out.println("sera de agregar el archivo TXT a traves del sftp");
				}else{
					ConsultaAgregadorPorHilo hilo = new ConsultaAgregadorPorHilo();
					hilo.setInsumos(insumos);
					hilo.setParametrizacion(agregador);
					hilo.start();
				}
			}
		} catch (SQLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de SQL");
		} catch (WSDLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de wsdl");
		} catch (Exception e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas general");
		}
		
	}
	
	public static ResultSet[] obtenerInsumos(){
		try {
			BdEjecucion 	ejecucion  = new BdEjecucion();
			ResultSet 		resultados = ejecucion.consultar("SELECT QUERY FROM AGR_INSUMOS");
			if(resultados != null){
				String[] querys = new String[tamanioRset(resultados)];
				for (int i = 0; resultados.next(); i++) {
					querys[i] = resultados.getString("QUERY");
				}
				return procesarQuerys(querys);
			}			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	private static ResultSet[] procesarQuerys(String[] querys) throws SQLException{
		ResultSet[] 			resultados = new ResultSet[querys.length];
		BdEjecucion 			ejecucion  = new BdEjecucion();
		Map<Integer, Integer> 	tamanios   = new HashMap<Integer, Integer>();//identificador, tamaño
		if (querys.length > 0) {
			for (int i = 0; i < querys.length; i++) {
				resultados[i] = ejecucion.consultar(querys[i]);
				tamanios.put(i, tamanioRset(resultados[i]));
			}
		}
		ejecucion = null;
		
		ResultSet[] rsets = new ResultSet[resultados.length];
		Map<Integer, Integer> sorted = sortByValues(tamanios);
		Iterator<Map.Entry<Integer, Integer>> iter = sorted.entrySet().iterator();
		int indice = 0;
		while(iter.hasNext()){
			Map.Entry<Integer, Integer> entry = iter.next();
			rsets[indice] = resultados[entry.getKey()];
			indice++;
		}
		
		tamanios 	= null;
		querys 		= null;
		resultados	= null;
		sorted		= null;
		iter		= null;
		return rsets;
	}
	
	private static int tamanioRset(ResultSet rset) throws SQLException {
		try {
			if (rset != null) {
				if (rset.last()) {
					return rset.getRow();
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new SQLException("No se puede obtener el tamaño del Resulset");
		}finally{
			rset.beforeFirst();
		}
		
	}
	
	/*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    @SuppressWarnings("rawtypes")
	private static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

			@SuppressWarnings("unchecked")
			@Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
	
	/**
	 * Obtener el Listado de Agregadores de servicios estos son extraidos desde
	 * la parametrizacion en la base de datos.
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @return {java.util.List} Listado de Agregadores obtenidos desde la Base
	 *         de Datos
	 * */
	private static List<Agregadores> obtenerAgregadores() throws SQLException, WSDLException{
		try {
			List<Agregadores> 			agregadores		= new ArrayList<Agregadores>();
			ResultSet 					rset 			= null;
			BdEjecucion ejecucion= new BdEjecucion();
			rset = ejecucion.consultar("select * from AGR_AGREGADORES");
			while(rset.next()){
				Agregadores agregador = new Agregadores();
				agregador.setEstado(rset.getInt("ESTADO"));
				agregador.setId(rset.getInt("ID"));
				agregador.setIdPais(rset.getInt("ID_PAIS"));
				agregador.setNombre_agregador(rset.getString("NOMBRE_AGREGADOR"));
				agregador.setServicios(obtenerSevicios(rset.getInt("ID")));
				agregadores.add(agregador);
			}
			rset.close();
			rset = null;
			return agregadores;
		} catch (SQLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de SQL obtenerAgregadores");
		} catch (WSDLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de wsdl obtenerAgregadores");
		} catch (Exception e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas general obtenerAgregadores");
		}
		
		
	}
	
	/**
	 * Metodo que obtendra el listado de servicios publicados en un agregador y
	 * estos servicios seran obtenidos a traves del id del agregador que se
	 * quiere ejecutar.
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @param id
	 *            {int} identificador del agregador de servicios
	 * @return {java.util.List} listado de servicios parametrizados en un
	 *         agregador
	 * */
	private static List<Servicios> obtenerSevicios(int id) throws SQLException, WSDLException{
		try {
			List<Servicios> servicios = new ArrayList<Servicios>();
			ResultSet rset = null;
			BdEjecucion ejecucion = new BdEjecucion();
			rset = ejecucion.consultar("SELECT * FROM AGR_SERVICIOS WHERE ID_AGREGADOR = " + id);
			Cliente cliente = new Cliente();
			while(rset.next()){
				Servicios servicio = new Servicios();
				servicio.setContrasenia(rset.getString("CONTRASENIA"));
				servicio.setId(rset.getInt("ID"));
				servicio.setUsuario(rset.getString("USUARIO"));
				servicio.setWsdlAgregador(rset.getString("WSDL_AGREGADOR"));
				servicio.setMetodos(obtenerMetodos(rset.getInt("ID"), cliente.getServicesInfo(rset.getString("WSDL_AGREGADOR"))));
				servicios.add(servicio);
			}
			rset.close();
			rset = null;
			return servicios;
		} catch (SQLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de SQL obtenerSevicios");
		} catch (WSDLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de wsdl obtenerSevicios");
		} catch (Exception e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas general obtenerSevicios");
		}
		
	}
	
	/**
	 * Este metodo debolvera una listado de metodos de servicios tambien
	 * conocidos como operaciones de los servicios web, esta busqueda se
	 * realizara a traves del id del servicio.
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @param id
	 *            {int} identificador del servicios web
	 * @param clientes
	 *            Listado de clientes obtenidos con aticipacion por una lectura
	 *            del wsdl
	 * @return {java.util.List} Listado de metodos con sus respectivos
	 *         parametros.
	 * */
	private static List<Metodos> obtenerMetodos(int id, List<?> clientes) throws SQLException{
		try {
			List<Metodos> metodos = new ArrayList<Metodos>();
			ResultSet rset = null;
			BdEjecucion ejecucion = new BdEjecucion();
			rset = ejecucion.consultar("SELECT * FROM AGR_METODOS WHERE ID_SERVICIOS = " + id);
			while(rset.next()){
				Metodos metodo = new Metodos();
				metodo.setId(rset.getInt("ID"));
				metodo.setNombre(rset.getString("NOMBRE"));
				metodo.setParametros(obtenerParametros(rset.getInt("ID")));
				
				Iterator<?> servicesIter = clientes.iterator();
				while (servicesIter.hasNext()) {
					ServiceInfo service = (ServiceInfo) servicesIter.next();
					Iterator<?> operationsIter = service.getOperations();
					while (operationsIter.hasNext()) {
						OperationInfo operation = (OperationInfo) operationsIter.next();
						if(operation.toString().equals(rset.getString("NOMBRE"))){
							metodo.setOperacionSRV(operation);
						}
					}
				}
				metodos.add(metodo);
			}
			rset.close();
			rset = null;
			return metodos;
		} catch (SQLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de SQL obtenerMetodos");
		}  catch (Exception e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas general obtenerMetodos");
		}
	}
	
	/**
	 * Dentro de la parametrizacion este metodo obtendra los paramatros del
	 * metodo de acuerdo al id del metodo que se le envie como parametro este
	 * mandara una lista de parametros
	 * 
	 * @author Edwin Mejia - Avantia Consultores
	 * @param id
	 *            {int} Es el ientificador del metodo del que se quieren
	 *            conocer los parametros
	 * @return {java.util.List} lista de parametros del metodo
	 * */
	private static List<Parametros> obtenerParametros(int id) throws SQLException{
		try {
			List<Parametros> parametros = new ArrayList<Parametros>();
			ResultSet rset = null;
			BdEjecucion ejecucion = new BdEjecucion();
			rset = ejecucion.consultar("SELECT * FROM AGR_PARAMETROS WHERE ID_METODO = " + id);
			while(rset.next()){
				Parametros parametro = new Parametros();
				parametro.setColumna(rset.getString("COLUMNA"));
				parametro.setId(rset.getInt("ID"));
				parametro.setNombre(rset.getString("NOMBRE"));
				parametro.setInsumo(rset.getString("INSUMO"));
				parametro.setTipo(rset.getString("TIPO"));
				parametros.add(parametro);
			}
			rset.close();
			rset = null;
			return parametros;
		} catch (SQLException e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas de SQL obtenerParametros");
		}  catch (Exception e) {
			if(e.getMessage()!=null)
				System.err.println(e.getMessage());
			throw new SQLException("Problemas general obtenerParametros");
		}
	}
}
