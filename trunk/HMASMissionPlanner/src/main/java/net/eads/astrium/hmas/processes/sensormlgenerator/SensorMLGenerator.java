/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorMLGenerator.java
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

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.dbhandler.SensorLoader;
import net.eads.astrium.hmas.util.Constants;
import net.eads.astrium.hmas.util.structures.OPTSensor;
import net.eads.astrium.hmas.util.structures.OPTSensor.OPTSensorMode;
import net.eads.astrium.hmas.util.structures.SARSensor;
import net.eads.astrium.hmas.util.structures.SARSensor.SARSensorMode;
import net.eads.astrium.hmas.util.structures.Sensor;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.sensorML.x102.CharacteristicsDocument.Characteristics;
import net.opengis.sensorML.x102.ClassificationDocument;
import net.opengis.sensorML.x102.ClassificationDocument.Classification;
import net.opengis.sensorML.x102.ComponentsDocument.Components;
import net.opengis.sensorML.x102.ContactDocument.Contact;
import net.opengis.sensorML.x102.ContactInfoDocument;
import net.opengis.sensorML.x102.IdentificationDocument;
import net.opengis.sensorML.x102.IdentificationDocument.Identification;
import net.opengis.sensorML.x102.ResponsiblePartyDocument;
import net.opengis.sensorML.x102.SensorMLDocument.SensorML;
import net.opengis.sensorML.x102.SystemType;
import net.opengis.sensorML.x102.TermDocument;
import net.opengis.sensorML.x102.ValidTimeDocument.ValidTime;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.QuantityType;

/**
 * This class permits to creates the SensorML description file for a given
 * sensor using the database
 *
 * @author re-sulrich
 */
public class SensorMLGenerator {

    private SensorLoader loader;
    private String instrumentId;
    private String instrumentType;
    private Sensor sensor;
    private OPTSensor optSensor;
    private SARSensor sarSensor;

    public SensorMLGenerator(SensorLoader loader, String instrumentId) throws SQLException {

        this.loader = loader;
        this.instrumentId = instrumentId;

        this.instrumentType = loader.getSensorType(instrumentId);

        if (instrumentType.equalsIgnoreCase("SAR")) {
            sarSensor = this.loader.loadSARSensor(instrumentId);
            sensor = (Sensor) sarSensor;
        }
        if (instrumentType.equalsIgnoreCase("OPT")) {
            optSensor = this.loader.loadOPTSensor(instrumentId);
            sensor = (Sensor) optSensor;
        }
        if (sensor == null) {
            throw new SQLException("Sensor " + instrumentId + " not found.");
        }
    }

    public SensorML createInstrumentModeSensorML102Description(String instrumentModeId) {

        SensorML sensorML = null;

        if (instrumentType.equalsIgnoreCase("SAR")) {
            sensorML = SarSpecificParametersGenerator.createInstrumentConfiguration(instrumentModeId, sarSensor);
        }
        if (instrumentType.equalsIgnoreCase("OPT")) {
            sensorML = OptSpecificParametersGenerator.createOPTDetector(instrumentModeId, optSensor);
        }

        return sensorML;
    }

    /**
     * This function create the SensorML description of the given sensor with
     * the data contained in the database following the following specifications
     * (GMES PH):
     * http://external.opengis.org/twiki_public/NREwg/GMESPHInstrumentCapabilities
     * http://external.opengis.org/twiki_public/NREwg/GMESPHInstrumentConfiguration
     *
     * and the two add-ons for optical and SAR satellites :
     * http://external.opengis.org/twiki_public/NREwg/GMESPHSARInstrument
     * http://external.opengis.org/twiki_public/NREwg/GMESPHOpticalInstrument
     *
     * @param sensorId
     * @return
     */
    public SensorML createSensorML102Description() {

        //Create base structure that will contain the description
        SensorML sensorML = SensorML.Factory.newInstance();
        SensorML.Member member = sensorML.addNewMember();

        member.setRole("urn:ogc:def:dictionary:CEOS:documentRoles:v01#instrument_capabilities");
        
        SystemType system = createSystem();
        
        member.setProcess(system);

        return sensorML;
    }

    /**
     * Creates the overall System structure that describes a sensor
     *
     * @return
     */
    private SystemType createSystem() {
        //TODO: Set values from BDD
//        String description = "";


        SystemType system = SystemType.Factory.newInstance();
        system.setId(instrumentId);

        //Sensor brief description
        system.addNewDescription().setStringValue(sensor.getSensorDescription());

        //identification section
        system.setIdentificationArray(new Identification[]{createIdentificationSection()});

        //Classification section
        system.setClassificationArray(new Classification[]{createClassificationSection()});

        //Valid time section
        system.setValidTime(createValidTimeSection());

        //TODO: fill in Contact Section
        system.setContactArray(new Contact[]{createNewContactSection()});

        //Array of Characteristics containing physical, geometrical and measurement, plus instrument modes links if OPT
        List<Characteristics> characteristics = new ArrayList<>();

        characteristics.add(createNewPhysicalCharacteristics());
        characteristics.add(createGeometricCharacteristics());
        characteristics.add(createMeasurementCharacteristics());

        characteristics.add(createInstrumentModes());

        //Set the array of Characteristics to the model
        system.setCharacteristicsArray(characteristics.toArray(new Characteristics[characteristics.size()]));

        //Set components containing the instrument modes if SAR
        system.setComponents(createComponents());

        return system;
    }

    /**
     * Identification : - Instrument UID - Platform UID - Short name - Long name
     */
    private Identification createIdentificationSection() {

        //Identification
//        String instrumentId = "";
        String platformId = sensor.getSatellite();
        String instrumentShortName = sensor.getSensorName();
        String instrumentLongName = sensor.getSensorName();

        Identification identification = Identification.Factory.newInstance();

        Identification.IdentifierList identifierList =
                identification.addNewIdentifierList();

        IdentificationDocument.Identification.IdentifierList.Identifier instId = identifierList.addNewIdentifier();
        instId.setName("Instrument UID");
        TermDocument.Term term = instId.addNewTerm();
        term.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentID");
        term.setValue(instrumentId);

        IdentificationDocument.Identification.IdentifierList.Identifier platId = identifierList.addNewIdentifier();
        platId.setName("Platform UID");
        TermDocument.Term term1 = platId.addNewTerm();
        term1.setDefinition("urn:ogc:def:property:CEOS:eop:PlatformID");
        term1.setValue(platformId);

        IdentificationDocument.Identification.IdentifierList.Identifier sName = identifierList.addNewIdentifier();
        sName.setName("Short name");
        TermDocument.Term term2 = sName.addNewTerm();
        term2.setDefinition("uurn:ogc:def:property:OGC:shortName");
        term2.setValue(instrumentShortName);

        IdentificationDocument.Identification.IdentifierList.Identifier lName = identifierList.addNewIdentifier();
        lName.setName("Long name");
        TermDocument.Term term3 = lName.addNewTerm();
        term3.setDefinition("urn:ogc:def:property:OGC:longName");
        term3.setValue(instrumentLongName);

        return identification;
    }

    /**
     * Classification : - Instrument Type - Acquisition Method - Application
     */
    private Classification createClassificationSection() {

        //Classification
//        String instrumentType = "";
        String acquisitionMethod = sensor.getAcquisitionMethod();
        List<String> applications = sensor.getApplications();

        Classification classification = Classification.Factory.newInstance();

        Classification.ClassifierList classList =
                classification.addNewClassifierList();

        Classification.ClassifierList.Classifier iType =
                classList.addNewClassifier();
        iType.setName("Instrument Type");
        TermDocument.Term term4 = iType.addNewTerm();
        term4.setDefinition("urn:ogc:def:property:OGC:sensorType");
        term4.addNewCodeSpace().setHref("urn:ogc:def:dictionary:CEOS:eop:InstrumentTypes:v01");
        term4.setValue(instrumentType);


        ClassificationDocument.Classification.ClassifierList.Classifier acqMethod =
                classList.addNewClassifier();
        acqMethod.setName("Acquisition Method");
        TermDocument.Term term5 = acqMethod.addNewTerm();
        term5.setDefinition("urn:ogc:def:property:OGC:sensorType");
        term5.addNewCodeSpace().setHref("urn:ogc:def:dictionary:CEOS:eop:AcquisitionMethods:v01");
        term5.setValue(acquisitionMethod);

        for (String application : applications) {

            ClassificationDocument.Classification.ClassifierList.Classifier app =
                    classList.addNewClassifier();
            app.setName("Application");
            TermDocument.Term term6 = app.addNewTerm();
            term6.setDefinition("urn:ogc:def:property:OGC:application");
            term6.addNewCodeSpace().setHref("urn:ogc:def:dictionary:CEOS:eop:InstrumentApplications:v01");
            term6.setValue(application);
        }


        return classification;
    }

    /**
     * Valid time : - begin - end
     */
    private ValidTime createValidTimeSection() {
        //Valid Time
        Date begin = new Date();
        Date end = new Date();

        ValidTime validTime = ValidTime.Factory.newInstance();

        TimePeriodType vt = validTime.addNewTimePeriod();

        vt.addNewBeginPosition().setStringValue(DateHandler.formatDate(begin));
        vt.addNewEndPosition().setStringValue(DateHandler.formatDate(end));
//        vt.addNewEndPosition().setIndeterminatePosition(TimeIndeterminateValueType.Enum.forString("now"));

        return validTime;
    }

    /**
     * Physical characteristics - mass - power consumption
     */
    private Characteristics createNewPhysicalCharacteristics() {

        //Physical Characteristics
        double instrumentMass = 0.0;
        double maxPowerConsumption = 100.0;

        try {
            instrumentMass = Double.valueOf(sensor.getInstrumentMass());
        } catch (NumberFormatException e) {
        }
        try {

            maxPowerConsumption = Double.valueOf(sensor.getMaxPowerConsumption());
        } catch (NumberFormatException e) {
        }


        Characteristics physicalCharacteristics = Characteristics.Factory.newInstance();

        physicalCharacteristics.setRole("urn:ogc:def:role:CEOS:eop:PhysicalCharacteristics");
        DataRecordType phcdr = physicalCharacteristics.addNewDataRecord();
        phcdr.setLabel("Physical Characteristics");

        DataRecordType.Field m = phcdr.addNewField();
        m.setName("Mass");
        QuantityType t1 = QuantityType.Factory.newInstance();
        t1.addNewUom().setCode("kg");
        t1.setDefinition("urn:ogc:def:property:OGC:mass");
        t1.setValue(instrumentMass);
        m.setAbstractDataComponent(t1);

        DataRecordType.Field pc = phcdr.addNewField();
        pc.setName("Maximum Power Consumption");
        QuantityType t2 = QuantityType.Factory.newInstance();
        t2.addNewUom().setCode("W");
        t2.setDefinition("urn:ogc:def:property:CEOS:eop:MaximumPowerConsumption");
        t2.setValue(maxPowerConsumption);
        pc.setAbstractDataComponent(t2);

        return physicalCharacteristics;
    }

    /**
     * Contact section
     *
     * @return C
     */
    private Contact createNewContactSection() {

        Contact contact = Contact.Factory.newInstance();
        ResponsiblePartyDocument.ResponsibleParty responsible = contact.addNewResponsibleParty();
        responsible.setIndividualName("");
        responsible.setOrganizationName("");
        ContactInfoDocument.ContactInfo contactInfo = responsible.addNewContactInfo();
        contactInfo.addNewPhone().addNewVoice().setStringValue("");
        ContactInfoDocument.ContactInfo.Address address = contactInfo.addNewAddress();
        address.addDeliveryPoint("");
        address.setPostalCode("");
        address.setCity("");
        address.setCountry("");

        return contact;
    }

    private Characteristics createGeometricCharacteristics() {

        Characteristics c = null;

        if (instrumentType.equalsIgnoreCase("opt")) {
            c = OptSpecificParametersGenerator.createOPTGeometricCharacteristics(optSensor);
        }
        if (instrumentType.equalsIgnoreCase("sar")) {
            c = SarSpecificParametersGenerator.createSARGeometricCharacteristics(sarSensor);
        }
        return c;
    }

    private Characteristics createMeasurementCharacteristics() {

        Characteristics c = null;

        if (instrumentType.equalsIgnoreCase("opt")) {
            c = OptSpecificParametersGenerator.createOPTMeasurementCharacteristics(optSensor);
        }
        if (instrumentType.equalsIgnoreCase("sar")) {
            c = SarSpecificParametersGenerator.createSARMeasurementCharacteristics(sarSensor);
        }
        return c;
    }

    private Characteristics createInstrumentModes() {


        Map<String, SARSensorMode> modes = null;


        String url = "127.0.0.1:8080/"+Constants.WAR_FILE_PATH+"/";
        

        String sensorModeDescriptionURL = "" + url + "reset/1.0.0/procedures/" + sensor.getSensorId();

        Characteristics c = Characteristics.Factory.newInstance();

        if (instrumentType.equalsIgnoreCase("sar")) {

            modes = sarSensor.getInstrumentModesDescriptions();

            c.setRole("urn:ogc:def:role:CEOS:eop:InstrumentConfigurations");
            DataRecordType dr = c.addNewDataRecord();
            dr.setLabel("Possible Instrument Configurations");

            for (SARSensorMode mode : modes.values()) {

                DataRecordType.Field f = dr.addNewField();
                f.setName(mode.getName());
                CategoryType type = CategoryType.Factory.newInstance();
                type.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
                type.setValue(sensorModeDescriptionURL + File.separator + mode.getId());
            }
        }

        return c;
    }

    private Components createComponents() {

        Map<String, OPTSensorMode> modes = null;

        String url = "127.0.0.1:8080/"+Constants.WAR_FILE_PATH+"/";
        

        String sensorModeDescriptionURL = "" + url + "reset/1.0.0/procedures/" + sensor.getSensorId();

        Components components = Components.Factory.newInstance();
        Components.ComponentList list = components.addNewComponentList();

        if (instrumentType.equalsIgnoreCase("opt")) {
            modes = optSensor.getInstrumentModesDescriptions();

            for (OPTSensorMode mode : modes.values()) {
                Components.ComponentList.Component c = list.addNewComponent();
                c.setName(mode.getName());
                c.setHref(sensorModeDescriptionURL + File.separator + mode.getId());
            }
        }

        return components;
    }
}
