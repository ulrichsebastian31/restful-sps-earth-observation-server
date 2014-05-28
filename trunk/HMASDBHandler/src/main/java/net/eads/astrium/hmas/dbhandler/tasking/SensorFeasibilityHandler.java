/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorFeasibilityHandler.java
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
package net.eads.astrium.hmas.dbhandler.tasking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.SegmentVisGS;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;

/**
 *
 * @author re-sulrich
 */
public class SensorFeasibilityHandler extends SensorTaskHandler {
    
    public SensorFeasibilityHandler() throws SQLException {
        
        super();
    }
    
    public SensorFeasibilityHandler(DBOperations dboperations) {
        
        super(dboperations);
    }
    
    public SensorFeasibilityHandler(String databaseURL, String user, String pass, String schemaName) {
        
        super(databaseURL, user, pass, schemaName);
    }
    
    public Status addNewFinishedStatus(String sensorTaskId, Date estimatedTimeOfCompletion) throws SQLException {
        
        int percentCompletion = 100;
        String statusIdentifier = "FEASIBILITY COMPLETED";
        String message = "Feasibility study is complete";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, estimatedTimeOfCompletion);
        
        String table = "SensorTask";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "taskId='" +sensorTaskId+ "'";
        
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        
        String statusId = this.statusHandler.createNewStatusWithPrevious(statusIdentifier, percentCompletion, message, DateHandler.formatDate(now), DateHandler.formatDate(estimatedTimeOfCompletion), oldStatus);
        
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    /**
     * Saving tasks and requests
     */
    public String createSegment(String sensorTaskId, Segment segment) throws SQLException {
        
        return this.createSegment(TaskType.feasibility, sensorTaskId, segment);
    }
    
    public String createOPTFeasibilityTask(
            String sensorId, String requestId) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.feasibility, null);
        this.linkSensorTaskToRequest(sensorTaskId, requestId);
        
        return sensorTaskId;
    }
    public String createSARFeasibilityTask(
            String sensorId, String requestId) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.feasibility, null);
        this.linkSensorTaskToRequest(sensorTaskId, requestId);
        
        return sensorTaskId;
    }
    
    public String createOPTFeasibilityTask(
            String sensorId, OPTTaskingParameters parameters) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.feasibility, null);
        this.saveOPTFeasibilityRequest(sensorTaskId, parameters);
        
        return sensorTaskId;
    }
    
    public String createSARFeasibilityTask(
            String sensorId, SARTaskingParameters parameters) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.feasibility, null);
        this.saveSARFeasibilityRequest(sensorTaskId, parameters);
        
        return sensorTaskId;
    }
    
    private String saveSARFeasibilityRequest(
            String sensorTaskId,
            SARTaskingParameters parameters
            ) throws SQLException {
        
        return this.saveSARTaskingRequest(TaskHandlerType.sensor, sensorTaskId, RequestType.getFeasibility, parameters);
    }
    
    private String saveOPTFeasibilityRequest(
            String sensorTaskId,
            OPTTaskingParameters parameters
            ) throws SQLException {
        
        return this.saveOPTTaskingRequest(TaskHandlerType.sensor, sensorTaskId, RequestType.getFeasibility, parameters);
    }
    
    
    
    
    /**
     * Getting information from the database
     */
    
    
    public List<Segment> getSegments(String taskId) throws SQLException, ParseException {
        return this.getSegments(TaskType.feasibility, taskId);
    }
    
    public Segment getSegment(String segmentId) throws SQLException, ParseException {
        
        Segment segment = null;
        
        String table = "Segment, Status, Downlink, GroundStation";
        
        List<String> fields = new ArrayList<String>();
        fields.add("segmentId");
        fields.add("coordinates");
        fields.add("startOrbit");
        fields.add("stopOrbit");
        fields.add("acquisitionStartTime");
        fields.add("acquisitionStopTime");
        fields.add("cloudCoverageSuccessRate");
        fields.add("estimatedCost");
        fields.add("instrumentMode");
        
        fields.add("statusIdentifier");
        fields.add("percentCompletion");
        fields.add("statusmessage");
        fields.add("updateTime");
        fields.add("estimatedToC");
        
        fields.add("groundStationId");
        fields.add("name");
        fields.add("beginTime");
        fields.add("endTime");
        
        List<String> conditions = new ArrayList<String>();
//        if (taskType.equals(TaskType.feasibility)) {
//            conditions.add("Segment.feasibility='"+sensorTaskId+"'");
//        }
//        else {
//            conditions.add("Segment.planning='"+sensorTaskId+"'");
//        }
        
        conditions.add("Segment.segmentId='"+segmentId+"'");
        conditions.add("Segment.status=Status.statusId");
        conditions.add("Segment.segmentId=Downlink.segment");
        conditions.add("GroundStation.groundStationId=Downlink.downlinkStation");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            List<String> list = result.get(0);
            List<Point> points = new ArrayList<>();
            
            String[] coords = list.get(1).split(" ");
            for (int i = 0; i < coords.length; i++) {
                String[] point = coords[i].split(",");
                
                double lon = Double.valueOf(point[0]);
                double lat = Double.valueOf(point[1]);
                
                points.add(new Point(lon, lat, 0.0));
            }
            
            segment = new Segment(
                            list.get(0), new Polygon(points), 
                            Long.valueOf(list.get(2)), Long.valueOf(list.get(3)), 
                            new Status(
                                    list.get(9), Integer.valueOf(list.get(10)), list.get(11), 
                                    DateHandler.parseBDDDate(list.get(12)), DateHandler.parseBDDDate(list.get(13))
                                ), 
                            DateHandler.getCalendar(DateHandler.parseBDDDate(list.get(4))), 
                            DateHandler.getCalendar(DateHandler.parseBDDDate(list.get(5))), 
                            Double.valueOf(list.get(6)), 
                            Double.valueOf(list.get(7)), 
                            list.get(8),
                            new SegmentVisGS(
                                    list.get(14), 
                                    list.get(15), 
                                    DateHandler.getCalendar(DateHandler.parseBDDDate(list.get(16))), 
                                    DateHandler.getCalendar(DateHandler.parseBDDDate(list.get(17)))
                                )
                        );
        }
        
        return segment;
    }
    
//    public SensorTask getTask(
//            String sensorTaskId) {
//        
//        Status status = null;
//        
//        SensorTask task = new SensorTask(TaskType.feasibility, request, status);
//        
//        
//    }
//    
//    public Status getCurrentStatus(
//            String sensorTaskId) {
//        
//    }
//    
//    public List<Status> getAllStatus(
//            String sensorTaskId) {
//        
//    }
    
}
