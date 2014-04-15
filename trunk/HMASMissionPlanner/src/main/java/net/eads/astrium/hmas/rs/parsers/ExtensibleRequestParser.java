/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ExtensibleRequestParser.java
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
package net.eads.astrium.hmas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;

import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class ExtensibleRequestParser {
    
    public static ExtensibleRequestDocument getExtensibleRequest(MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        String version = params.getFirst("version");
        String service = params.getFirst("service");
        String acceptFormat = params.getFirst("acceptFormat");
        
        if (service == null || service.equals("")) {
            service = "RESET";
        }
        if (version == null || version.equals("")) {
            version = "1.0.0";
        }
        if (acceptFormat == null || acceptFormat.equals("")) {
            acceptFormat = "text/xml";
        }
        
        ExtensibleRequestDocument doc = ExtensibleRequestDocument.Factory.newInstance();
        ExtensibleRequestType extensibleRequest = doc.addNewExtensibleRequest();
        
        extensibleRequest.setVersion(version);
        extensibleRequest.setAcceptFormat(acceptFormat);
        
        if (service != null)
            extensibleRequest.setService(service);
        else
        {
            if (acceptFormat != null)
                throw new MissingParameterException(
                        "Parameter service is missing. This parameter must be set.", 
                        acceptFormat
                    );
            else
                throw new MissingParameterException(
                        "Parameter service is missing. This parameter must be set."
                    );
        }
        
        return doc;
    }
}
