/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Populate.java
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static net.eads.astrium.hmas.hmasdbinitialiser.AddFiles.getJarEntry;
import static net.eads.astrium.hmas.hmasdbinitialiser.TestConnexion.insert;
import net.eads.astrium.hmas.util.structures.GroundStation;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class Populate {
    
    
    
    //The data about the Sentinel 1 mission has been collected on :
    // https://directory.eoportal.org/web/eoportal/satellite-missions/c-missions/copernicus-sentinel-1
    
    
    public static void populate() {
        
        try {
            
            addStationsFromXMLFile();
            addFucinoUnavailibilities();
            
            addSentinel1InstrumentModes();
            addSatellites();
            addSentinel1PolarizationModes();
            addInstrumentModesPolarisationModesJointures();

            
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void addStationsFromXMLFile() throws Exception {
        
        String javaPath = System.getProperty("java.class.path");
        System.out.println("" + javaPath);
        InputStream is = getJarEntry(javaPath, "stations.xml");
        
        List<GroundStation> stations = new ArrayList<GroundStation>();
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
//        String filePath = "C:\\Users\\re-sulrich\\.dream\\fas_old\\stations.xml";
//        
//        File inputDataFile = new File(filePath);
//        
//        if (!inputDataFile.exists())
//        {
//        	throw new FileNotFoundException("File " + filePath + " could not be found.");
//        }
        
        Document doc = documentBuilder.parse(is);
        
        System.out.println( "" + doc.getFirstChild().getNodeName() );
        
        System.out.println("");
        
        for (int i = 0; i < doc.getFirstChild().getChildNodes().getLength(); i++) {
            Node dataBlock = doc.getFirstChild().getChildNodes().item(i);
            String name = dataBlock.getNodeName();
            System.out.println(""+ name);
            
            if (name.contains("Data_Block"))
            {
                for (int j = 0; j < dataBlock.getChildNodes().getLength(); j++) {
                    Node stationsList = dataBlock.getChildNodes().item(j);
                    
                    System.out.println(" - " + stationsList.getNodeName());
                    
                    if (stationsList.getNodeName().contains("List_of_Ground_Stations"))
                    {
                        for (int k = 0; k < stationsList.getChildNodes().getLength(); k++) {
                            Node stat = stationsList.getChildNodes().item(k);
                            
                            if (stat.getNodeName().contains("Ground_Station"))
                            {
                                GroundStation station = new GroundStation();
                                
                                for (int l = 0; l < stat.getChildNodes().getLength(); l++) {
                                    Node property = stat.getChildNodes().item(l);
                                    
                                    String propName = property.getNodeName();
                                    
                                    if (propName.contains("Station_id"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setId(value);
                                    }
                                    if (propName.contains("Descriptor"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setName(value);
                                    }
                                    if (propName.contains("Type"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setType(value);
                                    }
                                    if (propName.contains("Antenna"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setAntennaType(value);
                                    }
                                    if (propName.contains("Purpose"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        station.setPurpose(value);
                                    }
                                    if (propName.contains("Default_El"))
                                    {
                                        String value = property.getTextContent();
                                        System.out.println("" + value);
                                        try {
                                            station.setDefaultElevation(Double.valueOf(value));
                                        } catch (Exception e) {
                                            station.setDefaultElevation(0.0);
                                        }
                                    }
                                    if (propName.contains("Location"))
                                    {
                                        System.out.println("" +  property.getChildNodes().getLength());
                                        for (int m = 0; m < property.getChildNodes().getLength(); m++) {
                                            Node coord = property.getChildNodes().item(m);
                                            String coordName = coord.getNodeName();
                                            
                                            if (coordName.contains("Long"))
                                            {
                                                String value = coord.getTextContent();
                                                
                                                System.out.println("" + Double.valueOf(value));
                                                station.setLon(Double.valueOf(value));
                                            }
                                            if (coordName.contains("Lat"))
                                            {
                                                String value = coord.getTextContent();
                                                
                                                System.out.println("" + Double.valueOf(value));
                                                station.setLat(Double.valueOf(value));
                                            }
                                            if (coordName.contains("Alt"))
                                            {
                                                String value = coord.getTextContent();
                                                
                                                System.out.println("" + Double.valueOf(value));
                                                station.setAlt(Double.valueOf(value));
                                            }
                                        }
                                    } // End if (propName.contains("Location"))
                                    if (propName.contains("List_of_Mask_Points")) {
                                        NodeList maskPoints = property.getChildNodes();
                                        
                                        for (int m = 0; m < maskPoints.getLength(); m++) {
                                            Node maskPoint = maskPoints.item(m);
                                            
                                            if (maskPoint.getNodeName().equals("Mask_Point")) {
                                                NodeList nodes = maskPoint.getChildNodes();
                                                
                                                double az = 0.0;
                                                double el = 0.0;
                                                for (int n = 0; n < nodes.getLength(); n++) {
                                                    Node node = nodes.item(n);
                                                    
                                                    if (node.getNodeName().equals("Az")) {
                                                        az = Double.valueOf(node.getTextContent());
                                                    }
                                                    if (node.getNodeName().equals("El")) {
                                                        el = Double.valueOf(node.getTextContent());
                                                    }
                                                }
                                                station.addMaskPoint(az, el);
                                                
                                            }
                                            
                                        }
                                    }//End if (propName.contains("Antenna")) {
                                } // End for (int l = 0; l < stat.getChildNodes().getLength(); l++)
                                
                                stations.add(station);
                            }
                        } // End for(int k = 0; k < stationsList.getChildNodes().getLength(); k++)
                    }
                }
            }
        } // End for (int i = 0; i < doc.getFirstChild().getChildNodes().getLength(); i++)
        
        List<String> fields = new ArrayList<String>();
        fields.add("groundStationId");
        fields.add("name");
        fields.add("longitude");
        fields.add("latitude");
        fields.add("altitude");
        fields.add("type");
        fields.add("antennaType");
        fields.add("purpose");
        fields.add("defaultElevation");
        fields.add("maskPointsList");
        for (GroundStation groundStation : stations) {
            
            List<String> value = new ArrayList<String>();
            value.add("'" + groundStation.getId()+ "'");
            value.add("'" + groundStation.getName() + "'");
            value.add("" + groundStation.getLon());
            value.add("" + groundStation.getLat());
            value.add("" + groundStation.getAlt());
            value.add("'" + groundStation.getType()+ "'");
            value.add("'" + groundStation.getAntennaType() + "'");
            value.add("'" + groundStation.getPurpose()+ "'");
            value.add("" + groundStation.getDefaultElevation());
            
            String listOfMaskPoints = "'";
        
            if (groundStation.getMaskPoints() != null && groundStation.getMaskPoints().size() > 0)
            for (Point point : groundStation.getMaskPoints()) {
                listOfMaskPoints += "" + point.getLongitude() + "," + point.getLatitude() + " ";
            }
            
            listOfMaskPoints += "'";
            
            value.add(listOfMaskPoints);
            
            List<List<String>> values = new ArrayList<List<String>>();
            values.add(value);
            try {
                TestConnexion.insert("GroundStation", fields, values);
            } catch (SQLException ex) {
//                Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static void addFucinoUnavailibilities() throws SQLException {
        
        String gsId = "'GFUCINBX'";
        
        List<String> fields = new ArrayList<String>();
        fields.add("beginU");
        fields.add("endU");
        fields.add("cause");
        fields.add("groundStationId");
        
        List<String> uStat1 = new ArrayList<String>();
        uStat1.add("'2013-08-12T00:00:00Z'");
        uStat1.add("'2013-08-13T00:00:00Z'");
        uStat1.add("'Maintenance'");
        uStat1.add(gsId);
        
        List<String> uStat2 = new ArrayList<String>();
        uStat2.add("'2013-08-01T06:00:00Z'");
        uStat2.add("'2013-08-01T18:00:00Z'");
        uStat2.add("'Occupied'");
        uStat2.add(gsId);
        
        List<String> uStat3 = new ArrayList<String>();
        uStat3.add("'2013-08-23T00:00:00Z'");
        uStat3.add("'2013-08-24T12:00:00Z'");
        uStat3.add("'Maintenance'");
        uStat3.add(gsId);
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(uStat1);
        values.add(uStat2);
        values.add(uStat3);
        
        insert(
                "Unavailibility", 
                fields, 
                values);
    }
    
    
    
    
    
    
    
//    @Test
    public static void addSatellites() throws SQLException {
        String table = "SatellitePlatform";
        
        List<String> fields = new ArrayList<String>();
        fields.add("satelliteId");
        fields.add("noradName");
        fields.add("name");
        fields.add("description");
        fields.add("href");


        List<String> sentinel1a = new ArrayList<String>();
        sentinel1a.add("'00001AAA'");
        sentinel1a.add("'Sentinel1A'");
        sentinel1a.add("'Sentinel 1 A'");
        sentinel1a.add("'Sentinel 1 A is a SAR satellite part of the Sentinel project.'");
        sentinel1a.add("'http://www.esa.int/Our_Activities/Observing_the_Earth/GMES/Sentinel-1'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel1a);
        
        insert(
                table, 
                fields, 
                values);
        
        
        
            addSentinel1Orbit("00001AAA");
            addSentinel1Sensor("00001AAA");
            addSentinel1GS("00001AAA");
            
            
            
    }
    
    public static void addSentinel1Orbit(String satelliteId) throws SQLException {
        
        String table = "Orbit";
        
        List<String> fields = new ArrayList<String>();
        
        fields.add("orbitType");
        fields.add("low_Min_Semi_Major_Axis");
        fields.add("low_Max_Semi_Major_Axis");
        fields.add("low_Min_Inclination");
        fields.add("low_Max_Inclination");
        fields.add("low_Min_Eccentricity");
        fields.add("low_Max_Eccentricity");
        fields.add("tight_Min_Semi_Major_Axis");
        fields.add("tight_Max_Semi_Major_Axis");
        fields.add("tight_Min_Inclination");
        fields.add("tight_Max_Inclination");
        fields.add("tight_Min_Eccentricity");
        fields.add("tight_Max_Eccentricity");
        fields.add("orbit_Min_Semi_Major_Axis");
        fields.add("orbit_Nom_Semi_Major_Axis");
        fields.add("orbit_Max_Semi_Major_Axis");
        fields.add("orbit_Min_Inclination");
        fields.add("orbit_Nom_Inclination");
        fields.add("orbit_Max_Inclination");
        fields.add("orbit_Nom_Eccentricity");
        fields.add("orbit_Nom_Arg_Perigee");
        
        fields.add("satelliteId");
        
        List<String> sent1 = new ArrayList<String>();
        
        sent1.add("'SSO'");
        sent1.add("7000000.0");
        sent1.add("7090000.0");
        sent1.add("97.68");
        sent1.add("98.49");
        sent1.add("0.0");
        sent1.add("0.5");
        sent1.add("7016000.0");
        sent1.add("7076000.0");
        sent1.add("97.78");
        sent1.add("98.39");
        sent1.add("0.0");
        sent1.add("0.007");
        sent1.add("7026000.0");
        sent1.add("7046931.569095");
        sent1.add("7066000.0");
        sent1.add("97.98");
        sent1.add("98.085948");
        sent1.add("98.18");
        sent1.add("0.001189");
        sent1.add("90.0");
        
        sent1.add("'"+satelliteId+"'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sent1);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    public static void addSentinel1Sensor(String satelliteId) throws SQLException {
        
        String sensorId = null;
        if (satelliteId.equals("00001AAA")) {
            sensorId = "S1ASAR";
        }
        else {
            sensorId = "S1BSAR";
        }
        
        String table = "Sensor";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("sensorId");
        fields.add("name");
        fields.add("sensorDescription");
        fields.add("type");
        fields.add("bandType");
        fields.add("minLatitude");
        fields.add("maxLatitude");
        fields.add("minLongitude");
        fields.add("maxLongitude");
        
        fields.add("mass");
        fields.add("maxPowerConsumption");
        fields.add("acqMethod");
        fields.add("applications");
        
        fields.add("satelliteId");


        List<String> sentinel1 = new ArrayList<String>();
        sentinel1.add("'"+sensorId+"'");
        sentinel1.add("'Sentinel 1 SAR'");
        sentinel1.add("'The Sentinel-1 SAR mission is part of the GMES system, which is designed to\n" +
                "provide an independent and operational information capacity to the European Union\n" +
                "to warrant environment and security policies and to support sustainable economic\n" +
                "growth. In particular, the mission will provide timely and high quality remote\n" +
                "sensing data to support monitoring the open ocean and the changes to marine and\n" +
                "coastal environmental conditions.'");
        sentinel1.add("'SAR'");
        sentinel1.add("'C-BAND'");
        sentinel1.add("-90");
        sentinel1.add("90");
        sentinel1.add("-180");
        sentinel1.add("180");
        
        sentinel1.add("880");
        sentinel1.add("4368");
        sentinel1.add("'C-band SAR'");
        sentinel1.add("'C-band SAR all-weather imaging capability'");
        
        sentinel1.add("'"+satelliteId+"'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sentinel1);
        
        insert(
                table, 
                fields, 
                values);
        
        addSentinel1SARSensorChar(sensorId);
        addSentinel1InstrumentModesSarChar(sensorId);
        
        addSentinel1Unavailibilities(sensorId);

    }
    
    public static void addSentinel1SARSensorChar(String sensorId) throws SQLException {
        
        
        String table = "SarSensorCharacteristics";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        fields.add("antennaLength");
        fields.add("antennaWidth");
        fields.add("groundLocationAccuracy");
        fields.add("revisitTimeInDays");
        fields.add("transmitFrequencyBand");
        fields.add("transmitCenterFrequency");
        
        fields.add("sensorId");
        
        List<String> s1Char = new ArrayList<String>();
        s1Char.add("12.3");
        s1Char.add("0.82");
        s1Char.add("0.0");
        s1Char.add("3");
        s1Char.add("'C'");
        s1Char.add("5.405");
        //Sensor 1 = sentinel1
        s1Char.add("'"+sensorId+"'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(s1Char);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    public static void addSentinel1InstrumentModes() throws SQLException {
        
        String table = "InstrumentMode";
        
        List<String> fields = new ArrayList<String>();
        fields.add("imId");
        fields.add("name");
        fields.add("description");

        List<String> sm = new ArrayList<String>();
        sm.add("'SM'");
        sm.add("'SM'");
        sm.add("'The conventional SAR strip mapping mode assumes a fixed pointing direction \n" +
                "of the radar antenna broadside to the platform track. \n" +
                "A strip map is an image formed in width by the swath of the SAR and \n" +
                "follows the length contour of the flight line of the platform itself. \n" +
                "A detailed description of this mode you will find on the topic SLAR.'");
        
        List<String> iw = new ArrayList<String>();
        iw.add("'IW'");
        iw.add("'IW'");
        iw.add("'The IW mode acquires data of wide swaths (composed of 3 sub-swaths),\n" +
                "at the expense of resolution, using the TOPSAR imaging technique. The TOPSAR\n" +
                "imaging is a form of ScanSAR imaging (the antenna beam is switched cyclically\n" +
                "among the three sub-swaths) where, for each burst, the beam\n" +
                "is electronically steered from backward to forward in the azimuth direction.\n" +
                "This leads to uniform NESZ and ambiguity levels within the\n" +
                "scan bursts, resulting in a higher quality image.'");
        
        List<String> ew = new ArrayList<String>();
        ew.add("'EW'");
        ew.add("'EW'");
        ew.add("'The EW mode uses the TOPSAR imaging technique.\n" +
                "The EW mode provides a very large swath coverage (obtained\n" +
                "from imaging 5 sub-swaths) at the expense of a further reduction in resolution.\n" +
                "The EW mode is a TOPSAR single sweep mode. The TOPSAR\n" +
                "imaging is a form of ScanSAR imaging (the antenna beam is switched cyclically\n" +
                "among the three sub-swaths) where, for each burst, the beam\n" +
                "is electronically steered from backward to forward in the azimuth direction.\n" +
                "This leads to uniform NESZ and ambiguity levels within the\n" +
                "scan bursts, resulting in a higher quality image.'");
        
        List<String> wv = new ArrayList<String>();
        wv.add("'WV'");
        wv.add("'WV'");
        wv.add("'The Synthetic Aperture Radar can be operated in wave mode. \n"
                + "The primary purpose is to measure directional wave spectra - wave energy \n"
                + "as a function of the directions and lengths of waves at the ocean surface - \n"
                + "from the backscattered radiation from sample areas. \n"
                + "For this function the SAR collects data at spatial intervals of either 200 km \n"
                + "(nominally) or 300 km anywhere within the swath available to the SAR mode \n"
                + "(100 km wide) in steps of approximately 2 km.'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(sm);
        values.add(iw);
        values.add(ew);
        values.add(wv);
        insert(
                table, 
                fields, 
                values);
    }
    
    public static void addSentinel1InstrumentModesSarChar(String sensorId) throws SQLException {
        
        
        String table = "SarImCharacteristics";
        
        List<String> fields = new ArrayList<String>();
//        fields.add("sdffile");
        
        fields.add("acrossTrackResolution");
        fields.add("alongTrackResolution");
        
        fields.add("maxPowerConsumption");
        fields.add("minAcrossTrackAngle");
        fields.add("maxAcrossTrackAngle");
        fields.add("minAlongTrackAngle");
        fields.add("maxAlongTrackAngle");
        fields.add("swathWidth");
        fields.add("radiometricResolution");
        fields.add("minRadioStab");
        fields.add("maxRadioStab");
        fields.add("minAlongTrackAmbRatio");
        fields.add("maxAlongTrackAmbRatio");
        fields.add("minAcrossTrackAmbRatio");
        fields.add("maxAcrossTrackAmbRatio");
        fields.add("minNoiseEquivalentSO");
        fields.add("maxNoiseEquivalentSO");
        
        //Foreign key
        fields.add("imId");
        fields.add("sensorId");
        
        // Stripmap (SM)
        List<String> s1Char = new ArrayList<String>();
        
        s1Char.add("5");
        s1Char.add("5");
        
        s1Char.add("4368");
        s1Char.add("20");
        s1Char.add("45");
        s1Char.add("20");
        s1Char.add("45");
        s1Char.add("80");
        s1Char.add("1");
        s1Char.add("1");
        s1Char.add("1");
        s1Char.add("-22");
        s1Char.add("-25");
        s1Char.add("-22");
        s1Char.add("-25");
        s1Char.add("-22");
        s1Char.add("-22");
        
        s1Char.add("'SM'");
        s1Char.add("'"+sensorId+"'");
        
        
        
        // Interferometric Wide Swath (IWS)
        List<String> s2Char = new ArrayList<String>();
        
        s2Char.add("5");
        s2Char.add("20");
        
        s2Char.add("4075");
        s2Char.add("25");
        s2Char.add("90");
        s2Char.add("25");
        s2Char.add("90");
        s2Char.add("250");
        s2Char.add("1");
        s2Char.add("1");
        s2Char.add("1");
        s2Char.add("-22");
        s2Char.add("-25");
        s2Char.add("-22");
        s2Char.add("-25");
        s2Char.add("-22");
        s2Char.add("-22");
        
        s2Char.add("'IW'");
        s2Char.add("'"+sensorId+"'");
        
        
        
        
        // Extra Wide Swath (EWS)
        List<String> s3Char = new ArrayList<String>();
        
        s3Char.add("25");
        s3Char.add("40");
        
        s3Char.add("4368");
        s3Char.add("20");
        s3Char.add("90");
        s3Char.add("20");
        s3Char.add("90");
        s3Char.add("400");
        s3Char.add("1");
        s3Char.add("1");
        s3Char.add("1");
        s3Char.add("-22");
        s3Char.add("-25");
        s3Char.add("-22");
        s3Char.add("-25");
        s3Char.add("-22");
        s3Char.add("-22");
        
        s3Char.add("'EW'");
        s3Char.add("'"+sensorId+"'");
        
        
        
        
        // Wave mode (WM)
        List<String> s4Char = new ArrayList<String>();
        
        s4Char.add("5");
        s4Char.add("5");
        
        s4Char.add("4368");
        s4Char.add("23");
        s4Char.add("36.5");
        s4Char.add("20");
        s4Char.add("45");
        s4Char.add("20");
        s4Char.add("1");
        s4Char.add("1");
        s4Char.add("1");
        s4Char.add("-22");
        s4Char.add("-25");
        s4Char.add("-22");
        s4Char.add("-25");
        s4Char.add("-22");
        s4Char.add("-22");
        
        s4Char.add("'WV'");
        s4Char.add("'"+sensorId+"'");
        
        
        
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(s1Char);
        values.add(s2Char);
        values.add(s3Char);
        values.add(s4Char);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    public static void addSentinel1PolarizationModes() throws SQLException {
        
        String table = "PolarisationMode";
        
        List<String> fields = new ArrayList<String>();
        fields.add("pmId");
        fields.add("name");
        fields.add("description");

        List<String> hh = new ArrayList<String>();
        hh.add("'HH'");
        hh.add("'HH'");
        hh.add("'Single horizontal mode'");
        List<String> vv = new ArrayList<String>();
        vv.add("'VV'");
        vv.add("'VV'");
        vv.add("'Single vertical mode'");
        List<String> hv = new ArrayList<String>();
        hv.add("'HV'");
        hv.add("'HV'");
        hv.add("'Dual horizontal vertical mode'");
        List<String> vh = new ArrayList<String>();
        vh.add("'VH'");
        vh.add("'VH'");
        vh.add("'Dual vertical horizontal mode'");
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(hh);
        values.add(vv);
        values.add(hv);
        values.add(vh);
        
        insert(
                table, 
                fields, 
                values);
    }
    
    public static void addInstrumentModesPolarisationModesJointures() throws SQLException {

        
        String table = "lnk_im_pm";
        
        String[] ims = {"'SM'","'IW'","'EW'","'WV'"};
        String[] sensors = {"'S1ASAR'"};
        String[] pms = {"'HH'","'VV'","'HV'","'VH'"};
        
        List<String> fields = new ArrayList<String>();
        fields.add("imId");
        fields.add("sensorId");
        fields.add("pmId");

        List<List<String>> values = new ArrayList<List<String>>();
        for (int s = 0; s < sensors.length; s++)
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++) {
                
                List<String> hh = new ArrayList<String>();
                hh.add("" + ims[i]);
                hh.add("" + sensors[s]);
                hh.add("" + pms[j]);
                values.add(hh);
            }
            
            List<String> hh = new ArrayList<String>();
            hh.add("" + ims[3]);
            hh.add("" + sensors[s]);
            hh.add("" + pms[0]);
            values.add(hh);
            
            List<String> vv = new ArrayList<String>();
            vv.add("" + ims[3]);
            vv.add("" + sensors[s]);
            vv.add("" + pms[1]);
            values.add(vv);
        }
        
        insert(
                table, 
                fields, 
                values);
    }
    
    public static void addSentinel1Unavailibilities(String sensorId) throws SQLException {
        
//        String sensorId = "'S1SAR'";
        
        List<String> fields = new ArrayList<String>();
        fields.add("beginU");
        fields.add("endU");
        fields.add("cause");
        fields.add("sensorId");
        
        List<String> uStat1 = new ArrayList<String>();
        uStat1.add("'2013-08-15T00:00:00Z'");
        uStat1.add("'2013-08-16T00:00:00Z'");
        uStat1.add("'Maintenance'");
        uStat1.add("'" + sensorId + "'");
        
        List<String> uStat2 = new ArrayList<String>();
        uStat2.add("'2013-08-19T06:00:00Z'");
        uStat2.add("'2013-08-19T18:00:00Z'");
        uStat2.add("'Occupied'");
        uStat2.add("'" + sensorId + "'");
        
        List<String> uStat3 = new ArrayList<String>();
        uStat3.add("'2013-08-21T00:00:00Z'");
        uStat3.add("'2013-08-21T12:00:00Z'");
        uStat3.add("'Occupied'");
        uStat3.add("'" + sensorId + "'");
        
        
        
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(uStat1);
        values.add(uStat2);
        values.add(uStat3);
        
        insert(
                "Unavailibility", 
                fields, 
                values);
    }
    
    public static void addSentinel1GS(String satelliteId) throws SQLException {
        
        String[] gss = new String[]{"GFUCINBX", "GKIRUNBX", "GMASPABX"};
        
        List<String> fields = new ArrayList<String>();
        fields.add("satelliteId");
        fields.add("groundStationId");
        
        List<List<String>> values = new ArrayList<List<String>>();
        
        for (int i = 0; i < gss.length; i++) {
            String gs = gss[i];
            
            
            List<String> lnk = new ArrayList<String>();
            lnk.add("'"+satelliteId+"'");
            lnk.add("'"+gs+"'");
            
            values.add(lnk);
        }
        insert(
                "LNK_Satellite_GroundStation", 
                fields, 
                values);
    }
}
