package sv.avantia.depurador.agregadores.ws.cliente.prueba1;

import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.Reader;

import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.exolab.castor.xml.schema.writer.SchemaWriter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


public class XMLSupport
{
   private XMLSupport()
   {
   }

   
   public static String outputString(Document doc)
   {
      XMLOutputter xmlWriter = new XMLOutputter();

      return xmlWriter.outputString(doc);
   }

  
   public static String outputString(Element elem)
   {
      XMLOutputter xmlWriter=new XMLOutputter();
      return xmlWriter.outputString(elem);
   }

  
   public static Document readXML(String xml) throws JDOMException, IOException
   {
      return readXML(new StringReader(xml));                                                                                                                                                                                                     
   }

  
   public static Document readXML(Reader reader) throws JDOMException, IOException
   {
      SAXBuilder xmlBuilder = new SAXBuilder(false);

      Document doc = xmlBuilder.build(reader);

      return doc;
   }

 
   public static Schema readSchema(Reader reader) throws IOException
   {
      
      InputSource inputSource = new InputSource(reader);

     
      SchemaReader schemaReader = new SchemaReader(inputSource);
      schemaReader.setValidation(false);

     
      Schema schema = schemaReader.read();

      return schema;
   }

  
   public static Element convertSchemaToElement(Schema schema)
           throws SAXException, IOException, JDOMException
   {
     
      String content = outputString(schema);

     
      if (content != null)
      {
       
         Document doc = readXML(new StringReader(content));

        
         return doc.getRootElement();
      }

     
      return null;
   }

   
   public static Schema convertElementToSchema(Element element) throws IOException
   {
  
      String content=outputString(element);
    
      if (content != null)
      {
         return readSchema(new StringReader(content));
      }

     
      return null;
   }

  
   public static String outputString(Schema schema) throws IOException, SAXException
   {
      StringWriter writer = new StringWriter();
      SchemaWriter schemaWriter = new SchemaWriter(writer);
      schemaWriter.write(schema);
      return writer.toString();
   }
  }