/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeSensorParser.java
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

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.rs.parsers.DreamRestEOSPSRequestGetParser;
import net.eads.astrium.hmas.rs.parsers.ExtensibleRequestParser;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.DescribeResultAccessType;
import net.opengis.swes.x21.DescribeSensorType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class DescribeResultAccessParser extends DreamRestEOSPSRequestGetParser{
    
    @Override
    public DescribeResultAccessDocument createXMLFromGetRequest( MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        String procedure = params.getFirst("procedure");
        String mmfasTask = params.getFirst("task");
        
        //Create XML request
        DescribeResultAccessDocument doc = DescribeResultAccessDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        DescribeResultAccessType describeResultAccess = doc.addNewDescribeResultAccess();
        
        //Add extensibleRequest parameters
        describeResultAccess.setService(extensibleRequest.getService());
        describeResultAccess.setVersion(extensibleRequest.getVersion());
        describeResultAccess.setAcceptFormat(extensibleRequest.getAcceptFormat());
        //Add DescribeResultAccessDocument parameters
        DescribeResultAccessType.Target target = describeResultAccess.addNewTarget();
        target.setTask(mmfasTask);
        target.setProcedure(procedure);
        
        return doc;
    }
}
