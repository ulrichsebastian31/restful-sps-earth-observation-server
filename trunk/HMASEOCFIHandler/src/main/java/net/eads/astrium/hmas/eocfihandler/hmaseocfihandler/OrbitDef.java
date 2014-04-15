/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrbitDef.java
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
 *   File Name							:	OrbitDef.java
 *   File Type							:	Source Code
 *   Description						:	This class provides access to the required EOCFI
 *   										orbit functions necessary for Feasibility Tasking
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

import EECFI.ANXTime;
import EECFI.CfiError;
import EECFI.ModelId;
import EECFI.OrbitId;
import EECFI.SatId;
import EECFI.TimeCorrelation;
import EECFI.TimeInterval;
import EECFI.TimeSegment;
import EECFI.StateVector;
import EECFI.Geodetic;
import EECFI.StateVectorExtraInfo;

import static EECFI.EnumLib.*;
import static EECFI.EnumOrbit.*;
import java.io.File;

import java.util.Vector;

public class OrbitDef {

	// Private Member Variables
        private static final long INITIAL_NULL_TIME = 0;
        
	private String m_fileLocation;			// File path to the required Orbit input files
	private SatId m_satID;				// Satellite ID object required by EOCFI
	private ModelId m_modelID;			// Model ID object required by EOCFI
	private TimeCorrelation m_timeID;		// TimeCorrelation object required by EOCFI to link the various Time references
	private OrbitId m_orbitID;			// Orbit ID object that will perform the various Orbit calculations
	private TimeSegment m_OrbitRange;		// TimeSegment object that will store the range of orbit numbers for a certain 
							// 	visibility period
	
	// Class constructor
	public OrbitDef(long lSatId) throws CfiError
	{
		m_satID = new SatId( lSatId );
			
		m_modelID = new ModelId();
			
		m_timeID = null;
			
		m_orbitID = null;
		
		m_OrbitRange = null;
	}
	
	// Class constructor
	public OrbitDef(String sSatFile) throws CfiError
	{
		m_satID = new SatId( sSatFile );
			
		m_modelID = new ModelId();
			
		m_timeID = null;
			
		m_orbitID = null;
		
		m_OrbitRange = null;
	}
        		
	// Initialise the member variables by reading from an OSF file
	public void initialiseFromOSFfile(String sOSFFilename) throws CfiError
	{
            // Create the vector of input files that will consist of the 
            //OSF filenames
            Vector<String> inputFiles = new Vector<String>();
            // Add the input OSF file to the vector
            inputFiles.add( m_fileLocation + sOSFFilename );
			
            // Create the Time Correlation object used to switch between time 
            // references from the OSF file
            TimeInterval valTime      = new TimeInterval();
            m_timeID = new TimeCorrelation( XLCFI_TIMEMOD_OSF, 
                    inputFiles, XOCFI_TIME_UTC, valTime);
					        
            // Create the Orbit ID by instantiating the OrbitId object from the 
            // values within the OSF file
            m_orbitID = new OrbitId( m_satID, m_modelID, m_timeID, 
                    XOCFI_TIME_UTC, XOCFI_ORBIT_INIT_OSF_MODE, inputFiles, 
                    XOCFI_SEL_FILE, 0, 0, 0, 0);
	}
	
	// Find the range of absolute orbit numbers that correspond to the 
        // Visibility period between AcqStartTime and AcqEndTime
	public void findOrbitRange(TimeOfInt oTOIVis) throws CfiError
	{	
            if (m_orbitID != null)
            {
                // Initialise both Start and Stop ANX Time objects using the OrbitID member variable object and a NULL
                // Orbit number, Time in Seconds and Microseconds - Correct values will be set later by timeToOrbit() function
                ANXTime oAnxStartTime = new ANXTime(m_orbitID, INITIAL_NULL_TIME, INITIAL_NULL_TIME, INITIAL_NULL_TIME);
                ANXTime oAnxStopTime = new ANXTime(m_orbitID, INITIAL_NULL_TIME, INITIAL_NULL_TIME, INITIAL_NULL_TIME);

                // Get the Orbit number corresponding to the Start Time of acquisition
                oAnxStartTime.timeToOrbit(XOCFI_TIME_UTC, oTOIVis.getStartTime());

                // Get the Orbit number corresponding to the End Time of acquisition
                oAnxStopTime.timeToOrbit(XOCFI_TIME_UTC, oTOIVis.getEndTime());

                // Set these start and stop ANX objects to the Range member variable
                if (m_OrbitRange == null)
                {
                    m_OrbitRange = new TimeSegment();
                }
                m_OrbitRange.start = oAnxStartTime;
                m_OrbitRange.stop = oAnxStopTime;
            }
	}
        
        // Returns the absolute orbit number that encompasses the received time
        // in MJD2000 format
        public ANXTime convertTimeToOrbit(double dTime) throws CfiError
        {
            ANXTime oAnxTime = null;
            
            // Check that the OrbitID object has been initialised
            if (m_orbitID != null)
            {
                oAnxTime = new ANXTime(m_orbitID, 
                        INITIAL_NULL_TIME, 
                        INITIAL_NULL_TIME, 
                        INITIAL_NULL_TIME);

                // Get the Orbit number corresponding to the Start Time of acquisition
                oAnxTime.timeToOrbit(XOCFI_TIME_UTC, dTime);
            }
            
            return oAnxTime;
        }
        
        // Returns the time in MJD2000 format that corresponds to the Ascending node
        // time for the received absolute orbit number
        public double convertOrbitToTime(long orbitNumber) throws CfiError
        {
            double dAnxTime = 0.0;
            
            // Check that the OrbitID object has been initialised
            if (m_orbitID != null)
            {
                ANXTime oAnxTime = new ANXTime(m_orbitID, 
                        orbitNumber, 
                        INITIAL_NULL_TIME, 
                        INITIAL_NULL_TIME);

                // Get the Time corresponding to the Orbit number
                dAnxTime = oAnxTime.orbitToTime(XOCFI_TIME_UTC);
            }
            
            return dAnxTime;
        }
	
	// Returns the Geodetic co-ordinates of the satellite position within the orbit for a required time (in days) 
	public Geodetic getOrbitalPosition(double dTimeReq) throws CfiError
	{
            // Compute the State Vector for the required time
            StateVector oStVec = calcStateVector(dTimeReq);
		
            // If the returned State Vector is not null, retrieve the Geodetic 
            // co-ordinates from the State Vector
            Geodetic oGeoPos = null;
            if (oStVec != null)
            {
                oGeoPos = oStVec.coord.getGeodetic(m_modelID, XLCFI_NO_DER);
            }
		
            return oGeoPos;
	}
        
        // Returns the State Vector for the satellite at a specific time 
        // (in days)
        public StateVector calcStateVector(double dTimeReq) throws CfiError
        {
            StateVector oRtnVector = null;
            
            // Check that the OrbitID member variable has been initialised
            if (m_orbitID != null)
            {
                // Compute the state vector for the required time
                oRtnVector = m_orbitID.osvCompute(XOCFI_PROPAG_MODEL_MEAN_KEPL, 
				XOCFI_TIME_REF_OF_UTC, dTimeReq);
            }
            
            return oRtnVector;
        }
        
        public double getOrbitPeriod() throws CfiError
        {
            double dOrbitPeriod = 0.0;
            
            // Check that the OrbitID member variable has been initialised
            if (m_orbitID != null)
            {
                // Compute the state vector for the required time
                StateVectorExtraInfo oVector = m_orbitID.osvComputeExtra(
                        XOCFI_ORBIT_EXTRA_DEP_ANX_TIMING);
                
                dOrbitPeriod = oVector.modelDep[0];
            }
            
            return dOrbitPeriod;
        }
        
	// Set the member variable to the File Path where input files will be stored
	public void setFilePath(String sFileLocation)
	{
            // If the input string does not end with a final folder separator, add one
            if (sFileLocation.endsWith(File.separator) == false)
            {
                sFileLocation = sFileLocation + File.separator;
            }
				
            // Set the member variable to the path string
            m_fileLocation = sFileLocation;
	}
	
	// Return member variable for the file path for initial file checking
	public String getFilePath()
	{
            return m_fileLocation;
	}
        
        // Provide access to the Sat ID member variable in order to pass it on to other classes
        public SatId getSatID()
        {
            return m_satID;
        } 
               
        // Provide access to the Model ID member variable in order to pass it on to other classes
	public ModelId getModelID()
        {
            return m_modelID;
        }      
                
        // Provide access to the Time Correlation member variable in order to pass it on to other classes
	public TimeCorrelation getTimeID()
        {
            return m_timeID;
        }
	
	// Provide access to the Orbit ID member variable in order to pass it on to other classes
	public OrbitId getOrbitID()
	{
            return m_orbitID;
	}
	
	// Provide access to the OrbitRange member variable in order to pass it on to other classes
	public TimeSegment getOrbitRange()
	{
            return m_OrbitRange;
	}

}
