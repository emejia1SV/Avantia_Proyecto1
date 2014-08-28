package sv.avantia.depurador.agregadores.ws.cliente.prueba1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exolab.castor.xml.schema.Schema;

public class ServiceInfo {
	
	private int sid;
	/**服务名*/
	private String name;
	/** WSDL文件位置 */
	private String wsdllocation;//
	private String endpoint;
	private String targetnamespace;
	private Schema wsdlType;
	 /** The list of operations that this service defines. */
	List operations = new ArrayList();
	private CategoryInfo category;
	
	private int state = 0;
	
	private String description;
	private String chName;//中文名称
	
	public Schema getWsdlType() {
		return wsdlType;
	}

	public void setWsdlType(Schema wsdlType) {
		this.wsdlType=wsdlType;
	}

	public List getOperation() {
		return operations;
	}
	
	public List getOperations(){
		   return	operations;
	}
	
	public void addOperation(OperationInfo operation) {
	      operations.add(operation);
	}
	public String toString(){
	      return getName();
    }
	public String getTargetnamespace() {
		return targetnamespace;
	}
	public void setTargetnamespace(String targetnamespace) {
		this.targetnamespace = targetnamespace;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getWsdllocation() {
		return wsdllocation;
	}
	public void setWsdllocation(String wsdllocation) {
		this.wsdllocation = wsdllocation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public void setOperations(List operations) {
		this.operations = operations;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public CategoryInfo getCategory() {
		return category;
	}

	public void setCategory(CategoryInfo category) {
		this.category = category;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}
	
}