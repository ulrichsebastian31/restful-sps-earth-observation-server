/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetStationAvailabilityOperation.java
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
 */package net.eads.astrium.hmas.mp.operations.describing;

import java.sql.SQLException;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import java.text.ParseException;
import java.util.List;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.exceptions.GetStationAvailabilityFault;

import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseType;
import net.opengis.eosps.x20.GetStationAvailabilityResponseType.ResponsePeriod;
import net.opengis.eosps.x20.GetStationAvailabilityType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;

/**
 * @file GetStationAvailabilityOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 *
 * @section LICENSE
 *
 * To be defined
 *
 * @section DESCRIPTION
 *
 * The GetStationAvailability Operation indicates if a station is available at a
 * given time period.
 */
public class GetStationAvailabilityOperation extends EOSPSOperation<MissionPlannerDBHandler, GetStationAvailabilityDocument, GetStationAvailabilityResponseDocument, GetStationAvailabilityFault> {

    /**
     *
     * @param request
     */
    public GetStationAvailabilityOperation(MissionPlannerDBHandler loader, GetStationAvailabilityDocument request) {

        super(loader,request);
    }

    @Override
    public void validRequest() throws GetStationAvailabilityFault {
    }

    @Override
    public void executeRequest() throws GetStationAvailabilityFault {

        GetStationAvailabilityType req = this.getRequest().getGetStationAvailability();
        String station = req.getStation();
        TimePeriodType period = req.getRequestPeriod().getTimePeriod();
        String beginRequest = period.getBeginPosition().getStringValue();
        String endRequest = period.getEndPosition().getStringValue();

        //Gets the list of availibilities from the database
        List<TimePeriod> availibilities = null;
        try {
            availibilities = this.getConfigurationLoader().getUnavailibilityHandler()
                    .getStationAvailibilities(station, beginRequest, endRequest);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new GetStationAvailabilityFault("SQLException : " + ex.getMessage());

        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new GetStationAvailabilityFault("ParseException : " + ex.getMessage());
        }



        GetStationAvailabilityResponseDocument responseDocument =
                GetStationAvailabilityResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        GetStationAvailabilityResponseType response =
                responseDocument.addNewGetStationAvailabilityResponse();



        //--------------------------------------------------------------
        //Start setting responsePeriod
        //--------------------------------------------------------------
        ResponsePeriod responsePeriod = response.addNewResponsePeriod();

        TimePeriodType responseTimePeriod = responsePeriod.addNewTimePeriod();

        TimePositionType responseTimePeriodBegin = responseTimePeriod.addNewBeginPosition();
        TimePositionType responseTimePeriodEnd = responseTimePeriod.addNewEndPosition();

        responseTimePeriodBegin.setStringValue(beginRequest);
        responseTimePeriodEnd.setStringValue(endRequest);

        for (TimePeriod anAvailibility : availibilities)
        {
            TimePeriodType tp = response.addNewAvailabilityPeriod().addNewTimePeriod();
            
            tp.addNewBeginPosition().setStringValue(DateHandler.formatDate(anAvailibility.getBegin()));
            tp.addNewEndPosition().setStringValue(DateHandler.formatDate(anAvailibility.getEnd()));
        }

        this.setResponse(responseDocument);
    }
}
