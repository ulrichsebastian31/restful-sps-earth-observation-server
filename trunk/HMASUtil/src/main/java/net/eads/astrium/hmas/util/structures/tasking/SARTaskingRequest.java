/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SARTaskingRequest.java
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
package net.eads.astrium.hmas.util.structures.tasking;

import java.util.Date;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;

/**
 *
 * @author re-sulrich
 */
public class SARTaskingRequest extends Request {

    private SARTaskingParameters parameters;
    
    public SARTaskingRequest(String requestId, Date incomingTime, RequestType type, Status status) {
        super(requestId, incomingTime, type, status);
    }

    public SARTaskingParameters getParameters() {
        return parameters;
    }

    public void setParameters(SARTaskingParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + this.parameters.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
