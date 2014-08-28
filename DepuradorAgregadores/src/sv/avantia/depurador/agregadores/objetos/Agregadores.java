package sv.avantia.depurador.agregadores.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Agregadores implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int estado;
	private String nombre_agregador;
	private int idPais;
	private List<Servicios> servicios = new ArrayList<Servicios>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getIdPais() {
		return idPais;
	}

	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}

	public String getNombre_agregador() {
		return nombre_agregador;
	}

	public void setNombre_agregador(String nombre_agregador) {
		this.nombre_agregador = nombre_agregador;
	}

	public List<Servicios> getServicios() {
		return servicios;
	}

	public void setServicios(List<Servicios> servicios) {
		this.servicios = servicios;
	}

}
