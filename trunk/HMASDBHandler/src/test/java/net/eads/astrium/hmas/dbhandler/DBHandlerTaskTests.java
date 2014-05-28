/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DBHandlerTaskTests.java
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
package net.eads.astrium.hmas.dbhandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.eads.astrium.hmas.dbhandler.tasking.RequestHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorFeasibilityHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.EarthObservationEquipment;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class DBHandlerTaskTests {
    
    
//    public static String url = "jdbc:postgresql://10.2.200.247:5432/HMASDatabase";
//    public static String url = "jdbc:postgresql://192.168.0.20:5432/HMASDatabase";
    public static String url = "jdbc:postgresql://127.0.0.1:5432/HMASDatabase";
    public static String user = "postgres";
    public static String passwd = "postgres";
    public static String schema = "MissionPlannerDatabase";

    public static final String testMMFASUser = "f9565cf6dba885cf5e25150f936fd9c1";

    
    @Test
    public void test() throws SQLException, ParseException {
//        testAddSentinel3();
//        addSentinel3Sensor();
//        testAddExternalFAS();
//        testGetFAS();
//        testGetSARRequest();
//        testGetRequestType();
//        testCreateMMFASTask();
//        testGetSensorTasks();
//        testGetSegmentEOE();
//        testGetMPBaseAddr();
    }
    
    public void testGetMPBaseAddr() throws SQLException {
        System.out.println("" + url);
        System.out.println("MP base addr : " + new SensorPlanningHandler(url, user, passwd,schema).
                getSensorTaskMPBaseAddress("1"));
    }
    
    public void testGetTaskSensorType() throws SQLException {
        System.out.println("Sensor Type : " + new SensorPlanningHandler(url, user, passwd,schema).getSensorType("4"));
    }
    
    public void testGetMMFASRequest() throws SQLException, ParseException {
        SARTaskingRequest req = new SensorFeasibilityHandler(url, user, passwd,schema).
                getSARRequest(TaskHandlerType.mmfas, "", "");
        
        System.out.println("" + req.toString());
        
    }
    
    
    
    public void testGetSegmentEOE() throws SQLException {
        
        for (int i = 0; i < 10; i++) {
            
            EarthObservationEquipment eoe = new SensorFeasibilityHandler(url, user, passwd,schema).getSegmentEarthObservationEquipment("60");

            System.out.println("" + eoe);
        }
    }
    
    
    
    public void testGetRequestType() throws SQLException {
        
        RequestHandler handler = new RequestHandler(url, user, passwd,schema);
        String type = handler.getRequestSensorType("4");
        
        System.out.println("" + type.toString());
    }
    
    
//    @Test
    public void testGetSARRequest() throws SQLException, ParseException {
        
        RequestHandler handler = new RequestHandler(url, user, passwd,schema);
        SARTaskingRequest tp = handler.getSARRequest(TaskHandlerType.mmfas, "5", null);
        
        System.out.println("" + tp.toString());
    }
    
//    @Test
    public void testGetRequest() throws SQLException {
        
        String table = "Request";
        
        List<String> fields = new ArrayList<String>();
        fields.add("requestId");
        fields.add("incomingTime");
        fields.add("type");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("incomingTime = (SELECT max(incomingTime) FROM Request)");
        
        List<List<String>> result = new DBOperations(url, user, passwd,schema).select(fields, table, conditions);
        List<String> list = result.get(0);
        
        System.out.println("" + list.get(0) + " : " + list.get(1));
    }
    
    
    
//    @Test
    public void testInsertTask() throws SQLException, ParseException {
        
//        SensorTaskHandler handler = new SensorTaskHandler(url, user, passwd,schema, "S1SAR");
//        String taskId = handler.createSensorTask(TaskType.feasibility);
        
        SARTaskingParameters parameters = new SARTaskingParameters(
                0.0, 0.0, true, 
                Arrays.asList(new String[]{"HH", "VV"}), 
                -90.0 , 90.0, -90.0 , 90.0, -90.0 , 90.0, -90.0 , 90.0,  
                new Polygon(
                    Arrays.asList(new Point[]{
                        new Point(133.726,33.211, 0.0), 
                        new Point(133.726,43.485, 0.0), 
                        new Point(123.706,43.485, 0.0), 
                        new Point(123.706,33.211, 0.0), 
                        new Point(133.726,33.211, 0.0)})), 
                "POLYGON", 
                Arrays.asList(new TimePeriod[] {new TimePeriod(DateHandler.parseDate("2013-03-03T00:00:00Z"), DateHandler.parseDate("2013-03-05T00:00:00Z"))} ),
                 
                5, 5, Arrays.asList(new String[]{"WV", "SM"}));
        
        SensorPlanningHandler handler = new SensorPlanningHandler(url, user, passwd,schema);
        
        String taskId = handler.createSARPlanningTask("S1SAR", parameters);
        
        System.out.println("Task : " + taskId);
        
        //        RequestHandler reqHandler = new RequestHandler(url, user, passwd,schema);
        //        String reqId = reqHandler.saveSARTaskingRequest(TaskHandlerType.sensor, taskId, RequestType.getFeasibility, parameters);
        //
        //        System.out.println("Task : " + taskId + "\nRequest : " + reqId);
    
        
    }
    
//    @Test
    public void testAddSentinel3() throws SQLException {
        
        String table = "SatellitePlatform";
        
        List<String> fields = new ArrayList<String>();
      
        fields.add("satelliteId");
        fields.add("name");
        fields.add("description");
        fields.add("orbittype");
        fields.add("href");


        List<String> sentinel3 = new ArrayList<String>();
        sentinel3.add("'Sentinel3'");
        sentinel3.add("'Sentinel3'");
        sentinel3.add("'Sentinel 3 is an atmospheric satellite part of the Sentinel project.'");
        sentinel3.add("'SSO'");
        sentinel3.add("'http://www.esa.int/Our_Activities/Observing_the_Earth/GMES/Sentinel-3'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel3);
        
        new DBOperations(url, user, passwd,schema).insert(
                table, 
                fields, 
                values);
    }
    
    
//    @Test
    public void addSentinel3Sensor() throws SQLException {
       
        String table = "Sensor";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("sensorId");
        fields.add("name");
        fields.add("sensorDescription");
        fields.add("type");
        fields.add("bandType");
        fields.add("minLatitude");
        fields.add("maxLatitude");
        fields.add("minLongitude");
        fields.add("maxLongitude");
        
        fields.add("mass");
        fields.add("maxPowerConsumption");
        fields.add("acqMethod");
        fields.add("applications");
        
        fields.add("platform");


        List<String> sentinel3 = new ArrayList<String>();
        sentinel3.add("'SARL'");
        sentinel3.add("'Sentinel 3 SAR Altimetric'");
        sentinel3.add("'Sentinel 3 is an Earth Observation satellite mission developed by the ESA as part of the Global Monitoring for Environment and Security GMES program. GMES is the European programme to establish a European capacity for Earth Observation designed to provide European policy makers and public authorities with accurate and timely information to better manage the environment, understand and mitigate the effects of climate change and ensure civil security.'");
        sentinel3.add("'SAR'");
        sentinel3.add("'C-BAND'");
        sentinel3.add("-90");
        sentinel3.add("90");
        sentinel3.add("-180");
        sentinel3.add("180");
        
        sentinel3.add("880");
        sentinel3.add("4368");
        sentinel3.add("'C-band SAR'");
        sentinel3.add("'C-band SAR all-weather imaging capability'");
        
        sentinel3.add("'Sentinel3'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel3);
        
        new DBOperations(url, user, passwd,schema).insert(
                table, 
                fields, 
                values);
    }
    
    
//    @Test
    public void testAddExternalFAS() throws SQLException {
        
        String table = "FAS";
        
        List<String> fields = new ArrayList<String>();
      
        fields.add("fasId");
        fields.add("name");
        fields.add("description");
        
        fields.add("externalFAS");
        fields.add("externalHref");
        
        fields.add("platform");
        
        
        List<String> gmess3 = new ArrayList<String>();
        gmess3.add("'s3-fas'");
        gmess3.add("'FAS Sentinel 3'");
        gmess3.add("'This server computes Feasibility Analysis for the sensors of the Sentinel 3 platform.'");
        
        //External FAS ?
        gmess3.add("TRUE");
        gmess3.add("83.142.234.45:8080");
        
        gmess3.add("'Sentinel3'");
        
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(gmess3);
        
        new DBOperations(url, user, passwd,schema).insert(table, fields, values);
    }
}
