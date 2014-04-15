/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class TestGetEOCFIVersionFromPOM {
    
//    @Test
    public void test() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder;
        Document document;
        
        dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.parse(new File("pom.xml"));
        Node version = document.getElementsByTagName("eocfi.version").item(0);
        
        System.out.println("" + version.getTextContent());
    }
}
