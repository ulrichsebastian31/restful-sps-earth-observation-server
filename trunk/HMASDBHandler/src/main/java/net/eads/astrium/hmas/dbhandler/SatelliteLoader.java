/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SatelliteLoader.java
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
import java.util.List;
import net.eads.astrium.hmas.util.structures.SatellitePlatform;
import net.eads.astrium.hmas.util.structures.Orbit;

/**
 *
 * @author re-sulrich
 */
public class SatelliteLoader extends SensorLoader {
    
    
    private String platform;
    
    public SatelliteLoader(String platform) {
        
        super(ConditionType.satellite, platform);
        
        this.platform = platform;
    }
    
    public SatelliteLoader(String platform, DBOperations dboperations) {
        
        super(ConditionType.satellite, platform, dboperations);
        this.platform = platform;
    }
    
    public SatelliteLoader(String platform, String databaseURL, String user, String pass, String schemaName) {
        
        super(ConditionType.satellite, platform, databaseURL, user, pass, schemaName);
        
        this.platform = platform;
    }
    
    public SatellitePlatform getSatellite() throws SQLException {
        
        SatellitePlatform satellite = null;

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("noradName");
        fields.add("name");
        fields.add("description");
        fields.add("href");

        fields.add("orbitType");
        fields.add("low_Min_Semi_Major_Axis");
        fields.add("low_Max_Semi_Major_Axis");
        fields.add("low_Min_Inclination");
        fields.add("low_Max_Inclination");
        fields.add("low_Min_Eccentricity");
        fields.add("low_Max_Eccentricity");
        fields.add("tight_Min_Semi_Major_Axis");
        fields.add("tight_Max_Semi_Major_Axis");
        fields.add("tight_Min_Inclination");
        fields.add("tight_Max_Inclination");
        fields.add("tight_Min_Eccentricity");
        fields.add("tight_Max_Eccentricity");
        fields.add("orbit_Min_Semi_Major_Axis");
        fields.add("orbit_Nom_Semi_Major_Axis");
        fields.add("orbit_Max_Semi_Major_Axis");
        fields.add("orbit_Min_Inclination");
        fields.add("orbit_Nom_Inclination");
        fields.add("orbit_Max_Inclination");
        fields.add("orbit_Nom_Eccentricity");
        fields.add("orbit_Nom_Arg_Perigee");
        
        String table = "SatellitePlatform, Orbit";
        
        //Filtering the DB results by sensor type
        List<String> conditions = new ArrayList<String>();
        conditions.add("SatellitePlatform.satelliteId='" + platform + "'");
        conditions.add("SatellitePlatform.satelliteId= Orbit.satelliteId");
        
        List<List<String>> result = getDboperations().select(fields, table, conditions);

        for (List<String> entry : result) {

            List<String> list = entry;
            
            satellite = new SatellitePlatform(
                    platform,
                    list.get(0), 
                    list.get(1), 
                    list.get(2), 
                    list.get(3), 
                    new Orbit(list.get(4), 
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
                        list.get(19), 
                        list.get(20), 
                        list.get(21), 
                        list.get(22), 
                        list.get(23), 
                        list.get(24)));
        }
        return satellite;
    }
    
    
    
    
}
