/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SegmentVisGS.java
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

import java.util.Calendar;

/**
 *
 * @author re-dboatswain
 */
public class SegmentVisGS {
    
    private String groundStationId;
    private String groundStationName;
    
    private Calendar startOfVisibility;
    private Calendar endOfVisibility;
    
    public SegmentVisGS(
            String groundStationId, 
            String groundStationName,
            Calendar startOfVisibility, 
            Calendar endOfVisibility 
            ) {
        
        this.groundStationId = groundStationId;
        this.groundStationName = groundStationName;
        this.startOfVisibility = startOfVisibility;
        this.endOfVisibility = endOfVisibility;
    }
    
    public String getGroundStationId() {
        return groundStationId;
    }

    public void setGroundStationId(String groundStationId) {
        this.groundStationId = groundStationId;
    }

    public String getGroundStationName() {
        return groundStationName;
    }

    public void setGroundStationName(String groundStationName) {
        this.groundStationName = groundStationName;
    }

    public Calendar getStartOfVisibility() {
        return startOfVisibility;
    }

    public void setStartOfVisibility(Calendar startOfVisibility) {
        this.startOfVisibility = startOfVisibility;
    }

    public Calendar getEndOfVisibility() {
        return endOfVisibility;
    }

    public void setEndOfVisibility(Calendar endOfVisibility) {
        this.endOfVisibility = endOfVisibility;
    }
    
    @Override
    public String toString() {
        return "Downlink window : "
                + "\n - Ground Station : " + this.groundStationId + " (" + this.groundStationName + ")"
                + "\n - Start : " + this.startOfVisibility.getTime()
                + "\n - End : " + this.endOfVisibility.getTime();
    }
    
}
