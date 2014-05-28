/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorPlanningServiceHandler.java
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
package net.eads.astrium.hmas.mp.tasking;

import java.util.List;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.Status;

/**
 *
 * @author re-sulrich
 */
public abstract class SensorPlanningServiceHandler {
    
    final String sensorType;
    final String sensorId;
    final SensorPlanningHandler dbHandler;
    
    String taskId;
    int estimatedCost;
    String currency;
    
    Status status;
    List<Segment> segments;

    public SensorPlanningServiceHandler(String sensorType, String sensorId, SensorPlanningHandler dbHandler) {
        this.sensorType = sensorType;
        this.sensorId = sensorId;
        this.dbHandler = dbHandler;
    }

    public abstract void doSubmit();
}
