/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeSensorFault.java
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
import net.opengis.ows.x11.ExceptionDocument;


/**
 * This class was generated by Apache CXF 2.6.1
 * 2012-07-06T10:49:28.625+01:00
 * Generated source version: 2.6.1
 */

public class DescribeSensorFault extends OWSException {
    
    public DescribeSensorFault() {
        super();
    }
    
    public DescribeSensorFault(String message) {
        super(message);
        
        exception = ExceptionDocument.Factory.newInstance();
        exception.addNewException().addExceptionText(message);
    }
    
    public DescribeSensorFault(String message, Throwable cause) {
        super(message, cause);
        exception.addNewException().addExceptionText(message);
    }

    public DescribeSensorFault(String message, net.opengis.ows.x11.ExceptionDocument exception) {
        super(message);
        this.exception = exception;
    }

    public DescribeSensorFault(String message, net.opengis.ows.x11.ExceptionDocument exception, Throwable cause) {
        super(message, cause);
        this.exception = exception;
    }

    public net.opengis.ows.x11.ExceptionDocument getFaultInfo() {
        return this.exception;
    }
}