/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import static geocal.GeoSegment.draw;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import static javafx.scene.transform.Rotate.X_AXIS;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class GeoPoint extends Circle{
    Label name=new Label();
    static SortedSet<String> st = new TreeSet<String>();
    static int cur=0;
    IntegerProperty state = new SimpleIntegerProperty();
    Vector dependent = new Vector();
    Vector<GeoPoint> v = new Vector<GeoPoint>();
    double X,Y;
    boolean independent;
    GeoPoint()
    {
        super(0,0,5);
        state.set(0);
        this.setStrokeWidth(0.5);
        this.setStroke(Color.BLACK);
        this.setFill(Color.BLUE);
        name = generate();
        independent=true;
    }
    GeoPoint(double x,double y,DoubleProperty bf)
    {
        super(x,y,5);
        state.set(0);
        this.setStrokeWidth(0.5);
        this.setStroke(Color.BLACK);
        this.setFill(Color.BLUE);
        this.strokeWidthProperty().bind(bf.multiply(0.5));
        this.radiusProperty().bind(bf.multiply(5));
        name=generate();
        name.layoutXProperty().bind(centerXProperty());
        name.layoutYProperty().bind(centerYProperty());
        name.scaleXProperty().bind(bf);
        name.scaleYProperty().bind(bf);
        Rotate flip = new Rotate(180,X_AXIS);
        name.getTransforms().add(flip);
        independent=true;
        calibrate();
        Tooltip tt= new Tooltip("Point");
        Tooltip.install(this, tt);
        tt.setOpacity(0.5);
        this.setOnMouseEntered(GraphHome.cursorHand);
        this.setOnMouseExited(GraphHome.cursorDefault);
        this.setOnMousePressed((MouseEvent ev)->{
            GraphHome.gPaper.getScene().setCursor(Cursor.CLOSED_HAND);
            this.setOnMouseEntered(null);
            this.setOnMouseExited(null);
        });
        
        this.setOnMouseReleased((MouseEvent ev)->{
            GraphHome.gPaper.getScene().setCursor(Cursor.HAND);
            this.setOnMouseEntered(GraphHome.cursorHand);
            this.setOnMouseExited(GraphHome.cursorDefault);
        });
        
        this.setOnMouseDragged((MouseEvent ev)->{
            ev.consume();
            GraphHome.gPaper.getScene().setCursor(Cursor.CLOSED_HAND);
            if(independent)
            drag(ev);
            calibrate();
        });
    }
    GeoPoint(double x,double y,DoubleProperty bf,boolean flag)
    {
        super(x,y,5);
        state.set(0);
        this.setStrokeWidth(0.5);
        this.setStroke(Color.BLACK);
        this.setFill(Color.BLUE);
        this.strokeWidthProperty().bind(bf.multiply(0.5));
        this.radiusProperty().bind(bf.multiply(5));
        name=generate();
        name.layoutXProperty().bind(centerXProperty());
        name.layoutYProperty().bind(centerYProperty());
        name.scaleXProperty().bind(bf);
        name.scaleYProperty().bind(bf);
        Rotate flip = new Rotate(180,X_AXIS);
        name.getTransforms().add(flip);
        independent=flag;
        calibrate();
        Tooltip tt= new Tooltip("Point");
        Tooltip.install(this, tt);
        tt.setOpacity(0.5);
        this.setOnMouseEntered(GraphHome.cursorHand);
        this.setOnMouseExited(GraphHome.cursorDefault);
        this.setOnMousePressed((MouseEvent ev)->{
            GraphHome.gPaper.getScene().setCursor(Cursor.CLOSED_HAND);
            this.setOnMouseEntered(null);
            this.setOnMouseExited(null);
        });
        this.setOnMouseReleased((MouseEvent ev)->{
            GraphHome.gPaper.getScene().setCursor(Cursor.HAND);
            this.setOnMouseEntered(GraphHome.cursorHand);
            this.setOnMouseExited(GraphHome.cursorDefault);
        });
        this.setOnMouseDragged((MouseEvent ev)->{
            ev.consume();
            this.setOnMouseReleased((MouseEvent event)->{
            calibrate();
            event.consume();
            this.setOnMouseReleased(null);
            });
            if(independent)
            drag(ev);
            calibrate();
        });
    }
    
    void calibrate()
    {
        X=getCenterX()/GraphHome.gPaper.pUnit.get();
        Y=getCenterY()/GraphHome.gPaper.pUnit.get();
        if(independent)
        {
            this.centerXProperty().bind(GraphHome.gPaper.pUnit.multiply(X));
            this.centerYProperty().bind(GraphHome.gPaper.pUnit.multiply(Y));
        }
    }
    void drag(MouseEvent ev)
    {
        this.centerXProperty().unbind();
        this.centerYProperty().unbind();
        try{
            this.setCenterX(ev.getX());
            this.setCenterY(ev.getY());
            GraphHome.showCor(ev.getX(), ev.getY());
        }
        catch(Exception e){
            System.out.println("couldn't drag point");
        }
    }
    public static void draw(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
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
                    C.calibrate();
                    layout.setOnMouseClicked(null);
                 
                    if(GraphHome.oneTouch.isSelected())
                        draw(layout);
                }
        });
    }
    public static void drawMid(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);

        GraphHome.vSize.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.equals(2))
                {
                    GraphHome.temporary.clear();
                    layout.setOnMouseMoved(null);
                    GeoPoint C = new GeoPoint((GraphHome.clickedPoints.get(0).getCenterX()+GraphHome.clickedPoints.get(1).getCenterX())/2,(GraphHome.clickedPoints.get(0).getCenterY()+GraphHome.clickedPoints.get(1).getCenterY())/2,GraphHome.gPaper.BalanceFactor,false);
                    C.add(layout);
                    C.v.addAll(GraphHome.clickedPoints);
                    C.v.get(0).dependent.add(C);
                    C.v.get(1).dependent.add(C);
                    C.v.get(0).flicker();
                    C.v.get(1).flicker();
                    ChangeListener change = new ChangeListener<Number>(){
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                C.setCenterX((C.v.get(0).getCenterX()+C.v.get(1).getCenterX())/2);
                                C.setCenterY((C.v.get(0).getCenterY()+C.v.get(1).getCenterY())/2);
                            }
                    };
                    C.v.get(0).centerXProperty().addListener(change);
                    C.v.get(0).centerYProperty().addListener(change);
                    C.v.get(1).centerXProperty().addListener(change);
                    C.v.get(1).centerYProperty().addListener(change);
                    layout.setOnMouseClicked(null);
                    GraphHome.buttonPressed();
                    if(GraphHome.oneTouch.isSelected())
                        drawMid(layout);                    
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
                    GraphHome.temporary.add(C);
                    GraphHome.clickedPoints.add(C);
                    GraphHome.vSize.set(GraphHome.vSize.get()+1);
                    
                }
        });
        
        
    }
    public static void drawProj(Pane layout)
    {
        System.out.println("dhukche");
        GraphHome.clickedLines.clear();
        GraphHome.lSize.set(0);
        GraphHome.lineAllowed=true;
        GraphHome.lSize.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("line clicked");
                GraphHome.lineAllowed=false;
                GraphHome.vSize.set(0);
                GraphHome.clickedPoints.clear();
                Line l;
                
                if(GraphHome.clickedLines.get(0) instanceof GeoLine)
                {
                    
                    l=new Line (((GeoLine) GraphHome.clickedLines.get(0)).getStartX(),((GeoLine) GraphHome.clickedLines.get(0)).getStartY(),((GeoLine) GraphHome.clickedLines.get(0)).getEndX(),((GeoLine) GraphHome.clickedLines.get(0)).getEndY());
                    layout.getChildren().add(l);
                    ((GeoLine) GraphHome.clickedLines.get(0)).flicker();
                    l.startXProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).startXProperty());
                    l.startYProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).startYProperty());
                    l.endXProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).endXProperty());
                    l.endYProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).endYProperty());
                    
                }
                else
                {
                    l=new Line (((GeoSegment) GraphHome.clickedLines.get(0)).getStartX(),((GeoSegment) GraphHome.clickedLines.get(0)).getStartY(),((GeoSegment) GraphHome.clickedLines.get(0)).getEndX(),((GeoSegment) GraphHome.clickedLines.get(0)).getEndY());
                    layout.getChildren().add(l);
                    ((GeoSegment) GraphHome.clickedLines.get(0)).flicker();
                    l.startXProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).startXProperty());
                    l.startYProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).startYProperty());
                    l.endXProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).endXProperty());
                    l.endYProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).endYProperty());
                }
                l.strokeWidthProperty().bind(GraphHome.gPaper.BalanceFactor.multiply(10));
                l.setStroke(Color.BLUE);
                l.setOpacity(0.4);
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
                    public void changed(ObservableValue<? extends Number> observ, Number oV, Number nV) {
                        if(nV.equals(1))
                        {
                            System.out.println("point clicked");
                            GraphHome.temporary.clear();
                            GraphHome.temporary.add(l);
                            if(GraphHome.clickedLines.get(0) instanceof GeoLine)
                            {
                                System.out.println("Line");
                                GeoLine x = (GeoLine) GraphHome.clickedLines.get(0);
                                
                                Point p = new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY());
                                Point q = x.project(p);
                                GeoPoint C = new GeoPoint(q.getX(),q.getY(),GraphHome.gPaper.BalanceFactor,false);
                                C.add(layout);
                                C.v.add(GraphHome.clickedPoints.get(0));
                                ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                   try{
                                       if(x.getStartX()==x.getEndX()&&x.getStartY()==x.getEndY())
                                       {
                                           return ;
                                       }
                                       Point r = x.project(new Point(C.v.get(0).getCenterX(),C.v.get(0).getCenterY()));
                                        C.setCenterX(r.getX());
                                        C.setCenterY(r.getY());
                                   }
                                   catch(Exception e)
                                   {
                                       System.out.println(e);
                                   }
                                }
                                };
                                C.v.get(0).flicker();
                                C.v.get(0).dependent.add(C);
                                x.dependent.add(C);
                                x.dependent.add(C);
                                x.startXProperty().addListener(change);
                                x.startYProperty().addListener(change);
                                x.endXProperty().addListener(change);
                                x.endYProperty().addListener(change);
                                C.v.get(0).centerXProperty().addListener(change);
                                C.v.get(0).centerYProperty().addListener(change);
                            }
                            else if(GraphHome.clickedLines.get(0) instanceof GeoSegment)
                            {
                                System.out.println("Segment");
                                GeoSegment x = (GeoSegment) GraphHome.clickedLines.get(0);
                                
                                Point p = new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY());
                                Point q = x.project(p);
                                GeoPoint C = new GeoPoint(q.getX(),q.getY(),GraphHome.gPaper.BalanceFactor,false);
                                C.add(layout);
                                C.v.add(GraphHome.clickedPoints.get(0));
                                ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    try{
                                        if(x.getStartX()==x.getEndX()&&x.getStartY()==x.getEndY())
                                       {
                                           return ;
                                       }
                                        Point r = x.project(new Point(C.v.get(0).getCenterX(),C.v.get(0).getCenterY()));
                                        C.setCenterX(r.getX());
                                        C.setCenterY(r.getY());
                                    }
                                    catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                }
                                };
                                C.v.get(0).flicker();
                                C.v.get(0).dependent.add(C);
                                x.dependent.add(C);
                                x.startXProperty().addListener(change);
                                x.startYProperty().addListener(change);
                                x.endXProperty().addListener(change);
                                x.endYProperty().addListener(change);
                                C.v.get(0).centerXProperty().addListener(change);
                                C.v.get(0).centerYProperty().addListener(change);
                            }
                            GraphHome.buttonPressed();
                            if(GraphHome.oneTouch.isSelected())
                                drawProj(layout);
                        }
                    }
                    
                });
                layout.setOnMouseClicked((MouseEvent event) -> {
                    if(event.getButton()!=MouseButton.SECONDARY)
                    {

                        Point p=new Point();
                        p.setX(Math.round(event.getX()));
                        p.setY(Math.round(event.getY()));
                        GeoPoint C = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
                        GraphHome.temporary.add(C);
                        C.add(layout);
                        GraphHome.clickedPoints.add(C);
                        GraphHome.vSize.set(GraphHome.vSize.get()+1);
                        
                    }
                });
            }
        });
        
        
        
    }
    public static void drawRefl(Pane layout)
    {
        GraphHome.clickedLines.clear();
        GraphHome.lSize.set(0);
        GraphHome.lineAllowed=true;
        GraphHome.lSize.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GraphHome.lineAllowed=false;
                GraphHome.vSize.set(0);
                GraphHome.clickedPoints.clear();
                Line l;
                if(GraphHome.clickedLines.get(0) instanceof GeoLine)
                {
                    
                    l=new Line (((GeoLine) GraphHome.clickedLines.get(0)).getStartX(),((GeoLine) GraphHome.clickedLines.get(0)).getStartY(),((GeoLine) GraphHome.clickedLines.get(0)).getEndX(),((GeoLine) GraphHome.clickedLines.get(0)).getEndY());
                    layout.getChildren().add(l);
                    ((GeoLine) GraphHome.clickedLines.get(0)).flicker();
                    l.startXProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).startXProperty());
                    l.startYProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).startYProperty());
                    l.endXProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).endXProperty());
                    l.endYProperty().bind(((GeoLine) GraphHome.clickedLines.get(0)).endYProperty());
                }
                else
                {
                   
                    l=new Line (((GeoSegment) GraphHome.clickedLines.get(0)).getStartX(),((GeoSegment) GraphHome.clickedLines.get(0)).getStartY(),((GeoSegment) GraphHome.clickedLines.get(0)).getEndX(),((GeoSegment) GraphHome.clickedLines.get(0)).getEndY());
                    layout.getChildren().add(l);
                    ((GeoSegment) GraphHome.clickedLines.get(0)).flicker();
                    l.startXProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).startXProperty());
                    l.startYProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).startYProperty());
                    l.endXProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).endXProperty());
                    l.endYProperty().bind(((GeoSegment) GraphHome.clickedLines.get(0)).endYProperty());
                }
                l.strokeWidthProperty().bind(GraphHome.gPaper.BalanceFactor.multiply(10));
                l.setStroke(Color.BLUE);
                l.setOpacity(0.4);
                for(GeoPoint demo: GraphHome.currentPoints)
                {
                    demo.flicker();
                }
                
                GraphHome.vSize.addListener(new ChangeListener<Number>(){
                    @Override
                    public void changed(ObservableValue<? extends Number> observ, Number oV, Number nV) {
                        if(nV.equals(1))
                        {
                            GraphHome.temporary.clear();
                            GraphHome.temporary.add(l);
                            System.out.println("point clicked");
                            
                            if(GraphHome.clickedLines.get(0) instanceof GeoLine)
                            {
                                System.out.println("Line");
                                GeoLine x = (GeoLine) GraphHome.clickedLines.get(0);
                                
                                Point p = new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY());
                                Point q = x.reflection(p);
                                GeoPoint C = new GeoPoint(q.getX(),q.getY(),GraphHome.gPaper.BalanceFactor,false);
                                C.add(layout);
                                C.v.add(GraphHome.clickedPoints.get(0));
                                C.addToStack();
                                ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    try{
                                        if(x.getStartX()==x.getEndX()&&x.getStartY()==x.getEndY())
                                       {
                                           return ;
                                       }
                                        Point r = x.reflection(new Point(C.v.get(0).getCenterX(),C.v.get(0).getCenterY()));
                                        C.setCenterX(r.getX());
                                        C.setCenterY(r.getY());
                                    }
                                    catch (Exception e){
                                        System.out.println(e);
                                    }
                                }
                                };
                                C.v.get(0).flicker();
                                C.v.get(0).dependent.add(C);
                                x.dependent.add(C);
                                x.startXProperty().addListener(change);
                                x.startYProperty().addListener(change);
                                x.endXProperty().addListener(change);
                                x.endYProperty().addListener(change);
                                C.v.get(0).centerXProperty().addListener(change);
                                C.v.get(0).centerYProperty().addListener(change);
                            }
                            else if(GraphHome.clickedLines.get(0) instanceof GeoSegment)
                            {
                                System.out.println("Segment");
                                GeoSegment x = (GeoSegment) GraphHome.clickedLines.get(0);
                                
                                Point p = new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY());
                                Point q = x.reflection(p);
                                GeoPoint C = new GeoPoint(q.getX(),q.getY(),GraphHome.gPaper.BalanceFactor,false);
                                C.add(layout);
                                C.v.add(GraphHome.clickedPoints.get(0));
                                C.addToStack();
                                ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    try{
                                        if(x.getStartX()==x.getEndX()&&x.getStartY()==x.getEndY())
                                       {
                                           return ;
                                       }
                                        Point r = x.reflection(new Point(C.v.get(0).getCenterX()+C.v.get(0).getTranslateX(),C.v.get(0).getCenterY()+C.v.get(0).getTranslateY()));
                                        C.setCenterX(r.getX());
                                        C.setCenterY(r.getY());
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                }
                                };
//                                ChangeListener chng = new ChangeListener<Number>(){
//                                @Override
//                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                                    try{
//                                        System.out.println("wtf");
//                                        C.v.get(0).setCenterX(C.v.get(0).getCenterX()+newValue.doubleValue());
//                                        C.v.get(0).setCenterY(C.v.get(0).getCenterY()+newValue.doubleValue());
//                                        C.v.get(0).setTranslateX(0);
//                                        C.v.get(0).setTranslateY(0);
//                                    }
//                                    catch (Exception e)
//                                    {
//                                        System.out.println(e);
//                                    }
//                                }
//                                };
                                C.v.get(0).flicker();
                                C.v.get(0).dependent.add(C);
                                x.dependent.add(C);
                                x.startXProperty().addListener(change);
                                x.startYProperty().addListener(change);
                                x.endXProperty().addListener(change);
                                x.endYProperty().addListener(change);
                                C.v.get(0).centerXProperty().addListener(change);
                                C.v.get(0).centerYProperty().addListener(change);
                                C.v.get(0).translateXProperty().addListener(change);
                                C.v.get(0).translateYProperty().addListener(change);
                                
                            }
                            
                            GraphHome.buttonPressed();
                            if(GraphHome.oneTouch.isSelected())
                                drawRefl(layout);
                        }
                    }
                    
                });
                layout.setOnMouseClicked((MouseEvent event) -> {
                    if(event.getButton()!=MouseButton.SECONDARY)
                    {

                        Point p=new Point();
                        p.setX(Math.round(event.getX()));
                        p.setY(Math.round(event.getY()));
                        GeoPoint C = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
                        GraphHome.temporary.add(C);
                        C.add(layout);
                        GraphHome.clickedPoints.add(C);
                        GraphHome.vSize.set(GraphHome.vSize.get()+1);
                       
                    }
                });
            }
        });
        
        
        
    }
    public static void draw_pivot(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
        GeoSegment seg = new GeoSegment();
        layout.getChildren().add(seg);
        GraphHome.temporary.add(seg);
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
                if(newValue.equals(2))
                {
                    GraphHome.temporary.clear();
                    
                    if(GraphHome.clickedPoints.get(0)==GraphHome.clickedPoints.get(1))
                    {
                        GraphHome.clickedPoints.remove(1);
                        GraphHome.vSize.set(GraphHome.vSize.get()-1);
                    }
                    else
                    {
                        GeoPoint p = GraphHome.clickedPoints.get(1);
                        layout.setOnMouseMoved(null);
                        GraphHome.clickedPoints.get(0).centerXProperty().addListener(new ChangeListener<Number> (){
                            @Override
                            public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue){
                                p.centerXProperty().unbind();
                                p.setCenterX(p.getCenterX()-oldValue.doubleValue()+newValue.doubleValue());
                            }
                            
                        });
                        GraphHome.clickedPoints.get(0).centerYProperty().addListener(new ChangeListener<Number> (){
                            @Override
                            public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue){
                                p.centerYProperty().unbind();
                                p.setCenterY(p.getCenterY()-oldValue.doubleValue()+newValue.doubleValue());
                            }
                            
                            
                        });
                        GraphHome.temporary.clear();
                        GraphHome.clickedPoints.clear();
                        GraphHome.buttonPressed();
                    }
                    
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
                    GraphHome.temporary.add(C);
                    GraphHome.vSize.set(GraphHome.vSize.get()+1);
                    seg.dependent.add(C);
                    
                    
                }
        });
    }
    void showDetails()
    {
        GraphHome.detailsBar.getChildren().clear();
        VBox par = new VBox(10);
        par.setPadding(new Insets(20,2,2,2));
        par.getStyleClass().add("details");
        Label type = new Label("Type : Point");
        Label naMe = new Label("Name : ");
        Label ind = new Label("Independent : ");
        if(independent)
            ind.setText(ind.getText()+"YES");
        else
            ind.setText(ind.getText()+"NO");
        naMe.setText(naMe.getText()+name.getText());
        Label center = new Label("Cordinate : "+String.valueOf(X)+" "+String.valueOf(Y));
        type.prefWidthProperty().bind(par.widthProperty().subtract(5));
        naMe.prefWidthProperty().bind(par.widthProperty().subtract(5));
        center.prefWidthProperty().bind(par.widthProperty().subtract(5));
        ind.prefWidthProperty().bind(par.widthProperty().subtract(5));
        type.getStyleClass().add("dText");
        naMe.getStyleClass().add("dText");
        center.getStyleClass().add("dText");
        ind.getStyleClass().add("dText");
        
        
        par.getChildren().addAll(type,naMe,ind,center);
        GraphHome.detailsBar.getChildren().add(par);
        
    }
    void addToStack()
    {
        HBox hb = new HBox(10);
        dependent.add(hb);
        hb.setPrefWidth(190);
        hb.setPadding(new Insets(3));
        ColorPicker cp = new ColorPicker();
        Label lbl = new Label();
        lbl.textProperty().bind(name.textProperty());
        hb.getChildren().addAll(lbl,cp);
        hb.getStyleClass().add("stklist");
        GraphHome.stkPt.getChildren().add(hb);
        ContextMenu cMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Remove");
        MenuItem reName = new MenuItem("Rename");
        Image img = new Image("img/context_clear.jpg");
        delete.setGraphic(new ImageView(img));
        img = new Image("img/context_rename.png");
        reName.setGraphic(new ImageView(img));
        delete.getStyleClass().add("cMenu");
        reName.getStyleClass().add("cMenu");
        cp.getCustomColors().add(Color.RED);
        cp.getCustomColors().add(Color.BLUE);
        cp.getCustomColors().add(Color.GREEN);
        cp.getCustomColors().add(Color.rgb(0, 255, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 0, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 255, 0, 1));
        if(independent)
        cp.setValue(Color.BLUE);
        else
            cp.setValue(Color.RED);
        cp.setPrefWidth(40);
        lbl.setPrefWidth(150);
        this.fillProperty().bind(cp.valueProperty());
        cMenu.getItems().addAll(reName,delete);
        
        this.setOnMouseClicked((MouseEvent ev)->{
            if(ev.getButton()==MouseButton.PRIMARY)
            {
                GraphHome.clickedPoints.add(this);
                GraphHome.vSize.unbind();
                GraphHome.vSize.set(GraphHome.vSize.get()+1);
                ev.consume();
            }
            else
            {
                GraphHome.contextCor.setLayoutX(ev.getX());
                GraphHome.contextCor.setLayoutY(ev.getY());
                cMenu.show(GraphHome.contextCor,Side.BOTTOM,0,0);
            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                remove();
            }
        });
        reName.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                inputName();
            }
        });

        GraphHome.currentPoints.add(this);
//        
        
    }
//    void Animate()
//    {
//        GeoAnimator.animate(this);
//    }
    public static Label generate()
    {
        String s;
        if(st.isEmpty())
        {
            s=String.valueOf((char)('A'+cur%26));
            if((cur-cur%26)!=0)
                s=s+String.valueOf((cur-cur%26)/26);
            cur++;
        }
        else
        {
            s=st.first();
            st.remove(s);
        }
        return new Label(s);
    }
    void remove()
    {
        st.add(this.name.getText());
        Pane par = (Pane) this.getParent();
        if(par==null) return ;
        par.getChildren().remove(this);
        par.getChildren().remove(name);
        for(int i=0;i<dependent.size();i++)
        {
            if(dependent.get(i) instanceof GeoCircle)
                ((GeoCircle)(dependent.get(i))).erase();
            else if(dependent.get(i) instanceof GeoSegment)
                ((GeoSegment)(dependent.get(i))).erase();
            else if(dependent.get(i) instanceof GeoLine)
                ((GeoLine)(dependent.get(i))).erase();
            else if(dependent.get(i) instanceof HBox)
            {
                VBox ancestor=(VBox) ((HBox)(dependent.get(i))).getParent();
                if(ancestor!=null)
                GraphHome.stkPt.getChildren().remove(dependent.get(i));
            }
            else if(dependent.get(i) instanceof GeoPoint)
            {
                ((GeoPoint)(dependent.get(i))).remove();
            }
            else if(dependent.get(i) instanceof GeoRay)
            {
                ((GeoRay)(dependent.get(i))).erase();
            }
        }
        GraphHome.currentPoints.remove(this);
    }
    void flicker()
    {
        Pane par = (Pane) this.getParent();
        if(par==null) return ;
        par.getChildren().remove(this);
        par.getChildren().add(this);
    }
    void add(Pane layout)
    {
        Pane par = (Pane) name.getParent();
        if(par!=null) return ;
        layout.getChildren().add(this);
        layout.getChildren().add(name);
        addToStack();
    }
    void hideLbl()
    {
        Pane par = (Pane) name.getParent();
        if(par==null) return ;
        par.getChildren().remove(name);
    }
    void showLbl()
    {
        Pane par = (Pane) name.getParent();
        if(par!=null) return ;
        par = (Pane) name.getParent();
        if(par==null) return;
        par.getChildren().add(name);
    }
    public void inputName()
    {
        Stage window = new Stage();
        window.setTitle("Rename");
        TextField x = new TextField();
        GridPane root = new GridPane();
        Button submit = new Button("Submit");
        root.addRow(0,x);
        root.addRow(1,submit);
        Scene scene = new Scene(root,200,60);
        window.setScene(scene);
        window.show();
        
        submit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                if(x.getText().isEmpty()||x.getText().length()>20)
                {
                    System.out.println("");
                    Alert.display("Error...!","Name Must be between 1 to 20 charecters");
                }
                else
                {
                    String s = x.getText();
                    name.setText(s);
                    window.close();
                }
            }
        });
    }
}
