/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Sensor.java
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
package net.eads.astrium.hmas.util.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author re-sulrich
 */
public class Sensor {
    
    private String sensorId;
    private String sensorName;
    private String sensorDescription;
    private String sensorType;
    private String bandType;
    private String minLatitude;
    private String maxLatitude;
    private String minLongitude;
    private String maxLongitude;
    private String instrumentMass;
    private String maxPowerConsumption;
    private String acquisitionMethod;
    private List<String> applications;
    private Map<String,String> instrumentModes;
    private Map<String,String> polarizationModes;
    private String satelliteId;

    public Sensor() {
        
    }
    
    public Sensor(String sensorId, String sensorName, String sensorDescription, 
            String sensorType, String bandType, 
            String minLatitude, String maxLatitude, String minLongitude, String maxLongitude, 
            String instrumentMass, String maxPowerConsumption,
            String acquisitionMethod, List<String> applications,
            String satelliteId) {
        
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.sensorDescription = sensorDescription;
        this.sensorType = sensorType;
        this.bandType = bandType;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.instrumentMass = instrumentMass;
        this.maxPowerConsumption = maxPowerConsumption;
        this.acquisitionMethod = acquisitionMethod;
        this.applications = applications;
        
        this.satelliteId = satelliteId;
    }

    /**
     * Getters and setters
     */
    
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorDescription() {
        return sensorDescription;
    }

    public void setSensorDescription(String sensorDescription) {
        this.sensorDescription = sensorDescription;
    }
    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getBandType() {
        return bandType;
    }

    public void setBandType(String bandType) {
        this.bandType = bandType;
    }

    public String getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(String minLatitude) {
        this.minLatitude = minLatitude;
    }

    public String getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(String maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public String getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(String minLongitude) {
        this.minLongitude = minLongitude;
    }

    public String getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(String maxLongitude) {
        this.maxLongitude = maxLongitude;
    }
    public String getInstrumentMass() {
        return instrumentMass;
    }

    public void setInstrumentMass(String instrumentMass) {
        this.instrumentMass = instrumentMass;
    }

    public String getMaxPowerConsumption() {
        return maxPowerConsumption;
    }

    public void setMaxPowerConsumption(String maxPowerConsumption) {
        this.maxPowerConsumption = maxPowerConsumption;
    }

    public String getAcquisitionMethod() {
        return acquisitionMethod;
    }

    public void setAcquisitionMethod(String acquisitionMethod) {
        this.acquisitionMethod = acquisitionMethod;
    }

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    public void addApplication(String application)
    {
        if (applications == null)
            applications = new ArrayList<String>();
        
        applications.add(application);
        
    }

    public Map<String,String> getInstrumentModes() {
        return this.instrumentModes;
    }
    
    public void setInstrumentModes(Map<String,String> instrumentModes) {
        this.instrumentModes = instrumentModes;
    }
    
    public void addInstrumentMode(String instrumentModeId, String instrumentModeName)
    {
        if (instrumentModes == null)
            instrumentModes = new HashMap<String,String>();
        
        instrumentModes.put(instrumentModeId, instrumentModeName);
        
    }

    public Map<String,String> getPolarizationModes() {
        return polarizationModes;
    }

    public void setPolarizationModes(Map<String,String> polarizationModes) {
        this.polarizationModes = polarizationModes;
    }
    
    public void addPolarizationMode(String polarizationModeId, String polarizationModeName) {
        
        if (polarizationModes == null)
            polarizationModes = new HashMap<String,String>();
        
        polarizationModes.put(polarizationModeId, polarizationModeName);
        
    }

    
    public void addPolarizationModes(Map<String, String> polarisationModes) {
        
        if (polarizationModes == null)
            polarizationModes = new HashMap<String,String>();
        
        for (String polMode : polarisationModes.keySet()) {
            if (!polarizationModes.containsKey(polMode))
            {
                polarizationModes.put(polMode, polarisationModes.get(polMode));
            }
        }
    }

    public String getSatellite() {
        return satelliteId;
    }

    public void setSatellite(String satellite) {
        this.satelliteId = satellite;
    }

    
    /**
     * END Getters and setters
     */
    @Override
    public String toString() {
        return "Sensor{" + "\n"
                + "sensorId=" + sensorId + ",\n"
                + " sensorName=" + sensorName + ",\n"
                + " sensorDescription=" + sensorDescription + ",\n"
                + " sensorType=" + sensorType + ",\n"
                + " bandType=" + bandType + ",\n"
                + " minLatitude=" + minLatitude + ",\n"
                + " maxLatitude=" + maxLatitude + ",\n"
                + " minLongitude=" + minLongitude + ",\n"
                + " maxLongitude=" + maxLongitude + ",\n"
                + " instrumentMass=" + instrumentMass + ",\n"
                + " instrumentMass=" + maxPowerConsumption + ",\n"
                + " acquisitionMethod=" + acquisitionMethod + ",\n"
                + " applications=" + applications + ",\n"
                + " instrumentModes=" + instrumentModes + ",\n"
                + " polarizationModes=" + polarizationModes + ",\n"
                + '}';
    }

    
}
