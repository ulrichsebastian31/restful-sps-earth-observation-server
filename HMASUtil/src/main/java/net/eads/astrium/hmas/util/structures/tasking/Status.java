/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Status.java
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

import java.util.Date;

/**
 *
 * @author re-sulrich
 */
public class Status {
    
    private String identifier;
    private int percentCompletion;
    private String message;
    private Date updateTime;
    private Date estimatedTimeOfCompletion;

    public Status(String identifier, int percentCompletion, String message, Date updateTime, Date estimatedTimeOfCompletion) {
        this.identifier = identifier;
        this.percentCompletion = percentCompletion;
        this.message = message;
        this.updateTime = updateTime;
        this.estimatedTimeOfCompletion = estimatedTimeOfCompletion;
    }

    public int getPercentCompletion() {
        return percentCompletion;
    }

    public void setPercentCompletion(int percentCompletion) {
        this.percentCompletion = percentCompletion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getEstimatedTimeOfCompletion() {
        return estimatedTimeOfCompletion;
    }

    public void setEstimatedTimeOfCompletion(Date estimatedTimeOfCompletion) {
        this.estimatedTimeOfCompletion = estimatedTimeOfCompletion;
    }

    @Override
    public String toString() {
        return "Status{" + "identifier=" + identifier + ", percentCompletion=" + percentCompletion + ", updateTime=" + updateTime + ", estimatedTimeOfCompletion=" + estimatedTimeOfCompletion + ", message=" + message + '}';
    }
}
