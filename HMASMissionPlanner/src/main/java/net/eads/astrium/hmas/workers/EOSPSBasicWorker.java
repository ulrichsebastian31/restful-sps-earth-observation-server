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

import java.util.List;
import net.eads.astrium.hmas.database.DBHandler;
import net.eads.astrium.hmas.exceptions.DescribeSensorFault;
import net.eads.astrium.hmas.exceptions.DescribeTaskingFault;
import net.eads.astrium.hmas.exceptions.OWSException;
import net.eads.astrium.hmas.exceptions.GetCapabilitiesFault;
import net.eads.astrium.hmas.exceptions.GetSensorAvailabilityFault;
import net.eads.astrium.hmas.exceptions.GetStationAvailabilityFault;
import net.eads.astrium.hmas.exceptions.GetStatusFault;
import net.eads.astrium.hmas.exceptions.GetTaskFault;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStatusDocument;
import net.opengis.eosps.x20.GetStatusResponseDocument;
import net.opengis.eosps.x20.GetTaskDocument;
import net.opengis.eosps.x20.GetTaskResponseDocument;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.CapabilitiesDocument;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 * 
 * This interface provides all the functions of a basic "feasibility" implementation of the EOSPS specification,
 * that is without the submitting part
 * i.e. the FAS
 */
public interface EOSPSBasicWorker extends AutoCloseable {
    
    @Override
    public void close();
    
    public String getInstanceDescription() throws Exception;
    
    public String getServerBaseURI();
    public String getService();
    public String getServiceInstance();
    public DBHandler getDatabaseConfiguration();
    
    public void destroy();
    
    public List<String> getInterfaces();
    
    public XmlObject execute(String interfaceId, String request, XmlObject parameters) throws Exception;
    
    public abstract XmlObject executeEOSPS(String request, XmlObject parameters) throws OWSException;
    
    public abstract CapabilitiesDocument getCapabilities(GetCapabilitiesDocument request) throws GetCapabilitiesFault;
    
    public abstract DescribeSensorResponseDocument describeSensor(DescribeSensorDocument request) throws DescribeSensorFault;
    
    public abstract DescribeTaskingResponseDocument describeTasking(DescribeTaskingDocument request) throws DescribeTaskingFault;
    
    public abstract GetSensorAvailabilityResponseDocument getSensorAvailability(GetSensorAvailabilityDocument request) throws GetSensorAvailabilityFault;
    
    public abstract GetStationAvailabilityResponseDocument getStationAvailability(GetStationAvailabilityDocument request) throws GetStationAvailabilityFault;
    
    public abstract GetStatusResponseDocument getStatus(GetStatusDocument request) throws GetStatusFault;
    
    public abstract GetTaskResponseDocument getTask(GetTaskDocument request) throws GetTaskFault;
}
