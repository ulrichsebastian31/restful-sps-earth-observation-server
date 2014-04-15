/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.processes.taskingparametersgenerator;

import java.util.ArrayList;
import java.util.List;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
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
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TimeOfInterestType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;

/**
 *
 * @author re-sulrich
 */
public class TaskingParametersGenerator {
    
    public static TaskingParametersType createOPTTaskingParameters(OPTTaskingParameters input) {
        
        TaskingParametersType eotp = TaskingParametersType.Factory.newInstance();
        
        CoverageProgrammingRequestType cov = eotp.addNewCoverageProgrammingRequest();

        RegionOfInterestType roi = cov.addNewRegionOfInterest();
        AbstractRingPropertyType ext = roi.addNewPolygon().addNewExterior();

        CoordinatesType coords = CoordinatesType.Factory.newInstance();
        coords.setDecimal(".");
        coords.setCs(",");
        coords.setTs(" ");
        coords.setStringValue(input.getCoordinates().printCoordinatesGML());
        LinearRingType lineRing = LinearRingType.Factory.newInstance();
        lineRing.setCoordinates(coords);
        ext.setAbstractRing(lineRing);

        TimeOfInterestType toi = cov.addNewTimeOfInterest();
        SurveyPeriodDocument.SurveyPeriod surveyPeriod = toi.addNewSurveyPeriod();
        TimePeriodType tp = surveyPeriod.addNewTimePeriod();
        TimePositionType beginPos = tp.addNewBeginPosition();
        beginPos.setStringValue(DateHandler.formatDate(input.getTimes().get(0).getBegin()));
        TimePositionType endPos = tp.addNewEndPosition();
        endPos.setStringValue(DateHandler.formatDate(input.getTimes().get(input.getTimes().size() - 1).getEnd()));




        MonoscopicAcquisitionType mono = cov.addNewAcquisitionType().addNewMonoscopicAcquisition();

        //TODO: coverage type input
//        mono.addNewCoverageType().setValue(input.getc);

        AcquisitionAngleType acqAngle = mono.addNewAcquisitionAngle();

        IncidenceRangeType incAng = acqAngle.addNewIncidenceRange();
        QuantityRangeType az = incAng.addNewAzimuthAngle();
        QuantityRangeType el = incAng.addNewElevationAngle();
        az.addNewUom().setCode("deg");
        el.addNewUom().setCode("deg");

        List l1 = new ArrayList<Double>();
        l1.add(input.getMinIncidenceAngleAzimuth());
        l1.add(input.getMaxIncidenceAngleAzimuth());
        az.setValue(l1);

        List l2 = new ArrayList<Double>();
        l2.add(input.getMinIncidenceAngleElevation());
        l2.add(input.getMaxIncidenceAngleElevation());
        el.setValue(l2);

        PointingRangeType poiAng = acqAngle.addNewPointingRange();
        QuantityRangeType ac = poiAng.addNewAcrossTrackAngle();
        QuantityRangeType al = poiAng.addNewAlongTrackAngle();

        List l3 = new ArrayList<Double>();
        l3.add(input.getMinPointingAngleAcross());
        l3.add(input.getMaxPointingAngleAcross());
        ac.setValue(l3);

        List l4 = new ArrayList<Double>();
        l4.add(input.getMinPointingAngleAlong());
        l4.add(input.getMaxPointingAngleAlong());
        al.setValue(l4);

//        mono.addNewMaxCoupleDelay();
//        mono.addNewBHRatio();

        AcquisitionParametersType params = mono.addNewAcquisitionParameters();

        //OPT acquisition parameters
        AcquisitionParametersOPTType optap = params.addNewAcquisitionParametersOPT();
        optap.addNewFusionAccepted().setValue(input.getFusionAccepted());

        if (input.getInstrumentModes()!= null && input.getInstrumentModes().size() == 1) {
            
            optap.addNewInstrumentMode().setValue(input.getInstrumentModes().get(0));
        }
        
        QuantityRangeType gr = optap.addNewGroundResolution();
        List l5 = new ArrayList<Double>();
        l5.add(input.getMinGroundResolution());
        l5.add(input.getMaxGroundResolution());

        gr.setValue(l5);

        QuantityType minLumi = optap.addNewMinLuminosity();
        minLumi.addNewUom().setCode("W");
        minLumi.setValue(input.getMinLuminosity());

        //OPT validation parameters

        ValidationParametersOPTType optvp = cov.addNewValidationParameters().addNewValidationParametersOPT();
        QuantityType cc = optvp.addNewMaxCloudCover();
        cc.addNewUom().setCode("%");
        cc.setValue(input.getMaxCloudCover());
        String msId = input.getMeteoServerId();
        if (msId != null && !msId.equals("")) {
            CategoryType str = CategoryType.Factory.newInstance();
            str.setDefinition("meteoServer");
            str.setValue(msId);

            cc.addNewExtension().set(str);
        }
        
        QuantityType sc = optvp.addNewMaxSnowCover();
        sc.addNewUom().setCode("%");
        sc.setValue(input.getMaxSnowCover());
        QuantityType msg = optvp.addNewMaxSunGlint();
        msg.addNewUom().setCode("%");
        msg.setValue(input.getMaxSunGlint());

        optvp.addNewHazeAccepted().setValue(input.getHazeAccepted());
        optvp.addNewSandWindAccepted().setValue(input.getSandWindAccepted());

        return eotp;
    }
    
    public static TaskingParametersType createSARTaskingParameters(SARTaskingParameters input) {
        
        TaskingParametersType eotp = TaskingParametersType.Factory.newInstance();
        
        CoverageProgrammingRequestType cov = eotp.addNewCoverageProgrammingRequest();

        RegionOfInterestType roi = cov.addNewRegionOfInterest();
        AbstractRingPropertyType ext = roi.addNewPolygon().addNewExterior();

        CoordinatesType coords = CoordinatesType.Factory.newInstance();
        coords.setDecimal(".");
        coords.setCs(",");
        coords.setTs(" ");
        coords.setStringValue(input.getCoordinates().printCoordinatesGML());
        LinearRingType lineRing = LinearRingType.Factory.newInstance();
        lineRing.setCoordinates(coords);
        ext.setAbstractRing(lineRing);

        TimeOfInterestType toi = cov.addNewTimeOfInterest();
        SurveyPeriodDocument.SurveyPeriod surveyPeriod = toi.addNewSurveyPeriod();
        TimePeriodType tp = surveyPeriod.addNewTimePeriod();
        TimePositionType beginPos = tp.addNewBeginPosition();
        beginPos.setStringValue(DateHandler.formatDate(input.getTimes().get(0).getBegin()));
        TimePositionType endPos = tp.addNewEndPosition();
        endPos.setStringValue(DateHandler.formatDate(input.getTimes().get(input.getTimes().size() - 1).getEnd()));




        MonoscopicAcquisitionType mono = cov.addNewAcquisitionType().addNewMonoscopicAcquisition();

        //TODO: coverage type input
//        mono.addNewCoverageType().setValue(input.getc);

        AcquisitionAngleType acqAngle = mono.addNewAcquisitionAngle();

        IncidenceRangeType incAng = acqAngle.addNewIncidenceRange();
        QuantityRangeType az = incAng.addNewAzimuthAngle();
        QuantityRangeType el = incAng.addNewElevationAngle();
        az.addNewUom().setCode("deg");
        el.addNewUom().setCode("deg");

        List l1 = new ArrayList<Double>();
        l1.add(input.getMinIncidenceAngleAzimuth());
        l1.add(input.getMaxIncidenceAngleAzimuth());
        az.setValue(l1);

        List l2 = new ArrayList<Double>();
        l2.add(input.getMinIncidenceAngleElevation());
        l2.add(input.getMaxIncidenceAngleElevation());
        el.setValue(l2);

        PointingRangeType poiAng = acqAngle.addNewPointingRange();
        QuantityRangeType ac = poiAng.addNewAcrossTrackAngle();
        QuantityRangeType al = poiAng.addNewAlongTrackAngle();

        List l3 = new ArrayList<Double>();
        l3.add(input.getMinPointingAngleAcross());
        l3.add(input.getMaxPointingAngleAcross());
        ac.setValue(l3);

        List l4 = new ArrayList<Double>();
        l4.add(input.getMinPointingAngleAlong());
        l4.add(input.getMaxPointingAngleAlong());
        al.setValue(l4);

//        mono.addNewMaxCoupleDelay();
//        mono.addNewBHRatio();

        AcquisitionParametersType params = mono.addNewAcquisitionParameters();

        //SAR acquisition parameters
        AcquisitionParametersSARType sarap = params.addNewAcquisitionParametersSAR();

        
        if (input.getInstrumentModes()!= null && input.getInstrumentModes().size() == 1) {
            
            sarap.addNewInstrumentMode().setValue(input.getInstrumentModes().get(0));
        }

        sarap.addNewFusionAccepted().setValue(input.getFusionAccepted());

        QuantityRangeType gr = sarap.addNewGroundResolution();
        List l5 = new ArrayList<Double>();
        l5.add(input.getMinGroundResolution());
        l5.add(input.getMaxGroundResolution());

        gr.setValue(l5);

        if (input.getPolarisationModes()!= null && input.getPolarisationModes().size() == 1) {
            
            sarap.addNewPolarizationMode().setValue(input.getPolarisationModes().get(0));
        }

        //SAR validation parameters
        ValidationParametersSARType sarvp = cov.addNewValidationParameters().addNewValidationParametersSAR();
        QuantityType n = sarvp.addNewMaxNoiseLevel();
        n.addNewUom().setCode("dB");
        n.setValue(input.getMaxNoiseLevel());
        QuantityType a = sarvp.addNewMaxAmbiguityLevel();
        a.addNewUom().setCode("dB");
        a.setValue(input.getMaxAmbiguityLevel());

        return eotp;
    }
    
}
