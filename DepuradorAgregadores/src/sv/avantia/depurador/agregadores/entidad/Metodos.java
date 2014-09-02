package sv.avantia.depurador.agregadores.entidad;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "AGR_METODOS")
@Table(name = "AGR_METODOS", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class Metodos {

	@Id
	@Column(name = "ID", nullable = false)
	private int id;

	@Column(name = "NOMBRE", nullable = false)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "ID_SERVICIOS")
	private Servicios servicio;

	@OneToMany(mappedBy = "metodo", cascade = { CascadeType.ALL })
	private Set<Parametros> parametros;

	@OneToMany(mappedBy = "metodo", cascade = { CascadeType.ALL })
	private Set<Respuesta> respuestas;
	
	@OneToMany(mappedBy = "metodo", cascade = { CascadeType.ALL })
	private Set<Depuracion_bck> depuraciones;
	
	
	// private OperationInfo operacionSRV;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Servicios getServicio() {
		return servicio;
	}

	public void setServicio(Servicios servicio) {
		this.servicio = servicio;
	}

	@Override
	public String toString() {
		return "Metodos [id=" + id + ", nombre=" + nombre + "]";
	}
	/*
	 * public OperationInfo getOperacionSRV() { return operacionSRV; }
	 * 
	 * public void setOperacionSRV(OperationInfo operacionSRV) {
	 * this.operacionSRV = operacionSRV; }
	 */
}
