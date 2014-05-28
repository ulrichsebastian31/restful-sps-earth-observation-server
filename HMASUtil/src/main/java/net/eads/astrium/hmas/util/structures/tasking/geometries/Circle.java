/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.structures.tasking.geometries;

/**
 *
 * @author re-sulrich
 */
public class Circle extends Geometry {
    
    private Point center;
    private double radius;

    private void setPointsFromCoords() {
        this.points.add(new Point(this.center.getLongitude(), this.center.getLatitude() + radius, 0.0));
        this.points.add(new Point(this.center.getLongitude() + radius, this.center.getLatitude(), 0.0));
        this.points.add(new Point(this.center.getLongitude(), this.center.getLatitude() - radius, 0.0));
    }
    private void setCoordsFromPoints() {
        
        center = circleCenter(points.get(0), points.get(1), points.get(2));
//        System.out.println("Center : " + center);
        radius = getDistance(center, points.get(0));
        
//        System.out.println("Radius : " + radius + " - " + getDistance(center, points.get(1)) + " - " + getDistance(center, points.get(2)));
    }
    
    private double getDistance(Point a, Point b) {
        
        double latDiff = b.getLatitude() - a.getLatitude();
        double lonDiff = b.getLongitude() - a.getLongitude();
        
        return Math.sqrt(
                (latDiff * latDiff) + (lonDiff * lonDiff)
           );
    }
    
    private Point circleCenter(Point A, Point B, Point C) {

        double yDelta_a = B.getLatitude() - A.getLatitude();
        double xDelta_a = B.getLongitude() - A.getLongitude();
        double yDelta_b = C.getLatitude() - B.getLatitude();
        double xDelta_b = C.getLongitude() - B.getLongitude();
        Point c = new Point(0,0,0);

        double aSlope = yDelta_a/xDelta_a;
        double bSlope = yDelta_b/xDelta_b;

        Point AB_Mid = new Point(
                (A.getLongitude()+B.getLongitude())/2, 
                (A.getLatitude()+B.getLatitude())/2, 
                0.0);
        Point BC_Mid = new Point(
                (B.getLongitude()+C.getLongitude())/2, 
                (B.getLatitude()+C.getLatitude())/2, 
                0.0);

//        System.out.println("AB mid : " + AB_Mid + "\nBC mid : " + BC_Mid);
        
        if(yDelta_a == 0)         //aSlope == 0
        {
            c.setLongitude(AB_Mid.getLongitude());
            if (xDelta_b == 0)         //bSlope == INFINITY
            {
                c.setLatitude(BC_Mid.getLatitude());
            }
            else
            {
                c.setLatitude(BC_Mid.getLatitude() + (BC_Mid.getLongitude()-c.getLongitude())/bSlope);
            }
        }
        else if (yDelta_b == 0)               //bSlope == 0
        {
            c.setLongitude(BC_Mid.getLongitude());
            if (xDelta_a == 0)             //aSlope == INFINITY
            {
                c.setLatitude(AB_Mid.getLatitude());
            }
            else
            {
                c.setLatitude(AB_Mid.getLatitude() + (AB_Mid.getLongitude()-c.getLongitude())/aSlope);
            }
        }
        else if (xDelta_a == 0)        //aSlope == INFINITY
        {
            c.setLatitude(AB_Mid.getLatitude());
            c.setLongitude(bSlope*(BC_Mid.getLatitude()-c.getLatitude()) + BC_Mid.getLongitude());
        }
        else if (xDelta_b == 0)        //bSlope == INFINITY
        {
            c.setLatitude(BC_Mid.getLatitude());
            c.setLongitude(aSlope*(AB_Mid.getLatitude()-c.getLatitude()) + AB_Mid.getLongitude());
        }
        else
        {
            c.setLongitude((aSlope*bSlope*(AB_Mid.getLatitude()-BC_Mid.getLatitude()) - aSlope*BC_Mid.getLongitude() + bSlope*AB_Mid.getLongitude())/(bSlope-aSlope));
            c.setLatitude(AB_Mid.getLatitude() - (c.getLongitude() - AB_Mid.getLongitude())/aSlope);
        }

        return c;
    }
    
    
    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
        
        setPointsFromCoords();
    }
    
    public Circle(Point p1, Point p2, Point p3) {
        this.points.add(p1);
        this.points.add(p2);
        this.points.add(p3);
        setCoordsFromPoints();
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
    
}
