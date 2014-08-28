package sv.avantia.depurador.agregadores.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Servicios implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String wsdlAgregador;
	private String usuario;
	private String contrasenia;
	private List<Metodos> metodos = new ArrayList<Metodos>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWsdlAgregador() {
		return wsdlAgregador;
	}

	public void setWsdlAgregador(String wsdlAgregador) {
		this.wsdlAgregador = wsdlAgregador;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public List<Metodos> getMetodos() {
		return metodos;
	}

	public void setMetodos(List<Metodos> metodos) {
		this.metodos = metodos;
	}
}
