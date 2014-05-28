/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ServiceNotFoundException.java
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
package net.eads.astrium.hmas.rs.exceptions;

import javax.ws.rs.core.Response;

/**
 *
 * @author re-sulrich
 */
public class ServiceNotFoundException extends DreamException {
    
    /**
      * Create a HTTP 401 (Unauthorized) exception.
     */
     public ServiceNotFoundException() {
         super(Response.status(Response.Status.NOT_FOUND).build());
     }

     /**
      * Create a HTTP 404 (Not Found) exception.
      * @param message the String that is the entity of the 404 response.
      */
     public ServiceNotFoundException(String service) {
         super(
                 Response.status(Response.Status.NOT_FOUND)
                 .entity("Service " + service + " not found.")
                 .type("text/plain")
                 .build()
             );
     }
}
