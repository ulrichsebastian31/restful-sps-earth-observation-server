/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TimeOfInt.java
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
 */package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

import net.eads.astrium.hmas.util.DateHandler;
import java.util.Date;
import java.util.Calendar;

public class TimeOfInt {
	
	public static final long SECONDS_IN_A_DAY = 86400; 		// Number of seconds in a day i.e. 24 x 60 x 60
	public static final long MILLISECONDS_IN_A_SEC = 1000;	// Number of milliseconds in a second
	/*
	 java.util.Date returns time in ms since January 1, 1970, 00:00:00 GMT
	 EOCFI time reference is in Modified Julian Day 2000 : "interval of time in days and fraction of day since
	 	2000 January 1 at 00:00:00"
	 
	 There are 30 Years between the two references, 7 of which are leap years
	 	Number of Seconds in the Standard Years 	= (30 - 7) x 365 x 86400 	= 725328000s
	 	Number of Seconds in the Leap Years			= 7 x 366 x 86400			= 221356800s
	 	Number of Seconds between the references	= 946684800s
	 */
	private static final long SECONDS_DIFF_1970_2000 = 946684800;
	
	private double m_dStartTime;	// UTC Start of the Time of Interest in days since 2000/01/01 00:00:00
	private double m_dEndTime;		// UTC End of the Time of Interest in days since 2000/01/01 00:00:00
	
	// Default Constructor
	public TimeOfInt( )
	{
		m_dStartTime = 0;
		m_dEndTime = 0;
	}
	
	// Constructor to set all the member variables at object creation
	public TimeOfInt( Date oStartDate, Date oEndDate )
	{
		// Convert the Start Date to a number of Days since 2000 January 1 at 00:00:00 and store in member variable	
		m_dStartTime = convert_ms1970_MJD2000(oStartDate.getTime());
		
		// Convert the End Date to a number of Days since 2000 January 1 at 00:00:00 and store in member variable		
		m_dEndTime = convert_ms1970_MJD2000(oEndDate.getTime());
	}
	
	// Set the Start Time of the Time of Interest directly
	public void setStartTime( double dReqStartTime )
	{
		m_dStartTime = dReqStartTime;
	}
	
	// Set the Start Time of the Time of Interest using a java.util.Date object
	public void setStartTime( Date oStartDate )
	{
		// Convert the Start Date to a number of Days since 2000 January 1 at 00:00:00 and store in member variable
		m_dStartTime = convert_ms1970_MJD2000(oStartDate.getTime());
	}
	
	// Set the End Time of the Time of Interest directly
	public void setEndTime( double dReqEndTime )
	{
		m_dEndTime = dReqEndTime;
	}
	
	// Set the End Time of the Time of Interest using a java.util.Date object
	public void setEndTime( Date oEndDate )
	{
		// Convert the End Date to a number of Days since 2000 January 1 at 00:00:00 and store in member variable
		m_dEndTime = convert_ms1970_MJD2000(oEndDate.getTime());
	}
	
	// Return the Time Of Interest Start Time
	public double getStartTime()
	{
		return m_dStartTime;
	}
        
        // Return the Time Of Interest Start Time as a String
        public String getStartTimeStr()
        {                       
            // Create a Calendar object, set the time in ms since 1970 then format it correctly
            Calendar oVal = Calendar.getInstance();
            oVal.setTimeInMillis(convert_MJD2000_ms1970(m_dStartTime));
            
            return DateHandler.formatDate(oVal.getTime());
        }
        
        // Return the Time Of Interest Start Time as a java.util.Date Object
        public Date getStartTimeDate()
        {
            // Create a Calendar object, set the time in ms since 1970 then format it correctly
            Calendar oVal = Calendar.getInstance();
            oVal.setTimeInMillis(convert_MJD2000_ms1970(m_dStartTime));
            
            return oVal.getTime();
        }
	
	// Return the Time Of Interest End Time
	public double getEndTime()
	{
		return m_dEndTime;
	}
        
        // Return the Time Of Interest End Time as a String
        public String getEndTimeStr()
        {                        
            // Create a Calendar object, set the time in ms since 1970 then format it correctly
            Calendar oVal = Calendar.getInstance();
            oVal.setTimeInMillis(convert_MJD2000_ms1970(m_dEndTime));
            
            return DateHandler.formatDate(oVal.getTime());
        }
        
        // Return the Time Of Interest Start Time as a java.util.Date Object
        public Date getEndTimeDate()
        {
            // Create a Calendar object, set the time in ms since 1970 then format it correctly
            Calendar oVal = Calendar.getInstance();
            oVal.setTimeInMillis(convert_MJD2000_ms1970(m_dEndTime));
            
            return oVal.getTime();
        }
        
        public boolean validityPeriodTest( Date oStart, Date oEnd )
        {
            boolean bValid = false;
            
            // Create a Calendar object representing the Validity Start Time
            Calendar oValStart = Calendar.getInstance();
            oValStart.setTime(oStart);
            
            // Create a Calendar object representing the Validity End Time
            Calendar oValEnd = Calendar.getInstance();
            oValEnd.setTime(oEnd);
            
            // Create a Calendar object and set the ToI start time in ms since 1970
            Calendar oToIStart = Calendar.getInstance();
            oToIStart.setTimeInMillis(convert_MJD2000_ms1970(m_dStartTime));
            
            // Create a Calendar object and set the ToI end time in ms since 1970
            Calendar oToIEnd = Calendar.getInstance();
            oToIEnd.setTimeInMillis(convert_MJD2000_ms1970(m_dEndTime));
            
            // If the ToI Start Time is After the Validity Start period and the
            // ToI End Time Before the Validity End Time then the ToI is valid
            if ( (oToIStart.after( oValStart )) && (oToIEnd.before( oValEnd )) )
            {
                bValid = true;
            }
            
            return bValid;
        }
	
	// Private member function for converting a time in milliseconds relative to January 1, 1970 to a time in days 
	// relative to January 1, 2000
	private double convert_ms1970_MJD2000(long iMilliSec1970)
	{
		double dSeconds2000 = (iMilliSec1970 / MILLISECONDS_IN_A_SEC) - SECONDS_DIFF_1970_2000;
		return (dSeconds2000 / SECONDS_IN_A_DAY);
	}
        
        // Private member function for converting a time in days relative to January 1, 2000 to a time in milliseconds 
        // relative to January 1, 1970
        private long convert_MJD2000_ms1970(double dMJD2000)
        {
                double dSeconds2000 = dMJD2000 * SECONDS_IN_A_DAY;
                double dMs1970 = (dSeconds2000 + SECONDS_DIFF_1970_2000) * MILLISECONDS_IN_A_SEC;
                return (long)dMs1970;
        }
}
