/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilitiesFault.java
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
 */
package net.eads.astrium.hmas.exceptions;

import javax.xml.ws.WebFault;
import net.opengis.ows.x11.ExceptionReportDocument;


/**
 * This class was generated by Apache CXF 2.6.1
 * 2012-07-06T10:49:28.656+01:00
 * Generated source version: 2.6.1
 */

public class GetCapabilitiesFault extends OWSException {
    
    private static final int CODE = 1;
    
    public GetCapabilitiesFault() {
        super(CODE);
    }
    
    public GetCapabilitiesFault(String message) {
        super(CODE,message);
    }
    
    public GetCapabilitiesFault(Throwable cause) {
        super(CODE,cause);
    }

    public GetCapabilitiesFault(String message, net.opengis.ows.x11.ExceptionReportDocument exception) {
        super(CODE,message,exception);
    }

    public GetCapabilitiesFault(String message, net.opengis.ows.x11.ExceptionReportDocument exception, Throwable cause) {
        super(CODE,message, exception, cause);
    }

    public net.opengis.ows.x11.ExceptionReportDocument getFaultInfo() {
        return this.exception;
    }
}