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
 *             that submit is to be treated as confidential and that submit may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.reset;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import net.eads.astrium.hmas.exceptions.GetStatusFault;
import net.eads.astrium.hmas.exceptions.GetTaskFault;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import static net.eads.astrium.hmas.reset.AbstractResource.addEOSPSExtensibleRequestParams;
import net.eads.astrium.hmas.rs.parsers.tasking.GetStatusParser;
import net.eads.astrium.hmas.rs.parsers.tasking.GetTaskParser;
import net.eads.astrium.hmas.rs.parsers.tasking.SubmitParser;
import net.eads.astrium.hmas.util.logging.SysoRedirect;
import net.eads.astrium.hmas.workers.EOSPSPlanningWorker;
import net.opengis.eosps.x20.GetStatusDocument;
import net.opengis.eosps.x20.GetStatusResponseDocument;
import net.opengis.eosps.x20.GetTaskDocument;
import net.opengis.eosps.x20.GetTaskResponseDocument;
import net.opengis.eosps.x20.SubmitDocument;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.reset.x10.PlanningDocument;

/**
 *
 * @author re-sulrich
 */
@Path("reset/1.0.0/planning/")
public class PlanningResource extends AbstractResource {
    
    
    @GET
    @Path("{task}")
    public Response getStatus() {
        
        Response response = null;
        
        String taskID = ui.getPathParameters().getFirst("task");
        System.out.println("GetTask: TaskID : " + taskID);
        
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params = addEOSPSExtensibleRequestParams(params);
        
        GetStatusDocument getStatus = new GetStatusParser().createXMLFromGetRequest(params);
        format = getStatus.getGetStatus().getAcceptFormat();
        
        try (EOSPSPlanningWorker w = (EOSPSPlanningWorker)worker) {
            SysoRedirect.redirectSysoToFiles("log", "log");
            
            GetStatusResponseDocument resp = w.getStatus(getStatus);
            
            response = Response.ok(resp.getGetStatusResponse().getStatusArray(0).xmlText(), format).build();
            
        }catch (ClassCastException e ){
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Worker " + worker.getClass().getName() + 
                    " does not implement the feasibility class."
                        )
                    .build();
            
        } catch (GetStatusFault ex) {
            
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText())
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
    @Path("{task}/segments/")
    public Response getTask() {
        
        Response response = null;
        
        String taskID = ui.getPathParameters().getFirst("task");
        System.out.println("GetTask: TaskID : " + taskID);
        
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params = CapabilitiesResource.addEOSPSExtensibleRequestParams(params);
        
        GetTaskDocument getTask = new GetTaskParser().createXMLFromGetRequest(params);
        format = getTask.getGetTask().getAcceptFormat();
        
        try (EOSPSPlanningWorker w = (EOSPSPlanningWorker)worker) {
            SysoRedirect.redirectSysoToFiles("log", "log");
            
            GetTaskResponseDocument resp = w.getTask(getTask);
            
            response = Response.ok(resp.getGetTaskResponse().xmlText(), format).build();
            
        }catch (ClassCastException e ){
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Worker " + worker.getClass().getName() + 
                    " does not implement the feasibility class."
                        )
                    .build();
            
        } catch (GetTaskFault ex) {
            
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText())
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
    
    @POST
    @Consumes("text/xml")
    public Response submit(String postRequest) {
        
        Response response = null;
        
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params.putAll(ui.getQueryParameters());
        
        System.out.println("Submit: Request : \n" + postRequest);
        SubmitDocument submit = 
                new SubmitParser().createXMLSubmit(postRequest);
        
        format = submit.getSubmit().getAcceptFormat();
        
        try (EOSPSPlanningWorker w = (EOSPSPlanningWorker)worker) {
            
            SysoRedirect.redirectSysoToFiles("log", "log");
            SubmitResponseDocument subResponse = w.submit(submit);
            
            PlanningDocument respDoc = PlanningDocument.Factory.newInstance();
            PlanningDocument.Planning plan = respDoc.addNewPlanning();
            plan.setStatus(
                    subResponse.getSubmitResponse().getResult().getStatusReport());
            
            for (   int i = 0; 
                    i < subResponse.getSubmitResponse().getExtensionArray().length; 
                    i++) {
                
                plan.addNewPlanning().set(
                        subResponse.getSubmitResponse().getExtensionArray(i));
            }
            
            response = Response.ok(respDoc.xmlText(), format).build();
            
        }catch (ClassCastException e ){
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Worker " + worker.getClass().getName() + 
                    " does not implement the planning class."
                        )
                    .build();
            
        } catch (SubmitFault ex) {
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText())
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
    
    @POST
    public Response submit() {
        
        Response response = null;
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params.putAll(ui.getQueryParameters());
        
        try (EOSPSPlanningWorker w = (EOSPSPlanningWorker)worker) {
            
            SubmitDocument submit = 
                    new SubmitParser().createXMLSubmit(params);

            System.out.println("Submit: Procedure : \n" + 
                    submit.getSubmit().getProcedure());

            format = submit.getSubmit().getAcceptFormat();
        
            SysoRedirect.redirectSysoToFiles("log", "log");
            SubmitResponseDocument subResponse = w.submit(submit);
            
            PlanningDocument respDoc = PlanningDocument.Factory.newInstance();
            PlanningDocument.Planning plan = respDoc.addNewPlanning();
            plan.setStatus(
                    subResponse.getSubmitResponse().getResult().getStatusReport());
            
            for (   int i = 0; 
                    i < subResponse.getSubmitResponse().getExtensionArray().length; 
                    i++) {
                
                plan.addNewPlanning().set(
                        subResponse.getSubmitResponse().getExtensionArray(i));
            }
            
            response = Response.ok(respDoc.xmlText(), format).build();
            
        }catch (ClassCastException e ){
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Worker " + worker.getClass().getName() + 
                    " does not implement the feasibility class."
                        )
                    .build();
        } catch (SubmitFault ex) {
            
            if (ex.getFaultInfo()!= null) {
                response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(ex.getFaultInfo().xmlText())
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
    
    @POST
    @Path("{taskID}/segments/{segment}")
    public Response validate() {
        Response response = null;
        
        return response;
    }
}
