/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DateHandler.java
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
package net.eads.astrium.hmas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author re-sulrich
 */
public class DateHandler {

    //Common ISO 8601 parsers
    public static String DATE_FORMAT_MS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    public static String SHORT_DT_FORMAT = "yyyy-MM-dd'T'HH:mm";
//    public static SimpleDateFormat msDateFormat = new SimpleDateFormat(DATE_FORMAT_MS);
//    public static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
//    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat(SHORT_DATE_FORMAT);
//    public static SimpleDateFormat shortDTFormat = new SimpleDateFormat(SHORT_DT_FORMAT);
    //Component specific time formats parsers
    public static String EOCFI_DATE_FORMAT = "yyyyMMdd'T'HHmmss";
    public static String BDD_DATE_FORMAT = "yyyy-MM-dd' 'HH:mm:ss";
//    public static SimpleDateFormat eocfiDateFormat = new SimpleDateFormat(EOCFI_DATE_FORMAT);
//    public static SimpleDateFormat bddDateFormat = new SimpleDateFormat(BDD_DATE_FORMAT);

    public static Date parseDate(String date) throws ParseException {
        
        SimpleDateFormat msDateFormat = new SimpleDateFormat(DATE_FORMAT_MS);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(SHORT_DATE_FORMAT);
        SimpleDateFormat shortDTFormat = new SimpleDateFormat(SHORT_DT_FORMAT);
        
        if (date == null || date.equals(""))
            return null;
        
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        shortDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        Date d = null;
        
//        System.out.println("DateHandler : before Z : " + date);
        
        if (date.contains("Z")) {
            date = date.substring(0, date.indexOf("Z")) + "+0000";
        }
        
//        System.out.println("DateHandler : after Z, input is : " + date);
        
        try {
            d = msDateFormat.parse(date);
//            System.out.println("DateHandler : msdtf : " + d);
        } catch (ParseException e) {
            try {
                d = dateFormat.parse(date);
//                    System.out.println("DateHandler : dtf : " + d);
            } catch (ParseException e1) {
                try {
                    d = shortDTFormat.parse(date);
//                    System.out.println("DateHandler : sdtf : " + d);
                } catch (ParseException e2) {
                    d = shortDateFormat.parse(date);
//                    System.out.println("DateHandler : sdf : " + d);
                }
            }
        }
        return d;
    }
    public static Date parseEocfiDate(String date) throws ParseException {
        
        if (date == null || date.equals(""))
            return null;
        
        SimpleDateFormat eocfiDateFormat = new SimpleDateFormat(EOCFI_DATE_FORMAT);
        eocfiDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        return eocfiDateFormat.parse(date);
    }

    public static Date parseBDDDate(String date) throws ParseException {
        
        if (date == null || date.equals(""))
            return null;
        
        SimpleDateFormat bddDateFormat = new SimpleDateFormat(BDD_DATE_FORMAT);
        
        bddDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        return bddDateFormat.parse(date);
    }

    public static String formatDate(Date date) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        return dateFormat.format(date);
    }

    public static String formatDateToSimple(Date date) {
        
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(SHORT_DATE_FORMAT);
        shortDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        return shortDateFormat.format(date);
    }

    public static String formatDateToEocfi(Date date) {
        
        SimpleDateFormat eocfiDateFormat = new SimpleDateFormat(EOCFI_DATE_FORMAT);
        eocfiDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        return eocfiDateFormat.format(date);
    }
    
    public static Calendar getCalendar(Date date) {
        
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        
        return calendar;
    }
    
    public static long secondsBetween(Calendar startDate, Calendar endDate) {  
        Calendar date = (Calendar) startDate.clone();  
        long secondsBetween=  0;  
        while (date.before(endDate)) {  
            date.add(Calendar.SECOND, 1);  
            secondsBetween++;  
        }  
        return secondsBetween;  
    }  
}
