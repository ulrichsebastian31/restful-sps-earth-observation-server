/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.eads.astrium.dream.xml.generating;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class AppTest {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File file = new File("src/main/resources/xsd/net.eads.astrium.hmas.xmlNamespaces.xml");
        FileInputStream content = new FileInputStream(file);
        OGCNamespacesXmlOptions options = new OGCNamespacesXmlOptions();
        Map prefixes = options.getSuggestedPrefixesFromConfFile(content);
        
        for (Object object : prefixes.keySet()) {
            String uri = (String)object;
            String prefix = (String)prefixes.get(object);
            
            System.out.println("namespace " + prefix + " : " + uri);
        }
    }
}
