/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorPlanningHandler.java
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
import net.eads.astrium.hmas.util.structures.tasking.Product;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;

/**
 *
 * @author re-sulrich
 */
public class SensorPlanningHandler extends SensorTaskHandler {
    
    public SensorPlanningHandler() throws SQLException {
        
        super();
    }
    
    public SensorPlanningHandler(DBOperations dboperations) {
        
        super(dboperations);
    }
    
    public SensorPlanningHandler(String databaseURL, String user, String pass, String schemaName) {
        
        super(databaseURL, user, pass, schemaName);
    }
    
    public String createPlanningTaskFromFeasibility(
            String feasibilityTaskId) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(
                this.getSensorId(feasibilityTaskId), 
                TaskType.planning, 
                feasibilityTaskId);
        
        this.linkSensorTaskToRequest(sensorTaskId, getRequestId(feasibilityTaskId));
        
        return sensorTaskId;
    }
    
    public String createOPTPlanningTaskFromMMFASTask(
            String sensorId, String requestId) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.planning, null);
        this.linkSensorTaskToRequest(sensorTaskId, requestId);
        
        return sensorTaskId;
    }
    
    public String createSARPlanningTaskFromMMFASTask(
            String sensorId, String requestId) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.planning, null);
        this.linkSensorTaskToRequest(sensorTaskId, requestId);
        
        return sensorTaskId;
    }
    
    public String createSARPlanningTask(
            String sensorId, SARTaskingParameters parameters) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.planning, null);
        this.saveSARSubmitRequest(sensorTaskId, parameters);
        
        return sensorTaskId;
    }
    
    public String createOPTPlanningTask(
            String sensorId, OPTTaskingParameters parameters) throws SQLException {
        
        String sensorTaskId = this.createSensorTask(sensorId, TaskType.planning, null);
        this.saveOPTSubmitRequest(sensorTaskId, parameters);
        
        return sensorTaskId;
    }
    
    
    private void saveSARSubmitRequest(
            String sensorTaskId,
            SARTaskingParameters parameters) throws SQLException {
        
        this.saveSARTaskingRequest(TaskHandlerType.sensor, sensorTaskId, RequestType.submit, parameters);
    }
    
    private void saveOPTSubmitRequest(
            String sensorTaskId,
            OPTTaskingParameters parameters) throws SQLException {
        
        this.saveOPTTaskingRequest(TaskHandlerType.sensor, sensorTaskId, RequestType.submit, parameters);
    }
    
    private void saveCancelRequest(
            String sensorTaskId) throws SQLException {
        
        this.createRequest(TaskHandlerType.sensor, sensorTaskId, RequestType.cancel);
    }
    
    public void setSegmentPlanningTask(String segmentId, String sensorPlanningTaskId) throws SQLException {
        
        String table = "Segment";
        
        List<String> fields = new ArrayList<>();
        fields.add("planning");
        
        List<String> values = new ArrayList<>();
        values.add("" + sensorPlanningTaskId);
        
        String condition = "segmentId='" +segmentId+ "'";
        
        this.getDboperations().update(table, fields, values, condition);
    }
    
    
    public Status addNewCancelledStatus(String sensorTaskId) throws SQLException {
        
        int percentCompletion = 0;
        return this.addNewCancelledStatus(sensorTaskId, percentCompletion);
    }
    
    public Status addNewCancelledStatus(String sensorTaskId, int percentCompletion) throws SQLException {
        
        String statusIdentifier = "PLANNING CANCELLED";
        String message = "Planning has been cancelled by the user.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, now);
        
        String table = "SensorTask";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "taskId='" +sensorTaskId+ "'";
        
        //SELECT status FROM SensorTask WHERE taskId=<sensorTaskId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the task
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(now), 
                oldStatus);
        //Add new statusId to the values to update in the SensorTask table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status addNewFailedStatus(String sensorTaskId) throws SQLException {
        
        int percentCompletion = 0;
        String statusIdentifier = "PLANNING FAILED";
        String message = "Planning has failed because no segments can be acquired.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, now);
        
        String table = "SensorTask";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "taskId='" +sensorTaskId+ "'";
        
        //SELECT status FROM SensorTask WHERE taskId=<sensorTaskId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the task
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(now), 
                oldStatus);
        //Add new statusId to the values to update in the SensorTask table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status addNewAcceptedStatus(String sensorTaskId, int percentCompletion, Date estimatedTimeOfCompletion) throws SQLException {
        
//        int percentCompletion = 100;
        String statusIdentifier = "PLANNING ACCEPTED";
        String message = "Planning is accepted and will be acquired.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, estimatedTimeOfCompletion);
        
        String table = "SensorTask";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "taskId='" +sensorTaskId+ "'";
        
        //SELECT status FROM SensorTask WHERE taskId=<sensorTaskId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the task
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(estimatedTimeOfCompletion), 
                oldStatus);
        //Add new statusId to the values to update in the SensorTask table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status addNewFinishedStatus(String sensorTaskId, int percentCompletion, Date estimatedTimeOfCompletion) 
            throws SQLException {
        
//        int percentCompletion = 100;
        String statusIdentifier = "PLANNING COMPLETED";
        String message = "Planning is completed. "
                + "Products are now available at the URL given by the DescribeResultAccess "
                + "and in the download managers.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, estimatedTimeOfCompletion);
        
        String table = "SensorTask";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "taskId='" +sensorTaskId+ "'";
        
        //SELECT status FROM SensorTask WHERE taskId=<sensorTaskId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the task
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(estimatedTimeOfCompletion), 
                oldStatus);
        //Add new statusId to the values to update in the SensorTask table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status setSegmentAcquiredStatus(String segmentId, Date estimatedTimeOfCompletion) throws SQLException {
        
        int percentCompletion = 100;
        String statusIdentifier = "ACQUIRED";
        String message = "This segment is acquired for acquisition.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, estimatedTimeOfCompletion);
        
        String table = "Segment";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "segmentId='" +segmentId+ "'";
        
        //SELECT status FROM Segment WHERE segmentId=<segmentId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the segment
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(estimatedTimeOfCompletion), 
                oldStatus);
        //Add new statusId to the values to update in the Segment table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status setSegmentPlannedStatus(String segmentId, Date estimatedTimeOfCompletion) throws SQLException {
        
        int percentCompletion = 100;
        String statusIdentifier = "PLANNED";
        String message = "This segment is planned for acquisition.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, estimatedTimeOfCompletion);
        
        String table = "Segment";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "segmentId='" +segmentId+ "'";
        
        //SELECT status FROM Segment WHERE segmentId=<segmentId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the segment
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(estimatedTimeOfCompletion), 
                oldStatus);
        //Add new statusId to the values to update in the Segment table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status setSegmentFailedStatus(String segmentId) throws SQLException {
        
        int percentCompletion = 0;
        String statusIdentifier = "FAILED";
        String message = "This segment acquisition has failed.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, now);
        
        String table = "Segment";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "segmentId='" +segmentId+ "'";
        
        //SELECT status FROM Segment WHERE segmentId=<segmentId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the segment
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(now), 
                oldStatus);
        //Add new statusId to the values to update in the Segment table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status setSegmentCancelledStatus(String segmentId) throws SQLException {
        
        int percentCompletion = 0;
        String statusIdentifier = "CANCELLED";
        String message = "This segment acquisition has been cancelled by the user.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, now);
        
        String table = "Segment";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "segmentId='" +segmentId+ "'";
        
        //SELECT status FROM Segment WHERE segmentId=<segmentId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the segment
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(now), 
                oldStatus);
        //Add new statusId to the values to update in the Segment table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    public Status setSegmentRejectedStatus(String segmentId) throws SQLException {
        
        int percentCompletion = 0;
        String statusIdentifier = "REJECTED";
        String message = "This segment has been rejected by the mission planning service.";
        Date now = Calendar.getInstance().getTime();
        Status status = new Status(statusIdentifier, percentCompletion, message, now, now);
        
        String table = "Segment";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "segmentId='" +segmentId+ "'";
        
        //SELECT status FROM Segment WHERE segmentId=<segmentId>
        String oldStatus = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        //Create new status with the previous of the segment
        String statusId = this.statusHandler.createNewStatusWithPrevious(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(now), 
                oldStatus);
        //Add new statusId to the values to update in the Segment table
        List<String> values = new ArrayList<>();
        values.add(statusId);
        
        this.getDboperations().update(table, fields, values, condition);
        
        return status;
    }
    
    /**
     * reading from the database
     */
    
    
    /**
     * Returns the base address of the MP's application server base address
     * @return
     * @throws SQLException 
     */
    public String getSensorTaskMPBaseAddress(String sensorTaskId) throws SQLException {
        
        String baseAddr = null;
        
        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("MissionPlanner.mpId");
        fields.add("ApplicationServer.serverBaseAddress");

        String table = "MissionPlanner, ApplicationServer,Sensor,SensorTask";
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("SensorTask.taskId= '"+sensorTaskId+"'");
        conditions.add("Sensor.sensorId= SensorTask.sensor");
        conditions.add("Sensor.satelliteId= MissionPlanner.satelliteId");
        conditions.add("ApplicationServer.asId= MissionPlanner.asId");
        
        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            
            List<String> list = result.get(0);

            String fasId = list.get(0);
            String mpHref = list.get(1);

            if (mpHref != null) {
                if (!mpHref.endsWith("/")) mpHref += "/";
                baseAddr = mpHref;
            }
        }
        
        return baseAddr;
    }
    
    
    public List<String> getFeasibilityPlanningTasks(String sensorFeasibilityTaskId) throws SQLException {
        
        List<String> planningTasks = new ArrayList<>();

        String table = "SensorTask";
        
        List<String> fields = new ArrayList<String>();
        fields.add("taskId");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("feasibility='"+sensorFeasibilityTaskId+"'");
            
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            for (List<String> list : result) {
                planningTasks.add(list.get(0));
            }
        }
        return planningTasks;
        
    }
    
    
    public String getFeasibilityTaskId(String sensorPlanningTaskId) throws SQLException {
        
        String mmfasTaskId = null;
        
        String table = "SensorTask";
        
        List<String> fields = new ArrayList<>();
        fields.add("feasibility");
        List<String> conditions = new ArrayList<>();
        
        conditions.add("taskId='"+sensorPlanningTaskId+"'");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            mmfasTaskId = result.get(0).get(0);
        }
        
        return mmfasTaskId;
    }
    
    public String getMissionPlannerDARMAHref(String missionPlannerId) throws SQLException {
        
        String darmaHref = null;
        
        String table = "MissionPlanner";
        
        List<String> fields = new ArrayList<>();
        fields.add("DARMAHref");
        
        List<String> conditions = new ArrayList<>();
        conditions.add("mpId='"+missionPlannerId+"'");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            darmaHref = result.get(0).get(0);
        }
        
        return darmaHref;
    }
    
    public String getSensorDARMAHref(String sensorId) throws SQLException {
        
        String darmaHref = null;
        
        String table = "Sensor, MissionPlanner";
        
        List<String> fields = new ArrayList<>();
        fields.add("MissionPlanner.DARMAHref");
        
        List<String> conditions = new ArrayList<>();
        conditions.add("Sensor.sensorId='"+sensorId+"'");
        conditions.add("Sensor.satelliteId=MissionPlanner.satelliteId");
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0){
            darmaHref = result.get(0).get(0);
        }
        
        return darmaHref;
    }
    
    public List<Segment> getSegments(String taskId) throws SQLException, ParseException {
        return this.getSegments(TaskType.planning, taskId);
    }
    
    
    
    /**
     * Product handling
     */
    
    public List<Product> getSegmentProducts(String segmentID) throws SQLException, ParseException {
        
        List<Product> prods = new ArrayList<>();
        
        List<String> fields = new ArrayList<>();
        fields.add("productID");
        fields.add("segmentId");
        fields.add("downloadURL");
        fields.add("availibility");
        fields.add("lastUpdateTime");
        fields.add("size");
        
        String table = "Product";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<>();
        conditions.add("segmentId='" + segmentID + "'");
        
        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            for (List<String> list : result) {
                prods.add(new Product(
                    list.get(0), 
                    list.get(1),                               //mmfasSegmentID
                    list.get(2),                               //downloadURL
                    list.get(3).equalsIgnoreCase("true"),      //IsAvailable
                    DateHandler.parseBDDDate(list.get(4)),    //lastUpdateTime
                    Long.valueOf(list.get(5))));                 //size);
            }
        }
        
        return prods;        
    }
    
    
    
    public String addProduct(String downloadURL, String mmfasSegmentID, boolean isAvailable, long size, Date lastUpdateTime) throws SQLException {
        
        String productID = null;
        
        String table = "Product";

        List<String> fields = new ArrayList<String>();

        fields.add("downloadURL");
        fields.add("availibility");
        fields.add("size");
        fields.add("lastUpdateTime");

        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + downloadURL + "'");
        depl1.add("'" + isAvailable + "'");
        depl1.add("'" + size + "'");
        depl1.add("'" + DateHandler.formatDate(lastUpdateTime) + "'");

        if (mmfasSegmentID != null && !mmfasSegmentID.equals("")) {
            
            fields.add("segmentID");
            depl1.add("'" + mmfasSegmentID + "'");
        }

        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);

        productID = this.getDboperations().insertReturningId(
                table,
                fields,
                values,
                "productID");

        return productID;
    }
    
    public void setSegmentProductsAvailibility(String segmentID, boolean isAvailable) throws SQLException {
        
        String table = "Product";
        
        List<String> fields = new ArrayList<>();
        fields.add("availibility");
        fields.add("lastUpdateTime");
        
        List<String> values = new ArrayList<>();
        values.add("'" + isAvailable + "'");
        values.add("'" + DateHandler.formatDate(Calendar.getInstance().getTime()) + "'");
        
        String condition = "mmfasSegmentID='" +segmentID+ "'";
        
        this.getDboperations().update(table, fields, values, condition);
    }
    
}
