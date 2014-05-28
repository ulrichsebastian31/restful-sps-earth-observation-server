/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GroundStation.java
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
import java.util.List;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;

/**
 *
 * @author re-sulrich
 */
public class GroundStation {

    private String id;
    private String name;
    private String type;
    private String antennaType;
    private String purpose;
    private double defaultElevation;
    private double lon;
    private double lat;
    private double alt;
    
    private List<Point> maskPoints;
    
    /**
     * Default constructor
     */
    public GroundStation() {
        
    }
    /**
     * Constructor with parameters
     * @param id
     * @param name
     * @param intId
     * @param antennaType
     * @param lon
     * @param lat
     * @param alt 
     */
    public GroundStation(String id, String name, String type, String antennaType, String purpose, double defaultElevation, double lon, double lat, double alt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.antennaType = antennaType;
        this.purpose = purpose;
        this.defaultElevation = defaultElevation;
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
        this.maskPoints = new ArrayList<Point>();
    }

    public GroundStation(String id, String name, String type, String antennaType, String purpose, double defaultElevation, double lon, double lat, double alt, List<Point> maskPoints) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.antennaType = antennaType;
        this.purpose = purpose;
        this.defaultElevation = defaultElevation;
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
        this.maskPoints = maskPoints;
    }


    /**
     * Getters and setters 
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAntennaType() {
        return antennaType;
    }

    public void setAntennaType(String antennaType) {
        this.antennaType = antennaType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getDefaultElevation() {
        return defaultElevation;
    }

    public void setDefaultElevation(double defaultElevation) {
        this.defaultElevation = defaultElevation;
    }

    public List<Point> getMaskPoints() {
        return maskPoints;
    }

    public void setMaskPoints(List<Point> maskPoints) {
        this.maskPoints = maskPoints;
    }
    public void addMaskPoint(Point maskPoint) {
        if (this.maskPoints == null) 
            this.maskPoints = new ArrayList<Point>();
        this.maskPoints.add(maskPoint);
    }
    public void addMaskPoint(double longitude, double latitude) {
        if (this.maskPoints == null) 
            this.maskPoints = new ArrayList<Point>();
        this.maskPoints.add(new Point(longitude, latitude, 0.0));
    }
}
