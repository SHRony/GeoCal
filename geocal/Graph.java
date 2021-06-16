/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class Graph {
    
    Polyline polyline;
    ObservableList<Double> points;
    public static final double ZOOM = 48.0;
    
    Graph()
    {
        polyline = new Polyline();
        polyline.setFill(Color.TRANSPARENT);
        polyline.setStroke(Color.BLACK);
        polyline.setStrokeWidth(1.5);
        points = polyline.getPoints();
    }
    
    /**
     * 
     * Take input: user function for draw graph
     * 
     * @param layout of primary stage
     */
    public static void Own_graph(GeoPane layout,String fun){
                if(fun==null || fun.isEmpty())
                {
                    Alert.display("Error..!", "Please enter your function");
                    
                }
                else
                {
                    Expression ex = new Expression(fun);
                    
                    double prex=-7499,prey=ZOOM*ex.getValue(-7499.0/ZOOM);
                    for(int x =-7500; x<=7500; x+=1)
                    {
                        double y=ex.getValue(x/ZOOM);

                        if(Double.isNaN(y)){
                            continue;
                        }
                        y*=ZOOM;
                        if(diffSign(y, prey) && Math.abs(y-prey)>100.0){
                            prex=x; prey=y;
                            continue;
                        }
                        

                        Line line = MyLine(prex, prey, x, y);
                        prex=x; prey=y;
                        layout.chld.getChildren().addAll(line);
                        GraphMenu.currentFunctions.add(line);
            
                    }
                    
                }


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
        //Rotate rotate = new Rotate(180, Rotate.X_AXIS);
        //line.getTransforms().add(rotate);
        return line;
    }
    
}
