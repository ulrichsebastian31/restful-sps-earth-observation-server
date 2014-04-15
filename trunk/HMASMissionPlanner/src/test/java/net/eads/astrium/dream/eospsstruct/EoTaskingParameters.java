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
package net.eads.astrium.hmas.eospsstruct;

import java.util.ArrayList;
import java.util.List;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.RegionOfInterestType;
import net.opengis.eosps.x20.SurveyPeriodDocument;
import net.opengis.eosps.x20.SwathProgrammingRequestType;
import net.opengis.eosps.x20.SwathSegmentType;
import net.opengis.eosps.x20.TaskingParametersDocument;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TimeOfInterestType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
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
//        System.out.println(readEOTaskingParams().xmlText());
        System.out.println(createEOTaskingParameters().xmlText());
    }
        
//    public static TaskingParametersType readEOTaskingParams()
//    {
//        TaskHandler tasks = new TaskHandler("C:\\Users\\re-sulrich\\.dream\\mmfas\\gmes-mmfas\\tasks\\00000000001");
//        TaskingParametersType taskingParams = null;
//
//        try {
//            String taskParam = tasks.getEOTaskingParameters();
//            taskingParams = TaskingParametersDocument.Factory.parse(taskParam).getTaskingParameters();
//
//        } catch (IOException ex) {
//            Logger.getLogger(CancelOperation.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XmlException ex) {
//            Logger.getLogger(CancelOperation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return taskingParams;
//    }
    
    public static TaskingParametersDocument createEOTaskingParameters()
    {
        TaskingParametersDocument doc = TaskingParametersDocument.Factory.newInstance();
        TaskingParametersType taskingparams = doc.addNewTaskingParameters();
        CoverageProgrammingRequestType coverage = taskingparams.addNewCoverageProgrammingRequest();
        
        taskingparams.setCoverageProgrammingRequest(createCoverageRequest(coverage));
        
        SwathProgrammingRequestType swath = taskingparams.addNewSwathProgrammingRequest();
        
        taskingparams.setSwathProgrammingRequest(createSwathRequest(swath));
        
        return doc;
    }
    
    public static CoverageProgrammingRequestType createCoverageRequest(CoverageProgrammingRequestType coverage)
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
        
        
        
        
        RegionOfInterestType roi = coverage.addNewRegionOfInterest();
        PolygonType polygon = roi.addNewPolygon();
        AbstractRingPropertyType ext = polygon.addNewExterior();
        
        CoordinatesType coords = CoordinatesType.Factory.newInstance();
        coords.setDecimal(".");
        coords.setCs(",");
        coords.setTs(" ");
        coords.setStringValue(
                  "23.43,34.87 "
                + "23.76,34.87 "
                + "23.76,35.16 "
                + "23.43,35.16 "
                + "23.43,34.87");
        LinearRingType lineRing = LinearRingType.Factory.newInstance();
        lineRing.setCoordinates(coords);
        ext.setAbstractRing(lineRing);
        
        TimeOfInterestType toi = coverage.addNewTimeOfInterest();
        SurveyPeriodDocument.SurveyPeriod surveyPeriod = toi.addNewSurveyPeriod();
        TimePeriodType tp = surveyPeriod.addNewTimePeriod();
        TimePositionType begin = tp.addNewBeginPosition();
        begin.setStringValue("2013-06-30T00:00:00Z");
        TimePositionType end = tp.addNewEndPosition();
        end.setStringValue("2013-07-20T00:00:00Z");
        
        MonoscopicAcquisitionType mono = coverage.addNewAcquisitionType().addNewMonoscopicAcquisition();
        
        mono.addNewAcquisitionAngle();
        mono.addNewBHRatio();
        mono.addNewCoverageType();
        mono.addNewMaxCoupleDelay();
        
        AcquisitionParametersType params = mono.addNewAcquisitionParameters();
        AcquisitionParametersOPTType opt = params.addNewAcquisitionParametersOPT();
        opt.addNewFusionAccepted().setValue(true);
        opt.addNewInstrumentMode().setValue("");
        
        List resolutions = new ArrayList<Double>();
        resolutions.add((double)10.0);
        resolutions.add((double)100.0);
        QuantityRangeType res = opt.addNewGroundResolution();
        res.setValue(resolutions);
        res.addNewUom().setCode("m");
        QuantityType minLum = opt.addNewMinLuminosity();
        minLum.addNewUom().setCode("W");
        
        
        
        
        //        AcquisitionParametersSARType sar = params.addNewAcquisitionParametersSAR();
        //
        //        sar.addNewInstrumentMode().setValue("SM");
        //        sar.addNewPolarizationMode().setValue("HH");
        //
        //        sar.addNewFusionAccepted().setValue(true);
        
        return coverage;
    }
    
    public static SwathProgrammingRequestType createSwathRequest(SwathProgrammingRequestType swath)
    {
        SwathSegmentType segment = swath.addNewSwathSegment();
        
        
        
        
        
        
        
        
        
        
        
        return swath;
    }
}
