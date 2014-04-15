/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.eads.astrium.hmas.rs.parsers.tasking;

import java.util.ArrayList;
import java.util.List;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Geometry;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;

/**
 *
 * @author re-sulrich
 */
public class TaskingParser {

    
    public static Geometry parseGeometry(String geometryString) {
        
        Geometry geometry = null;
        
        System.out.println("" + geometryString);
        
        String[] geom = geometryString.split("\\(", 2);
            
        String geomType = geom[0];
        String coords = geom[1].substring(0,geom[1].length() - 1);

        System.out.println("" + geomType + "\n" + coords + "\n" + coords.substring(1, coords.length() - 1));
        
        switch (geomType) {
//            POINT(6 10)
            case "POINT":

            break;
//            LINESTRING(3 4,10 50,20 25)
            case "LINESTRING":

            break;
//            POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))
            case "POLYGON":
                List<Point> points = new ArrayList<>();
                String[] pols = coords.substring(1, coords.length() - 1).split("\\),\\(");
                String exterior = pols[0];
                String[] pointsCoords = exterior.split(",");
                for (int i = 0; i < pointsCoords.length; i++) {
                    String[] pointCoords = pointsCoords[i].split(" ");
                    points.add(new Point(
                            Double.valueOf(pointCoords[1]), 
                            Double.valueOf(pointCoords[0]), 
                            0.0));
                }

                geometry = new Polygon(points);

            break;
//            MULTIPOINT(3.5 5.6, 4.8 10.5)
            case "MULTIPOINT":

            break;
//            MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))
            case "MULTILINESTRING":

            break;
//            MULTIPOLYGON(
            //(
            //(1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))
            //,
            //((6 3,9 2,9 4,6 3))
            //)
            case "MULTIPOLYGON":

            break;
        }

        return geometry;
    }
}
