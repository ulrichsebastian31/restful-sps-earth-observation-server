/*
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
public abstract class Geometry {
    
    List<Point> points;

    public List<Point> getPoints() {
        return points;
    }

    public Geometry() {
        points = new ArrayList<>();
    }

    public String printCoordinatesGML() {
        
        String print = "";
        
        for (Point point : points) {
            print += "" + point.printCoordinatesGML() + " ";
        }
        return print;
    }
    
    public String printCoordinatesGMLWithAltitude() {
        
        String print = "";
        
        for (Point point : points) {
            print += "" + point.printCoordinatesGMLWithAltitude()+ " ";
        }
        return print;
    }
    
    @Override
    public String toString() {
        return this.printCoordinatesGML();
    }
}
