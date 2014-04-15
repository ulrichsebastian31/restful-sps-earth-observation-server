/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SubmitOperation.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.mp.tasking.SensorFeasibilityPlanningServiceHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.FeasibilityStudyDocument;
import net.opengis.eosps.x20.FeasibilityStudyType;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.SegmentPropertyType;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.eosps.x20.SubmitDocument;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.eosps.x20.SubmitResponseType;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDType;
import net.opengis.eosps.x20.SubmitType;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.TimePeriodType;
import org.apache.xmlbeans.XmlObject;


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
public class SubmitOperation extends EOSPSOperation<MissionPlannerDBHandler,SubmitDocument,SubmitResponseDocument,SubmitFault> {

    
    /**
     * 
     * @param request
     */
    public SubmitOperation(MissionPlannerDBHandler handler, SubmitDocument request){

            super(handler,request);
    }

    @Override
    public void validRequest() throws SubmitFault{
    }

    @Override
    public void executeRequest() throws SubmitFault{

        SubmitType sub = this.getRequest().getSubmit();
        //Create the GetFeasibility request from the current request
        GetFeasibilityDocument getFeasibilityRequest = GetFeasibilityDocument.Factory.newInstance();
        GetFeasibilityType getFeasibility = getFeasibilityRequest.addNewGetFeasibility();
        getFeasibility.setService(sub.getService());
        getFeasibility.setVersion(sub.getVersion());
        getFeasibility.setAcceptFormat(sub.getAcceptFormat());
        getFeasibility.setProcedure(sub.getProcedure());
        getFeasibility.setEoTaskingParameters(sub.getEoTaskingParameters());

        //Call the GetFeasibility operation
        GetFeasibilityOperation getFeasibilityOperation = 
                new GetFeasibilityOperation(this.getConfigurationLoader(), getFeasibilityRequest);
        
        try {
            getFeasibilityOperation.executeRequest();
        } catch (GetFeasibilityFault ex) {
            ex.printStackTrace();
            throw new SubmitFault(ex.getMessage(), ex.getFaultInfo());
        }
        
        //Reading the response from the GetFeasibility execution
        StatusReportType feasibilityStatus = 
                getFeasibilityOperation.getResponse().getGetFeasibilityResponse().getResult().getStatusReport();
        XmlObject[] extensions = getFeasibilityOperation.getResponse().getGetFeasibilityResponse().getExtensionArray();
        
        List<String> segments = new ArrayList<>();
        
        for (XmlObject xmlObject : extensions) {
            FeasibilityStudyType feasibilityStudy = null;
            if (xmlObject instanceof FeasibilityStudyDocument) {
                System.out.println("Document");
                FeasibilityStudyDocument doc = (FeasibilityStudyDocument)xmlObject;
                feasibilityStudy = doc.getFeasibilityStudy();
            }
            if (xmlObject instanceof FeasibilityStudyType) {
                System.out.println("Type");
                feasibilityStudy = (FeasibilityStudyType)xmlObject;
            }
            
            if (feasibilityStudy != null) {
                for (SegmentPropertyType segmentPropertyType : feasibilityStudy.getSegmentArray()) {
                    String segmentId = segmentPropertyType.getSegment().getId();
                    segments.add(segmentId);
                }
            }
        }
        
        //Create the SubmitSegmentByID request
        SubmitSegmentByIDDocument submitRequestDocument = SubmitSegmentByIDDocument.Factory.newInstance();
        SubmitSegmentByIDType submitRequest = submitRequestDocument.addNewSubmitSegmentByID();
        submitRequest.setService(sub.getService());
        submitRequest.setVersion(sub.getVersion());
        submitRequest.setAcceptFormat(sub.getAcceptFormat());
        
        submitRequest.setTask(feasibilityStatus.getTask());
        
        for (String segmentId : segments) {
            submitRequest.addSegmentID(segmentId);
        }
        
        //Call the SubmitSegmentByID operation
        SubmitSegmentByIDOperation submitOperation = 
                new SubmitSegmentByIDOperation(
                        this.getConfigurationLoader(), 
                        submitRequestDocument, SensorFeasibilityPlanningServiceHandler.ExecutionType.planningSynchronous);
        
        submitOperation.executeRequest();
        
        this.setResponse(submitOperation.getResponse());
    }
}
