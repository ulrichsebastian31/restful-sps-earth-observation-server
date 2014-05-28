package net.eads.astrium.hmas.workers;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EOSPSFullWorker.java
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


import net.eads.astrium.hmas.exceptions.ConfirmFault;
import net.eads.astrium.hmas.exceptions.DescribeResultAccessFault;
import net.eads.astrium.hmas.exceptions.UpdateFault;
import net.opengis.eosps.x20.ConfirmDocument;
import net.opengis.eosps.x20.ConfirmResponseDocument;
import net.opengis.eosps.x20.ReserveDocument;
import net.opengis.eosps.x20.ReserveResponseDocument;
import net.opengis.eosps.x20.UpdateDocument;
import net.opengis.eosps.x20.UpdateResponseDocument;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseDocument;


/**
 *
 * @author re-sulrich
 * 
 * This interface provides all the functions of a full implementation of the EOSPS specification,
 * i.e. the MMFAS
 */
public interface EOSPSReservationWorker extends EOSPSPlanningWorker {
    
    public abstract ReserveResponseDocument reserve(
            ReserveDocument request) throws ConfirmFault;
    
    public abstract ConfirmResponseDocument confirm(
            ConfirmDocument request) throws ConfirmFault;
    
    public abstract UpdateResponseDocument update(
            UpdateDocument request) throws UpdateFault;

    public abstract DescribeResultAccessResponseDocument describeResultAccess(
            DescribeResultAccessDocument request) throws DescribeResultAccessFault;
}
