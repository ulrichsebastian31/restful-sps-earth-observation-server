/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Algorithms.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.SegmentVisGS;

/**
 *
 * @author re-sulrich
 */
public class Algorithms {
    
    
    public static int getRandom(int min, int max) {
        return (int)((Math.random() * (max - min + 1)) + min);
    }
    
    public static String getRandom32BitsID() {
        
        String id = "";
        
        for (int i = 0; i < 32; i++) {
            
            Double val = Math.random() * 16;
            Double valMod = val/1;
            char x = '0';
            
            if (valMod >= 10) {
                x = (char) ('a' + (valMod - 10));
            }
            else {
                x = (char) ('0' + valMod);
            }
//            System.out.println("" + val + " - " + valMod + " - " + x);
            
            id += x;
        }
        
        return id;
    }
    
    public static void getClockWise(Polygon p) {
        
        double meanLat = 0.0;
        double meanLon = 0.0;
        
        boolean isClockwise = false;
        
//        for (Point point : p.getPoints()) {
//            meanLat += point.getLatitude();
//            meanLon += point.getLongitude();
//        }
//        
//        meanLat = meanLat / p.getPoints().size();
//        meanLon = meanLon / p.getPoints().size();
        
        int north = getMaxNorth(p);
        int south = getMaxSouth(p);
        int west = getMaxWest(p);
        int east = getMaxEast(p);
        
        System.out.println("" + north + " - " + east + " - " + west + " - " + south);
        
        int current = north;
        int good = east;
        int bad = west;
        if (current == good) {
            current = east;
            good = south;
            System.out.println("North = East");
        }
        for (int i = 1; i < p.getPoints().size(); i++) {
            
            if (  (current + i)%(p.getPoints().size()) == good   ) {
                isClockwise = true;
                break;
            }
            if (  (current + i)%(p.getPoints().size()) == bad   ) {
                break;
            }
        }
        System.out.println("Is clockwise ? " + isClockwise);
        if (!isClockwise) {
            List<Point> neww = new ArrayList<Point>();
            for (int i = (p.getPoints().size() - 1); i >= 0; i--) {
                
                System.out.println("i = " + i + " - points size : " + p.getPoints().size());
                
                neww.add(p.getPoints().get(i));
            }
            p.setPoints(neww);
        }
    }
    
    public static int getMaxNorth(Polygon p) {
        
        int rank = 0;
        Point result = p.getPoints().get(0);
        
        for (int i = 0; i < p.getPoints().size(); i++) {
            Point point = p.getPoints().get(i);
            
            if (result.getLatitude() < point.getLatitude()) {
                result = point;
                rank = i;
            }
        }
        
        return rank;
    }
    
    public static int getMaxSouth(Polygon p) {
        
        int rank = 0;
        Point result = p.getPoints().get(0);
        
        for (int i = 0; i < p.getPoints().size(); i++) {
            Point point = p.getPoints().get(i);
            
            System.out.println("" + result.getLatitude() + " cmp " + point.getLatitude());
            
            if (result.getLatitude() > point.getLatitude()) {
                result = point;
                rank = i;
                System.out.println("true");
            }
        }
        
        return rank;
    }
    
    public static int getMaxWest(Polygon p) {
        
        int rank = 0;
        Point result = p.getPoints().get(0);
        
        for (int i = 0; i < p.getPoints().size(); i++) {
            Point point = p.getPoints().get(i);
            
            if (result.getLongitude() > point.getLongitude()) {
                result = point;
                rank = i;
            }
        }
        
        return rank;
    }
    
    public static int getMaxEast(Polygon p) {
        
        int rank = 0;
        Point result = p.getPoints().get(0);
        
        for (int i = 0; i < p.getPoints().size(); i++) {
            Point point = p.getPoints().get(i);
            
            if (result.getLongitude() < point.getLongitude()) {
                result = point;
                rank = i;
            }
        }
        
        return rank;
    }
    
    
    
    public static List<Double> triParInsertionDouble(List<Double> periods)
    {
//        List<Double> d = Arrays.asList(new Double[]{0.0,0.0});
        
        for (int i = 1; i < periods.size(); i++)
        {
            Double x = periods.get(i);
            int j = i;
            while (j > 0 && periods.get(j - 1) > (x))
            {
                Collections.swap(
                        periods, 
                        j, 
                        j - 1);
                
                j--;
            }
            periods.set(j, x);
        }
        
        return periods;
    }
    
    
    
    
    
    public static List<TimePeriod> triParInsertionDate(List<TimePeriod> periods)
    {
        for (int i = 1; i < periods.size(); i++)
        {
            TimePeriod x = periods.get(i);
            int j = i;
            while (j > 0 && periods.get(j - 1).getBegin().after(x.getBegin()))
            {
                Collections.swap(
                        periods, 
                        j, 
                        j - 1);
                
                j--;
            }
            periods.set(j, x);
        }
        
        return periods;
    }
    
    public static List<SegmentVisGS> triParInsertionSVG(List<SegmentVisGS> vis)
    {
        for (int i = 1; i < vis.size(); i++)
        {
            SegmentVisGS x = vis.get(i);
            int j = i;
            while (j > 0 && vis.get(j - 1).getStartOfVisibility().after(x.getStartOfVisibility()))
            {
                Collections.swap(
                        vis, 
                        j, 
                        j - 1);
                
                j--;
            }
            vis.set(j, x);
        }
        
        return vis;
    }
}
