/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.reset;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ws.rs.core.MultivaluedMap;
import net.opengis.reset.x10.FeasibilityDocument;
import net.opengis.reset.x10.PlanningDocument;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestResetInterface {
    
    public static String serverBaseAddr = "http://localhost:8080";
//    public static String serviceBaseAddr = "/DreamServices/dream/mmfas/gmes-mmfas/reset/1.0.0/";
    public static String serviceBaseAddr = "/HMASResetServices/hmas/reset/1.0.0/";
    
    
    
    
    public static final String testFolderPath = 
            "C:\\Users\\re-sulrich\\.hmas\\Tests\\testResetRequests\\";
    
    private String feasibilityTaskID;
    private String planningTaskID;
    
    
    @BeforeClass
    public static void init() {
        File file = new File(testFolderPath);
        file.mkdirs();
    }
    
    @Test
    public void test() throws IOException, Exception {
        testGetCapabilities();
        testDescribeSensor();
        testDescribeTasking();
        testGetSensorAvailibilities();
        testSynchronousPast();
        testGetStatus(feasibilityTaskID);
        testGetTask(feasibilityTaskID); 
        testSubmitTenSegmentsByIDAsynchronous(feasibilityTaskID, 0);
        testGetStatus(planningTaskID);
        testGetTask(planningTaskID); 
        
        testGetAllTasks();
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
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "capabilities.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "capabilities.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
    public void testDescribeSensor() throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                "procedures/S1ASAR/sensorML";
        
        WebResource webResource = client.resource(request);
        
        String s = webResource
//                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "sensorML.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "sensorML.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
    public void testDescribeTasking() throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                "procedures/S1ASAR/tasking";
        
        WebResource webResource = client.resource(request);
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        String s = webResource
//                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "tasking.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "tasking.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
    public void testGetSensorAvailibilities() throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                "procedures/S1ASAR/availibilities";
        
        WebResource webResource = client.resource(request);
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        //directly done submission
        queryParams.add("start", "2013-08-01");
        queryParams.add("end", "2013-08-31");
        
        String s = webResource
                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "availibilities.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "availibilities.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
    public void testSynchronousPast() throws Exception {
        
//        nbSegmentsInDatabase = 0;
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                "feasibility";
        
        WebResource webResource = client.resource(request);
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("procedure", "SAR");
        queryParams.add("box", "23.32,54.65,25.43,50.23");
//        queryParams.add("box", "0.0,60.0,90.0,90.0");
        //directly done submission
        queryParams.add("start", "2013-08-01T09:20:02Z");
        queryParams.add("end", "2013-08-10T20:58:46Z");
        //cancellable
//        queryParams.add("start", "2014-08-01T09:20:02Z");
//        queryParams.add("end", "2014-08-10T20:58:46Z");
        
//        queryParams.add("synchronous", "true");
        
        String s = webResource
                .queryParams(queryParams)
                .accept("application/xml")
                .post(String.class);
        
//        System.out.println(s);
        
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "getFeasibility.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "getFeasibility.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
        
        FeasibilityDocument resp = FeasibilityDocument.Factory.parse(s);
        feasibilityTaskID = resp.getFeasibility().getStatus().getTask();
        
    }
    
    
    public void testGetStatus(String taskId) throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                "feasibility/"+taskId+"";
        
        WebResource webResource = client.resource(request);
        
        
        String s = webResource
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "getStatus"+taskId+".xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "getStatus"+taskId+".xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
    public void testGetTask(String taskId) throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                ""
                + "feasibility/"+taskId+"/segments";
        
        WebResource webResource = client.resource(request);
        
        
        String s = webResource
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "getTask"+taskId+".xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "getTask"+taskId+".xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
    
    
    public void testSubmitTenSegmentsByIDAsynchronous(String taskId, int firstSegment) throws Exception {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr + 
                ""
                + "feasibility/"+taskId+"/segments/";
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        System.out.println("First segment : " + firstSegment);
//        queryParams.add("task", mmfasFeasibilityID);
        
        if (feasibilityTaskID == null)
            feasibilityTaskID = "1";
        
        String segmentIds = "";
        for (int i = 1; i < 11; i++) {
//            segmentIds+= "" + i + ",";
            segmentIds+= "" + (i + firstSegment) + ",";
        }
        segmentIds = segmentIds.substring(0, segmentIds.length() - 1);
        System.out.println("" + segmentIds);
        
        request += segmentIds;
//        queryParams.add("segment", segmentIds);
        
        //No "synchronous" should do only submission without wait for completion
        //So segments should be "ACCEPTED" and/or "REJECTED"
        //And status "PLANNING ACCEPTED" or "PLANNING FAILED"
//        queryParams.add("synchronous", "false");
        
        System.out.println("" + request);
        
        WebResource webResource = client.resource(request);
        
        String s = webResource
                .queryParams(queryParams)
                .accept("application/xml")
                .post(String.class);
        
        System.out.println(s);
        
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "submitSegmentByID.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "submitSegmentByID.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
        
        PlanningDocument resp = PlanningDocument.Factory.parse(s);
        planningTaskID = resp.getPlanning().getStatus().getTask();
        
        
        if (resp.getPlanning().getStatus().getIdentifier().equalsIgnoreCase("ALREADY DONE")) {
            System.out.println("This task has already been done.");
        }
        else {
            System.out.println("Started task.");
//            testGetTask(mmfasPlanningID);
        }
    }
    
    
    public void testGetAllTasks() throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = serverBaseAddr + serviceBaseAddr
                + "feasibility/";
        
        WebResource webResource = client.resource(request);
        
        
        String s = webResource
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
        
        new File(testFolderPath).mkdir();
        new File(testFolderPath + "getAllTasks.xml").createNewFile();
        PrintWriter writer = new PrintWriter(testFolderPath + "getAllTasks.xml", "UTF-8");
        writer.print(s);
        writer.flush();
        writer.close();
    }
    
}
