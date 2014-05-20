/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SarSpecificParametersGenerator.java
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
package net.eads.astrium.hmas.processes.sensormlgenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.SARSensor;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.sensorML.x102.CharacteristicsDocument;
import net.opengis.sensorML.x102.IdentificationDocument;
import net.opengis.sensorML.x102.SensorMLDocument.SensorML;
import net.opengis.sensorML.x102.SystemType;
import net.opengis.sensorML.x102.TermDocument;
import net.opengis.sensorML.x102.ValidTimeDocument;
import net.opengis.swe.x20.CategoryDocument;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.QuantityDocument;
import net.opengis.swe.x20.QuantityRangeDocument;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;

/**
 *
 * @author re-sulrich
 */
public class SarSpecificParametersGenerator {

    public static CharacteristicsDocument.Characteristics createSARGeometricCharacteristics(SARSensor sarSensor) {

        double antennaLength = 10.0;
        double antennaWidth = 1.3;
        double groundLocationAccuracy = 10.0;
        double revisitTimeInDays = 3.0;

        try {
        antennaLength = Double.valueOf(sarSensor.getAntennaLength());
        } catch (NumberFormatException e) {
            
        }
        try {
        antennaWidth = Double.valueOf(sarSensor.getAntennaWidth());
        } catch (NumberFormatException e) {
            
        }
        try {
        groundLocationAccuracy = Double.valueOf(sarSensor.getGroundLocationAccuracy());
        } catch (NumberFormatException e) {
            
        }
        try {
        revisitTimeInDays = Double.valueOf(sarSensor.getRevisitTimeInDays());
        } catch (NumberFormatException e) {
            
        }
        
        CharacteristicsDocument.Characteristics geometricCharacteristics = CharacteristicsDocument.Characteristics.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        geometricCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:GeometricCharacteristics");
        DataRecordType gcdr = geometricCharacteristics.addNewDataRecord();
        gcdr.setLabel("Geometric_Characteristics");

        DataRecordType.Field fov = gcdr.addNewField();
        QuantityDocument d = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t = d.addNewQuantity();
        t.addNewUom().setCode("m");
        t.setDefinition("urn:ogc:def:property:CEOS:sar:AntennaLength");
        t.setValue(antennaLength);
        fov.set(d);
        fov.setName("Antenna_Length");

        DataRecordType.Field sw = gcdr.addNewField();
        QuantityDocument d1 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t1 = d1.addNewQuantity();
        t1.addNewUom().setCode("m");
        t1.setDefinition("urn:ogc:def:property:CEOS:sar:AntennaWidth");
        t1.setValue(antennaWidth);
        sw.set(d1);
        sw.setName("Antenna_Width");

        DataRecordType.Field gla = gcdr.addNewField();
        QuantityDocument d2 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t2 = d2.addNewQuantity();
        t2.addNewUom().setCode("m");
        t2.setDefinition("urn:ogc:def:property:CEOS:eop:GroundLocationAccuracy");
        t2.setValue(groundLocationAccuracy);
        gla.set(d2);
        gla.setName("Ground_Location_Accuracy");

        DataRecordType.Field revTime = gcdr.addNewField();
        QuantityDocument d3 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t3 = d3.addNewQuantity();
        t3.addNewUom().setCode("d");
        t3.setDefinition("urn:ogc:def:property:CEOS:eop:RevisitTime");
        t3.setValue(revisitTimeInDays);
        revTime.set(d3);
        revTime.setName("Revisit_Time");

        return geometricCharacteristics;
    }

    public static CharacteristicsDocument.Characteristics createSARMeasurementCharacteristics(SARSensor sarSensor) {

        String transmitFrequencyBand = "";
        double transmitCenterFrequency = 5.331;

        try {
            transmitFrequencyBand = sarSensor.getTransmitFrequencyBand();
        } catch (NumberFormatException e) {
            
        }
        try {
            transmitCenterFrequency = Double.valueOf(sarSensor.getTransmitCenterFrequency());
        } catch (NumberFormatException e) {
            
        }
        
        CharacteristicsDocument.Characteristics measurementCharacteristics = CharacteristicsDocument.Characteristics.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        measurementCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:MeasurementCharacteristics");
        DataRecordType mcdr = measurementCharacteristics.addNewDataRecord();
        mcdr.setLabel("Measurement_Characteristics");
        
        DataRecordType.Field tcf = mcdr.addNewField();
        QuantityDocument d = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t = d.addNewQuantity();
        t.addNewUom().setCode("GHz");
        t.setDefinition("urn:ogc:def:property:CEOS:sar:TransmitCenterFrequency");
        t.setValue(transmitCenterFrequency);
        tcf.set(d);
        tcf.setName("Transmit_Center_Frequency");

        DataRecordType.Field tfb = mcdr.addNewField();
        CategoryDocument d1 = CategoryDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        CategoryType t1 = d1.addNewCategory();
        t1.setDefinition("urn:ogc:def:property:CEOS:sar:TransmitFrequencyBand");
        t1.setValue(transmitFrequencyBand);
        tfb.set(d1);
        tfb.setName("Transmit_Frequency_Band");

        return measurementCharacteristics;
    }

    public static SensorML createInstrumentConfiguration(String instrumentModeId, SARSensor sarSensor) {

//        String instrumentModeId = "";
        String modeShortName = "";
        String description = "";
        String instrumentId  = "";
        //Valid Time
        Date begin = new Date();
        Date end = new Date();
        //Physical characteristics
        double maxPowerConsumption = 100.0;
        //Geometric characteristics
        double minAcrossTrackAngle = 5.0; 
        double maxAcrossTrackAngle = 5.0;
        double minAlongTrackAngle = 5.0;
        double maxAlongTrackAngle = 5.0;
        double swathWidth = 60.0;
        double acrossTrackResolution = 5.0;
        double alongTrackResolution = 5.0;
        //Measurement characteristics
        double radiometricResolution = 5.0;
        double minRadioStab = 5.0;
        double maxRadioStab = 5.0;
        double minAlongTrackAmbRatio = 5.0;
        double maxAlongTrackAmbRatio = 5.0;
        double minAcrossTrackAmbRatio = 5.0;
        double maxAcrossTrackAmbRatio = 5.0;
        double minNoiseEquivalentSO = 5.0;
        double maxNoiseEquivalentSO = 5.0;
        
        Map<String, String> polarizationModes = new HashMap<String, String>();
        
        SARSensor.SARSensorMode sensorMode = sarSensor.getInstrumentModesDescriptions().get(instrumentModeId);
        
        modeShortName = sensorMode.getId();
        description = sensorMode.getDescription();
        instrumentId  = sarSensor.getSensorId();
        
        try {
        maxPowerConsumption = Double.valueOf(sensorMode.getMaxPowerConsumption());
        } catch (NumberFormatException e) {
            
        }
        
        try {
        minAcrossTrackAngle = Double.valueOf(sensorMode.getMinAcrossTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxAcrossTrackAngle = Double.valueOf(sensorMode.getMaxAcrossTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        minAlongTrackAngle = Double.valueOf(sensorMode.getMinAlongTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxAlongTrackAngle = Double.valueOf(sensorMode.getMaxAlongTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        swathWidth = Double.valueOf(sensorMode.getSwathWidth());
        } catch (NumberFormatException e) {
            
        }
        
        
        
        try {
        acrossTrackResolution = Double.valueOf(sensorMode.getAcrossTrackResolution());
        } catch (NumberFormatException e) {
            
        }
        try {
        alongTrackResolution = Double.valueOf(sensorMode.getAlongTrackResolution());
        } catch (NumberFormatException e) {
            
        }
        
        
        
        try {
        radiometricResolution = Double.valueOf(sensorMode.getRadiometricResolution());
        } catch (NumberFormatException e) {
            
        }
        try {
        minRadioStab = Double.valueOf(sensorMode.getMinRadioStab());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxRadioStab = Double.valueOf(sensorMode.getMaxRadioStab());
        } catch (NumberFormatException e) {
            
        }
        try {
        minAlongTrackAmbRatio = Double.valueOf(sensorMode.getMinAlongTrackAmbRatio());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxAlongTrackAmbRatio = Double.valueOf(sensorMode.getMaxAlongTrackAmbRatio());
        } catch (NumberFormatException e) {
            
        }
        try {
        minAcrossTrackAmbRatio = Double.valueOf(sensorMode.getMinAcrossTrackAmbRatio());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxAcrossTrackAmbRatio = Double.valueOf(sensorMode.getMaxAcrossTrackAmbRatio());
        } catch (NumberFormatException e) {
            
        }
        try {
        minNoiseEquivalentSO = Double.valueOf(sensorMode.getMinNoiseEquivalentSO());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxNoiseEquivalentSO = Double.valueOf(sensorMode.getMaxNoiseEquivalentSO());
        } catch (NumberFormatException e) {
            
        }
        
        polarizationModes = sensorMode.getPolarizationModes();
        
        
        SensorML sensorML = SensorML.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        SensorML.Member member = sensorML.addNewMember();

        member.setRole("urn:ogc:def:dictionary:CEOS:documentRoles:v01#instrument_configuration");

        SystemType system = SystemType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        system.setId(instrumentModeId);
        system.addNewDescription().setStringValue(description);

        /**
         * Identification
         */
        IdentificationDocument.Identification.IdentifierList identification = system.addNewIdentification().addNewIdentifierList();

        IdentificationDocument.Identification.IdentifierList.Identifier modeIdentifier = identification.addNewIdentifier();
        modeIdentifier.setName("Configuration_UID");
        TermDocument.Term term = modeIdentifier.addNewTerm();
        term.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
        term.setValue(instrumentModeId);

        IdentificationDocument.Identification.IdentifierList.Identifier instIdentifier = identification.addNewIdentifier();
        instIdentifier.setName("Instrument_UID");
        TermDocument.Term term1 = instIdentifier.addNewTerm();
        term1.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentID");
        term1.setValue(instrumentId);

        IdentificationDocument.Identification.IdentifierList.Identifier sNameIdentifier = identification.addNewIdentifier();
        sNameIdentifier.setName("Short_Name");
        TermDocument.Term term2 = sNameIdentifier.addNewTerm();
        term2.setDefinition("urn:ogc:def:property:OGC:shortName");
        term2.setValue(modeShortName);

        /**
         * Valid time
         */
        ValidTimeDocument.ValidTime validTime = system.addNewValidTime();

        TimePeriodType vt = validTime.addNewTimePeriod();
        vt.setId("urn:ogc:data:time:iso8601");
        
        vt.addNewBeginPosition().setStringValue(DateHandler.formatDate(begin));
        vt.addNewEndPosition().setStringValue(DateHandler.formatDate(end));
//        vt.addNewEndPosition().setIndeterminatePosition(TimeIndeterminateValueType.Enum.forString("now"));

        /**
         * Physical Characteristics
         */
        CharacteristicsDocument.Characteristics physicalCharacteristics = system.addNewCharacteristics();
        physicalCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:PhysicalCharacteristics");
        DataRecordType phcdr = physicalCharacteristics.addNewDataRecord();
        phcdr.setLabel("Physical_Characteristics");

        DataRecordType.Field pc = phcdr.addNewField();
        QuantityDocument d = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t = d.addNewQuantity();
        t.addNewUom().setCode("W");
        t.setDefinition("urn:ogc:def:property:CEOS:eop:MaximumPowerConsumption");
        t.setValue(maxPowerConsumption);
        pc.set(d);
        pc.setName("Maximum_Power_Consumption");

        /**
         * Geometric Characteristics
         */
        CharacteristicsDocument.Characteristics geometricCharacteristics = system.addNewCharacteristics();
        geometricCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:GeometricCharacteristics");
        DataRecordType gcdr = geometricCharacteristics.addNewDataRecord();
        gcdr.setLabel("Geometric_Characteristics");


        DataRecordType.Field across = gcdr.addNewField();
        QuantityRangeDocument d1 = QuantityRangeDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityRangeType t1 = d1.addNewQuantityRange();
        t1.addNewUom().setCode("deg");
        t1.setDefinition("urn:ogc:def:property:CEOS:eop:AcrossTrackPointingRange");
        
        List l1 = new ArrayList<Double>();
        l1.add(minAcrossTrackAngle);
        l1.add(maxAcrossTrackAngle);
        t1.setValue(l1);
//        t1.setValue(Arrays.asList(new double[]{minAcrossTrackAngle, maxAcrossTrackAngle}));
        
        across.set(d1);
        across.setName("Across-Track_Pointing_Range");
        
        DataRecordType.Field along = gcdr.addNewField();
        QuantityRangeDocument d2 = QuantityRangeDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityRangeType t2 = d2.addNewQuantityRange();
        t2.addNewUom().setCode("deg");
        t2.setDefinition("urn:ogc:def:property:CEOS:eop:AlongTrackPointingRange");
        
        List l2 = new ArrayList<Double>();
        l2.add(minAlongTrackAngle);
        l2.add(maxAlongTrackAngle);
        t2.setValue(l2);
//        t2.setValue(Arrays.asList(new double[]{minAlongTrackAngle, maxAlongTrackAngle}));
        along.set(d2);
        along.setName("Along-Track_Pointing_Range");

        DataRecordType.Field sw = gcdr.addNewField();
        QuantityDocument d3 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t3 = d3.addNewQuantity();
        t3.addNewUom().setCode("km");
        t3.setDefinition("urn:ogc:def:property:CEOS:eop:NadirSwathWidth");
        t3.setValue(swathWidth);
        sw.set(d3);
        sw.setName("Swath_Width");

        DataRecordType.Field acrossRes = gcdr.addNewField();
        QuantityDocument d4 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t4 = d4.addNewQuantity();
        t4.addNewUom().setCode("m");
        t4.setDefinition("urn:ogc:def:property:CEOS:eop:AcrossTrackGroundResolution");
        t4.setValue(acrossTrackResolution);
        acrossRes.set(d4);
        acrossRes.setName("Across-Track_Ground_Resolution");

        DataRecordType.Field alongRes = gcdr.addNewField();
        QuantityDocument d5 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t5 = d5.addNewQuantity();
        t5.addNewUom().setCode("m");
        t5.setDefinition("urn:ogc:def:property:CEOS:eop:AlongTrackGroundResolution");
        t5.setValue(alongTrackResolution);
        alongRes.set(d5);
        alongRes.setName("Along-Track_Ground_Resolution");


        /**
         * Measurement Characteristics
         */
        CharacteristicsDocument.Characteristics measurementCharacteristics = system.addNewCharacteristics();
        measurementCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:MeasurementCharacteristics");
        DataRecordType mcdr = measurementCharacteristics.addNewDataRecord();
        mcdr.setLabel("Measurement_Characteristics");

        DataRecordType.Field rr = mcdr.addNewField();
        QuantityDocument d6 = QuantityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityType t6 = d6.addNewQuantity();
        t6.addNewUom().setCode("m");
        t6.setValue(radiometricResolution);
        t6.setDefinition("urn:ogc:def:property:CEOS:eop:RadiometricResolution");
        rr.set(d6);
        rr.setName("Radiometric_Resolution");

        DataRecordType.Field rs = mcdr.addNewField();
        QuantityRangeDocument d7 = QuantityRangeDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityRangeType t7 = d7.addNewQuantityRange();
        t7.addNewUom().setCode("dB");
        
        List l7 = new ArrayList<Double>();
        l7.add(minRadioStab);
        l7.add(maxRadioStab);
        t7.setValue(l7);
        
//        t7.setValue(Arrays.asList(new double[]{minRadioStab, maxRadioStab}));
        t7.setDefinition("urn:ogc:def:property:CEOS:sar:RadiometricStability");
        rs.set(d7);
        rs.setName("Radiometric_Stability");

        DataRecordType.Field alongAR = mcdr.addNewField();
        QuantityRangeDocument d8 = QuantityRangeDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityRangeType t8 = d8.addNewQuantityRange();
        t8.addNewUom().setCode("dB");
        
        List l8 = new ArrayList<Double>();
        l8.add(minAlongTrackAmbRatio);
        l8.add(maxAlongTrackAmbRatio);
        t8.setValue(l8);
        
//        t8.setValue(Arrays.asList(new double[]{minAlongTrackAmbRatio, maxAlongTrackAmbRatio}));
        t8.setDefinition("urn:ogc:def:property:CEOS:eop:AlongTrackAmbiguityRatio");
        alongAR.set(d8);
        alongAR.setName("Along-Track_Ambiguity_Ratio");


        DataRecordType.Field acrossAR = mcdr.addNewField();
        QuantityRangeDocument d9 = QuantityRangeDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityRangeType t9 = d9.addNewQuantityRange();
        t9.addNewUom().setCode("dB");
        
        List l9 = new ArrayList<Double>();
        l9.add(minAcrossTrackAmbRatio);
        l9.add(maxAcrossTrackAmbRatio);
        t9.setValue(l9);
//        t9.setValue(Arrays.asList(new double[]{minAcrossTrackAmbRatio, maxAcrossTrackAmbRatio}));
        
        t9.setDefinition("urn:ogc:def:property:CEOS:eop:AcrossTrackAmbiguityRatio");
        acrossAR.set(d9);
        acrossAR.setName("Across-Track_Ambiguity_Ratio");

        DataRecordType.Field neso = mcdr.addNewField();
        QuantityRangeDocument d10 = QuantityRangeDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        QuantityRangeType t10 = d10.addNewQuantityRange();
        t10.addNewUom().setCode("dB");
        
        List l10 = new ArrayList<Double>();
        l10.add(minNoiseEquivalentSO);
        l10.add(maxNoiseEquivalentSO);
        t10.setValue(l10);
//        t10.setValue(Arrays.asList(new double[]{minNoiseEquivalentSO, maxNoiseEquivalentSO}));
        
        t10.setDefinition("urn:ogc:def:property:CEOS:eop:NoiseEquivalentS0");
        neso.set(d10);
        neso.setName("Noise_Equivalent_S0");

        //Polarization modes from 
        for (String polarizationMode : polarizationModes.keySet()) {
            DataRecordType.Field polMode = mcdr.addNewField();
            CategoryDocument d11 = CategoryDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            CategoryType t11 = d11.addNewCategory();
            t11.setDefinition("urn:ogc:def:property:CEOS:sar:PolarizationMode");
            t11.setValue(polarizationMode);
            t11.addNewCodeSpace().setHref("urn:ogc:def:dictionary:CEOS:sar:PolarizationTypes:v01");
            polMode.set(d11);
            polMode.setName("Polarization");
        }

        //Set the created structure in the SensorML structure
        member.setProcess(system);

        return sensorML;
    }
}
