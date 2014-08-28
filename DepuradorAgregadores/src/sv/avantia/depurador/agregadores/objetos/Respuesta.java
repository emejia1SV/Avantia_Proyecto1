package sv.avantia.depurador.agregadores.objetos;

import java.io.Serializable;

public class Respuesta implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String tipo;
	private int posicion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

}
