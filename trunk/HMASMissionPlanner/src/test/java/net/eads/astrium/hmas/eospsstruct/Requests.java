/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Requests.java
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

import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.opengis.eosps.x20.CancelDocument;
import net.opengis.eosps.x20.ConfirmDocument;
import net.opengis.eosps.x20.FeasibilityStudyDocument;
import net.opengis.eosps.x20.FeasibilityStudyType;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityType;
import net.opengis.eosps.x20.ReserveDocument;
import net.opengis.eosps.x20.ReserveType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.SubmitDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDType;
import net.opengis.eosps.x20.SubmitType;
import net.opengis.eosps.x20.UpdateDocument;
import net.opengis.eosps.x20.UpdateType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.sps.x21.CancelType;
import net.opengis.sps.x21.ConfirmType;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class Requests {
    
    @Test
    public void test() {
        
        System.out.println(cancel().xmlText());
        System.out.println();
        System.out.println();
        System.out.println(confirm().xmlText());
        System.out.println();
        System.out.println();
        System.out.println(getFeasibility().xmlText());
        System.out.println();
        System.out.println();
        System.out.println(reserve().xmlText());
        System.out.println();
        System.out.println();
        System.out.println(submitSegmentById().xmlText());
        System.out.println();
        System.out.println();
        System.out.println(update().xmlText());
        System.out.println();
        System.out.println();
        System.out.println(submit().xmlText());
        System.out.println();
        System.out.println();
    }
    
    public static GetFeasibilityDocument getFeasibility()
    {
        GetFeasibilityDocument doc = GetFeasibilityDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        GetFeasibilityType getFeasibility = doc.addNewGetFeasibility();
        
        getFeasibility.setService("EOSPS");
        getFeasibility.setAcceptFormat("text/xml");
        getFeasibility.setVersion("2.0");
        getFeasibility.setProcedure("urn:ogc:object:feature:Sensor:sentinel1:HMA:SAR-1");
        getFeasibility.setEoTaskingParameters(EoTaskingParameters.createEOTaskingParameters().getTaskingParameters());
        
        
        return doc;
    }
    
    public static SubmitDocument submit()
    {
        SubmitDocument doc = SubmitDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        SubmitType submit = doc.addNewSubmit();
        submit.setService("EOSPS");
        submit.setAcceptFormat("text/xml");
        submit.setVersion("2.0");
        submit.setProcedure("urn:ogc:object:feature:Sensor:sentinel1:HMA:SAR-1");
        
        
        submit.setEoTaskingParameters(EoTaskingParameters.createEOTaskingParameters().getTaskingParameters());
        
        return doc;
    }
    
    public static CancelDocument cancel()
    {
        CancelDocument doc = CancelDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        CancelType cancel = doc.addNewCancel();
        
        cancel.setService("EOSPS");
        cancel.setAcceptFormat("text/xml");
        cancel.setVersion("2.0");
        cancel.setTask("09812353");
        
        return doc;
        
    }
    
    public static ReserveDocument reserve()
    {
        ReserveDocument doc = ReserveDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ReserveType reserve = doc.addNewReserve();
        
        reserve.setService("EOSPS");
        reserve.setAcceptFormat("text/xml");
        reserve.setVersion("2.0");
        reserve.setProcedure("urn:ogc:object:feature:Sensor:sentinel1:HMA:SAR-1");
        reserve.setEoTaskingParameters(EoTaskingParameters.createEOTaskingParameters().getTaskingParameters());
        
        return doc;
    }
    
    public static UpdateDocument update()
    {
        UpdateDocument doc = UpdateDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        UpdateType update = doc.addNewUpdate();
        
        update.setService("EOSPS");
        update.setAcceptFormat("text/xml");
        update.setVersion("2.0");
        update.setTargetTask("09812353");
        
        return doc;
    }
    
    public static SubmitSegmentByIDDocument submitSegmentById()
    {
        SubmitSegmentByIDDocument doc = SubmitSegmentByIDDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        SubmitSegmentByIDType submitSegmentById = doc.addNewSubmitSegmentByID();
        
        submitSegmentById.setService("EOSPS");
        submitSegmentById.setAcceptFormat("text/xml");
        submitSegmentById.setVersion("2.0");
        submitSegmentById.setTask("09812353");
        submitSegmentById.addSegmentID("41K8GF7G98H");
        
        return doc;
    }
    
    public static ConfirmDocument confirm()
    {
        ConfirmDocument doc = ConfirmDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        ConfirmType confirm = doc.addNewConfirm();
        
        confirm.setService("EOSPS");
        confirm.setAcceptFormat("text/xml");
        confirm.setVersion("2.0");
        confirm.setTask("09812353");
        
        return doc;
    }
}
