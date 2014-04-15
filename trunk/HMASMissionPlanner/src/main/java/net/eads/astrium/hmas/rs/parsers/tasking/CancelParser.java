/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CancelParser.java
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
package net.eads.astrium.hmas.rs.parsers.tasking;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.parsers.DreamRestEOSPSRequestGetParser;
import net.eads.astrium.hmas.rs.parsers.ExtensibleRequestParser;
import net.opengis.eosps.x20.CancelDocument;
import net.opengis.sps.x21.CancelType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class CancelParser extends DreamRestEOSPSRequestGetParser{
    
    @Override
    public CancelDocument createXMLFromGetRequest(MultivaluedMap<String, String> params) {
        
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        String task = params.getFirst("task");
        
        //Create XML request
        CancelDocument doc = CancelDocument.Factory.newInstance();
        CancelType cancel = doc.addNewCancel();
        
        //Add extensibleRequest parameters
        cancel.setService(extensibleRequest.getService());
        cancel.setVersion(extensibleRequest.getVersion());
        cancel.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        cancel.setTask(task);
        
        //TODO: valid time
        
        return doc;
    }
    
}
