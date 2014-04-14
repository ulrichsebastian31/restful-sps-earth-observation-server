/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DBHandlerTests.java
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.dbhandler.tasking.SensorFeasibilityHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorTaskHandler;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.Sensor;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class DBHandlerTests {
    
    
    public static String url;
    public static String user;
    public static String pass;
    public static String schema;
    private static SatelliteLoader loader;
    @BeforeClass
    public static void init()
    {
//        url = "jdbc:postgresql://10.2.200.247:5432/HMASDatabase";
//        url = "jdbc:postgresql://192.168.0.20:5432/HMASDatabase";
        url = "jdbc:postgresql://127.0.0.1:5432/HMASDatabase";
//        user= "opensourcedbms";
//        pass = "opensourcedbms";
        user= "postgres";
        pass = "postgres";
        schema = "MissionPlannerDatabase";
        loader = new SatelliteLoader("00001AAA", url, user, pass, schema);
    }
    
    
//    @Test
    public void testGetTaskSensorType() throws SQLException {
        System.out.println("Sensor type : " + new SensorTaskHandler(loader.getDboperations()).getSensorType("1"));
    }
    
    public void testGetSensorType() throws SQLException {
        String sens = loader.getSensorType("S1ASAR");
        
        System.out.println("" + sens);
    }
    
//    @Test
    public void getSegments() throws SQLException, ParseException {
        List<Segment> segments = new SensorFeasibilityHandler(url, user, pass, schema).getSegments("8");
        
        for (Segment segment : segments) {
            System.out.println("" + segment);
        }
    }
    
//    @Test
    public void loadSensorsByType() throws SQLException {
        String sensorType = "SAR";
        List<Sensor> sensors = loader.loadSensorsByType(sensorType);
        
        for (Sensor sensor : sensors) {
            System.out.println("" + sensor);
            System.out.println("");
        }
    }
    
    
//    @Test
    public void test() throws SQLException
    {
        try {
            Date d = DateHandler.parseBDDDate("2013-08-12 00:00:00");
            
            System.out.println("" + d);
        } catch (ParseException ex) {
            Logger.getLogger(DBHandlerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /*
    
    
//    @Test
    public void loadPMs() throws SQLException {
        String sensorId = "3";
        
        List<String> pms = loader.getPolarisationModes(sensorId);
        
        for (String string : pms) {
            System.out.println("" + string);
        }
    }
//    @Test
    public void loadIMs() throws SQLException {
        String sensorId = "3";
        
        List<String> ims = loader.getInstrumentModes(sensorId);
        
        for (String string : ims) {
            System.out.println("" + string);
        }
    }
    
//    @Test
    public void testCount() throws SQLException {
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("sensorId='3'");
        int nb = new DBOperations(url).count("sensor", conditions);
        
        System.out.println("" + nb);
    }
    
//    @Test
    public void getSensorUnavailibilities() throws SQLException
    {
        List<TimePeriod> unavailibilities = loader.getSensorUnavailibilities("1", "2013-08-01T00:00:00Z", "2013-08-17T00:00:00Z");
        
        for (TimePeriod timePeriod : unavailibilities) {
            
            System.out.println("Sensor unavailibility : \nBegin : " + timePeriod.getBegin() + "\nend : " + timePeriod.getEnd());
        }
    }
//    @Test
    public void getStationUnavailibilities() throws SQLException
    {
        List<TimePeriod> unavailibilities = loader.getStationUnavailibilities("1", "2013-08-01T00:00:00Z", "2013-08-17T00:00:00Z");
        
        for (TimePeriod timePeriod : unavailibilities) {
            
            System.out.println("Station unavailibility : \nBegin : " + timePeriod.getBegin() + "\nend : " + timePeriod.getEnd());
        }
    }
    
    */
//    @Test
    public void getSensorAvailibilities() throws SQLException, ParseException {
        
        List<TimePeriod> availibilities = 
                new UnavailibilityHandler(url, user, pass, schema)
                .getSensorAvailibilities("1", "2013-08-01T00:00:00Z", "2013-08-17T00:00:00Z");
        
        for (TimePeriod timePeriod : availibilities) {
            
            System.out.println("Sensor availibility : \nBegin : " + timePeriod.getBegin() + "\nend : " + timePeriod.getEnd());
        }
    }
//    @Test
    public void getStationAvailibilities() throws SQLException, ParseException {
        
        List<TimePeriod> availibilities = 
                new UnavailibilityHandler(url, user, pass, schema)
                .getStationAvailibilities("1", "2013-08-01T00:00:00Z", "2013-08-17T00:00:00Z");
        
        for (TimePeriod timePeriod : availibilities) {
            
            System.out.println("Station availibility : \nBegin : " + timePeriod.getBegin() + "\nend : " + timePeriod.getEnd());
        }
    }
    
    
    
    
    
    
//    @Test
    public void loadSensors() throws SQLException {
        String sensorId = "1";
        Sensor sensor = loader.loadSensor(sensorId);
        
        System.out.println("" + sensor);
        System.out.println("");
        
    }
    
//    @Test
    public void getInstrumentModes() throws SQLException {
        
        DBOperations dbOperations = new DBOperations(url, user, pass, schema);
        String sensorId = "1";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("imId");
        fields.add("name");
        fields.add("description");
        
        fields.add("maxPowerConsumption");
        fields.add("minAcrossTrackAngle");
        fields.add("maxAcrossTrackAngle");
        fields.add("minAlongTrackAngle");
        fields.add("maxAlongTrackAngle");
        fields.add("swathWidth");
        fields.add("acrossTrackResolution");
        fields.add("alongTrackResolution");
        fields.add("radiometricResolution");
        fields.add("minRadioStab");
        fields.add("maxRadioStab");
        fields.add("minAlongTrackAmbRatio");
        fields.add("maxAlongTrackAmbRatio");
        fields.add("minAcrossTrackAmbRatio");
        fields.add("maxAcrossTrackAmbRatio");
        fields.add("minNoiseEquivalentSO");
        fields.add("maxNoiseEquivalentSO");
        
        String table = "InstrumentMode, SARIMCharacteristics";
        List<String> conditions = new ArrayList<String>();
        conditions.add("InstrumentMode.imId in (SELECT mode FROM LNK_Sensor_IM WHERE sensor = '" + sensorId + "')");
        conditions.add("SARIMCharacteristics.instMode = InstrumentMode.imId");

        List<List<String>> instrumentModes = dbOperations.select(fields, table, conditions);

    }
}
