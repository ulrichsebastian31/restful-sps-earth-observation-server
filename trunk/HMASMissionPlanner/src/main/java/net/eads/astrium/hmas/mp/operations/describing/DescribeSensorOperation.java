/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeSensorOperation.java
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
import net.eads.astrium.hmas.processes.sensormlgenerator.SensorMLGenerator;
import net.eads.astrium.hmas.exceptions.DescribeSensorFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;

import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.sensorML.x102.SensorMLDocument.SensorML;
import net.opengis.swes.x21.DescribeSensorResponseType;
import net.opengis.swes.x21.DescribeSensorType;
import net.opengis.swes.x21.SensorDescriptionType;

/**
 * @file DescribeSensorOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 *
 * @section LICENSE
 *
 * To be defined
 *
 * @section DESCRIPTION
 *
 * The DescribeSensor Operation gives information about the way the sensor can
 * be tasked.
 *
 * This class is not currently working as some part of the response creation are
 * not yet available.
 *
 * Plus, a lot of parameters should be found in a config file and not
 * hard-coded.
 */
public class DescribeSensorOperation extends EOSPSOperation<MissionPlannerDBHandler, DescribeSensorDocument, DescribeSensorResponseDocument, DescribeSensorFault> {

    /**
     *
     * @param request
     */
    public DescribeSensorOperation(MissionPlannerDBHandler loader, DescribeSensorDocument request) {

        super(loader,request);
    }

    @Override
    public void validRequest() throws DescribeSensorFault {
    }

    @Override
    public void executeRequest() throws DescribeSensorFault {

        this.validRequest();

        DescribeSensorType req = this.getRequest().getDescribeSensor();
        
        String procedure = req.getProcedure();
        String[] ids = req.getProcedure().split("::");
        String instrumentId = ids[0];
        
        String instrumentModeId = null;
        if (ids.length > 1)
            instrumentModeId = ids[1];
        
        DescribeSensorResponseDocument responseDocument = DescribeSensorResponseDocument.Factory.newInstance();

        DescribeSensorResponseType resp = responseDocument.addNewDescribeSensorResponse2();
        resp.setProcedureDescriptionFormat("http://www.opengis.net/sensorml/1.0.2");
        
        /**
         * New version : create structures and fill in with database data
         */
        SensorDescriptionType sensor = resp.addNewDescription().addNewSensorDescription();
        SensorDescriptionType.Data data = sensor.addNewData();
        
        SensorML sensorML = null;
        
        try {
            SensorMLGenerator generator = new SensorMLGenerator(this.getConfigurationLoader().getSatelliteLoader(), instrumentId);
            if (instrumentModeId != null) {

                sensorML = generator.createInstrumentModeSensorML102Description(instrumentModeId);
            }
            else
            {
                sensorML = generator.createSensorML102Description();
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            
            throw new DescribeSensorFault("SQLException loading sensor description: " + ex.getMessage());
        }
        
        data.set(sensorML);
        
        this.setResponse(responseDocument);
    }
}
