/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FileSearchTest.java
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

import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author re-dboatswain
*/

public class FileSearchTest {
    
    @Test
    public void test()
    {       
       String sFileDirectory = System.getProperty("user.home") + File.separator +
                ".dream" + File.separator + "fas"; 
        
        // ********************************************************************
        // ******************* Utilities inputDirTest Test ********************
        // ********************************************************************
        System.out.println("---------------------------------------");
        System.out.println("TEST of Utilities Directory Test");
        System.out.println("---------------------------------------");
        List<String> lTestSearches = new ArrayList<String>();
        
        lTestSearches.add(sFileDirectory);
        lTestSearches.add(sFileDirectory + File.separator + "Bad Satellite");
        sFileDirectory = sFileDirectory + File.separator + "Sentinel1";
        lTestSearches.add(sFileDirectory);
        sFileDirectory = sFileDirectory + File.separator + "OSF" + File.separator;
        lTestSearches.add(sFileDirectory);
        lTestSearches.add("");
        
        for (int iTestIndex = 0; iTestIndex < lTestSearches.size(); iTestIndex++)
        {
            System.out.println("Folder String = " + lTestSearches.get(iTestIndex));
            
            boolean bDirExists = Utilities.inputDirTest(
                    lTestSearches.get(iTestIndex));
        
            if (bDirExists)
            {
                System.out.println("Directory DOES Exists\n");
            }
            else
            {
                System.out.println("Directory does NOT Exists\n");
            }
            System.out.println();
        }
        
        // ********************************************************************
        // ******************* Utilities inputFileTest Test ********************
        // ********************************************************************
        System.out.println("---------------------------------------");
        System.out.println("TEST of Utilities File Exists Test");
        System.out.println("---------------------------------------");
        lTestSearches.clear();
        
        lTestSearches.add(sFileDirectory);
        lTestSearches.add(System.getProperty("user.home") + File.separator +
                ".dream" + File.separator + "fas");
        
        File inputDir = new File( sFileDirectory );
        String[] sFileList = inputDir.list();

        if (sFileList.length > 0)
        {
            lTestSearches.add(sFileDirectory + sFileList[0]);
        }
        else
        {
            System.out.println("ERROR = No Files to test in " + sFileDirectory);
        }

        lTestSearches.add(sFileDirectory + "Bad File.xml");
        lTestSearches.add("");
        
        for (int iTestIndex = 0; iTestIndex < lTestSearches.size(); iTestIndex++)
        {
            System.out.println("File Path String = " + lTestSearches.get(iTestIndex));
            
            boolean bDirExists = Utilities.inputFileTest(
                    lTestSearches.get(iTestIndex));
        
            if (bDirExists)
            {
                System.out.println("File DOES Exists\n");
            }
            else
            {
                System.out.println("File does NOT Exists\n");
            }
            System.out.println();
        }
        
        // ********************************************************************
        // *************** Utilities findLatestFileVersion Test ***************
        // ********************************************************************
        System.out.println("---------------------------------------");
        System.out.println("TEST of Utilities Latest Version Search");
        System.out.println("---------------------------------------");
        lTestSearches.clear();
        
        lTestSearches.add("*");
        lTestSearches.add("S1A_TEST_*");
        lTestSearches.add("S1A_TEST_");
        lTestSearches.add("S1A_TEST_MPL_ORBSCT_20020301T092001_99999999T999999");
        lTestSearches.add("Missing*");
        
        for (int iTestIndex = 0; iTestIndex < lTestSearches.size(); iTestIndex++)
        {
            System.out.println("Search String = " + lTestSearches.get(iTestIndex));
            
            String sFoundFile = Utilities.findLatestFileVersion(sFileDirectory, 
                    lTestSearches.get(iTestIndex));
        
            System.out.println("File Found in " + sFileDirectory + "\n");
            System.out.println("\t" + sFoundFile);
            System.out.println();
        }
        
    }
}
