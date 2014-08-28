package sv.avantia.depurador.agregadores.ws.cliente.prueba1;

import java.util.Set;

/**
 * 服务的类别
 * 
 * @author biao
 * 
 */
public class CategoryInfo {

	private int cid;

	private String cname;

	private Set services;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Set getServices() {
		return services;
	}

	public void setServices(Set services) {
		this.services = services;
	}

}