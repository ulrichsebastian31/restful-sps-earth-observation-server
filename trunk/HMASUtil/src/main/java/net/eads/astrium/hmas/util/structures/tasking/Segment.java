/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Segment.java
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

import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public class Segment {
    
    //Comes from the database
    private String segmentId;
    private Status status;
    
    private Polygon polygon;
    
    private long startOrbit;
    private long endOrbit;
    
    private Calendar startOfAcquisition;
    private Calendar endOfAcquisition;
    
    private double cloudCoverSuccessRate;
    private double estimatedCost;
    
    private String instrumentMode;
    
    private SegmentVisGS groundStationDownlink;

    public Segment(
            String segmentId,
            Polygon polygon,
            long startOrbit,
            long endOrbit,
            Status status, 
            Calendar startOfAcquisition, 
            Calendar endOfAcquisition, 
            double cloudCoverSuccessRate,
            SegmentVisGS groundStationDownlink) {
        
        this.segmentId = segmentId;
        this.polygon = polygon;
        this.startOrbit = startOrbit;
        this.endOrbit = endOrbit;
        this.status = status;
        this.startOfAcquisition = startOfAcquisition;
        this.endOfAcquisition = endOfAcquisition;
        this.cloudCoverSuccessRate = cloudCoverSuccessRate;
        this.groundStationDownlink = groundStationDownlink;
    }

    public Segment(
            String segmentId, 
            Polygon polygon, 
            long startOrbit, 
            long endOrbit, 
            Status status, 
            Calendar startOfAcquisition, 
            Calendar endOfAcquisition, 
            double cloudCoverSuccessRate, 
            double estimatedCost, 
            String instrumentMode, 
            SegmentVisGS groundStationDownlink) {
        this.segmentId = segmentId;
        this.status = status;
        this.polygon = polygon;
        this.startOrbit = startOrbit;
        this.endOrbit = endOrbit;
        this.startOfAcquisition = startOfAcquisition;
        this.endOfAcquisition = endOfAcquisition;
        this.cloudCoverSuccessRate = cloudCoverSuccessRate;
        this.estimatedCost = estimatedCost;
        this.instrumentMode = instrumentMode;
        this.groundStationDownlink = groundStationDownlink;
    }

    
    
    
    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public void setInstrumentMode(String instrumentMode) {
        this.instrumentMode = instrumentMode;
    }

    public String getInstrumentMode() {
        return instrumentMode;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
    
    public long getStartOrbit() {
        return startOrbit;
    }

    public void setStartOrbit(long startOrbit) {
        this.startOrbit = startOrbit;
    }
    
    public long getEndOrbit() {
        return endOrbit;
    }

    public void setEndOrbit(long endOrbit) {
        this.endOrbit = endOrbit;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Calendar getStartOfAcquisition() {
        return startOfAcquisition;
    }

    public void setStartOfAcquisition(Calendar startOfAcquisition) {
        this.startOfAcquisition = startOfAcquisition;
    }

    public Calendar getEndOfAcquisition() {
        return endOfAcquisition;
    }

    public void setEndOfAcquisition(Calendar endOfAcquisition) {
        this.endOfAcquisition = endOfAcquisition;
    }

    public double getCloudCoverSuccessRate() {
        return cloudCoverSuccessRate;
    }

    public void setCloudCoverSuccessRate(double cloudCoverSuccessRate) {
        this.cloudCoverSuccessRate = cloudCoverSuccessRate;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public SegmentVisGS getGroundStationDownlink() {
        return groundStationDownlink;
    }

    public void setGroundStationDownlink(SegmentVisGS groundStationDownlink) {
        this.groundStationDownlink = groundStationDownlink;
    }
    
    public String toCSV() {
        return "";
    }

    @Override
    public String toString() {
        return "Segment : "+segmentId + ""
                + "\n - Polygon : " + this.polygon.printCoordinatesGML()
                + "\n - Status : " + this.status
                + "\n - Acquisition : "
                + "\n   - Start : " + this.startOfAcquisition.getTime() + " (" + this.startOrbit + ")"
                + "\n   - End : " + this.endOfAcquisition.getTime() + " (" + this.endOrbit + ")"
                + "\n - Downlink : " + this.groundStationDownlink
                + "\n - Cloud Coverage : " + this.cloudCoverSuccessRate
                + "\n - Cost : " + this.estimatedCost
                + "";
    }
}
