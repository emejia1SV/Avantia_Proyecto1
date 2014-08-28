package sv.avantia.depurador.agregadores.ws.cliente;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Particle;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SimpleTypesFactory;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.XMLType;
import org.jdom.input.DOMBuilder;


import sv.avantia.depurador.agregadores.ws.cliente.prueba1.OperationInfo;
import sv.avantia.depurador.agregadores.ws.cliente.prueba1.ParameterInfo;
import sv.avantia.depurador.agregadores.ws.cliente.prueba1.ServiceInfo;
import sv.avantia.depurador.agregadores.ws.cliente.prueba1.XMLSupport;



public class ComponentBuilder {

	WSDLFactory wsdlFactory = null;

	SimpleTypesFactory simpleTypesFactory = null;

	private Vector wsdlTypes = new Vector();

	public final static String DEFAULT_SOAP_ENCODING_STYLE = "http://schemas.xmlsoap.org/soap/encoding/";

	public ComponentBuilder() {
		try {
			wsdlFactory = WSDLFactory.newInstance();
			simpleTypesFactory = new SimpleTypesFactory();
		} catch (Throwable t) {
			System.err.println(t.getMessage());
		}
	}

	public ServiceInfo buildserviceinformation(ServiceInfo serviceinfo)
			throws Exception{
		WSDLReader reader=wsdlFactory.newWSDLReader();
		Definition def=reader.readWSDL(null, serviceinfo.getWsdllocation());
		wsdlTypes = createSchemaFromTypes(def);

		
		Map services=def.getServices();
		if (services!= null) {
			Iterator svcIter = services.values().iterator();
			populateComponent(serviceinfo, (Service) svcIter.next());
		}
		return serviceinfo;
	}

	private Schema createschemafromtype(org.w3c.dom.Element schemaElement,
			Definition wsdlDefinition) {
		if (schemaElement == null) {
			System.err.println("Unable to find schema extensibility element in WSDL");
			return null;
		}
		DOMBuilder domBuilder = new DOMBuilder();
		org.jdom.Element jdomSchemaElement = domBuilder.build(schemaElement);
		if (jdomSchemaElement == null) {
			System.err.println("Unable to read schema defined in WSDL");
			return null;
		}
		Map namespaces = wsdlDefinition.getNamespaces();
		if (namespaces != null && !namespaces.isEmpty()) {
			
			Iterator nsIter = namespaces.keySet().iterator();
			while (nsIter.hasNext()) {
				String nsPrefix = (String) nsIter.next();
				String nsURI = (String) namespaces.get(nsPrefix);
				
				if (nsPrefix!=null&&nsPrefix.length() > 0) {
					org.jdom.Namespace nsDecl = org.jdom.Namespace
							.getNamespace(nsPrefix, nsURI);
					jdomSchemaElement.addNamespaceDeclaration(nsDecl);
				}
			}
		}
		jdomSchemaElement.detach();
		Schema schema = null;
		try {
			schema = XMLSupport.convertElementToSchema(jdomSchemaElement);
		}
		catch (Exception e) {
			System.out.println("a");
			System.err.println(e.getMessage());
			System.out.println("a");
		}
		return schema;
	}

	protected Vector createSchemaFromTypes(Definition wsdlDefinition) {
		Vector schemas=new Vector();
		org.w3c.dom.Element schemaElementt=null;
		if (wsdlDefinition.getTypes()!= null) {
			Vector schemaExtElem=findExtensibilityElement(wsdlDefinition
					.getTypes().getExtensibilityElements(), "schema");
			for (int i = 0; i < schemaExtElem.size(); i++) {
				ExtensibilityElement schemaElement = (ExtensibilityElement) schemaExtElem
						.elementAt(i);
				if (schemaElement != null
						&& schemaElement instanceof UnknownExtensibilityElement) {
					schemaElementt = ((UnknownExtensibilityElement) schemaElement)
							.getElement();
					Schema schema = createschemafromtype(schemaElementt,
							wsdlDefinition);
					schemas.add(schema);
				}
			}

		}
		return schemas;
	}

	private ServiceInfo populateComponent(ServiceInfo component, Service service) {
		QName qName = service.getQName();
		String namespace = qName.getNamespaceURI();
		
		component.setTargetnamespace(namespace);//���������ռ�
		String name = qName.getLocalPart();
		
		component.setName(name);
		Map ports=service.getPorts();
		Iterator portIter = ports.values().iterator();
		while (portIter.hasNext()) {
			Port port = (Port) portIter.next();
			Binding binding = port.getBinding();
			List operations=buildOperations(binding);
			Iterator operIter = operations.iterator();
			while (operIter.hasNext()) {
				OperationInfo operation = (OperationInfo) operIter.next();
				Vector addrElems = findExtensibilityElement(port
						.getExtensibilityElements(), "address");
				ExtensibilityElement element = (ExtensibilityElement) addrElems
						.elementAt(0);
				if (element != null && element instanceof SOAPAddress) {
					SOAPAddress soapAddr = (SOAPAddress) element;
					operation.setTargetURL(soapAddr.getLocationURI());
				}
				component.addOperation(operation);
			}
		}
		return component;
	}

	private List buildOperations(Binding binding) {
		List operationInfos = new ArrayList();

		List operations=binding.getBindingOperations();

		if (operations!= null && !operations.isEmpty()) {
             
			Vector soapBindingElems = findExtensibilityElement(binding
					.getExtensibilityElements(), "binding");
			String style = "document"; // default
			
			ExtensibilityElement soapBindingElem = (ExtensibilityElement) soapBindingElems
					.elementAt(0);
			if (soapBindingElem != null
					&& soapBindingElem instanceof SOAPBinding) {
				//SOAPBinding������ľ���<wsdl:binding>�µ���Ԫ��:<wsdlsoap:bindingԪ��>
				SOAPBinding soapBinding = (SOAPBinding) soapBindingElem;
				style = soapBinding.getStyle();
			}

			Iterator opIter = operations.iterator();

			while (opIter.hasNext()) {
				//BindingOperation������ľ���<wsdl:binding>�µ���Ԫ��:<wsdlsoap:operationԪ��>
				BindingOperation oper = (BindingOperation) opIter.next();
				Vector operElems = findExtensibilityElement(oper
						.getExtensibilityElements(), "operation");
				ExtensibilityElement operElem = (ExtensibilityElement) operElems
						.elementAt(0);
				//SOAPOperation������ľ���<wsdlsoap:operation>�µ���Ԫ��:<wsdlsoap:operation/>
				if (operElem != null && operElem instanceof SOAPOperation) {

					OperationInfo operationInfo = new OperationInfo(style);

					buildOperation(operationInfo, oper);

					operationInfos.add(operationInfo);
				}
			}
		}

		return operationInfos;
	}

	private OperationInfo buildOperation(OperationInfo operationInfo,
			BindingOperation bindingOper) {
		Operation oper = bindingOper.getOperation();
		operationInfo.setTargetMethodName(oper.getName());
		Vector operElems = findExtensibilityElement(bindingOper
				.getExtensibilityElements(), "operation");
		ExtensibilityElement operElem = (ExtensibilityElement) operElems
				.elementAt(0);
		if (operElem != null && operElem instanceof SOAPOperation) {
			SOAPOperation soapOperation = (SOAPOperation) operElem;
			operationInfo.setSoapActionURI(soapOperation.getSoapActionURI());
		}
		BindingInput bindingInput = bindingOper.getBindingInput();
		BindingOutput bindingOutput = bindingOper.getBindingOutput();
		Vector bodyElems = findExtensibilityElement(bindingInput
				.getExtensibilityElements(), "body");
		ExtensibilityElement bodyElem = (ExtensibilityElement) bodyElems
				.elementAt(0);

		if (bodyElem != null && bodyElem instanceof SOAPBody) {
			SOAPBody soapBody = (SOAPBody) bodyElem;

			List styles = soapBody.getEncodingStyles();
			String encodingStyle = null;

			if (styles != null) {

				encodingStyle = styles.get(0).toString();
			}

			if (encodingStyle == null) {

				encodingStyle = DEFAULT_SOAP_ENCODING_STYLE;
			}

			operationInfo.setEncodingStyle(encodingStyle.toString());

			operationInfo.setTargetObjectURI(soapBody.getNamespaceURI());
		}

		Input inDef = oper.getInput();
		if (inDef != null) {
			Message inMsg = inDef.getMessage();
			if (inMsg != null) {
				operationInfo.setInputMessageName(inMsg.getQName().getLocalPart());
                //������Ϣ�Ĳ�������
				getParameterFromMessage(operationInfo, inMsg, 1);
				operationInfo.setInmessage(inMsg);
			}
		}

		Output outDef = oper.getOutput();

		if (outDef != null) {

			Message outMsg = outDef.getMessage();

			if (outMsg != null) {
				operationInfo.setOutputMessageName(outMsg.getQName()
						.getLocalPart());
                //�����Ϣ�Ĳ�������
				getParameterFromMessage(operationInfo, outMsg, 2);
				operationInfo.setOutmessage(outMsg);
			}
		}
		

		return operationInfo;
	}

	private void getParameterFromMessage(OperationInfo operationInfo,
			Message msg, int manner) {
        String tip="";
        if(manner==1){
        	tip="����";
        }
        else{
        	
        	tip="���";
        }
       
		List msgParts = msg.getOrderedParts(null);
		Schema wsdlType = null;
		Iterator iter = msgParts.iterator();
		while (iter.hasNext()) {
			Part part = (Part) iter.next();
			String targetnamespace = "";
			XMLType xmlType=getXMLType(part, wsdlType, operationInfo);
			if (xmlType!=null&&xmlType.isComplexType()) {
				buildComplexParameter((ComplexType) xmlType, operationInfo,
						manner);
			}
			else {
				String partName = part.getName();
				ParameterInfo parameter = new ParameterInfo();
				parameter.setName(partName);
				parameter.setKind(part.getTypeName().getLocalPart());
				if (manner == 1) {
					//1��ʾ�������ǲ������������
					operationInfo.addInparameter(parameter);
				} else {
					operationInfo.addOutparameter(parameter);
				}
			}
			operationInfo.setWsdltype(wsdlTypes);

		}

	}

	private void buildComplexParameter(ComplexType type,
			OperationInfo operationInfo, int manner) {
		//XML Schema �淶�����˴����������
		//��schema��complexType��simpleType��group��annotation��include��import��element �� attribute �ȵȡ�
		//particleEnum����ComplexType�µ���Ԫ������,����������Ĳ���������
		Enumeration particleEnum=type.enumerate();
		//group����Ԫ��(�����Ǹ�������)����
		Group group = null;
		if(!particleEnum.hasMoreElements()){
		}
		while (particleEnum.hasMoreElements()) {
			Particle particle = (Particle) particleEnum.nextElement();
			if (particle instanceof Group) {
				group = (Group) particle;
				break;
			}
		}
		if (group != null) {
			
			Enumeration groupEnum = group.enumerate();
			while (groupEnum.hasMoreElements()) {
					//�����˸����������͵�ÿһ��Element���
				Structure item = (Structure) groupEnum.nextElement();
				if (item.getStructureType()==Structure.ELEMENT) {	
					ElementDecl elementDecl = (ElementDecl) item;
					XMLType xmlType = elementDecl.getType();
					if (xmlType != null && xmlType.isComplexType()) {
						buildComplexParameter((ComplexType)xmlType,operationInfo, manner);
						
					} else {
						
						ParameterInfo parameter = new ParameterInfo();
						parameter.setName(elementDecl.getName());
						parameter.setKind(elementDecl.getType().getName());
						if (manner == 1) {
							operationInfo.addInparameter(parameter);
						} else {
							operationInfo.addOutparameter(parameter);
						}
					}

				}
			}
		
		}
		
	}

	protected XMLType getXMLType(Part part, Schema wsdlType,
			OperationInfo operationInfo) {
		if (wsdlTypes == null) {
			return null;
		}

		XMLType xmlType = null;

		if (part.getElementName()!= null) {
			String elemName=part.getElementName().getLocalPart();
			ElementDecl elemDecl = null;
			for (int i = 0; i < wsdlTypes.size(); i++) {
				wsdlType = (Schema) (wsdlTypes.elementAt(i));
				String targetnamespace=wsdlType.getTargetNamespace();
				operationInfo.setNamespaceURI(targetnamespace);
				elemDecl=wsdlType.getElementDecl(elemName);
				if (elemDecl!=null) {
					break;
				}

			}
			if (elemDecl!=null) {
				 xmlType = elemDecl.getType();
                 //System.out.println(xmlType);				
			}
		
		}

		return xmlType;
	}

	private static Vector findExtensibilityElement(List extensibilityElements,
			String elementType) {
		int i = 0;
		Vector elements = new Vector();
		if (extensibilityElements!= null) {
			Iterator iter = extensibilityElements.iterator();
			while (iter.hasNext()) {
				ExtensibilityElement elment = (ExtensibilityElement)iter.next();
				if (elment.getElementType().getLocalPart().equalsIgnoreCase(elementType)) {
					elements.add(elment);
				}
			}
		}
		return elements;
	}
}