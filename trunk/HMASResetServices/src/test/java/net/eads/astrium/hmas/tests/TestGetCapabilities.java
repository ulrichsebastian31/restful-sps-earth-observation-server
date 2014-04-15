/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestGetCapabilities.java
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
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Test;



/**
 *
 * @author re-sulrich
 */
public class TestGetCapabilities {
    
//    private static Client client;
    
//    @BeforeClass
//    public static void init()
//    {
//    }
    
    // http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/s1-fas?request=GetCapabilities&service=eosps&acceptFormat=application/xml&acceptVersion=2.0
    @Test
    public void getCapabilitiesXML()
    {
        Client client = Client.create();
        WebResource webResource = client.resource("http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/s1-fas");
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("request", "GetCapabilities");
        queryParams.add("service", "eosps");
        queryParams.add("acceptFormat", "application/xml");
        queryParams.add("acceptFormat", "application/json");
        queryParams.add("acceptVersion", "2.0");
        
        String s = webResource.queryParams(queryParams).accept("text/xml").get(String.class);
        
        System.out.println(s);
    }
    
    @Test
    public void getCapabilitiesJson()
    {
        Client client = Client.create();
        WebResource webResource = client.resource("http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/s1-fas");
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("request", "GetCapabilities");
        queryParams.add("service", "eosps");
        queryParams.add("acceptFormat", "application/json");
        queryParams.add("acceptVersion", "2.0");
        
        String s = webResource.queryParams(queryParams).accept("application/json").get(String.class);
        
        System.out.println(s);
    }
}
