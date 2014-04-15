/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestDescribeTasking.java
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
import org.junit.BeforeClass;
import org.junit.Test;



/**
 *
 * @author re-sulrich
 */
public class TestDescribeTasking {
    
    private static Client client;
    
    @BeforeClass
    public static void init()
    {
        client = Client.create();
    }
    
    public void describeTasking()
    {
        WebResource webResource = client.resource("http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/s1-fas");
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("service", "eosps");
        queryParams.add("version", "2.0");
        queryParams.add("request", "DescribeTasking");
        queryParams.add("acceptFormat", "application/xml");
        queryParams.add("procedure", "Sentinel1SAR");
        
        String s = webResource.queryParams(queryParams).accept("text/xml").get(String.class);
        
        System.out.println(s);
    }
    
    
    
    @Test
    public void testGetAtom()
    {
        
        WebResource webResource = client.resource("http://en.wikipedia.org/w/opensearch_desc.php");
        
//        WebResource webResource = client.resource("http://en.wikipedia.org/wiki/specials:Search");
//        
//        MultivaluedMap queryParams = new MultivaluedMapImpl();
//        
//        queryParams.add("search", "chien");
////        queryParams.add("version", "2.0");
////        queryParams.add("request", "DescribeTasking");
////        queryParams.add("acceptFormat", "application/xml");
////        queryParams.add("procedure", "Sentinel1SAR");
//        .queryParams(queryParams)
        String s = webResource.accept("application/opensearchdescription+xml").get(String.class);
        
        System.out.println(s);
    }
    
}
