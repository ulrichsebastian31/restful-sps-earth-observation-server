/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Utilities.java
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

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class Utilities {

	// Test if the file pointed by sFilePath (File Path and Name) exists and is readable 
	public static boolean inputFileTest( String sFilePath )
	{
		boolean bFileExists = false;
		
		File fTest = new File (sFilePath);
		
		if ((fTest.isFile()) && (fTest.canRead()))
		{
			bFileExists = true;
		}		
		
		return bFileExists;
	}
        
        // Test if an input directory exists before attempting to read from it
	public static boolean inputDirTest( String sDirPath )
	{
		boolean bDirExists = false;
		
		File fTest = new File (sDirPath);
		
		// If the directory exists, return true
		if (fTest.exists())
		{
			bDirExists = true;
		}
		
		return bDirExists;
	}
	
	// Test if an output directory exists before attempting to write to it
	public static boolean outputDirTest( String sDirPath )
	{
		boolean bDirExists = false;
		
		File fTest = new File (sDirPath);
		
		// If the directory exists, return true
		if (fTest.exists())
		{
			bDirExists = true;
		}
		// If not attempt to create it and return the success of the mkdirs function
		else
		{
			bDirExists = fTest.mkdirs();
		}
		
		return bDirExists;
	}
        
        public static String findLatestFileVersion( String sDir, String sSearchStr )
        {
            // Create String variable that will be returned
            String sFilename = "";
            
            // Check that the Directory String ends in a File separator, add one if not
            if (sDir.endsWith(File.separator) == false)
            {
                sDir = sDir + File.separator;
            }
            
            // Create a File object pointing at the required directory
            File inputDir = new File( sDir );
            
            // Create the Filter object for filtering the Files within the directory
            ValidFileFilter oFilter = new ValidFileFilter( sSearchStr );

            // Retrieve a String array of the filenames within the target directory
            // that match the filter
            String[] sFileList = inputDir.list(oFilter);
            
            // If the return array contains files, return empty string if not
            if (sFileList != null)
            {
                int iNumFiles = sFileList.length;   // Max files to break loop
                int iFileCount = 0;                 // File Counter
                int iFileVersion = 0;               // Version number of the current file
                int iLatestVersion = 0;             // Highest Version number found so far
                
                // Search through the files until all have been checked
                while ( iFileCount < iNumFiles )
                {
                    // Test if the string is an accessible file
                    if ( inputFileTest(sDir + sFileList[iFileCount]) )
                    {
                        iFileVersion = getFileVersionNumber(sFileList[iFileCount]);
                    }
                    // If not, it is either not readable or a directory
                    // therefore set fileVersion to 0
                    else
                    {
                        iFileVersion = 0;
                    }
                    
                    // If the file version is greater than the highest found
                    // so far, set the return string as the file name
                    if (iFileVersion > iLatestVersion)
                    {
                        iLatestVersion = iFileVersion;
                        sFilename = sFileList[iFileCount];
                    }

                    iFileCount++;
                }
            }
            
            return sFilename;
        }
        
        public static int findMaxValue(double[] dArray, int iNumElements)
        {
            int iMaxIndex = 0;
            
            for (int iArrayIndex = 0; iArrayIndex < iNumElements; iArrayIndex++)
            {
                if (dArray[iArrayIndex] > dArray[iMaxIndex])
                {
                    iMaxIndex = iArrayIndex;
                }
            }
            
            return iMaxIndex;
        }
        
	// Create a String representing the current UTC timestamp
	public static String getCurrentUtcTimeStr()
	{
		// Create the SimpleDateFormat object initialised with the required format
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		sdfTime.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		// Set the value of the object to the current system time in UTC
		String sFinalTime = sdfTime.format(Calendar.getInstance().getTime());
		
		// Replace the underscore delimiter between date and time with a T
		sFinalTime = sFinalTime.replace("_", "T");
						
		return sFinalTime;
	}
        
        private static int getFileVersionNumber( String sFilename )
        {
            int iFileNum = 0;

            // Split the file by delimiter ("_")
            String[] sNameSeg = sFilename.split("_");

            try
            {
                String sFileVersion;
                
                // Check there is a file extension - if so, select the substring
                // so as not to include it
                if (sNameSeg[sNameSeg.length - 1].indexOf(".") != -1)
                {
                    sFileVersion = sNameSeg[sNameSeg.length - 1].substring(0, 
                            sNameSeg[sNameSeg.length - 1].indexOf("."));
                }
                else
                {
                    sFileVersion = sNameSeg[sNameSeg.length - 1];
                }
                
                // Convert the string version number to an integer
                iFileNum = Integer.parseInt(sFileVersion); 
            }
            // Catch incase the last field erroneously contains non
            // integer values
            catch (NumberFormatException exc)
            {
                iFileNum = 0;   // Set the file version to 0 if so
            }
            
            return iFileNum;
        }
        
        
        // Class for filtering file searches
        private static class ValidFileFilter implements FilenameFilter
        {
            // Filter string
            private String m_sFilter;
            
            // Constructor, receives the string to use as filter
            public ValidFileFilter( String sFilter )
            {
                // If the string length is greater than 1 and ends with a 
                // wildcard, remove it as it will cause comparisons to fail
                if ( (sFilter.length() > 1) && (sFilter.endsWith("*")) )
                {
                    m_sFilter = sFilter.substring(1, sFilter.length() - 1);
                }
                else
                {
                    m_sFilter = sFilter;
                }
            }
            
            @Override
            public boolean accept(File dir, String name)
            {
                boolean bValid = false;
                
                // If the filter consists purely of a wilcard, then all files
                // are automatically valid
                if (m_sFilter.equals("*"))
                {
                    bValid = true;
                }
                else
                {
                    // Otherwise only if the file name contains the search string
                    // it is valid
                    if(name.contains(m_sFilter))
                    {
                        bValid = true;
                    }
                }
                                
                return bValid;
            }
        }
}


