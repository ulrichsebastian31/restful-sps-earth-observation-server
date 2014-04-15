/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetStationAvailibilityParser.java
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
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class GetStationAvailibilityParser extends DreamRestEOSPSRequestGetParser{
    
    @Override
    public GetStationAvailabilityDocument createXMLFromGetRequest(MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses GetSensorAvailabilityDocument parameters
        String station = params.getFirst("station");
        String begin = params.getFirst("begin");
        String end = params.getFirst("end");
        
        //Create XML request
        GetStationAvailabilityDocument doc = GetStationAvailabilityDocument.Factory.newInstance();
        GetStationAvailabilityType desc = doc.addNewGetStationAvailability();
        
        //Add extensibleRequest parameters
        desc.setService(extensibleRequest.getService());
        desc.setVersion(extensibleRequest.getVersion());
        desc.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        //Add GetSensorAvailabilityDocument parameters
        desc.setStation(station);
        TimePeriodType timePeriod = desc.addNewRequestPeriod().addNewTimePeriod();
        timePeriod.addNewBeginPosition().setStringValue(begin);
        timePeriod.addNewEndPosition().setStringValue(end);
        
        return doc;
    }
}
