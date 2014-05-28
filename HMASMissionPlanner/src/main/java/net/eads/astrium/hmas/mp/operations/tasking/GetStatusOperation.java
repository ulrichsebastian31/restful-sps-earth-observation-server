/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetStatusOperation.java
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
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.exceptions.GetStatusFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.processes.taskingparametersgenerator.TaskingParametersGenerator;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.opengis.eosps.x20.GetStatusDocument;
import net.opengis.eosps.x20.GetStatusResponseDocument;
import net.opengis.eosps.x20.GetStatusResponseType;
import net.opengis.eosps.x20.StatusReportType;
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
public class GetStatusOperation extends EOSPSOperation<MissionPlannerDBHandler,GetStatusDocument,GetStatusResponseDocument,GetStatusFault> {

    
	/**
	 * 
	 * @param request
	 */
	public GetStatusOperation(MissionPlannerDBHandler handler, GetStatusDocument request){

            super(handler,request);
	}

	@Override
	public void validRequest() throws GetStatusFault{
            if (
                this.getRequest().getGetStatus()!= null && 
                this.getRequest().getGetStatus().getTask() != null) {
            
        }
        else {
            throw new GetStatusFault("Please fill in the task parameter.");
        }
	}

	@Override
	public void executeRequest() throws GetStatusFault{
		
            this.validRequest();
            
            GetStatusResponseDocument responseDocument = GetStatusResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            GetStatusResponseType resp = responseDocument.addNewGetStatusResponse();

            try {
                //What we need here is a SensorTaskHandler, so any of Planning or Feasibility will do
                SensorPlanningHandler taskHandler = this.getConfigurationLoader().getPlanningHandler();

                String taskId = this.getRequest().getGetStatus().getTask();
                Status status = taskHandler.getStatus(taskId);

                String sensorId = taskHandler.getSensorId(taskId);
                String sensorType = taskHandler.getSensorRequestSensorType(taskId);
                String reqStatus = "";

                TaskingParametersType parameters = null;

                if (sensorType.equalsIgnoreCase("OPT")) {
                    OPTTaskingRequest req = taskHandler.getOPTRequest(TaskHandlerType.sensor, taskId, null);
                    parameters = TaskingParametersGenerator.createOPTTaskingParameters(req.getParameters());
                    
                    String reqStat = req.getStatus().getIdentifier();
                    if (reqStat.contains("SUCCEDDED")) {
                        reqStatus = "Accepted";
                    }
                    else {
                        reqStatus = "Rejected";
                    }
                }
                if (sensorType.equalsIgnoreCase("SAR")) {
                    SARTaskingRequest req = taskHandler.getSARRequest(TaskHandlerType.sensor, taskId, null);
                    parameters = TaskingParametersGenerator.createSARTaskingParameters(req.getParameters());
                    
                    String reqStat = req.getStatus().getIdentifier();
                    if (reqStat.contains("SUCCEDDED")) {
                        reqStatus = "Accepted";
                    }
                    else {
                        reqStatus = "Rejected";
                    }
                }

                StatusReportType statusReport = resp.addNewStatus().addNewStatusReport();

                statusReport.setProcedure(sensorId);
                statusReport.setTask(taskId);

                statusReport.setRequestStatus(reqStatus);

                statusReport.setIdentifier(status.getIdentifier());
                statusReport.setUpdateTime(DateHandler.getCalendar(status.getUpdateTime()));
                statusReport.setEstimatedToC(DateHandler.getCalendar(status.getEstimatedTimeOfCompletion()));
                statusReport.setPercentCompletion(status.getPercentCompletion());
                statusReport.addNewStatusMessage().setStringValue(status.getMessage());

                //Add the parameters of the request to the response (informative)
                statusReport.addNewEoTaskingParameters().set(
                        parameters
                    );

            } catch (SQLException|ParseException ex) {
                ex.printStackTrace();
                throw new GetStatusFault(ex.getClass().getName() + " : " + ex.getMessage() + ".");
            }
            this.setResponse(responseDocument);
	}
}
