/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ToITest.java
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

import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.TimeOfInt;
import java.text.ParseException;
import org.junit.Test;
import java.util.Date;
import java.util.Calendar;
import net.eads.astrium.hmas.util.DateHandler;

/**
 *
 * @author re-dboatswain
 */
public class ToITest {
    
    @Test
    public void test() {
        
        System.out.println("******* Utility TEST *******\n");
/*        
        // FIRST TEST - Bad String test
        System.out.println("Negative Test with Non Numeric values");   
        String sTestStrS = "200z0301T105f46";
        System.out.println("Utility Test Time = " + sTestStrS);
        Date testDateS = Utilities.convertStr2Date(sTestStrS);
        
        if ( testDateS != null )
        {
            System.out.println("Correct Date Format - TEST FAIL");
        }
        else
        {
            System.out.println("Error converting Times - Incorrect Format (Expected yyyymmddThhmmss)");
        }
        
        // SECOND TEST - Bad String test
        System.out.println("Negative Test with Badly formatted dateTime delimiter");   
        sTestStrS = "20020301_105846";
        System.out.println("Utility Test Time = " + sTestStrS);
        testDateS = Utilities.convertStr2Date(sTestStrS);
        
        if ( testDateS != null )
        {
            System.out.println("Correct Date Format - TEST FAIL");
        }
        else
        {
            System.out.println("Error converting Times - Incorrect Format (Expected yyyymmddThhmmss)");
        }
        
        // THIRD TEST - Bad String test
        System.out.println("Negative Test with Badly formatted date string");
        sTestStrS = "2002/03/01T105846";
        System.out.println("Utility Test Time = " + sTestStrS);
        testDateS = Utilities.convertStr2Date(sTestStrS);
        
        if ( testDateS != null )
        {
            System.out.println("Correct Date Format - TEST FAIL");
        }
        else
        {
            System.out.println("Error converting Times - Incorrect Format (Expected yyyymmddThhmmss)");
        }
        
        // FOURTH TEST - Bad String test
        System.out.println("Negative Test with Badly formatted time string"); 
        sTestStrS = "20020301T10:58:46";
        System.out.println("Utility Test Time = " + sTestStrS);
        testDateS = Utilities.convertStr2Date(sTestStrS);
        
        if ( testDateS != null )
        {
            System.out.println("Correct Date Format - TEST FAIL");
        }
        else
        {
            System.out.println("Error converting Times - Incorrect Format (Expected yyyymmddThhmmss)");
        }
*/        
        // FIFTH TEST - Good String test
        System.out.println();  
        System.out.println("Good Test with well formatted strings");
        String sTestStrS = "20020301T092002";
        String sTestStrE = "20020301T105846";
        System.out.println("Utility Test Start Time = " + sTestStrS);
        System.out.println("Utility Test End Time = " + sTestStrE);
        
        Date testDateS = null;//Utilities.convertStr2Date(sTestStrS);
        Date testDateE = null;//Utilities.convertStr2Date(sTestStrE);
        
        try
        {
            testDateS = DateHandler.parseEocfiDate(sTestStrS);
            testDateE = DateHandler.parseEocfiDate(sTestStrE);
        }
        catch (ParseException exc)
        {
            testDateS = null;
            testDateE = null;
        }
        
        if ( (testDateS != null) && (testDateE != null) )
        {
            TimeOfInt oToI_UT = new TimeOfInt(testDateS, testDateE);
        
            double dSStart_UT = oToI_UT.getStartTime();
            double dEStart_UT = oToI_UT.getEndTime();

            System.out.println("Start Time In MJD2000 = " + dSStart_UT);
            System.out.println("End Time In MJD2000 = " + dEStart_UT);
            System.out.println();
        }
        else
        {
            System.out.println("Error converting Times - Incorrect Format (Expected yyyymmddThhmmss)");
        }
        
        
        System.out.println("******* Time Of Interest TEST *******\n");
        
        System.out.println("-- TIME Conversion Tests --");
        System.out.println("First Time Conversion Test");
        String sSTime = "2002-03-01_09:20:02";
        String sETime = "2002-03-01_10:58:46";
        
        Date oSDate = TestUtilities.extractVisibilityDate(sSTime);
        Date oEDate = TestUtilities.extractVisibilityDate(sETime);
        
        System.out.println("Input Start Time = " + sSTime);
        System.out.println("Input End Time = " + sETime);
        System.out.println();
        
        TimeOfInt oToI = new TimeOfInt(oSDate, oEDate);
        
        double dSStart = oToI.getStartTime();
        double dEStart = oToI.getEndTime();
        
        System.out.println("Start Time In MJD2000 = " + dSStart);
        System.out.println("End Time In MJD2000 = " + dEStart);
        System.out.println();
      
        String sStartStr = oToI.getStartTimeStr();
        String sEndStr = oToI.getEndTimeStr();
        
        System.out.println("Start Time String = " + sStartStr);
        System.out.println("End Time String = " + sEndStr);
        System.out.println();
        
        // Second Conversion Test
        System.out.println("Second Time Conversion Test - Clocks Forward");
        sSTime = "2013-03-31_00:00:00";
        sETime = "2013-04-01_00:00:00";
        
        oSDate = TestUtilities.extractVisibilityDate(sSTime);
        oEDate = TestUtilities.extractVisibilityDate(sETime);
        
        System.out.println("Input Start Time = " + sSTime);
        System.out.println("Input End Time = " + sETime);
        System.out.println();
        
        TimeOfInt oToI2 = new TimeOfInt(oSDate, oEDate);
        
        dSStart = oToI2.getStartTime();
        dEStart = oToI2.getEndTime();
        
        System.out.println("Start Time In MJD2000 = " + dSStart);
        System.out.println("End Time In MJD2000 = " + dEStart);
        System.out.println();
      
        sStartStr = oToI2.getStartTimeStr();
        sEndStr = oToI2.getEndTimeStr();
        
        System.out.println("Start Time String = " + sStartStr);
        System.out.println("End Time String = " + sEndStr);
        System.out.println();
        
        // Third Conversion Test
        System.out.println("Third Time Conversion Test - Clocks Back");
        sSTime = "2013-10-27_00:00:00";
        sETime = "2013-10-28_00:00:00";
        
        oSDate = TestUtilities.extractVisibilityDate(sSTime);
        oEDate = TestUtilities.extractVisibilityDate(sETime);
        
        System.out.println("Input Start Time = " + sSTime);
        System.out.println("Input End Time = " + sETime);
        System.out.println();
        
        TimeOfInt oToI3 = new TimeOfInt(oSDate, oEDate);
        
        dSStart = oToI3.getStartTime();
        dEStart = oToI3.getEndTime();
        
        System.out.println("Start Time In MJD2000 = " + dSStart);
        System.out.println("End Time In MJD2000 = " + dEStart);
        System.out.println();
      
        sStartStr = oToI3.getStartTimeStr();
        sEndStr = oToI3.getEndTimeStr();
        
        System.out.println("Start Time String = " + sStartStr);
        System.out.println("End Time String = " + sEndStr);
        System.out.println();
        
        
        System.out.println("-- ToI Validity Tests --");
        System.out.println("ToI Start Time = " + oToI.getStartTimeStr() + 
                ", End Time = " + oToI.getEndTimeStr());
        
        // FIRST FILE TEST - Good File
        String sFileValSt = "2002-03-01_09:20:01";
        String sFileValEn = "9999-99-99_99:99:99";
        
        Date oValStDate = TestUtilities.extractVisibilityDate(sFileValSt);
        Date oValEnDate = TestUtilities.extractVisibilityDate(sFileValEn);
        
        System.out.println("First File validity Period - START = " + sFileValSt +
                ", END = " + sFileValEn);
        if (oToI.validityPeriodTest(oValStDate, oValEnDate))
        {
            System.out.println("File IS valid");
        }
        else
        {
            System.out.println("File NOT valid");
        }
        System.out.println();
        
        // SECOND FILE TEST - Bad Start Time
        sFileValSt = "2002-03-01_09:20:03";
        sFileValEn = "9999-99-99_99:99:99";
        
        oValStDate = TestUtilities.extractVisibilityDate(sFileValSt);
        oValEnDate = TestUtilities.extractVisibilityDate(sFileValEn);
        
        System.out.println("Second File validity Period - START = " + sFileValSt +
                ", END = " + sFileValEn);
        if (oToI.validityPeriodTest(oValStDate, oValEnDate))
        {
            System.out.println("File IS valid");
        }
        else
        {
            System.out.println("File NOT valid");
        }
        System.out.println();
        
        // THIRD FILE TEST - Bad End Time
        sFileValSt = "2002-03-01_09:20:01";
        sFileValEn = "2002-03-01_10:58:45";
        
        oValStDate = TestUtilities.extractVisibilityDate(sFileValSt);
        oValEnDate = TestUtilities.extractVisibilityDate(sFileValEn);
        
        System.out.println("Third File validity Period - START = " + sFileValSt +
                ", END = " + sFileValEn);
        if (oToI.validityPeriodTest(oValStDate, oValEnDate))
        {
            System.out.println("File IS valid");
        }
        else
        {
            System.out.println("File NOT valid");
        }
        System.out.println();

    }
  
}
