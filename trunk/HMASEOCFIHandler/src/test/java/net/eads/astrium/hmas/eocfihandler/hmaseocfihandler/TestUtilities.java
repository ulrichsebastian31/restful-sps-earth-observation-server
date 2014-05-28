/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestUtilities.java
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
package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;

/**
 *
 * @author re-dboatswain
 */
public class TestUtilities {
    
    public static Date extractVisibilityDate(BufferedReader dataIn) throws IOException
    {
	// Extract the first non-commented value - Taken as the Visibility Start Period
	String sFileLn = "#";
	while ((sFileLn.startsWith("#")) || (sFileLn.isEmpty()))
	{
            sFileLn = dataIn.readLine();
	}
	
	// Write Retrieved Visibility Time to Console
	System.out.println("Visibility Time = " + sFileLn + "\n");
		    	
        return extractVisibilityDate( sFileLn );
    }
    
    public static Date extractVisibilityDate( String strDate )
    {
        Date oVisTime = null;
                    
        try
        {
            // Create a java.util.Date object consisting of the received Visibility Start Time
            String[] sDateTime = strDate.split("_");	// Split the string into Date and Time
            if (sDateTime.length > 1)
            {
                String[] sDate = sDateTime[0].split("-");	// Split the Date into Years, Months, Days
                String[] sTime = sDateTime[1].split(":");	// Split the Time into Hours, Minutes, Seconds

                Calendar oTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                oTime.set(Integer.parseInt(sDate[0]), Integer.parseInt(sDate[1]) - 1, Integer.parseInt(sDate[2]), 
                        Integer.parseInt(sTime[0]), Integer.parseInt(sTime[1]), Integer.parseInt(sTime[2]));
                oVisTime = oTime.getTime();
            }
        }
        catch(NumberFormatException exc)
        {
            oVisTime = null;
        }
        
        return oVisTime;
    }
	
    public static Polygon extractVisibilityAOI(BufferedReader dataIn) throws IOException
    {
        double dAltitude = 0;
				
	// Extract the first non-commented value - Taken as the Visibility Start Period
	String sFileLn = "#";
	while ((sFileLn.startsWith("#")) || (sFileLn.isEmpty()))
	{
            sFileLn = dataIn.readLine();
	}
		
	// Split the text string into Longitude and Latitude
	String[] sLatLong = sFileLn.split("_");
	Point oVisPnt = new Point(Double.parseDouble(sLatLong[1]), Double.parseDouble(sLatLong[3]), dAltitude);
	List<Point> visPoints = new ArrayList<Point>();
	visPoints.add(oVisPnt);
	Polygon oVisAoI = new Polygon(visPoints);
		
	// Extract the polygon Lat and Long values
	boolean bEoAOI = false;
	while (bEoAOI == false)
	{
            // Read the next line from the file
            sFileLn = dataIn.readLine();
			
            // If this is not the end of the file ...
            if (sFileLn != null)
            {
                // If the line is not commented or empty ...
		if ((!sFileLn.startsWith("#")) && (!sFileLn.matches("")))
		{
                    // Split the text string into Longitude and Latitude, 
                    // set a point with those values and add to Polygon
                    sLatLong = sFileLn.split("_");
                    Point oNewVisPnt = new Point(Double.parseDouble(sLatLong[1]), Double.parseDouble(sLatLong[3]),
                            dAltitude);
                    oVisAoI.addNewPoint(oNewVisPnt);
		}
		else	// If the line is commented or empty, that signals the end of the AoI
		{
                    bEoAOI = true;
		}
            }
            else	// If it is the end of the file then break loop
            {
		bEoAOI = true;
            }

	}

	return oVisAoI;
    }
}
