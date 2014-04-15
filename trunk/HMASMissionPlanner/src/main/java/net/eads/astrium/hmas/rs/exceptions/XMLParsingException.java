/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               XMLParsingException.java
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
import net.eads.astrium.hmas.util.FormatHandler;

/**
 *
 * @author re-sulrich
 */
public class XMLParsingException extends DreamException {
    
    /**
      * Creates a 400 request
     */
     public XMLParsingException() {
         super(Response.status(Response.Status.BAD_REQUEST).entity("Bad Request Exception").build());
     }
     
     /**
      * Creates a 400 request
     */
     public XMLParsingException(String message) {
         super(Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build());
     }
     
     /**
      * Creates a 400 request with information in the given MIME type
      *  if this one is supported
      *  or text/plain otherwise
      * @param message the String that is the entity of the 400 response.
      * @param type the MIME encoding type of the Exception
      */
     public XMLParsingException(String message, String type) {
         super(
                 Response.status(Response.Status.BAD_REQUEST)
                        .entity(getExceptionText(message,type))
                        .type(FormatHandler.isSupportedType(type))
                        .build());
     }
     /**
      * Creates a 400 request with information in the first valid MIME type
      *  or text/plain otherwise
      * @param message the String that is the entity of the 400 response.
      * @param type the MIME encoding type of the Exception
      */
     public XMLParsingException(String message, String[] outputTypes) {
         super(
                 Response.status(Response.Status.BAD_REQUEST)
                        .entity(getExceptionText(message,FormatHandler.getFirstSupportedType(outputTypes)))
                        .type(FormatHandler.getFirstSupportedType(outputTypes))
                        .build());
     }
     
}
