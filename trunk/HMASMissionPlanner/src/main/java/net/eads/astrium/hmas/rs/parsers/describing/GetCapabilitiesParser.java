/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilitiesParser.java
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
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.ows.x11.AcceptFormatsType;
import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.GetCapabilitiesType;

/**
 *
 * @author re-sulrich
 */
public class GetCapabilitiesParser {
    
    public static GetCapabilitiesDocument createXMLGetCapabilities( MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        String service = params.getFirst("service");
        List<String> requestFormats = params.get("acceptFormat");
        List<String> requestVersions = params.get("acceptVersion");
        
        GetCapabilitiesDocument doc = GetCapabilitiesDocument.Factory.newInstance();
        GetCapabilitiesType getCapabilities = doc.addNewGetCapabilities2();
        
        AcceptFormatsType formats = getCapabilities.addNewAcceptFormats();
        if (requestFormats != null && !requestFormats.isEmpty())
        for (String aFormat : requestFormats)
            formats.addOutputFormat(aFormat);
        
        AcceptVersionsType versions = getCapabilities.addNewAcceptVersions();
        if (requestVersions != null && !requestVersions.isEmpty())
        for (String aVersion : requestVersions)
            versions.addVersion(aVersion);
        
        
        
        if (service != null)
            getCapabilities.setService(service);
        else
        {
            if (formats.sizeOfOutputFormatArray() > 0)
                throw new MissingParameterException(
                        "Parameter service is missing. This parameter must be set.", 
                        formats.getOutputFormatArray()
                    );
            else
                throw new MissingParameterException(
                        "Parameter service is missing. This parameter must be set."
                    );
            
        }
        return doc;
    }
}
