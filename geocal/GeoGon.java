/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import java.util.Vector;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Administrator
 */
public class GeoGon extends Polygon{
    Vector<Point> vec=new Vector<>();
    GeoGon()
    {
        super();
        vec=new Vector<>();      
    }
    GeoGon(Vector<Point> V)
    {
        super();
        vec=V;
        for(int i=0;i<vec.size();i++)
        {
            this.getPoints().add(vec.get(i).getX());
            this.getPoints().add(vec.get(i).getY());
        }
    }
}
