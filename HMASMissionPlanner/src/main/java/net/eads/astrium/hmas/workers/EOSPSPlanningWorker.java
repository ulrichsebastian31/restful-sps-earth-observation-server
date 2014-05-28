/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EOSPSPlanningWorker.java
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
package net.eads.astrium.hmas.workers;

import net.eads.astrium.hmas.exceptions.DescribeResultAccessFault;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import net.eads.astrium.hmas.exceptions.ValidateFault;
import net.opengis.eosps.x20.SubmitDocument;
import net.opengis.eosps.x20.SubmitResponseDocument;
import net.opengis.eosps.x20.ValidateDocument;
import net.opengis.eosps.x20.ValidateResponseDocument;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseDocument;


/**
 *
 * @author re-sulrich
 * 
 * This interface provides all the functions of a full implementation of the EOSPS specification,
 * i.e. the MMFAS
 */
public interface EOSPSPlanningWorker extends EOSPSBasicWorker {
    
    public abstract SubmitResponseDocument submit(SubmitDocument request) throws SubmitFault;
    
    public abstract ValidateResponseDocument validate(ValidateDocument request) throws ValidateFault;
    
    public abstract DescribeResultAccessResponseDocument describeResultAccess(
            DescribeResultAccessDocument request) throws DescribeResultAccessFault;
    
}
