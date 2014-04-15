/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.planning;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestSubmitSegmentByID {
    
    @Test
    public void test() throws IOException {
        testCallSubmitSegmentByID();
//        testLoopSegments();
//        testCallMMFASSubmitSegmentByID();
    }

    private void testCallSubmitSegmentByID() throws IOException {
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = "http://127.0.0.1:8080/DreamServices/dream/mp/s1a-mp/eosps";
        
        WebResource webResource = client.resource(request);
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("version", "2.0");
        queryParams.add("service", "eosps");
        queryParams.add("acceptFormat", "text/xml");
        queryParams.add("request", "SubmitSegmentByID");
        
        queryParams.add("task", "1");
        
        String segmentIds = "";
        for (int i = 1; i < 11; i++) {
            segmentIds+= "" + i + ",";
        }
        segmentIds = segmentIds.substring(0, segmentIds.length() - 1);
        System.out.println("" + segmentIds);
        
        queryParams.add("segment", segmentIds);
        
        String s = webResource
                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
        System.out.println(s);
        
        
        
        
        
        new File("C:\\Users\\re-sulrich\\.dream\\testTaskMissionPlanner\\").mkdir();
        new File("C:\\Users\\re-sulrich\\.dream\\testTaskMissionPlanner\\planning.xml").createNewFile();
        PrintWriter writer = new PrintWriter("C:\\Users\\re-sulrich\\.dream\\testTaskMissionPlanner\\planning.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }

    private void testCallMMFASSubmitSegmentByID() throws IOException {
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = "http://127.0.0.1:8080/DreamServices/dream/mmfas/gmes-mmfas/eosps";
        
        WebResource webResource = client.resource(request);
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("version", "2.0");
        queryParams.add("service", "eosps");
        queryParams.add("acceptFormat", "text/xml");
        queryParams.add("request", "SubmitSegmentByID");
        
        queryParams.add("task", "1");
        
        String segmentIds = "";
        for (int i = 1; i < 11; i++) {
            segmentIds+= "" + i + ",";
        }
        segmentIds = segmentIds.substring(0, segmentIds.length() - 1);
        System.out.println("" + segmentIds);
        
        queryParams.add("segment", segmentIds);
        
        String s = webResource
                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
        System.out.println(s);
        
        new File("C:\\Users\\re-sulrich\\.dream\\testTaskMMFAS\\").mkdir();
        new File("C:\\Users\\re-sulrich\\.dream\\testTaskMMFAS\\planning.xml").createNewFile();
        PrintWriter writer = new PrintWriter("C:\\Users\\re-sulrich\\.dream\\testTaskMMFAS\\planning.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }

    private void testLoopSegments() {
        int cpt = 0;
        int nbComplete = 0;
        List<Integer> accepted = new ArrayList<>();
        for (int i = 0; i < 5;i++)
            accepted.add(i);
        
        while (!(nbComplete == accepted.size())) {
            
            List<Integer> acquired = new ArrayList<>();
            for (int i = 0; i < accepted.size(); i++) {
                System.out.println("" + cpt + " == " + accepted.get(i) + " = " + (cpt == accepted.get(i)));
                if (cpt == accepted.get(i)) {
                    
                    nbComplete++;
                }//End if now after segment
            }//End for segments
            cpt++;
            
            
            System.out.println("" + nbComplete + " == " +  accepted.size() + " = " + (nbComplete == accepted.size()));
            
        }//End while not completed
    }
}
