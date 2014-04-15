/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeTaskingParser.java
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
package net.eads.astrium.hmas.rs.parsers.describing;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.parsers.DreamRestEOSPSRequestGetParser;
import net.eads.astrium.hmas.rs.parsers.ExtensibleRequestParser;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.sps.x21.DescribeTaskingType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class DescribeTaskingParser extends DreamRestEOSPSRequestGetParser{
    
    public DescribeTaskingDocument createXMLFromGetRequest(MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeTaskingDocument parameters
        String procedure = params.getFirst("procedure");
        
        //Create XML request
        DescribeTaskingDocument doc = DescribeTaskingDocument.Factory.newInstance();
        DescribeTaskingType desc = doc.addNewDescribeTasking();
        
        //Add extensibleRequest parameters
        desc.setService(extensibleRequest.getService());
        desc.setVersion(extensibleRequest.getVersion());
        desc.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        //Add DescribeTaskingDocument parameters
        desc.setProcedure(procedure);
        
        return doc;
    }
}
        
