/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CreateReqTaskStruct.java
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public class CreateReqTaskStruct {
    
    
    public static void testCreateLink_TP_InstModes() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"params", "integer references TaskingParameters(tpId)"});
        fields.add(new String[]{"instrumentMode", "varchar(5) references InstrumentMode(imId)"});
        
        TestConnexion.create("TaskingParametersInstrumentModes", fields);
    }
    
    
    public static void testCreateLink_TP_PolModes() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"params", "integer references SARTaskingParameters(sarTPId)"});
        fields.add(new String[]{"polarisationMode", "varchar(5) references PolarisationMode(pmId)"});
        
        TestConnexion.create("SARTaskingParametersPolarisationModes", fields);
    }
    
    
    public static void testCreateOPTParamsTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"optTPId", "serial primary key"});
        fields.add(new String[]{"minLuminosity", "integer"});
        fields.add(new String[]{"fusionAccepted", "char(5)"});
        fields.add(new String[]{"maxCloudCover", "smallint"});
        fields.add(new String[]{"maxSnowCover", "smallint"});
        fields.add(new String[]{"maxSunGlint", "smallint"});
        fields.add(new String[]{"hazeAccepted", "char(5)"});
        fields.add(new String[]{"sandWindAccepted", "char(5)"});
        
        fields.add(new String[]{"params", "integer references TaskingParameters(tpId)"});
        
        TestConnexion.create("OPTTaskingParameters", fields);
    }
    
    public static void testCreateSARParamsTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sarTPId", "serial primary key"});
        fields.add(new String[]{"fusionAccepted", "char(5)"});
        fields.add(new String[]{"maxNoiseLevel", "char(20)"});
        fields.add(new String[]{"maxAmbiguityLevel", "char(20)"});
        
        fields.add(new String[]{"params", "integer references TaskingParameters(tpId)"});
        
        TestConnexion.create("SARTaskingParameters", fields);
    }
    
    
    public static void testCreateTaskingParametersTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"tpId", "serial primary key"});
        
        fields.add(new String[]{"incidenceAngleAzimuth", "varchar(50)"});
        fields.add(new String[]{"incidenceAngleElevation", "varchar(50)"});
        fields.add(new String[]{"pointingAngleAcross", "varchar(50)"});
        fields.add(new String[]{"pointingAngleAlong", "varchar(50)"});
        fields.add(new String[]{"coordinates", "varchar(1024)"});
        fields.add(new String[]{"roiType", "varchar(50)"});
        fields.add(new String[]{"beginTime", "timestamp"});
        fields.add(new String[]{"endTime", "timestamp"});
        fields.add(new String[]{"groundResolution", "varchar(50)"});
        
        //Foreign keys
        fields.add(new String[]{"request", "integer references Request(requestId)"});
        
        TestConnexion.create("TaskingParameters", fields);
    }
    
    public static void testCreateStatusTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"statusId", "serial primary key"});
        fields.add(new String[]{"percentCompletion", "integer"});
        fields.add(new String[]{"statusMessage", "varchar(1024)"});
        fields.add(new String[]{"updateTime", "timestamp"});
        fields.add(new String[]{"estimatedTOC", "timestamp"});
        fields.add(new String[]{"statusIdentifier", "varchar(30)"});
        
        //Foreign keys
        fields.add(new String[]{"previous", "integer references Status(statusId)"});
        
        TestConnexion.create("Status", fields);
    }
    
    public static void testCreateRequestTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"requestId", "serial primary key"});
        fields.add(new String[]{"type", "varchar(20)"});
        fields.add(new String[]{"incomingTime", "timestamp"});
        //Foreign keys
        fields.add(new String[]{"status", "integer references Status(statusId)"});
        //Now done by a link table
//        fields.add(new String[]{"mmfasTask", "integer references MMFASTask(taskId)"});
        
        TestConnexion.create("Request", fields);
    }
    
    public static void testCreateSensorTaskTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"taskId", "serial primary key"});
        fields.add(new String[]{"type", "varchar(15)"});
        
        //Foreign keys
        fields.add(new String[]{"feasibility", "integer references SensorTask(taskId)"});
        fields.add(new String[]{"sensor", "varchar(10) references Sensor(sensorId)"});
        fields.add(new String[]{"status", "integer references Status(statusId)"});
        
        
        TestConnexion.create("SensorTask", fields);
    }
    
    public static void testCreateLinkSensorTaskRequestTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();

        fields.add(new String[]{"sensorTask", "integer references SensorTask(taskId)"});
        fields.add(new String[]{"request", "integer references Request(requestId)"});
        
        TestConnexion.create("LNK_SensorTask_Request", fields);
    }
    
    public static void testCreateSegmentTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"segmentId", "serial primary key"});
        fields.add(new String[]{"coordinates", "varchar(1024)"});
        fields.add(new String[]{"acquisitionStartTime", "timestamp"});
        fields.add(new String[]{"acquisitionStopTime", "timestamp"});
        
        fields.add(new String[]{"startOrbit", "bigint"});
        fields.add(new String[]{"stopOrbit", "bigint"});
        
        fields.add(new String[]{"cloudCoverageSuccessRate", "double precision"});
        fields.add(new String[]{"estimatedCost", "double precision"});
        
        //Foreign keys
        fields.add(new String[]{"instrumentMode", "varchar(5) references InstrumentMode(imId)"});
        
        fields.add(new String[]{"feasibility", "integer references SensorTask(taskId)"});
        fields.add(new String[]{"planning", "integer references SensorTask(taskId)"});
        fields.add(new String[]{"status", "integer references Status(statusId)"});
        
        TestConnexion.create("Segment", fields);
    }
    
    public static void testCreateResultTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"resultId", "serial primary key"});
        fields.add(new String[]{"downloadURL", "varchar(50)"});
        fields.add(new String[]{"serverType", "varchar(50)"});
        fields.add(new String[]{"serverUser", "varchar(50)"});
        fields.add(new String[]{"serverPassword", "varchar(50)"});
        fields.add(new String[]{"size", "varchar(50)"});
        fields.add(new String[]{"fileType", "varchar(50)"});
        
        //Foreign keys
        fields.add(new String[]{"task", "integer references SensorTask(taskId)"});
        
        TestConnexion.create("Result", fields);
    }
    
    /**
     * Downlink : link between Segment and Ground Station with start and end of visibility
     * @throws SQLException 
     */
    public static void testCreateDownlinkTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"beginTime", "timestamp"});
        fields.add(new String[]{"endTime", "timestamp"});
        
        //Foreign keys
        fields.add(new String[]{"downlinkStation", "char(8) references GroundStation(groundStationId)"});
        fields.add(new String[]{"segment", "integer references Segment(segmentId)"});
        
        TestConnexion.create("Downlink", fields);
    }
}
