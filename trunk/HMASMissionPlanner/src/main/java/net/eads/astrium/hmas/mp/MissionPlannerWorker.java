/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               MissionPlannerWorker.java
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
package net.eads.astrium.hmas.mp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.exceptions.CancelFault;
import net.eads.astrium.hmas.exceptions.DescribeResultAccessFault;
import net.eads.astrium.hmas.exceptions.DescribeSensorFault;
import net.eads.astrium.hmas.exceptions.DescribeTaskingFault;
import net.eads.astrium.hmas.exceptions.OWSException;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.exceptions.GetSensorAvailabilityFault;
import net.eads.astrium.hmas.exceptions.GetStationAvailabilityFault;
import net.eads.astrium.hmas.exceptions.GetStatusFault;
import net.eads.astrium.hmas.exceptions.GetTaskFault;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import net.eads.astrium.hmas.exceptions.ValidateFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.mp.operations.tasking.CancelOperation;
import net.eads.astrium.hmas.mp.operations.tasking.DescribeResultAccessOperation;
import net.eads.astrium.hmas.mp.operations.describing.DescribeSensorOperation;
import net.eads.astrium.hmas.mp.operations.describing.DescribeTaskingOperation;
import net.eads.astrium.hmas.mp.operations.tasking.GetFeasibilityOperation;
import net.eads.astrium.hmas.mp.operations.describing.GetSensorAvailabilityOperation;
import net.eads.astrium.hmas.mp.operations.describing.GetStationAvailabilityOperation;
import net.eads.astrium.hmas.mp.operations.tasking.GetStatusOperation;
import net.eads.astrium.hmas.mp.operations.tasking.GetTaskOperation;
import net.eads.astrium.hmas.mp.operations.tasking.SubmitOperation;
import net.eads.astrium.hmas.mp.operations.tasking.SubmitSegmentByIDOperation;
import net.eads.astrium.hmas.mp.tasking.SensorFeasibilityPlanningServiceHandler;
import net.eads.astrium.hmas.workers.EOSPSCancellationWorker;
import net.eads.astrium.hmas.workers.EOSPSFeasibilityPlanningWorker;
import net.eads.astrium.hmas.workers.GenericEOSPSWorker;
import net.opengis.eosps.x20.CancelDocument;
import net.opengis.eosps.x20.CancelResponseDocument;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStatusDocument;
import net.opengis.eosps.x20.GetStatusResponseDocument;
import net.opengis.eosps.x20.GetTaskDocument;
import net.opengis.eosps.x20.GetTaskResponseDocument;
import net.opengis.eosps.x20.SubmitDocument;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.eosps.x20.ValidateDocument;
import net.opengis.eosps.x20.ValidateResponseDocument;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseDocument;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 */
public class MissionPlannerWorker extends GenericEOSPSWorker 
        implements EOSPSFeasibilityPlanningWorker, EOSPSCancellationWorker {
    
    @Override
    public void close() {
//        databaseConfiguration = null;
    }
    
    @Override
    public List<String> getInterfaces() {
        return Arrays.asList(new String[]{"eosps"});
    }
    
    @Override
    public String getServerBaseURI() {
        return serverBaseURI;
    }
    
    @Override
    public String getService() {
        return "mp";
    }

    @Override
    public String getServiceInstance() {
        return databaseConfiguration.getMissionPlannerId();
    }
    
    @Override
    public String getInstanceDescription() throws SQLException {
        String description = "<missionPlanner>\n"
                + "<mpId>" + databaseConfiguration.getMissionPlannerId()+ "</mpId>\n"
                + "<description>"
                + "Mission planning system interface for satellite "+databaseConfiguration.getSatelliteId()+""
                + "</description>\n"
                + "</missionPlanner>";
        
        return description;
    }
    
    private MissionPlannerDBHandler databaseConfiguration;
    private final String serverBaseURI;

    @Override
    public MissionPlannerDBHandler getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public MissionPlannerWorker(String instanceId, String satelliteId, String serverBaseURI) throws SQLException, IOException
    {
        this.serverBaseURI = serverBaseURI;
        databaseConfiguration = new MissionPlannerDBHandler(instanceId, satelliteId);
    }
    
    @Override
    public void destroy() {
        //TODO: destroy what ?
    }

    @Override
    public XmlObject execute(String interfaceId, String request, XmlObject parameters) throws OWSException {
        return executeEOSPS(request, parameters);
    }
    
    @Override
    public XmlObject executeEOSPS(String request, XmlObject parameters) throws OWSException {
        XmlObject response = null;

        switch (request.toLowerCase()) {
            //Non user specific
            case "describetasking":
                response = this.describeTasking((DescribeTaskingDocument)parameters);
            break;
            case "describesensor":
                response = this.describeSensor((DescribeSensorDocument)parameters);
            break;
            case "getsensoravailibility":
                response = this.getSensorAvailability((GetSensorAvailabilityDocument)parameters);
            break;
            case "getstationavailibility":
                response = this.getStationAvailability((GetStationAvailabilityDocument)parameters);
            break;
            //User specific
            case "getfeasibility":
                response = this.getFeasibilityAsynchronous((GetFeasibilityDocument)parameters);
            break;
            case "submitsegmentbyid":   //Default = planning synchronous
                response = this.submitSegmentByIDPlanningSynchronous((SubmitSegmentByIDDocument)parameters);
            break;
            case "submitsegmentbyidas": //All synchronous
                response = this.submitSegmentByID((SubmitSegmentByIDDocument)parameters);
            break;
            case "submitsegmentbyidaa": //All asynchronous
                response = this.submitSegmentByIDAllAsynchronous((SubmitSegmentByIDDocument)parameters);
            break;
            case "cancel":
                response = this.cancel((CancelDocument)parameters);
            break;
            case "gettask":
                response = this.getTask((GetTaskDocument)parameters);
            break;
            case "getstatus":
                response = this.getStatus((GetStatusDocument)parameters);
            break;
            case "describeresultaccess":
                response = this.describeResultAccess((DescribeResultAccessDocument)parameters);
            break;
            default:    //GetCapabilities
                response = this.getCapabilities((GetCapabilitiesDocument)parameters);
            break;
        }
        
        return response;
    }

    @Override
    public DescribeSensorResponseDocument describeSensor(DescribeSensorDocument request) throws DescribeSensorFault {
        
        DescribeSensorOperation operation = new DescribeSensorOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public DescribeTaskingResponseDocument describeTasking(DescribeTaskingDocument request) throws DescribeTaskingFault {
        
        DescribeTaskingOperation operation = new DescribeTaskingOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public GetSensorAvailabilityResponseDocument getSensorAvailability(GetSensorAvailabilityDocument request) throws GetSensorAvailabilityFault {
        GetSensorAvailabilityOperation operation = new GetSensorAvailabilityOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public GetStationAvailabilityResponseDocument getStationAvailability(GetStationAvailabilityDocument request) throws GetStationAvailabilityFault {
        GetStationAvailabilityOperation operation = new GetStationAvailabilityOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public GetFeasibilityResponseDocument getFeasibility(GetFeasibilityDocument request) throws GetFeasibilityFault {
        GetFeasibilityOperation operation = new GetFeasibilityOperation(databaseConfiguration, request);
        
        operation.executeRequest();
         
        return operation.getResponse();
    }

    
    @Override
    public SubmitResponseDocument submit(SubmitDocument request) throws SubmitFault {
        SubmitOperation operation = new SubmitOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }
    
    @Override
    public CancelResponseDocument cancel(CancelDocument request) throws CancelFault {
        CancelOperation operation = new CancelOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public DescribeResultAccessResponseDocument describeResultAccess(DescribeResultAccessDocument request) throws DescribeResultAccessFault {
        DescribeResultAccessOperation operation = new DescribeResultAccessOperation(databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    /**
     * This function executes the whole submission process, wait for the acquisition of the segments before returning the results
     * @param request
     * @return
     * @throws SubmitFault 
     */
    @Override
    public SubmitResponseDocument submitSegmentByID(SubmitSegmentByIDDocument request) throws SubmitFault {
        SubmitSegmentByIDOperation operation = new SubmitSegmentByIDOperation( databaseConfiguration, request, 
                SensorFeasibilityPlanningServiceHandler.ExecutionType.allSynchronous);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    /**
     * This function executes the submission of the segments, then returns the status of the segments as "ACCEPTED", "REJECTED", 
     * or "CANCELLED" if the Cancel function has been called in the mean time
     * Then it starts a Thread that will wait for the acquisitions of the segments and update the results in the database
     * @param request
     * @return
     * @throws SubmitFault 
     */
    public SubmitResponseDocument submitSegmentByIDPlanningSynchronous(SubmitSegmentByIDDocument request) throws SubmitFault {
        SubmitSegmentByIDOperation operation = new SubmitSegmentByIDOperation( databaseConfiguration, request, 
                SensorFeasibilityPlanningServiceHandler.ExecutionType.planningSynchronous);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    /**
     * This function does the whole process asynchronously
     * It only saves the task in the database and returns the ID associated with it
     * Then starts the thread that will run through the whole process
     * @param request
     * @return
     * @throws SubmitFault 
     */
    public SubmitResponseDocument submitSegmentByIDAllAsynchronous(SubmitSegmentByIDDocument request) throws SubmitFault {
        SubmitSegmentByIDOperation operation = new SubmitSegmentByIDOperation( databaseConfiguration, request, 
                SensorFeasibilityPlanningServiceHandler.ExecutionType.allAsynchronous);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public GetStatusResponseDocument getStatus(GetStatusDocument request) throws GetStatusFault {
        GetStatusOperation operation = new GetStatusOperation( databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public GetTaskResponseDocument getTask(GetTaskDocument request) throws GetTaskFault {
        GetTaskOperation operation = new GetTaskOperation( databaseConfiguration, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    @Override
    public GetFeasibilityResponseDocument getFeasibilityAsynchronous(GetFeasibilityDocument request) throws GetFeasibilityFault {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ValidateResponseDocument validate(ValidateDocument request) throws ValidateFault {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
