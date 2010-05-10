import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.VisitorSupport;
import org.dom4j.dtd.AttributeDecl;
import org.dom4j.dtd.ElementDecl;
import org.dom4j.dtd.ExternalEntityDecl;
import org.dom4j.dtd.InternalEntityDecl;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class Dom4JTester extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDom4J() throws DocumentException, IOException {
		
    	// Get Document with a specified file name.
		File file = new File("Invoice.xml");
		SAXReader reader = new SAXReader();    // DOMReader is an another choice.
		Document document = reader.read(file);
		System.out.println("Xml Document Name: " + document.getName());
		
    	// Get root element.
		Element root = document.getRootElement();
		System.out.println("Root Element Name: " + root.getName());
        System.out.println("Root getStringValue: " + root.getStringValue());
        System.out.println("Root getText: " + root.getText());
		
		// Get the children element.
		Iterator it = root.elementIterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			System.out.println("Root Element Children Name: " + element.getName());
		}
		
		// Get the children element with the name "Header".
		it = root.elementIterator("Header");
		while (it.hasNext()) {
			Element element = (Element) it.next();
			System.out.println("Root Element Children Name Specified With \"Header\": " + element.getName());
		}
        
		// Get the children element with the name "Header".
		Element element = root.element("Header");
		System.out.println("Root Element Children Name Specified With \"Header\": " + element.getName());
        
		// Get the value of children element with the name "Month".
		String monthText = root.element("Header").element("Date").elementText("Month");
		System.out.println("Root Element Children Name Specified With \"Month\"'s text value: " + monthText);
		
		// Get the attributes and attribute values belong to the speicifed element.
        Element header = (Element) root.elementIterator("Header").next();
        System.out.println("Header Attribute Value: " + header.attributeValue("invoiceNumber"));
		it = header.attributeIterator();
		while (it.hasNext()) {
			Attribute attribute = (Attribute) it.next();
			System.out.println("Header Element Attribute Name: " + attribute.getName());
            System.out.println("Header Attribute Value: " + attribute.getValue());
		}
		
		// Tree walk method to get all the children element.
		treeWalk(root);
		
		// Visitor Design Pattern
		root.accept(new MyVisitor());
		
		// XPath function: To invoke this method, you should add jaxen.jar to this classpath.
		Node node = document.selectSingleNode("//Invoice/Header/BillTo");
		System.out.println("Select Single Node: " + node.getName());
		System.out.println("node.valueof(\"@name\"): " + node.valueOf("@name"));
		
		List list = document.selectNodes("//Invoice/Header");
		Iterator its = list.iterator();
		while (its.hasNext()) {
			Node nodes = (Node) its.next();
			System.out.println("Select Nodes: " + nodes.getName());
		}
        
        // XML to String and String to XML
        String text = document.asXML();
        System.out.println(text);
        
        text = "<person><name>James</name></person>";
        Document newDocument = DocumentHelper.parseText(text); 
        System.out.println("new Document root: " + newDocument.getRootElement().getName());
        
        // Create XML
        Document document2 = DocumentHelper.createDocument();
        Element root2 = document2.addElement("root");
        root2.addElement("author")
             .addAttribute("name", "James")
             .addAttribute("location", "UK")
             .addText("James Strachan");
        root2.addElement("author")
             .addAttribute("name", "Bob")
             .addAttribute("location", "US")
             .addText("Bob McWhirter");
        
        // Write a XML
        XMLWriter writer = new XMLWriter(
                new FileWriter( "output.xml" )
            );
        writer.write( document2 );


        // Pretty print the document to System.out
        OutputFormat format = OutputFormat.createPrettyPrint();
        writer = new XMLWriter( System.out, format );
        writer.write( document2 );
        
        System.out.println("\n===================================\n");

        // Compact format to System.out
        format = OutputFormat.createCompactFormat();
        writer = new XMLWriter( System.out, format );
        writer.write( document2 );
        
        // Validate the xml===============================================================
        reader = new SAXReader();
        reader.setValidation(true);
        
        // specify the schema to use
//        try {
//          reader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
//                             "Invoice.dtd"
//          );
//      } catch (SAXException e) {
//          e.printStackTrace();
//      }
        
        // add error handler which turns any errors into XML
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler( errorHandler );

        // parse the document
        document = reader.read(new File("ErrorInvoice.xml"));

        // output the errors XML
        writer = new XMLWriter( OutputFormat.createPrettyPrint() );
        if ( errorHandler.getErrors().hasContent()) {
            writer.write(errorHandler.getErrors());
            System.out.println();
            System.out.println("System.out.println getErrors().getName(): " + errorHandler.getErrors().getName());
            System.out.println("System.out.println getErrors().getStringValue(): " + errorHandler.getErrors().getStringValue());
        } else {
            System.out.println("Validation no error found.");
        }
        
        // DTD Test ===============================================================================
        System.out.println("\nBegin to test DTD.....................................................");
        reader = new SAXReader();
        reader.setIncludeInternalDTDDeclarations(false);
        reader.setIncludeExternalDTDDeclarations(true);
        reader.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                InputStream is = new FileInputStream("Invoice.dtd");
                return new InputSource(is);
            }
        });
        
        document = reader.read(new File("Invoice.xml"));
        DocumentType documentType = document.getDocType();
        List documentTypeList = documentType.getExternalDeclarations();
        Iterator documentTypeIterator = documentTypeList.iterator();
        while (documentTypeIterator.hasNext()) {
            Object obj = documentTypeIterator.next();
            System.out.println(obj.getClass().getName());
            if (obj instanceof AttributeDecl) {
                AttributeDecl attributeDecl = (AttributeDecl) obj;
                System.out.println("attributeDecl.getElementName(): " + attributeDecl.getElementName());
                System.out.println("attributeDecl.getAttributeName(): " + attributeDecl.getAttributeName());
                System.out.println("attributeDecl.getType(): " + attributeDecl.getType());
                System.out.println("attributeDecl.getValue(): " + attributeDecl.getValue());
                System.out.println("attributeDecl.getValueDefault(): " + attributeDecl.getValueDefault());
            } else if (obj instanceof ElementDecl) {
                ElementDecl elementDecl = (ElementDecl) obj;
                System.out.println("elementDecl.getModel(): " + elementDecl.getModel());
                System.out.println("elementDecl.getName(): " + elementDecl.getName());
            } else if (obj instanceof InternalEntityDecl) {
                InternalEntityDecl internalEntityDecl = (InternalEntityDecl) obj;
                System.out.println("internalEntityDecl.getName(): " + internalEntityDecl.getName());
                System.out.println("internalEntityDecl.getValue(): " + internalEntityDecl.getValue());
            } else if (obj instanceof ExternalEntityDecl) {
                ExternalEntityDecl externalEntityDecl = (ExternalEntityDecl) obj;
                System.out.println("externalEntityDecl.getName(): " + externalEntityDecl.getName());
                System.out.println("externalEntityDecl.getPublicID(): " + externalEntityDecl.getPublicID());
                System.out.println("externalEntityDecl.getSystemID(): " + externalEntityDecl.getSystemID());
            } else {
               throw new AssertionError("Unexpected declaration type: "
                        + obj.getClass());
            }
        }
        
        writer.close();
	}
	
	public void treeWalk(Element element) {
		for (int i = 0; i < element.nodeCount(); i++) {
			Node node = element.node(i);
			if (node instanceof Element) {
				treeWalk((Element)node);
				System.out.println("ALL THE ELEMENT NAME: " + node.getName());
			} else {
			}
		}
	}
	
	private class MyVisitor extends VisitorSupport {

		public void visit(Attribute attribute) {
			System.out.println("Visitor attribute name: " + attribute.getName());
		}

		public void visit(Element element) {
			System.out.println("Visitor element name: " + element.getName());
		}
	}
}
