/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorLoader.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.eads.astrium.hmas.util.Constants;
import net.eads.astrium.hmas.util.structures.Sensor;
import net.eads.astrium.hmas.util.structures.OPTSensor;
import net.eads.astrium.hmas.util.structures.SARSensor;

/**
 *
 * @author re-sulrich
 */
public abstract class SensorLoader extends DatabaseLoader {
    
//    private String condition;


    private String id;
    private ConditionType conditionType;
    
    public enum ConditionType { mmfas, satellite };
    
    /**
     * Generic constructor
     * Permits to create a SensorLoader that will load the sensors (and characteristics)
     * that fulfill the following condition : 
     *  - if MMFAS : sensors which platform is in the "id" MMFAS' platforms
     *  - if FAS (Satellite) : sensors which platform is the "id"
     * @param type
     * @param id 
     */
    public SensorLoader(ConditionType type, String id) {
        
        super("MissionPlannerDatabase");
        this.id = id;
        this.conditionType = type;
    }
    
    public SensorLoader(ConditionType type, String id, DBOperations dboperations) {
        
        super(dboperations);
        this.id = id;
        this.conditionType = type;
    }
    
    public SensorLoader(ConditionType type, String id, String databaseURL, String user, String pass, String schemaName) {
        
        super(databaseURL, user, pass, schemaName);
        
        this.id = id;
        this.conditionType = type;
    }
    
    public List<String> getSensorsIds() throws SQLException {
        
        return getSensorsIds(null);
    }
    
    public List<String> getSensorsIds(String sensorType) throws SQLException {
        
        List<String> sensors = new ArrayList<String>();
        
        String table = "Sensor";
        
        List<String> fields = new ArrayList<String>();
        fields.add("sensorId");
        
        //Filtering the DB results by platform and sensorType if not null
        List<String> conditions = new ArrayList<String>();
        conditions.add(this.getCondition("satelliteId"));
        
        if (sensorType != null)
            conditions.add("type='" + sensorType + "'");
        
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        for (List<String> list : result) {
            for (String string : list) {
                
                sensors.add(string);
            }
        }
        
        return sensors;
    }
    
    public List<String> getStationsIds() throws SQLException {
        
        List<String> stationsIds = new ArrayList<String>();
        
        List<String> fields = new ArrayList<String>();
        fields.add("groundStationId");

        String table = "LNK_Satellite_GroundStation";
        List<String> conditions = new ArrayList<String>();
        
        //TODO: add condition for sensors
        conditions.add(getCondition("satelliteId"));

        List<List<String>> stations = getDboperations().select(fields, table, conditions);

        if (stations != null)
        for (List<String> station : stations) {
            
            if (station != null) {
                stationsIds.add(station.get(0));
            }
        }
        
        return stationsIds;
    }
    
    public String getSensorType(String sensorId) throws SQLException {
        
        String sensorType = "";
        
        List<String> fields = new ArrayList<String>();
        fields.add("type");

        String table = "Sensor";
        List<String> conditions = new ArrayList<String>();
        conditions.add("sensorId='" + sensorId + "'");

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        List<String> list = sensors.get(0);
        sensorType = list.get(0);
        
        return sensorType;
    }
    
    
    
    public String getCondition(String satelliteIDColumnName) {
        
        String condition = "";
        
        if (conditionType == ConditionType.mmfas) {
            condition = satelliteIDColumnName + " in "
                + "(SELECT satelliteId FROM LNK_MMFAS_Satellite WHERE mmfasId = '" + id + "')";
        }
        else {
            condition = satelliteIDColumnName + "='" + id + "'";
        }
        
        return condition;
    }
    
    
    public String getSensorId(String satelliteId, String sensorType) throws SQLException {
        
        String sensorId = null;
        
        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("sensorId");
        
        String table = "Sensor";
        
        List<String> conditions = new ArrayList<String>();
        conditions.add(getCondition("Sensor.satelliteId"));
        conditions.add("Sensor.satelliteId='"+satelliteId+"'");
        conditions.add("Sensor.type='"+sensorType+"'");
        
        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && !result.isEmpty()) {
            sensorId = result.get(0).get(0);
        }
        
        return sensorId;
    }
    
    /**
     * Gets the sensors of a given platform
     *
     * @param condition         the condition that will be use to sort sensors
     *                          Is one of (mmfas=<mmfasId>), (platform=<satelliteId>), null
     * @return
     * @throws SQLException
     */
    public List<Sensor> loadSensors() throws SQLException {

        //Result
        List<Sensor> sensors = new ArrayList<Sensor>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
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
        
        fields.add("satelliteId");

        String table = "Sensor";

        //Filtering the DB results by sensor type
        List<String> conditions = new ArrayList<String>();
        conditions.add(getCondition("satelliteId"));

        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);

        if (result != null && !result.isEmpty())
        for (List<String> entry : result) {

            List<String> list = entry;
            Sensor sensor = new Sensor(
                    list.get(0),
                    list.get(1),
                    list.get(2),
                    list.get(3),
                    list.get(4),
                    list.get(5),
                    list.get(6),
                    list.get(7),
                    list.get(8),
                    list.get(9),
                    list.get(10),
                    list.get(11),
                    Arrays.asList(list.get(12).split(",")),
                    list.get(13));


            sensor.setInstrumentModes(getInstrumentModes(list.get(0)));
            for (String instrumentModeId : sensor.getInstrumentModes().keySet()) {

                sensor.setPolarizationModes(getPolarisationModes(sensor.getSensorId(), instrumentModeId));

            }
            sensors.add(sensor);
        }

        return sensors;
    }

    /**
     * This function permits to load all the sensors for a given type
     *
     * @return
     * @throws SQLException
     */
    public List<Sensor> loadSensorsByType(String sensorType) throws SQLException {

        //Result
        List<Sensor> sensors = new ArrayList<Sensor>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
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

        fields.add("satelliteId");
        
        String table = "Sensor";

        //Filtering the DB results by sensor type
        List<String> conditions = new ArrayList<String>();
        conditions.add("type='" + sensorType + "'");
        conditions.add(getCondition("satelliteId"));
//        conditions.add("platform='" + platform + "'");

        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);

        if (result != null && !result.isEmpty())
        for (List<String> entry : result) {

            List<String> list = entry;
            Sensor sensor = new Sensor(
                    list.get(0),
                    list.get(1),
                    list.get(2),
                    list.get(3),
                    list.get(4),
                    list.get(5),
                    list.get(6),
                    list.get(7),
                    list.get(8),
                    list.get(9),
                    list.get(10),
                    list.get(11),
                    Arrays.asList(list.get(12).split(",")),
                    list.get(13));
            
            sensor.setInstrumentModes(getInstrumentModes(list.get(0)));
            
            
            if (sensor.getSensorType().equalsIgnoreCase("SAR")) {
                
                for (String instrumentId : sensor.getInstrumentModes().keySet()) {
                    sensor.setPolarizationModes(getPolarisationModes(sensor.getSensorId(), instrumentId));
                }
            }
            
            
            sensors.add(sensor);

        }

        return sensors;
    }

    /**
     * Permits to load the sensor with the given ID
     *
     * @param sensorId
     * @return
     * @throws SQLException
     */
    public Sensor loadSensor(String sensorId) throws SQLException {

        Sensor sensor = null;

        List<String> fields = new ArrayList<String>();
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

        fields.add("satelliteId");
        
        String table = "Sensor";
        List<String> conditions = new ArrayList<String>();
        conditions.add("sensorId='" + sensorId + "'");
        conditions.add(getCondition("satelliteId"));

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        if (sensors != null) {
            
            List<String> list = sensors.get(0);

            sensor = new Sensor(
                    sensorId,
                    list.get(1),
                    list.get(2),
                    list.get(3),
                    list.get(4),
                    list.get(5),
                    list.get(6),
                    list.get(7),
                    list.get(8),
                    list.get(9),
                    list.get(10),
                    list.get(11),
                    Arrays.asList(list.get(12).split(",")),
                        list.get(13));

            sensor.setInstrumentModes(getInstrumentModes(sensorId));


            if (sensor.getSensorType().equalsIgnoreCase("SAR")) {
                for (String instrumentModeId : sensor.getInstrumentModes().keySet()) {

                    sensor.addPolarizationModes(getPolarisationModes(sensor.getSensorId(), instrumentModeId));
                }
            }
        }
        
        return sensor;
    }

    public List<String> getResolutions(String sensorId, String sensorType) throws SQLException {

        //Result
        List<String> resolutions = new ArrayList<String>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("acrosstrackresolution");
        fields.add("alongtrackresolution");

        
        String table = "";

        if (sensorType.equalsIgnoreCase("OPT"))
            table = "OptIMCharacteristics";
        if (sensorType.equalsIgnoreCase("SAR"))
            table = "SarIMCharacteristics";
        
        //Filtering the DB results by sensor ID
        List<String> conditions = new ArrayList<String>();
        conditions.add("sensorId = '" + sensorId + "'");

        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);

        if (result != null) {
            for (List<String> entry : result) {

                resolutions.add(entry.get(0) + " " + entry.get(1));
            }
        }

        return resolutions;
    }
    
    public Map<String,String> getInstrumentModes(String sensorId) throws SQLException {
        
        String sensorType = getSensorType(sensorId);
        
        //Result
        Map<String,String> modes = new HashMap<String,String>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("imId");
        fields.add("name");

        String table = "InstrumentMode";

        //Filtering the DB results by sensor ID
        List<String> conditions = new ArrayList<String>();
        
        if (sensorType.equalsIgnoreCase("OPT"))
        conditions.add("imId in (SELECT imId FROM OptIMCharacteristics WHERE sensorId = '" + sensorId + "')");

        if (sensorType.equalsIgnoreCase("SAR"))
        conditions.add("imId in (SELECT imId FROM SarIMCharacteristics WHERE sensorId = '" + sensorId + "')");

        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);

        if (result != null) {
            for (List<String> entry : result) {

                modes.put(entry.get(0), entry.get(1));
            }
        }

        return modes;
    }

    
    public List<OPTSensor> loadOPTSensors() throws SQLException {
        
        List<OPTSensor> optSensors = new ArrayList<OPTSensor>();
        
        List<String> fields = new ArrayList<String>();
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
        
        fields.add("satelliteId");
        
        String table = "Sensor";
        List<String> conditions = new ArrayList<String>();
        conditions.add(getCondition("satelliteId"));
        conditions.add("type = 'OPT'");
        

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        if (sensors != null)
        {
            for (List<String> list : sensors) {

                OPTSensor sensor = new OPTSensor(
                                  list.get(0),
                                  list.get(1),
                                  list.get(2),
                                  list.get(3),
                                  list.get(4),
                                  list.get(5),
                                  list.get(6),
                                  list.get(7),
                                  list.get(8),
                                  list.get(9),
                                  list.get(10),
                                  list.get(11),
                                  Arrays.asList(list.get(12).split(",")),
                                  list.get(13));

                getOPTCharacteristics(sensor);
                getOPTInstrumentModes(sensor);

                optSensors.add(sensor);
            }
        }
        
        return optSensors;
    }
    
    public OPTSensor loadOPTSensor(String sensorId) throws SQLException {
        
        OPTSensor sensor = null;
        
        List<String> fields = new ArrayList<String>();
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
        
        fields.add("satelliteId");
        
        String table = "Sensor";
        List<String> conditions = new ArrayList<String>();
        conditions.add(getCondition("satelliteId"));
        conditions.add("sensorId = '" + sensorId + "'");
        conditions.add("type = 'OPT'");

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        if (sensors != null)
        {
            List<String> list = sensors.get(0);

            sensor = new OPTSensor(
                    list.get(0),
                    list.get(1),
                    list.get(2),
                    list.get(3),
                    list.get(4),
                    list.get(5),
                    list.get(6),
                    list.get(7),
                    list.get(8),
                    list.get(9),
                    list.get(10),
                    list.get(11),
                    Arrays.asList(list.get(12).split(",")),
                    list.get(13));

            getOPTCharacteristics(sensor);
            getOPTInstrumentModes(sensor);
            
        }
        
        return sensor;
    }
    
    
    private void getOPTCharacteristics(OPTSensor sensor) throws SQLException {
        List<String> fields = new ArrayList<String>();
        
        fields.add("acrossTrackFOV");
        fields.add("minAcrossTrackAngle");
        fields.add("maxAcrossTrackAngle");
        fields.add("minAlongTrackAngle");
        fields.add("maxAlongTrackAngle");
        fields.add("swathWidth");
        fields.add("groundLocationAccuracy");
        fields.add("revisitTimeInDays");
        fields.add("numberOfBands");
        
        String table = "OptSensorCharacteristics";
        List<String> conditions = new ArrayList<String>();
        conditions.add("sensorId='" + sensor.getSensorId() + "'");

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        List<String> list = sensors.get(0);

        sensor.setCharacteristics(
                list.get(0),
                list.get(1),
                list.get(2),
                list.get(3),
                list.get(4),
                list.get(5),
                list.get(6),
                list.get(7),
                list.get(8));
    }

    private void getOPTInstrumentModes(OPTSensor sensor) throws SQLException {
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("InstrumentMode.imId");
        fields.add("name");
        fields.add("description");
        
        fields.add("maxPowerConsumption");
        
        fields.add("acrossTrackResolution");
        fields.add("alongTrackResolution");
        
        fields.add("numberOfSamples");
        fields.add("bandType");
        fields.add("minSpectralRange");
        fields.add("maxSpectralRange");
        fields.add("snrRatio");
        fields.add("noiseEquivalentRadiance");
        
        String table = "InstrumentMode, OPTIMCharacteristics";
        List<String> conditions = new ArrayList<String>();
        conditions.add("InstrumentMode.imId in (SELECT imId FROM OPTIMCharacteristics WHERE sensorId = '" + sensor.getSensorId() + "')");
        conditions.add("OPTIMCharacteristics.imId = InstrumentMode.imId");
        
        
        List<List<String>> instrumentModes = getDboperations().select(fields, table, conditions);

        for (List<String> list : instrumentModes) {
            
            String instrumentModeId = list.get(0);
            
            //Add simple Instrument mode information (id + name) for Describing (DescTasking and DescDocument)
            sensor.addInstrumentMode(instrumentModeId, list.get(1));
            
            OPTSensor.OPTSensorMode mode = sensor.addNewInstrumentModeDescription(instrumentModeId);
            mode.setIdentification(instrumentModeId,
                list.get(1),
                list.get(2));
            
            mode.setCharacteristics(
                    list.get(3), 
                    list.get(4), 
                    list.get(5), 
                    list.get(6), 
                    list.get(7), 
                    list.get(8), 
                    list.get(9), 
                    list.get(10), 
                    list.get(11));
        }
        
        
//            mode.setPolarizationModes(getPolarisationModes(instrumentModeId));
    }
    
    
    
    public SARSensor loadSARSensor(String sensorId) throws SQLException {

        SARSensor sensor = null;
        
        List<String> fields = new ArrayList<String>();
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
        
        fields.add("satelliteId");
        
        String table = "Sensor";
        List<String> conditions = new ArrayList<String>();
        conditions.add(getCondition("satelliteId"));
        conditions.add("type = 'SAR'");
        conditions.add("sensorId = '" + sensorId + "'");

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        if (sensors != null)
        {
            List<String> list = sensors.get(0);

            sensor = new SARSensor(
                    list.get(0),
                    list.get(1),
                    list.get(2),
                    list.get(3),
                    list.get(4),
                    list.get(5),
                    list.get(6),
                    list.get(7),
                    list.get(8),
                    list.get(9),
                    list.get(10),
                    list.get(11),
                    Arrays.asList(list.get(12).split(",")),
                    list.get(13));

            getSARCharacteristics(sensor);
            getSARInstrumentModes(sensor);
            
            
            
        }
        
        return sensor;
    }
    
    public List<SARSensor> loadSARSensors() throws SQLException {

        List<SARSensor> sarSensors = new ArrayList<SARSensor>();
        
        List<String> fields = new ArrayList<String>();
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
        
        fields.add("satelliteId");
        
        String table = "Sensor";
        List<String> conditions = new ArrayList<String>();
        conditions.add(getCondition("satelliteId"));
        conditions.add("type = 'SAR'");

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        if (sensors != null)
        {
            for (List<String> list : sensors) {

                SARSensor sensor = new SARSensor(
                        list.get(0),
                        list.get(1),
                        list.get(2),
                        list.get(3),
                        list.get(4),
                        list.get(5),
                        list.get(6),
                        list.get(7),
                        list.get(8),
                        list.get(9),
                        list.get(10),
                        list.get(11),
                        Arrays.asList(list.get(12).split(",")),
                    list.get(13));

                getSARCharacteristics(sensor);
                getSARInstrumentModes(sensor);
                sarSensors.add(sensor);
            }
            
        }
        
        return sarSensors;
    }
    
    private void getSARCharacteristics(SARSensor sensor) throws SQLException {
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("antennaLength");
        fields.add("antennaWidth");
        fields.add("groundLocationAccuracy");
        fields.add("revisitTimeInDays");
        fields.add("transmitFrequencyBand");
        fields.add("transmitCenterFrequency");
        
        String table = "SarSensorCharacteristics";
        List<String> conditions = new ArrayList<String>();
        conditions.add("sensorId='" + sensor.getSensorId() + "'");

        List<List<String>> sensors = getDboperations().select(fields, table, conditions);

        List<String> list = sensors.get(0);

        sensor.setCharacteristics(
                list.get(0),
                list.get(1),
                list.get(2),
                list.get(3),
                list.get(4),
                list.get(5));


    }
    
    private void getSARInstrumentModes(SARSensor sensor) throws SQLException {
        
        
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("InstrumentMode.imId");
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
        conditions.add("InstrumentMode.imId in (SELECT imId FROM SARIMCharacteristics WHERE sensorId = '" + sensor.getSensorId() + "')");
        conditions.add("SARIMCharacteristics.imId = InstrumentMode.imId");
        
        List<List<String>> instrumentModes = getDboperations().select(fields, table, conditions);

        for (List<String> list : instrumentModes) {
            
            String instrumentModeId = list.get(0);
            //Add simple Instrument mode information (id + name) for Describing (DescTasking and DescDocument)
            sensor.addInstrumentMode(instrumentModeId, list.get(1));
            
            SARSensor.SARSensorMode mode = sensor.addNewInstrumentModeDescription(instrumentModeId);
            mode.setIdentification(instrumentModeId,
                list.get(1),
                list.get(2));
            
            mode.setCharacteristics(
                    list.get(3), 
                    list.get(4), 
                    list.get(5), 
                    list.get(6), 
                    list.get(7), 
                    list.get(8), 
                    list.get(9), 
                    list.get(10), 
                    list.get(11), 
                    list.get(12), 
                    list.get(13), 
                    list.get(14), 
                    list.get(15), 
                    list.get(16), 
                    list.get(17), 
                    list.get(18), 
                    list.get(19));
            
            mode.setPolarizationModes(getPolarisationModes(sensor.getSensorId(), instrumentModeId));
        }
    }
    
    /**
     * Get all the polarisation modes for a given sensor (Sensor must be SAR
     * because OPT do not have PolarisationMode)
     *
     * @param sensorId
     * @return
     * @throws SQLException
     */
    public Map<String,String> getPolarisationModes(String sensorId, String instrumentModeId) throws SQLException {

        //Result
        Map<String,String> modes = new HashMap<String,String>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("pmId");
        fields.add("name");

        String table = "PolarisationMode";

        //Filtering the DB results by sensor ID
        List<String> conditions = new ArrayList<String>();
        conditions.add("pmId in "
                + "("
                    + "SELECT pmId FROM LNK_IM_PM "
                    + "WHERE imId='" + instrumentModeId + "' "
                    + "AND sensorId='" + sensorId + "'"
                + ")"
            );
        
        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);

        if (result != null) {
            for (List<String> entry : result) {

                modes.put(entry.get(0), entry.get(1));
            }
        }
        return modes;
    }
}
