/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               MissingParameterException.java
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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import net.eads.astrium.hmas.util.FormatHandler;

/**
 *
 * @author re-sulrich
 */
public class MissingParameterException extends DreamException {
    
    /**
      * Creates a 400 request
     */
     public MissingParameterException() {
         super(Response.status(Response.Status.BAD_REQUEST).entity("Missing Parameter Exception").build());
     }

     /**
      * Creates a 400 request with information in text/plain
      * @param message the String that is the entity of the 400 response.
      */
     public MissingParameterException(String message) {
         super(Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build());
     }
     /**
      * Creates a 400 request with information in the given MIME type
      * @param message the String that is the entity of the 400 response.
      * @param type the MIME encoding type of the Exception
      */
     public MissingParameterException(String message, String type) {
         super(
                 Response.status(Response.Status.BAD_REQUEST)
                        .entity(getExceptionText(message,type))
                        .type(FormatHandler.isSupportedType(type))
                        .build());
     }
     /**
      * Creates a 400 request with information in the given MIME type
      * @param message the String that is the entity of the 400 response.
      * @param type the MIME encoding type of the Exception
      */
     public MissingParameterException(String message, String[] outputTypes) {
         super(
                 Response.status(Response.Status.BAD_REQUEST)
                        .entity(getExceptionText(message,FormatHandler.getFirstSupportedType(outputTypes)))
                        .type(FormatHandler.getFirstSupportedType(outputTypes))
                        .build());
     }
     
}
