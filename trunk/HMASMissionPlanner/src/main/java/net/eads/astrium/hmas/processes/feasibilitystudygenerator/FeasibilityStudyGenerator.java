/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.processes.feasibilitystudygenerator;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.dbhandler.tasking.SensorFeasibilityHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorTaskHandler;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.tasking.EarthObservationEquipment;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.SensorTask;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;
import net.opengis.eosps.x20.FeasibilityStudyDocument;
import net.opengis.eosps.x20.FeasibilityStudyType;
import net.opengis.eosps.x20.ProgrammingStatusDocument;
import net.opengis.eosps.x20.ProgrammingStatusType;
import net.opengis.eosps.x20.SegmentPropertyType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.SegmentValidationDataDocument;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingDocument;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.swe.x20.CategoryDocument;
import net.opengis.swe.x20.CategoryType;
import xint.esa.earth.eop.DownlinkInformationDocument;
import xint.esa.earth.eop.DownlinkInformationType;
import xint.esa.earth.eop.EarthObservationEquipmentType;
import xint.esa.earth.eop.InstrumentType;
import xint.esa.earth.eop.OrbitTypeValueType;
import xint.esa.earth.eop.PlatformType;
import xint.esa.earth.eop.SensorType;

/**
 *
 * @author re-sulrich
 */
public class FeasibilityStudyGenerator {
    
    /**
     * Creates the FeasibilityStudyDocument from the data of the SensorFeasibilityAnalysisHandler
     * @return 
     */
    public static FeasibilityStudyDocument getFeasibilityStudy(SensorFeasibilityHandler handler, String sensorTaskId) throws SQLException, ParseException {
        
        SensorTask task = handler.getSensorTask(sensorTaskId);
        
        FeasibilityStudyDocument studyDoc = FeasibilityStudyDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        FeasibilityStudyType study = studyDoc.addNewFeasibilityStudy();
        study.setExpirationDate(DateHandler.getCalendar(task.getExpirationTime()));
        
        System.out.println("StudyDoc creation : " + studyDoc.xmlText(OGCNamespacesXmlOptions.getInstance()));
        
        study.addInformationUsed("KINEMATIC MODEL");
        study.addInformationUsed("ESTIMATED WORKLOAD");
        
        //TODO: estimated cost by segment ? by task ? + add sensorTask status ?
        study.addNewEstimatedCost().setDoubleValue(task.getCost());
        study.getEstimatedCost().setUom("Euros");
        study.setId("FEASIBILITY_" + sensorTaskId);
        
        study.setSuccessRate(100);
        
        study.setSegmentArray(addSegments(TaskType.feasibility, handler, sensorTaskId));
        
        return studyDoc;
    }
    
    public static ProgrammingStatusDocument getProgrammingStatus(SensorPlanningHandler handler, String sensorTaskId) throws SQLException, ParseException {
        
        ProgrammingStatusDocument studyDoc = ProgrammingStatusDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ProgrammingStatusType study = studyDoc.addNewProgrammingStatus();
        
        //TODO: estimated cost by segment ? by task ? + add sensorTask status ?
//        study.addNewEstimatedCost().setDoubleValue(this.feasibilityHandler.getEstimatedCost());
//        study.getEstimatedCost().setUom("Euros");
        study.setId("PLANNING_" + sensorTaskId);
        
        study.setSegmentArray(addSegments(TaskType.planning, handler, sensorTaskId));
        
        return studyDoc;
    }
    
    /**
     * Creates the Segments response structure from the data of the SensorFeasibilityAnalysisHandler
     * @return 
     */
    private static SegmentPropertyType[] addSegments(TaskType type, SensorTaskHandler handler, String sensorTaskId) throws SQLException, ParseException {
        
        EarthObservationEquipment eoe = handler.getEarthObservationEquipment(sensorTaskId);
        
        List<Segment> segments = null;
        if (type.equals(TaskType.feasibility)) {
            segments = ((SensorFeasibilityHandler)handler).getSegments(sensorTaskId);
        }
        else {
            segments = ((SensorPlanningHandler)handler).getSegments(sensorTaskId);
        }
        
        SegmentPropertyType[] ss = new SegmentPropertyType[segments.size()];
        
        int i = 0;
        for (Segment segment : segments) {
            
            SegmentPropertyType s = SegmentPropertyType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            
            SegmentType seg = s.addNewSegment();
            
            //Times
            seg.setAcquisitionStartTime(segment.getStartOfAcquisition());
            seg.setAcquisitionStopTime(segment.getEndOfAcquisition());

            seg.setId("SEGMENT_" + segment.getSegmentId());
            
            seg.setStatus(segment.getStatus().getIdentifier());
            
            //EOP
            EarthObservationEquipmentType eoEquipment = seg.addNewAcquisitionMethod().addNewEarthObservationEquipment();
            eoEquipment.setId("SEGMENT_ACQUISITION_DATA_" + segment.getSegmentId());
            //Sensor
            SensorType sensor = eoEquipment.addNewSensor().addNewSensor();
            
            
            //Sensor Type
            CodeType instType = sensor.addNewSensorType();
            instType.setCodeSpace("urn:ogc:def:property:OGC:sensorType");
            instType.setStringValue(eoe.getSensorType());
            //Instrument Mode
            CategoryDocument instModeDoc = CategoryDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            CategoryType instMode = instModeDoc.addNewCategory();
            instMode.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
            instMode.setValue(segment.getInstrumentMode());
            
            sensor.addNewOperationalMode().set(instModeDoc);
            
            //Instrument identifier
            InstrumentType instrument = eoEquipment.addNewInstrument().addNewInstrument();
            instrument.setShortName(eoe.getSensorName());
            
            
            //Platform
            PlatformType plat = eoEquipment.addNewPlatform().addNewPlatform();
            plat.setSerialIdentifier(eoe.getPlatformId());
            
            if ("LEO GEO".contains(eoe.getOrbitType())) {
                plat.setOrbitType(OrbitTypeValueType.Enum.forString(eoe.getOrbitType()));
            }
            else
            {
                plat.setOrbitType(OrbitTypeValueType.Enum.forString("LEO"));
            }
            
            plat.setShortName(eoe.getPlatformName());

            eoEquipment.addNewIdentifier().setStringValue(eoe.getPlatformId() + " - " + eoe.getSensorName());
            eoEquipment.getIdentifier().setCodeSpace("NORAD_IDENTIFIER_DATA");

            eoEquipment.addNewDescription().setStringValue(eoe.getPlatformDescription());

            //Polygon
            PolygonType polygon = seg.addNewFootprint().addNewPolygon();
            polygon.setId("ACQUISITION_FOOTPRINT_" + segment.getSegmentId());

            CoordinatesType coords = CoordinatesType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");

            coords.setStringValue(segment.getPolygon().printCoordinatesGML());

            LinearRingDocument polDoc = LinearRingDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            LinearRingType lineRing = polDoc.addNewLinearRing();
            lineRing.setCoordinates(coords);
            polygon.addNewExterior().set(polDoc);
            
            SegmentValidationDataDocument valData = SegmentValidationDataDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            valData.addNewSegmentValidationData().setCloudCoverSuccessRate(segment.getCloudCoverSuccessRate());
            seg.addNewExtension().set(valData);
            
            DownlinkInformationDocument downlinkDoc = DownlinkInformationDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            DownlinkInformationType downlink = downlinkDoc.addNewDownlinkInformation();
            
            downlink.setAcquisitionDate(segment.getGroundStationDownlink().getEndOfVisibility());
            
            CategoryDocument acqStationDoc = CategoryDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            CategoryType acqStation = instModeDoc.addNewCategory();
            acqStation.setDefinition("urn:ogc:def:property:CEOS:eop:GroundStation");
            acqStation.setValue(segment.getGroundStationDownlink().getGroundStationId());
            
            downlink.addNewAcquisitionStation().set(acqStationDoc);
            
            seg.addNewExtension().set(downlinkDoc);
            
            ss[i] = s;
            i++;
        }
        
        return ss;
    }
    
}
