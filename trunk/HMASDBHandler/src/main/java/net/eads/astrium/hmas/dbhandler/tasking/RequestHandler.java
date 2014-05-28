/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               RequestHandler.java
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
import net.eads.astrium.hmas.dbhandler.DatabaseLoader;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.Request;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.TaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;
import net.eads.astrium.hmas.util.structures.tasking.enums.StatusParentType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Circle;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Geometry;

/**
 *
 * @author re-sulrich
 */
public class RequestHandler extends DatabaseLoader {

    StatusHandler statusHandler;
    
    public RequestHandler() {
        
        super("MissionPlannerDatabase");
        statusHandler = new StatusHandler();
    }
    
    public RequestHandler(DBOperations dboperations) {
        
        super(dboperations);
        statusHandler = new StatusHandler(dboperations);
    }
    
    public RequestHandler(String databaseURL, String user, String pass, String schemaName) {
        super(databaseURL, user, pass, schemaName);
        
        statusHandler = new StatusHandler(databaseURL, user, pass, schemaName);
    }
    
    public Status addNewGetFeasibilityRequestSucceddedStatus(TaskHandlerType handlerType, String taskId) throws SQLException, ParseException {
        return this.addRequestStatus(handlerType, taskId, RequestType.getFeasibility, true);
    }
    
    public Status addNewGetFeasibilityRequestFailedStatus(TaskHandlerType handlerType, String taskId) throws SQLException, ParseException {
        return this.addRequestStatus(handlerType, taskId, RequestType.getFeasibility, false);
    }
    
    public Status addNewSubmitSegmentsRequestSucceddedStatus(TaskHandlerType handlerType, String taskId) throws SQLException, ParseException {
        return this.addRequestStatus(handlerType, taskId, RequestType.submitSegmentByID, true);
    }
    
    public Status addNewSubmitSegmentsRequestFailedStatus(TaskHandlerType handlerType, String taskId) throws SQLException, ParseException {
        return this.addRequestStatus(handlerType, taskId, RequestType.submitSegmentByID, false);
    }
    
    public Status addNewCancelRequestSucceddedStatus(TaskHandlerType handlerType, String taskId) throws SQLException, ParseException {
        return this.addRequestStatus(handlerType, taskId, RequestType.cancel, true);
    }
    
    public Status addNewCancelRequestFailedStatus(TaskHandlerType handlerType, String taskId) throws SQLException, ParseException {
        return this.addRequestStatus(handlerType, taskId, RequestType.cancel, false);
    }
    
    private Status addRequestStatus(TaskHandlerType handlerType, String taskId, RequestType reqType, boolean isSuccedded) throws SQLException, ParseException {
        Request req = this.getRequest(handlerType, taskId, null, reqType);
        Date now = Calendar.getInstance().getTime();
        String identifier = reqType.name().toUpperCase();
        if (isSuccedded)
            identifier += " SUCCEDDED";
        else 
            identifier += " FAILED";
        
        return this.addRequestStatus(req.getRequestId(), new Status(identifier, 100, identifier, now, now));
    }
    
    private Status addRequestStatus(String requestId, Status status) throws SQLException, ParseException {
        
        String table = "Request";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        String condition = "requestId='" +requestId+ "'";
        
        //SELECT status FROM Request WHERE requestId=<requestId>
        String oldStatusId = this.select(fields, table, Arrays.asList(new String[]{condition})).get(0).get(0);
        
        String newStatusId = this.statusHandler.createNewStatusWithPrevious(
                status.getIdentifier(), 
                status.getPercentCompletion(), 
                status.getMessage(), 
                DateHandler.formatDate(status.getUpdateTime()), 
                DateHandler.formatDate(status.getEstimatedTimeOfCompletion()),
                oldStatusId);
        
        this.setRequestStatusId(requestId, newStatusId);
        
        return status;
    }
    
    private void setRequestStatusId(String requestId, String statusId) throws SQLException {
        
        String table = "Request";
        List<String> fields = new ArrayList<>();
        fields.add("status");
        List<String> values = new ArrayList<>();
        values.add("" + statusId);
        String condition = "requestId='" +requestId+ "'";
        
        this.getDboperations().update(table, fields, values, condition);
    }
    
    public String saveSARTaskingRequest(
            TaskHandlerType handlerType, 
            String taskId, 
            RequestType type, 
            SARTaskingParameters params) throws SQLException {
        
        String requestId = null;
        
        requestId = createRequest(handlerType, taskId, type);
        
        createSARTaskingParameters(params, requestId);
        
        return requestId;
    }
    
    public String saveOPTTaskingRequest(
            TaskHandlerType handlerType, 
            String taskId, 
            RequestType type, 
            OPTTaskingParameters params) throws SQLException {
        
        String requestId = null;
        
        requestId = createRequest(handlerType, taskId, type);
        
        createOPTTaskingParameters(params, requestId);
        
        return requestId;
    }
    
    
    
    public String createRequest(TaskHandlerType handlerType, String taskId, RequestType requestType) throws SQLException {
        
        String requestId = null;
        TaskType taskType = null;
        if (requestType.equals(RequestType.getFeasibility)) {
            taskType = TaskType.feasibility;
        }
        else {
            taskType = TaskType.planning;
        }
        
        String status = statusHandler.createRequestNewStatusPending(StatusParentType.REQUEST, taskType, requestType);
        
        String table = "Request";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("type");
        fields.add("incomingTime");
        fields.add("status");
        
        List<String> depl1 = new ArrayList<String>();
        //Add request type = getfeasibility, submit,...
        depl1.add("'"+requestType+"'");
        //Add incomingTime of the request = now
        depl1.add("'" + DateHandler.formatDate(Calendar.getInstance().getTime()) + "'");
        //Add statusId from the new created status
        depl1.add("'"+status+"'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        requestId = this.getDboperations().insertReturningId(
                       table, 
                       fields, 
                       values,
                       "requestId");
        
        if (handlerType.equals(TaskHandlerType.sensor))
        {
            linkSensorTaskToRequest(taskId, requestId);
        }
        
        if (handlerType.equals(TaskHandlerType.mmfas))
        {
            linkMMFASTaskToRequest(taskId, requestId);
        }
        
        return requestId;
    }
    
    
    
    
    
    
    public void linkMMFASTaskToRequest(String mmfasTaskId, String requestId) throws SQLException {
        
        String t = "LNK_MMFASTask_Request";
            
        List<String> f = new ArrayList<String>();
        f.add("mmfasTask");
        f.add("request");

        List<String> d = new ArrayList<String>();
        d.add("'"+mmfasTaskId+"'");
        d.add("'"+requestId+"'");

        List<List<String>> v = new ArrayList<List<String>>();
        v.add(d);

        this.getDboperations().insert(
                   t, 
                   f, 
                   v);
    }
    
    
    
    
    
    public void linkSensorTaskToRequest(String sensorTaskId, String requestId) throws SQLException {
        
        String t = "LNK_SensorTask_Request";
            
        List<String> f = new ArrayList<String>();
        f.add("sensorTask");
        f.add("request");

        List<String> d = new ArrayList<String>();
        d.add("'"+sensorTaskId+"'");
        d.add("'"+requestId+"'");

        List<List<String>> v = new ArrayList<List<String>>();
        v.add(d);

        this.getDboperations().insert(
                   t, 
                   f, 
                   v);
    }
    
    private String createTaskingParameters(TaskingParameters params, String requestId) throws SQLException {
        
        
        String tpId = null;
        
        String table = "TaskingParameters";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("incidenceAngleAzimuth");
        fields.add("incidenceAngleElevation");
        fields.add("pointingAngleAcross");
        fields.add("pointingAngleAlong");
        fields.add("coordinates");
        fields.add("roiType");
        fields.add("beginTime");
        fields.add("endTime");
        fields.add("groundResolution");
        
        fields.add("request");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'"+params.getMinIncidenceAngleAzimuth()+"," + params.getMaxIncidenceAngleAzimuth() +  "'");
        depl1.add("'"+params.getMinIncidenceAngleElevation()+"," + params.getMaxIncidenceAngleElevation() +  "'");
        depl1.add("'"+params.getMinPointingAngleAcross()+"," + params.getMaxPointingAngleAcross() +  "'");
        depl1.add("'"+params.getMinPointingAngleAlong()+"," + params.getMaxPointingAngleAlong() +  "'");
        depl1.add("'"+params.getCoordinates().printCoordinatesGML()+"'");
        depl1.add("'"+params.getRoiType()+"'");
        depl1.add("'"+params.getTimes().get(0).getBegin()+"'");
        depl1.add("'"+params.getTimes().get(params.getTimes().size() - 1).getEnd()+"'");
        depl1.add("'"+params.getMinGroundResolution()+","+params.getMaxGroundResolution()+"'");
        
        depl1.add("'"+requestId+"'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);

        tpId = this.getDboperations().insertReturningId(
                   table, 
                   fields, 
                   values, 
                   "tpId");
        
        if (params.getInstrumentModes() != null)
        for (String instMode : params.getInstrumentModes()) {
            String t = "TaskingParametersInstrumentModes";
        
            List<String> f = new ArrayList<String>();

            f.add("params");
            f.add("instrumentMode");


            List<String> d = new ArrayList<String>();
            d.add("'"+tpId+"'");
            d.add("'"+instMode+"'");


            List<List<String>> v = new ArrayList<List<String>>();
            v.add(d);

            this.getDboperations().insert(
                           t, 
                           f, 
                           v);
        }
        return tpId;
    }
    
    private String createOPTTaskingParameters(OPTTaskingParameters params, String requestId) throws SQLException {
        
        String tpId = null;
        
        tpId = createTaskingParameters((TaskingParameters)params, requestId);
        
        String table = "OPTTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
       
        fields.add("minLuminosity");
        fields.add("fusionAccepted");
        fields.add("maxCloudCover");
        fields.add("maxSnowCover");
        fields.add("maxSunGlint");
        fields.add("hazeAccepted");
        fields.add("sandWindAccepted");
        
        fields.add("params");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add(""+(int)params.getMinLuminosity()+"");
        depl1.add(""+params.getFusionAccepted()+"");
        depl1.add(""+(int)params.getMaxCloudCover()+"");
        depl1.add(""+(int)params.getMaxSnowCover()+"");
        depl1.add(""+(int)params.getMaxSunGlint()+"");
        depl1.add(""+params.getHazeAccepted()+"");
        depl1.add(""+params.getSandWindAccepted()+"");
        
        depl1.add("'"+tpId+"'");
        
        if (params.getMeteoServerId() != null && !params.getMeteoServerId().equals("")) {
            fields.add("msId");
            depl1.add("'"+params.getMeteoServerId()+"'");
        }
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
            
        this.getDboperations().insert(
                       table, 
                       fields, 
                       values);
        
        return tpId;
    }
    
    private String createSARTaskingParameters(SARTaskingParameters params, String requestId) throws SQLException {
        
        String tpId = null;
        
        tpId = createTaskingParameters((TaskingParameters)params, requestId);
        
        String table = "SARTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
       
        fields.add("fusionAccepted");
        fields.add("maxNoiseLevel");
        fields.add("maxAmbiguityLevel");
        
        fields.add("params");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'"+params.getFusionAccepted()+"'");
        depl1.add(""+params.getMaxNoiseLevel()+"");
        depl1.add(""+params.getMaxAmbiguityLevel()+"");
        
        depl1.add("'"+tpId+"'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        String sarTpId = this.getDboperations().insertReturningId(
                              table, 
                              fields, 
                              values, 
                              "sarTPId");
        
        
        if (params.getPolarisationModes() != null)
        for (String polMode : params.getPolarisationModes()) {
            String t = "SARTaskingParametersPolarisationModes";
        
            List<String> f = new ArrayList<String>();

            f.add("params");
            f.add("polarisationMode");

            List<String> d = new ArrayList<String>();
            d.add("'"+sarTpId+"'");
            d.add("'"+polMode+"'");

            List<List<String>> v = new ArrayList<List<String>>();
            v.add(d);

            this.getDboperations().insert(
                           t, 
                           f, 
                           v);
        }
        
        return tpId;
    }
    
    
    
    
    /**
     * Reading from the database
     */
    
    public Date getTaskingRequestBeginTime(TaskHandlerType taskType, String taskId) throws SQLException, ParseException {
        
        Date beginTime = null;
        
        String table = "Request,TaskingParameters";
        if (taskType.equals(TaskHandlerType.mmfas)) 
            table += ",LNK_MMFASTask_Request";
        else 
            table += ",LNK_SensorTask_Request";
        
        List<String> fields = new ArrayList<String>();
        fields.add("TaskingParameters.beginTime");
        
        List<String> conditions = new ArrayList<String>();
        if (taskType.equals(TaskHandlerType.mmfas))  {
            conditions.add("LNK_MMFASTask_Request.mmfasTask="+taskId+"");
            conditions.add("Request.requestId=LNK_MMFASTask_Request.request");
        } 
        else {
            conditions.add("LNK_SensorTask_Request.sensorTask="+taskId+"");
            conditions.add("Request.requestId=LNK_SensorTask_Request.request");
        }
        conditions.add("Request.requestId=TaskingParameters.request");
        List<List<String>> result = select(fields, table, conditions);
        if (result != null && !result.isEmpty()) {
            beginTime = DateHandler.parseBDDDate(result.get(0).get(0));
        }
        
        return beginTime;
    }
    
    
    public String getRequestSensorType(String requestId) throws SQLException {
        
        String type = null;
        
        String table = "TaskingParameters, "
                + "SARTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
        fields.add("sarTpId");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("TaskingParameters.request="+requestId+"");
        
        conditions.add("TaskingParameters.tpId=SARTaskingParameters.params");
        
        int resultSAR = count(table, conditions);
        if (resultSAR > 0) {
            type = "SAR";
        }
        else {
            table = "TaskingParameters, "
                + "OPTTaskingParameters";
        
            fields = new ArrayList<String>();
            fields.add("optTpId");

            conditions = new ArrayList<String>();
            conditions.add("TaskingParameters.request="+requestId+"");

            conditions.add("TaskingParameters.tpId=OPTTaskingParameters.params");

            int resultOPT = count(table, conditions);
            if (resultOPT > 0) {
                type = "OPT";
            }
        }
        
        return type;
    }
    
    public String getMMFASRequestSensorType(String mmfasTaskId) throws SQLException {
        
        String type = null;
        
        String table = "LNK_MMFASTask_Request, Request, TaskingParameters, "
                + "SARTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
        fields.add("sarTpId");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("TaskingParameters.request=Request.requestId");
        conditions.add("Request.requestId=LNK_MMFASTask_Request.request");
        conditions.add("LNK_MMFASTask_Request.mmfasTask="+mmfasTaskId+"");
        conditions.add("Request.type IN ('getFeasibility', 'submit')");
        
        conditions.add("TaskingParameters.tpId=SARTaskingParameters.params");
        
        int resultSAR = count(table, conditions);
        if (resultSAR > 0) {
            type = "SAR";
        }
        else {
            table = "LNK_MMFASTask_Request, Request, TaskingParameters, "
                + "OPTTaskingParameters";
        
            fields = new ArrayList<String>();
            fields.add("optTpId");

            conditions = new ArrayList<String>();
            conditions.add("TaskingParameters.request=Request.requestId");
            conditions.add("Request.requestId=LNK_MMFASTask_Request.request");
            conditions.add("LNK_MMFASTask_Request.mmfasTask="+mmfasTaskId+"");
            conditions.add("Request.type IN ('getFeasibility', 'submit')");

            conditions.add("TaskingParameters.tpId=OPTTaskingParameters.params");

            int resultOPT = count(table, conditions);
            if (resultOPT > 0) {
                type = "OPT";
            }
        }
        
        return type;
    }
    
    public String getSensorRequestSensorType(String sensorTaskId) throws SQLException {
        
        String type = null;
        
        String table = "LNK_SensorTask_Request, Request, TaskingParameters, "
                + "SARTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
        fields.add("sarTpId");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("TaskingParameters.request=Request.requestId");
        conditions.add("Request.requestId=LNK_SensorTask_Request.request");
        conditions.add("LNK_SensorTask_Request.sensorTask="+sensorTaskId+"");
        conditions.add("Request.type IN ('getFeasibility', 'submit')");
        
        conditions.add("TaskingParameters.tpId=SARTaskingParameters.params");
        
        int resultSAR = count(table, conditions);
        if (resultSAR > 0) {
            type = "SAR";
        }
        else {
            table = "LNK_SensorTask_Request, Request, TaskingParameters, "
                + "OPTTaskingParameters";
        
            fields = new ArrayList<String>();
            fields.add("optTpId");

            conditions = new ArrayList<String>();
            conditions.add("TaskingParameters.request=Request.requestId");
            conditions.add("Request.requestId=LNK_SensorTask_Request.request");
            conditions.add("LNK_SensorTask_Request.sensorTask="+sensorTaskId+"");
            conditions.add("Request.type IN ('getFeasibility', 'submit')");

            conditions.add("TaskingParameters.tpId=OPTTaskingParameters.params");

            int resultOPT = count(table, conditions);
            if (resultOPT > 0) {
                type = "OPT";
            }
        }
        
        return type;
    }
    
    public Request getRequest(String requestId) throws SQLException, ParseException {
        
        String table = "Request";
        
        List<String> fields = new ArrayList<>();
        fields.add("requestId");
        fields.add("incomingTime");
        fields.add("type");
        
        List<String> conditions = new ArrayList<>();
        
        conditions.add("requestId=" + requestId);
        
        Request request = null;
        
        List<List<String>> result = select(fields, table, conditions);
        if (result != null && !result.isEmpty()) {
            List<String> list = result.get(0);

            request = new Request(
                    list.get(0),                                                                    //RequestID
                    DateHandler.parseBDDDate(list.get(1)),                                          //Incoming Time
                    RequestType.valueOf(list.get(2)),                                               //Request Type : getFeasibility, submit, ...
                    this.statusHandler.getCurrentStatus(StatusParentType.REQUEST, list.get(0)));    //Get Status from requestID with the StatusHandler
        }
        return request;
    }
    
    public Request getRequest(TaskHandlerType type, String taskId, String requestId) throws SQLException, ParseException {
        return this.getRequest(type, taskId, requestId, null);
    }
    
    public Request getRequest(TaskHandlerType type, String taskId, String requestId, RequestType reqType) throws SQLException, ParseException {
        
        String table = "Request";
        
        List<String> fields = new ArrayList<>();
        fields.add("requestId");
        fields.add("incomingTime");
        fields.add("type");
        
        List<String> conditions = new ArrayList<>();
        String taskCondition = null;
        
        if (type == TaskHandlerType.mmfas) {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_MMFASTask_Request WHERE mmfasTask = '"+taskId+"'"
                    + ")";
        }
        else {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_SensorTask_Request WHERE sensorTask = '"+taskId+"'"
                    + ")";
        }
        conditions.add(taskCondition);
        
        conditions.add("incomingTime = (SELECT max(incomingTime) FROM Request WHERE " + taskCondition + " )");
        
        if (requestId != null)
            conditions.add("requestId=" + requestId);
        
        if (reqType != null) {
            conditions.add("type='" + reqType + "'");
        }
        
        for (String string : conditions) {
            System.out.println("Condition : " + string);
        }
        
        Request request = null;
        
        List<List<String>> result = select(fields, table, conditions);
        if (result != null && !result.isEmpty()) {
            List<String> list = result.get(0);

            request = new Request(
                    list.get(0),                                                                    //RequestID
                    DateHandler.parseBDDDate(list.get(1)),                                          //Incoming Time
                    RequestType.valueOf(list.get(2)),                                               //Request Type : getFeasibility, submit, ...
                    this.statusHandler.getCurrentStatus(StatusParentType.REQUEST, list.get(0)));    //Get Status from requestID with the StatusHandler
    
        }
        return request;
    }
    
    public Request getTaskingRequest(TaskHandlerType type, String taskId, String requestId) throws SQLException, ParseException {
        
        String table = "Request";
        
        List<String> fields = new ArrayList<>();
        fields.add("requestId");
        fields.add("incomingTime");
        fields.add("type");
        
        List<String> conditions = new ArrayList<>();
        String taskCondition = null;
        
        if (type == TaskHandlerType.mmfas) {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_MMFASTask_Request WHERE mmfasTask = '"+taskId+"'"
                    + ")";
        }
        else {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_SensorTask_Request WHERE sensorTask = '"+taskId+"'"
                    + ")";
        }
        conditions.add(taskCondition);
        
//        conditions.add("incomingTime = (SELECT max(incomingTime) FROM Request WHERE " + taskCondition + " )");
        conditions.add("Request.type IN ('getFeasibility', 'submit')");
        
        if (requestId != null)
            conditions.add("requestId=" + requestId);
        
        for (String string : conditions) {
            System.out.println("Condition : " + string);
        }
        
        Request request = null;
        
        List<List<String>> result = select(fields, table, conditions);
        if (result != null && !result.isEmpty()) {
            List<String> list = result.get(0);

            request = new Request(
                    list.get(0),                                                                    //RequestID
                    DateHandler.parseBDDDate(list.get(1)),                                          //Incoming Time
                    RequestType.valueOf(list.get(2)),                                               //Request Type : getFeasibility, submit, ...
                    this.statusHandler.getCurrentStatus(StatusParentType.REQUEST, list.get(0)));    //Get Status from requestID with the StatusHandler
    
        }
        return request;
    }
    
    
    
    public OPTTaskingRequest getOPTRequest(TaskHandlerType type, String taskId, String requestId) throws ParseException, SQLException {
        
        String table = "Request, TaskingParameters, OPTTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
        fields.add("requestId");
        fields.add("incomingTime");
        fields.add("type");
        
        fields.add("incidenceAngleAzimuth");
        fields.add("incidenceAngleElevation");
        fields.add("pointingAngleAcross");
        fields.add("pointingAngleAlong");
        fields.add("coordinates");
        fields.add("roiType");
        fields.add("beginTime");
        fields.add("endTime");
        fields.add("groundResolution");
        
        fields.add("minLuminosity");
        fields.add("fusionAccepted");
        fields.add("maxCloudCover");
        fields.add("maxSnowCover");
        fields.add("maxSunGlint");
        fields.add("hazeAccepted");
        fields.add("sandWindAccepted");
        
        //Getting the taskingParameters ID to get the Instrument Modes from the join table
        fields.add("tpId");
        fields.add("msId");
        
        List<String> conditions = new ArrayList<String>();
        String taskCondition = null;
        
        if (type == TaskHandlerType.mmfas) {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_MMFASTask_Request WHERE mmfasTask = '"+taskId+"'"
                    + ")";
        }
        else {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_SensorTask_Request WHERE sensorTask = '"+taskId+"'"
                    + ")";
        }
        conditions.add(taskCondition);
        
//        conditions.add("incomingTime = (SELECT max(incomingTime) FROM Request WHERE " + taskCondition + " )");
        
        if (requestId != null)
            conditions.add("requestId=" + requestId);
        
        //Links between tables
        conditions.add("Request.requestId=TaskingParameters.request");
        conditions.add("TaskingParameters.tpId=OPTTaskingParameters.params");
        conditions.add("Request.type IN ('getFeasibility', 'submit')");
        
        List<List<String>> result = select(fields, table, conditions);
        List<String> list = result.get(0);
        
        OPTTaskingRequest response = new OPTTaskingRequest(
                                  list.get(0),                                                                    //RequestID
                                  DateHandler.parseBDDDate(list.get(1)),                                          //Incoming Time
                                  RequestType.valueOf(list.get(2)),                                               //Request Type : getFeasibility, submit, ...
                                  this.statusHandler.getCurrentStatus(StatusParentType.REQUEST, list.get(0))     //Get Status from requestID with the StatusHandler
                              );
        
        String tpId = list.get(19);
        List<String> instModes = this.getInstrumentModes(tpId);
        
        
        //Sorting out OPT tasking parameters
        String[] incidenceAz = list.get(3).split(",");
        String[] incidenceEl = list.get(4).split(",");
        String[] pointingAc = list.get(5).split(",");
        String[] pointingAl = list.get(6).split(",");
        
        String roiType = list.get(8);
        
        Geometry coords = null;
        
        if (roiType.equalsIgnoreCase("POLYGON")) {
            Polygon pol = new Polygon();
            String[] points = list.get(7).split(" ");
            for (int i = 0; i < points.length; i++) {
                String[] point = points[i].split(",");
                pol.addNewPoint(new Point(Double.valueOf(point[1]), Double.valueOf(point[0]), 0.0));
            }
            
            coords = pol;
        }
        
        if (roiType.equalsIgnoreCase("CIRCLE")) {
            
            String[] points = list.get(7).split(" ");
            
            if (points.length > 2) {
                List<Point> p = new ArrayList<>();
                
                for (int i = 0; i < 3; i++) {
                    String[] point = points[i].split(",");
                    p.add(new Point(Double.valueOf(point[0]), Double.valueOf(point[1]), 0.0));
                }
                Circle pol = new Circle(p.get(0), p.get(1), p.get(2));
                
                coords = pol;
            }
        }
        
        List<TimePeriod> times = new ArrayList<TimePeriod>();
        times.add(new TimePeriod(DateHandler.parseBDDDate(list.get(9)), DateHandler.parseBDDDate(list.get(10))));
        
        String[] resolutions = list.get(11).split(",");
        OPTTaskingParameters parameters = new OPTTaskingParameters(
                Double.valueOf(list.get(14)), Double.valueOf(list.get(15)), Double.valueOf(list.get(16)), 
                (list.get(17).equals("true")), (list.get(18).equals("true")),
                Double.valueOf(list.get(12)), (list.get(13).equals("true")), 
                Double.valueOf(incidenceAz[0]), Double.valueOf(incidenceAz[1]), Double.valueOf(incidenceEl[0]), Double.valueOf(incidenceEl[1]), 
                Double.valueOf(pointingAc[0]), Double.valueOf(pointingAc[1]), Double.valueOf(pointingAl[0]), Double.valueOf(pointingAl[1]), 
                coords, roiType, 
                times, 
                Integer.valueOf(resolutions[0]), Integer.valueOf(resolutions[1]), instModes
            );
        
        if (list.get(20) != null && !list.get(20).equals("") && !list.get(20).equalsIgnoreCase("null"))
            parameters.setMeteoServerId(list.get(20));
        
        response.setParameters(parameters);
        
        return response;
    }
    
    public SARTaskingRequest getSARRequest(TaskHandlerType type, String taskId, String requestId) throws SQLException, ParseException {
        
        String table = "Request, TaskingParameters, SARTaskingParameters";
        
        List<String> fields = new ArrayList<String>();
        fields.add("requestId");
        fields.add("incomingTime");
        
//        fields.add("max(incomingTime)");
        fields.add("type");
        
        fields.add("incidenceAngleAzimuth");
        fields.add("incidenceAngleElevation");
        fields.add("pointingAngleAcross");
        fields.add("pointingAngleAlong");
        fields.add("coordinates");
        fields.add("roiType");
        fields.add("beginTime");
        fields.add("endTime");
        fields.add("groundResolution");
        
        fields.add("fusionAccepted");
        fields.add("maxNoiseLevel");
        fields.add("maxAmbiguityLevel");
        
        //Getting the taskingParameters ID to get the Instrument Modes from the join table
        fields.add("tpId");
        fields.add("sarTpId");
        
        List<String> conditions = new ArrayList<>();
        
        String taskCondition = null;
        
        if (type == TaskHandlerType.mmfas) {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_MMFASTask_Request WHERE mmfasTask = '"+taskId+"'"
                    + ")";
        }
        else {
            taskCondition = "requestId IN ("
                    + "SELECT request FROM LNK_SensorTask_Request WHERE sensorTask = '"+taskId+"'"
                    + ")";
        }
        conditions.add(taskCondition);
        
//        conditions.add("incomingTime = (SELECT max(incomingTime) FROM Request WHERE " + taskCondition + " )");
        
        if (requestId != null && !requestId.equals(""))
            conditions.add("requestId=" + requestId);
        
        //Links between tables
        conditions.add("Request.requestId=TaskingParameters.request");
        conditions.add("TaskingParameters.tpId=SARTaskingParameters.params");
        conditions.add("Request.type IN ('getFeasibility', 'submit')");
        
        List<List<String>> result = select(fields, table, conditions);
        List<String> list = result.get(0);
        
        System.out.println("Nb responses : " + result.size());
        
        SARTaskingRequest response = new SARTaskingRequest(
                list.get(0),                                        //RequestID
                DateHandler.parseBDDDate(list.get(1)),              //Incoming Time
                RequestType.valueOf(list.get(2)),                   //Request Type : getFeasibility, submit, ...
                this.statusHandler.getCurrentStatus(
                        StatusParentType.REQUEST, list.get(0)       //Get Status from requestID with the StatusHandler
        ));
        String tpId = list.get(15);
        List<String> instModes = this.getInstrumentModes(tpId);
        
        String sarTpId = list.get(16);
        List<String> polarizationModes = this.getPolarizationModes(sarTpId);
        
        //Sorting out OPT tasking parameters
        String[] incidenceAz = list.get(3).split(",");
        String[] incidenceEl = list.get(4).split(",");
        String[] pointingAc = list.get(5).split(",");
        String[] pointingAl = list.get(6).split(",");
        
        
        String roiType = list.get(8);
        
        Geometry coords = null;
        
        if (roiType.equalsIgnoreCase("POLYGON")) {
            Polygon pol = new Polygon();
            String[] points = list.get(7).split(" ");
            for (int i = 0; i < points.length; i++) {
                String[] point = points[i].split(",");
                pol.addNewPoint(new Point(Double.valueOf(point[1]), Double.valueOf(point[0]), 0.0));
            }
            
            coords = pol;
        }
        
        if (roiType.equalsIgnoreCase("CIRCLE")) {
            
            String[] points = list.get(7).split(" ");
            
            if (points.length > 2) {
                List<Point> p = new ArrayList<>();
                
                for (int i = 0; i < 3; i++) {
                    String[] point = points[i].split(",");
                    p.add(new Point(Double.valueOf(point[0]), Double.valueOf(point[1]), 0.0));
                }
                Circle pol = new Circle(p.get(0), p.get(1), p.get(2));
                
                coords = pol;
            }
        }
        
        List<TimePeriod> times = new ArrayList<TimePeriod>();
        times.add(new TimePeriod(DateHandler.parseBDDDate(list.get(9)), DateHandler.parseBDDDate(list.get(10))));
        
        String[] resolutions = list.get(11).split(",");
        
        response.setParameters(new SARTaskingParameters(
                Double.valueOf(list.get(13)), Double.valueOf(list.get(14)),
                (list.get(12).equals("true")), polarizationModes,
                Double.valueOf(incidenceAz[0]), Double.valueOf(incidenceAz[1]), Double.valueOf(incidenceEl[0]), Double.valueOf(incidenceEl[1]), 
                Double.valueOf(pointingAc[0]), Double.valueOf(pointingAc[1]), Double.valueOf(pointingAl[0]), Double.valueOf(pointingAl[1]), 
                coords, roiType, 
                times, 
                Integer.valueOf(resolutions[0]), Integer.valueOf(resolutions[1]), instModes
            ));
        
        return response;
    }
    
    private List<String> getInstrumentModes(String tpId) throws SQLException {
        
        List<String> instModes = new ArrayList<String>();
        
        String table = "TaskingParametersInstrumentModes";
        
        List<String> fields = new ArrayList<String>();
        fields.add("instrumentMode");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("params=" + tpId);
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0)
        for (List<String> list : result) {
            instModes.add(list.get(0));
        }
        
        return instModes;
    }
    
    
    private List<String> getPolarizationModes(String tpId) throws SQLException {
        
        List<String> polModes = new ArrayList<String>();
        
        String table = "SARTaskingParametersPolarisationModes";
        
        List<String> fields = new ArrayList<String>();
        fields.add("polarisationMode");
        
        List<String> conditions = new ArrayList<String>();
        conditions.add("params=" + tpId);
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0)
        for (List<String> list : result) {
            polModes.add(list.get(0));
        }
        
        return polModes;
    }
}
