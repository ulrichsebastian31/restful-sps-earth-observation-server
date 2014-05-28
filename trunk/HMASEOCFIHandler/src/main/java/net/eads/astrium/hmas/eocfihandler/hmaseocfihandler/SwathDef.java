/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SwathDef.java
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
 *  ---------------------------------------------------------------------------------------------
 *   Project							:	DREAM
 *  ---------------------------------------------------------------------------------------------
 *   File Name							:	SwathDef.java
 *   File Type							:	Source Code
 *   Description						:	This class provides access to the required EOCFI
 *   										swath functions necessary for Feasibility Tasking
 *   
 *  ---------------------------------------------------------------------------------------------
 *  
 *  =============================================================================================
 *  	(C)	COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *  	This software is supplied by EADS Astrium Limited on the express terms
 *  	that it is to be treated as confidential and that it may not be copied,
 *  	used or disclosed to others for any purpose except as authorised in
 *  	writing by this Company.
 *  ---------------------------------------------------------------------------------------------
 */

package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.*;
import net.eads.astrium.hmas.util.structures.tasking.geometries.*;
import static EECFI.EnumOrbit.XOCFI_TIME_UTC;
import EECFI.ANXTime;
import EECFI.CfiError;
import EECFI.OrbitId;
import EECFI.SdfFile;
import EECFI.StfFile;
import EECFI.Swath;
import EECFI.TimeSegment;
import EECFI.ZoneRec;
import EECFI.VisibilityList;
import EECFI.VisibilitySegment;
import EECFI.Geodetic;
import EECFI.EnumDataHandling;

import EECFI.EnumVisibility;
import EECFI.StationFile;
        
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Vector;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EoCfiHandler.FileType;


public class SwathDef {
	
        // Swath points are generated every "ORBIT_NUM_SWATH_PTS" orbits
        private final long ORBIT_NUM_SWATH_PTS = 1;
    
	private String m_sdfFileLocation;
        private String m_gndStFileLocation;
	private OrbitId m_orbitID;
	private Swath m_swathObj;
	private StfFile m_stfFile;
	private TimeSegment m_OrbitRange;
	private VisibilityList m_VisList;
        private List<Segment> m_VisCoverage;
	private double m_dMinVisDuration;
        
        // Ground station visibility variables
        private String m_sGndStFilename;
        Vector<String> m_vStationId;
        Vector<String> m_vStationName;
        double[] m_vAoSEl;
        double[] m_vLoSEl;
        long[] m_vMask;
        
        List<String> m_vGndStationListId;
        List<String> m_vGndStationListName;
                	
	// Class constructor
	public SwathDef( OrbitId oReqOrbitId,
                String sdfFileLocation,
                String sSDFFilename ) throws CfiError
	{	
            // If the input string does not end with a final folder separator, add one
            if (sdfFileLocation.endsWith(File.separator) == false)
            {
                m_sdfFileLocation = sdfFileLocation + File.separator;
            }
            else
            {
                m_sdfFileLocation = sdfFileLocation;
            }
            
            // Initialise the OrbitID and Swath member variables and set the
            // SDF file within the Swath object
            if (!initialiseSwath(oReqOrbitId, sSDFFilename))
            {
                // Set to null if the initialisation above fails
                m_orbitID = null;
                
                m_swathObj = null;
            }
		
            // Initialise STF File member variable
            m_stfFile = null;
            m_VisList = null;
            m_OrbitRange = null;
            m_VisCoverage = null;
		
            // Initialise the Minimum duration for visibility (in seconds)
            m_dMinVisDuration = 1;	
         
            // Initialise the Ground Station visibility member variables
            m_vStationId = null;
            m_vStationName = null;
            m_vAoSEl = null;
            m_vLoSEl = null;
            m_vMask = null;
	}
        
        private boolean initialiseSwath( OrbitId oReqOrbitId,
                String sSDFFilename) throws CfiError
        {
            // Set the OrbitID member variable to that received
            boolean bInitRes = setOrbitID( oReqOrbitId );
            
            if (bInitRes)
            {
                // Create the Swath object that will be used to generate the STF
                m_swathObj = new Swath();

                // Initialise the Swath object and then generate the STF
                m_swathObj.set(m_orbitID, ORBIT_NUM_SWATH_PTS, 
                        m_sdfFileLocation + sSDFFilename); 
            }
            
            return bInitRes;
        }
	
	public void createSTFFromSDF(String sSDFFilename) throws CfiError
	{		
            if (m_swathObj != null)
            {
                // Create SDF file object using the member variable location and the received filename
                // Read both the header and the file contents from this file
                SdfFile oSdfObj = new SdfFile(m_sdfFileLocation + sSDFFilename);
                oSdfObj.read();

                // Generate the Swath Template Object by projecting the 
                // Instrument swath as defined in the SDF to the intersection with
                // the Earth
                m_stfFile = m_swathObj.genSwath(m_OrbitRange.start.orbit, oSdfObj);

                // Clear the variables
                oSdfObj = null;
            }
	}
	
        // Assess whether the calculated Swath covers the received ZoneRec
        // object during the sepcified times
	public boolean analyseZoneVisibility(ZoneRec oVisZone) throws CfiError
	{
            // Returned boolean to indicate success
            boolean bResult = false;
		
            // Ensure the pre-requisite objects have been initialised
            if ((m_swathObj != null) && (m_OrbitRange != null) &&
                    (m_stfFile != null))
            {
                // Call to the EOCFI Swath function to analyse the zone 
                // visibility using the OrbitRange start and stop times and the
                // read STF file
                bResult = true;
		m_VisList = m_swathObj.zoneVisTime(
                        m_OrbitRange.start.orbit, 
                        m_OrbitRange.stop.orbit, 
                        EnumDataHandling.XDCFI_RECTANGULAR, 
                        m_stfFile, 
                        oVisZone, 
                        m_dMinVisDuration);
            }

            return bResult;
	}
	
	public boolean calculatePositionCoord() throws CfiError
	{
            boolean bResult = false;
		
            if (m_VisCoverage == null)
            {
		m_VisCoverage = new ArrayList<Segment>();
            }
		
            if ((m_swathObj != null) && (m_stfFile != null))
            {		
                        
                for (int iSegment = 0; iSegment < m_VisList.segments.size(); iSegment++)
                {
                    VisibilitySegment oVisSeg = m_VisList.segments.elementAt(iSegment);

                    if (checkSegmentTime(oVisSeg))
                    {
                        // Build a Polygon consisting of the Swath Positions at
                        // the Start and Stop points of the Acquisition
                        Polygon oSegStart = getSwathPosition(oVisSeg.start);
                        Polygon oSegStop = getSwathPosition(oVisSeg.stop);

                        // Add the Stop Polygon to the Start Polygon to stitch 
                        // the two together and create the Segment
                        oSegStart.add(oSegStop);

                        // Translate the Visibility Start and Stop times from
                        // the segment to Calendar objects
                        Calendar oAcqStart = translateOrbitTime(oVisSeg.start);
                        Calendar oAcqEnd = translateOrbitTime(oVisSeg.stop);

                        int id = iSegment + 1;

                        String message = "";
                        int percentCompletion = 0;
                        
                        Status status = new Status("" + id, 
                                percentCompletion, 
                                message, 
                                Calendar.getInstance().getTime(), 
                                Calendar.getInstance().getTime());

                        // Create the Segment object that contains all of the 
                        // return information
                        Segment oIndSeg = new Segment("" + id, oSegStart,
                                oVisSeg.start.orbit, oVisSeg.stop.orbit, status,
                                oAcqStart, oAcqEnd, 100.0, null);                        

                        m_VisCoverage.add(oIndSeg);

                    }
                }

                bResult = true;
            }
				
            return bResult;
	}
	
	public List<Segment> getVisibilityCoverage()
	{
            return m_VisCoverage;
	}
        
        private Calendar translateOrbitTime(ANXTime oReqTime) throws CfiError
        {
            // Create the Calendar object to be returned
            Calendar oCalTime = null;
            
            // return null if the OrbitId object has not be initialised
            if (m_orbitID != null)
            {
                // Create a Time Of interest object and a ANXTime object 
                // initialised to the values of the received ANXTime object.
                // (Required as ANXTime functions throw an AccessException when
                // attempted to be called from the visibility Segment member
                // variables)
                TimeOfInt oTimeOI = new TimeOfInt();
                ANXTime oOrbitTime = new ANXTime(m_orbitID,
                                    oReqTime.orbit, oReqTime.seconds,
                                    oReqTime.microseconds);

                // Set the TimeOfInterest Start Time to the UTC time corresponding
                // to the ANXTime received
                oTimeOI.setStartTime(oOrbitTime.orbitToTime(XOCFI_TIME_UTC));

                // Create a Calendar object and set the time using the TimeOfInt
                // object
                oCalTime = Calendar.getInstance();
                oCalTime.setTime(oTimeOI.getStartTimeDate());
            }
            
            return oCalTime;
        }
	
	private Polygon getSwathPosition(ANXTime oReqTime) throws CfiError
	{
            Polygon oCoverage = new Polygon(); 
            
            // Check that the required member variables have been initialised
            if ( (m_swathObj != null) && (m_stfFile != null) )
            {
                // Get the Geodetic coordinates of the swath at the required
                // time
                Vector<Geodetic> vGCoord = m_swathObj.getPos(m_stfFile, oReqTime);

                // For each element in the returned Vector, create a new Point
                // object from the Geodetic Latitude, Longitude and Altitude and
                // add it to the Polygon
                for (int iSwathPoint = 0; iSwathPoint < vGCoord.size(); iSwathPoint++)
                {
                    Point oNewVisPnt = new Point(vGCoord.elementAt(iSwathPoint).lon, 
                            vGCoord.elementAt(iSwathPoint).lat, 
                            vGCoord.elementAt(iSwathPoint).alt); 

                    oCoverage.addNewPoint(oNewVisPnt);
                }
            }
		
            return oCoverage;
	}
	
	private boolean checkSegmentTime(VisibilitySegment oVisSeg) throws CfiError
	{
            boolean bGoodVisSt = false;
            boolean bGoodVisEn = false;
                
            // -------- Check that the Segment Start time is not before the ToI Start Time --------
            // If the Visibility Orbit Number is greater than the ToI Start Orbit Number the Segment is valid
            if (oVisSeg.start.orbit > m_OrbitRange.start.orbit)
            {
                bGoodVisSt = true;
            }
            // If the Visibility Orbit Number is equal to the ToI Start Orbit Number the Segment MAY be valid
            else if (oVisSeg.start.orbit == m_OrbitRange.start.orbit)
            {
                // If the Visibility Seconds are greater than the ToI Start Orbit Seconds then the Segment is valid
		if (oVisSeg.start.seconds > m_OrbitRange.start.seconds)
		{
                    bGoodVisSt = true;
		}
		// If the Visibility Seconds are equal to the ToI Start Orbit Seconds then the Segment MAY be valid
		else if (oVisSeg.start.seconds == m_OrbitRange.start.seconds)
		{
                    // If the Visibility Microseconds are greater than the ToI Start Orbit Microseconds then the Segment is valid
                    if (oVisSeg.start.microseconds > m_OrbitRange.start.microseconds)
                    {
                        bGoodVisSt = true;
                    }
		}
            }

            // -------- Check that the Segment End time is not after the ToI End Time --------
            // If the Visibility Orbit Number is less than the ToI End Orbit Number the Segment is valid
            if (oVisSeg.stop.orbit < m_OrbitRange.stop.orbit)
            {
                bGoodVisEn = true;
            }
            // If the Visibility Orbit Number is equal to the ToI End Orbit Number the Segment MAY be valid
            else if (oVisSeg.stop.orbit == m_OrbitRange.stop.orbit)
            {
                // If the Visibility Seconds are less than the ToI End Orbit Seconds then the Segment is valid
		if (oVisSeg.stop.seconds < m_OrbitRange.stop.seconds)
		{
                    bGoodVisEn = true;
		}
                // If the Visibility Seconds are equal to the ToI End Orbit Seconds then the Segment MAY be valid
		else if (oVisSeg.stop.seconds == m_OrbitRange.stop.seconds)
		{
                    // If the Visibility Microseconds are less than the ToI End Orbit Microseconds then the Segment is valid
                    if (oVisSeg.stop.microseconds < m_OrbitRange.stop.microseconds)
                    {
                        bGoodVisEn = true;
                    }
                }
            }

            return (bGoodVisEn && bGoodVisSt);
	}
                
        public void readGndStationFile ( ) throws CfiError
        {
            // Read the Ground Station File to extract the available stations
            StationFile gsFile = new StationFile( m_gndStFileLocation + m_sGndStFilename );
            gsFile.read();
            
            // Initialise the class member variables for the file contents
            m_vStationId = new Vector<String>();
            m_vStationName = new Vector<String>();
            m_vAoSEl = new double[gsFile.stationList.size()];
            m_vLoSEl = new double[gsFile.stationList.size()];
            m_vMask = new long[gsFile.stationList.size()];

            // Loop through each station in the file and extract the relevant information
            for (int iStation = 0; iStation < m_vAoSEl.length; iStation++)
            {
                // Get Station ID and Station Name
                m_vStationId.add(gsFile.stationList.get(iStation).stationId);
                m_vStationName.add(gsFile.stationList.get(iStation).descriptor);

                // Set the Minimum AOS and LOS Elevation values as the Maximum
                // Mask elevation for the Ground Station
                int iMaxIndex = Utilities.findMaxValue(
                        gsFile.stationList.get(iStation).elevation, 
                        (int)gsFile.stationList.get(iStation).numMaskPt);
                m_vAoSEl[iStation] = gsFile.stationList.get(iStation).elevation[iMaxIndex];
                m_vLoSEl[iStation] = gsFile.stationList.get(iStation).elevation[iMaxIndex];

                m_vMask[iStation] = EnumVisibility.XVCFI_COMBINE;
            }
            
            gsFile = null;
                        
        }
       
        private VisibilityList calculateStationVis(long startOrbit, long stopOrbit) throws CfiError
        {           
            // Initialise the class member variables used to pass the station
            // information
            m_vGndStationListId = new ArrayList<String>();
            m_vGndStationListName = new ArrayList<String>();
                    
            // Overall Visibilty Vector for all Ground Stations
            Vector<VisibilitySegment> oVisSeg = new Vector<VisibilitySegment>();
                        
            // Loop through each station that was extracted from the Station File
            for (int iStnIndex = 0; iStnIndex < m_vStationId.size(); iStnIndex++)
            {                
                // Call the EOCFI function to analyse station visibility
                VisibilityList oVisList = m_swathObj.stationVisTime(
                        startOrbit, stopOrbit, 
                        m_vStationId.get(iStnIndex), m_gndStFileLocation + m_sGndStFilename, 
                        m_vMask[iStnIndex], m_vAoSEl[iStnIndex], m_vLoSEl[iStnIndex],
                        m_dMinVisDuration);
                
                // Copy the segments onto the end of the overall Visibilty Vector
                oVisSeg.addAll(oVisList.segments);
                
                // Loop through all the segments in the stations visibility list
                // and add the station ID and name to the member variables for 
                // use later
                for (int iSegIndex = 0; iSegIndex < oVisList.segments.size(); iSegIndex++)
                {
                    m_vGndStationListId.add(m_vStationId.get(iStnIndex));
                    m_vGndStationListName.add(m_vStationName.get(iStnIndex));
                }
            }
            
            return new VisibilityList(oVisSeg);
        }
        
        public List<SegmentVisGS> calculateGndStationVis(long startOrbit, 
                long stopOrbit, ANXTime oStartPoint) throws CfiError
        {
            // Create the Ground Segment Visibility object to be returned
            // (null if the Swath is not initialised correctly)
            List<SegmentVisGS> oGndStVis = null;
                    
            if (m_swathObj != null)
            {
                oGndStVis = new ArrayList<SegmentVisGS>();
                
                // Assess the visibility of the Ground Station between the 
                // received times
                VisibilityList oStVis = calculateStationVis(startOrbit, stopOrbit);
                
                // If the returned List contains visibility segments, populate
                // the Segment Vis object with the required information
                for (int iSegIndex = 0; iSegIndex < oStVis.segments.size(); iSegIndex++)
                {                
                    // Test if the Visibility times of the segment are valid
                    // compared to the received download start point
                    if (validateGndStationSeg(oStVis.segments.get(iSegIndex),
                            oStartPoint))
                    {
                        // Translate the visibility start and stop times to Calendar
                        // objects                    
                        Calendar oStartTime = translateOrbitTime(
                                oStVis.segments.get(iSegIndex).start);
                        Calendar oStopTime = translateOrbitTime(
                                oStVis.segments.get(iSegIndex).stop);

                        // Create the returned object from this information
                       oGndStVis.add( new SegmentVisGS(
                                m_vGndStationListId.get(iSegIndex),
                                m_vGndStationListName.get(iSegIndex),
                                oStartTime,
                                oStopTime
                                ));
                    }
                    
                }
 
            }
 
            return oGndStVis;                
        }
        
        private boolean validateGndStationSeg(VisibilitySegment oVisSeg,
                            ANXTime oStartPoint) throws CfiError
        {
            boolean bGoodVis = false;
            
            // -------- Check that the Segment End time is not after the ToI End Time --------
            // If the Visibility Orbit Number is greater than the StartPoint Orbit Number the Segment is valid
            if (oVisSeg.stop.orbit > oStartPoint.orbit)
            {
                bGoodVis = true;
            }
            // If the Visibility Orbit Number is equal to the StartPoint Orbit Number the Segment MAY be valid
            else if (oVisSeg.stop.orbit == oStartPoint.orbit)
            {
                // If the Visibility Seconds are greater than the StartPoint Orbit Seconds then the Segment is valid
		if (oVisSeg.stop.seconds > oStartPoint.seconds)
		{
                    bGoodVis = true;
		}
                // If the Visibility Seconds are equal to the StartPoint Orbit Seconds then the Segment MAY be valid
		else if (oVisSeg.stop.seconds == oStartPoint.seconds)
		{
                    // If the Visibility Microseconds are greater than the StartPoint Orbit Microseconds then the Segment is valid
                    if (oVisSeg.stop.microseconds > oStartPoint.microseconds)
                    {
                        bGoodVis = true;
                    }
                }
            }

            return bGoodVis;
        }
	
	public void setFilePath( FileType eFileType, String sFileLocation )
	{
            // If the input string does not end with a final folder separator, add one
            if (sFileLocation.endsWith(File.separator) == false)
            {
                sFileLocation = sFileLocation + File.separator;
            }
				
            // Set the member variable to the path string
            switch (eFileType) {
                case SWATH_DEFINITION_FILE:
                    m_sdfFileLocation = sFileLocation;
                    break;
			                            
                case GND_STATION_FILE:
                    m_gndStFileLocation = sFileLocation;
                    break;
            }
		
	}
	
	// Return member variable for the file path for initial file checking
	public String getFilePath( FileType eFileType )
	{
            String sFileLocation = "";
		
            switch (eFileType) {
                case SWATH_DEFINITION_FILE:
                    sFileLocation = m_sdfFileLocation;
                    break;
                    
                case GND_STATION_FILE:
                    sFileLocation = m_gndStFileLocation;
                    break;
            }

            return sFileLocation;
	}
        
        public void setGndStationFilename( String sFilename )
        {
            m_sGndStFilename = sFilename;
        }
		
	private boolean setOrbitID( OrbitId oReqOrbitId ) throws CfiError
	{
            boolean bResult = false;
			
            // Test if the received OrbitId object is not null
            if (oReqOrbitId != null)
            {			
                // If the member variable object is null, call the copy constructor
		if (m_orbitID == null)
		{
                    m_orbitID = new OrbitId(oReqOrbitId);
		}
		// Otherwise call the copy operator
		else
		{
                    m_orbitID.eq(oReqOrbitId);
		}
			
		// Return true
		bResult = true;
            }
		
            return bResult;
	}
	
	public boolean setOrbitRange(TimeSegment oReqOrbitRange)
	{
            boolean bResult = false;
		
            // Test if the received TimeSegment object is not null
            if (oReqOrbitRange != null)
            {
                // If the member variable object is null, if so instantiate it
		if (m_OrbitRange == null)
		{
                    m_OrbitRange = new TimeSegment();
		}
			
		// Then assign the member variable the value of the received TimeSegment 
		m_OrbitRange = oReqOrbitRange;
			
		// Return true
		bResult = true;
            }
		
            return bResult;
	}
        
        public void setMinVisDuration(double dMinVisDuration)
        {
            m_dMinVisDuration = dMinVisDuration;
        }

}
