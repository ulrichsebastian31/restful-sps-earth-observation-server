/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestDates.java
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
package net.eads.astrium.dream.dreamutil;

import java.text.ParseException;
import java.util.Date;
import net.eads.astrium.hmas.util.DateHandler;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestDates {
    
//    @Test
    public void test() throws ParseException {
        
        Date d1 = (DateHandler.parseDate("2013-10-10T00:00:00Z"));
        Date d2 = (DateHandler.parseDate("2013-10-10T00:00:00+0000"));
        Date d3 = (DateHandler.parseDate("2013-10-10T10:00:00"));
        
        System.out.println("" + DateHandler.formatDate(d1));
        System.out.println("" + DateHandler.formatDateToEocfi(d2));
        System.out.println("" + DateHandler.formatDateToSimple(d3));
    }
    @Test
    public void test2() throws ParseException {
        
        Date d1 = (DateHandler.parseDate("2013-10-10T00:00:00.000Z"));
        Date d4 = (DateHandler.parseDate("2013-10-10T00:00:00Z"));
        Date d2 = (DateHandler.parseDate("2013-10-10T00:00:00+0000"));
        Date d3 = (DateHandler.parseDate("2013-10-10T10:00:00"));
        
        System.out.println("" + d1 + " - " + DateHandler.formatDate(d1));
        System.out.println("" + DateHandler.formatDateToEocfi(d2));
        System.out.println("" + DateHandler.formatDateToSimple(d3));
        System.out.println("" + DateHandler.formatDate(d4));
    }
}
