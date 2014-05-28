/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CreateSensorStruct.java
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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public class CreateSensorStruct {
    
    public static void testCreateOPTIMChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"instrumentModeId", "serial primary key"});
        
        fields.add(new String[]{"acrossTrackResolution", "double precision"});
        fields.add(new String[]{"alongTrackResolution", "double precision"});
        
        fields.add(new String[]{"maxPowerConsumption", "double precision"});
        fields.add(new String[]{"numberOfSamples", "double precision"});
        fields.add(new String[]{"bandType", "varchar(20)"});
        fields.add(new String[]{"minSpectralRange", "double precision"});
        fields.add(new String[]{"maxSpectralRange", "double precision"});
        fields.add(new String[]{"snrRatio", "double precision"});
        fields.add(new String[]{"noiseEquivalentRadiance", "double precision"});
        
        //Foreign key
        fields.add(new String[]{"imId", "varchar(5) references InstrumentMode(imId)"});
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
//        fields.add(new String[]{"sdfId", "integer references SDF(sdfId)"});
        
        TestConnexion.create("OptIMCharacteristics", fields);
        
        TestConnexion.addUniqueConstraint("OptIMCharacteristics", Arrays.asList(new String[]{"imId","sensorId"}));
    }
    
    public static void testCreateSARIMChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"instrumentModeId", "serial primary key"});
        
        fields.add(new String[]{"acrossTrackResolution", "double precision"});
        fields.add(new String[]{"alongTrackResolution", "double precision"});
        
        fields.add(new String[]{"maxPowerConsumption", "double precision"});
        fields.add(new String[]{"minAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"maxAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"minAlongTrackAngle", "double precision"});
        fields.add(new String[]{"maxAlongTrackAngle", "double precision"});
        fields.add(new String[]{"swathWidth", "double precision"});
        fields.add(new String[]{"radiometricResolution", "double precision"});
        fields.add(new String[]{"minRadioStab", "double precision"});
        fields.add(new String[]{"maxRadioStab", "double precision"});
        fields.add(new String[]{"minAlongTrackAmbRatio", "double precision"});
        fields.add(new String[]{"maxAlongTrackAmbRatio", "double precision"});
        fields.add(new String[]{"minAcrossTrackAmbRatio", "double precision"});
        fields.add(new String[]{"maxAcrossTrackAmbRatio", "double precision"});
        fields.add(new String[]{"minNoiseEquivalentSO", "double precision"});
        fields.add(new String[]{"maxNoiseEquivalentSO", "double precision"});
        
        //Foreign key
        fields.add(new String[]{"imId", "varchar(5) references InstrumentMode(imId)"});
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
//        fields.add(new String[]{"sdfId", "integer references SDF(sdfId)"});
        
        TestConnexion.create("SarIMCharacteristics", fields);
        
        TestConnexion.addUniqueConstraint("SarIMCharacteristics", Arrays.asList(new String[]{"imId","sensorId"}));
    }
    
    public static void testCreatePolarisationModeTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"pmId", "varchar(5) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        
        TestConnexion.create("PolarisationMode", fields);
    }
    
    public static void testCreateInstrumentModeTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"imId", "varchar(5) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        
        TestConnexion.create("InstrumentMode", fields);
    }
    
    public static void testCreateIMPMTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        //Foreign keys
        fields.add(new String[]{"imId", "varchar(5) references InstrumentMode(imId)"});
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
        fields.add(new String[]{"pmId", "varchar(5) references PolarisationMode(pmId)"});
        
        TestConnexion.create("LNK_IM_PM", fields);
    }
    
    public static void testCreateSatelliteTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"satelliteId", "varchar(10) primary key"});
        fields.add(new String[]{"noradName", "varchar(10)"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"description", "varchar(1024)"});
        fields.add(new String[]{"href", "varchar(1024)"});
        
        fields.add(new String[]{"antennaSdfFileContent", "bytea"});
        
        TestConnexion.create("SatellitePlatform", fields);
    }
    
    public static void testCreateOrbitTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"orbitId", "serial primary key"});
        fields.add(new String[]{"orbitType", "varchar(10)"});
        fields.add(new String[]{"low_Min_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"low_Max_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"low_Min_Inclination", "double precision"});
        fields.add(new String[]{"low_Max_Inclination", "double precision"});
        fields.add(new String[]{"low_Min_Eccentricity", "double precision"});
        fields.add(new String[]{"low_Max_Eccentricity", "double precision"});
        fields.add(new String[]{"tight_Min_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"tight_Max_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"tight_Min_Inclination", "double precision"});
        fields.add(new String[]{"tight_Max_Inclination", "double precision"});
        fields.add(new String[]{"tight_Min_Eccentricity", "double precision"});
        fields.add(new String[]{"tight_Max_Eccentricity", "double precision"});
        fields.add(new String[]{"orbit_Min_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"orbit_Nom_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"orbit_Max_Semi_Major_Axis", "double precision"});
        fields.add(new String[]{"orbit_Min_Inclination", "double precision"});
        fields.add(new String[]{"orbit_Nom_Inclination", "double precision"});
        fields.add(new String[]{"orbit_Max_Inclination", "double precision"});
        fields.add(new String[]{"orbit_Nom_Eccentricity", "double precision"});
        fields.add(new String[]{"orbit_Nom_Arg_Perigee", "double precision"});
        
        //Foreign keys
        //Satellite
        fields.add(new String[]{"satelliteId", "varchar(10) references SatellitePlatform(satelliteId)"});
        
        TestConnexion.create("Orbit", fields);
    }
    
    public static void testCreateSensorTable() throws SQLException {
        
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sensorId", "varchar(10) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"sensorDescription", "varchar(1024)"});
        fields.add(new String[]{"type", "SensorTypes"});
        fields.add(new String[]{"bandType", "varchar(10)"});
        fields.add(new String[]{"minLatitude", "double precision"});
        fields.add(new String[]{"maxLatitude", "double precision"});
        fields.add(new String[]{"minLongitude", "double precision"});
        fields.add(new String[]{"maxLongitude", "double precision"});
        //Characteristics
        fields.add(new String[]{"acqMethod", "varchar(20)"});
        fields.add(new String[]{"applications", "varchar(100)"});
        fields.add(new String[]{"mass", "double precision"});
        fields.add(new String[]{"maxPowerConsumption", "double precision"});
        
        //Foreign keys
        //Satellite
        fields.add(new String[]{"satelliteId", "varchar(10) references SatellitePlatform(satelliteId)"});
        
        TestConnexion.create("Sensor", fields);
    }
    
    /**
     * Creates the properties of the sensor
     */
    public static void testCreateOPTSensorChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sensorCharId", "serial primary key"});
        
        fields.add(new String[]{"acrossTrackFOV", "double precision"});
        fields.add(new String[]{"minAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"maxAcrossTrackAngle", "double precision"});
        fields.add(new String[]{"minAlongTrackAngle", "double precision"});
        fields.add(new String[]{"maxAlongTrackAngle", "double precision"});
        fields.add(new String[]{"swathWidth", "double precision"});
        fields.add(new String[]{"groundLocationAccuracy", "double precision"});
        fields.add(new String[]{"revisitTimeInDays", "double precision"});
        fields.add(new String[]{"numberOfBands", "integer"});
        
        //Foreign keys
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("OptSensorCharacteristics", fields);
    }
    
    public static void testCreateSARSensorChar() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sensorCharId", "serial primary key"});
        
        fields.add(new String[]{"antennaLength", "double precision"});
        fields.add(new String[]{"antennaWidth", "double precision"});
        fields.add(new String[]{"groundLocationAccuracy", "double precision"});
        fields.add(new String[]{"revisitTimeInDays", "double precision"});
        fields.add(new String[]{"transmitFrequencyBand", "varchar(20)"});
        fields.add(new String[]{"transmitCenterFrequency", "double precision"});
        
        //Foreign keys
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("SarSensorCharacteristics", fields);
    }
    
    
    public static void testCreateSDF() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"sdfId", "serial primary key"});
        
        fields.add(new String[]{"content", "bytea"});
        
        //Foreign keys
        fields.add(new String[]{"imId", "varchar(5) references InstrumentMode(imId)"});
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("SDF", fields);
        
        TestConnexion.addUniqueConstraint("SDF", Arrays.asList(new String[]{"imId","sensorId"}));
    }
    
    public static void testCreateOSF() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"osfId", "serial primary key"});
        
        fields.add(new String[]{"validityStart", "timestamp"});
        fields.add(new String[]{"validityStop", "timestamp"});
        fields.add(new String[]{"content", "bytea"});
        
        TestConnexion.create("OSF", fields);
    }
    
    public static void testCreateSatelliteOSFTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        //Foreign keys
        fields.add(new String[]{"satelliteId", "varchar(10) references SatellitePlatform(satelliteId)"});
        fields.add(new String[]{"osfId", "integer references OSF(osfId)"});
        
        TestConnexion.create("LNK_Satellite_OSF", fields);
    }
    
    
    
    
    
    public static void testCreateGroundStationTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"groundStationId", "char(8) primary key"});
        fields.add(new String[]{"name", "varchar(100)"});
        fields.add(new String[]{"type", "varchar(10)"});
        fields.add(new String[]{"longitude", "double precision"});
        fields.add(new String[]{"latitude", "double precision"});
        fields.add(new String[]{"altitude", "double precision"});
        fields.add(new String[]{"antennaType", "varchar(10)"});
        fields.add(new String[]{"purpose", "varchar(20)"});
        fields.add(new String[]{"defaultElevation", "double precision"});
        fields.add(new String[]{"maskPointsList", "varchar(1024)"});
        
        TestConnexion.create("GroundStation", fields);
        
    }
    
    
    public static void testCreateLNK_Satellite_GroundStationTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"satelliteId", "varchar(10) references SatellitePlatform(satelliteId)"});
        fields.add(new String[]{"groundStationId", "varchar(10) references GroundStation(groundStationId)"});
        
        TestConnexion.create("LNK_Satellite_GroundStation", fields);
    }
    
    
    public static void testCreateUnavailibilityTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"unavailibilityId", "serial primary key"});
        fields.add(new String[]{"beginU", "timestamp"});
        fields.add(new String[]{"endU", "timestamp"});
        fields.add(new String[]{"cause", "varchar(1024)"});
        
        //Foreign keys
        fields.add(new String[]{"groundStationId", "char(8) references GroundStation(groundStationId)"});
        fields.add(new String[]{"sensorId", "varchar(10) references Sensor(sensorId)"});
        
        TestConnexion.create("Unavailibility", fields);
    }
}
