/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SensorTask.java
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
package net.eads.astrium.hmas.util.structures.tasking;

import java.util.ArrayList;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskType;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public class SensorTask {
    
    private String sensorTaskId;
    private TaskType type;
    private Status status;
    
    private List<Segment> segments;
    
    public SensorTask(String sensorTaskId, TaskType type, Status status) {
        
        this.sensorTaskId = sensorTaskId;
        this.type = type;
        this.status = status;
    }

    public String getSensorTaskId() {
        return sensorTaskId;
    }

    public void setSensorTaskId(String sensorTaskId) {
        this.sensorTaskId = sensorTaskId;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Segments
     */
    
    public List<Segment> getSegments() {
        return segments;
    }

    public void addSegments(List<Segment> segments) {
        
        if(this.segments != null)
            this.segments = new ArrayList<Segment>();
        this.segments.addAll(segments);
    }
    public void addSegment(Segment segment) {
        
        if(this.segments != null)
            this.segments = new ArrayList<Segment>();
        this.segments.add(segment);
    }
    
    
}
