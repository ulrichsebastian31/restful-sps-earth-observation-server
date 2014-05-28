/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestGetSensorAvailibility.java
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
import javax.ws.rs.core.MultivaluedMap;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestGetSensorAvailibility {
    
    private static Client client;
    
    @BeforeClass
    public static void init()
    {
        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
    }
    
    @Test
    public void getSensorAvailibility()
    {
        WebResource webResource = client.resource("http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/s1-fas");
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("request", "GetSensorAvailibility");
        
        queryParams.add("service", "eosps");
        queryParams.add("version", "2.0");
        queryParams.add("acceptFormat", "application/xml");
        queryParams.add("procedure", "s1-sar");
        queryParams.add("begin", "2013-06-30T12:00:00Z");
        queryParams.add("end", "2013-07-20T12:00:00Z");
        
        String s = webResource.queryParams(queryParams).accept("text/xml").get(String.class);
        
        System.out.println(s);
    }
}
