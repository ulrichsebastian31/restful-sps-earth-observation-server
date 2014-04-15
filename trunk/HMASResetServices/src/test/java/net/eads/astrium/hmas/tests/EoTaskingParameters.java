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
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class EoTaskingParameters {
    
    @Test
    public void test()
    {
        TaskingParametersDocument taskParams = createCoverageRequest();
        
        System.out.println("" + taskParams.xmlText());
    }
    
    
    
    
    
    
    public static TaskingParametersDocument createCoverageRequest() {
        
        TaskingParametersDocument doc = TaskingParametersDocument.Factory.newInstance();
        TaskingParametersType taskingparams = doc.addNewTaskingParameters();
        CoverageProgrammingRequestType coverage = taskingparams.addNewCoverageProgrammingRequest();
        
        
        // Region Of Interest France
        //  La métropole est comprise entre les latitudes 42°19'46" N et 51°5'47" N, ainsi que les longitudes 4°46' O et 8°14'42" E
        
        RegionOfInterestType roi = coverage.addNewRegionOfInterest();
        PolygonType polygon = roi.addNewPolygon();
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
        LinearRingType lineRing = LinearRingType.Factory.newInstance();
        lineRing.setCoordinates(coords);
        ext.setAbstractRing(lineRing);
        
        TimeOfInterestType toi = coverage.addNewTimeOfInterest();
        SurveyPeriodDocument.SurveyPeriod surveyPeriod = toi.addNewSurveyPeriod();
        TimePeriodType tp = surveyPeriod.addNewTimePeriod();
        TimePositionType begin = tp.addNewBeginPosition();
        begin.setStringValue("2013-08-01T00:00:00Z");
        TimePositionType end = tp.addNewEndPosition();
        end.setStringValue("2013-08-10T00:00:00Z");
        
        MonoscopicAcquisitionType mono = coverage.addNewAcquisitionType().addNewMonoscopicAcquisition();
        AcquisitionAngleType acqAngles = mono.addNewAcquisitionAngle();
        IncidenceRangeType incidence = acqAngles.addNewIncidenceRange();
        incidence.addNewAzimuthAngle().setValue(Arrays.asList(new Object[]{"-90", "90"}));
        incidence.addNewElevationAngle().setValue(Arrays.asList(new Object[]{"-90", "90"}));
        PointingRangeType pointing = acqAngles.addNewPointingRange();
        pointing.addNewAcrossTrackAngle().setValue(Arrays.asList(new Object[]{"-90", "90"}));
        pointing.addNewAlongTrackAngle().setValue(Arrays.asList(new Object[]{"-90", "90"}));
        
        mono.addNewBHRatio();
        mono.addNewCoverageType();
        mono.addNewMaxCoupleDelay();
        
        return doc;
    }
    
    
    public static CoverageProgrammingRequestType addOPTParams(CoverageProgrammingRequestType coverage)
    {
        ValidationParametersType valParams = coverage.addNewValidationParameters();
        ValidationParametersOPTType optVP = valParams.addNewValidationParametersOPT();
        
        QuantityType cc = optVP.addNewMaxCloudCover();
        cc.addNewUom().setCode("%");
        cc.setValue(30);
        QuantityType sc = optVP.addNewMaxSnowCover();
        sc.addNewUom().setCode("%");
        sc.setValue(30);
        BooleanType sandWind = optVP.addNewSandWindAccepted();
        
        sandWind.setValue(true);
        
        BooleanType haze = optVP.addNewHazeAccepted();
        haze.setValue(true);
        
        QuantityType msg = optVP.addNewMaxSunGlint();
        msg.addNewUom().setCode("%");
        msg.setValue(30);
        
        
        AcquisitionParametersType params = coverage.getAcquisitionType().getMonoscopicAcquisitionArray(0).addNewAcquisitionParameters();
        AcquisitionParametersOPTType opt = params.addNewAcquisitionParametersOPT();
        opt.addNewFusionAccepted().setValue(true);
        opt.addNewInstrumentMode().setValue("MSI");
        
        List resolutions = new ArrayList<Double>();
        resolutions.add((double)10.0);
        resolutions.add((double)100.0);
        QuantityRangeType res = opt.addNewGroundResolution();
        res.setValue(resolutions);
        res.addNewUom().setCode("m");
        QuantityType minLum = opt.addNewMinLuminosity();
        minLum.addNewUom().setCode("W");
        
        return coverage;
    }
    
    
    public static CoverageProgrammingRequestType addSARParams(CoverageProgrammingRequestType coverage)
    {
        ValidationParametersType valParams = coverage.addNewValidationParameters();
        ValidationParametersSARType sarVP = valParams.addNewValidationParametersSAR();
        QuantityType mNL = sarVP.addNewMaxNoiseLevel();
        mNL.addNewUom().setCode("W");
        mNL.setValue(30);
        QuantityType mAL = sarVP.addNewMaxAmbiguityLevel();
        mAL.addNewUom().setCode("W");
        mAL.setValue(30);
        
        
        AcquisitionParametersType params = coverage.getAcquisitionType().getMonoscopicAcquisitionArray(0).addNewAcquisitionParameters();
        
        AcquisitionParametersSARType sar = params.addNewAcquisitionParametersSAR();

        sar.addNewInstrumentMode().setValue("SM");
        sar.addNewPolarizationMode().setValue("HH");

        sar.addNewFusionAccepted().setValue(true);
        
        List resolutions = new ArrayList<Double>();
        resolutions.add((double)10.0);
        resolutions.add((double)100.0);
        QuantityRangeType res = sar.addNewGroundResolution();
        res.setValue(resolutions);
        res.addNewUom().setCode("m");
        
        return coverage;
    }
    
    
    public static SwathProgrammingRequestType createSwathRequest(SwathProgrammingRequestType swath)
    {
        SwathSegmentType segment = swath.addNewSwathSegment();
        
        
        
        
        
        
        
        
        
        
        
        return swath;
    }
}
