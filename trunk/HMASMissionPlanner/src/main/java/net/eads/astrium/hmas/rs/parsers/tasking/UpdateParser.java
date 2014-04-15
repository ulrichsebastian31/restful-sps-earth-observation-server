/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetFeasibilityParser.java
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
package net.eads.astrium.hmas.rs.parsers.tasking;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.eads.astrium.hmas.rs.exceptions.XMLParsingException;
import net.eads.astrium.hmas.rs.parsers.ExtensibleRequestParser;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Circle;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Geometry;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.UpdateDocument;
import net.opengis.eosps.x20.UpdateType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CircleType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author re-sulrich
 */
public class UpdateParser {

    public UpdateDocument createXMLUpdate(MultivaluedMap<String, String> params)
            throws MissingParameterException, XMLParsingException, ParseException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        
        Geometry geometry = null;
        
        if (params.get("box") != null && !params.get("box").equals("")) {
            String[] box = null;
        
            box = params.remove("box").get(0).split(",");
            if (box.length < 4) {
                throw new ParseException("box", -1);
            }
            
            List<Point> points = new ArrayList<>();
            points.add(new Point(Double.valueOf(box[0]), Double.valueOf(box[1]), 0.0));
            points.add(new Point(Double.valueOf(box[2]), Double.valueOf(box[1]), 0.0));
            points.add(new Point(Double.valueOf(box[2]), Double.valueOf(box[3]), 0.0));
            points.add(new Point(Double.valueOf(box[0]), Double.valueOf(box[3]), 0.0));
            points.add(new Point(Double.valueOf(box[0]), Double.valueOf(box[1]), 0.0));
            
            geometry = new Polygon(points);
        }
        
        if (params.get("radius") != null && !params.get("radius").equals("")) {
            double lon = Double.valueOf(params.remove("lon").get(0));
            double lat = Double.valueOf(params.remove("lat").get(0));
            double radius = Double.valueOf(params.remove("radius").get(0));
            
            geometry = new Circle(new Point(lon, lat, 0.0), radius);
        }
        
        if (params.get("geometry") != null && !params.get("geometry").equals("")) {
            
            geometry = TaskingParser.parseGeometry(params.remove("geometry").get(0));
        }
        
        String procedure = params.getFirst("procedure");
        String start = params.getFirst("start");
        String end = params.getFirst("end");
        
        //Create XML request
        UpdateDocument doc = UpdateDocument.Factory.newInstance();
        UpdateType getFeas = doc.addNewUpdate();
        CoverageProgrammingRequestType eoTP = 
                getFeas.addNewEoTaskingParameters().addNewCoverageProgrammingRequest();
        
        if (geometry instanceof Polygon) {
            
            AbstractRingPropertyType ext = 
                    eoTP.addNewRegionOfInterest().addNewPolygon().addNewExterior();

            CoordinatesType coords = CoordinatesType.Factory.newInstance();
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");
            coords.setStringValue(geometry.printCoordinatesGML());
            LinearRingType lineRing = LinearRingType.Factory.newInstance();
            lineRing.setCoordinates(coords);
            ext.setAbstractRing(lineRing);
        }
        else if (geometry instanceof Circle) {
            
            CircleType ext = eoTP.addNewRegionOfInterest().addNewCircle();

            CoordinatesType coords = CoordinatesType.Factory.newInstance();
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");
            coords.setStringValue(geometry.printCoordinatesGML());
            
            ext.setCoordinates(coords);
        }
        TimePeriodType tp = 
                eoTP.addNewTimeOfInterest().addNewSurveyPeriod().addNewTimePeriod();
        tp.addNewBeginPosition().setStringValue(start);
        tp.addNewEndPosition().setStringValue(end);
        
        eoTP.addNewAcquisitionType().addNewMonoscopicAcquisition();
        
        //Add extensibleRequest parameters
        getFeas.setService(extensibleRequest.getService());
        getFeas.setVersion(extensibleRequest.getVersion());
        getFeas.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        getFeas.setProcedure(procedure);
        
        return doc;
    }
    
    public UpdateDocument createXMLUpdate(String xmlRequest)
            throws MissingParameterException, XMLParsingException
    {
        UpdateDocument doc = null;
        try {
            doc = UpdateDocument.Factory.parse(xmlRequest);
        } catch (XmlException ex) {
            ex.printStackTrace();
            
            throw new XMLParsingException(
                    "Failed parsing XML request : " + ex.getMessage(), 
                    "text/xml");
        }
        
        return doc;
    }
}
