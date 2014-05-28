/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FASWebService.java
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
package net.eads.astrium.hmas.reset;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.exceptions.DescribeSensorFault;
import net.eads.astrium.hmas.exceptions.DescribeTaskingFault;
import net.eads.astrium.hmas.exceptions.GetSensorAvailabilityFault;
import net.eads.astrium.hmas.rs.parsers.describing.DescribeSensorParser;
import net.eads.astrium.hmas.rs.parsers.describing.DescribeTaskingParser;
import net.eads.astrium.hmas.rs.parsers.describing.GetSensorAvailibilityParser;
import net.eads.astrium.hmas.util.logging.SysoRedirect;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseType;
import net.opengis.reset.x10.SensorAvailibilityDocument;
import net.opengis.reset.x10.SensorDescriptionDocument;
import net.opengis.reset.x10.TaskingDescriptionDocument;
import net.opengis.sensorML.x102.SensorMLDocument;
import net.opengis.sensorML.x102.SensorMLDocument.SensorML;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 */
@Path("reset/1.0.0/procedures/{procedure}/")
public class ProceduresResource extends AbstractResource {
    
    @GET
    @Path("{sensorDescriptionFormat}")
    public Response describeSensor() {
        
        Response response = null;
        
        String procedure = ui.getPathParameters().getFirst("procedure");
        String descFormat = ui.getPathParameters().getFirst("sensorDescriptionFormat");
        
        System.out.println("DescribeSensor: Procedure : " + procedure + " , format : " + descFormat);
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params = CapabilitiesResource.addEOSPSExtensibleRequestParams(params);
        
        DescribeSensorDocument describeSensor = new DescribeSensorParser().createXMLFromGetRequest(params);
        format = describeSensor.getDescribeSensor().getAcceptFormat();
        
        try {
            SysoRedirect.redirectSysoToFiles("log", "log");
            
            DescribeSensorResponseDocument resp = worker.describeSensor(describeSensor);
            
            SensorDescriptionDocument doc = SensorDescriptionDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInnerInstance());
            SensorDescriptionDocument.SensorDescription sensDesc = doc.addNewSensorDescription();
            sensDesc.setSensor(resp.getDescribeSensorResponse2().getDescriptionArray(0).
                            getSensorDescription());
            
            response = Response.ok(
                    sensDesc.xmlText(OGCNamespacesXmlOptions.getInstance()), 
                    format).build();
            
        } catch (DescribeSensorFault ex) {
            
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText(OGCNamespacesXmlOptions.getInstance()))
                    .build();
            }
            else {
                response = Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .type(MediaType.APPLICATION_XML)
                        .entity(ex.getMessage()
                                + "\n" + "Contact the server administrator."
                            )
                        .build();
            }
            
            ex.printStackTrace();
            
        } catch (Exception ex) {
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getMessage()
                            + "\n" + "Contact the server administrator."
                        )
                    .build();
            
            ex.printStackTrace();
        }
            
        return response;
    }
    
    @GET
    @Path("tasking")
    public Response describeTasking() {
        
        Response response = null;
        
        String procedure = ui.getPathParameters().getFirst("procedure");
        
        System.out.println("DescribeTasking: Procedure : " + procedure);
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params = addEOSPSExtensibleRequestParams(params);
        
        DescribeTaskingDocument describeTasking = new DescribeTaskingParser().createXMLFromGetRequest(params);
        format = describeTasking.getDescribeTasking().getAcceptFormat();
        
        try {
            
            SysoRedirect.redirectSysoToFiles("log", "log");
            
            DescribeTaskingResponseDocument resp = worker.describeTasking(describeTasking);
            
            TaskingDescriptionDocument doc = TaskingDescriptionDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            TaskingDescriptionDocument.TaskingDescription taskDesc = doc.addNewTaskingDescription();
            taskDesc.setTaskParameters(resp.getDescribeTaskingResponse2().getEoTaskingParameters());
            
            String respText = doc.xmlText(OGCNamespacesXmlOptions.getInstance());
            
            response = Response.ok(respText, format).build();
            
        } catch (DescribeTaskingFault ex) {
            
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText(OGCNamespacesXmlOptions.getInstance()))
                    .build();
            }
            else {
                response = Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .type(MediaType.APPLICATION_XML)
                        .entity(ex.getMessage()
                                + "\n" + "Contact the server administrator."
                            )
                        .build();
            }
            
            ex.printStackTrace();
            
        } catch (Exception ex) {
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getMessage()
                            + "\n" + "Contact the server administrator."
                        )
                    .build();
            
            ex.printStackTrace();
        }
            
        return response;
    }
    
    @GET
    @Path("availibility")
    public Response getSensorAvailibility() {
        Response response = null;
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params.putAll(ui.getQueryParameters());
        
        String procedure = params.getFirst("procedure");
        String begin = params.getFirst("begin");
        String end = params.getFirst("end");
        
        System.out.println("GetSensorAvailability: Procedure : " + procedure + " , dates : " + begin + " - " + end);
        params = CapabilitiesResource.addEOSPSExtensibleRequestParams(params);
        
        GetSensorAvailabilityDocument getSensorAvailibility = new GetSensorAvailibilityParser().createXMLFromGetRequest(params);
        format = getSensorAvailibility.getGetSensorAvailability().getAcceptFormat();
        
        try {
            
            SysoRedirect.redirectSysoToFiles("log", "log");
            GetSensorAvailabilityResponseDocument resp = worker.getSensorAvailability(getSensorAvailibility);
            
            SensorAvailibilityDocument doc = SensorAvailibilityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            SensorAvailibilityDocument.SensorAvailibility avail = doc.addNewSensorAvailibility();
            
            avail.addNewResponsePeriod().setTimePeriod(resp.getGetSensorAvailabilityResponse().getResponsePeriod().getTimePeriod());
            
            for (int i = 0; i < resp.getGetSensorAvailabilityResponse().getAvailabilityPeriodArray().length; i++) {
                GetSensorAvailabilityResponseType.AvailabilityPeriod availI = resp.getGetSensorAvailabilityResponse().getAvailabilityPeriodArray(i);
                avail.addNewAvailabilityPeriod().setTimePeriod(availI.getTimePeriod());
            }
            
            response = Response.ok(doc.xmlText(OGCNamespacesXmlOptions.getInstance()), format).build();
            
        } catch (GetSensorAvailabilityFault ex) {
            
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText(OGCNamespacesXmlOptions.getInstance()))
                    .build();
            }
            else {
                response = Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .type(MediaType.APPLICATION_XML)
                        .entity(ex.getMessage()
                                + "\n" + "Contact the server administrator."
                            )
                        .build();
            }
            
            ex.printStackTrace();
            
        } catch (Exception ex) {
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getMessage()
                            + "\n" + "Contact the server administrator."
                        )
                    .build();
            
            ex.printStackTrace();
        }
            
        return response;
    }
}
