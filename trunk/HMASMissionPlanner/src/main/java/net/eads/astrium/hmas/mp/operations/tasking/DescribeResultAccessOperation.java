/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeResultAccessOperation.java
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
 */package net.eads.astrium.hmas.mp.operations.tasking;

import net.eads.astrium.hmas.exceptions.DescribeResultAccessFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.opengis.sps.x21.DataNotAvailableType;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseType;


/**
 * @file DescribeResultAccessOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The DescribeResultAccess Operation permits to task a satellite mission planner
 */
public class DescribeResultAccessOperation extends EOSPSOperation<MissionPlannerDBHandler,DescribeResultAccessDocument,DescribeResultAccessResponseDocument,DescribeResultAccessFault> {

    
	/**
	 * 
	 * @param request
	 */
	public DescribeResultAccessOperation(MissionPlannerDBHandler handler, DescribeResultAccessDocument request){

            super(handler,request);
	}

	@Override
	public void validRequest() throws DescribeResultAccessFault{
	}

	@Override
	public void executeRequest() throws DescribeResultAccessFault{
		
            this.validRequest();
		
            DescribeResultAccessResponseDocument responseDocument = DescribeResultAccessResponseDocument.Factory.newInstance();
            DescribeResultAccessResponseType resp = responseDocument.addNewDescribeResultAccessResponse();
            DescribeResultAccessResponseType.Availability availability = resp.addNewAvailability();
            
            DataNotAvailableType dataNotAvailable = availability.addNewUnavailable().addNewDataNotAvailable();
            dataNotAvailable.setUnavailableCode("001");
            dataNotAvailable.addNewMessage().setStringValue("Data is not available yet");
            
            this.setResponse(responseDocument);
	}
}
