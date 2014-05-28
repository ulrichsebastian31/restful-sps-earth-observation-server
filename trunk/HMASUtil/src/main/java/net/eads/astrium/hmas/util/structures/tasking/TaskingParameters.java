/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TaskingParameters.java
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

import java.util.List;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Geometry;

/**
 *
 * @author re-sulrich
 */
public class TaskingParameters {

    private double minIncidenceAngleAzimuth;
    private double maxIncidenceAngleAzimuth;
    private double minIncidenceAngleElevation;
    private double maxIncidenceAngleElevation;
    private double minPointingAngleAcross;
    private double maxPointingAngleAcross;
    private double minPointingAngleAlong;
    private double maxPointingAngleAlong;
    private Geometry coordinates;
    private String roiType;
    private List<TimePeriod> times;
    private int minGroundResolution;
    private int maxGroundResolution;
    private List<String> instrumentModes;

    public TaskingParameters(
            double minIncidenceAngleAzimuth, double maxIncidenceAngleAzimuth, 
            double minIncidenceAngleElevation, double maxIncidenceAngleElevation, 
            double minPointingAngleAcross, double maxPointingAngleAcross, 
            double minPointingAngleAlong, double maxPointingAngleAlong, 
            Geometry coordinates, String roiType, List<TimePeriod> times, int minGroundResolution, int maxGroundResolution, List<String> instrumentModes) {
        this.minIncidenceAngleAzimuth = minIncidenceAngleAzimuth;
        this.maxIncidenceAngleAzimuth = maxIncidenceAngleAzimuth;
        this.minIncidenceAngleElevation = minIncidenceAngleElevation;
        this.maxIncidenceAngleElevation = maxIncidenceAngleElevation;
        this.minPointingAngleAcross = minPointingAngleAcross;
        this.maxPointingAngleAcross = maxPointingAngleAcross;
        this.minPointingAngleAlong = minPointingAngleAlong;
        this.maxPointingAngleAlong = maxPointingAngleAlong;
        this.coordinates = coordinates;
        this.roiType = roiType;
        this.times = times;
        this.minGroundResolution = minGroundResolution;
        this.maxGroundResolution = maxGroundResolution;
        this.instrumentModes = instrumentModes;
    }

    public double getMinIncidenceAngleAzimuth() {
        return minIncidenceAngleAzimuth;
    }

    public void setMinIncidenceAngleAzimuth(double minIncidenceAngleAzimuth) {
        this.minIncidenceAngleAzimuth = minIncidenceAngleAzimuth;
    }

    public double getMaxIncidenceAngleAzimuth() {
        return maxIncidenceAngleAzimuth;
    }

    public void setMaxIncidenceAngleAzimuth(double maxIncidenceAngleAzimuth) {
        this.maxIncidenceAngleAzimuth = maxIncidenceAngleAzimuth;
    }

    public double getMinIncidenceAngleElevation() {
        return minIncidenceAngleElevation;
    }

    public void setMinIncidenceAngleElevation(double minIncidenceAngleElevation) {
        this.minIncidenceAngleElevation = minIncidenceAngleElevation;
    }

    public double getMaxIncidenceAngleElevation() {
        return maxIncidenceAngleElevation;
    }

    public void setMaxIncidenceAngleElevation(double maxIncidenceAngleElevation) {
        this.maxIncidenceAngleElevation = maxIncidenceAngleElevation;
    }

    public double getMinPointingAngleAcross() {
        return minPointingAngleAcross;
    }

    public void setMinPointingAngleAcross(double minPointingAngleAcross) {
        this.minPointingAngleAcross = minPointingAngleAcross;
    }

    public double getMaxPointingAngleAcross() {
        return maxPointingAngleAcross;
    }

    public void setMaxPointingAngleAcross(double maxPointingAngleAcross) {
        this.maxPointingAngleAcross = maxPointingAngleAcross;
    }

    public double getMinPointingAngleAlong() {
        return minPointingAngleAlong;
    }

    public void setMinPointingAngleAlong(double minPointingAngleAlong) {
        this.minPointingAngleAlong = minPointingAngleAlong;
    }

    public double getMaxPointingAngleAlong() {
        return maxPointingAngleAlong;
    }

    public void setMaxPointingAngleAlong(double maxPointingAngleAlong) {
        this.maxPointingAngleAlong = maxPointingAngleAlong;
    }


    public Geometry getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Geometry coordinates) {
        this.coordinates = coordinates;
    }

    public String getRoiType() {
        return roiType;
    }

    public void setRoiType(String roiType) {
        this.roiType = roiType;
    }

    public List<TimePeriod> getTimes() {
        return times;
    }

    public void setTimes(List<TimePeriod> times) {
        this.times = times;
    }

    public void addTime(TimePeriod times) {
        this.times.add(times);
    }

    public int getMinGroundResolution() {
        return minGroundResolution;
    }

    public void setMinGroundResolution(int minGroundResolution) {
        this.minGroundResolution = minGroundResolution;
    }

    public int getMaxGroundResolution() {
        return maxGroundResolution;
    }

    public void setMaxGroundResolution(int maxGroundResolution) {
        this.maxGroundResolution = maxGroundResolution;
    }

    
    public List<String> getInstrumentModes() {
        return instrumentModes;
    }

    public void setInstrumentModes(List<String> instrumentModes) {
        this.instrumentModes = instrumentModes;
    }

    @Override
    public String toString() {
        String res = "Tasking parameters" +
        "\nminIncidenceAngleAzimuth : " + this.minIncidenceAngleAzimuth + 
        "\nmaxIncidenceAngleAzimuth : " + this.maxIncidenceAngleAzimuth + 
        "\nminIncidenceAngleElevation : " + this.minIncidenceAngleElevation + 
        "\nmaxIncidenceAngleElevation : " + this.maxIncidenceAngleElevation + 
        "\nminPointingAngleAcross : " + this.minPointingAngleAcross + 
        "\nmaxPointingAngleAcross : " + this.maxPointingAngleAcross + 
        "\nminPointingAngleAlong : " + this.minPointingAngleAlong + 
        "\nmaxPointingAngleAlong : " + this.maxPointingAngleAlong + 
        "\ncoordinates : " + this.coordinates + 
        "\nroiType : " + this.roiType + 
        "\ntimes : " + this.times + 
        "\nminGroundResolution : " + this.minGroundResolution + 
        "\nmaxGroundResolution : " + this.maxGroundResolution + 
        "\ninstrumentModes : " + this.instrumentModes;
        
        
        
        
        
        return res;
        
    }
}
