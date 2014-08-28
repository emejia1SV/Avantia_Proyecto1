package sv.avantia.depurador.agregadores.inicio;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sv.avantia.depurador.agregadores.jdbc.BdEjecucion;
import sv.avantia.depurador.agregadores.ws.cliente.ComponentBuilder;
import sv.avantia.depurador.agregadores.ws.cliente.prueba1.ServiceInfo;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test3();
	}
	
	public static void test4(){
		ComponentBuilder builder = new ComponentBuilder();
		ServiceInfo serviceinfo = new ServiceInfo();
		
		try {
			serviceinfo.setWsdllocation("http://api.nixps.net/services/claro/wsAgregadoresClaro/wsagregadores.php?wsdl");
			builder.buildserviceinformation(serviceinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test3(){
		BdEjecucion 			ejecucion  = new BdEjecucion();
		try {
			ResultSet 			resultados = ejecucion.consultar("SELECT QUERY FROM AGR_INSUMOS");
			if(resultados != null){
				String[] querys = new String[tamanioRset(resultados)];
				for (int i = 0; resultados.next(); i++) {
					querys[i] = resultados.getString("QUERY");
				}
				procesarQuerys(querys);
			}			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void procesarQuerys(String[] querys) throws SQLException{
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
		procesarResultados(rsets);
	}
	
	private static void procesarResultados(ResultSet[] rsets){
		try {
			if (rsets != null && 0 < rsets.length && rsets[0] != null) {
				ResultSet rset1 = new BdEjecucion().consultar("select * from prueba");
				ResultSet resultSet0 = rsets[0];
				ResultSet resultSet1 = rsets[1];
				ResultSet resultSet2 = rsets[2];
				resultSet0.first();
				while(resultSet0.next()){
					if (rsets != null && 1 < rsets.length && rsets[1] != null) {
						resultSet1.first();
						while(resultSet1.next()){
							if (rsets != null && 2 < rsets.length && rsets[2] != null) {
								resultSet2.first();
								while(resultSet2.next()){
									if (rsets != null && 3 < rsets.length && rsets[3] != null) {
										while(rsets[3].next()){
											if (rsets != null && 4 < rsets.length && rsets[4] != null) {
												while(rsets[4].next()){
													if (rsets != null && 5 < rsets.length && rsets[5] != null) {
														while(rsets[5].next()){
															
														}
													}
												}
											}
										}
									}else{
										rset1.first();
										if (rset1 != null) {
											while (rset1.next()) {
												try {
													if 		 (rset1.getString("TABLA").equals(resultSet0.getString("INSUMO"))) {														
														System.out.print(" " + Class.forName(rset1.getString("TIPO")).getConstructor(String.class).newInstance(resultSet0.getString(rset1.getString("NOMBRE").toUpperCase())));
													}else if (rset1.getString("TABLA").equals(resultSet1.getString("INSUMO"))) {
														System.out.print(" " + Class.forName(rset1.getString("TIPO")).getConstructor(String.class).newInstance(resultSet1.getString(rset1.getString("NOMBRE").toUpperCase())));
													}else if (rset1.getString("TABLA").equals(resultSet2.getString("INSUMO"))) {
														System.out.print(" " + Class.forName(rset1.getString("TIPO")).getConstructor(String.class).newInstance(resultSet2.getString(rset1.getString("NOMBRE").toUpperCase())));
													}
												} catch (ClassNotFoundException e) {

												} catch (IllegalArgumentException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (SecurityException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (InstantiationException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (IllegalAccessException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (InvocationTargetException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (NoSuchMethodException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

											}
											
										}
									}
								}
							}
						}
						
					}else{
						System.out.println("no hay datos que procesar");
					}
				}
				rset1.close();
				resultSet0.close();
				resultSet0 = null;
				resultSet1.close();
				resultSet1 = null;
				resultSet2.close();
				resultSet2 = null;
			} else {
				System.out.println("Hasta aqui llegue porque no hay data que procesar");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test2(){
		try {
			
			BdEjecucion ejecucion = new BdEjecucion();
			Map<String, Integer> tamanios = new HashMap<String, Integer>();
			ResultSet rset2 = ejecucion.consultar("select nombre as servicio from AGR_METODOS");
			ResultSet rset3 = ejecucion.consultar("select estado as indice from AGR_AGREGADORES");
			ResultSet rset4 = ejecucion.consultar("select numero from CLIENTE_TEL");
			
			tamanios.put("rset2", tamanioRset(rset2));
			tamanios.put("rset3", tamanioRset(rset3));
			tamanios.put("rset4", tamanioRset(rset4));
			
			Map<String, Integer> sorted = sortByValues(tamanios);
			System.out.println("Sorted Map in Java by values: " + sorted);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

	
	public static void test1(){
		try { 
			ResultSet rset1 = new BdEjecucion().consultar("select * from prueba");
			ResultSet rset2 = new BdEjecucion().consultar("select nombre as servicio from AGR_METODOS");
			ResultSet rset3 = new BdEjecucion().consultar("select estado as indice from AGR_AGREGADORES");
			ResultSet rset4 = new BdEjecucion().consultar("select numero from CLIENTE_TEL");
			
			if (rset2 != null) {
				while(rset2.next()){}
			}
			if (rset3 != null) {
				while(rset3.next()){}
			}
			if (rset4 != null) {
				while(rset4.next()){}
			}
			
			if(rset1!=null){
				while(rset1.next()){
					try {
						if(rset1.getString("TABLA").equals("insumo1")){
							System.out.println(Class.forName(rset1.getString("TIPO")).getConstructor(String.class).newInstance(rset1.getString("NUMERO")));
						}
						if(rset1.getString("TABLA").equals("insumo2")){
							System.out.println(Class.forName(rset1.getString("TIPO")).getConstructor(String.class).newInstance(rset2.getString("NUMERO")));
						}
						if(rset1.getString("TABLA").equals("insumo3")){
							System.out.println(Class.forName(rset1.getString("TIPO")).getConstructor(String.class).newInstance(rset3.getString("NUMERO")));
						}
						
					} catch (ClassNotFoundException e) {
						
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
