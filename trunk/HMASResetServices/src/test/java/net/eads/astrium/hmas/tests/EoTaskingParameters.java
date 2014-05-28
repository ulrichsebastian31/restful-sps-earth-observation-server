/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               EoTaskingParameters.java
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
package net.eads.astrium.hmas.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;
import net.opengis.eosps.x20.AcquisitionParametersType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.IncidenceRangeType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.PointingRangeType;
import net.opengis.eosps.x20.RegionOfInterestType;
import net.opengis.eosps.x20.SurveyPeriodDocument;
import net.opengis.eosps.x20.SwathProgrammingRequestType;
import net.opengis.eosps.x20.SwathSegmentType;
import net.opengis.eosps.x20.TaskingParametersDocument;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TimeOfInterestType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.eosps.x20.ValidationParametersType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingDocument;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class EoTaskingParameters {
    
    @Test
    public void testSAR()
    {
        TaskingParametersDocument taskParams = createCoverageRequest();
        addSARParams(taskParams.getTaskingParameters().getCoverageProgrammingRequest());
        
        OGCNamespacesXmlOptions opts;
        try {
            opts = new OGCNamespacesXmlOptions();
        
            System.out.println("" + taskParams.xmlText(opts));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(EoTaskingParameters.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(EoTaskingParameters.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testOPT()
    {
        TaskingParametersDocument taskParams = createCoverageRequest();
        addOPTParams(taskParams.getTaskingParameters().getCoverageProgrammingRequest());

        OGCNamespacesXmlOptions opts;
        try {
            opts = new OGCNamespacesXmlOptions();
        
            System.out.println("" + taskParams.xmlText(opts));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(EoTaskingParameters.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(EoTaskingParameters.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    public static TaskingParametersDocument createCoverageRequest() {
        
        TaskingParametersDocument doc = TaskingParametersDocument.Factory.newInstance();
        TaskingParametersType taskingparams = doc.addNewTaskingParameters();
        CoverageProgrammingRequestType coverage = taskingparams.addNewCoverageProgrammingRequest();
        
        
        // Region Of Interest France
        //  La métropole est comprise entre les latitudes 42°19'46" N et 51°5'47" N, ainsi que les longitudes 4°46' O et 8°14'42" E
        
        RegionOfInterestType roi = coverage.addNewRegionOfInterest();
        PolygonType polygon = roi.addNewPolygon();
        polygon.setId("Region_Of_Interest");
        AbstractRingPropertyType ext = polygon.addNewExterior();
        
        CoordinatesType coords = CoordinatesType.Factory.newInstance();
        coords.setDecimal(".");
        coords.setCs(",");
        coords.setTs(" ");
        coords.setStringValue(
                  "4.46,42.19 "
                + "8.14,42.19 "
                + "8.14,51.05 "
                + "4.46,51.05 "
                + "4.46,42.19");
        
        LinearRingDocument ringDoc = LinearRingDocument.Factory.newInstance();
        LinearRingType lineRing = ringDoc.addNewLinearRing();
        lineRing.setCoordinates(coords);
        
        ext.set(ringDoc);
        
        TimeOfInterestType toi = coverage.addNewTimeOfInterest();
        SurveyPeriodDocument.SurveyPeriod surveyPeriod = toi.addNewSurveyPeriod();
        TimePeriodType tp = surveyPeriod.addNewTimePeriod();
        tp.setId("Survey_Period");
        TimePositionType begin = tp.addNewBeginPosition();
        begin.setStringValue("2013-08-01T00:00:00Z");
        TimePositionType end = tp.addNewEndPosition();
        end.setStringValue("2013-08-10T00:00:00Z");
        
        MonoscopicAcquisitionType mono = coverage.addNewAcquisitionType().addNewMonoscopicAcquisition();
        AcquisitionAngleType acqAngles = mono.addNewAcquisitionAngle();
        IncidenceRangeType incidence = acqAngles.addNewIncidenceRange();
        QuantityRangeType az = incidence.addNewAzimuthAngle();
        az.addNewUom().setCode("deg");
        az.setLabel("Incidence_Azimuth_Angle");
        az.setValue(Arrays.asList(new Object[]{"-90", "90"}));
        QuantityRangeType el = incidence.addNewElevationAngle();
        el.addNewUom().setCode("deg");
        el.setLabel("Incidence_Elevation_Angle");
        el.setValue(Arrays.asList(new Object[]{"-90", "90"}));
        
        PointingRangeType pointing = acqAngles.addNewPointingRange();
        QuantityRangeType ac = pointing.addNewAcrossTrackAngle();
        ac.addNewUom().setCode("deg");
        ac.setLabel("Pointing_Across_Track_Angle");
        ac.setValue(Arrays.asList(new Object[]{"-90", "90"}));
        QuantityRangeType al = pointing.addNewAlongTrackAngle();
        al.addNewUom().setCode("deg");
        al.setLabel("Pointing_Along_Track_Angle");
        al.setValue(Arrays.asList(new Object[]{"-90", "90"}));
        
//        mono.addNewBHRatio();
//        mono.addNewCoverageType();
//        mono.addNewMaxCoupleDelay();
        
        return doc;
    }
    
    
    public static CoverageProgrammingRequestType addOPTParams(CoverageProgrammingRequestType coverage)
    {
        ValidationParametersType valParams = coverage.addNewValidationParameters();
        ValidationParametersOPTType optVP = valParams.addNewValidationParametersOPT();
        
        
        QuantityType cc = optVP.addNewMaxCloudCover();
        cc.setLabel("Cloud_Cover");
        cc.addNewUom().setCode("%");
        cc.setValue(30);
        QuantityType sc = optVP.addNewMaxSnowCover();
        sc.setLabel("Snow_Cover");
        sc.addNewUom().setCode("%");
        sc.setValue(30);
        BooleanType sandWind = optVP.addNewSandWindAccepted();
        sandWind.setLabel("Sand_Wind_Accepted");
        sandWind.setValue(true);
        
        BooleanType haze = optVP.addNewHazeAccepted();
        haze.setLabel("Haze_accepted");
        haze.setValue(true);
        
        QuantityType msg = optVP.addNewMaxSunGlint();
        msg.setLabel("Maximum_Sun_Glint");
        msg.addNewUom().setCode("%");
        msg.setValue(30);
        
        
        AcquisitionParametersType params = coverage.getAcquisitionType().getMonoscopicAcquisitionArray(0).addNewAcquisitionParameters();
        AcquisitionParametersOPTType opt = params.addNewAcquisitionParametersOPT();

        BooleanType fa = opt.addNewFusionAccepted();
        fa.setLabel("Coverage_Type");
        fa.setValue(true);
        
        List resolutions = new ArrayList<Double>();
        resolutions.add((double)10.0);
        resolutions.add((double)100.0);
        QuantityRangeType res = opt.addNewGroundResolution();
        res.setLabel("Ground_Resolution");
        res.addNewUom().setCode("m");
        res.setValue(resolutions);
                
        CategoryType im = opt.addNewInstrumentMode();
        im.setLabel("Instrument_Mode");
        im.setValue("MSI");
        
        QuantityType minLum = opt.addNewMinLuminosity();
        minLum.setLabel("Minimum_Luminosity");
        minLum.addNewUom().setCode("W");
        
        return coverage;
    }
    
    public static CoverageProgrammingRequestType addSARParams(CoverageProgrammingRequestType coverage)
    {
        ValidationParametersType valParams = coverage.addNewValidationParameters();
        ValidationParametersSARType sarVP = valParams.addNewValidationParametersSAR();
        QuantityType mNL = sarVP.addNewMaxNoiseLevel();
        mNL.setLabel("Maximum_Noise_Level");
        mNL.addNewUom().setCode("W");
        mNL.setValue(30);
        QuantityType mAL = sarVP.addNewMaxAmbiguityLevel();
        mAL.setLabel("Maximum_Ambiguity_Level");
        mAL.addNewUom().setCode("W");
        mAL.setValue(30);
        
        AcquisitionParametersType params = coverage.getAcquisitionType().getMonoscopicAcquisitionArray(0).addNewAcquisitionParameters();
        
        AcquisitionParametersSARType sar = params.addNewAcquisitionParametersSAR();
        
        CategoryType im = sar.addNewInstrumentMode();
        im.setLabel("Instrument_Mode");
        im.setValue("SM");
        
        CategoryType pm = sar.addNewPolarizationMode();
        pm.setLabel("Polarization_Mode");
        pm.setValue("HH");
        
        BooleanType fa = sar.addNewFusionAccepted();
        fa.setLabel("Coverage_Type");
        fa.setValue(true);
        
        List resolutions = new ArrayList<Double>();
        resolutions.add((double)10.0);
        resolutions.add((double)100.0);
        QuantityRangeType res = sar.addNewGroundResolution();
        res.setLabel("Ground_Resolution");
        res.addNewUom().setCode("m");
        res.setValue(resolutions);
        
        return coverage;
    }
    
    
    public static SwathProgrammingRequestType createSwathRequest(SwathProgrammingRequestType swath)
    {
        SwathSegmentType segment = swath.addNewSwathSegment();
        
        
        
        
        
        
        
        
        
        
        
        return swath;
    }
}
