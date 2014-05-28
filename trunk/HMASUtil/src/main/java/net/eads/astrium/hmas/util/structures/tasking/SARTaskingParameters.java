/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SARTaskingParameters.java
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
public class SARTaskingParameters extends TaskingParameters {
    
    private double maxNoiseLevel;
    private double maxAmbiguityLevel;
    private boolean fusionAccepted;
    private List<String> polarisationModes;

    public SARTaskingParameters(double maxNoiseLevel, double maxAmbiguityLevel, boolean fusionAccepted, List<String> polarisationModes, double minIncidenceAngleAzimuth, double maxIncidenceAngleAzimuth, double minIncidenceAngleElevation, double maxIncidenceAngleElevation, double minPointingAngleAcross, double maxPointingAngleAcross, double minPointingAngleAlong, double maxPointingAngleAlong, Geometry coordinates, String roiType, List<TimePeriod> times, int minGroundResolution, int maxGroundResolution, List<String> instrumentModes) {
        super(minIncidenceAngleAzimuth, maxIncidenceAngleAzimuth, minIncidenceAngleElevation, maxIncidenceAngleElevation, minPointingAngleAcross, maxPointingAngleAcross, minPointingAngleAlong, maxPointingAngleAlong, coordinates, roiType, times, minGroundResolution, maxGroundResolution, instrumentModes);
        this.maxNoiseLevel = maxNoiseLevel;
        this.maxAmbiguityLevel = maxAmbiguityLevel;
        this.fusionAccepted = fusionAccepted;
        this.polarisationModes = polarisationModes;
    }

    

 

    
    public double getMaxNoiseLevel() {
        return maxNoiseLevel;
    }

    public void setMaxNoiseLevel(double maxNoiseLevel) {
        this.maxNoiseLevel = maxNoiseLevel;
    }

    public double getMaxAmbiguityLevel() {
        return maxAmbiguityLevel;
    }

    public void setMaxAmbiguityLevel(double maxAmbiguityLevel) {
        this.maxAmbiguityLevel = maxAmbiguityLevel;
    }

    public boolean getFusionAccepted() {
        return fusionAccepted;
    }

    public void setFusionAccepted(boolean fusionAccepted) {
        this.fusionAccepted = fusionAccepted;
    }

    public List<String> getPolarisationModes() {
        return polarisationModes;
    }

    public void setPolarisationModes(List<String> polarisationModes) {
        this.polarisationModes = polarisationModes;
    }

    @Override
    public String toString() {
        String res = super.toString() +
        "\nmaxNoiseLevel: " + maxNoiseLevel +
        "\nmaxAmbiguityLevel: " + maxAmbiguityLevel +
        "\nfusionAccepted: " + fusionAccepted +
        "\npolarisationModes: " + polarisationModes;
        
        
        return res;
    }
    
    
}
