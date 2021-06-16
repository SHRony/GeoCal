/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import static javafx.scene.transform.Rotate.X_AXIS;
import javafx.scene.transform.Scale;

/**
 *
 * @author Administrator
 */
public class GeoPane extends GridPane{
    class Delta{
        double x,y;
        Delta(double X,double Y)
        {
            x=X;
            y=Y;
        }
        Delta()
        {
            x=y=0;
        }
    };
    Delta delta=new Delta();
    Pane chld = new Pane();
    double perUnit=50;
    DoubleProperty Unit=new SimpleDoubleProperty(); 
    DoubleProperty pUnit = new SimpleDoubleProperty();
    DoubleProperty BalanceFactor = new SimpleDoubleProperty();
    EventHandler drag = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent ev)
            {
                mouse_drag(ev);
            }
        };
    double current_zoom =1.0;
    GeoPane()
    {
        super();
        Unit.set(1.0/perUnit);
        pUnit.set(perUnit);
        BalanceFactor.set(1);
        this.prefHeightProperty().bind(GeoCal.root.heightProperty());
        this.prefWidthProperty().bind(GeoCal.root.widthProperty());
        Rotate flip = new Rotate(180,X_AXIS);
        chld.getTransforms().add(flip);
        this.getStylesheets().add("img/style.css");
        this.getStyleClass().add("graph_back");
        this.chld.getStyleClass().add("graph_front");
        chld.setTranslateX(this.getWidth()/2);
        chld.setTranslateY(this.getHeight()/2);
        this.getChildren().add(chld);
        this.setAlignment(Pos.CENTER);
        for(int i=-5000;i<=5000;i+=10)
        {
            Line lx = new Line(i,-5000,i,5000);
            Line ly = new Line(-5000,i,5000,i);
            lx.setStroke(Color.BLACK);
            ly.setStroke(Color.BLACK);
            if(i==0)
            {
                lx.setStrokeWidth(1.2);
                ly.setStrokeWidth(1.2);
            }
            else if(i%50==0)
            {
                GeoLabel tx = new GeoLabel(i,0,i,this.Unit);
                GeoLabel ty = new GeoLabel(0,i,i,this.Unit);
                tx.getTransforms().add(flip);
                ty.getTransforms().add(flip);
                tx.calibrate(this.Unit.get());
                ty.calibrate(this.Unit.get());
                
//                tx.textProperty().bind(Unit.multiply(i).asString("%1.1e"));
//                ty.textProperty().bind(Unit.multiply(i).asString("%1.1e"));
//                tx.setScaleX(0.7);
//                ty.setScaleX(0.7);
//                tx.setScaleY(0.7);
//                ty.setScaleY(0.7);
//                tx.setLayoutX(i);
//                tx.setLayoutY(0);
//                ty.setLayoutX(0);
//                ty.setLayoutY(i);
//                tx.setOpacity(0.7);
//                ty.setOpacity(0.7);
                lx.setStrokeWidth(1.1);
                ly.setStrokeWidth(1.1);
                lx.setOpacity(0.3);
                ly.setOpacity(0.3);
                chld.getChildren().add(tx);
                chld.getChildren().add(ty);
            }
            else
            {
                lx.setOpacity(0.1);
                ly.setOpacity(0.1);
            }
            chld.getChildren().add(lx);
            chld.getChildren().add(ly);
        }
        this.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent ev)
            {
                mouse_click(ev);
            }
        }
        );
        this.setOnMouseDragged(drag);
        this.setOnScroll(new EventHandler<ScrollEvent>(){
            @Override
            public void handle(ScrollEvent ev)
            {
                mouse_scroll(ev);
            }
        });
    }
    void mouse_click(MouseEvent ev)
    {
        delta = new Delta(ev.getX()-chld.getTranslateX(),ev.getY()-chld.getTranslateY());
    }
    void mouse_drag(MouseEvent ev)
    {    
        if(ev.getX()-delta.x>-4500&&ev.getX()-delta.x<4500)
        chld.setTranslateX(ev.getX()-delta.x);
        if(ev.getY()-delta.y>-4500&&ev.getY()-delta.y<4500)
        chld.setTranslateY(ev.getY()-delta.y);
        
    }
    void mouse_scroll(ScrollEvent event)
    {
        try{
            double zoomFactor = 1.01;
                double deltaY = event.getDeltaY()*1.5;
                
                if (deltaY < 0)
                zoomFactor = 0.99;
                if(current_zoom>2.5&&deltaY>0)
                {
                    if(perUnit>5001) return ;
                    zoomFactor=1.0/current_zoom;
                    perUnit*=10.0;
                    Unit.set(1.0/perUnit);
                    pUnit.set(perUnit);
                    this.chld.setTranslateX(Math.max(-5000,Math.min(5000,10*this.chld.getTranslateX()-9*event.getX())));
                    this.chld.setTranslateY(Math.max(-5000, Math.min(5000,10*this.chld.getTranslateY()-9*event.getY())));
                    GraphHome.buttonPressed();
                }
                else if(current_zoom<1&&deltaY<0)
                {
                    if(perUnit<0.00000051) return ;
                    zoomFactor = 2.5/current_zoom;
                    perUnit/=10.0;
                    
                    Unit.set(1.0/perUnit);
                    pUnit.set(perUnit);
                    this.chld.setTranslateX(Math.max(-4500,Math.min(4500,(this.chld.getTranslateX()+event.getX()*9)/10)));
                    this.chld.setTranslateY(Math.max(-4500,Math.min(4500,(this.chld.getTranslateY()+event.getY()*9)/10))); 
                    GraphHome.buttonPressed();
                    
                }
                
                current_zoom = zoomFactor*current_zoom;
                BalanceFactor.set(1.0/current_zoom);
                Scale scale = new Scale();
                scale.setPivotX(event.getX());
                scale.setPivotY(event.getY());
                scale.setX(this.getScaleX() * zoomFactor);
                scale.setY(this.getScaleY() * zoomFactor);
                this.getTransforms().add(scale);
                event.consume();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

}
