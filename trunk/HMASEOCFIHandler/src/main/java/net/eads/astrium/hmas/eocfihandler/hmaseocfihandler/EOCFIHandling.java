/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EOCFIHandling.java
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
package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

import net.eads.astrium.hmas.util.structures.tasking.*;
import net.eads.astrium.hmas.util.structures.tasking.geometries.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public interface EOCFIHandling {
    
    
    public class Factory {
        public static EOCFIHandling newInstance(String satelliteId) throws EoCfiHndlrError {
            return new EoCfiHandler(satelliteId);
        }
    }
    
    public List<Point> getOrbitPropagation( 
            Date oStartTime, 
            Date oEndTime,
            double dTimeIncrement) throws EoCfiHndlrError;

    public List<Segment> getOPTFeasibility(
            String sensorId, 
            OPTTaskingParameters optParam,
            double dMinVisDuration) throws EoCfiHndlrError;
    
    public List<Segment> getSARFeasibility(
            String sensorId, 
            SARTaskingParameters sarParam,
            double dMinVisDuration) throws EoCfiHndlrError;

    public List<SegmentVisGS> getNextStationDownlink(
            Date oAcqEndTime, 
            long iOrbitNum,
            double dMinVisDuration) throws EoCfiHndlrError;
    
    public Date getOrbitBeginTime(long orbitNumber) throws EoCfiHndlrError;
}
