/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               MissionPlannerDBHandler.java
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
package net.eads.astrium.hmas.mp.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.eads.astrium.hmas.database.EOSPSDBHandler;
import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.dbhandler.SatelliteLoader;
import net.eads.astrium.hmas.dbhandler.SensorLoader;
import net.eads.astrium.hmas.dbhandler.UnavailibilityHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorFeasibilityHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;

/**
 *
 * @author re-sulrich
 */
public class MissionPlannerDBHandler extends EOSPSDBHandler {

    @Override
    public SensorLoader getSensorLoader() {
        return satelliteHandler;
    }
    
    @Override
    public String getService() {
        return "mp";
    }

    @Override
    public String getServiceId() {
        return missionPlannerId;
    }
    
    private UnavailibilityHandler unavailibilityHandler;
    private SatelliteLoader satelliteHandler;
    private SensorFeasibilityHandler feasibilityHandler;
    private SensorPlanningHandler planningHandler;
    
    private final String missionPlannerId;
    private final String satelliteId;

    public String getMissionPlannerId() {
        return missionPlannerId;
    }

    public String getSatelliteId() {
        return satelliteId;
    }
    
    public MissionPlannerDBHandler(String missionPlannerId, String satelliteId) throws SQLException {
    
        super("MissionPlannerDatabase");
        
        this.missionPlannerId = missionPlannerId;
        this.satelliteId = satelliteId;
        
        unavailibilityHandler = new UnavailibilityHandler(this.getDboperations());
        System.out.println("created unavailibility handlUnavailibilityHandlerer");
        
        satelliteHandler = new SatelliteLoader(satelliteId, this.getDboperations());
        System.out.println("created MMFAS handler");
        
        feasibilityHandler = new SensorFeasibilityHandler(this.getDboperations());
        System.out.println("created feasibility handler");
        
        planningHandler = new SensorPlanningHandler(this.getDboperations());
        System.out.println("created planning handler");
    }

    public MissionPlannerDBHandler(String missionPlannerId, String satelliteId, DBOperations dboperations) throws SQLException {
    
        super(dboperations);
        
        this.missionPlannerId = missionPlannerId;
        this.satelliteId = satelliteId;
        
        unavailibilityHandler = new UnavailibilityHandler(this.getDboperations());
        System.out.println("created unavailibility handlUnavailibilityHandlerer");
        
        satelliteHandler = new SatelliteLoader(satelliteId, this.getDboperations());
        System.out.println("created MMFAS handler");
        
        feasibilityHandler = new SensorFeasibilityHandler(this.getDboperations());
        System.out.println("created feasibility handler");
        
        planningHandler = new SensorPlanningHandler(this.getDboperations());
        System.out.println("created planning handler");
    }

    public SatelliteLoader getSatelliteLoader() {
        return satelliteHandler;
    }

    public UnavailibilityHandler getUnavailibilityHandler() {
        return unavailibilityHandler;
    }

    public SensorFeasibilityHandler getFeasibilityHandler() {
        return feasibilityHandler;
    }

    public SensorPlanningHandler getPlanningHandler() {
        return planningHandler;
    }
}
