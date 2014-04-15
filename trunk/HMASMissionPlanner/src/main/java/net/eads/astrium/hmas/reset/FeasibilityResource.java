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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.exceptions.GetStatusFault;
import net.eads.astrium.hmas.exceptions.GetTaskFault;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import net.eads.astrium.hmas.rs.parsers.tasking.GetFeasibilityParser;
import net.eads.astrium.hmas.rs.parsers.tasking.GetStatusParser;
import net.eads.astrium.hmas.rs.parsers.tasking.GetTaskParser;
import net.eads.astrium.hmas.rs.parsers.tasking.SubmitSegmentByIDParser;
import net.eads.astrium.hmas.util.logging.SysoRedirect;
import net.eads.astrium.hmas.workers.EOSPSFeasibilityPlanningWorker;
import net.eads.astrium.hmas.workers.EOSPSFeasibilityWorker;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.GetStatusDocument;
import net.opengis.eosps.x20.GetStatusResponseDocument;
import net.opengis.eosps.x20.GetTaskDocument;
import net.opengis.eosps.x20.GetTaskResponseDocument;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.reset.x10.FeasibilityDocument;
import net.opengis.reset.x10.PlanningDocument;

/**
 *
 * @author re-sulrich
 */
@Path("reset/1.0.0/feasibility/")
public class FeasibilityResource extends AbstractResource {
    
    
    @GET
    public Response getUserTasksStatus() {
        
        Response response = null;
        
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params = addEOSPSExtensibleRequestParams(params);
        
        GetTaskDocument getTask = new GetTaskParser().createXMLFromGetRequest(params);
        format = getTask.getGetTask().getAcceptFormat();
        
        try (EOSPSFeasibilityWorker w = (EOSPSFeasibilityWorker)worker) {
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
    @Path("{task}")
    public Response getStatus() {
        
        Response response = null;
        
        String taskID = ui.getPathParameters().getFirst("task");
        System.out.println("GetStatus: TaskID : " + taskID);
        
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params = addEOSPSExtensibleRequestParams(params);
        
        GetStatusDocument getStatus = new GetStatusParser().createXMLFromGetRequest(params);
        format = getStatus.getGetStatus().getAcceptFormat();
        
        try (EOSPSFeasibilityWorker w = (EOSPSFeasibilityWorker)worker) {
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
        
        try (EOSPSFeasibilityWorker w = (EOSPSFeasibilityWorker)worker) {
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
    public Response getFeasibility(String postRequest) {
        
//        EOSPSFeasibilityWorker w = null;
//        if (worker instanceof EOSPSFeasibilityWorker) {
//            w = (EOSPSFeasibilityWorker)worker;
//        }
        
        Response response = null;
        
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params.putAll(ui.getQueryParameters());
        
        System.out.println("GetFeasibility: Request : \n" + postRequest);
        GetFeasibilityDocument getFeasibility = 
                new GetFeasibilityParser().createXMLGetFeasibility(postRequest);
        
        format = getFeasibility.getGetFeasibility().getAcceptFormat();
        
        try (EOSPSFeasibilityWorker w = (EOSPSFeasibilityWorker)worker) {
            
            SysoRedirect.redirectSysoToFiles("log", "log");
            GetFeasibilityResponseDocument getFeasResponse = w.getFeasibility(getFeasibility);
            
            FeasibilityDocument respDoc = FeasibilityDocument.Factory.newInstance();
            FeasibilityDocument.Feasibility feas = respDoc.addNewFeasibility();
            feas.setStatus(
                    getFeasResponse.getGetFeasibilityResponse().getResult().getStatusReport());
            
            for (   int i = 0; 
                    i < getFeasResponse.getGetFeasibilityResponse().getExtensionArray().length; 
                    i++) {
                
                feas.addNewFeasibility().set(
                        getFeasResponse.getGetFeasibilityResponse().getExtensionArray(i));
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
            
        } catch (GetFeasibilityFault ex) {
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
    public Response getFeasibility() {
        
        Response response = null;
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params.putAll(ui.getQueryParameters());
        
        try (EOSPSFeasibilityWorker w = (EOSPSFeasibilityWorker)worker) {
            
            GetFeasibilityDocument getFeasibility = 
                    new GetFeasibilityParser().createXMLGetFeasibility(params);

            System.out.println("GetFeasibility: Procedure : \n" + 
                    getFeasibility.getGetFeasibility().getProcedure());

            format = getFeasibility.getGetFeasibility().getAcceptFormat();
        
            SysoRedirect.redirectSysoToFiles("log", "log");
            GetFeasibilityResponseDocument getFeasResponse = w.getFeasibility(getFeasibility);
            
            FeasibilityDocument respDoc = FeasibilityDocument.Factory.newInstance();
            FeasibilityDocument.Feasibility feas = respDoc.addNewFeasibility();
            feas.setStatus(
                    getFeasResponse.getGetFeasibilityResponse().getResult().getStatusReport());
            
            for (int i = 0; 
                    i < getFeasResponse.getGetFeasibilityResponse().getExtensionArray().length; 
                    i++) {
                
                feas.addNewFeasibility().set(
                        getFeasResponse.getGetFeasibilityResponse().getExtensionArray(i));
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
        } catch (GetFeasibilityFault ex) {
            
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
    @Path("{task}/segments/{segment}")
    public Response submitSegmentsByID(String postRequest) {
        
        Response response = null;
        MultivaluedMap<String, String> params = ui.getPathParameters();
        params.putAll(ui.getQueryParameters());
        
        String service = params.getFirst("service");
        String serviceId = params.getFirst("serviceId");
        
        System.out.println("SubmitSegmentByID: Request : \n" + postRequest);
        
        params = addEOSPSExtensibleRequestParams(params);
        
        SubmitSegmentByIDDocument subSegs = 
                new SubmitSegmentByIDParser().createXMLFromGetRequest(params);
        
        format = subSegs.getSubmitSegmentByID().getAcceptFormat();
        
        try (EOSPSFeasibilityPlanningWorker w = (EOSPSFeasibilityPlanningWorker)worker){
            SysoRedirect.redirectSysoToFiles("log", "log");
            
            SubmitResponseDocument subSegsResponse = 
                    w.submitSegmentByID(subSegs);

            PlanningDocument respDoc = PlanningDocument.Factory.newInstance();
            PlanningDocument.Planning plan = respDoc.addNewPlanning();
            plan.setStatus(subSegsResponse.getSubmitResponse().getResult().getStatusReport());
            plan.addNewPlanning().set(subSegsResponse.getSubmitResponse().getExtensionArray(0));

            response = Response.ok(respDoc.xmlText(), format).build();
            
        }catch (ClassCastException e ){
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Worker " + worker.getClass().getName() + 
                    " does not implement the feasibility planning class."
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
}
