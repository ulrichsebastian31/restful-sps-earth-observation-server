/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityTest.java
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

import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.*;
import net.eads.astrium.hmas.util.structures.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.junit.Test;

/**
 *
 * @author re-dboatswain
 */
public class FeasibilityTest {
    
        @Test
        public void test()
        {
            String userDir = null;
            
            try {
                userDir = System.getProperty("user.dir");
            }catch (Exception e) {
                e.printStackTrace();
            }
            
            
            // Instantiate the variables required for the test to run
            String sConfigDir = userDir +
//                File.separator + "DreamEOCFIHandler" +
                File.separator + "src" + File.separator + "test" +
                File.separator + "resources" + File.separator + "Configuration" +
                File.separator + "Test Configs" + 
                File.separator;
            double dAcqDuration = 1; 
            double dImgDlDuration = 1;
            
            List<String> lTestDesc = new ArrayList<String>();
            List<String> lTestFiles = new ArrayList<String>();
            
            // First Test - Positive
//            lTestDesc.add("Feasibility Request Test - Positive Test:");
//            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive.txt");
            
//            lTestDesc.add("Feasibility Request Test - Positive Test Larger Window:");
//            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_Big.txt");
            
//            lTestDesc.add("Feasibility Request Test - Realistic Test:");
//            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_UK.txt");
            
//            lTestDesc.add("Feasibility Request Test - Realistic Test Square:");
//            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_UK_Simple.txt");
                        
//            lTestDesc.add("Feasibility Request Test - Realistic Test Square from Client:");
//            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_UK_SimpleClient.txt");

            // Investigation to HMA observation
            lTestDesc.add("Feasibility Request Test - HMA Investigation:");
            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_MMFATest.txt");
            
            // Legitimate tests
            /*            
            // First Test - Positive
            lTestDesc.add("Feasibility Request Test - Real Test:");
            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_RealTest.txt");
            
            // Second Test - Positive
            lTestDesc.add("Feasibility Request Test - Repeat Run:");
            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_RealTest.txt");
            
            // Third Test - Negative
            lTestDesc.add("Feasibility Request Test - Negative Test - No OSF:");
            lTestFiles.add(sConfigDir + "Input_FeasReq_Negative_NoOSF.txt");
            
            // Fourth Test - Negative
            lTestDesc.add("Feasibility Request Test - Negative Test - No Visibility:");
            lTestFiles.add(sConfigDir + "Input_FeasReq_Negative_NoVis.txt");
            
            // Fifth Test - Negative
            lTestDesc.add("Feasibility Request Test - Negative Test - No Ground Station Visibility:");
            lTestFiles.add(sConfigDir + "Input_FeasReq_Positive_RealTest.txt");
*/            
            for (int iTestIndex = 0; iTestIndex < lTestFiles.size(); iTestIndex++)
            {
                if (iTestIndex == 4)
                {
                    dImgDlDuration = 2000;
                }
                
                System.out.println("********************************************************");
                System.out.println("TEST " + (iTestIndex + 1) + " - " + lTestDesc.get(iTestIndex));
                System.out.println();
                testCFIHndlCall( lTestFiles.get(iTestIndex),
                        dAcqDuration, dImgDlDuration);               
            }
        }
        
        private void testCFIHndlCall( String sInputFile, 
                double dAcqDuration, double dImgDlDuration) 
        {     
            try
            {
    		// ********************** Start of Reading Input Parameters from Text File ********************** 
                
    		// Open Input Parameters Text File    	
    		BufferedReader dataIn = new BufferedReader( new FileReader(sInputFile) );
    		    	
    		// Read Visibility Start Time
    		System.out.println("START = ");
    		Date oVisBeginFirst = TestUtilities.extractVisibilityDate(dataIn);
                Date oVisBeginSecond = TestUtilities.extractVisibilityDate(dataIn);
		
    		// Read Visibility End Time
    		System.out.println("END = ");
    		Date oVisEndFirst = TestUtilities.extractVisibilityDate(dataIn);
                Date oVisEndSecond = TestUtilities.extractVisibilityDate(dataIn);

    		// Read Visibility Area of Interest
    		Polygon oVisAoI = TestUtilities.extractVisibilityAOI(dataIn); 
                
    		// ********************** End of Reading Input Parameters from Text File **********************
    		
    		// ********************** Start of EOCFI Test using a OSF file **********************
//               EoCfiHandler oCfiTest = new EoCfiHandler("Sentinel1");
    		EoCfiHandler oCfiTest = new EoCfiHandler("00001AAA");
//               EoCfiHandler oCfiTest = new EoCfiHandler("00001AAB");
                
                List<String> instModes = new ArrayList<>();
//                instModes.add(null);
                instModes.add("EW");
                instModes.add("IW");
                instModes.add("SM");
                instModes.add("WV");
//                instModes.add("MSI");
//                instModes.add("INSTR2");
/*                instModes.add("Mode1");
                instModes.add("Mode2");
                instModes.add("Mode3");
                instModes.add("Mode4");
                instModes.add("Mode5");
                instModes.add("Mode6");
*/ 
                
//                OPTTaskingParameters oOptTest = new OPTTaskingParameters(
//                        0.0, 0.0, 0.0, true, true, 
//                        0.0, true, 0.0, 0.0, 
//                        0.0, 0.0, 0.0, 
//                        0.0, 0.0, 0.0, oVisAoI, 
//                       "Rectangle", oVisibilityBegin, oVisibilityEnd, 0, 0, 
//                        instModes);
                
                List<TimePeriod> oToIList = new ArrayList<>();
                oToIList.add( new TimePeriod(
                        oVisBeginFirst,
                        oVisEndFirst) );
                
                oToIList.add( new TimePeriod(
                        oVisBeginSecond,
                        oVisEndSecond) );
                
                SARTaskingParameters oSarTest = new SARTaskingParameters(
                        0.0, 0.0, true, instModes, 0.0, 0.0, 0.0, 0.0, 
                        0.0, 0.0, 0.0, 0.0, oVisAoI, "", oToIList, 0, 0, instModes);

                List<Segment> oTotalCoverage = oCfiTest.getSARFeasibility(
                        "S1ASAR", oSarTest, dAcqDuration);
//                List<Segment> oTotalCoverage = oCfiTest.getSARFeasibility(
//                        "MSI", oSarTest, dAcqDuration);
    		
                System.out.println();
                if (oTotalCoverage.isEmpty())
                {
                    System.out.println("No Visible periods of AoI at the received ToI");
                }
                else
                {
                    for (int iSegment = 0; iSegment < oTotalCoverage.size(); iSegment++)
		    {
                        System.out.println("--- Segment " + iSegment + " ---");
                        
                        System.out.println("Instrument Mode = " +
                                oTotalCoverage.get(iSegment).getInstrumentMode());
                        
                        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                        sdfTime.setTimeZone(TimeZone.getTimeZone("UTC"));
		
                        // Set the value of the object to the current system time in UTC
                        String sTime = sdfTime.format(oTotalCoverage.get(iSegment).getStartOfAcquisition().getTime());
                        
                        System.out.println("Acquisition Start Time = " + sTime);
                        System.out.println("Acquisition Start Orbit Number = " + 
                                oTotalCoverage.get(iSegment).getStartOrbit());
                        
                        sTime = sdfTime.format(oTotalCoverage.get(iSegment).getEndOfAcquisition().getTime());
                        System.out.println("Acquisition End Time = " + sTime);
                        System.out.println("Acquisition End Orbit Number = " + 
                                oTotalCoverage.get(iSegment).getEndOrbit());
                        
                        sTime = sdfTime.format(oCfiTest.getOrbitBeginTime(
                                oTotalCoverage.get(iSegment).getEndOrbit()));
                        
                        System.out.println("Ascending Node Time of the End Orbit Number = " +
                                sTime);
                                        
                        System.out.println();
                        
                        System.out.println("Visibility :");
		    	
                        List<Point> visPoints = oTotalCoverage.get(iSegment).getPolygon().getPoints();
                        for (int i = 0; i < visPoints.size(); i++)
                        {
                            System.out.println("Polygon Point " + (i + 1) + " - Longitude = " + visPoints.get(i).getLongitude() + 
                                    ", Latitude = " + visPoints.get(i).getLatitude() + 
                                    ", Altitude = " + visPoints.get(i).getAltitude());
                        }
                        System.out.println();
                        
                        List<SegmentVisGS> oGndSt = oCfiTest.getNextStationDownlink(
                                oTotalCoverage.get(iSegment).getEndOfAcquisition().getTime(), 
                                oTotalCoverage.get(iSegment).getEndOrbit(),
                                dImgDlDuration);
                        
                        System.out.println();
                        
                        if (oGndSt.isEmpty())
                        {
                            System.out.println("No Ground Station Visible periods for current Segment");
                        }
                        else
                        {
                            for (int iGndSegIndex = 0; iGndSegIndex < oGndSt.size(); iGndSegIndex++)
                            {
                                System.out.println("Ground Station ID = " + 
                                        oGndSt.get(iGndSegIndex).getGroundStationId());
                                System.out.println("Ground Station = " + 
                                        oGndSt.get(iGndSegIndex).getGroundStationName());
                                sTime = sdfTime.format(
                                        oGndSt.get(iGndSegIndex).getStartOfVisibility().getTime());

                                System.out.println("Ground Station Visibility Start Time = " + sTime);

                                sTime = sdfTime.format(
                                        oGndSt.get(iGndSegIndex).getEndOfVisibility().getTime());

                                System.out.println("Ground Station Visibility End Time = " + sTime);

                                System.out.println();
                            }

                            System.out.println();
                        }
                        
                        System.out.println();
		    }
                }
                
                dataIn.close();
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
