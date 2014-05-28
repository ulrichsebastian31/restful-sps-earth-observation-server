/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Point.java
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
package net.eads.astrium.hmas.util.structures.tasking.geometries;

/**
 *
 * @author re-sulrich
 */
public class Point {
    
    private double longitude;
    private double latitude;
    private double altitude;

    public Point(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public boolean equals(Object obj) {
        
        boolean is = false;
        
        if (obj instanceof Point) {
            
            Point p = (Point)obj;
            
            if (p.longitude == this.longitude && p.latitude == this.latitude && p.altitude == this.altitude) {
                is = true;
            }
        }
        
        return is;
    }
    
    public String printCoordinatesGML() {
        
//        return "" + this.longitude + "," + this.latitude;
        return "" + this.latitude + "," + this.longitude;
    }
    
    public String printCoordinatesGMLWithAltitude() {
        
//        return "" + this.longitude + "," + this.latitude + "," + this.altitude;
        return "" + this.latitude + "," + this.longitude + "," + this.altitude;
    }
    
    @Override
    public String toString() {
        return this.printCoordinatesGML();
    }
}
