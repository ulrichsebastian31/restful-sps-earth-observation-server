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

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;
import net.eads.astrium.hmas.rs.exceptions.RequestNotFoundException;
import net.eads.astrium.hmas.util.logging.SysoRedirect;
import net.opengis.ows.x11.OperationDocument;
import net.opengis.reset.x10.CapabilitiesDocument;
import net.opengis.reset.x10.ClassId;
import net.opengis.reset.x10.RESETContentsType;
import net.opengis.reset.x10.ResourceId;
import net.opengis.sps.x21.CapabilitiesType;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.SPSContentsType;

/**
 *
 * @author re-sulrich
 */
@Path("reset/1.0.0{section : (/[^/]+?)?}")
public class CapabilitiesResource extends AbstractResource {
    
    @GET
    public Response getCapabilities()
    {
        
        URI baseURI = httpContext.getUriInfo().getAbsolutePath();
        serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort() + baseURI.getPath();

        if (serversAdress.contains("1.0.0"))
        {
            serversAdress = serversAdress.substring(0, serversAdress.lastIndexOf("1.0.0") + "1.0.0".length());
        }
        if (!serversAdress.endsWith("/")) serversAdress += "/";

        System.out.println("Server base address : " + serversAdress);

        Response response = null;
        
        try {
            
            SysoRedirect.redirectSysoToFiles("log", "log");
            
            //Add a default request
            //TODO: put parameters (format ? osdd ?)
            GetCapabilitiesDocument doc = GetCapabilitiesDocument.Factory.newInstance();
            doc.addNewGetCapabilities2();
            
            net.opengis.sps.x21.CapabilitiesDocument capabilities = worker.getCapabilities(doc);
            CapabilitiesDocument respdoc = getRESETCapabilities(capabilities);
            
            String responseText = "";
            
            String section = ui.getPathParameters().getFirst("section");
           
            System.out.println("Section : " + section);
            
            //Selecting section
            if (section != null && !section.equals("")) {
                 //Removing first character : /
                section = section.substring(1);
                
                switch (section) {
                    case "ServiceIdentification":
                        responseText = respdoc.getCapabilities().getServiceIdentification().xmlText();
                    break;
                    case "ServiceProvider":
                        responseText = respdoc.getCapabilities().getServiceProvider().xmlText();
                    break;
                    case "Notifications":
                        responseText = respdoc.getCapabilities().getNotifications().xmlText();
                    break;
                    case "Contents":
                        responseText = respdoc.getCapabilities().getContents().xmlText();
                    break;
                }
            }
            //If no section chosen, put the whole Capabilities
            if (responseText == null || responseText.equals("")) {
                responseText = respdoc.xmlText();
            }
            
            response = Response.ok(responseText, "text/xml").build();
            
        } catch (Exception ex) {
            Logger.getLogger(CapabilitiesResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (response == null) {
            
            System.out.println("Response null");
            
            throw new RequestNotFoundException(request);
        }
        
        return response;
    }
    
    private CapabilitiesDocument getRESETCapabilities(
            net.opengis.sps.x21.CapabilitiesDocument capabilitiesDocument) {
        
        CapabilitiesType capabilities = capabilitiesDocument.getCapabilities();
        
        CapabilitiesDocument doc = CapabilitiesDocument.Factory.newInstance();
        CapabilitiesDocument.Capabilities caps = doc.addNewCapabilities();

        caps.setServiceIdentification(capabilities.getServiceIdentification());
        caps.setServiceProvider(capabilities.getServiceProvider());
        caps.setExtensionArray(capabilities.getExtensionArray());
        caps.setUpdateSequence(capabilities.getUpdateSequence());
        
        caps.addNewNotifications().setNotificationProducerMetadata(
                capabilities.getNotifications().getNotificationProducerMetadata()
            );
        SPSContentsType contents = capabilities.getContents().getSPSContents();
        
        RESETContentsType conts = caps.addNewContents();
        conts.setIdentifier(contents.getIdentifier());
        conts.setNameArray(contents.getNameArray());
        conts.setDescription(contents.getDescription());
        conts.setExtensionArray(contents.getExtensionArray());
        conts.setMinStatusTime(conts.getMinStatusTime());
        conts.setObservablePropertyArray(contents.getObservablePropertyArray());
        conts.setProcedureDescriptionFormatArray(contents.getProcedureDescriptionFormatArray());
        conts.setSupportedEncodingArray(contents.getSupportedEncodingArray());
        
        Set<String> implementedClasses = new HashSet<>();
        Set<String> implementedResources = new HashSet<>();
        implementedResources.add("capabilities");
        implementedResources.add("procedures");
        
        System.out.println("Operations : " + capabilities.getOperationsMetadata().getOperationArray().length);
        
        for (int i = 0; i < capabilities.getOperationsMetadata().getOperationArray().length; i++) {
            OperationDocument.Operation operation = 
                    capabilities.getOperationsMetadata().getOperationArray(i);
            System.out.println("" + i);
            System.out.println("XML : \n" + operation.xmlText());
            System.out.println("name : " + operation.getName());
            switch (operation.getName()) {
                case "GetFeasibility":
                    implementedClasses.add("feasibility");
                    implementedResources.add("feasibility");
                break;
                case "SubmitSegmentByID":
                    implementedClasses.add("feasibilityplanning");
                    implementedResources.add("planning");
                break;
                case "Submit":
                    implementedClasses.add("planning");
                    implementedResources.add("planning");
                break;
                case "Reserve":
                    //Logically, a reservation server will also implement the planning class (Submit)
                    //However, not obliged theoretically so discarded here
//                    implementedClasses.add("planning");
                    implementedClasses.add("reservation");
                    implementedResources.add("reservation");
                    implementedResources.add("planning");
                break;
                case "Cancel":
                    implementedClasses.add("cancellation");
                break;
            }
        }
        for (String classe : implementedClasses) {
            
            switch (classe) {
                case "feasibility":
                    conts.addNewSupportedClass().setResource(ClassId.FEASIBILITY);
                break;
                case "feasibilityplanning":
                    conts.addNewSupportedClass().setResource(ClassId.FEASIBILITYPLANNING);
                break;
                case "planning":
                    conts.addNewSupportedClass().setResource(ClassId.PLANNING);
                break;
                case "reservation":
                    conts.addNewSupportedClass().setResource(ClassId.RESERVATION);
                break;
                case "cancellation":
                    conts.addNewSupportedClass().setResource(ClassId.CANCELLATION);
                break;
            }
        }
        
        for (String res : implementedResources) {
            switch (res) {
                case "capabilities":
                    RESETContentsType.ResourceURL capa = conts.addNewResourceURL();
                    capa.setResource(ResourceId.CAPABILITIES);
                    capa.setMethods("GET");
                    capa.setURL(serversAdress + "");
                    String[] sections = new String[]{"ServiceIdentification","ServiceProvider","Notifications","Contents"};
                    for (int i = 0; i < sections.length; i++) {
                        RESETContentsType.ResourceURL sect = conts.addNewResourceURL();
                        sect.setResource(ResourceId.CAPABILITIES);
                        sect.setMethods("GET");
                        sect.setURL(serversAdress + sections[i]);
                    }
                break;
                case "procedures":
                    RESETContentsType.ResourceURL avail = conts.addNewResourceURL();
                    avail.setResource(ResourceId.PROCEDURES);
                    avail.setMethods("GET");
                    avail.setURL(serversAdress + "procedures/{procedureID}/availibility");
                    
                    RESETContentsType.ResourceURL desctask = conts.addNewResourceURL();
                    desctask.setResource(ResourceId.PROCEDURES);
                    desctask.setMethods("GET");
                    desctask.setURL(serversAdress + "procedures/{procedureID}/tasking");
                    
                    RESETContentsType.ResourceURL descsens = conts.addNewResourceURL();
                    descsens.setResource(ResourceId.PROCEDURES);
                    descsens.setMethods("GET");
                    descsens.setURL(serversAdress + "procedures/{procedureID}/sensorML");
                break;
                case "feasibility":
                    
                    RESETContentsType.ResourceURL getfeas = conts.addNewResourceURL();
                    getfeas.setResource(ResourceId.FEASIBILITY);
                    getfeas.setMethods("POST");
                    getfeas.setURL(serversAdress + "feasibility/");
                    
                    RESETContentsType.ResourceURL getstat = conts.addNewResourceURL();
                    getstat.setResource(ResourceId.FEASIBILITY);
                    getstat.setMethods("GET");
                    getstat.setURL(serversAdress + "feasibility/{taskID}");
                    
                    RESETContentsType.ResourceURL gettask = conts.addNewResourceURL();
                    gettask.setResource(ResourceId.FEASIBILITY);
                    gettask.setMethods("GET");
                    gettask.setURL(serversAdress + "feasibility/{taskID}/segments/");
                    
                    if (implementedClasses.contains("feasibilityplanning")) {
                        
                        RESETContentsType.ResourceURL submitSegmentById = conts.addNewResourceURL();
                        submitSegmentById.setResource(ResourceId.FEASIBILITY);
                        submitSegmentById.setMethods("POST");
                        submitSegmentById.setURL(serversAdress + "feasibility/{taskID}/segments/{segmentId*}");
                    }
                break;
                case "planning":
                    
                    RESETContentsType.ResourceURL submit = conts.addNewResourceURL();
                    submit.setResource(ResourceId.PLANNING);
                    submit.setMethods("POST");
                    submit.setURL(serversAdress + "planning/");
                break;
                case "reservation":
                    RESETContentsType.ResourceURL reserve = conts.addNewResourceURL();
                    reserve.setResource(ResourceId.RESERVATION);
                    reserve.setMethods("POST");
                    reserve.setURL(serversAdress + "reservation/");
                break;
            }
                
        }
        
        return doc;
    }
}
