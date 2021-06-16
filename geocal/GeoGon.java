/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import static geocal.GeoSegment.draw;
import java.util.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class GeoGon {
    Vector dependent = new Vector();
    Label name = new Label();
    Vector<GeoPoint> vecPoint = new Vector<GeoPoint>();
    Vector<GeoSegment> vecSeg = new Vector<GeoSegment> ();
    GeoLine lst;
    GeoGon()
    {        
        super();
    }
    GeoGon(Vector<GeoPoint> V){
        vecPoint=V;
        for(int i=0;i<V.size();i++){
            V.get(i).dependent.add(this);
        }
    }
    void addPoint(GeoPoint p,Pane layout){
        vecPoint.add(p);
        if(lst!=null) lst.erase();
        if(vecPoint.size()>1){
            GeoSegment seg=new GeoSegment();
            seg.name.setText(GraphHome.genName());
            seg.v.add(p);
            seg.v.add(vecPoint.get(vecPoint.size()-2));
            seg.v.get(0).flicker();
            seg.v.get(1).flicker();
            p.dependent.add(seg);
            vecPoint.get(vecPoint.size()-2).dependent.add(seg);
            seg.init();
            seg.startXProperty().bind(vecPoint.get(vecPoint.size()-2).centerXProperty());
            seg.startYProperty().bind(vecPoint.get(vecPoint.size()-2).centerYProperty());
            seg.endXProperty().bind(p.centerXProperty());
            seg.endYProperty().bind(p.centerYProperty());
            seg.addToStack();
            
            layout.getChildren().add(seg);
            
            if(vecSeg.size()>1){
                vecSeg.get(vecSeg.size()-1).erase();
            }
            if(vecSeg.size()>0){
                vecSeg.add(seg);
                seg=new GeoSegment();
                seg.name.setText(GraphHome.genName());
                seg.v.add(p);
                seg.v.add(vecPoint.get(0));
                seg.v.get(0).flicker();
                seg.v.get(1).flicker();
                seg.v.get(0).dependent.add(seg);
                p.dependent.add(seg);
                vecPoint.get(0).dependent.add(seg);
                seg.init();
                seg.startXProperty().bind(vecPoint.get(0).centerXProperty());
                seg.startYProperty().bind(vecPoint.get(0).centerYProperty());
                seg.endXProperty().bind(p.centerXProperty());
                seg.endYProperty().bind(p.centerYProperty());
                layout.getChildren().add(seg);
                vecSeg.add(seg);
            }
            else{
                vecSeg.add(seg);
            }
                
            
            
        }
        if(vecPoint.size()>2){
            GraphHome.currentPoints.clear();
            GraphHome.vSize.set(0);
        }
    }
    static void draw(Pane layout){
        GeoGon poly = new GeoGon();
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(!demo.independent)
                demo.flicker();
        }
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(demo.independent)
                demo.flicker();
        }
        GraphHome.vSize.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    
                if(newValue.intValue()>0){
                    layout.setOnMouseMoved(null);
                    poly.addPoint(GraphHome.clickedPoints.get(GraphHome.clickedPoints.size()-1),layout);
                    GraphHome.vSize.addListener(GraphHome.nothing);
                }
                                       
            }
        });
        layout.setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton()!=MouseButton.SECONDARY)
                {
                    Point p=new Point();
                    p.setX(Math.round(event.getX()/5)*5);
                    p.setY(Math.round(event.getY()/5)*5);
                    GeoPoint C = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
                    C.add(layout);
                    GraphHome.clickedPoints.add(C);
                    GraphHome.vSize.set(GraphHome.vSize.get()+1);                    
                }
        });
        
    }
    
}
