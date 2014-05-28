/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SARSensor.java
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class permits to load sensor 
 * @author re-sulrich
 */
public class SARSensor extends Sensor {

    public class SARSensorMode {
        
        private String id;
        private String name;
        private String description;
        //Power characteristics
        private String maxPowerConsumption;
        //Geometric characteristics
        private String minAcrossTrackAngle;
        private String maxAcrossTrackAngle;
        private String minAlongTrackAngle;
        private String maxAlongTrackAngle;
        private String swathWidth;
        private String acrossTrackResolution;
        private String alongTrackResolution;
        //Measurement characteristics
        private String radiometricResolution;
        private String minRadioStab;
        private String maxRadioStab;
        private String minAlongTrackAmbRatio;
        private String maxAlongTrackAmbRatio;
        private String minAcrossTrackAmbRatio;
        private String maxAcrossTrackAmbRatio;
        private String minNoiseEquivalentSO;
        private String maxNoiseEquivalentSO;
        
        private Map<String, String> polarizationModes;

        public SARSensorMode() {
            
        }
        
        public SARSensorMode(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
        
        public void setIdentification(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
        
        public void setCharacteristics(
                String maxPowerConsumption, 
                String minAcrossTrackAngle,
                String maxAcrossTrackAngle, 
                String minAlongTrackAngle, 
                String maxAlongTrackAngle, 
                String swathWidth,
                String acrossTrackResolution, 
                String alongTrackResolution, 
                String radiometricResolution, 
                String minRadioStab, 
                String maxRadioStab, 
                String minAlongTrackAmbRatio, 
                String maxAlongTrackAmbRatio, 
                String minAcrossTrackAmbRatio, 
                String maxAcrossTrackAmbRatio, 
                String minNoiseEquivalentSO, 
                String maxNoiseEquivalentSO) {
            
            this.maxPowerConsumption = maxPowerConsumption;
            this.minAcrossTrackAngle = minAcrossTrackAngle;
            this.maxAcrossTrackAngle = maxAcrossTrackAngle;
            this.minAlongTrackAngle = minAlongTrackAngle;
            this.maxAlongTrackAngle = maxAlongTrackAngle;
            this.swathWidth = swathWidth;
            this.acrossTrackResolution = acrossTrackResolution;
            this.alongTrackResolution = alongTrackResolution;
            this.radiometricResolution = radiometricResolution;
            this.minRadioStab = minRadioStab;
            this.maxRadioStab = maxRadioStab;
            this.minAlongTrackAmbRatio = minAlongTrackAmbRatio;
            this.maxAlongTrackAmbRatio = maxAlongTrackAmbRatio;
            this.minAcrossTrackAmbRatio = minAcrossTrackAmbRatio;
            this.maxAcrossTrackAmbRatio = maxAcrossTrackAmbRatio;
            this.minNoiseEquivalentSO = minNoiseEquivalentSO;
            this.maxNoiseEquivalentSO = maxNoiseEquivalentSO;
        }
        
        /**
         * @Section gettersSetters
         */

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

        public String getRadiometricResolution() {
            return radiometricResolution;
        }

        public void setRadiometricResolution(String radiometricResolution) {
            this.radiometricResolution = radiometricResolution;
        }

        public String getMinRadioStab() {
            return minRadioStab;
        }

        public void setMinRadioStab(String minRadioStab) {
            this.minRadioStab = minRadioStab;
        }

        public String getMaxRadioStab() {
            return maxRadioStab;
        }

        public void setMaxRadioStab(String maxRadioStab) {
            this.maxRadioStab = maxRadioStab;
        }

        public String getMinAlongTrackAmbRatio() {
            return minAlongTrackAmbRatio;
        }

        public void setMinAlongTrackAmbRatio(String minAlongTrackAmbRatio) {
            this.minAlongTrackAmbRatio = minAlongTrackAmbRatio;
        }

        public String getMaxAlongTrackAmbRatio() {
            return maxAlongTrackAmbRatio;
        }

        public void setMaxAlongTrackAmbRatio(String maxAlongTrackAmbRatio) {
            this.maxAlongTrackAmbRatio = maxAlongTrackAmbRatio;
        }

        public String getMinAcrossTrackAmbRatio() {
            return minAcrossTrackAmbRatio;
        }

        public void setMinAcrossTrackAmbRatio(String minAcrossTrackAmbRatio) {
            this.minAcrossTrackAmbRatio = minAcrossTrackAmbRatio;
        }

        public String getMaxAcrossTrackAmbRatio() {
            return maxAcrossTrackAmbRatio;
        }

        public void setMaxAcrossTrackAmbRatio(String maxAcrossTrackAmbRatio) {
            this.maxAcrossTrackAmbRatio = maxAcrossTrackAmbRatio;
        }

        public String getMinNoiseEquivalentSO() {
            return minNoiseEquivalentSO;
        }

        public void setMinNoiseEquivalentSO(String minNoiseEquivalentSO) {
            this.minNoiseEquivalentSO = minNoiseEquivalentSO;
        }

        public String getMaxNoiseEquivalentSO() {
            return maxNoiseEquivalentSO;
        }

        public void setMaxNoiseEquivalentSO(String maxNoiseEquivalentSO) {
            this.maxNoiseEquivalentSO = maxNoiseEquivalentSO;
        }

        public Map<String, String> getPolarizationModes() {
            return polarizationModes;
        }

        public void setPolarizationModes(Map<String, String> polarizationModes) {
            this.polarizationModes = polarizationModes;
        }

        /**
         * @EndSection gettersSetters
         */
        
        @Override
        public String toString() {
            return  ",\nSARSensorMode{" + ",\n"
                    + "id=" + id + ",\n"
                    + "name=" + name + ",\n"
                    + "description=" + description + ",\n"
                    + "maxPowerConsumption=" + maxPowerConsumption + ",\n"
                    + "minAcrossTrackAngle=" + minAcrossTrackAngle + ",\n"
                    + "maxAcrossTrackAngle=" + maxAcrossTrackAngle + ",\n"
                    + "minAlongTrackAngle=" + minAlongTrackAngle + ",\n"
                    + "maxAlongTrackAngle=" + maxAlongTrackAngle + ",\n"
                    + "swathWidth=" + swathWidth + ",\n"
                    + "acrossTrackResolution=" + acrossTrackResolution + ",\n"
                    + "alongTrackResolution=" + alongTrackResolution + ",\n"
                    + "radiometricResolution=" + radiometricResolution + ",\n"
                    + "minRadioStab=" + minRadioStab + ",\n"
                    + "maxRadioStab=" + maxRadioStab + ",\n"
                    + "minAlongTrackAmbRatio=" + minAlongTrackAmbRatio + ",\n"
                    + "maxAlongTrackAmbRatio=" + maxAlongTrackAmbRatio + ",\n"
                    + "minAcrossTrackAmbRatio=" + minAcrossTrackAmbRatio + ",\n"
                    + "maxAcrossTrackAmbRatio=" + maxAcrossTrackAmbRatio + ",\n"
                    + "minNoiseEquivalentSO=" + minNoiseEquivalentSO + ",\n"
                    + "maxNoiseEquivalentSO=" + maxNoiseEquivalentSO + ",\n"
                    + '}';
        }

        
    }
    
    public SARSensor() {
        
    }
    
    public SARSensor(
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

    public void setCharacteristics(String antennaLength, String antennaWidth, String groundLocationAccuracy, 
            String revisitTimeInDays, String transmitFrequencyBand, String transmitCenterFrequency) {
        
        this.antennaLength = antennaLength;
        this.antennaWidth = antennaWidth;
        this.groundLocationAccuracy = groundLocationAccuracy;
        this.revisitTimeInDays = revisitTimeInDays;
        this.transmitFrequencyBand = transmitFrequencyBand;
        this.transmitCenterFrequency = transmitCenterFrequency;
    }
    
    //SAR specific
    private String antennaLength;
    private String antennaWidth;
    private String groundLocationAccuracy;
    private String revisitTimeInDays;
    private String transmitFrequencyBand;
    private String transmitCenterFrequency;

    private Map<String,SARSensorMode> instrumentModesDescriptions;

        /**
         * @Section gettersSetters
         */

    public String getAntennaLength() {
        return antennaLength;
    }

    public void setAntennaLength(String antennaLength) {
        this.antennaLength = antennaLength;
    }

    public String getAntennaWidth() {
        return antennaWidth;
    }

    public void setAntennaWidth(String antennaWidth) {
        this.antennaWidth = antennaWidth;
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

    public String getTransmitFrequencyBand() {
        return transmitFrequencyBand;
    }

    public void setTransmitFrequencyBand(String transmitFrequencyBand) {
        this.transmitFrequencyBand = transmitFrequencyBand;
    }

    public String getTransmitCenterFrequency() {
        return transmitCenterFrequency;
    }

    public void setTransmitCenterFrequency(String transmitCenterFrequency) {
        this.transmitCenterFrequency = transmitCenterFrequency;
    }

    
    

    public Map<String,SARSensorMode> getInstrumentModesDescriptions() {
        return instrumentModesDescriptions;
    }

    public void setInstrumentModesDescriptions(Map<String,SARSensorMode> instrumentModesDescriptions) {
        this.instrumentModesDescriptions = instrumentModesDescriptions;
    }
    
    public SARSensorMode addNewInstrumentModeDescription(String modeId) {
        if (instrumentModesDescriptions == null)
            instrumentModesDescriptions = new HashMap<String,SARSensorMode>();
        
        SARSensorMode instrumentModeDescription = new SARSensorMode();
        
        instrumentModesDescriptions.put(modeId, instrumentModeDescription);
        
        return instrumentModeDescription;
    }
    
    public void addInstrumentMode(SARSensorMode instrumentModeDescription) {
        if (instrumentModesDescriptions == null)
            instrumentModesDescriptions = new HashMap<String,SARSensorMode>();
        
        instrumentModesDescriptions.put(instrumentModeDescription.getId(), instrumentModeDescription);
        
    }

    /**
     * @EndSection gettersSetters
     */
    
    @Override
    public String toString() {
        return "SARSensor{" + ",\n"
                + "sensorId=" + getSensorId() + ",\n"
                + " sensorName=" + getSensorName() + ",\n"
                + " sensorDescription=" + getSensorDescription() + ",\n"
                + " sensorType=" + getSensorType() + ",\n"
                + " bandType=" + getBandType() + ",\n"
                + " minLatitude=" + getMinLatitude() + ",\n"
                + " maxLatitude=" + getMaxLatitude() + ",\n"
                + " minLongitude=" + getMinLongitude() + ",\n"
                + " maxLongitude=" + getMaxLongitude() + ",\n"
                + "acquisitionMethod=" + getAcquisitionMethod() + ",\n"
                + "applications=" + getApplications() + ",\n"
                + "antennaLength=" + antennaLength + ",\n"
                + "antennaWidth=" + antennaWidth + ",\n"
                + "groundLocationAccuracy=" + groundLocationAccuracy + ",\n"
                + "revisitTimeInDays=" + revisitTimeInDays + ",\n"
                + "transmitFrequencyBand=" + transmitFrequencyBand + ",\n"
                + "transmitCenterFrequency=" + transmitCenterFrequency + ",\n"
                + "instrumentModesDescriptions=" + instrumentModesDescriptions + '}';
    }

    
    
}
