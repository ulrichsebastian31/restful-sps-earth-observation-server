/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OPTTaskingParameters.java
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

import net.eads.astrium.hmas.util.structures.tasking.geometries.Geometry;
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.util.structures.TimePeriod;

/**
 *
 * @author re-sulrich
 */
public class OPTTaskingParameters extends TaskingParameters {
    
           
    private String meteoServerId;
    
    private double maxCloudCover;
    private double maxSnowCover;
    private double maxSunGlint;
    private boolean hazeAccepted;
    private boolean sandWindAccepted;
    private double minLuminosity;
    private boolean fusionAccepted;

    public OPTTaskingParameters(double maxCloudCover, double maxSnowCover, double maxSunGlint, boolean hazeAccepted, boolean sandWindAccepted, double minLuminosity, boolean fusionAccepted, double minIncidenceAngleAzimuth, double maxIncidenceAngleAzimuth, double minIncidenceAngleElevation, double maxIncidenceAngleElevation, double minPointingAngleAcross, double maxPointingAngleAcross, double minPointingAngleAlong, double maxPointingAngleAlong, Geometry coordinates, String roiType, List<TimePeriod> times, int minGroundResolution, int maxGroundResolution, List<String> instrumentModes) {
        super(minIncidenceAngleAzimuth, maxIncidenceAngleAzimuth, minIncidenceAngleElevation, maxIncidenceAngleElevation, minPointingAngleAcross, maxPointingAngleAcross, minPointingAngleAlong, maxPointingAngleAlong, coordinates, roiType, times, minGroundResolution, maxGroundResolution, instrumentModes);
        this.maxCloudCover = maxCloudCover;
        this.maxSnowCover = maxSnowCover;
        this.maxSunGlint = maxSunGlint;
        this.hazeAccepted = hazeAccepted;
        this.sandWindAccepted = sandWindAccepted;
        this.minLuminosity = minLuminosity;
        this.fusionAccepted = fusionAccepted;
    }

    public double getMaxCloudCover() {
        return maxCloudCover;
    }

    public void setMaxCloudCover(double maxCloudCover) {
        this.maxCloudCover = maxCloudCover;
    }

    public double getMaxSnowCover() {
        return maxSnowCover;
    }

    public void setMaxSnowCover(double maxSnowCover) {
        this.maxSnowCover = maxSnowCover;
    }

    public double getMaxSunGlint() {
        return maxSunGlint;
    }

    public void setMaxSunGlint(double maxSunGlint) {
        this.maxSunGlint = maxSunGlint;
    }

    public boolean getHazeAccepted() {
        return hazeAccepted;
    }

    public void setHazeAccepted(boolean hazeAccepted) {
        this.hazeAccepted = hazeAccepted;
    }

    public boolean getSandWindAccepted() {
        return sandWindAccepted;
    }

    public void setSandWindAccepted(boolean sandWindAccepted) {
        this.sandWindAccepted = sandWindAccepted;
    }

    public double getMinLuminosity() {
        return minLuminosity;
    }

    public void setMinLuminosity(double minLuminosity) {
        this.minLuminosity = minLuminosity;
    }

    public boolean getFusionAccepted() {
        return fusionAccepted;
    }

    public void setFusionAccepted(boolean fusionAccepted) {
        this.fusionAccepted = fusionAccepted;
    }

    public String getMeteoServerId() {
        return meteoServerId;
    }

    public void setMeteoServerId(String meteoServerId) {
        this.meteoServerId = meteoServerId;
    }
    
    @Override
    public String toString() {
        String res = super.toString() +
        ", maxCloudCover=" + maxCloudCover +
        ", maxSnowCover=" + maxSnowCover +
        ", maxSunGlint=" + maxSunGlint +
        ", hazeAccepted=" + hazeAccepted +
        ", sandWindAccepted=" + sandWindAccepted +
        ", minLuminosity=" + minLuminosity +
        ", fusionAccepted=" + fusionAccepted;
        
        if (meteoServerId != null) 
            res += ", meteoServerId=" + meteoServerId;
        
        return res;
    }
}
