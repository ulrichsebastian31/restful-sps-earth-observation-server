/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EoCfiHandler.java
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
 *   File Name							:	EoCfiHandler.java
 *   File Type							:	Source Code
 *   Description						:	This class handles the various other functional 
 *   										classes for allowing access to the EOCFI functions
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

import net.eads.astrium.hmas.util.structures.tasking.*;
import net.eads.astrium.hmas.util.structures.tasking.geometries.*;
import net.eads.astrium.hmas.util.structures.*;
import net.eads.astrium.hmas.util.DateHandler;
import EECFI.CfiError;
import EECFI.Geodetic;
import EECFI.ZonePoint;
import EECFI.ZoneRec;
import EECFI.EnumDataHandling;
import EECFI.ANXTime;
import static EECFI.EnumLib.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import java.util.Date;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EOCFIHandling;
import net.eads.astrium.hmas.util.Algorithms;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class EoCfiHandler implements EOCFIHandling {
    
    
    /*
     * -----------------------------------------------------------
     * ----------- EoCfiHandler Class Member Variables ----------- 
     * -----------------------------------------------------------
     */
    
    // PUBLIC
    public static final String DREAM_FAS_CONF_FOLDER = 
            System.getProperty("user.home") + File.separator + ".dream" + 
            File.separator + "fas"  + File.separator;
    
    // PROTECTED
    protected enum FileType {
        SAT_CONFIG_FILE,
        ORBIT_SCENARIO_FILE,
	SWATH_DEFINITION_FILE,
        GND_STATION_FILE,
        COMMS_SWATH_DEF_FILE
    }
        
    // PRIVATE
    private static final double NULL_DOUBLE = 0.0;
    private static final String EMPTY_STRING = "";
    
    private String satelliteId;             // Satellite Identifier initialised at construction
    private OrbitDef m_OrbitDef;            // Orbit Definition class for access to EOCFI orbit calculations
    private SwathDef m_SwathDef;            // Swath Definition class for access to EOCFI swath calculations
    private TimeOfInt m_VisToI;             // Class for storing the Time of Interest Tasking parameter
    private ZoneRec m_VisAoI;               // Zone Rec Object for storing the Area of Interest Tasking parameter
	
    private double m_dPosIncrement;         // Time increment in Days between orbital location calculations
    private List<Point> m_PositionDef;      // List Points for storing Position results of the satellite orbit
	
    private String m_sFileDir;              // Location to the Directory Structure containing all EOCFI files
    private String m_sSatFile;              // File Path and name of the Satellite Configuration File
    private String m_sOsfFilePath;          // File Path to read the OSF File from

    
    /*
     * -----------------------------------------------------------
     * ---------------- EoCfiHandler CONSTRUCTOR -----------------
     * -----------------------------------------------------------
     */
    // One passed parameter - String representing the Satellilte Id
    public EoCfiHandler(String satelliteId) throws EoCfiHndlrError
    {
        handleLogMessage("Initialising for use with EOCFI library - Version " +
                getCfiLibraryVersion());
        
        // Path to the directory containing all the EOCFI Files
        m_sFileDir = DREAM_FAS_CONF_FOLDER;
        if (m_sFileDir.endsWith(File.separator) == false)
        {
            m_sFileDir = m_sFileDir + File.separator;
        }

        // Initialise the OrbitDef class to the correct Satellite
        this.satelliteId = satelliteId;
        initialiseSatellite( satelliteId );

        m_SwathDef = null;                          // Swath Definition class
        m_VisToI = null;				// Time Of Interest pointer set by function call
        m_VisAoI = null;				// Area of Interest pointer set by function call
        m_dPosIncrement = 0.0;
        m_PositionDef = new ArrayList<Point>();     // Instantiate the Position Definition List
    }
    
    /*
     * -----------------------------------------------------------
     * ---------- EoCfiHandler PUBLIC Member Functions ----------- 
     * -----------------------------------------------------------
     */
    
    @Override
    public List<Point> getOrbitPropagation(
            Date oStartTime, 
            Date oEndTime, 
            double dTimeIncrement ) throws EoCfiHndlrError
    {     
        handleLogMessage("Performing getOrbitPropagation request");
        
        // Set the time increment between each Orbital Position calculation,
        // variable used in days but passed parameter received in seconds
        m_dPosIncrement = dTimeIncrement / TimeOfInt.SECONDS_IN_A_DAY;
        
        // Build the Path to the OSF file now we know which satellite
        m_sOsfFilePath = findFilePath( FileType.ORBIT_SCENARIO_FILE, 
                satelliteId, "" );
                
        // Check the validity of the start and end date objects and if correct, 
        // assign them to the member variable representing ToI
        setTimeOfInterest( oStartTime, oEndTime );
        
        // Find the file that is valid for the ToI
        String osfFile = findValidFile( m_sOsfFilePath, null );
        
        // Call function to read the OSF filename and calculate the Satellites
        // position during the ToI
        calculateSatellitePosition( osfFile );
        
        handleLogMessage("getOrbitPropagation request Complete");
        
        // Member variable that represents the List<Points> for the Satellites
        // orbit propagation
        return m_PositionDef;
    }	
    
    @Override
    public List<Segment> getOPTFeasibility(
            String sensorId, 
            OPTTaskingParameters optParam,
            double dMinVisDuration) throws EoCfiHndlrError
    {        
       
        handleLogMessage("Performing getOPTFeasibility request");
        
        // Build the Path to the OSF, SDF and STF file now we know which satellite
        m_sOsfFilePath = findFilePath( FileType.ORBIT_SCENARIO_FILE, 
                satelliteId, sensorId );
        String sensorSdfFilePath = findFilePath( FileType.SWATH_DEFINITION_FILE, 
                satelliteId, sensorId );
        
        // Set the Area Of Interest to that received in the function call
        if (optParam.getCoordinates() instanceof Polygon)
        {
            setAreaOfInterestPolygon( (Polygon)optParam.getCoordinates() );
        }
        else if (optParam.getCoordinates() instanceof Circle)
        {
            setAreaOfInterestCircle( (Circle)optParam.getCoordinates() );
        }
                        
        // Extract the List of Times for the individual feasibility requests
        List<TimePeriod> oToIList = optParam.getTimes();
        
        // Create the return Segment List
        List<Segment> oVisList = new ArrayList<>();
        
        for (int iInstrIndex = 0; iInstrIndex < optParam.getInstrumentModes().size();
                iInstrIndex++)
        {
            String sensorMode = optParam.getInstrumentModes().get(iInstrIndex);
        
            handleLogMessage("Performing Feasibility Request for Instrument = " 
                        + sensorMode);
        
            // For each TimePeriod in the ToI List, perform a feasibility request
            // for the start and end times
            for (int iTimeIndex = 0; iTimeIndex < oToIList.size(); iTimeIndex++)
            {
                handleLogMessage("Performing Feasibility Request for Time Of Interest Period " 
                        + (iTimeIndex + 1));

                // Perform Feasibility Request and add the return Segment list to the
                // overall list
                oVisList.addAll( 
                    performFeasibilityRequest(
                        oToIList.get(iTimeIndex).getBegin(), 
                        oToIList.get(iTimeIndex).getEnd(),
                        sensorSdfFilePath,
                        sensorMode,
                        dMinVisDuration)  
                    );

            }
        }
        
        handleLogMessage("getOPTFeasibility request Complete");
       
        return oVisList;
    }
    
    @Override
    public List<Segment> getSARFeasibility(
            String sensorId, 
            SARTaskingParameters sarParam,
            double dMinVisDuration) throws EoCfiHndlrError
    {
        handleLogMessage("Performing getSARFeasibility request");
        
        // Build the Path to the OSF, SDF and STF file now we know which satellite
        m_sOsfFilePath = findFilePath( FileType.ORBIT_SCENARIO_FILE, 
                satelliteId, sensorId );
        String sensorSdfFilePath = findFilePath( FileType.SWATH_DEFINITION_FILE, 
                satelliteId, sensorId );
        
        // Set the Area Of Interest to that received in the function call
        if (sarParam.getCoordinates() instanceof Polygon)
        {
            setAreaOfInterestPolygon( (Polygon)sarParam.getCoordinates() );
        }
        else if (sarParam.getCoordinates() instanceof Circle)
        {
            setAreaOfInterestCircle( (Circle)sarParam.getCoordinates() );
        }
                        
        // Extract the List of Times for the individual feasibility requests
        List<TimePeriod> oToIList = sarParam.getTimes();
        
        // Create the return Segment List
        List<Segment> oVisList = new ArrayList<>();
        
        for (int iInstrIndex = 0; iInstrIndex < sarParam.getInstrumentModes().size();
                iInstrIndex++)
        {
            try 
            {
                
                String sensorMode = sarParam.getInstrumentModes().get(iInstrIndex);

                handleLogMessage("Performing Feasibility Request for Instrument Modes = " 
                            + sensorMode);

                // For each TimePeriod in the ToI List, perform a feasibility request
                // for the start and end times
                for (int iTimeIndex = 0; iTimeIndex < oToIList.size(); iTimeIndex++)
                {
                    handleLogMessage("Performing Feasibility Request for Time Of Interest Period " 
                            + (iTimeIndex + 1));

                    // Perform Feasibility Request and add the return Segment list to the
                    // overall list
                    oVisList.addAll( 
                         performFeasibilityRequest(
                            oToIList.get(iTimeIndex).getBegin(), 
                            oToIList.get(iTimeIndex).getEnd(),
                            sensorSdfFilePath,
                            sensorMode,
                            dMinVisDuration)
                        );
                }
            }
            catch (EoCfiHndlrError e) 
            {
                // If an error is encountered for this instrument, unless it is the last in
                // the list, continue to process the next instrument as this may provide
                // good results
                if (iInstrIndex == (sarParam.getInstrumentModes().size() - 1))
                {
                    if (oVisList.isEmpty())
                    {
                        throw e;
                    }
                }
            }
        }
        
        handleLogMessage("getSARFeasibility request Complete");
        
        return oVisList;
    }
    
    @Override
    public List<SegmentVisGS> getNextStationDownlink(
            Date oAcqEndTime, 
            long iOrbitNum,
            double dMinVisDuration) throws EoCfiHndlrError
    {
        handleLogMessage("Performing getNextStationDownlink request");
        
        List<SegmentVisGS> vGndStVis = null;
        
        try
        {
            if (m_SwathDef == null)
            {
                handleLogMessage("Initialising EOCFI Ground Station pre-requisites ...");

                // Build the file path to the Ground Station File and SDF file
                // that defines the Sub-Satellite Point
                String gndStFilePath = findFilePath( FileType.GND_STATION_FILE, 
                    satelliteId, "" );
                String sspSdfFilePath = findFilePath( FileType.COMMS_SWATH_DEF_FILE,
                    satelliteId, "" );

                // Find the correct files from these directories
                String sGndStFile = findValidFile( gndStFilePath, null );
                handleLogMessage("Found Ground Station File = " + sGndStFile);

                String sSspSdfFile = findValidFile( sspSdfFilePath, null );
                handleLogMessage("Found TmTc Antenna SDF File = " + sSspSdfFile);

                // Initialise the Swath Definition object for the SSP SDF
                initialSwathCalc( sspSdfFilePath, sSspSdfFile,
                        dMinVisDuration);
                
                // Pass the retrieved directories and filenames to the SwathDef class
                m_SwathDef.setFilePath(FileType.GND_STATION_FILE, gndStFilePath);
                m_SwathDef.setGndStationFilename(sGndStFile);

                // Initialise the objects for the Ground Station calculation
                m_SwathDef.readGndStationFile();
            }
            
            // Add the minimum visibility duration to the Acquisition end time
            // so as to make sure segments are returned that are too short
            Calendar oStartTime = Calendar.getInstance();
            oStartTime.setTime(oAcqEndTime);
            oStartTime.add(Calendar.SECOND, (int)Math.round(dMinVisDuration));
            
            // Translate the Date object received to an Orbit number
            ANXTime oStartPoint = translateTimeToOrbit(oStartTime.getTime());
            
            // Calculate the Visibility of the Ground Stations for one orbit
            // after the Start orbit
            handleLogMessage("Calculating Ground Station Visibility ...");
            vGndStVis = m_SwathDef.calculateGndStationVis(
                    iOrbitNum, iOrbitNum, oStartPoint);
            
            // Sort the Ground Station Visibility Segments to be in start date order
            vGndStVis = Algorithms.triParInsertionSVG(vGndStVis);
            
            handleLogMessage("getNextStationDownlink request Complete");
            
        }
        catch(CfiError cfiError)
        {
            handleCfiError(cfiError);
        }
        
        return vGndStVis;
    }
    
    @Override
    public Date getOrbitBeginTime(long orbitNumber) throws EoCfiHndlrError
    {
        handleLogMessage("Performing getOrbitBeginTime request for Orbit Number " +
                orbitNumber);
        
        Date oOrbTime = null;
        
        // Check that the Orbit Definition has been initialised
        if (m_OrbitDef == null)
        {
            handleNonCfiError("Orbit Not Initialised");
        }
        
        try
        {
            // Calculate the Ascending Node crossing time for the start of the
            // received orbit
            double dTime = m_OrbitDef.convertOrbitToTime(orbitNumber);
            
            // Use the TimeOfInt class to convert the returned time from 
            // MJD2000 to a java.util.Date object
            TimeOfInt oTimeConv = new TimeOfInt();
            oTimeConv.setStartTime(dTime);
            
            oOrbTime = oTimeConv.getStartTimeDate();
            
            handleLogMessage("getOrbitBeginTime request Complete - Orbit Start Time = " +
                    oTimeConv.getStartTimeStr());
        }
        catch(CfiError cfiError)
        {
            handleCfiError(cfiError);
        }
        
        return oOrbTime;
    }
    
    /*
     * -----------------------------------------------------------
     * ---------- EoCfiHandler PRIVATE Member Functions ---------- 
     * -----------------------------------------------------------
     */
    
    // Initialise the OrbitDef member variable for the desired Satellite
    private void initialiseSatellite( String satelliteId ) throws EoCfiHndlrError
    {
        handleLogMessage("Configuring for Satellite = " + satelliteId);
                
        try
        {
            // Find the correct file path for the Satellite Configuration File
            String sSATFilePath = findFilePath( FileType.SAT_CONFIG_FILE, 
                        satelliteId, "" );
            
            // Find the latest Satellite Configuration File present in that path
            String sSatFile = findCfiFile( sSATFilePath );
            
            // If there is a Satellite Configuration file present in the 
            // Sat filepath, instantiate the Orbit using a Satellite Config File
            if (!sSatFile.equals(""))
            {
                handleLogMessage("Initialising Satellite using SAT CFG File = " + sSatFile);
                
                m_sSatFile = sSATFilePath + File.separator + sSatFile;
                    
                m_OrbitDef = new OrbitDef(m_sSatFile);		
            }
            // Otherwise use the Generic Satellite Identifier
            else
            {
                handleLogMessage("Initialising Satellite using GENERIC Satellite Flag "
                        + "- Note State Vector checks are disabled");
                
                m_OrbitDef = new OrbitDef(XLCFI_SAT_GENERIC);
            }
        }
        catch(CfiError cfiError)
        {
            handleCfiError(cfiError);
        }
    }
	
    // Set the Time Of Interest member variable using a Start and End java.util.Date object
    private void setTimeOfInterest( Date oStartDate, Date oEndDate ) throws EoCfiHndlrError
    {
        // Check to ensure that the Start and End Date objects are valid and will not cause an error later
        checkValidToI(oStartDate, oEndDate);

        // If the ToI member variable has not already been instantiated then do so and set the start and end times
        if (m_VisToI == null)
        {
            m_VisToI = new TimeOfInt();
        }
        m_VisToI.setStartTime(oStartDate);
        m_VisToI.setEndTime(oEndDate);
        
        handleLogMessage("Received Time Of Interest (ToI). Start time = " + 
                m_VisToI.getStartTimeStr() + ", End time = " + 
                m_VisToI.getEndTimeStr());
    }
    
    private void checkValidToI( Date oStartDate, Date oEndDate ) throws EoCfiHndlrError
    {        
        // Throw an Error if the Start Date object is null
        if (oStartDate == null)
        {
            handleNonCfiError("Invalid Time of Interest. Null Start Time");
        }
        
        // Throw an Error if the End Date object is null
        if (oEndDate == null)
        {
            handleNonCfiError("Invalid Time of Interest. Null End Time");
        }
        
        // Throw an Error if the End Date is not Greater than the Start Date
        if (oStartDate.getTime() >= oEndDate.getTime())
        {
                        
            // Format the Error message to log both Start and End Times
            String sTimeErr = "Invalid Time of Interest. Start Time = " + 
                    DateHandler.formatDate(oStartDate) + ", End Time = " + 
                    DateHandler.formatDate(oEndDate);
                    		
            // Throw the error
            handleNonCfiError(sTimeErr);
        }
    }
    
    private void checkTimeIncrement() throws EoCfiHndlrError
    {
        final int NUMBER_OF_POINTS_PER_ORBIT = 20;
        
        double dIncrement = m_dPosIncrement * TimeOfInt.SECONDS_IN_A_DAY;
        
        if ( dIncrement < 0.1 )
        {
            try
            {
                handleLogMessage("Received Invalid Orbit Increment Value (" + dIncrement + 
                        "s) - Calculating new value based on " + 
                        NUMBER_OF_POINTS_PER_ORBIT + " positions per Orbit ...");
                
                if (m_OrbitDef != null)
                {
                    double dOrbitPeriod = m_OrbitDef.getOrbitPeriod();
                    
                    if (dOrbitPeriod != 0.0)
                    {
                        m_dPosIncrement = (dOrbitPeriod / TimeOfInt.SECONDS_IN_A_DAY) / NUMBER_OF_POINTS_PER_ORBIT;
                        
                        handleLogMessage("Orbit Period = " + dOrbitPeriod + 
                                " (s). Therefore new increment = " + 
                                (m_dPosIncrement * TimeOfInt.SECONDS_IN_A_DAY) + " (s)");
                    }
                    else
                    {
                        // Format the Error message to log both Start and End Times
                        String sOrbitErr = "Unable to Calculate Default Increment";
                    		
                        // Throw the error
                        handleNonCfiError(sOrbitErr);
                    }
                }
            }
            catch(CfiError cfiError)
            {
		handleCfiError(cfiError);
            }
        }
    }
	
    // Set the Area Of Interest member variable using a received Polygon of Point objects consisting of Lat and Long
    private void setAreaOfInterestPolygon( Polygon oAreaOfInt )
    {
        // Create the vector of EECFI ZonePoint objects that will consist of the Latitude and Longitude values
        Vector<ZonePoint> oAoIPoints = new Vector<ZonePoint>();

        // Extract the Point objects from the received Polygon, loop through each one, extracting the Lat and Long values,
        // use these to instantiate a ZonePoint and assign it to the end of the Vector
        List<Point> visPoints = oAreaOfInt.getPoints();
        for (int i = 0; i < visPoints.size(); i++)
        {
            oAoIPoints.addElement( new ZonePoint(visPoints.get(i).getLongitude(), visPoints.get(i).getLatitude()) );
        }

        // Create the ZoneRec member variable representing the Area of Interest using this new Vector of ZonePoints
        m_VisAoI = new ZoneRec("User Area Of Interest", EMPTY_STRING, 
                EMPTY_STRING, EMPTY_STRING, EnumDataHandling.XDCFI_POLYGON, 
                EnumDataHandling.XDCFI_RECTANGULAR, NULL_DOUBLE, oAoIPoints);

        handleLogMessage("Received Area Of Interest (AoI) - Polygon.");
    }
    
    private void setAreaOfInterestCircle( Circle oAreaOfInt )
    {
        // Create the vector of EECFI ZonePoint objects that will consist of the Latitude and Longitude values
        Vector<ZonePoint> oAoIPoints = new Vector<ZonePoint>();
        Point circleCentre = oAreaOfInt.getCenter();
        double circleRadius = oAreaOfInt.getRadius();
            
        oAoIPoints.addElement( new ZonePoint(circleCentre.getLongitude(), circleCentre.getLatitude()) );   
        
        // Create the ZoneRec member variable representing the Area of Interest using this new Vector of ZonePoints
        m_VisAoI = new ZoneRec("User Area Of Interest", EMPTY_STRING, 
                EMPTY_STRING, EMPTY_STRING, EnumDataHandling.XDCFI_CIRCLE, 
                EnumDataHandling.XDCFI_RECTANGULAR, (circleRadius * 2), oAoIPoints);

        handleLogMessage("Received Area Of Interest (AoI) - Circle.");
    }

    private List<Segment> performFeasibilityRequest(
            Date oStartTime, 
            Date oEndTime,
            String sensorSdfFilePath,
            String sensorMode,
            double dMinVisDuration) throws EoCfiHndlrError
    {
        // Check the validity of the start and end date objects and if correct, 
        // assign them to the member variable representing ToI
        setTimeOfInterest( oStartTime, oEndTime );
        
        // Find the OSF and SDF files that are valid for the ToI
        String osfFile = findValidFile( m_sOsfFilePath, null );
        handleLogMessage("Found Applicable OSF File = " + osfFile);
        
        String sensorSdfFile = findValidFile( sensorSdfFilePath, sensorMode );
        handleLogMessage("Found SDF File = " + sensorSdfFile);
                        
        // Calculate the Feasibility of the required task
        List<Segment> oVisList = calculateFeasibility( osfFile, 
                sensorSdfFilePath, 
                sensorSdfFile,
                dMinVisDuration);
        
        // Loop over the retrieved Visibility Segment list and insert the 
        // sensor mode string to each segment
        for (int iSegIndex = 0; iSegIndex < oVisList.size(); iSegIndex++)
        {
            oVisList.get(iSegIndex).setInstrumentMode(sensorMode);
        }
        
        return oVisList;
    }

        
    // Build the path to a file based on its type
    private String findFilePath( FileType eFileType, String sSatString,
            String sSensorString)
    {
        String sFilePath = "";
        String sFileType = "";
            
        switch (eFileType) {
            case SAT_CONFIG_FILE:
                sFileType = "satellite";
                break;
                    
            case ORBIT_SCENARIO_FILE:
                sFileType = "OSF";
                break;
			
            case SWATH_DEFINITION_FILE:
                sFileType = "sensors" + File.separator + sSensorString + File.separator + "SDF";
                break;	
                    
            case GND_STATION_FILE:
                sFileType = "Ground Station";
                break;
                
            case COMMS_SWATH_DEF_FILE:
                sFileType = "antenna";
        }
            
        sFilePath = m_sFileDir + sSatString + File.separator + sFileType;
            
        return sFilePath;
    }
        
    // Find the file within the input directory that is valid for the 
    // required ToI
    private String findValidFile( String sFilePath, String sInstrument ) throws EoCfiHndlrError
    {           
        // Create String variable that will be returned
        String sFilename = "";
        
        // Constants representing the location of the Start and End Validity 
        // Date from the end of the filename
        final int START_DATE_LOCATION = 3;
        final int END_DATE_LOCATION = 2;
        final int INSTRUMENT_MODE_LOCATION = 4;
            
        // Test the passed directory to ensure that it exists
        if ( !Utilities.inputDirTest( sFilePath ) )
        {
            handleNonCfiError("Error Finding Input File in " + sFilePath + 
                    " : Directory cannot be found");
        }
            
        // Test that the ToI object has been set
        if ( m_VisToI == null )
        {
            handleNonCfiError("No ToI set to check file validity against");
        }
            
        // Create the File object that represents the Directory holding the 
        // potential input files
        File inputDir = new File( sFilePath );
            
        // Instantiate the variables required
        boolean bFileFound = false;             // Flag to indicate a valid file has been found
        int iFileCount = 0;                     // File Counter
        String[] sFileList = inputDir.list();   // Array of File names found in the Directory
        int iNumFiles = sFileList.length;       // The number of Files in the Directory
            
        // Loop over the files in the folder until either ...
        //  - a valid file is found or all the files have been checked
        while ((bFileFound == false) && (iFileCount < iNumFiles))
        {
            // Split the file by delimiter ("_")
            String[] sNameSeg = sFileList[iFileCount].split("_");
                
            // Check the resulting array has the minimum expected components
            if (sNameSeg.length > INSTRUMENT_MODE_LOCATION)
            {
                // The Validity Start Date is third from last
                // The Validity End Date is second from the last
                Date oValStart;
                Date oValEnd;
                
                try
                {
                    oValStart = DateHandler.parseEocfiDate(
                            sNameSeg[sNameSeg.length - START_DATE_LOCATION]);
                    oValEnd = DateHandler.parseEocfiDate(
                            sNameSeg[sNameSeg.length - END_DATE_LOCATION]);
                }
                catch (ParseException exc)
                {
                    oValStart = null;
                    oValEnd = null;
                }
                
                // If the instrument received value is not null ...
                if (sInstrument != null)
                {
                    // Check that the instrument name field of the filename matches
                    // the received value, if not discard the file and continue the search
                    if (!sNameSeg[sNameSeg.length - INSTRUMENT_MODE_LOCATION].equals(sInstrument))
                    {
                        oValStart = null;
                        oValEnd = null;
                    }
                }

                // If the resulting Date objects are not null then assess
                // them against the ToI
                if ( (oValStart != null) && (oValEnd != null) )
                {
                    // Test that the Validity Start is earlier than the ToI Start and
                    // that the Validity End is later than the ToI End
                    bFileFound = m_VisToI.validityPeriodTest(oValStart, oValEnd);
                    
                    // If the file is valid then search for the latest version
                    if (bFileFound)
                    {
                        sFilename = Utilities.findLatestFileVersion(sFilePath, 
                                sFileList[iFileCount].substring(0, 
                                sFileList[iFileCount].lastIndexOf("_"))
                                );
                    }
                }
            }
                
            // Increment the file counter to prepare for the next loop
            iFileCount++;
        }
            
        // If no file is found, throw an error
        if (!bFileFound)
        {
            handleNonCfiError("No Valid File for ToI can be found. Directory = " +
                    sFilePath + ", ToI Start = " + m_VisToI.getStartTimeStr() + 
                    ", ToI End = " + m_VisToI.getEndTimeStr());
        }
            
        // Otherwise, return the filename
        return sFilename;
    }
        
    // Find the file within the input directory that matches the correct format
    private String findCfiFile( String sFilePath ) throws EoCfiHndlrError
    {                        
        // Test the passed directory to ensure that it exists
        if ( !Utilities.inputDirTest( sFilePath ) )
        {
            handleNonCfiError("Error Finding Input File in " + sFilePath + 
                    " : Directory cannot be found");
        }
        
        String sFilename = Utilities.findLatestFileVersion(sFilePath, "*");
              
        return sFilename;
    }
	
	// ----------------------------------------------------------------------------------------------------
	// -------------------------- EoCfiHandler Functions for Orbit Calculations ---------------------------
	// ----------------------------------------------------------------------------------------------------
		
	// Calculate the Satellite position during the TOI
	private void calculateSatellitePosition( String sOsfFile ) throws EoCfiHndlrError
	{	
            // Initialise the Orbit ID using the data contained in an OSF File
            initialOrbitCalc(sOsfFile);
            
            // Check that the time increment between each Orbit Position calculation
            // is a valid number (assign a default in the case that it isnt)
            checkTimeIncrement();
		
            try
            {
                if (m_VisToI != null)
		{
                    // For each time increment betwee the Start and End Times defined in the ToI
                    for (double dReqTime = m_VisToI.getStartTime(); 
                            dReqTime < m_VisToI.getEndTime(); 
                            dReqTime += m_dPosIncrement)
                    {
                        // Get the Orbital position, extract the Long, Lat and Alt and create a new Point object
			// to be added to the Polygon member variable
			Geodetic oGeoPoint = m_OrbitDef.getOrbitalPosition(dReqTime);
                        
                        if (oGeoPoint == null)
                        {
                            handleNonCfiError("Error Retrieving Geodetic Coordinates for the Time = " +
                                    dReqTime + " days");
                        }
                        
			Point oNewVisPnt = new Point(oGeoPoint.lon, oGeoPoint.lat, oGeoPoint.alt); 
                        m_PositionDef.add(oNewVisPnt);
                    }
		}
                else
		{
                    handleNonCfiError("Satellite Position Request with no Time Of Interest correctly set");
		}
			
            }
            catch(CfiError cfiError)
            {
		handleCfiError(cfiError);
            }
		
	}
	
        private List<Segment> calculateFeasibility( String sOsfFile, String sSdfFilePath,
                String sSdfFile, double dMinVisDuration ) throws EoCfiHndlrError
	{	
            // Initialise the Orbit ID using the data contained in an OSF File
            handleLogMessage("Initialising Satellite Orbit from OSF ...");
            initialOrbitCalc(sOsfFile);
		
            // Calculate the First and Last points in the Satellites Orbit corresponding to the visibility period
            handleLogMessage("Calculating Range of Orbit Numbers for ToI ...");
            calculateRangeOfOrbits();

            // Initialise the Swath object by passing over the required fields from the Orbit ID and then create a
            // Swath Template File from the identified Swath Definition File
            handleLogMessage("Creating Swath Template File Object for the ToI ...");
            initialSwathCalc(sSdfFilePath, sSdfFile, dMinVisDuration);

            // Assess if there is visibility of the satellite from the Area of Interest within the identified range 
            // of orbits, if so retrieve the Geodetic co-ordinates of those points  
            List<Segment> oVisCoverage = calculateZoneVisibility(sSdfFile);
            
            // Clear the Swath Def member variable so it can be initialised again
            // with either the Sensor Swath or the SSP swath in the next function call
            m_SwathDef = null;
            
            return oVisCoverage;
	}
				
	private void initialOrbitCalc( String sOsfFile ) throws EoCfiHndlrError
	{
            if (m_OrbitDef == null)
            {
                // Throw an Error if not
                handleNonCfiError("No OrbitDef Object present at initialOrbitCalc call");
            }
            
            try
            {	
                // Set the file path where the OSF file should be stored
		m_OrbitDef.setFilePath(m_sOsfFilePath);
			
		// Test if the OSF file is Present in the correct Drive Location and is Readable
		if (!Utilities.inputFileTest(m_OrbitDef.getFilePath() + sOsfFile))
		{
                    // Throw an Error if not
                    handleNonCfiError("Non-Existant or Inaccessible OSF File - " + 
                            (m_OrbitDef.getFilePath() + sOsfFile));
		}
                
		// Initialise the required objects from the OSF File within this directory
		m_OrbitDef.initialiseFromOSFfile(sOsfFile);
            }
            catch(CfiError cfiError)
            {
		handleCfiError(cfiError);
            }	
	}
	
	private void calculateRangeOfOrbits() throws EoCfiHndlrError
	{
            try
            {			
                // Check that the OrbitID has been initialised
                if (m_OrbitDef == null)
                {
                    handleNonCfiError("No Orbit ID initialised for Orbit Range Calculation");
                }
                        
		// Check that the Time Of Interest has been set
		if (m_VisToI == null)
		{
                    handleNonCfiError("No Valid Time of Interest set for Orbit Range Calculation");
                    
		}
                
                // Pass the Visibility Start and End times to acquire a Range of Orbit Numbers
                m_OrbitDef.findOrbitRange(m_VisToI);  
	
            }
            catch(CfiError cfiError)
            {
     		handleCfiError(cfiError);
            }	
	}
	
	// ----------------------------------------------------------------------------------------------------
	// -------------------------- EoCfiHandler Functions for Swath Calculations ---------------------------
	// ----------------------------------------------------------------------------------------------------
			
	private void initialSwathCalc( String sSdfFilePath, String sSdfFile,
                double dMinVisDuration) throws EoCfiHndlrError
	{
            try
            {
                // If the input string does not end with a final folder separator, add one
                if (sSdfFilePath.endsWith(File.separator) == false)
                {
                    sSdfFilePath = sSdfFilePath + File.separator;
                }
			
		// Test if the SDF file is Present in the correct Drive Location and is Readable
                if (!Utilities.inputFileTest(sSdfFilePath + sSdfFile))
		{
                    handleNonCfiError("Non-Existant or Inaccessible SDF File" + 
                            (sSdfFilePath + sSdfFile));
		}
                
                // Test that the Orbit Definition object has been created
                if (m_OrbitDef == null)
                {
                    handleNonCfiError("Cannot initialise Instrument Swath without "
                            + "initialising Satellite Orbit");
                }
                
                // Initialise the Swath Definition object using the Orbit ID
                // object from the Orbit Definition and the SDF file
                m_SwathDef = new SwathDef(m_OrbitDef.getOrbitID(),
                        sSdfFilePath, sSdfFile);
							
		// Pass the Range of Orbit numbers corresponding to the visibility period to the SwathDef class
		if (!m_SwathDef.setOrbitRange(m_OrbitDef.getOrbitRange()))
		{
                    handleNonCfiError("Error Passing Orbit Range covering the Visibility Period " +
                            "to Swath Definition Class");
		}
                
                // Set the minimum Visibility Duration for the Image Acquisition
                m_SwathDef.setMinVisDuration(dMinVisDuration);
						
            }
            catch(CfiError cfiError)
            {
		handleCfiError(cfiError);
            }	
	}
    	
        private ANXTime translateTimeToOrbit( Date oStartTime ) throws EoCfiHndlrError
        {
            ANXTime oOrbitPoint = null;
            
            // Check that the Orbit Definition object has been initialised
            if (m_OrbitDef == null)
            {
                handleNonCfiError("Error Calculating Orbit number for required"
                        + "time - Orbit Definition is not initialised");
            }
            
            try
            {
                // Use the TimeOfInt class to translate the Date object to a
                // time in MJD2000 format, then pass it to the OrbitDef object
                // to calculate an absolute orbit number
                TimeOfInt oTime = new TimeOfInt();
                oTime.setStartTime(oStartTime);
                oOrbitPoint = m_OrbitDef.convertTimeToOrbit(oTime.getStartTime());
            }
            catch(CfiError cfiError)
            {
		handleCfiError(cfiError);
            }
            
            return oOrbitPoint;
        }
                
	private List<Segment> calculateZoneVisibility( String sSdfFile ) throws EoCfiHndlrError
	{
            List<Segment> oVisCoverage = null;
                    
            // Check that the Swath Definition object has been initialised
            if (m_SwathDef == null)
            {
                handleNonCfiError("Cannot calculcate Zone visibility - "
                        + "Swath Definition not initialised");
            }

            try
            {
                // Read the SDF file and create an STF file valid for the first orbit in the visibility period
                handleLogMessage("Calculating Swath Ground Track ...");
                m_SwathDef.createSTFFromSDF(sSdfFile);
            
                // Check that the Area of Interest has been set
                if (m_VisAoI == null)
		{
                    handleNonCfiError("No Valid Area of Interest set for Visibility Calculation");
		}
			
                // Call the function to assess whether there is visibility of 
                // that Area at the required Times of Interest
                handleLogMessage("Calculating the Visibility over AoI ...");
		m_SwathDef.analyseZoneVisibility(m_VisAoI);
				
                // Translate those visibility segments in the list into Geodetic
                // coordinates
                handleLogMessage("Calculating Segment Geodetic data for Visibility coverage ...");
		if (!m_SwathDef.calculatePositionCoord())
		{
                    handleNonCfiError("Error in Swath or STF file objects required for Ground Track Position");
		}
		
                // Extract the List of Segments and assign to the member variable
		oVisCoverage = m_SwathDef.getVisibilityCoverage();	
			
            }
            catch(CfiError cfiError)
            {
		handleCfiError(cfiError);
            }
            
            return oVisCoverage;
	}
        	
	// ----------------------------------------------------------------------------------------------------
	// ---------------------------- EoCfiHandler Functions for Handling Errors ----------------------------
	// ----------------------------------------------------------------------------------------------------
			
	// Translate the CFiError object returned from the EOCFI libraries to the Handler Error object and throw upwards
	private void handleCfiError( CfiError cfiError ) throws EoCfiHndlrError
	{           
            handleLogMessage("Error reported from the EOCFI libraries:");
                    
            String sErrorMessage = "";
            
            // Create and retrieve the Vector of Error Messages returned from
            // the EOCFI custom error class that is thrown by the EOCFI library
            // functions
            Vector<String> errMsgVec = new Vector<String>();
            errMsgVec = cfiError.getMsg(errMsgVec);

            // If the vector is empty then the null string is thrown
            if (!errMsgVec.isEmpty())
            {              
                // Print the Error Messages
                for (int iMsgIndex = 0; iMsgIndex < errMsgVec.size(); iMsgIndex++)
                {
                    handleLogMessage("ERROR #" + (iMsgIndex + 1) + " : " + 
                            errMsgVec.get(iMsgIndex));
                }
                
                sErrorMessage = errMsgVec.get(0);
            }
            
            // Instantiate the Exception class with the Error message
            EoCfiHndlrError oErrDetail = new EoCfiHndlrError( sErrorMessage );
            
            // Set the Flag to indicate that the Error is from the CFI Libraries
            oErrDetail.setCfiErrorFlag();
            
            throw oErrDetail; 
	}
	
	// Create Handler Error object, set the error message to the received string and throw upwards
	private void handleNonCfiError( String sErrorMessage ) throws EoCfiHndlrError
	{
            handleLogMessage("Error reported from the EOCFI Handler:");
                    
            handleLogMessage("ERROR : " + sErrorMessage);
            
            // Instantiate the Exception class with the Error message
            EoCfiHndlrError oErrDetail = new EoCfiHndlrError( sErrorMessage );
            
            // Set the Flag to indicate that the Error is from the Handler
            oErrDetail.setNonCfiErrorFlag();
            
            throw oErrDetail;
	}
        
        private void handleLogMessage( String sLogMessage )
        {
            final String EOCFI_LOG_HEADER = "EOCFIHndlr - ";
            
            System.out.println(EOCFI_LOG_HEADER + sLogMessage);
        }
        
        private String getCfiLibraryVersion()
        {
            String sLibVersion = "";
            
            try
            {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder;
                Document document;
        
                dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse(new File("pom.xml"));
                Node version = document.getElementsByTagName("eocfi.version").item(0);
        
                sLibVersion = version.getTextContent();
            }
            catch (SAXException | IOException | ParserConfigurationException exc)
            {
                sLibVersion = "Unknown";
            }
            
            return sLibVersion;
        }
		
}
