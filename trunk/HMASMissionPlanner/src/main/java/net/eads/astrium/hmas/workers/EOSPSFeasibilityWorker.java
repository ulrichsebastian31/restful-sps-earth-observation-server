/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EOSPSBasicWorker.java
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

import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;

/**
 *
 * @author re-sulrich
 * 
 * This interface provides all the functions of a basic "feasibility" implementation of the EOSPS specification,
 * that is without the submitting part
 * i.e. the FAS
 */
public interface EOSPSFeasibilityWorker extends EOSPSBasicWorker {
    
    public abstract GetFeasibilityResponseDocument getFeasibility(
            GetFeasibilityDocument request) throws GetFeasibilityFault;
    
    public abstract GetFeasibilityResponseDocument getFeasibilityAsynchronous(
            GetFeasibilityDocument request) throws GetFeasibilityFault;
}
