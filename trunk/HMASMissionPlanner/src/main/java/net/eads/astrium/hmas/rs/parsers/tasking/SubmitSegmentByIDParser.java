/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SubmitSegmentByIDParser.java
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

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.parsers.DreamRestEOSPSRequestGetParser;
import net.eads.astrium.hmas.rs.parsers.ExtensibleRequestParser;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class SubmitSegmentByIDParser extends DreamRestEOSPSRequestGetParser{
    
    @Override
    public SubmitSegmentByIDDocument createXMLFromGetRequest(MultivaluedMap<String, String> params) {
        
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        String task = params.getFirst("task");
        List<String> segments = Arrays.asList(params.getFirst("segment").split(","));
        
        //Create XML request
        SubmitSegmentByIDDocument doc = SubmitSegmentByIDDocument.Factory.newInstance();
        SubmitSegmentByIDType submitSegmentByID = doc.addNewSubmitSegmentByID();
        
        //Add extensibleRequest parameters
        submitSegmentByID.setService(extensibleRequest.getService());
        submitSegmentByID.setVersion(extensibleRequest.getVersion());
        submitSegmentByID.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        submitSegmentByID.setTask(task);
        
        for (String segment : segments) {
            
            submitSegmentByID.addSegmentID(segment);
        }
        
        String mmfasTask = params.getFirst("mmfasTask");
        if (mmfasTask != null && !mmfasTask.equals("")) {
            CategoryType str = CategoryType.Factory.newInstance();
            str.setDefinition("mmfasTask");
            str.setValue(mmfasTask);
            submitSegmentByID.addNewExtension().set(str);
        }
        
        return doc;
    }
}
