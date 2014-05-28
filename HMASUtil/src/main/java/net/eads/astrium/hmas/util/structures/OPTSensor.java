/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OPTSensor.java
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
 * This class permits to load sensor 
 * @author re-sulrich
 */
public class OPTSensor extends Sensor {

    public class OPTSensorMode {

        private String id;
        private String name;
        private String description;
        //Power characteristics
        private String maxPowerConsumption;
        //Geometrical characteristics
        private String acrossTrackResolution;
        private String alongTrackResolution;
        private String numberOfSamples;
        //Measurement characteristics
        private String bandType;
        private String minSpectralRange;
        private String maxSpectralRange;
        private String snrRatio;
        private String noiseEquivalentRadiance;

        public OPTSensorMode() {
            
        }
        
        public OPTSensorMode(String id, String name, String description
                ) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
        
        public void setIdentification(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
        
        public void setCharacteristics(String maxPowerConsumption, 
                String acrossTrackResolution, 
                String alongTrackResolution, 
                String numberOfSamples, 
                String bandType, 
                String minSpectralRange, 
                String maxSpectralRange, 
                String snrRatio, 
                String noiseEquivalentRadiance) {
            
            this.maxPowerConsumption = maxPowerConsumption;
            this.acrossTrackResolution = acrossTrackResolution;
            this.alongTrackResolution = alongTrackResolution;
            this.numberOfSamples = numberOfSamples;
            this.bandType = bandType;
            this.minSpectralRange = minSpectralRange;
            this.maxSpectralRange = maxSpectralRange;
            this.snrRatio = snrRatio;
            this.noiseEquivalentRadiance = noiseEquivalentRadiance;
        }
        
        
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMaxPowerConsumption() {
            return maxPowerConsumption;
        }

        public void setMaxPowerConsumption(String maxPowerConsumption) {
            this.maxPowerConsumption = maxPowerConsumption;
        }

        public String getAcrossTrackResolution() {
            return acrossTrackResolution;
        }

        public void setAcrossTrackResolution(String acrossTrackResolution) {
            this.acrossTrackResolution = acrossTrackResolution;
        }

        public String getAlongTrackResolution() {
            return alongTrackResolution;
        }

        public void setAlongTrackResolution(String alongTrackResolution) {
            this.alongTrackResolution = alongTrackResolution;
        }

        public String getNumberOfSamples() {
            return numberOfSamples;
        }

        public void setNumberOfSamples(String numberOfSamples) {
            this.numberOfSamples = numberOfSamples;
        }

        public String getBandType() {
            return bandType;
        }

        public void setBandType(String bandType) {
            this.bandType = bandType;
        }

        public String getMinSpectralRange() {
            return minSpectralRange;
        }

        public void setMinSpectralRange(String minSpectralRange) {
            this.minSpectralRange = minSpectralRange;
        }

        public String getMaxSpectralRange() {
            return maxSpectralRange;
        }

        public void setMaxSpectralRange(String maxSpectralRange) {
            this.maxSpectralRange = maxSpectralRange;
        }

        public String getSnrRatio() {
            return snrRatio;
        }

        public void setSnrRatio(String snrRatio) {
            this.snrRatio = snrRatio;
        }

        public String getNoiseEquivalentRadiance() {
            return noiseEquivalentRadiance;
        }

        public void setNoiseEquivalentRadiance(String noiseEquivalentRadiance) {
            this.noiseEquivalentRadiance = noiseEquivalentRadiance;
        }
        
    }
    
    public OPTSensor() {
        
    }
    
    public OPTSensor(
            String sensorId, String sensorName, String sensorDescription, 
            String sensorType, String bandType, 
            String minLatitude, String maxLatitude, String minLongitude, String maxLongitude, 
            String instrumentMass, String maxPowerConsumption,
            String acquisitionMethod, List<String> applications,
            String satelliteId
            ) {
        
        super(sensorId, sensorName, sensorDescription, 
                sensorType, bandType, 
                minLatitude, maxLatitude, minLongitude, maxLongitude, 
                instrumentMass, maxPowerConsumption, 
                acquisitionMethod, applications, satelliteId);
        
    }

    public void setCharacteristics(
            String acrossTrackFOV, 
            String minAcrossTrackAngle, String maxAcrossTrackAngle, 
            String minAlongTrackAngle, String maxAlongTrackAngle, 
            String swathWidth, String groundLocationAccuracy, 
            String revisitTimeInDays, String numberOfBands) {
        
        this.acrossTrackFOV = acrossTrackFOV;
        this.minAcrossTrackAngle = minAcrossTrackAngle;
        this.maxAcrossTrackAngle = maxAcrossTrackAngle;
        this.minAlongTrackAngle = minAlongTrackAngle;
        this.maxAlongTrackAngle = maxAlongTrackAngle;
        this.swathWidth = swathWidth;
        this.groundLocationAccuracy = groundLocationAccuracy;
        this.revisitTimeInDays = revisitTimeInDays;
        this.numberOfBands = numberOfBands;
    }
    
    //OPT specific
    private String acrossTrackFOV;
    private String minAcrossTrackAngle;
    private String maxAcrossTrackAngle;
    private String minAlongTrackAngle;
    private String maxAlongTrackAngle;
    private String swathWidth;
    private String groundLocationAccuracy;
    private String revisitTimeInDays;
    private String numberOfBands;

    private Map<String,OPTSensorMode> instrumentModesDescriptions;

    /**
     * @Section gettersSetters
     */
    
    
    public String getAcrossTrackFOV() {
        return acrossTrackFOV;
    }

    public void setAcrossTrackFOV(String acrossTrackFOV) {
        this.acrossTrackFOV = acrossTrackFOV;
    }

    public String getMinAcrossTrackAngle() {
        return minAcrossTrackAngle;
    }

    public void setMinAcrossTrackAngle(String minAcrossTrackAngle) {
        this.minAcrossTrackAngle = minAcrossTrackAngle;
    }

    public String getMaxAcrossTrackAngle() {
        return maxAcrossTrackAngle;
    }

    public void setMaxAcrossTrackAngle(String maxAcrossTrackAngle) {
        this.maxAcrossTrackAngle = maxAcrossTrackAngle;
    }

    public String getMinAlongTrackAngle() {
        return minAlongTrackAngle;
    }

    public void setMinAlongTrackAngle(String minAlongTrackAngle) {
        this.minAlongTrackAngle = minAlongTrackAngle;
    }

    public String getMaxAlongTrackAngle() {
        return maxAlongTrackAngle;
    }

    public void setMaxAlongTrackAngle(String maxAlongTrackAngle) {
        this.maxAlongTrackAngle = maxAlongTrackAngle;
    }

    public String getSwathWidth() {
        return swathWidth;
    }

    public void setSwathWidth(String swathWidth) {
        this.swathWidth = swathWidth;
    }

    public String getGroundLocationAccuracy() {
        return groundLocationAccuracy;
    }

    public void setGroundLocationAccuracy(String groundLocationAccuracy) {
        this.groundLocationAccuracy = groundLocationAccuracy;
    }

    public String getRevisitTimeInDays() {
        return revisitTimeInDays;
    }

    public void setRevisitTimeInDays(String revisitTimeInDays) {
        this.revisitTimeInDays = revisitTimeInDays;
    }

    public String getNumberOfBands() {
        return numberOfBands;
    }

    public void setNumberOfBands(String numberOfBands) {
        this.numberOfBands = numberOfBands;
    }
    
    
    public Map<String,OPTSensorMode> getInstrumentModesDescriptions() {
        return instrumentModesDescriptions;
    }

    public void setInstrumentModesDescriptions(Map<String,OPTSensorMode> instrumentModesDescriptions) {
        this.instrumentModesDescriptions = instrumentModesDescriptions;
    }
    
    public OPTSensorMode addNewInstrumentModeDescription(String modeId) {
        if (instrumentModesDescriptions == null)
            instrumentModesDescriptions = new HashMap<String,OPTSensorMode>();
        
        OPTSensorMode instrumentModeDescription = new OPTSensorMode();
        
        instrumentModesDescriptions.put(modeId, instrumentModeDescription);
        
        return instrumentModeDescription;
    }
    
    public void addInstrumentMode(OPTSensorMode instrumentModeDescription) {
        if (instrumentModesDescriptions == null)
            instrumentModesDescriptions = new HashMap<String,OPTSensorMode>();
        
        instrumentModesDescriptions.put(instrumentModeDescription.getId(), instrumentModeDescription);
        
    }
    
    /**
     * @EndSection gettersSetters
     */
    @Override
    public String toString() {
        return "OPTSensor{"
                + "sensorId=" + getSensorId() + ",\n"
                + " sensorName=" + getSensorName() + ",\n"
                + " sensorDescription=" + getSensorDescription() + ",\n"
                + " sensorType=" + getSensorType() + ",\n"
                + " bandType=" + getBandType() + ",\n"
                + " minLatitude=" + getMinLatitude() + ",\n"
                + " maxLatitude=" + getMaxLatitude() + ",\n"
                + " minLongitude=" + getMinLongitude() + ",\n"
                + " maxLongitude=" + getMaxLongitude() + ",\n"
                + "instrumentMass=" + getInstrumentMass() + ",\n"
                + "acquisitionMethod=" + getAcquisitionMethod() + ",\n"
                + "applications=" + getApplications() + ",\n"
                + "acrossTrackFOV=" + acrossTrackFOV + ",\n"
                + "minAcrossTrackAngle=" + minAcrossTrackAngle + ",\n"
                + "maxAcrossTrackAngle=" + maxAcrossTrackAngle + ",\n"
                + "minAlongTrackAngle=" + minAlongTrackAngle + ",\n"
                + "maxAlongTrackAngle=" + maxAlongTrackAngle + ",\n"
                + "swathWidth=" + swathWidth + ",\n"
                + "groundLocationAccuracy=" + groundLocationAccuracy + ",\n"
                + "revisitTimeInDays=" + revisitTimeInDays + ",\n"
                + "numberOfBands=" + numberOfBands + ",\n"
                + "instrumentModesDescriptions=" + instrumentModesDescriptions + '}';
    }


    
}
