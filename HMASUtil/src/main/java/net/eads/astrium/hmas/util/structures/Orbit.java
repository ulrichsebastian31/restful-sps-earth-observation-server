/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Orbit.java
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

/**
 *
 * @author re-sulrich
 */

public class Orbit {


    private String orbitType;
    private String low_Min_Semi_Major_Axis;
    private String low_Max_Semi_Major_Axis;
    private String low_Min_Inclination;
    private String low_Max_Inclination;
    private String low_Min_Eccentricity;
    private String low_Max_Eccentricity;
    private String tight_Min_Semi_Major_Axis;
    private String tight_Max_Semi_Major_Axis;
    private String tight_Min_Inclination;
    private String tight_Max_Inclination;
    private String tight_Min_Eccentricity;
    private String tight_Max_Eccentricity;
    private String orbit_Min_Semi_Major_Axis;
    private String orbit_Nom_Semi_Major_Axis;
    private String orbit_Max_Semi_Major_Axis;
    private String orbit_Min_Inclination;
    private String orbit_Nom_Inclination;
    private String orbit_Max_Inclination;
    private String orbit_Nom_Eccentricity;
    private String orbit_Nom_Arg_Perigee;

    public Orbit(String orbitType, String low_Min_Semi_Major_Axis, String low_Max_Semi_Major_Axis, String low_Min_Inclination, String low_Max_Inclination, String low_Min_Eccentricity, String low_Max_Eccentricity, String tight_Min_Semi_Major_Axis, String tight_Max_Semi_Major_Axis, String tight_Min_Inclination, String tight_Max_Inclination, String tight_Min_Eccentricity, String tight_Max_Eccentricity, String orbit_Min_Semi_Major_Axis, String orbit_Nom_Semi_Major_Axis, String orbit_Max_Semi_Major_Axis, String orbit_Min_Inclination, String orbit_Nom_Inclination, String orbit_Max_Inclination, String orbit_Nom_Eccentricity, String orbit_Nom_Arg_Perigee) {
        this.orbitType = orbitType;
        this.low_Min_Semi_Major_Axis = low_Min_Semi_Major_Axis;
        this.low_Max_Semi_Major_Axis = low_Max_Semi_Major_Axis;
        this.low_Min_Inclination = low_Min_Inclination;
        this.low_Max_Inclination = low_Max_Inclination;
        this.low_Min_Eccentricity = low_Min_Eccentricity;
        this.low_Max_Eccentricity = low_Max_Eccentricity;
        this.tight_Min_Semi_Major_Axis = tight_Min_Semi_Major_Axis;
        this.tight_Max_Semi_Major_Axis = tight_Max_Semi_Major_Axis;
        this.tight_Min_Inclination = tight_Min_Inclination;
        this.tight_Max_Inclination = tight_Max_Inclination;
        this.tight_Min_Eccentricity = tight_Min_Eccentricity;
        this.tight_Max_Eccentricity = tight_Max_Eccentricity;
        this.orbit_Min_Semi_Major_Axis = orbit_Min_Semi_Major_Axis;
        this.orbit_Nom_Semi_Major_Axis = orbit_Nom_Semi_Major_Axis;
        this.orbit_Max_Semi_Major_Axis = orbit_Max_Semi_Major_Axis;
        this.orbit_Min_Inclination = orbit_Min_Inclination;
        this.orbit_Nom_Inclination = orbit_Nom_Inclination;
        this.orbit_Max_Inclination = orbit_Max_Inclination;
        this.orbit_Nom_Eccentricity = orbit_Nom_Eccentricity;
        this.orbit_Nom_Arg_Perigee = orbit_Nom_Arg_Perigee;
    }

    public String getOrbitType() {
        return orbitType;
    }

    public void setOrbitType(String orbitType) {
        this.orbitType = orbitType;
    }

    public String getLow_Min_Semi_Major_Axis() {
        return low_Min_Semi_Major_Axis;
    }

    public void setLow_Min_Semi_Major_Axis(String low_Min_Semi_Major_Axis) {
        this.low_Min_Semi_Major_Axis = low_Min_Semi_Major_Axis;
    }

    public String getLow_Max_Semi_Major_Axis() {
        return low_Max_Semi_Major_Axis;
    }

    public void setLow_Max_Semi_Major_Axis(String low_Max_Semi_Major_Axis) {
        this.low_Max_Semi_Major_Axis = low_Max_Semi_Major_Axis;
    }

    public String getLow_Min_Inclination() {
        return low_Min_Inclination;
    }

    public void setLow_Min_Inclination(String low_Min_Inclination) {
        this.low_Min_Inclination = low_Min_Inclination;
    }

    public String getLow_Max_Inclination() {
        return low_Max_Inclination;
    }

    public void setLow_Max_Inclination(String low_Max_Inclination) {
        this.low_Max_Inclination = low_Max_Inclination;
    }

    public String getLow_Min_Eccentricity() {
        return low_Min_Eccentricity;
    }

    public void setLow_Min_Eccentricity(String low_Min_Eccentricity) {
        this.low_Min_Eccentricity = low_Min_Eccentricity;
    }

    public String getLow_Max_Eccentricity() {
        return low_Max_Eccentricity;
    }

    public void setLow_Max_Eccentricity(String low_Max_Eccentricity) {
        this.low_Max_Eccentricity = low_Max_Eccentricity;
    }

    public String getTight_Min_Semi_Major_Axis() {
        return tight_Min_Semi_Major_Axis;
    }

    public void setTight_Min_Semi_Major_Axis(String tight_Min_Semi_Major_Axis) {
        this.tight_Min_Semi_Major_Axis = tight_Min_Semi_Major_Axis;
    }

    public String getTight_Max_Semi_Major_Axis() {
        return tight_Max_Semi_Major_Axis;
    }

    public void setTight_Max_Semi_Major_Axis(String tight_Max_Semi_Major_Axis) {
        this.tight_Max_Semi_Major_Axis = tight_Max_Semi_Major_Axis;
    }

    public String getTight_Min_Inclination() {
        return tight_Min_Inclination;
    }

    public void setTight_Min_Inclination(String tight_Min_Inclination) {
        this.tight_Min_Inclination = tight_Min_Inclination;
    }

    public String getTight_Max_Inclination() {
        return tight_Max_Inclination;
    }

    public void setTight_Max_Inclination(String tight_Max_Inclination) {
        this.tight_Max_Inclination = tight_Max_Inclination;
    }

    public String getTight_Min_Eccentricity() {
        return tight_Min_Eccentricity;
    }

    public void setTight_Min_Eccentricity(String tight_Min_Eccentricity) {
        this.tight_Min_Eccentricity = tight_Min_Eccentricity;
    }

    public String getTight_Max_Eccentricity() {
        return tight_Max_Eccentricity;
    }

    public void setTight_Max_Eccentricity(String tight_Max_Eccentricity) {
        this.tight_Max_Eccentricity = tight_Max_Eccentricity;
    }

    public String getOrbit_Min_Semi_Major_Axis() {
        return orbit_Min_Semi_Major_Axis;
    }

    public void setOrbit_Min_Semi_Major_Axis(String orbit_Min_Semi_Major_Axis) {
        this.orbit_Min_Semi_Major_Axis = orbit_Min_Semi_Major_Axis;
    }

    public String getOrbit_Nom_Semi_Major_Axis() {
        return orbit_Nom_Semi_Major_Axis;
    }

    public void setOrbit_Nom_Semi_Major_Axis(String orbit_Nom_Semi_Major_Axis) {
        this.orbit_Nom_Semi_Major_Axis = orbit_Nom_Semi_Major_Axis;
    }

    public String getOrbit_Max_Semi_Major_Axis() {
        return orbit_Max_Semi_Major_Axis;
    }

    public void setOrbit_Max_Semi_Major_Axis(String orbit_Max_Semi_Major_Axis) {
        this.orbit_Max_Semi_Major_Axis = orbit_Max_Semi_Major_Axis;
    }

    public String getOrbit_Min_Inclination() {
        return orbit_Min_Inclination;
    }

    public void setOrbit_Min_Inclination(String orbit_Min_Inclination) {
        this.orbit_Min_Inclination = orbit_Min_Inclination;
    }

    public String getOrbit_Nom_Inclination() {
        return orbit_Nom_Inclination;
    }

    public void setOrbit_Nom_Inclination(String orbit_Nom_Inclination) {
        this.orbit_Nom_Inclination = orbit_Nom_Inclination;
    }

    public String getOrbit_Max_Inclination() {
        return orbit_Max_Inclination;
    }

    public void setOrbit_Max_Inclination(String orbit_Max_Inclination) {
        this.orbit_Max_Inclination = orbit_Max_Inclination;
    }

    public String getOrbit_Nom_Eccentricity() {
        return orbit_Nom_Eccentricity;
    }

    public void setOrbit_Nom_Eccentricity(String orbit_Nom_Eccentricity) {
        this.orbit_Nom_Eccentricity = orbit_Nom_Eccentricity;
    }

    public String getOrbit_Nom_Arg_Perigee() {
        return orbit_Nom_Arg_Perigee;
    }

    public void setOrbit_Nom_Arg_Perigee(String orbit_Nom_Arg_Perigee) {
        this.orbit_Nom_Arg_Perigee = orbit_Nom_Arg_Perigee;
    }


}
    
