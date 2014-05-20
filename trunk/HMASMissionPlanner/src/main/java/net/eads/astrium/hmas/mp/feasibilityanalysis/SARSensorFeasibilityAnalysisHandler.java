/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SARSensorFeasibilityAnalysisHandler.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EoCfiHndlrError;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.SegmentVisGS;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;

/**
 *
 * @author re-sulrich
 */
public class SARSensorFeasibilityAnalysisHandler extends SensorFeasibilityAnalysisHandler {
    
    private final SARTaskingParameters sarParameters;

    public SARSensorFeasibilityAnalysisHandler(
            SARTaskingParameters sarParameters, 
            String satelliteId, String sensorId, 
            MissionPlannerDBHandler dbHandler
        ) throws EoCfiHndlrError, SQLException, ParseException {
        
        super(satelliteId, "sar", sensorId, dbHandler);
        this.sarParameters = sarParameters;
        
        taskId = dbHandler.getFeasibilityHandler().createSARFeasibilityTask(sensorId, sarParameters);
        status = dbHandler.getFeasibilityHandler().getStatus(taskId);
    }

    @Override
    public void doFeasibility() throws Exception {
       
        int minAcquisitionTime = 1;
        int minDownlinkTime = 1;
        
        segments = handler.getSARFeasibility(sensorId, sarParameters, minAcquisitionTime);

        System.out.println("Nb segments before sorting : " + segments.size());
        
        Date estimatedTOC = Calendar.getInstance().getTime();
        
        for (Segment segment : segments) {
            
            System.out.println("Segment : " + DateHandler.formatDate(segment.getStartOfAcquisition().getTime()) + " - " + DateHandler.formatDate(segment.getEndOfAcquisition().getTime()));

            this.getDownlinkInformation(segment, this.sarParameters.getTimes().get(this.sarParameters.getTimes().size() - 1).getEnd(), minDownlinkTime);
            
            if (segment.getGroundStationDownlink() == null) {
                segments.remove(segment);
                break;
            }
            
            Date segmentTOC = segment.getGroundStationDownlink().getEndOfVisibility().getTime();
            
            segment.setStatus(new Status(
                    "POTENTIAL", 
                    0, 
                    "This segment is acquirable.", 
                    Calendar.getInstance().getTime(), 
                    segmentTOC));
            String segmentId = this.dbHandler.getFeasibilityHandler().createSegment(taskId, segment);
            segment.setSegmentId(segmentId);
            
            if (segmentTOC.after(estimatedTOC)) {
                estimatedTOC = segmentTOC;
            }
        }
        System.out.println("Nb segments after sorting : " + segments.size());
        
        status = this.dbHandler.getFeasibilityHandler().addNewFinishedStatus(taskId, estimatedTOC);
    }

    @Override
    public void doFakeFeasibility() {
        
        Calendar cstart = Calendar.getInstance();cstart.add(Calendar.HOUR_OF_DAY, 1);
        Calendar cend = Calendar.getInstance();cend.add(Calendar.HOUR_OF_DAY, 2);
        
        this.estimatedCost = 1;
        this.currency = "Euros";
        this.taskId = "123";
        
        status = new Status(
                "FEASIBILITY COMPLETE", 
                100, 
                "The task is completed", 
                Calendar.getInstance().getTime(), 
                new Date(2013, 11, 21, 0, 0, 0));
        
        segments = new ArrayList<>();
        segments.add(
                new Segment(
                        "21", 
                        new Polygon( Arrays.asList(new Point[]{
                            new Point(125,35, 0.0), 
                            new Point(127,37, 0.0), 
                            new Point(128,36, 0.0), 
                            new Point(126,34, 0.0), 
                            new Point(125,35, 0.0)})), 
                        (long)1243, (long)12, status, 
                        Calendar.getInstance(), Calendar.getInstance(),  100.0,
                        new SegmentVisGS("GHATOJHX", "Kumamoto (JAPAN)", cstart, cend)
                    )
            );
        
        segments.add(
                new Segment(
                        "22", 
                        new Polygon( Arrays.asList(new Point[]{
                            new Point(129,39, 0.0), 
                            new Point(131,41, 0.0), 
                            new Point(132,40, 0.0), 
                            new Point(130,38, 0.0), 
                            new Point(129,39, 0.0)})), 
                        (long)1243, (long)12, status, 
                        Calendar.getInstance(), Calendar.getInstance(),  100.0,
                        new SegmentVisGS("GHATOJHX", "Kumamoto (JAPAN)", cstart, cend)
                    )
            );
        
        
        
    }
}
