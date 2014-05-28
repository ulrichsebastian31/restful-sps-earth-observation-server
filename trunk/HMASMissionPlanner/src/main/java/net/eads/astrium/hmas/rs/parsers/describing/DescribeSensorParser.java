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
package net.eads.astrium.hmas.rs.parsers.describing;

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.rs.parsers.DreamRestEOSPSRequestGetParser;
import net.eads.astrium.hmas.rs.parsers.ExtensibleRequestParser;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.swes.x21.DescribeSensorType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class DescribeSensorParser extends DreamRestEOSPSRequestGetParser{
    
    @Override
    public DescribeSensorDocument createXMLFromGetRequest( MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        String procedure = params.getFirst("procedure");
        String procedureDescriptionFormat = params.getFirst("procedureDescriptionFormat");
        
        //Create XML request
        DescribeSensorDocument doc = DescribeSensorDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        DescribeSensorType describeSensor = doc.addNewDescribeSensor();
        
        //Add extensibleRequest parameters
        describeSensor.setService(extensibleRequest.getService());
        describeSensor.setVersion(extensibleRequest.getVersion());
        describeSensor.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        //Add DescribeSensorDocument parameters
        describeSensor.setProcedureDescriptionFormat(procedureDescriptionFormat);
        describeSensor.setProcedure(procedure);
        
        //TODO: valid time
        
        return doc;
    }
}
