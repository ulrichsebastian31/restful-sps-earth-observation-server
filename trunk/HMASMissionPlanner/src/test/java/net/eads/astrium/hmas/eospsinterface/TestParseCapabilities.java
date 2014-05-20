/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.eads.astrium.hmas.eospsinterface;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.reset.x10.CapabilitiesDocument;
import net.opengis.sps.x21.SensorOfferingDocument;
import net.opengis.sps.x21.SensorOfferingType;
import org.apache.xmlbeans.XmlException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestParseCapabilities {

    public static String serverBaseAddr = "http://localhost:8080";
//    public static String serviceBaseAddr = "/DreamServices/dream/mmfas/gmes-mmfas/reset/1.0.0/";
    public static String serviceBaseAddr = "/HMASResetServices/hmas/reset/1.0.0/";
    
    public static final String testFolderPath = 
            "C:\\Users\\re-sulrich\\.hmas\\Tests\\testResetRequests\\";
    
    
    @BeforeClass
    public static void init() {
        File file = new File(testFolderPath);
        file.mkdirs();
    }
    
    @Test
    public void test() throws IOException {
        
        testGetCapabilities();
    }
    
    
    public void testGetCapabilities() throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr;
        
        WebResource webResource = client.resource(request);
        
        String s = webResource
//                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
        System.out.println(s);
        try {
            CapabilitiesDocument doc = CapabilitiesDocument.Factory.parse(s);
            
            System.out.println("" + doc.xmlText(OGCNamespacesXmlOptions.getInstance()));
            
            
            System.out.println("" + 
                    ((LinearRingType)(
                        ((SensorOfferingType)
                         (doc.getCapabilities().getContents().getOfferingArray(0).getAbstractOffering())
                        )
                        .getObservableArea().getByPolygon().getPolygon().getExterior().getAbstractRing()
                    )).addNewPosList().getStringValue() );
            
    //        new File(testFolderPath).mkdir();
    //        new File(testFolderPath + "capabilities.xml").createNewFile();
    //        PrintWriter writer = new PrintWriter(testFolderPath + "capabilities.xml", "UTF-8");
    //        writer.print(s);
    //        writer.flush();
    //        writer.close();
        } catch (XmlException ex) {
            ex.printStackTrace();
        }
    }
    
}
