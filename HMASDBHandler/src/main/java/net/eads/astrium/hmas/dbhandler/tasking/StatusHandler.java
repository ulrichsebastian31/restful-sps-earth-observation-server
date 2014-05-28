/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               StatusHandler.java
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.dbhandler.DatabaseLoader;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;
import net.eads.astrium.hmas.util.structures.tasking.enums.StatusParentType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;

/**
 *
 * @author re-sulrich
 */
public class StatusHandler extends DatabaseLoader {

    public StatusHandler() {
        
        super("MissionPlannerDatabase");
    }
    
    public StatusHandler(DBOperations dboperations) {
        
        super(dboperations);
    }
    
    public StatusHandler(String databaseURL, String user, String pass, String schemaName) {
        super(databaseURL, user, pass, schemaName);
    }
    
    String createNewStatusPending(StatusParentType parent, TaskType type) throws SQLException {
        return createRequestNewStatusPending(parent, type, null);
    }
    
    String createRequestNewStatusPending(StatusParentType parent, TaskType type, RequestType reqType) throws SQLException {
        
        //Setting defaults
        String statusIdentifier = "PENDING";
        int percentCompletion = 0;
        
        String message = null;
        if (parent.equals(StatusParentType.MMFASTASK) || parent.equals(StatusParentType.SENSORTASK)) {
            message = "Task has been saved";
            if (type.equals(TaskType.feasibility)) {
                statusIdentifier = "FEASIBILITY IN_EXECUTION";
            }
            else {
                statusIdentifier = "PLANNING IN_EXECUTION";
            }
        } else {
            if (parent.equals(StatusParentType.REQUEST))
            {
                statusIdentifier = reqType.name().toUpperCase() + " PENDING";
                message = "Request has been saved";
            }
            else
            {
                message = "Segment has been saved";
            }
        }
        
        Date now = Calendar.getInstance().getTime();
        Calendar zeroCal = Calendar.getInstance();
        zeroCal.set(0, 0, 0, 0, 0, 0);
        Date zero = zeroCal.getTime();
        
        return createNewStatus(
                statusIdentifier, 
                percentCompletion, 
                message, 
                DateHandler.formatDate(now), 
                DateHandler.formatDate(zero));
    }

    String createNewStatus( 
            String statusIdentifier, 
            int percentCompletion, 
            String statusMessage, 
            String updateTime, 
            String estimatedTimeOfCompletion) throws SQLException {
        
        String statusId = null;
        
        String table = "Status";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("statusIdentifier");
        fields.add("percentCompletion");
        fields.add("statusmessage");
        fields.add("updateTime");
        fields.add("estimatedToC");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + statusIdentifier + "'");
        depl1.add("" + percentCompletion);
        depl1.add("'" + statusMessage + "'");
        depl1.add("'" + updateTime + "'");
        depl1.add("'" + estimatedTimeOfCompletion + "'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        statusId = this.getDboperations().insertReturningId(
                table, 
                fields, 
                values,
                "statusId");
        
        return statusId;
    }
    
    String createNewStatusWithPrevious( 
            String statusIdentifier, 
            int percentCompletion, 
            String statusMessage, 
            String updateTime, 
            String estimatedTimeOfCompletion,
            String previous) throws SQLException {
        
        String statusId = null;
        
        String table = "Status";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("statusIdentifier");
        fields.add("percentCompletion");
        fields.add("statusmessage");
        fields.add("updateTime");
        fields.add("estimatedToC");
        fields.add("previous");
        
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + statusIdentifier + "'");
        depl1.add("" + percentCompletion);
        depl1.add("'" + statusMessage + "'");
        depl1.add("'" + updateTime + "'");
        depl1.add("'" + estimatedTimeOfCompletion + "'");
        depl1.add("" + previous);
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        
        statusId = this.getDboperations().insertReturningId(
                table, 
                fields, 
                values,
                "statusId");
        
        return statusId;
    }
    
    
    /**
     * Reading from database
     */
    
    Status getCurrentStatus(StatusParentType type, String parentId) throws SQLException, ParseException {
        
        Status status = null;
        
        String table = "Status";
        
        List<String> fields = new ArrayList<String>();
        fields.add("statusIdentifier");
        fields.add("percentCompletion");
        fields.add("statusMessage");
        fields.add("updateTime");
        fields.add("estimatedToC");
        List<String> conditions = new ArrayList<String>();
        
        switch (type) {
            case MMFASTASK:
                table += ", MMFASTask";
                
                conditions.add("MMFASTask.taskId = '" + parentId + "'");
                conditions.add("Status.statusId = MMFASTask.status");
            break;
            case SENSORTASK:
                table += ", SensorTask";
                
                conditions.add("SensorTask.taskId = '" + parentId + "'");
                conditions.add("Status.statusId = SensorTask.status");
            break;
            case REQUEST:
                table += ", Request";
                
                conditions.add("Request.requestId = '" + parentId + "'");
                conditions.add("Status.statusId = Request.status");
            break;
            case SEGMENT:
                table += ", Segment";
                
                conditions.add("Segment.segmentId = '" + parentId + "'");
                conditions.add("Status.statusId = Segment.status");
            break;
        }
        
        List<List<String>> result = select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            List<String> s = result.get(0);

            String identifier = null;
            String message = null;
            int percentCompletion = 0;
            Date update = null;
            Date estimatedTOC = null;

            if (s != null) {
                identifier = s.get(0);
                percentCompletion = Integer.valueOf(s.get(1));
                message = s.get(2);
                update = DateHandler.parseBDDDate(s.get(3));
                estimatedTOC = DateHandler.parseBDDDate(s.get(4));
            }

            status = new Status(identifier, percentCompletion, message, update, estimatedTOC);
        }
        
        return status;
    } 
}
