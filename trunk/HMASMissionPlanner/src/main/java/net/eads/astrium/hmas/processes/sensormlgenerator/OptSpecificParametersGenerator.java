/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OptSpecificParametersGenerator.java
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.OPTSensor;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.sensorML.x102.CharacteristicsDocument.Characteristics;
import net.opengis.sensorML.x102.ComponentType;
import net.opengis.sensorML.x102.IdentificationDocument;
import net.opengis.sensorML.x102.SensorMLDocument.SensorML;
import net.opengis.sensorML.x102.TermDocument;
import net.opengis.sensorML.x102.ValidTimeDocument;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.CountType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;

/**
 *
 * @author re-sulrich
 */
public class OptSpecificParametersGenerator {
    
    public static Characteristics createOPTGeometricCharacteristics(OPTSensor optSensor) {
        
        double acrossTrackFOV = 4.13;
        double minAcrossTrackAngle = 5.0;
        double maxAcrossTrackAngle = 5.0;
        double minAlongTrackAngle = 5.0;
        double maxAlongTrackAngle = 5.0;
        double swathWidth = 60.0;
        double groundLocationAccuracy  = 50.0;
        double revisitTimeInDays = 3.0;
        
        try {
        acrossTrackFOV = Double.valueOf(optSensor.getAcrossTrackFOV());
        } catch (NumberFormatException e) {
            
        }
        try {
        minAcrossTrackAngle = Double.valueOf(optSensor.getMinAcrossTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxAcrossTrackAngle = Double.valueOf(optSensor.getMaxAcrossTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        minAlongTrackAngle = Double.valueOf(optSensor.getMinAlongTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxAlongTrackAngle = Double.valueOf(optSensor.getMaxAlongTrackAngle());
        } catch (NumberFormatException e) {
            
        }
        try {
        swathWidth = Double.valueOf(optSensor.getSwathWidth());
        } catch (NumberFormatException e) {
            
        }
        try {
        groundLocationAccuracy = Double.valueOf(optSensor.getGroundLocationAccuracy());
        } catch (NumberFormatException e) {
            
        }
        try {
        revisitTimeInDays = Double.valueOf(optSensor.getRevisitTimeInDays());
        } catch (NumberFormatException e) {
            
        }
        
        
        
        
        
        
        
        Characteristics geometricCharacteristics = Characteristics.Factory.newInstance();
        geometricCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:GeometricCharacteristics");
        DataRecordType gcdr = geometricCharacteristics.addNewDataRecord();
        gcdr.setLabel("Geometric Characteristics");
        
        DataRecordType.Field fov = gcdr.addNewField();
        fov.setName("Across-Track FOV");
        QuantityType t = QuantityType.Factory.newInstance();
        t.addNewUom().setCode("deg");
        t.setDefinition("urn:ogc:def:property:CEOS:eop:AcrossTrackFOV");
        t.setValue(acrossTrackFOV);
        fov.setAbstractDataComponent(t);
        
        DataRecordType.Field across = gcdr.addNewField();
        across.setName("Across-Track Pointing Range");
        QuantityRangeType t1 = QuantityRangeType.Factory.newInstance();
        t1.addNewUom().setCode("deg");
        t1.setDefinition("urn:ogc:def:property:CEOS:eop:AcrossTrackPointingRange");
        
        List l1 = new ArrayList<Double>();
        l1.add(minAcrossTrackAngle);
        l1.add(maxAcrossTrackAngle);
        t1.setValue(l1);
//        t1.setValue(Arrays.asList(new double[]{minAcrossTrackAngle, maxAcrossTrackAngle}));
        
        across.setAbstractDataComponent(t1);
        
        DataRecordType.Field along = gcdr.addNewField();
        along.setName("Along-Track Pointing Range");
        QuantityRangeType t2 = QuantityRangeType.Factory.newInstance();
        t2.addNewUom().setCode("deg");
        t2.setDefinition("urn:ogc:def:property:CEOS:eop:AlongTrackPointingRange");
        
        List l2 = new ArrayList<Double>();
        l2.add(minAlongTrackAngle);
        l2.add(maxAlongTrackAngle);
        t2.setValue(l2);
//        t2.setValue(Arrays.asList(new double[]{minAlongTrackAngle, maxAlongTrackAngle}));
        along.setAbstractDataComponent(t2);
        
        DataRecordType.Field sw =  gcdr.addNewField();
        sw.setName("Swath Width");
        QuantityType t3 = QuantityType.Factory.newInstance();
        t3.addNewUom().setCode("km");
        t3.setDefinition("urn:ogc:def:property:CEOS:eop:NadirSwathWidth");
        t3.setValue(swathWidth);
        sw.setAbstractDataComponent(t3);
        
        DataRecordType.Field gla =  gcdr.addNewField();
        gla.setName("Ground Location Accuracy");
        QuantityType t4 = QuantityType.Factory.newInstance();
        t4.addNewUom().setCode("m");
        t4.setDefinition("urn:ogc:def:property:CEOS:eop:GroundLocationAccuracy");
        t4.setValue(groundLocationAccuracy);
        gla.setAbstractDataComponent(t4);
        
        DataRecordType.Field revTime =  gcdr.addNewField();
        revTime.setName("Revisit Time");
        QuantityType t5 = QuantityType.Factory.newInstance();
        t5.addNewUom().setCode("d");
        t5.setDefinition("urn:ogc:def:property:CEOS:eop:RevisitTime");
        t5.setValue(revisitTimeInDays);
        revTime.setAbstractDataComponent(t5);
        
        return geometricCharacteristics;
    }
    
    public static Characteristics createOPTMeasurementCharacteristics(OPTSensor optSensor) {
        
        int numberOfBands = 5;
        
        
        try {
        numberOfBands = Integer.valueOf(optSensor.getNumberOfBands());
        } catch (NumberFormatException e) {
            
        }
        
        
        
        Characteristics measurementCharacteristics = Characteristics.Factory.newInstance();
        measurementCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:MeasurementCharacteristics");
        DataRecordType mcdr = measurementCharacteristics.addNewDataRecord();
        mcdr.setLabel("Measurement Characteristics");
        
        DataRecordType.Field nos =  mcdr.addNewField();
        nos.setName("Number of Samples");
        CountType t = CountType.Factory.newInstance();
        t.setDefinition("urn:ogc:def:property:CEOS:eop:NumberOfBands");
        t.setValue(BigInteger.valueOf(numberOfBands));
        nos.setAbstractDataComponent(t);
        
        return measurementCharacteristics;
    }
    
    public static SensorML createOPTDetector(String instrumentModeId, OPTSensor optSensor) {
        
//        String instrumentModeId = "";
        String instrumentId  = "";
        String modeShortName = "";
        String description = "";
        //Valid Time
        Date begin = new Date();
        Date end = new Date();
        //Physical characteristics
        double maxPowerConsumption = 100.0;
        //Geometrical characteristics
        double acrossTrackResolution = 5.0;
        double alongTrackResolution = 5.0;
        int numberOfSamples = 12000;
        //Measurement characteristics
        String bandType = "";
        double minSpectralRange_nm = 490.0;
        double maxSpectralRange_nm = 690.0;
        double snrRatio_dB = 170;
        double noiseEquivalentRadiance = 0.1;
        
        //Get info from database
        OPTSensor.OPTSensorMode sensorMode = optSensor.getInstrumentModesDescriptions().get(instrumentModeId);
        
        modeShortName = sensorMode.getId();
        description = sensorMode.getDescription();
        instrumentId  = optSensor.getSensorId();
        
        try {
        maxPowerConsumption = Double.valueOf(sensorMode.getMaxPowerConsumption());
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
        numberOfSamples = Integer.valueOf(sensorMode.getNumberOfSamples());
        } catch (NumberFormatException e) {
            
        }
        try {
        minSpectralRange_nm = Double.valueOf(sensorMode.getMinSpectralRange());
        } catch (NumberFormatException e) {
            
        }
        try {
        maxSpectralRange_nm = Double.valueOf(sensorMode.getMaxSpectralRange());
        } catch (NumberFormatException e) {
            
        }
        try {
        snrRatio_dB = Double.valueOf(sensorMode.getSnrRatio());
        } catch (NumberFormatException e) {
            
        }
        try {
        noiseEquivalentRadiance = Double.valueOf(sensorMode.getNoiseEquivalentRadiance());
        } catch (NumberFormatException e) {
            
        }
        
        
        
        
        
        
        
        
        
        
        SensorML sensorML = SensorML.Factory.newInstance();
        SensorML.Member member = sensorML.addNewMember();

        member.setRole("urn:ogc:def:dictionary:CEOS:documentRoles:v01#instrument_configuration");

        ComponentType descriptor = ComponentType.Factory.newInstance();
        descriptor.setId(instrumentId);
        descriptor.addNewDescription().setStringValue(description);
        /**
         * Identification
         */
        IdentificationDocument.Identification.IdentifierList identification = descriptor.addNewIdentification().addNewIdentifierList();
        
        IdentificationDocument.Identification.IdentifierList.Identifier modeIdentifier = identification.addNewIdentifier();
        modeIdentifier.setName("Configuration UID");
        TermDocument.Term term = modeIdentifier.addNewTerm();
        term.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
        term.setValue(instrumentModeId);
        
        IdentificationDocument.Identification.IdentifierList.Identifier instIdentifier = identification.addNewIdentifier();
        instIdentifier.setName("Instrument UID");
        TermDocument.Term term1 = instIdentifier.addNewTerm();
        term1.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentID");
        term1.setValue(instrumentId);
        
        IdentificationDocument.Identification.IdentifierList.Identifier sNameIdentifier = identification.addNewIdentifier();
        sNameIdentifier.setName("Short Name");
        TermDocument.Term term2 = sNameIdentifier.addNewTerm();
        term2.setDefinition("urn:ogc:def:property:OGC:shortName");
        term2.setValue(modeShortName);
        
        /**
         * Valid time
         */
        ValidTimeDocument.ValidTime validTime = descriptor.addNewValidTime();
        
        TimePeriodType vt = validTime.addNewTimePeriod();

        vt.addNewBeginPosition().setStringValue(DateHandler.formatDate(begin));
        vt.addNewEndPosition().setStringValue(DateHandler.formatDate(end));
//        vt.addNewEndPosition().setIndeterminatePosition(TimeIndeterminateValueType.Enum.forString("now"));
        
        /**
         * Physical Characteristics
         */
        Characteristics physicalCharacteristics = descriptor.addNewCharacteristics();
        physicalCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:PhysicalCharacteristics");
        DataRecordType phcdr = physicalCharacteristics.addNewDataRecord();
        phcdr.setLabel("Physical Characteristics");

        DataRecordType.Field pc = phcdr.addNewField();
        pc.setName("Maximum Power Consumption");
        QuantityType t = QuantityType.Factory.newInstance();
        t.addNewUom().setCode("W");
        t.setDefinition("urn:ogc:def:property:CEOS:eop:MaximumPowerConsumption");
        t.setValue(maxPowerConsumption);
        pc.setAbstractDataComponent(t);
        
        /**
         * Geometric Characteristics
         */
        Characteristics geometricCharacteristics = descriptor.addNewCharacteristics();
        geometricCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:GeometricCharacteristics");
        DataRecordType gcdr = geometricCharacteristics.addNewDataRecord();
        gcdr.setLabel("Geometric Characteristics");
        
        DataRecordType.Field across = gcdr.addNewField();
        across.setName("Across-Track Ground Resolution");
        QuantityType t1 = QuantityType.Factory.newInstance();
        t1.addNewUom().setCode("m");
        t1.setDefinition("urn:ogc:def:property:CEOS:eop:AcrossTrackGroundResolution");
        t1.setValue(acrossTrackResolution);
        across.setAbstractDataComponent(t1);
        
        DataRecordType.Field along = gcdr.addNewField();
        along.setName("Along-Track Ground Resolution");
        QuantityType t2 = QuantityType.Factory.newInstance();
        t2.addNewUom().setCode("m");
        t2.setDefinition("urn:ogc:def:property:CEOS:eop:AlongTrackGroundResolution");
        t2.setValue(alongTrackResolution);
        along.setAbstractDataComponent(t2);
        
        DataRecordType.Field nos =  gcdr.addNewField();
        nos.setName("Number of Samples");
        CountType t3 = CountType.Factory.newInstance();
        t3.setDefinition("urn:ogc:def:property:CEOS:eop:NumberOfSamples");
        t3.setValue(BigInteger.valueOf(numberOfSamples));
        nos.setAbstractDataComponent(t3);
        
        /**
         * Measurement Characteristics
         */
        Characteristics measurementCharacteristics = descriptor.addNewCharacteristics();
        measurementCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:MeasurementCharacteristics");
        DataRecordType mcdr = measurementCharacteristics.addNewDataRecord();
        mcdr.setLabel("Measurement Characteristics");
        
        DataRecordType.Field bt = mcdr.addNewField();
        bt.setName("Band Type");
        CategoryType t4 = CategoryType.Factory.newInstance();
        t4.setValue(bandType);
        t4.setDefinition("urn:ogc:def:property:CEOS:eop:BandType");
        bt.setAbstractDataComponent(t4);
        
        DataRecordType.Field sr = mcdr.addNewField();
        sr.setName("Spectral Range");
        QuantityRangeType t5 = QuantityRangeType.Factory.newInstance();
        
        List l5 = new ArrayList<Double>();
        l5.add(minSpectralRange_nm);
        l5.add(maxSpectralRange_nm);
        t5.setValue(l5);
//        t5.setValue(Arrays.asList(new double[]{minSpectralRange_nm, maxSpectralRange_nm}));
        
        t5.addNewUom().setCode("nm");
        t5.setDefinition("urn:ogc:def:property:CEOS:eop:SpectralRange");
        sr.setAbstractDataComponent(t5);
        
        DataRecordType.Field snr = mcdr.addNewField();
        snr.setName("SNR Ratio");
        QuantityType t6 = QuantityType.Factory.newInstance();
        t6.setValue(snrRatio_dB);
        t6.addNewUom().setCode("dB");
        t6.setDefinition("urn:ogc:def:property:CEOS:eop:SNR");
        snr.setAbstractDataComponent(t6);
        
        DataRecordType.Field ner = mcdr.addNewField();
        ner.setName("Noise Equivalent Radiance");
        QuantityType t7 = QuantityType.Factory.newInstance();
        t7.setValue(noiseEquivalentRadiance);
        t7.addNewUom().setCode("W.m-2.sr.um-1");
        t7.setDefinition("urn:ogc:def:property:CEOS:eop:NEDR");
        ner.setAbstractDataComponent(t7);
        
        
        
        //Set the created structure in the SensorML structure
        member.setProcess(descriptor);
        
        
        
        return sensorML;
    } 
}
