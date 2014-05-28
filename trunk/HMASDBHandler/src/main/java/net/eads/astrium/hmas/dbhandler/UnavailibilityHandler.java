/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               UnavailibilityHandler.java
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
package net.eads.astrium.hmas.dbhandler;

import net.eads.astrium.hmas.util.Algorithms;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.TimePeriod;

/**
 *
 * @author re-sulrich
 */
public class UnavailibilityHandler extends DatabaseLoader {


    public UnavailibilityHandler() {
        
        super("MissionPlannerDatabase");
    }
    public UnavailibilityHandler(DBOperations dboperations) {
        
        super(dboperations);
    }
    
    public UnavailibilityHandler(String databaseURL, String user, String pass, String schemaName) {
        
        super(databaseURL, user, pass, schemaName);
    }

    public List<TimePeriod> getSensorAvailibilities(String sensorId, String begin, String end)
            throws SQLException, ParseException {
        List<TimePeriod> unavailibilities = this.getSensorUnavailibilities(sensorId, begin, end);
        
        Date b = DateHandler.parseDate(begin);
        Date e = DateHandler.parseDate(end);
        
        return getAvailibilities(b, e, unavailibilities);
        
    }

    public List<TimePeriod> getStationAvailibilities(String stationId, String begin, String end)
            throws SQLException, ParseException {
        List<TimePeriod> unavailibilities = getStationUnavailibilities(stationId, begin, end);
        
        Date b = DateHandler.parseDate(begin);
        Date e = DateHandler.parseDate(end);
        
        return getAvailibilities(b, e, unavailibilities);
        
    }
    public List<TimePeriod> getSensorUnavailibilities(String sensorId, String begin, String end) throws SQLException, ParseException {
        String table = "Unavailibility";
        
        //Result
        List<TimePeriod> unavailibilities = new ArrayList<TimePeriod>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("beginu");
        fields.add("endu");
        
        //Filtering the DB results by sensor type
        List<String> conditions = new ArrayList<String>();
        conditions.add("NOT(Unavailibility.beginu < '"+begin+"' AND Unavailibility.endu < '"+begin+"')");
        conditions.add("NOT(Unavailibility.beginu > '"+end+"' AND Unavailibility.endu > '"+end+"')");
        conditions.add("sensorId='"+sensorId+"'");

        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0)
        for (List<String> list : result) {
            
            Date b = null;
            Date e = null;
            
            b = DateHandler.parseBDDDate(list.get(0));
            e = DateHandler.parseBDDDate(list.get(1));
            
            unavailibilities.add(new TimePeriod(b, e));
        }
        
        return unavailibilities;
    }
    
    public List<TimePeriod> getStationUnavailibilities(String stationId, String begin, String end) throws SQLException, ParseException {
        
        String table = "Unavailibility";
        
        //Result
        List<TimePeriod> unavailibilities = new ArrayList<TimePeriod>();

        //Fields needed to fill in the Sensor structures
        List<String> fields = new ArrayList<String>();
        fields.add("beginu");
        fields.add("endu");
        
        //Filtering the DB results by sensor type
        List<String> conditions = new ArrayList<String>();
        conditions.add("NOT(Unavailibility.beginu < '"+begin+"' AND Unavailibility.endu < '"+begin+"')");
        conditions.add("NOT(Unavailibility.beginu > '"+end+"' AND Unavailibility.endu > '"+end+"')");
        conditions.add("groundStationId='"+stationId+"'");

        //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0)
        for (List<String> list : result) {
            
            Date b = null;
            Date e = null;
            
            b = DateHandler.parseBDDDate(list.get(0));
            e = DateHandler.parseBDDDate(list.get(1));
            
            unavailibilities.add(new TimePeriod(b, e));
        }
        
        return unavailibilities;
    }


    public List<TimePeriod> getAvailibilities(Date beginRequest, Date endRequest, List<TimePeriod> unavailibilities) {
        List<TimePeriod> availibilities = new ArrayList<TimePeriod>();

        TimePeriod requestPeriod = new TimePeriod(beginRequest, endRequest);
        availibilities.add(requestPeriod);

        for (TimePeriod anUnavailibility : unavailibilities) {
            List<TimePeriod> tmp = new ArrayList<TimePeriod>();

            for (TimePeriod anAvailibility : availibilities) {
                if (this.areCorrespondingDates(
                        anUnavailibility.getBegin(), anUnavailibility.getEnd(),
                        anAvailibility.getBegin(), anAvailibility.getEnd())) {
                    if (anAvailibility.getEnd().after(anUnavailibility.getEnd())) {
                        tmp.add(
                                new TimePeriod(anUnavailibility.getEnd(), anAvailibility.getEnd()));

                        if (anAvailibility.getBegin().before(anUnavailibility.getBegin())) {
                            tmp.add(
                                    new TimePeriod(anAvailibility.getBegin(), anUnavailibility.getBegin()));
                        }
                    } else {
                        if (anAvailibility.getBegin().before(anUnavailibility.getBegin())) {
                            tmp.add(
                                    new TimePeriod(
                                    anAvailibility.getBegin(),
                                    anUnavailibility.getBegin()));
                        }
                    }
                } else {
                    tmp.add(anAvailibility);
                }
            }

            availibilities = tmp;
        }


        availibilities = Algorithms.triParInsertionDate(availibilities);

        return availibilities;
    }
    
    private boolean areCorrespondingDates(
            Date beginRequest, Date endRequest,
            Date begin, Date end) {

        boolean isCorrespondance = false;

        if (begin.after(beginRequest) && begin.before(endRequest)) {
            isCorrespondance = true;
        } else {
            if (end.after(beginRequest) && end.before(endRequest)) {
                isCorrespondance = true;
            } else {
                if (!begin.after(beginRequest)
                        && end.after(endRequest)) {
                    isCorrespondance = true;
                }
            }
        }

        return isCorrespondance;
    }
}
