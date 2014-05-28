/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SubmitSegmentByIDOperation.java
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.mp.tasking.SensorFeasibilityPlanningServiceHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.processes.feasibilitystudygenerator.FeasibilityStudyGenerator;
import net.eads.astrium.hmas.processes.taskingparametersgenerator.TaskingParametersGenerator;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.EarthObservationEquipment;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingRequest;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;
import net.opengis.eosps.x20.ProgrammingStatusDocument;
import net.opengis.eosps.x20.ProgrammingStatusType;
import net.opengis.eosps.x20.SegmentPropertyType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.SegmentValidationDataDocument;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.eosps.x20.SubmitResponseType;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDType;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.swe.x20.CategoryType;
import org.apache.xmlbeans.XmlObject;
import xint.esa.earth.eop.DownlinkInformationDocument;
import xint.esa.earth.eop.DownlinkInformationType;
import xint.esa.earth.eop.EarthObservationEquipmentType;
import xint.esa.earth.eop.InstrumentType;
import xint.esa.earth.eop.OrbitTypeValueType;
import xint.esa.earth.eop.PlatformType;
import xint.esa.earth.eop.SensorType;


/**
 * @file SubmitOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The Submit Operation permits to task a satellite mission planner
 */
public class SubmitSegmentByIDOperation extends EOSPSOperation<MissionPlannerDBHandler,SubmitSegmentByIDDocument,SubmitResponseDocument,SubmitFault> {

    
    private String serverBaseURI;
    
    private String sensorFeasibilityTaskId;
    private SensorFeasibilityPlanningServiceHandler handler;

    private List<Segment> alreadyPlannedSegments;
    SensorFeasibilityPlanningServiceHandler.ExecutionType executionType;
    
    /**
     * 
     * @param request
     */
    public SubmitSegmentByIDOperation(MissionPlannerDBHandler dbHandler, SubmitSegmentByIDDocument request, String serverBaseURI, SensorFeasibilityPlanningServiceHandler.ExecutionType executionType){

            super(dbHandler,request);
            this.executionType = executionType;
            this.serverBaseURI = serverBaseURI;
    }

    @Override
    public void validRequest() throws SubmitFault{
        SubmitSegmentByIDType req = this.getRequest().getSubmitSegmentByID();
        try {
            
            sensorFeasibilityTaskId = req.getTask();
            Status status = this.getConfigurationLoader().getFeasibilityHandler().getStatus(sensorFeasibilityTaskId);
            if (status == null) {
                throw new SubmitFault("Task " + sensorFeasibilityTaskId + " does not exist.");
            }
            if (status.getIdentifier().equalsIgnoreCase("FEASIBILITY IN_EXECUTION")) {
                throw new SubmitFault("Task " + sensorFeasibilityTaskId + " is not complete yet. Please try again later.");
            }
            
            List<Segment> feasibility = 
                    this.getConfigurationLoader().getFeasibilityHandler().
                    getSegments(sensorFeasibilityTaskId);
            
            String[] askeds = req.getSegmentIDArray();
            List<Segment> segments = new ArrayList<>();
            
            alreadyPlannedSegments = new ArrayList<>();
            
            for (int i = 0; i < askeds.length; i++) 
            for (Segment segment : feasibility) 
                if (askeds[i].equals(segment.getSegmentId())) {
                    if ("POTENTIAL CANCELLED".contains(segment.getStatus().getIdentifier())) {
                        segments.add(segment);
                    }
                    else {
                        //If the segment has already been submitted
                        if ("PLANNED REJECTED ACQUIRED FAILED".contains(segment.getStatus().getIdentifier())) {
                            alreadyPlannedSegments.add(segment);
                        }
                    }
                    break;
                }
            
            if (segments.isEmpty()) {
                if (alreadyPlannedSegments.isEmpty()) {
                    throw new SubmitFault("None asked segments of task " + sensorFeasibilityTaskId + " match the asked segments.");
                }
                else {
                    //If the user sends the same segments twice, this method will send back the status of those segments
                    throw new SubmitFault("Segments all planned.");
                }
            }
            
            XmlObject[] exts = req.getExtensionArray();
            String mmfasTask = null;
            if (exts!= null && exts.length > 0) {
                for (int i = 0; i < exts.length; i++) {
                    XmlObject ext = exts[i];
                    if (ext instanceof CategoryType) {
                        CategoryType string = (CategoryType) ext;
                        switch (string.getDefinition()) {
                            case "mmfasTask":
                                mmfasTask = string.getValue();
                            break;
                        }
                    }
                }
            }
            
            handler = new SensorFeasibilityPlanningServiceHandler(
                    this.executionType,
                    sensorFeasibilityTaskId, 
                    segments, 
                    this.getConfigurationLoader().getPlanningHandler(), 
                    serverBaseURI);
            
        } catch (ParseException|SQLException ex) {
            ex.printStackTrace();
            throw new SubmitFault("Error while parsing request : " + ex.getClass().getName() + " : " + ex.getMessage() + "");
        }
    }

    @Override
    public void executeRequest() throws SubmitFault{

        boolean allPlanned = false;
        
        try {
            this.validRequest();
        } 
        catch (SubmitFault e) {
            //If the user sends the same segments twice, this method will send back the status of those segments
            if (!e.getMessage().equals("Segments all planned.")) {
                throw e;
            }
            else {
                allPlanned = true;
            }
        }
        
        SubmitResponseDocument responseDocument = SubmitResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        if (!allPlanned) {
            
            SensorPlanningHandler dbHandler = (this.getConfigurationLoader()).getPlanningHandler();

            String sensorId = null;
            Status status = null;
            TaskingParametersType parameters = null;

            //Load response data from the database
            try {
                //Running the dummy planning
                handler.runSynchronousPartThenStart();
                
                String planningTaskId = this.handler.getSensorPlanningTaskId();
                sensorId = dbHandler.getSensorId(planningTaskId);
                status = this.handler.getStatus();
                String sensorType = dbHandler.getSensorType(planningTaskId);
                System.out.println("SensorType : " + sensorType);

                if (sensorType.equalsIgnoreCase("OPT")) {
                    OPTTaskingRequest req = dbHandler.getOPTRequest(
                            TaskHandlerType.sensor, planningTaskId, null);
                    parameters = TaskingParametersGenerator.createOPTTaskingParameters(req.getParameters());
                    System.out.println("OPT parameters done; sensor type : " + sensorType);
                }
                if (sensorType.equalsIgnoreCase("SAR")) {
                    SARTaskingRequest req = dbHandler.getSARRequest(
                            TaskHandlerType.sensor, planningTaskId, null);
                    parameters = TaskingParametersGenerator.createSARTaskingParameters(req.getParameters());
                }

            } catch (SQLException|ParseException ex) {
            ex.printStackTrace();
                throw new SubmitFault("" + ex.getClass().getName() + " - " + ex.getMessage());
            }

            responseDocument = SubmitResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            SubmitResponseType resp = responseDocument.addNewSubmitResponse();
            StatusReportType statusReport = resp.addNewResult().addNewStatusReport();

            statusReport.setProcedure(sensorId);

            statusReport.setTask(this.handler.getSensorPlanningTaskId());

            statusReport.setIdentifier(status.getIdentifier());
            statusReport.setUpdateTime(DateHandler.getCalendar(status.getUpdateTime()));
            statusReport.setEstimatedToC(DateHandler.getCalendar(status.getEstimatedTimeOfCompletion()));
            statusReport.setPercentCompletion(status.getPercentCompletion());
            statusReport.addNewStatusMessage().setStringValue(status.getMessage());

            //Add the parameters of the request to the response (informative)
            statusReport.addNewEoTaskingParameters().set(
                    parameters
                );


            try {
                String taskId = this.handler.getSensorPlanningTaskId();
                ProgrammingStatusDocument study = 
                        FeasibilityStudyGenerator.getProgrammingStatus(
                                this.getConfigurationLoader().getPlanningHandler(), 
                                taskId);

                resp.addNewExtension().set(study);
                
                String requestStatus = null;
                //Setting new request status
                if (!status.getIdentifier().equals("PLANNING IN_EXECUTION")) {

                    Status reqStat = null;
                    if (status.getIdentifier().equals("PLANNING ACCEPTED") ||
                            status.getIdentifier().equals("PLANNING COMPLETED")) {
                        
                        requestStatus = "Accepted";
                        reqStat = dbHandler.
                                addNewSubmitSegmentsRequestSucceddedStatus(TaskHandlerType.sensor, handler.getSensorPlanningTaskId());
                    }
                    else {
                        requestStatus = "Rejected";
                        reqStat = dbHandler.
                                addNewSubmitSegmentsRequestFailedStatus(TaskHandlerType.sensor, handler.getSensorPlanningTaskId());
                    }
                }
                else {
                        requestStatus = "Pending";
                }
                statusReport.setRequestStatus(requestStatus);
                
            } catch (SQLException|ParseException ex) {
            ex.printStackTrace();
                throw new SubmitFault("" + ex.getClass().getName() + " - " + ex.getMessage());
            }
        }
        else {
            responseDocument = SubmitResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            SubmitResponseType res = responseDocument.addNewSubmitResponse();

            StatusReportType statusReport = res.addNewResult().addNewStatusReport();

            statusReport.setIdentifier("ALREADY DONE");
            statusReport.addNewStatusMessage().setStringValue(
                    "The segments required for submission have all already been submitted. "
                    + "Please check your previous submission tasks for this feasibility analysis.");
        }
        
        //Adding the already planned segments
        SubmitResponseType res = responseDocument.getSubmitResponse();

        System.out.println("alreadyPlannedSegments ? " + (alreadyPlannedSegments != null));

        if (alreadyPlannedSegments != null && alreadyPlannedSegments.size() > 0) {

            System.out.println("Already asked segments nb : " + alreadyPlannedSegments.size());

            ProgrammingStatusDocument studyDoc = ProgrammingStatusDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            ProgrammingStatusType study = studyDoc.addNewProgrammingStatus();
            
            study.setId("PREVIOUS");
            
            SegmentPropertyType[] ss = null;
            try {
                ss = addSegments(alreadyPlannedSegments);
            } catch (SQLException ex) {
                Logger.getLogger(SubmitSegmentByIDOperation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(SubmitSegmentByIDOperation.class.getName()).log(Level.SEVERE, null, ex);
            }

            study.setSegmentArray(ss);

            (res.addNewExtension()).set(studyDoc);
        }
        else {
            System.out.println("No already done segments");
        }
        
        
        this.setResponse(responseDocument);
    }
    
    private SegmentPropertyType[] addSegments(List<Segment> segments) throws SQLException, ParseException {
        
        String sensorTaskId = this.handler.getSensorPlanningTaskId();
        
        EarthObservationEquipment eoe = 
                (this.getConfigurationLoader().getPlanningHandler()).getEarthObservationEquipment(sensorTaskId);
        
//        List<Segment> segments = handler.getSegments();
        
        SegmentPropertyType[] ss = new SegmentPropertyType[segments.size()];
        
        int i = 0;
        for (Segment segment : segments) {
            
            SegmentPropertyType s = SegmentPropertyType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            
            SegmentType seg = s.addNewSegment();
            
            seg.setStatus(segment.getStatus().getIdentifier());
            
            //Times
            seg.setAcquisitionStartTime(segment.getStartOfAcquisition());
            seg.setAcquisitionStopTime(segment.getEndOfAcquisition());

            seg.setId(segment.getSegmentId());
            
            //EOP
            EarthObservationEquipmentType eoEquipment = seg.addNewAcquisitionMethod().addNewEarthObservationEquipment();
            //Sensor
            SensorType sensor = eoEquipment.addNewSensor().addNewSensor();
            
            
            //Sensor Type
            CodeType instType = sensor.addNewSensorType();
            instType.setCodeSpace("urn:ogc:def:property:OGC:sensorType");
            instType.setStringValue(eoe.getSensorType());
            //Instrument Mode
            CategoryType instMode = CategoryType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            instMode.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
            instMode.setValue(segment.getInstrumentMode());
            sensor.addNewOperationalMode().set(instMode);
            //Instrument identifier
            InstrumentType instrument = eoEquipment.addNewInstrument().addNewInstrument();
            instrument.setShortName(eoe.getSensorName());
            
            
            //Platform
            PlatformType plat = eoEquipment.addNewPlatform().addNewPlatform();
            plat.setSerialIdentifier(eoe.getPlatformId());
            
            if ("LEO GEO".contains(eoe.getOrbitType())) {
                plat.setOrbitType(OrbitTypeValueType.Enum.forString(eoe.getOrbitType()));
            }
            else
            {
                plat.setOrbitType(OrbitTypeValueType.Enum.forString("LEO"));
            }
            
            plat.setShortName(eoe.getPlatformName());

            eoEquipment.addNewIdentifier().setStringValue(eoe.getPlatformId() + " - " + eoe.getSensorName());

            eoEquipment.addNewDescription().setStringValue(eoe.getPlatformDescription());

            //Polygon
            PolygonType polygon = seg.addNewFootprint().addNewPolygon();

            CoordinatesType coords = CoordinatesType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");

            coords.setStringValue(segment.getPolygon().printCoordinatesGML());

            LinearRingType lineRing = LinearRingType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            lineRing.setCoordinates(coords);
            polygon.addNewExterior().setAbstractRing(lineRing);
            
            SegmentValidationDataDocument valData = SegmentValidationDataDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            valData.addNewSegmentValidationData().setCloudCoverSuccessRate(segment.getCloudCoverSuccessRate());
            seg.addNewExtension().set(valData);
            
            DownlinkInformationDocument downlinkDoc = DownlinkInformationDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            DownlinkInformationType downlink = downlinkDoc.addNewDownlinkInformation();
            
            downlink.setAcquisitionDate(segment.getGroundStationDownlink().getStartOfVisibility());
            
            CategoryType acqStation = CategoryType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            acqStation.setDefinition("urn:ogc:def:property:CEOS:eop:GroundStation");
            acqStation.setValue(segment.getGroundStationDownlink().getGroundStationId());
            
            downlink.addNewAcquisitionStation().set(acqStation);
            
            seg.addNewExtension().set(downlinkDoc);
            
            ss[i] = s;
            i++;
        }
        
        return ss;
    }
    
    
    
    
    
}
