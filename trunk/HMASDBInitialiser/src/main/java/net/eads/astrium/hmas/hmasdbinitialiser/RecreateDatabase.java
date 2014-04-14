/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               RecreateDatabase.java
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
package net.eads.astrium.hmas.hmasdbinitialiser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import net.eads.astrium.hmas.util.logging.SysoRedirect;

/**
 *
 * @author re-sulrich
 */
public class RecreateDatabase {
    
   public static void main(String[] args) throws Exception {
        
       

        SysoRedirect.redirectSysoToFiles("dbinit", "dbinit");
        
        System.out.println("dsafseagd");
        TestConnexionParameter.setUrl(args[0]);
        TestConnexionParameter.setUser(args[1]);
        TestConnexionParameter.setPass(args[2]);
        
        String toBeDone = null;
        if (args.length < 4 || args[3] == null) {
            toBeDone = "All";
        }
        else {
            toBeDone = args[3];
        }

        System.out.println("Loading : " + toBeDone);
        
        TestConnexionParameter.setMmfasDatabase("MissionPlannerDatabase");
        TestConnexion.conn = TestConnexion.createConnexion();
        TestConnexion.testDropAndCreateSchema("MissionPlannerDatabase");
        CreateStruct.createMain();
        Populate.populate();
        AddFiles.testInsertS1AFiles();
//        AddFiles.testInsertS2AFiles();
//        AddFiles.testInsertS1BFiles();
//        AddFiles.testInsertS2BFiles();
           
    }
}
