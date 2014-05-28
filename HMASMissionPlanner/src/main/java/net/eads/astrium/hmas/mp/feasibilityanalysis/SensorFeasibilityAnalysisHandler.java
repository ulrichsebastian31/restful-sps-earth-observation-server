/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorFeasibilityAnalysisHandler.java
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
package net.eads.astrium.hmas.mp.feasibilityanalysis;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EoCfiHndlrError;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EOCFIHandling;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.SegmentVisGS;
import net.eads.astrium.hmas.util.structures.tasking.Status;

/**
 *
 * @author re-sulrich
 */
public abstract class SensorFeasibilityAnalysisHandler extends Thread {
    
    final String satelliteId;
    final String sensorType;
    final String sensorId;
    
    final MissionPlannerDBHandler dbHandler;
    
    final EOCFIHandling handler;
    
    String taskId;
    int estimatedCost;
    String currency;
    
    Status status;
    List<Segment> segments;

    public String getTaskId() {
        return taskId;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public String getCurrency() {
        return currency;
    }

    public Status getStatus() {
        return status;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    
    public SensorFeasibilityAnalysisHandler(
            String satelliteId, String sensorType, String sensorId, 
            MissionPlannerDBHandler dbHandler) 
            throws EoCfiHndlrError, SQLException {
        
        super();
        
        this.satelliteId = satelliteId;
        this.sensorType = sensorType;
        this.sensorId = sensorId;
        this.dbHandler = dbHandler;
        
        //Default
        this.currency = "Euros";
        
        handler = EOCFIHandling.Factory.newInstance(satelliteId);
    }
    
    @Override
    public void run() {
        
        try {
            doFeasibility();
        } catch (Exception ex) {
            System.out.println("Error during feasibility analysis : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public abstract void doFeasibility() throws Exception;
    
    public abstract void doFakeFeasibility();
    
    
    void getDownlinkInformation(Segment segment, Date endFeasibility, int minDownlinkTime) throws SQLException, ParseException, EoCfiHndlrError {
        
        Date startTime = segment.getEndOfAcquisition().getTime();
        long orbit = segment.getEndOrbit();
        boolean isStationAvailable = false;
        boolean isOrbitAfterEndFeasibility = false;
        
        while (!isStationAvailable && !isOrbitAfterEndFeasibility) {
            
            List<SegmentVisGS> orbitDownlinks = handler.getNextStationDownlink(startTime, orbit, minDownlinkTime);
            
            for (int i = 0; i < orbitDownlinks.size(); i++) {
                
                SegmentVisGS segmentVisGS = orbitDownlinks.get(i);
                
                //If the whole station visibility is after the end of feasibility study (Time Of Interest)
                //Skip this station visibility
                //Only for the last orbit
                if (segmentVisGS.getStartOfVisibility().after(endFeasibility)) {
                    break;
                }
                
                List<TimePeriod> unavailibilities = this.dbHandler.getUnavailibilityHandler().getStationUnavailibilities(
                                         segmentVisGS.getGroundStationId(), 
                                         DateHandler.formatDate(segmentVisGS.getStartOfVisibility().getTime()),
                                         DateHandler.formatDate(segmentVisGS.getEndOfVisibility().getTime()));
                
                if (unavailibilities == null || unavailibilities.isEmpty()) {
                    
                    segment.setGroundStationDownlink(segmentVisGS);
                    isStationAvailable = true;
                    break;
                }
                else {
                    System.out.println("Downlink to " + segmentVisGS.getGroundStationId() + " starting " + segmentVisGS.getStartOfVisibility().getTime() + " not possible because : ");
                    for (TimePeriod timePeriod : unavailibilities) {
                        System.out.println(" - " + DateHandler.formatDate(timePeriod.getBegin()));
                    }
                }
            }
            
            orbit++;
            isOrbitAfterEndFeasibility = handler.getOrbitBeginTime(orbit).after(endFeasibility);
        }  
    }
}
