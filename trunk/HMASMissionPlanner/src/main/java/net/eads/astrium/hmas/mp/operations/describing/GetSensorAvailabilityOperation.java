/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetSensorAvailabilityOperation.java
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
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.exceptions.GetSensorAvailabilityFault;

import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseType;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseType.ResponsePeriod;
import net.opengis.eosps.x20.GetSensorAvailabilityType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;

/**
 * @file GetSensorAvailabilityOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 *
 * @section LICENSE
 *
 * To be defined
 *
 * @section DESCRIPTION
 *
 * The GetSensorAvailability Operation indicates if the sensor is available at a
 * given time period.
 */
public class GetSensorAvailabilityOperation extends EOSPSOperation<MissionPlannerDBHandler,  GetSensorAvailabilityDocument, GetSensorAvailabilityResponseDocument, GetSensorAvailabilityFault> {

    /**
     *
     * @param request
     */
    public GetSensorAvailabilityOperation(MissionPlannerDBHandler loader, GetSensorAvailabilityDocument request) {

        super(loader, request);
    }

    @Override
    public void validRequest() throws GetSensorAvailabilityFault {
    }

    @Override
    public void executeRequest() throws GetSensorAvailabilityFault {
        GetSensorAvailabilityType req = this.getRequest().getGetSensorAvailability();
        String sensor = req.getProcedure();
        TimePeriodType period = req.getRequestPeriod().getTimePeriod();
        String beginRequest = period.getBeginPosition().getStringValue();
        String endRequest = period.getEndPosition().getStringValue();
        
        //Gets the list of availibilities from the database
        List<TimePeriod> availibilities = null;
        try {
            availibilities = this.getConfigurationLoader().getUnavailibilityHandler()
                                     .getSensorAvailibilities(sensor, beginRequest, endRequest);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new GetSensorAvailabilityFault("SQLException : " + ex.getMessage());
            
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new GetSensorAvailabilityFault("ParseException : " + ex.getMessage());
        }

        GetSensorAvailabilityResponseDocument responseDocument = GetSensorAvailabilityResponseDocument.Factory.newInstance();
        GetSensorAvailabilityResponseType response = responseDocument.addNewGetSensorAvailabilityResponse();

        //Complete response document
        

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
