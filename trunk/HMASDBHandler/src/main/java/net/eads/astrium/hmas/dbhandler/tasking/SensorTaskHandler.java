/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorTaskHandler.java
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
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.EarthObservationEquipment;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.SegmentVisGS;
import net.eads.astrium.hmas.util.structures.tasking.SensorTask;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.StatusParentType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;

/**
 *
 * @author re-sulrich
 */
public class SensorTaskHandler extends RequestHandler {

    SensorTaskHandler() {
        
        statusHandler = new StatusHandler();
    }
    
    public SensorTaskHandler(DBOperations dboperations) {
        
        super(dboperations);
        statusHandler = new StatusHandler(dboperations);
    }
    
    SensorTaskHandler(String databaseURL, String user, String pass, String schemaName) {
        
        super(databaseURL, user, pass, schemaName);
        statusHandler = new StatusHandler(databaseURL, user, pass, schemaName);
    }
    
    String createSensorTask(String sensorId, TaskType type, String mmfasTaskId, String feasibilityTaskId) throws SQLException {
        
        String status = statusHandler.createNewStatusPending(StatusParentType.SENSORTASK, type);
        
        String sensorTaskId = null;
        
        String table = "SensorTask";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("type");
        
        //Foreign keys
        fields.add("sensor");
        fields.add("status");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + type + "'");
        
        //Foreign keys
        depl1.add("'" + sensorId + "'");
        depl1.add(status);
        
        if (mmfasTaskId != null) {
            fields.add("mmfasTask");
            depl1.add("" + mmfasTaskId);
        }
        if (feasibilityTaskId != null) {
            fields.add("feasibility");
            depl1.add("" + feasibilityTaskId);
        }
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        sensorTaskId = this.getDboperations().insertReturningId(
                table, 
                fields, 
                values,
                "taskId");
        
        return sensorTaskId;
    }
    
    String createSegmentWithNewStatus(
            String sensorTaskId, 
            TaskType type, 
            Polygon polygon, 
            Date acquisitionStartTime, 
            Date acquisitionEndTime,
            long startOrbit,
            long stopOrbit,
            double cloudCoverage,
            double estimatedCost,
            String instrumentMode,
            String downloadStationId,
            Date startVisibility,
            Date stopVisibility,
            String statusIdentifier,
            int percentCompletion,
            String statusMessage,
            Date updateTime,
            Date estimatedTimeOfCompletion) throws SQLException {
    
        String statusId = statusHandler.createNewStatus(
                statusIdentifier, 
                percentCompletion, 
                statusMessage, 
                DateHandler.formatDate(updateTime), 
                DateHandler.formatDate(estimatedTimeOfCompletion));
 
        return createSegmentWithExistingStatus(sensorTaskId, type, polygon, acquisitionStartTime, acquisitionEndTime, startOrbit, stopOrbit, cloudCoverage, estimatedCost, instrumentMode, downloadStationId, startVisibility, stopVisibility, statusId);
    }
    
    String createSegmentWithExistingStatus(
            String sensorTaskId, 
            TaskType type, 
            Polygon polygon, 
            Date acquisitionStartTime, 
            Date acquisitionEndTime,
            long startOrbit,
            long stopOrbit,
            double cloudCoverage,
            double estimatedCost,
            String instrumentMode,
            String downloadStationId,
            Date startVisibility,
            Date stopVisibility,
            String statusId) throws SQLException {
        
        String table = "Segment";
        
        List<String> fields = new ArrayList<String>();
        fields.add("coordinates");
        fields.add("acquisitionStartTime");
        fields.add("acquisitionStopTime");
        fields.add("startOrbit");
        fields.add("stopOrbit");
        fields.add("cloudCoverageSuccessRate");
        fields.add("estimatedCost");
        fields.add("instrumentMode");
        
        fields.add("status");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + polygon.printCoordinatesGML() + "'");
        depl1.add("'" + type + "'");
        depl1.add("'" + DateHandler.formatDate(acquisitionStartTime) + "'");
        depl1.add("'" + DateHandler.formatDate(acquisitionEndTime) + "'");
        depl1.add("" + startOrbit);
        depl1.add("" + stopOrbit);
        depl1.add("" + cloudCoverage);
        depl1.add("" + estimatedCost);
        depl1.add("'" + instrumentMode + "'");
        
        depl1.add(statusId);
        depl1.add("'" + sensorTaskId +  "'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        String segmentId = this.getDboperations().insertReturningId(
                       table, 
                       fields, 
                       values,
                       "segmentId");
        
        createDownlinkLink(
                segmentId, 
                downloadStationId, 
                startVisibility, 
                stopVisibility);
        
        return segmentId;
    }
    
    String createSegment(
            TaskType type, String sensorTaskId, Segment segment) throws SQLException {
        
        String statusId = statusHandler.createNewStatus(
                segment.getStatus().getIdentifier(), 
                segment.getStatus().getPercentCompletion(), 
                segment.getStatus().getMessage(), 
                DateHandler.formatDate(segment.getStatus().getUpdateTime()), 
                DateHandler.formatDate(segment.getStatus().getEstimatedTimeOfCompletion())
            );
 
        String table = "Segment";
        
        List<String> fields = new ArrayList<String>();
        fields.add("coordinates");
        fields.add("acquisitionStartTime");
        fields.add("acquisitionStopTime");
        fields.add("startOrbit");
        fields.add("stopOrbit");
        fields.add("cloudCoverageSuccessRate");
        fields.add("estimatedCost");
        fields.add("instrumentMode");
        
        fields.add("status");
        
        if (type.equals(TaskType.feasibility)) {
            
            fields.add("feasibility");
        } else {
            
            fields.add("planning");
        }
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + segment.getPolygon().printCoordinatesGML() + "'");
        depl1.add("'" + DateHandler.formatDate(segment.getStartOfAcquisition().getTime()) + "'");
        depl1.add("'" + DateHandler.formatDate(segment.getEndOfAcquisition().getTime()) + "'");
        depl1.add("" + segment.getStartOrbit());
        depl1.add("" + segment.getEndOrbit());
        
        depl1.add("" + segment.getCloudCoverSuccessRate());
        depl1.add("" + segment.getEstimatedCost());
        depl1.add("'" + segment.getInstrumentMode() + "'");
        
        depl1.add(statusId);
        depl1.add("'" + sensorTaskId + "'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        String segmentId = this.getDboperations().insertReturningId(
                       table, 
                       fields, 
                       values,
                       "segmentId");
        
        createDownlinkLink(
                segmentId, 
                segment.getGroundStationDownlink().getGroundStationId(), 
                segment.getGroundStationDownlink().getStartOfVisibility().getTime(), 
                segment.getGroundStationDownlink().getEndOfVisibility().getTime());
        
        return segmentId;
    }
    
    void createDownlinkLink(String segmentId, String stationId, Date start, Date stop) throws SQLException {
        
        String table = "Downlink";
        
        List<String> fields = new ArrayList<String>();
        fields.add("beginTime");
        fields.add("endTime");
        fields.add("downlinkStation");
        fields.add("segment");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + DateHandler.formatDate(start) + "'");
        depl1.add("'" + DateHandler.formatDate(stop) + "'");
        depl1.add("'" + stationId + "'");
        depl1.add("" + segmentId + "");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        this.getDboperations().insert(
                       table, 
                       fields, 
                       values);
        
        
    }
    
    //TODO: implement method for planning requests
    private void createResult(String sensorTaskId) {
        
        
    }
    
    /**
     * Reading from database
     */
    
    public SensorTask getSensorTask(String sensorTaskId) throws SQLException, ParseException {
        SensorTask task = null;
        
        String table = "SensorTask";
        
        List<String> fields = new ArrayList<>();
        fields.add("taskId");
        fields.add("type");
        List<String> conditions = new ArrayList<>();
        
        conditions.add("taskId='"+sensorTaskId+"'");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            task = new SensorTask(
                    result.get(0).get(0), 
                    TaskType.valueOf(result.get(0).get(1)), 
                    statusHandler.getCurrentStatus(StatusParentType.SENSORTASK, result.get(0).get(0)));//Status of taskID
        }
        
        return task;
    }
    
    public List<String> getSensorTasksIds(TaskType taskType) throws SQLException, ParseException {
        List<String> tasks = null;
        
        String table = "SensorTask";
        
        List<String> fields = new ArrayList<>();
        fields.add("taskId");
        
        List<String> conditions = new ArrayList<>();
        if (taskType != null) {
            conditions.add("type='" + taskType + "'");
        }
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            for (List<String> list : result) {
                tasks.add(list.get(0));
            }
        }
        
        return tasks;
    }
    
    public Status getStatus(String sensorTaskId) throws SQLException, ParseException {
        
        return this.statusHandler.getCurrentStatus(StatusParentType.SENSORTASK, sensorTaskId);
    }
    
    
    public String getSensorType(String sensorTaskId) throws SQLException {
        
        String sensorId = null;
        
        String table = "SensorTask, Sensor";
        
        List<String> fields = new ArrayList<>();
        fields.add("Sensor.type");
        List<String> conditions = new ArrayList<>();
        
        conditions.add("SensorTask.taskId='"+sensorTaskId+"'");
        conditions.add("SensorTask.sensor=Sensor.sensorId");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            sensorId = result.get(0).get(0);
        }
        
        return sensorId;
    }
    
    public String getSensorId(String sensorTaskId) throws SQLException {
        
        String sensorId = null;
        
        String table = "SensorTask";
        
        List<String> fields = new ArrayList<>();
        fields.add("sensor");
        List<String> conditions = new ArrayList<>();
        
        conditions.add("taskId='"+sensorTaskId+"'");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            sensorId = result.get(0).get(0);
        }
        
        return sensorId;
    }
    
    public String getRequestId(String sensorTaskId) throws SQLException {
        
        String requestId = null;
        
        String table = "LNK_SensorTask_Request";
        
        List<String> fields = new ArrayList<>();
        fields.add("request");
        List<String> conditions = new ArrayList<>();
        
        conditions.add("sensorTask='"+sensorTaskId+"'");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            requestId = result.get(0).get(0);
        }
        
        return requestId;
    }
    
    
    
    public EarthObservationEquipment getEarthObservationEquipment(String sensorTaskId) throws SQLException {
        
        EarthObservationEquipment eoe = null;
        
        String table = "SensorTask, Sensor, SatellitePlatform, Orbit";
        
        List<String> fields = new ArrayList<>();
        fields.add("SatellitePlatform.satelliteId");
        fields.add("SatellitePlatform.noradName");
        fields.add("SatellitePlatform.description");
        fields.add("Orbit.orbitType");
        fields.add("Sensor.sensorId");
        fields.add("Sensor.type");
        
        List<String> conditions = new ArrayList<>();
        
        conditions.add("SensorTask.taskId='"+sensorTaskId+"'");
        conditions.add("SensorTask.sensor=Sensor.sensorId");
        conditions.add("Sensor.satelliteId=SatellitePlatform.satelliteId");
        conditions.add("SatellitePlatform.satelliteId=Orbit.satelliteId");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            List<String> list = result.get(0);
            
            eoe = new EarthObservationEquipment(
                    list.get(0), 
                    list.get(1), 
                    list.get(2), 
                    list.get(3), 
                    list.get(4), 
                    list.get(5)
                );
        }
        
        return eoe;
    }
    
    public EarthObservationEquipment getSegmentEarthObservationEquipment(String segmentID) throws SQLException {
        
        EarthObservationEquipment eoe = null;
        
        String table = "Segment, SensorTask, Sensor, SatellitePlatform, Orbit";
        
        List<String> fields = new ArrayList<>();
        fields.add("SatellitePlatform.satelliteId");
        fields.add("SatellitePlatform.noradName");
        fields.add("SatellitePlatform.description");
        fields.add("Orbit.orbitType");
        fields.add("Sensor.sensorId");
        fields.add("Sensor.type");
        
        List<String> conditions = new ArrayList<>();
        
        conditions.add("Segment.segmentId='"+segmentID+"'");
        conditions.add("(SensorTask.taskId=Segment.feasibility OR SensorTask.taskId=Segment.planning)");
        conditions.add("SensorTask.sensor=Sensor.sensorId");
        conditions.add("Sensor.satelliteId=SatellitePlatform.satelliteId");
        conditions.add("SatellitePlatform.satelliteId=Orbit.satelliteId");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            List<String> list = result.get(0);
            
            eoe = new EarthObservationEquipment(
                    list.get(0), 
                    list.get(1), 
                    list.get(2), 
                    list.get(3), 
                    list.get(4), 
                    list.get(5)
                );
        }
        
        return eoe;
    }
    
    List<Segment> getSegments(TaskType taskType, String sensorTaskId) throws SQLException, ParseException {
        
        List<Segment> segments = new ArrayList<Segment>();
        
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
        if (taskType.equals(TaskType.feasibility)) {
            conditions.add("Segment.feasibility='"+sensorTaskId+"'");
        }
        else {
            conditions.add("Segment.planning='"+sensorTaskId+"'");
        }
        
        conditions.add("Segment.status=Status.statusId");
        conditions.add("Segment.segmentId=Downlink.segment");
        conditions.add("GroundStation.groundStationId=Downlink.downlinkStation");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && !result.isEmpty())
        for (List<String> list : result) {
            List<Point> points = new ArrayList<>();
            
            String[] coords = list.get(1).split(" ");
            for (int i = 0; i < coords.length; i++) {
                String[] point = coords[i].split(",");
                
                double lon = Double.valueOf(point[0]);
                double lat = Double.valueOf(point[1]);
                
                points.add(new Point(lon, lat, 0.0));
            }
            
            segments.add(
                    new Segment(
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
                        )
                );
        }
        
        
        return segments;
    }
}
