/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.dreamutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.eads.astrium.hmas.util.Algorithms;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Circle;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestGeometries {
    
    
    @Test
    public void testCircleFromPointsThenCircleToPoints() {
        
        Circle c = new Circle(new Point(1.0, 1.0, 0.0), 2.0);
        
        Circle c2 = new Circle(c.getPoints().get(0), c.getPoints().get(1), c.getPoints().get(2));
        
        System.out.println("C : " + c.getCenter() + " - " + c.getRadius());
        
        System.out.println("C2 : " + c2.getCenter() + " - " + c2.getRadius());
    }
    
//    @Test
    public void testCircleFromPoints() {
        Circle c = new Circle(
                new Point(0, 0, 0),
                new Point(1, 3, 0),
                new Point(2, 0, 0)
                );
    }
//    @Test
    public void testDistance() {
        
        Point a = new Point(0.0, 0.0, 0.0);
        Point b = new Point(3.0, 4.0, 0.0);
        
        double latDiff = b.getLatitude() - a.getLatitude();
        double lonDiff = b.getLongitude() - a.getLongitude();
        
        System.out.println("latDiff : " + latDiff + "\nlonDiff : " + lonDiff + "\nResult : " + Math.sqrt(
                (latDiff * latDiff) + (lonDiff * lonDiff)
           ));
    }
    
//    @Test
    public void testClockwise() {
        
        System.out.println("Clockwise test");
        
        List<Point> clock = new ArrayList<>();
//        23.32,54.65,25.43,50.23
//        clock.add(new Point(23.32, 54.65, 0.0));
//        clock.add(new Point(25.43, 54.65, 0.0));
//        clock.add(new Point(25.43, 50.23, 0.0));
//        clock.add(new Point(23.32, 50.23, 0.0));
//        clock.add(new Point(23.32, 54.65, 0.0));
        
        clock.add(new Point(20.0, 20.0, 0.0));
        clock.add(new Point(20.0, 0.0, 0.0));
        clock.add(new Point(0.0, 0.0, 0.0));
        clock.add(new Point(0.0, 20.0, 0.0));
        clock.add(new Point(20.0, 20.0, 0.0));
        
        Polygon cw = new Polygon(clock);
        
        Algorithms.getClockWise(cw);
        
        System.out.println("" + cw.printCoordinatesGML());
    }
    
//    @Test
    public void testAntiClockwise() {
        
        System.out.println("AntiClockwise test");
        
        List<Point> antiClock = new ArrayList<>();
        
        antiClock.add(new Point(20.0, 20.0, 0.0));
        antiClock.add(new Point(0.0, 20.0, 0.0));
        antiClock.add(new Point(0.0, 0.0, 0.0));
        antiClock.add(new Point(20.0, 0.0, 0.0));
        antiClock.add(new Point(20.0, 20.0, 0.0));
        
        Polygon acw = new Polygon(antiClock);
        
        
        
        Algorithms.getClockWise(acw);
        
        System.out.println("" + acw.printCoordinatesGML());
    }
}
