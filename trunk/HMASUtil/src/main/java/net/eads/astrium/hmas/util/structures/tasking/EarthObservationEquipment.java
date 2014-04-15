/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.structures.tasking;

/**
 *
 * @author re-sulrich
 */
public class EarthObservationEquipment {
    private String platformId;
    private String platformName;
    private String platformDescription;
    private String orbitType;
    private String sensorName;
    private String sensorType;

    public EarthObservationEquipment(String platformId, String platformName, String platformDescription, String orbitType, String sensorName, String sensorType) {
        this.platformId = platformId;
        this.platformName = platformName;
        this.platformDescription = platformDescription;
        this.orbitType = orbitType;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformDescription() {
        return platformDescription;
    }

    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    public String getOrbitType() {
        return orbitType;
    }

    public void setOrbitType(String orbitType) {
        this.orbitType = orbitType;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    @Override
    public String toString() {
        return "EarthObservationEquipment{" + "platformId=" + platformId + ", platformName=" + platformName + ", platformDescription=" + platformDescription + ", orbitType=" + orbitType + ", sensorName=" + sensorName + ", sensorType=" + sensorType + '}';
    }
}
