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

import net.eads.astrium.hmas.exceptions.CancelFault;
import net.opengis.eosps.x20.CancelDocument;
import net.opengis.eosps.x20.CancelResponseDocument;

/**
 *
 * @author re-sulrich
 * 
 * This interface provides all the functions of a basic "feasibility" implementation of the EOSPS specification,
 * that is without the submitting part
 * i.e. the FAS
 */
public interface EOSPSCancellationWorker extends EOSPSBasicWorker {
    
    public abstract CancelResponseDocument cancel(CancelDocument request) throws CancelFault;
}
