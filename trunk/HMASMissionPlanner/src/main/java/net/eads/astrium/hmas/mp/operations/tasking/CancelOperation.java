/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CancelOperation.java
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
 */package net.eads.astrium.hmas.mp.operations.tasking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.exceptions.CancelFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.mp.tasking.SensorFeasibilityPlanningServiceHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.processes.feasibilitystudygenerator.FeasibilityStudyGenerator;
import net.eads.astrium.hmas.util.structures.tasking.Request;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.SensorTask;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.opengis.eosps.x20.CancelDocument;
import net.opengis.eosps.x20.CancelResponseDocument;
import net.opengis.eosps.x20.CancelResponseType;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.sps.x21.CancelType;


/**
 * @file CancelOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The Cancel Operation permits to task a satellite mission planner
 */
public class CancelOperation extends EOSPSOperation<MissionPlannerDBHandler,CancelDocument,CancelResponseDocument,CancelFault> {

    public static int nbOrbitHours = 2;
    public static int nbCancelReportValidityMinutes = 2;
    
	/**
	 * 
	 * @param request
	 */
	public CancelOperation(MissionPlannerDBHandler handler, CancelDocument request){

            super(handler,request);
	}

	@Override
	public void validRequest() throws CancelFault{
	}

	@Override
	public void executeRequest() throws CancelFault{
		
            this.validRequest();
            CancelType req = this.getRequest().getCancel();
            String service = req.getService();
            String version = req.getVersion();
            String acceptFormat = req.getAcceptFormat();
            String task = req.getTask();
            
            CancelResponseDocument responseDocument = CancelResponseDocument.Factory.newInstance();
            CancelResponseType resp = responseDocument.addNewCancelResponse();
            CancelResponseType.Result result = resp.addNewResult();
            
            StatusReportType statusReport = result.addNewStatusReport();
            SensorPlanningHandler taskHandler = this.getConfigurationLoader().getPlanningHandler();
            
            Request lastRequest = null;
            SensorTask databaseTask = null;
            try {
                databaseTask = taskHandler.getSensorTask(task);
                lastRequest = taskHandler.getRequest(TaskHandlerType.sensor, task, null);
                
                if (databaseTask == null) {
                    throw new CancelFault("Planning task " + task + " has not been found.");
                }
                if (lastRequest != null) {
                    System.out.println("" + lastRequest.getRequestId() + " - " + lastRequest.getType());
                }
                
                if (databaseTask.getStatus().getIdentifier().equals("PLANNING COMPLETED")) {
                    throw new CancelFault("Planning task " + task + " has already been acquired.");
                }
                
                SensorFeasibilityPlanningServiceHandler taskExecutor = 
                        SensorFeasibilityPlanningServiceHandler.currentTasks.get(task);
                //Should never happen because at this stage, we are sure the task exists and is not completed, so it should be running.
                if (taskExecutor == null) {
                    throw new CancelFault("Planning task " + task + " is not currently running on this server.");
                }

                List<Segment> segments = null;
                
                //Calculate if the last incoming request was done less than <nbCancelReportValidityMinutes> minutes ago
                Calendar nowMinusValidityMin = Calendar.getInstance();
                nowMinusValidityMin.add(Calendar.MINUTE, -nbCancelReportValidityMinutes);
                System.out.println("Last request inc time : " + lastRequest.getIncomingTime());
                System.out.println("nowMinusValidityMin : " + nowMinusValidityMin.getTime());
                System.out.println("Result : " + lastRequest.getIncomingTime().after(nowMinusValidityMin.getTime()));
                
                boolean isCancelValid = lastRequest.getIncomingTime().after(nowMinusValidityMin.getTime());
                //If the last incoming request is a cancel, and the request has been done less than <nbCancelReportValidityMinutes> minutes ago
                //Confirm the cancellation and cancel all cancellable segments
                if (lastRequest.getType().equals(RequestType.cancel) && isCancelValid) {
                    
                    //Stop the thread that runs this task
                    //This thread will automatically update the task and segments status 
                    //to "PLANNING_CANCELLED" and "CANCELLED"
                    taskExecutor.interrupt();
                    //Read again from the database to see the task and segments' new status
                    databaseTask = taskHandler.getSensorTask(task);
                    segments = taskHandler.getSegments(task);

                    if (segments == null || segments.isEmpty()) {
                        throw new CancelFault("No segments found for planning task " + task + ".");
                    }
                    int nbCancelled = 0;
                    for (Segment segment : segments) {
                        if (segment.getStatus().getIdentifier().equals("CANCELLED")) {
                            nbCancelled++;
                        }
                    }
                    
                    Status reqStat = null;
                    if (nbCancelled > 0) {
                        reqStat = taskHandler.
                                addNewCancelRequestSucceddedStatus(TaskHandlerType.sensor, taskExecutor.getSensorPlanningTaskId());
                    }
                    else {
                        reqStat = taskHandler.
                                addNewCancelRequestFailedStatus(TaskHandlerType.sensor, taskExecutor.getSensorPlanningTaskId());
                    }
                    
                    statusReport.setIdentifier(reqStat.getIdentifier());
                    statusReport.setRequestStatus(reqStat.getIdentifier());
                }
                //If there is no previous "cancel" to validate, tell the user what segments are cancellable, so he can send the second request back to cancel them
                else {
                    
                    List<Segment> cancellable = new ArrayList<>();
                    List<Segment> nonCancellable = new ArrayList<>();

                    segments = taskHandler.getSegments(task);

                    if (segments == null || segments.isEmpty()) {
                        throw new CancelFault("Planning task " + task + " has not been found.");
                    }
                    Calendar nowPlusOrbitHours = Calendar.getInstance();
                    nowPlusOrbitHours.add(Calendar.HOUR, nbOrbitHours);
                    for (Segment segment : segments) {
                        switch (segment.getStatus().getIdentifier()) {
                            case "POTENTIAL":
                                cancellable.add(segment);
                            break;
                            case "PLANNED":
                                System.out.println("PLANNED : " + nowPlusOrbitHours + " - " + segment.getStartOfAcquisition());
                                if (segment.getStartOfAcquisition().after(nowPlusOrbitHours))
                                    cancellable.add(segment);
                                else {
                                    nonCancellable.add(segment);
                                }
                            break;
                            case "CANCELLED":
                                nonCancellable.add(segment);
                            break;
                            case "REJECTED":
                                nonCancellable.add(segment);
                            break;
                            case "ACQUIRED":
                                nonCancellable.add(segment);
                            break;
                            case "FAILED":
                                nonCancellable.add(segment);
                            break;
                        }
                    }

                    if (cancellable.isEmpty()) {

                        statusReport.setIdentifier("CANCEL FAILED");
                        statusReport.setRequestStatus("CANCEL FAILED");
                        statusReport.addNewStatusMessage().setStringValue(
                                "No segments are cancellable for this task. This task has finished or failed. Please see the programmingStatus.");
                    }
                    else {
                        taskHandler.
                                createRequest(TaskHandlerType.sensor, task, RequestType.cancel);
                        
                        statusReport.setIdentifier("CANCEL PENDING");
                        statusReport.setRequestStatus("CANCEL PENDING");
                        String message = null;
                        if (nonCancellable.isEmpty()) {
                            message = "All segments are cancellable. Send the cancel request back to validate this action."
                                    + "This report will expire in "+nbCancelReportValidityMinutes+" minute(s).";
                        }
                        else {
                            message = "Some segments are not cancellable. Please see the programming status "
                                    + "and send the cancel request back to validate this action."
                                    + "This report will expire in "+nbCancelReportValidityMinutes+" minute(s).";
                        }
                        statusReport.addNewStatusMessage().setStringValue(message);
                    }
                }
                
                //In any case, add to the status report the taskID, the task status and the segments in a programming status
                statusReport.setTask(databaseTask.getSensorTaskId());
                statusReport.setTaskStatus(databaseTask.getStatus().getIdentifier());
                
                (resp.addNewExtension()).set(
                        FeasibilityStudyGenerator.getProgrammingStatus(
                            taskHandler, 
                            task));
                
                
            } catch (SQLException|ParseException ex) {
                ex.printStackTrace();
            }
            
            this.setResponse(responseDocument);
	}
}
