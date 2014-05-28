/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Responses.java
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
package net.eads.astrium.hmas.eospsstruct;

import java.util.Calendar;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.opengis.eosps.x20.CancelResponseDocument;
import net.opengis.eosps.x20.CancelResponseType;
import net.opengis.eosps.x20.ConfirmResponseDocument;
import net.opengis.eosps.x20.ConfirmResponseType;
import net.opengis.eosps.x20.FeasibilityStudyDocument;
import net.opengis.eosps.x20.FeasibilityStudyType;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseType;
import net.opengis.eosps.x20.GetStatusResponseDocument;
import net.opengis.eosps.x20.GetTaskResponseDocument;
import net.opengis.eosps.x20.ReserveResponseDocument;
import net.opengis.eosps.x20.ReserveResponseType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.eosps.x20.SubmitResponseType;
import net.opengis.eosps.x20.UpdateResponseDocument;
import net.opengis.eosps.x20.UpdateResponseType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.MeasureType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.StringOrRefType;
import net.opengis.ows.x11.AbstractReferenceBaseType;
import net.opengis.ows.x11.ExceptionDocument;
import net.opengis.ows.x11.ExceptionType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.sps.x21.DataAvailableType;
import net.opengis.sps.x21.DescribeResultAccessResponseDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseType;
import org.junit.Test;
import xint.esa.earth.eop.EarthObservationEquipmentType;
import xint.esa.earth.eop.OrbitTypeValueType;
import xint.esa.earth.eop.PlatformType;
import xint.esa.earth.eop.SensorType;

/**
 *
 * @author re-sulrich
 */
public class Responses {
    
    
    @Test
    public void test() {
        
//        System.out.println(getException().xmlText());
//        System.out.println();
//        System.out.println();
//        System.out.println(describeResultAccess().xmlText());
//        System.out.println();
//        System.out.println();
//        System.out.println(getStatus().xmlText());
//        System.out.println();
//        System.out.println();
        System.out.println(getFeasibility().xmlText());
        System.out.println();
        System.out.println();
//        System.out.println(reserve().xmlText());
//        System.out.println();
//        System.out.println();
//        System.out.println(submitSegmentById().xmlText());
//        System.out.println();
//        System.out.println();
//        System.out.println(update().xmlText());
//        System.out.println();
//        System.out.println();
//        System.out.println(submit().xmlText());
//        System.out.println();
//        System.out.println();
    }
    
    private DescribeResultAccessResponseDocument describeResultAccess()
    {
        DescribeResultAccessResponseDocument responseDocument = DescribeResultAccessResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        DescribeResultAccessResponseType resp = responseDocument.addNewDescribeResultAccessResponse();
        DescribeResultAccessResponseType.Availability availability = resp.addNewAvailability();

//        DataNotAvailableType dataNotAvailable = availability.addNewUnavailable().addNewDataNotAvailable();
//        dataNotAvailable.setUnavailableCode("001");
//        dataNotAvailable.addNewMessage().setStringValue("Data is not available yet");
        
        DataAvailableType dataAvailable = availability.addNewAvailable().addNewDataAvailable();
        AbstractReferenceBaseType dataRef = dataAvailable.addNewDataReference().addNewReferenceGroup().addNewAbstractReferenceBase();
    
        dataAvailable.addNewDataReference().addNewReferenceGroup().addNewIdentifier().setStringValue("AD7897CDFG876");
        dataRef.setHref("http://143.2.577.43/requests/data/geotiff/AD7897CDFG876");
        dataRef.setTitle("AD7897CDFG876_GEOTIFF");
        dataRef.setType("image/tiff");
        
        return responseDocument;
    }

    private GetStatusResponseDocument getStatus()
    {
        GetStatusResponseDocument doc = GetStatusResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        StatusReportType statusReport = doc.addNewGetStatusResponse().addNewStatus().addNewStatusReport();
        
//        statusReport.setEoTaskingParameters(getTP());
        
//        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("FEASIBILITY IN_EXECUTION");
        
        
        
        return doc;
    }
    
    private ExceptionDocument getException()
    {
        ExceptionDocument doc = ExceptionDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ExceptionType exc = doc.addNewException();
        exc.setExceptionCode("001");
        exc.addExceptionText("IOException : reading configuration file.");
        
        return doc;
    }
    
    private GetTaskResponseDocument getTask()
    {
        GetTaskResponseDocument doc = GetTaskResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        net.opengis.eosps.x20.TaskType task = doc.addNewGetTaskResponse().addNewTask().addNewTask();
        StatusReportType statusReport = task.addNewStatus().addNewStatusReport();
//        statusReport.setEoTaskingParameters(getTP());
        
//        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("CANCELLED");
        
        return doc;
    }
    
    private CancelResponseDocument cancel() {


        CancelResponseDocument responseDocument = CancelResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        CancelResponseType resp = responseDocument.addNewCancelResponse();
        CancelResponseType.Result result = resp.addNewResult();

        StatusReportType statusReport = result.addNewStatusReport();

//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("CANCELLED");
        
        return responseDocument;
    }

    private ConfirmResponseDocument confirm() {
        
        ConfirmResponseDocument responseDocument = ConfirmResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ConfirmResponseType resp = responseDocument.addNewConfirmResponse();

        ConfirmResponseType.Result result = resp.addNewResult();

        StatusReportType statusReport = result.addNewStatusReport();

//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("CONFIRMED");

        return responseDocument;
    }
    
    private GetFeasibilityResponseDocument getFeasibility() {
        GetFeasibilityResponseDocument doc = GetFeasibilityResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        GetFeasibilityResponseType resp = doc.addNewGetFeasibilityResponse();
        
        GetFeasibilityResponseType.Result result = resp.addNewResult();
        
        StatusReportType statusReport = result.addNewStatusReport();
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("FEASIBILITY SUBMITTED");

//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters().set(EoTaskingParameters.createEOTaskingParameters());
        
        
        FeasibilityStudyDocument studyDoc = FeasibilityStudyDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        FeasibilityStudyType study = studyDoc.addNewFeasibilityStudy();
        
        study.addNewEstimatedCost().setStringValue("1");
        
        SegmentType segment = study.addNewSegment().addNewSegment();
        
        segment.setAcquisitionStartTime(Calendar.getInstance());
        segment.setAcquisitionStopTime(Calendar.getInstance());
        
        
        
        
        segment.setId("327589237");
        EarthObservationEquipmentType eoEquipment = segment.addNewAcquisitionMethod().addNewEarthObservationEquipment();
        SensorType sensor = eoEquipment.addNewSensor().addNewSensor();
        sensor.addNewSensorType().setCodeSpace("opt");
        MeasureType res = sensor.addNewResolution();
        res.setUom("m");
        res.setStringValue("10");
        
        PlatformType platform = eoEquipment.addNewPlatform().addNewPlatform();
        platform.setOrbitType(OrbitTypeValueType.Enum.forString("LEO"));
        platform.setShortName("Sentinel 1");
        
        CodeWithAuthorityType id = eoEquipment.addNewIdentifier();
        id.setStringValue("6302148799");
        StringOrRefType desc = eoEquipment.addNewDescription();
        desc.setStringValue("");
        
        PolygonType polygon = segment.addNewFootprint().addNewPolygon();
        
        CoordinatesType coords = CoordinatesType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        coords.setDecimal(".");
        coords.setCs(",");
        coords.setTs(" ");
        
        coords.setStringValue(""
                + "0.0,0.0 "
                + "0.0,1.0 "
                + "1.0,1.0 "
                + "1.0,0.0 "
                + "0.0,0.0");

        LinearRingType lineRing = LinearRingType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        lineRing.setCoordinates(coords);

        polygon.addNewExterior().setAbstractRing(lineRing);
        
        
        resp.addNewExtension().set(study);
        

        return doc;
    }

    private ReserveResponseDocument reserve() {
        ReserveResponseDocument doc = ReserveResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        ReserveResponseType resp = doc.addNewReserveResponse();
        
        ReserveResponseType.Result result = resp.addNewResult();
        
        
        
        StatusReportType statusReport = result.addNewStatusReport();

//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("RESERVED");

        
        
        
        
        return doc;
    }

    private SubmitResponseDocument submitSegmentById() {
        
        SubmitResponseDocument doc = SubmitResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        SubmitResponseType resp = doc.addNewSubmitResponse();
        
        SubmitResponseType.Result result = resp.addNewResult();
        
        
        
        StatusReportType statusReport = result.addNewStatusReport();

//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("SUBMITTED");

        return doc;
    }

    private UpdateResponseDocument update() {
        
        UpdateResponseDocument doc = UpdateResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        UpdateResponseType resp = doc.addNewUpdateResponse();
        
        UpdateResponseType.Result result = resp.addNewResult();
        StatusReportType statusReport = result.addNewStatusReport();
        
//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("UPDATED");

        return doc;
    }

    private SubmitResponseDocument submit() {
        SubmitResponseDocument doc = SubmitResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        SubmitResponseType resp = doc.addNewSubmitResponse();
        
        SubmitResponseType.Result result = resp.addNewResult();
        
        
        
        
        StatusReportType statusReport = result.addNewStatusReport();

//        statusReport.setEoTaskingParameters(getTP());
        
        statusReport.addNewEoTaskingParameters();
        
        LanguageStringType status = statusReport.addNewStatusMessage();
        status.setStringValue("SUBMITTED");

        return doc;
    }
    
//    public static TaskingParametersType getTP()
//    {
//        TaskHandler tasks = new TaskHandler("C:\\Users\\re-sulrich\\.dream\\mmfas\\gmes-mmfas\\tasks\\00000000001");
//        TaskingParametersType taskingParams = null;
//
//        try {
//            String taskParam = tasks.getEOTaskingParameters();
//            taskingParams = TaskingParametersDocument.Factory.parse(taskParam).getTaskingParameters();
//
//        } catch (IOException ex) {
//            Logger.getLogger(CancelOperation.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XmlException ex) {
//            Logger.getLogger(CancelOperation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return taskingParams;
//    }
}
