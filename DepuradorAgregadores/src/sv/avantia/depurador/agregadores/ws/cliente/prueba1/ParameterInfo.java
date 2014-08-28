package sv.avantia.depurador.agregadores.ws.cliente.prueba1;

public class ParameterInfo {
	private int pid;
	private String name;// 参数名
	private String kind;// 参数类型
	private int id;// 参数标识
	private String value;// 参数值
	private String serviceid;// 服务id
	private OperationInfo operation;// 操作名
	private String inputtype = null;
	private String type;
	private String parameterDes;// 参数描述
	private String chName;// 中文名称

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInputtype() {
		return inputtype;
	}

	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String name2) {
		this.kind = name2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParameterDes() {
		return parameterDes;
	}

	public void setParameterDes(String parameterDes) {
		this.parameterDes = parameterDes;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public OperationInfo getOperation() {
		return operation;
	}

	public void setOperation(OperationInfo operation) {
		this.operation = operation;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

}