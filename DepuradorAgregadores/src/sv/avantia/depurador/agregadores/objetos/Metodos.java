package sv.avantia.depurador.agregadores.objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cladonia.xml.webservice.wsdl.OperationInfo;

public class Metodos implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private List<Parametros> parametros = new ArrayList<Parametros>();
	private OperationInfo operacionSRV;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Parametros> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametros> parametros) {
		this.parametros = parametros;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public OperationInfo getOperacionSRV() {
		return operacionSRV;
	}

	public void setOperacionSRV(OperationInfo operacionSRV) {
		this.operacionSRV = operacionSRV;
	}
}
