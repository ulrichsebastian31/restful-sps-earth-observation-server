/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.eads.astrium.hmas;

import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.LinearRingDocument;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.sps.x21.SensorOfferingDocument;
import net.opengis.sps.x21.SensorOfferingType;
import net.opengis.swes.x21.AbstractContentsType;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestXmlOptions {

    @Test
    public void test() {
        
        AbstractContentsType.Offering offering = AbstractContentsType.Offering.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        SensorOfferingDocument sensorOfferingDocument = SensorOfferingDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        SensorOfferingType sensorOffering = sensorOfferingDocument.addNewSensorOffering();
        
        sensorOffering.setDescription("Programming service for sentinel 1 only");
        sensorOffering.setIdentifier("Sentinel 1");
//        sensorOffering.addNewExtension();
        sensorOffering.setProcedure("Sentinel 1");

        SensorOfferingType.ObservableArea observableArea = sensorOffering.addNewObservableArea();

        SensorOfferingType.ObservableArea.ByPolygon byPolygon = observableArea.addNewByPolygon();
        PolygonType polygon = byPolygon.addNewPolygon();
        polygon.setId("gid0");

        
        AbstractRingPropertyType exterior = polygon.addNewExterior();
        
        LinearRingDocument linearRingDocument = LinearRingDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        LinearRingType linearRing = linearRingDocument.addNewLinearRing();
        
        DirectPositionListType posList = linearRing.addNewPosList();
        posList.setSrsName("urn:ogc:def:crs:EPSG::4326");

        String coordinates = "-90 -180 -90 180 90 180 90 -180";
        
        posList.setStringValue(coordinates);
        
        System.out.println("lin ring : " + linearRing.xmlText(OGCNamespacesXmlOptions.getInstance()));
        
        if (exterior.getAbstractRing() == null) exterior.addNewAbstractRing();
        exterior.getAbstractRing().substitute(linearRingDocument.schemaType().getDocumentElementName(), linearRingDocument.schemaType());
        
        exterior.getAbstractRing().set(linearRingDocument);
        
        System.out.println("Qname : " + sensorOfferingDocument.schemaType() + " - " + SensorOfferingDocument.type + "\r\n" +
                "Elements : " + sensorOfferingDocument.schemaType().getDocumentElementName() + " - " + SensorOfferingDocument.type.getDocumentElementName());
        
//        exterior.getAbstractRing().changeType(linearRingDocument.schemaType());
        
        System.out.println("abs ring 1 : " + exterior.getAbstractRing().xmlText(OGCNamespacesXmlOptions.getInstance()));
        
        exterior.getAbstractRing().substitute(linearRingDocument.schemaType().getDocumentElementName(), linearRingDocument.schemaType());

        System.out.println("abs ring 2 : " + exterior.getAbstractRing().xmlText(OGCNamespacesXmlOptions.getInstance()));
        
        
        if (offering.getAbstractOffering() == null) offering.addNewAbstractOffering();
        
        offering.getAbstractOffering().substitute(sensorOfferingDocument.schemaType().getDocumentElementName(), sensorOfferingDocument.schemaType());

        offering.getAbstractOffering().set(sensorOfferingDocument);
        
//        offering.getAbstractOffering().changeType(sensorOfferingDocument.schemaType());
        
//        offering.getAbstractOffering().substitute(sensorOfferingDocument.schemaType().getDocumentElementName(), sensorOfferingDocument.schemaType());
        
        System.out.println("" + offering.xmlText(OGCNamespacesXmlOptions.getInstance()));
    }
}
