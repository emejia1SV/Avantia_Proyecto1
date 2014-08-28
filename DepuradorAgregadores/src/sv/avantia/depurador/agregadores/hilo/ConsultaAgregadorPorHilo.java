package sv.avantia.depurador.agregadores.hilo;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;

import sv.avantia.depurador.agregadores.objetos.Agregadores;
import sv.avantia.depurador.agregadores.objetos.Metodos;
import sv.avantia.depurador.agregadores.objetos.Parametros;
import sv.avantia.depurador.agregadores.objetos.Respuesta;
import sv.avantia.depurador.agregadores.objetos.Servicios;
import sv.avantia.depurador.agregadores.ws.cliente.Cliente;


public class ConsultaAgregadorPorHilo extends Thread {

	private ResultSet[] insumos;
	private Agregadores parametrizacion;

	public void run() {
		// consultar un agregador WS
		try {
			procesarResultados();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void procesarResultados(){
		try {
			if (getInsumos() != null && 0 < getInsumos().length && getInsumos()[0] != null) {
				ResultSet resultSet0 = getInsumos()[0];
				ResultSet resultSet1 = getInsumos()[1];
				ResultSet resultSet2 = getInsumos()[2];
				resultSet0.first();
				while(resultSet0.next()){
					if (getInsumos() != null && 1 < getInsumos().length && getInsumos()[1] != null) {
						resultSet1.first();
						while(resultSet1.next()){
							if (getInsumos() != null && 2 < getInsumos().length && getInsumos()[2] != null) {
								resultSet2.first();
								while(resultSet2.next()){
									if (getInsumos() != null && 3 < getInsumos().length && getInsumos()[3] != null) {
										while(getInsumos()[3].next()){
											if (getInsumos() != null && 4 < getInsumos().length && getInsumos()[4] != null) {
												while(getInsumos()[4].next()){
													if (getInsumos() != null && 5 < getInsumos().length && getInsumos()[5] != null) {
														while(getInsumos()[5].next()){
															
														}
													}
												}
											}
										}
									}else{
										try {
											if (getParametrizacion() != null) {				
												if(getParametrizacion().getServicios()!=null){
													for (Servicios servicio : getParametrizacion().getServicios()) {
														if(servicio.getMetodos()!=null){
															for (Metodos metodo : servicio.getMetodos()) {
																if(metodo.getParametros()!=null){
																	for (Parametros parametro : metodo.getParametros()) {
																		
																		
																		if (parametro.getInsumo().equals(resultSet0.getString("INSUMO"))) {
																			metodo.getOperacionSRV().setInputMessageText(
																					metodo.getOperacionSRV().getInputMessageText().replace(("_*"+parametro.getNombre()+"_*"), "" + Class.forName(parametro.getTipo()).getConstructor(String.class).newInstance(resultSet0.getString(parametro.getColumna().toUpperCase())))
																					);
																		}else if (parametro.getInsumo().equals(resultSet1.getString("INSUMO"))) {
																			metodo.getOperacionSRV().setInputMessageText(
																					metodo.getOperacionSRV().getInputMessageText().replace(("_*"+parametro.getNombre()+"_*"), "" + Class.forName(parametro.getTipo()).getConstructor(String.class).newInstance(resultSet1.getString(parametro.getColumna().toUpperCase())))
																					);
																		}else if (parametro.getInsumo().equals(resultSet2.getString("INSUMO"))) {
																			metodo.getOperacionSRV().setInputMessageText(
																					metodo.getOperacionSRV().getInputMessageText().replace(("_*"+parametro.getNombre()+"_*"), "" + Class.forName(parametro.getTipo()).getConstructor(String.class).newInstance(resultSet2.getString(parametro.getColumna().toUpperCase())))
																					);
																		}	
																	}
																}
																obtenerRespuesta(Cliente.invokeOperation(metodo.getOperacionSRV()));
																System.out.println();
																	}
																	
																}
															}
														}
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
						
					}else{
						System.out.println("no hay datos que procesar");
					}
				}
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
	
	private Respuesta obtenerRespuesta(String xmlResponse){
		return null;
	}

	public Agregadores getParametrizacion() {
		return parametrizacion;
	}

	public void setParametrizacion(Agregadores parametrizacion) {
		this.parametrizacion = parametrizacion;
	}

	public ResultSet[] getInsumos() {
		return insumos;
	}

	public void setInsumos(ResultSet[] insumos) {
		this.insumos = insumos;
	}
}