/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.processes.feasibilitystudygenerator;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import net.eads.astrium.hmas.dbhandler.tasking.SensorFeasibilityHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.dbhandler.tasking.SensorTaskHandler;
import net.eads.astrium.hmas.util.structures.tasking.EarthObservationEquipment;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
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
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
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
        
        FeasibilityStudyDocument studyDoc = FeasibilityStudyDocument.Factory.newInstance();
        FeasibilityStudyType study = studyDoc.addNewFeasibilityStudy();
        
        //TODO: estimated cost by segment ? by task ? + add sensorTask status ?
//        study.addNewEstimatedCost().setDoubleValue(this.feasibilityHandler.getEstimatedCost());
//        study.getEstimatedCost().setUom("Euros");
        study.setId(sensorTaskId);
        
        study.setSegmentArray(addSegments(TaskType.feasibility, handler, sensorTaskId));
        
        return studyDoc;
    }
    
    public static ProgrammingStatusDocument getProgrammingStatus(SensorPlanningHandler handler, String sensorTaskId) throws SQLException, ParseException {
        
        ProgrammingStatusDocument studyDoc = ProgrammingStatusDocument.Factory.newInstance();
        ProgrammingStatusType study = studyDoc.addNewProgrammingStatus();
        
        //TODO: estimated cost by segment ? by task ? + add sensorTask status ?
//        study.addNewEstimatedCost().setDoubleValue(this.feasibilityHandler.getEstimatedCost());
//        study.getEstimatedCost().setUom("Euros");
        study.setId(sensorTaskId);
        
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
            
            SegmentPropertyType s = SegmentPropertyType.Factory.newInstance();
            
            SegmentType seg = s.addNewSegment();
            
            //Times
            seg.setAcquisitionStartTime(segment.getStartOfAcquisition());
            seg.setAcquisitionStopTime(segment.getEndOfAcquisition());

            seg.setId(segment.getSegmentId());
            
            seg.setStatus(segment.getStatus().getIdentifier());
            
            //EOP
            EarthObservationEquipmentType eoEquipment = seg.addNewAcquisitionMethod().addNewEarthObservationEquipment();
            //Sensor
            SensorType sensor = eoEquipment.addNewSensor().addNewSensor();
            
            
            //Sensor Type
            CodeType instType = sensor.addNewSensorType();
            instType.setCodeSpace("urn:ogc:def:property:OGC:sensorType");
            instType.setStringValue(eoe.getSensorType());
            //Instrument Mode
            CategoryType instMode = CategoryType.Factory.newInstance();
            instMode.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
            instMode.setValue(segment.getInstrumentMode());
            sensor.addNewOperationalMode().set(instMode);
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

            eoEquipment.addNewDescription().setStringValue(eoe.getPlatformDescription());

            //Polygon
            PolygonType polygon = seg.addNewFootprint().addNewPolygon();

            CoordinatesType coords = CoordinatesType.Factory.newInstance();
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");

            coords.setStringValue(segment.getPolygon().printCoordinatesGML());

            LinearRingType lineRing = LinearRingType.Factory.newInstance();
            lineRing.setCoordinates(coords);
            polygon.addNewExterior().setAbstractRing(lineRing);
            
            SegmentValidationDataDocument valData = SegmentValidationDataDocument.Factory.newInstance();
            valData.addNewSegmentValidationData().setCloudCoverSuccessRate(segment.getCloudCoverSuccessRate());
            seg.addNewExtension().set(valData);
            
            DownlinkInformationDocument downlinkDoc = DownlinkInformationDocument.Factory.newInstance();
            DownlinkInformationType downlink = downlinkDoc.addNewDownlinkInformation();
            
            downlink.setAcquisitionDate(segment.getGroundStationDownlink().getEndOfVisibility());
            
            CategoryType acqStation = CategoryType.Factory.newInstance();
            acqStation.setDefinition("urn:ogc:def:property:CEOS:eop:GroundStation");
            acqStation.setValue(segment.getGroundStationDownlink().getGroundStationId());
            
            downlink.addNewAcquisitionStation().set(acqStation);
            
            seg.addNewExtension().set(downlinkDoc);
            
            ss[i] = s;
            i++;
        }
        
        return ss;
    }
    
}
