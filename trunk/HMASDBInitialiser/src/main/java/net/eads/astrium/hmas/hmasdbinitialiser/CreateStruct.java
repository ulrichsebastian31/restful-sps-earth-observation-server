/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CreateStruct.java
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
package net.eads.astrium.hmas.hmasdbinitialiser;

import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author re-sulrich
 */
public class CreateStruct {
    
    public static void createMain() throws SQLException {
        
        
        
        System.out.println("Begin");
        
        TestConnexion.createEnum("BandTypes", Arrays.asList(new String[]{"'L-BAND'", "'C-BAND'", "'S-BAND'", "'X-BAND'"}));
        
        TestConnexion.createEnum("SensorTypes", Arrays.asList(new String[]{"'SAR'", "'OPT'"}));
        
        
        CreateSensorStruct.testCreatePolarisationModeTable();
        CreateSensorStruct.testCreateInstrumentModeTable();
        
        
        CreateSensorStruct.testCreateSatelliteTable();
        
        //Depends on Satellite
        CreateSensorStruct.testCreateSensorTable();
        //Depends on sensor
        CreateSensorStruct.testCreateSARSensorChar();
        CreateSensorStruct.testCreateOPTSensorChar();
        
        CreateSensorStruct.testCreateSDF();
        //Depends on sensor and InstrumentMode
        CreateSensorStruct.testCreateOPTIMChar();
        CreateSensorStruct.testCreateSARIMChar();
        
        //Depends on SARIM and Pol Mode
        CreateSensorStruct.testCreateIMPMTable();
        
        CreateSensorStruct.testCreateGroundStationTable();
        
        CreateSensorStruct.testCreateUnavailibilityTable();
        
        CreateReqTaskStruct.testCreateStatusTable();
        
        //Depends on Status and MMFASTask
        CreateReqTaskStruct.testCreateRequestTable();
        
        CreateReqTaskStruct.testCreateTaskingParametersTable();
        
        CreateReqTaskStruct.testCreateOPTParamsTable();
        CreateReqTaskStruct.testCreateSARParamsTable();
        
        CreateReqTaskStruct.testCreateLink_TP_InstModes();
        CreateReqTaskStruct.testCreateLink_TP_PolModes();
        
        
        CreateReqTaskStruct.testCreateSensorTaskTable();
        //Depends on SensorTask and Request
        CreateReqTaskStruct.testCreateLinkSensorTaskRequestTable();
        //Depends on Status and SensorTask and GroundStation
        CreateReqTaskStruct.testCreateSegmentTable();
        //Depends on Status and SensorTask
        CreateReqTaskStruct.testCreateResultTable();
        CreateReqTaskStruct.testCreateProductTable();
        //Depends on Segment
        CreateReqTaskStruct.testCreateDownlinkTable();
        
        CreateSensorStruct.testCreateOrbitTable();
        CreateSensorStruct.testCreateOSF();
        CreateSensorStruct.testCreateSatelliteOSFTable();
        
        CreateSensorStruct.testCreateLNK_Satellite_GroundStationTable();
    }
}
