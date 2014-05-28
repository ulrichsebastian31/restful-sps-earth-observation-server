/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SatPropTest.java
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

import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EoCfiHndlrError;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EoCfiHandler;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.junit.Test;

/**
 *
 * @author re-dboatswain
 */
public class SatPropTest {
    
        @Test
        public void test()
        {                          
            // Instantiate the variables required for the test to run
            String sConfigDir = System.getProperty("user.home") + File.separator + "dream" +
                File.separator + "DreamEOCFIHandler" +
                File.separator + "src" + File.separator + "test" +
                File.separator + "resources" + File.separator + "Configuration" +
                File.separator + "Test Configs" + 
                File.separator;
            
            List<String> lTestDesc = new ArrayList<String>();
            List<String> lTestFiles = new ArrayList<String>();
            
            // First Test - Positive
            lTestDesc.add("Satellite Orbital Propagation Test - Positive Test:");
            lTestFiles.add(sConfigDir + "Input_SatProp_Positive.txt");
            
            // Second Test - Negative
            lTestDesc.add("Satellite Orbital Propagation Test - Negative Test - Bad Start Time File:");
            lTestFiles.add(sConfigDir + "Input_SatProp_Negative_BadSt.txt");
            
            // Third Test - Negative
            lTestDesc.add("Satellite Orbital Propagation Test - Negative Test - Bad End Time Value:");
            lTestFiles.add(sConfigDir + "Input_SatProp_Negative_BadEt.txt");
            
            // Fourth Test - Negative
            lTestDesc.add("Satellite Orbital Propagation Test - Negative Test - Null Times:");
            lTestFiles.add(sConfigDir + "Input_SatProp_Negative_Nullt.txt");
            
            for (int iTestIndex = 0; iTestIndex < lTestFiles.size(); iTestIndex++)
            {
                System.out.println("********************************************************");
                System.out.println("TEST " + (iTestIndex + 1) + " - " + lTestDesc.get(iTestIndex));
                System.out.println();
                testCFIHndlCall( lTestFiles.get(iTestIndex) );
            }
        }
    	
        private void testCFIHndlCall( String sInputFile ) 
        {     	
            try
            {
    		// ********************** Start of Reading Input Parameters from Text File ********************** 
            
    		// Open Input Parameters Text File    	
    		BufferedReader dataIn = new BufferedReader( new FileReader(sInputFile) );
    		    	
    		// Read Visibility Start Time
    		System.out.println("Satellite Propagation START time = ");
    		Date oVisibilityBegin = TestUtilities.extractVisibilityDate(dataIn);
		
    		// Read Visibility End Time
    		System.out.println("Satellite Propagation END time = ");
    		Date oVisibilityEnd = TestUtilities.extractVisibilityDate(dataIn);
   
    		// ********************** End of Reading Input Parameters from Text File **********************
    		
    		// ********************** Start of EOCFI Test using a OSF file **********************
//    		EoCfiHandler oCfiTest = new EoCfiHandler("Sentinel1");
                EoCfiHandler oCfiTest = new EoCfiHandler("00001AAA");
//                EoCfiHandler oCfiTest = new EoCfiHandler("00001AAB");
                
                List<String> instModes = new ArrayList<String>();               
                
//                List<Point> visPoints = oCfiTest.getOrbitPropagation(oVisibilityBegin, oVisibilityEnd, 30.0);
                List<Point> visPoints = oCfiTest.getOrbitPropagation(oVisibilityBegin, oVisibilityEnd, 0.0);
                for (int i = 0; i < visPoints.size(); i++)
                {
                    System.out.println("Polygon Point " + (i + 1) + " - Longitude = " + visPoints.get(i).getLongitude() + 
                            ", Latitude = " + visPoints.get(i).getLatitude() + 
                            ", Altitude = " + visPoints.get(i).getAltitude());
                }    		
    	
            }
	    catch(IOException exc)
	    {
	    	System.out.println("User Input Error: " + exc.getMessage());
	    }
            catch(EoCfiHndlrError oErrorDetail)
            {
                System.out.println();
                
                if (oErrorDetail.getCfiErrorFlag())
    		{
    			System.out.println("Error reported from the EOCFI libraries:");
    		}
    		else
    		{
    			System.out.println("Error reported from the EOCFI Handler:");
    		}
                
                String sErrMessage = oErrorDetail.getMessage();
                
                System.out.println(sErrMessage);
            }
    	
            System.out.println("\n*** Test RAN SUCCESSFULLY ***");
	}
}
