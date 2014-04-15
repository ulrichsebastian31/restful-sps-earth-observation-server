/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetTaskOperation.java
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
package net.eads.astrium.hmas.mp.operations.tasking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.exceptions.GetTaskFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.processes.feasibilitystudygenerator.FeasibilityStudyGenerator;
import net.eads.astrium.hmas.processes.taskingparametersgenerator.TaskingParametersGenerator;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.opengis.eosps.x20.GetTaskDocument;
import net.opengis.eosps.x20.GetTaskResponseDocument;
import net.opengis.eosps.x20.GetTaskResponseType;
import net.opengis.eosps.x20.ProgrammingStatusDocument;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.eosps.x20.TaskType;
import net.opengis.eosps.x20.TaskingParametersType;


/**
 * @file GetStatusOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The GetStatus Operation permits to task a satellite mission planner
 */
public class GetTaskOperation extends EOSPSOperation<MissionPlannerDBHandler,GetTaskDocument,GetTaskResponseDocument,GetTaskFault> {

    
    /**
     * 
     * @param request
     */
    public GetTaskOperation(MissionPlannerDBHandler handler, GetTaskDocument request){

            super(handler,request);
    }

    @Override
    public void validRequest() throws GetTaskFault{
        if (
                this.getRequest().getGetTask() != null && 
                this.getRequest().getGetTask().getTaskArray() != null &&
                this.getRequest().getGetTask().getTaskArray().length > 0 &&
                !this.getRequest().getGetTask().getTaskArray(0).equals("")) {
            
        }
        else {
            //If no taskId is provided, put all the 'feasibility' tasks
            //Which will take all associated 'planning' tasks (see lines 150 - 170)
            //So in the end, all tasks are displayed
            try {
                this.getConfigurationLoader().getFeasibilityHandler().getSensorTasksIds(
                        net.eads.astrium.hmas.util.structures.tasking.enums.TaskType.feasibility);
            } catch (SQLException|ParseException ex) {
                throw new GetTaskFault("Error while loading existing tasks : " + ex.getClass().getName() + " : " + ex.getMessage());
            }
        }
    }

    @Override
    public void executeRequest() throws GetTaskFault{

        this.validRequest();

        GetTaskResponseDocument responseDocument = GetTaskResponseDocument.Factory.newInstance();
        GetTaskResponseType resp = responseDocument.addNewGetTaskResponse();

        try {
            String[] tasks = this.getRequest().getGetTask().getTaskArray();
            SensorPlanningHandler taskHandler = this.getConfigurationLoader().getPlanningHandler();

            System.out.println("Tasks in request : " + tasks.length);

            for (int i = 0; i < tasks.length; i++) {
                String taskId = tasks[i];
                Status status = taskHandler.getStatus(taskId);

                String sensorType = taskHandler.getSensorRequestSensorType(taskId);

                TaskingParametersType parameters = null;

                if (sensorType.equalsIgnoreCase("OPT")) {
                    OPTTaskingRequest req = taskHandler.getOPTRequest(TaskHandlerType.sensor, taskId, null);
                    parameters = TaskingParametersGenerator.createOPTTaskingParameters(req.getParameters());
                }
                if (sensorType.equalsIgnoreCase("SAR")) {
                    SARTaskingRequest req = taskHandler.getSARRequest(TaskHandlerType.sensor, taskId, null);
                    parameters = TaskingParametersGenerator.createSARTaskingParameters(req.getParameters());
                }

                TaskType task = resp.addNewTask().addNewTask();
                task.setIdentifier(taskId);

                StatusReportType statusReport = task.addNewStatus().addNewStatusReport();

                statusReport.setProcedure(sensorType);

                statusReport.setTask(taskId);

                statusReport.setIdentifier(status.getIdentifier());
                statusReport.setUpdateTime(DateHandler.getCalendar(status.getUpdateTime()));
                statusReport.setEstimatedToC(DateHandler.getCalendar(status.getEstimatedTimeOfCompletion()));
                statusReport.setPercentCompletion(status.getPercentCompletion());
                statusReport.addNewStatusMessage().setStringValue(status.getMessage());

                //Add the parameters of the request to the response (informative)
                statusReport.addNewEoTaskingParameters().set(
                        parameters
                    );

                if (status.getIdentifier().equalsIgnoreCase("FEASIBILITY COMPLETED")) {

                    task.addNewExtension().set(
                            FeasibilityStudyGenerator.getFeasibilityStudy(
                                    this.getConfigurationLoader().getFeasibilityHandler(), 
                                    taskId
                                )
                        );
                    
                    List<String> sensorPlanningTasks = taskHandler.getFeasibilityPlanningTasks(taskId);

                    for (String sensorPlanningTask : sensorPlanningTasks) {
                        ProgrammingStatusDocument progStatus = FeasibilityStudyGenerator.getProgrammingStatus(
                                taskHandler, 
                                sensorPlanningTask
                            );
                        progStatus.getProgrammingStatus().setId(sensorPlanningTask);

                        task.addNewExtension().set(
                                progStatus
                            );
                    }
                }

                if (status.getIdentifier().contains("PLANNING")) {

                    task.addNewExtension().set(
                            FeasibilityStudyGenerator.getProgrammingStatus(
                                    taskHandler, 
                                    taskId
                                )
                        );
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        this.setResponse(responseDocument);
    }
}
