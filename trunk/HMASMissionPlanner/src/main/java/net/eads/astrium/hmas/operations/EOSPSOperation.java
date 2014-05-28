/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EOSPSOperation.java
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
 */package net.eads.astrium.hmas.operations;

import net.eads.astrium.hmas.exceptions.OWSException;


/**
 * 
 * @author re-sulrich
 * 
 * This class is a generic operation that generalises the EOSPS operations
 * and provides basic functions such as getting request, writing response
 * and two functions called validRequest and executeRequest that will be implemented by the specifications
 * 
 */
public abstract class EOSPSOperation<DBHandler, RequestClass, ResponseClass, ErrorClass extends OWSException> {

	private DBHandler configurationLoader;
	private RequestClass request;
	private ResponseClass response;
	/**
	 * 
	 * @param request
	 */
	public EOSPSOperation(DBHandler loader, RequestClass request) {

		this.setRequest(request);
                configurationLoader = loader;
	}

        
        /**
         * @param configurationFolder
         */
	public final DBHandler getConfigurationLoader() {
        return configurationLoader;

	}

    public final void setConfigurationLoader(DBHandler configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

        
        
	/**
	 * 
	 * @param request
	 */
	public void setRequest(RequestClass request) {

		this.request = request;

	}

	public RequestClass getRequest() {
		return request;
	}

	/**
	 * 
	 * @param response
	 */
	public void setResponse(ResponseClass response) {
		this.response = response;
	}

	public ResponseClass getResponse() {
		return response;
	}
        
	public abstract void validRequest() throws OWSException;

	public abstract void executeRequest() throws OWSException;

}
