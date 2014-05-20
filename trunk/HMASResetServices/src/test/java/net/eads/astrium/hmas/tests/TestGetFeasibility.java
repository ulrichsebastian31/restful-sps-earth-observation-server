/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestGetFeasibility.java
 *   File Type                                          :               Source Code
 *   Description                                        :                *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (coffee) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.tests;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityType;
import net.opengis.eosps.x20.TaskingParametersDocument;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestGetFeasibility {
    
    private static Client client;
    
    @BeforeClass
    public static void init()
    {
        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
    }
    
    
    
    
    
    @Test
    public void getFeasibilityOPT() throws IOException
    {
        GetFeasibilityDocument doc = GetFeasibilityDocument.Factory.newInstance();
        TaskingParametersDocument tp = EoTaskingParameters.createCoverageRequest();
        
        tp.getTaskingParameters().setCoverageProgrammingRequest(EoTaskingParameters.addOPTParams(tp.getTaskingParameters().getCoverageProgrammingRequest()));
        GetFeasibilityType feasibility = doc.addNewGetFeasibility();
        feasibility.setEoTaskingParameters(tp.getTaskingParameters());
        feasibility.setProcedure("S2AOPT");
        
        String foutoidemagueule = doc.xmlText(OGCNamespacesXmlOptions.getInstance());
        
        System.out.println("" + foutoidemagueule);
        
//        GetFeasibilityDocument doc2mescouilles = GetFeasibilityDocument.Factory.pa

//        ClientConfig config = new DefaultClientConfig();
//        Client client = Client.create(config);
//        WebResource webResource = client.resource("http://127.0.0.1:8080/DreamServices-1.0-SNAPSHOT/dream/fas/s2a-fas");
//        
//        MultivaluedMap queryParams = new MultivaluedMapImpl();
//        
//        queryParams.add("service", "eosps");
//        queryParams.add("version", "2.0");
//        queryParams.add("request", "GetFeasibility");
//        queryParams.add("acceptFormat", "application/xml");
//        
//        String xmlrequest = doc.xmlText();
//        
//        System.out.println("" + xmlrequest);
//        
//        String s = webResource.queryParams(queryParams).entity(xmlrequest, "text/xml").accept("text/xml").post(String.class);
//        
//        System.out.println(s);
//        
//        File file = new File("C:\\Users\\re-sulrich\\.dream\\fas\\feasibilityResultOPT.xml");
//        file.delete();
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(s.getBytes());
//        fos.flush();
        
    }
    
    
    
    
    
    
    
    
    
    
    @Test
    public void getFeasibilitySAR() throws IOException
    {
        GetFeasibilityDocument doc = GetFeasibilityDocument.Factory.newInstance();
        TaskingParametersDocument tp = EoTaskingParameters.createCoverageRequest();
        
        tp.getTaskingParameters().setCoverageProgrammingRequest(EoTaskingParameters.addSARParams(tp.getTaskingParameters().getCoverageProgrammingRequest()));
        GetFeasibilityType feasibility = doc.addNewGetFeasibility();
        feasibility.setEoTaskingParameters(tp.getTaskingParameters());
        feasibility.setProcedure("S1ASAR");
        
        
        System.out.println("" + doc.xmlText(OGCNamespacesXmlOptions.getInstance()));
        
//        ClientConfig config = new DefaultClientConfig();
//        Client client = Client.create(config);
//        WebResource webResource = client.resource("http://127.0.0.1:8080/DreamServices-1.0-SNAPSHOT/dream/fas/s1-fas");
//        
//        MultivaluedMap queryParams = new MultivaluedMapImpl();
//        
//        queryParams.add("service", "eosps");
//        queryParams.add("version", "2.0");
//        queryParams.add("request", "GetFeasibility");
//        queryParams.add("acceptFormat", "application/xml");
//        
//        String xmlrequest = doc.xmlText();
//        
//        System.out.println("" + xmlrequest);
//        
//        String s = webResource.queryParams(queryParams).entity(xmlrequest, "text/xml").accept("text/xml").post(String.class);
//        
//        System.out.println(s);
//        
//        File file = new File("C:\\Users\\re-sulrich\\.dream\\fas\\feasibilityResultSAR.xml");
//        file.delete();
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(s.getBytes());
//        fos.flush();
        
    }
}