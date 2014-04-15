/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ConfirmParser.java
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
import net.opengis.eosps.x20.ConfirmDocument;
import net.opengis.sps.x21.ConfirmType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class ConfirmParser extends DreamRestEOSPSRequestGetParser{
    
    @Override
    public ConfirmDocument createXMLFromGetRequest(MultivaluedMap<String, String> params) {
        
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        String task = params.getFirst("task");
        
        //Create XML request
        ConfirmDocument doc = ConfirmDocument.Factory.newInstance();
        ConfirmType confirm = doc.addNewConfirm();
        
        //Add extensibleRequest parameters
        confirm.setService(extensibleRequest.getService());
        confirm.setVersion(extensibleRequest.getVersion());
        confirm.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        confirm.setTask(task);
        
        //TODO: valid time
        
        return doc;
    }
}