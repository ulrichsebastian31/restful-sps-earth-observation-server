/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OWSException.java
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
package net.eads.astrium.hmas.exceptions;

import javax.xml.ws.WebFault;
import net.opengis.ows.x11.ExceptionDocument;
import net.opengis.ows.x11.ExceptionType;

/**
 *
 * @author re-sulrich
 */
@WebFault(name = "Exception", targetNamespace = "http://www.opengis.net/ows/1.1")
public class OWSException extends Exception {
    
    protected static final long serialVersionUID = -6279041538977056569L;
    protected ExceptionDocument exception;
    
    public OWSException() {
        super();
        this.exception = ExceptionDocument.Factory.newInstance();
        ExceptionType exc = this.exception.addNewException();
        exc.addExceptionText("" + this.getClass().getName());
    }
    
    public OWSException(String message) {
        super(message);
        this.exception = ExceptionDocument.Factory.newInstance();
        ExceptionType exc = this.exception.addNewException();
        exc.addExceptionText(this.getClass().getName() + " : " + message);
    }
    
    public OWSException(String message, Throwable cause) {
        super(message, cause);
        this.exception = ExceptionDocument.Factory.newInstance();
        ExceptionType exc = this.exception.addNewException();
        exc.addExceptionText(message);
    }

    public OWSException(String message, ExceptionDocument exception) {
        super(message);
        this.exception = exception;
    }

    public OWSException(String message, ExceptionDocument exception, Throwable cause) {
        super(message, cause);
        this.exception = exception;
    }

    public ExceptionDocument getFaultInfo() {
        return this.exception;
    }
}
