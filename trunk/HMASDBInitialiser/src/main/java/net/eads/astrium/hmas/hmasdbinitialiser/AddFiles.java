/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               AddFiles.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.util.DateHandler;

/**
 *
 * @author re-sulrich
 */
public class AddFiles {
    
    
    public static void testInsertS1AFiles() throws ParseException, SQLException, IOException, URISyntaxException {
        
//        String url = "jdbc:postgresql://10.2.200.247:5432/MMFASDatabase";
//        String url = "jdbc:postgresql://192.168.0.20:5432/MMFASDatabase";
//
//        String user = "opensourcedbms";
//        String passwd = "opensourcedbms";
        
        String javaPath = System.getProperty("java.class.path");
        System.out.println("" + javaPath);
        
        try {
            
        
       
//        InputStream stream = getJarEntry(javaPath, "OSF1");
//        System.out.println("");
//        System.out.println(" : " + (stream==null) + "" + stream.available());
        
//        System.out.println("Next");
        
//        String resourcesPath = "resources" + File.separator;
        String resourcesPath = "";
        
        addOSF(
                "00001AAA",
                DateHandler.parseDate("2002-03-01T09:20:01Z"), 
                DateHandler.parseDate("9999-11-30T23:59:59Z"), 
//                AddFiles.class.getResourceAsStream("OSF1")
                getJarEntry(javaPath, "OSF1")
                );

        addSDF(
                "S1ASAR", 
                "SM", 
//                AddFiles.class.getResourceAsStream("SDF1"))
                getJarEntry(javaPath, "SDF1")
                );
    
        addSDF(
                "S1ASAR", 
                "IW", 
//                AddFiles.class.getResourceAsStream("SDF1"))
                getJarEntry(javaPath, "SDF1")
                );
    
        addSDF(
                "S1ASAR", 
                "EW", 
//                AddFiles.class.getResourceAsStream("SDF1"))
                getJarEntry(javaPath, "SDF1")
                );
    
        addSDF(
                "S1ASAR", 
                "WV", 
//                AddFiles.class.getResourceAsStream("SDF1"))
                getJarEntry(javaPath, "SDF1")
                );
    
        addAntennaSDF("00001AAA", 
//                (AddFiles.class.getResourceAsStream("SDFA")))
                getJarEntry(javaPath, "SDFA")
                );
    
        
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void testInsertS2AFiles() throws  Exception {
        
//        String url = "jdbc:postgresql://10.2.200.247:5432/MMFASDatabase";
//        String url = "jdbc:postgresql://192.168.0.20:5432/MMFASDatabase";
//
//        String user = "opensourcedbms";
//        String passwd = "opensourcedbms";
        
        
        String javaPath = System.getProperty("java.class.path");
        System.out.println("" + javaPath);
        
//        String resourcesPath = "resources" + File.separator;
        String resourcesPath = "";
        
        addOSF(
                "00002AAA",
                DateHandler.parseDate("2012-06-27T22:30:01Z"), 
                DateHandler.parseDate("9999-11-30T23:59:59Z"), 
//                AddFiles.class.getResourceAsStream("OSF1"))
                getJarEntry(javaPath, "OSF1")
                );

        addSDF(
                "S2AOPT", 
                "MSI", 
//                AddFiles.class.getResourceAsStream("SDF2"))
                getJarEntry(javaPath, "SDF2")
                );
    
        addAntennaSDF("00002AAA", 
//                (AddFiles.class.getResourceAsStream("SDFA")))
                getJarEntry(javaPath, "SDFA")
                );
    }
    
    public static void testInsertS1BFiles() throws ParseException, SQLException, IOException, URISyntaxException, Exception {
        
//        String url = "jdbc:postgresql://10.2.200.247:5432/MMFASDatabase";
//        String url = "jdbc:postgresql://192.168.0.20:5432/MMFASDatabase";
//
//        String user = "opensourcedbms";
//        String passwd = "opensourcedbms";
        
        String javaPath = System.getProperty("java.class.path");
        System.out.println("" + javaPath);
        
//        String resourcesPath = "resources" + File.separator;
        String resourcesPath = "";
        
        addOSF(
                "00001BBB",
                DateHandler.parseDate("2002-03-01T09:20:01Z"), 
                DateHandler.parseDate("9999-11-30T23:59:59Z"), 
//                (AddFiles.class.getResourceAsStream("OSF2"))
                getJarEntry(javaPath, "OSF2")
                );

        addSDF(
                "S1BSAR", 
                "SM", 
//                AddFiles.class.getResourceAsStream("SDF1"));
                getJarEntry(javaPath, "SDF1")
                );
    
        addSDF(
                "S1BSAR", 
                "IW", 
//                AddFiles.class.getResourceAsStream("SDF1"));
                getJarEntry(javaPath, "SDF1")
                );
    
        addSDF(
                "S1BSAR", 
                "EW", 
//                AddFiles.class.getResourceAsStream("SDF1"));
                getJarEntry(javaPath, "SDF1")
                );
    
        addSDF(
                "S1BSAR", 
                "WV", 
//                AddFiles.class.getResourceAsStream("SDF1")
                getJarEntry(javaPath, "SDF1")
                );
    
        addAntennaSDF("00001BBB", 
//                (AddFiles.class.getResourceAsStream("SDFA")))
                getJarEntry(javaPath, "SDFA")
                );
    }
    
    public static void testInsertS2BFiles() throws  Exception {
        
//        String url = "jdbc:postgresql://10.2.200.247:5432/MMFASDatabase";
//        String url = "jdbc:postgresql://192.168.0.20:5432/MMFASDatabase";
//
//        String user = "opensourcedbms";
//        String passwd = "opensourcedbms";
        
        String resourcesPath = "";
        
        String javaPath = System.getProperty("java.class.path");
        System.out.println("" + javaPath);
        
        addOSF(
                "00002BBB",
                DateHandler.parseDate("2012-06-27T22:30:01Z"), 
                DateHandler.parseDate("9999-11-30T23:59:59Z"), 
//                (AddFiles.class.getResourceAsStream(
//                "OSF2")));
                getJarEntry(javaPath, "OSF2")
                );

        addSDF(
                "S2BOPT", 
                "MSI", 
//                (AddFiles.class.getResourceAsStream("SDF2")));
                getJarEntry(javaPath, "SDF2")
                );
    
        addAntennaSDF("00002BBB", 
//                (AddFiles.class.getResourceAsStream("SDFA")));
                getJarEntry(javaPath, "SDFA")
                );
    }
    
    public static void addPCTTable(String meteoServerId, InputStream content) throws SQLException, IOException {
        
        String url = "jdbc:postgresql://" + TestConnexionParameter.getUrl() + "/HMASDatabase";
        String user = TestConnexionParameter.getUser();
        String passwd = TestConnexionParameter.getPass();
            
        System.out.println("URL : " + url);
        DBOperations dboperations = new DBOperations(url, user, passwd, "MissionPlannerDatabase");
        
        String table = "WMSServer";
        
        //Insert file content
        String idColumn = "msId";
        String fileColumn = "pctTable";
//        InputStream fileContent = new FileInputStream(content);
//        long fileSize = content.length();
        System.out.println("");
        System.out.println("Content available : " + content.available());
        System.out.println("");
        dboperations.insertFileInExistingId(table, idColumn, fileColumn, meteoServerId, content, content.available());
    }
    
    public static void addOSF(String satelliteId, Date start, Date end, InputStream content) throws SQLException, IOException {
        
        String url = "jdbc:postgresql://" + TestConnexionParameter.getUrl() + "/HMASDatabase";
        String user = TestConnexionParameter.getUser();
        String passwd = TestConnexionParameter.getPass();
            
        System.out.println("URL : " + url);
        DBOperations dboperations = new DBOperations(url, user, passwd, "MissionPlannerDatabase");
        
        String table = "OSF";
        
        //Create new OSF with validity times
        List<String> fields = new ArrayList<String>();
        fields.add("validityStart");
        fields.add("validityStop");
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'"+ DateHandler.formatDate(start)+"'");
        depl1.add("'"+ DateHandler.formatDate(end)+"'");
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        String osfId = dboperations.insertReturningId(table, fields, values, "osfId");
        
        //Link OSF to satellite
        table = "LNK_Satellite_OSF";
        fields = new ArrayList<String>();
        fields.add("satelliteId");
        fields.add("osfId");
        depl1 = new ArrayList<String>();
        depl1.add("'"+satelliteId+"'");
        depl1.add(""+osfId+"");
        values = new ArrayList<List<String>>();
        values.add(depl1);
        dboperations.insert(table, fields, values);
        
        //Insert file content
        table = "OSF";
        String idColumn = "osfId";
        String fileColumn = "content";
//        InputStream fileContent = new FileInputStream(content);
//        long fileSize = content.length();
        System.out.println("");
        System.out.println("Content available : " + content.available());
        System.out.println("");
        dboperations.insertFileInExistingId(table, idColumn, fileColumn, osfId, content, content.available());
    }
    
    public static void addSDF(String sensorId, String instModeId, InputStream content) throws SQLException, IOException {
        
        String url = "jdbc:postgresql://" + TestConnexionParameter.getUrl() + "/HMASDatabase";
        String user = TestConnexionParameter.getUser();
        String passwd = TestConnexionParameter.getPass();
            
        
        DBOperations dboperations = new DBOperations(url, user, passwd, "MissionPlannerDatabase");
        
        String table = "SDF";
        
        //Create new SDF with validity times
        List<String> fields = new ArrayList<String>();
        fields.add("sensorId");
        fields.add("imId");
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'"+ sensorId+"'");
        depl1.add("'"+ instModeId+"'");
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);
        String sdfId = dboperations.insertReturningId(table, fields, values, "sdfId");
        
        
        
        //Insert file content
        table = "SDF";
        String idColumn = "sdfId";
        String fileColumn = "content";
//        InputStream fileContent = new FileInputStream(content);
//        long fileSize = content.length();
        System.out.println("");
        System.out.println("Content available : " + content.available());
        System.out.println("");
        dboperations.insertFileInExistingId(table, idColumn, fileColumn, sdfId, content, content.available());
    }
    
    public static void addAntennaSDF(String satelliteId, InputStream content) throws SQLException, IOException {
        
        String url = "jdbc:postgresql://" + TestConnexionParameter.getUrl() + "/HMASDatabase";
        String user = TestConnexionParameter.getUser();
        String passwd = TestConnexionParameter.getPass();
            
        
        DBOperations dboperations = new DBOperations(url, user, passwd, "MissionPlannerDatabase");
        
        String table = "SatellitePlatform";
        
        dboperations.insertFileInExistingId(table, "satelliteId", "antennaSdfFileContent", "" + satelliteId + "", content, content.available());
        
    }
    
    
    public static void getJarEntries(String inputPath) throws Exception {
        
//        String inputPath = "path/to/.jar";
//        String outputPath = "Desktop/CopyText.txt";

//        File resStreamOut = new File(outputPath);

         int readBytes;
         JarFile file = new JarFile(inputPath);

//         FileWriter fw = new FileWriter(resStreamOut);

        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
//        if (entry.getName().equals("readMe/tempReadme.txt")) {

                System.out.println("Entry : " + entry);
                
//            InputStream is = file.getInputStream(entry);
//            while ((readBytes = is.read()) != -1) {
//                
//            }
//        }
        } 
    }
    
    public static InputStream getJarEntry(String inputPath, String fileName) throws Exception {
        
        InputStream is = null;
        
//        String inputPath = "path/to/.jar";
//        String outputPath = "Desktop/CopyText.txt";

//        File resStreamOut = new File(outputPath);

         int readBytes;
         JarFile file = new JarFile(inputPath);

//         FileWriter fw = new FileWriter(resStreamOut);

        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
//            if (!entry.getName().contains("/"))
//                System.out.println("" + entry.getName());
            
//            System.out.println("" + entry.getName() + " - " + fileName +   "equals ? " + entry.getName().equals(fileName));
        if (entry.getName().equals(fileName)) {

            System.out.println("Entry : " + entry);

            is = file.getInputStream(entry);
//            while ((readBytes = is.read()) != -1) {
//                
//            }
            System.out.println("IS : " + is.available());
        }
        }
//        BufferedReader input = new BufferedReader(new InputStreamReader(is));
//        System.out.println("" + input.readLine() + "\r\n" + input.readLine());
        return is;
    }
    
}
