/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.eads.astrium.dream.xml.generating;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class OGCNamespacesXmlOptions extends XmlOptions {

    public static OGCNamespacesXmlOptions instance;
    
    public static OGCNamespacesXmlOptions getInstance() {
        if (instance == null) {
            try {
                instance = new OGCNamespacesXmlOptions();
                instance.setSaveOuter();
                
            } catch (    ParserConfigurationException | SAXException ex) {
                ex.printStackTrace();
            }
        }
        
        return instance;
    }
    
    public static OGCNamespacesXmlOptions getInnerInstance() {
        if (instance == null) {
            try {
                instance = new OGCNamespacesXmlOptions();
                instance.setSaveInner();
                
            } catch (    ParserConfigurationException | SAXException ex) {
                ex.printStackTrace();
            }
        }
        
        return instance;
    }
    
    //http://codebrane.com/blog/2005/12/12/xmlbeans-and-namespaces/
    public OGCNamespacesXmlOptions() throws ParserConfigurationException, SAXException {
    
        System.out.println("Getting namespace prefixes...");
        Map ogcPrefixes = this.getSuggestedPrefixes();
        
//        super.setLoadSubstituteNamespaces(ogcPrefixes);
        super.setLoadAdditionalNamespaces(ogcPrefixes);
//        super.setload
        
        super.setSaveSuggestedPrefixes(ogcPrefixes);
        super.setSaveNamespacesFirst();
        super.setSaveImplicitNamespaces(ogcPrefixes);
        super.setSaveAggressiveNamespaces();
    }

    public OGCNamespacesXmlOptions(XmlOptions other) throws ParserConfigurationException, SAXException {
        super(other);
        Map ogcPrefixes = this.getSuggestedPrefixes();
        
//        super.setLoadSubstituteNamespaces(ogcPrefixes);
        super.setLoadAdditionalNamespaces(ogcPrefixes);
        
        super.setSaveSuggestedPrefixes(ogcPrefixes);
        super.setSaveNamespacesFirst();
        super.setSaveImplicitNamespaces(ogcPrefixes);
        super.setSaveAggressiveNamespaces();
    }

    private Map getSuggestedPrefixes() {
        
        Map suggestedPrefixes = null;
        try {
            suggestedPrefixes = this.getSuggestedPrefixesFromConfFile();
        } catch (IllegalArgumentException | ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
            System.out.println("");
            System.out.println("" + ex.getClass().getName() + " : " + ex.getMessage());
            System.out.println("");
            System.out.println("");
            Logger.getLogger(OGCNamespacesXmlOptions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (suggestedPrefixes == null || suggestedPrefixes.isEmpty()) {
            suggestedPrefixes = new HashMap();
            suggestedPrefixes.put("http://www.opengis.net/reset/1.0", "reset");
            suggestedPrefixes.put("http://www.opengis.net/eosps/2.0", "eosps");
            suggestedPrefixes.put("http://www.opengis.net/sps/2.0", "sps");
            suggestedPrefixes.put("http://www.opengis.net/sps/2.1", "sps");
            suggestedPrefixes.put("http://www.opengis.net/swes/2.0", "swes");
            suggestedPrefixes.put("http://www.opengis.net/swes/2.1", "swes");
            suggestedPrefixes.put("http://www.opengis.net/swe/2.0", "swe");
            suggestedPrefixes.put("http://www.opengis.net/sensorML/1.0.2", "sml");
            suggestedPrefixes.put("http://www.opengis.net/gml/3.1", "gml");
            suggestedPrefixes.put("http://www.opengis.net/gml/3.2", "gml");
            suggestedPrefixes.put("http://www.opengis.net/ows/1.0", "ows");
            suggestedPrefixes.put("http://www.opengis.net/ows/1.1", "ows");
            suggestedPrefixes.put("http://earth.esa.int/eop/", "eop");
        }
        
        return suggestedPrefixes;
    }
    
    public Map getSuggestedPrefixesFromConfFile() throws ParserConfigurationException, SAXException, IOException {
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream content = classLoader.getResourceAsStream("net.eads.astrium.hmas.xmlNamespaces.xml");
        
        InputStream content = new FileInputStream("C:\\Users\\re-sulrich\\My Projects\\HMAS-GC\\trunk\\HMASXMLParser\\src\\main\\resources\\xsd\\net.eads.astrium.hmas.xmlNamespaces.xml");
        
        if (content != null)
            System.out.println("Content : " + content.available());
        else 
            System.out.println("Content null");
        
        return getSuggestedPrefixesFromConfFile(content);
    }
    
    public Map getSuggestedPrefixesFromConfFile(InputStream content) throws ParserConfigurationException, SAXException, IOException {
        
        Map suggestedPrefixes = new HashMap();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(content);
        Element root = doc.getDocumentElement();
        root.normalize();
        NodeList namespaces = root.getElementsByTagName("namespaces").item(0).getChildNodes();
        
        for (int i = 0; i < namespaces.getLength(); i++) {
            Node namespace = namespaces.item(i);
            
            if (namespace.getNodeName().equals("namespace")) {
                NodeList nodes = namespace.getChildNodes();
                
                String prefix = null;
                List<String> uris = new ArrayList<>();
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node node = nodes.item(j);
                    
                    if (node.getNodeName().equals("prefix")) {
                        prefix = node.getTextContent();
                    }
                    if (node.getNodeName().equals("uri")) {
                        uris.add(node.getTextContent());
                    }                    
                }
                
                System.out.println("Namespaces : ");
                for (String string : uris) {
                    suggestedPrefixes.put(string, prefix);
                    System.out.println("" + string + " is " + prefix);
                }
            }
        }
        
        return suggestedPrefixes;
    }
}
