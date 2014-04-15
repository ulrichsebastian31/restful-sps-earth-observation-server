/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Polygon.java
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
package net.eads.astrium.hmas.util.structures.tasking.geometries;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author re-sulrich
 */
public class Polygon extends Geometry {
    
    // Default constructor
    public Polygon()
    {
        
    }
    
    public Polygon(List<Point> points) {
        // Assign the received Lit of points to the member variable
        this.points = points;
        
        // Ensure that the Polygon is enclosed i.e. that the first Polygon point
        // is the same as the last one, if not add a copy of the first point to 
        // the end of the list
        if (!this.points.get(0).equals(this.points.get(this.points.size() - 1)))
        {
            this.points.add(this.points.get(0));
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        // Assign the received Lit of points to the member variable
        this.points = points;
        
        // Ensure that the Polygon is enclosed i.e. that the first Polygon point
        // is the same as the last one, if not add a copy of the first point to 
        // the end of the list
        if (!this.points.get(0).equals(this.points.get(this.points.size() - 1)))
        {
            this.points.add(this.points.get(0));
        }
    }
    
    
    public void addNewPoint(Point point) {
        
        // If this is the first new point, create the list and insert the point
        if (this.points == null) {
            this.points = new ArrayList<Point>();
            this.points.add(point);
        }
        else if (this.points.isEmpty()) {
            this.points.add(point);
        }
        // Otherwise...
        else
        {
            // If it is the second point in the list, duplicate the first point
            // and add it to the list to ensure the Polygon is always closed
            if (this.points.size() == 1)
            {
                this.points.add(this.points.get(0));
            }
            // then insert the new point one in penultimate position in the list
            this.points.add(this.points.size() - 1, point);
        }
    }
    
    public void add(Polygon oNewPoly)
    {
    	List<Point> oPnts = oNewPoly.getPoints();
    	
        // Loop through the New Polygon in the opposite direction of the first
        // and append the points to the existing Polygon. Skip the last point
        // as it should be a copy for the first i.e. an enclosed Polygon
        for (int iPntIndex = 2; iPntIndex <= oPnts.size(); iPntIndex++)
    	{
            addNewPoint(oPnts.get(oPnts.size() - iPntIndex));
    	}

    }
    
}
