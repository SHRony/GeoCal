/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

/**
 *
 * @author Lenovo
 */
public class Graph {
    
    Polyline polyline;
    ObservableList<Double> points;
    
    Graph()
    {
        polyline = new Polyline();
        polyline.setFill(Color.TRANSPARENT);
        polyline.setStroke(Color.BLACK);
        polyline.setStrokeWidth(1.5);
        points = polyline.getPoints();
    }
    
    public static void Sin(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=150.0*Math.sin(x/48.0);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Cos(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=150.0*Math.cos(x/48.0);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Tan(GeoPane pane)
    {
        double prex=-7499,prey=150.0*Math.tan(-7499.0/48.0);
        for(int x =-7500; x<=7500; x+=1)
        {
            double y=150.0*Math.tan(x/48.0);
            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
        }
    }
    
    public static void Cot(GeoPane pane)
    {
        double prex=-7499,prey=150.0*(1.0/Math.tan(-7499.0/48.0));
        for(int x =-7500; x<=7500; x+=1)
        {
            if(Math.tan(x/48.0)==0.0)
            {
                continue;
            }
            double y=150.0*(1.0/Math.tan(x/48.0));

            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
            
        }
    }
    public static void Sec(GeoPane pane)
    {
        double prex=-7499,prey=150.0*(1.0/Math.cos(-7499.0/48.0));
        for(int x =-7500; x<=7500; x+=1)
        {
            if(Math.cos(x/48.0)==0.0)
            {
                continue;
            }
            double y=150.0*(1.0/Math.cos(x/48.0));

            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
            
        }
    }
    
    public static void Cosec(GeoPane pane)
    {
        double prex=-7499,prey=150.0*(1.0/Math.sin(-7499.0/48.0));
        for(int x =-7500; x<=7500; x+=1)
        {
            if(Math.sin(x/48.0)==0.0)
            {
                continue;
            }
            double y=150.0*(1.0/Math.sin(x/48.0));

            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
            
        }
    }
    
    public static void Square(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 0.025;
        for(int x =-7500; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*x*x;
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Sqrt(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 40.0;
        for(int x =0; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.sqrt(x/48.0);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    public static void Ln(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 30.0;
        for(int x =1; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.log(x);
            g.points.add(y);
            //System.out.println(x+"..."+y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Log10(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 30.0;
        for(int x =1; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.log10(x);
            g.points.add(y);
            //System.out.println(x+"..."+y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Log2(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 30.0;
        for(int x =1; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.log10(x)/Math.log10(2.0);
            g.points.add(y);
            //System.out.println(x+"..."+y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Abs(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=Math.abs(x);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Ex(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 50.0;
        for(int x =-1500; x<=500; x++)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.exp(x/100.0);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Cubic(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x++)
        {
            g.points.add(x+0.0);
            double y=Math.pow(x/48.0, 3.0);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static boolean diffSign(double x, double y)
    {
        return ((x<0.0)&&(y>=0.0)) || ((y<0.0)&&(x>=0.0));
    }
    
    private static Line MyLine(double startX, double startY, double endX, double endY)
    {
        Line line = new Line(startX, startY, endX, endY);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1.15);
        return line;
    }
    
}
