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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author re-sulrich
 */
@Path("reset/1.0.0/reservation/")
public class ReservationResource extends AbstractResource {
    
    private String request;
    private String format;
    
    @GET
    public Response reserve() {
        Response response = null;
        
        response = Response.status(Response.Status.UNAUTHORIZED).build();
        
        return response;
    }
    
    @GET
    @Path("{task}/")
    public Response getStatus() {
        Response response = null;
        
        response = Response.status(Response.Status.UNAUTHORIZED).build();
        
        return response;
    }
    
    @GET
    @Path("{task}/segments/")
    public Response getTask() {
        Response response = null;
        
        response = Response.status(Response.Status.UNAUTHORIZED).build();
        
        return response;
    }
    
    @POST
    @Path("{task}/segments/")
    public Response confirm() {
        Response response = null;
        
        response = Response.status(Response.Status.UNAUTHORIZED).build();
        
        return response;
    }
    
    @PUT
    @Path("{task}/")
    public Response update() {
        Response response = null;
        
        response = Response.status(Response.Status.UNAUTHORIZED).build();
        
        return response;
    }
}
