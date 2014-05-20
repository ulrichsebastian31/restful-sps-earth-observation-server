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
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
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
    
    public OWSException(int code) {
        super();
        this.exception = ExceptionDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ExceptionType exc = this.exception.addNewException();
        exc.setExceptionCode("" + code);
        exc.addExceptionText("" + this.getMessage());
    }
    
    public OWSException(int code, String message) {
        super(message);
        this.exception = ExceptionDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ExceptionType exc = this.exception.addNewException();
        exc.setExceptionCode("" + code);
        exc.addExceptionText("" + this.getMessage());
    }
    
    public OWSException(int code, Throwable cause) {
        super(cause.getMessage(), cause);
        this.exception = ExceptionDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ExceptionType exc = this.exception.addNewException();
        exc.setExceptionCode("" + code);
        exc.addExceptionText(cause.getMessage());
    }

    public OWSException(int code, String message, ExceptionDocument exception) {
        this(code, message);
        if (exception != null) {
            exception.getException().setExceptionCode("" + code);
            exception.getException().setExceptionTextArray(
                    0, this.exception.getException().getExceptionTextArray(0).concat("\r\nCaused by : \r\n" + message));

            this.exception = exception;
        }
    }

    public OWSException(int code, String message, ExceptionDocument exception, Throwable cause) {
        super(message, cause);
        this.exception = exception;
    }

    public ExceptionDocument getFaultInfo() {
        return this.exception;
    }
}
