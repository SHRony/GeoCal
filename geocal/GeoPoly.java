/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Lenovo
 */
public class GeoPoly {
    
    Polygon poly,hull;
    boolean isHullReady=false,isHullVisible=false;
    ObservableList<Double> points;
    Vector<Point> vec=new Vector<Point>();
    public static Map<Integer, GeoCircle> PolyMap = new HashMap();
    public static List<Integer> temp = new ArrayList<Integer>();
    public List<Integer> ids = new ArrayList<Integer>();
    public static Integer id =0;
    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL];
    private static String input="";
    private static Stage window = new Stage();
    private static Label txt = new Label();
    SmallMenu menu;
    private int vertices;
    private double length;
    private GeoPane layout;
    
    GeoPoly()
    {
        poly = new Polygon();
        hull = new Polygon();
        poly.setFill(Color.TRANSPARENT);
        poly.setStroke(Color.BLACK);
        poly.setStrokeWidth(1.5);
        points = poly.getPoints();
        menu = new SmallMenu();
        menu.addForPolygon();
    }
    public void showHull(Pane layout)
    {
        if(this.isHullReady)
            ;
        else
        {
            this.isHullReady=true;
            this.hull=GeoMetry.ConvexHull(this.vec);
        }
        if(!this.isHullVisible)
        {
            this.isHullVisible=true;
            layout.getChildren().add(this.hull);
        }
    }
    public void hideHull(Pane layout)
    {
        if(this.isHullVisible)
        {
            this.isHullVisible=false;
            layout.getChildren().remove(this.hull);
        }
    }
    private Point center()
    {
        int i = poly.getPoints().size()/2;
        
        if(i%2==1 && i>1)
        {
            i--;
        }
        Point a = new Point(poly.getPoints().get(0), poly.getPoints().get(1));
        Point b = new Point(poly.getPoints().get(i), poly.getPoints().get(i-1));
        return GeoMetry.mid(a ,b);
    }
    
    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
    
    public GeoPane getLayout() {
        return layout;
    }
    
    public void setLayout(GeoPane layout) {
        this.layout = layout;
    }
    
    
     static {
        for(int i=0; i<TOTAL; i++)
        {
            point[i] = new Point();
        }    
        Take_Input();
    }
    public static void setCircle(GeoPoly geo)
    {
        geo.ids.addAll(temp);
        temp.clear();
    }
    static void showPoint(Point pp, GeoPane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
        PolyMap.put(id, cc);
        temp.add(id++);
        layout.chld.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    } 
    
    public static void Delete(GeoPoly p, Boolean all)
    {
        //p.hideHull(((Pane)p.poly.getParent()));
        p.hideHull(p.layout.chld);
        p.ids.forEach((Integer x) -> {
            ((Pane)p.poly.getParent()).getChildren().remove((PolyMap.get(x)).menu.getLabel());
            ((Pane)p.poly.getParent()).getChildren().remove(PolyMap.get(x));
        });
                
        if(all) ((Pane)p.poly.getParent()).getChildren().remove(p.menu.getLabel());
        ((Pane)p.poly.getParent()).getChildren().remove(p.poly);
        p.points.clear();
        p.ids.clear();
        p.vec.clear();
    }
    
    public static void DrawPoly(GeoPane layout)
    {
        temp.clear();
        GeoPoly p = new GeoPoly();
        layout.chld.getChildren().addAll(p.menu.getLabel());
        
        //Event handler to remove it
        SmallMenu.menuSet(p);
        
        List<Double> newPoints = new ArrayList<>();
        
        //selecting points
        layout.chld.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(MainMenu.move)
                    return ;
                if(event.getButton()!=MouseButton.SECONDARY) 
                {
                    newPoints.clear();
                    newPoints.addAll(p.points);
                    Point pp = new Point(event.getX(), event.getY());
                    p.vec.add(pp);
                    p.isHullReady=false;
                    if(p.isHullVisible)
                        layout.chld.getChildren().remove(p.hull);
                    p.isHullVisible=false;
                    newPoints.add(pp.getX());
                    newPoints.add(pp.getY());
                    showPoint(pp, layout);
                    layout.chld.getChildren().remove(p.poly);
                    p.points.clear();
                    p.points.addAll(newPoints);
                    layout.chld.getChildren().addAll(p.poly);
                   
                    setCircle(p);
                    temp.clear();
                    pp = p.center();
                    p.menu.set(pp.getX(), pp.getY());
                    p.menu.editLength.setDisable(true);
                    p.menu.editVertices.setDisable(true);
                    p.setLayout(layout);

                }
            }
        });
    }
    
    public static void RegularPolygon(GeoPane layout)
    {
        temp.clear();
        click = 0;
        
        layout.chld.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(MainMenu.move)
                    return ;
                if(event.getButton()!=MouseButton.SECONDARY) 
                {
                    click++;
                    if(click%2==1)
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        showPoint(point[click], layout);
                    }
                    else
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        showPoint(point[click], layout);
                        
                        
                        //take input
                        txt.setText("Enter Number of Vertices");
                        window.showAndWait(); //wait untill give input!
                        int n = new Integer(input);
                        input="";
                        
                        GeoPoly p = new GeoPoly();
                        p.menu.showhull.setDisable(true);
                        p.menu.hidehull.setDisable(true);
                        p.points.addAll(point[click-1].getX(), point[click-1].getY(), point[click].getX(), point[click].getY());
                        Point a = point[click-1], b = point[click];
                        p.setVertices(n);
                        //p.setLength(GeoMetry.distance(a, b)/MainMenu.factor);
                        p.setLength(GeoMetry.distance(a, b));
                        p.setLayout(layout);
                        System.out.println("Saved: "+p.getVertices()+","+p.getLength());
                        
                        double angle = (n-2)*Math.PI/n; //external angle
                        
                        //draw n-2 points
                        for(int i=2; i<n; i++)
                        {
                            a = GeoMetry.rotation(b, a, -1.0*angle);
                            p.points.addAll(a.getX(), a.getY());
                            showPoint(a, layout);
                            //swap
                            Point tt = b;
                            b = a;
                            a = tt;
                        }
                        layout.chld.getChildren().addAll(p.poly, p.menu.getLabel());
                        setCircle(p);
                        a = p.center();
                        p.menu.set(a.getX(), a.getY());
                        
                        //Event handler to remove it
                        SmallMenu.menuSet(p);
                    }
                }
            }
        });
    }
    
    /**
     * Take new length of Regular Polygon
     * @param p
     */
    public static void EditLength(GeoPoly p)
    {
        temp.clear();
        txt.setText("Enter New Length of side of regular polygon");
        window.showAndWait();
        double h = (new Double(input))*MainMenu.factor;
        input="";
        System.out.println("Old Length: "+p.getLength()+", new set "+h);
        System.out.println("Vertices "+p.getVertices());
        GeoPane layout = p.getLayout();
        p.setLength(h);
        
        //1st point unchanged
        Point a = new Point(p.points.get(0), p.points.get(1));
        showPoint(a, layout);
        
        //old 2nd point
        Point b = new Point(p.points.get(2), p.points.get(3));
        
        //new 2nd
        b = GeoMetry.ab_to_d(a, b, h);
        showPoint(b, layout);
        
        // remove old points
        Delete(p, true);
        p.points.clear();
        
        p.points.addAll(a.getX(), a.getY(), b.getX(), b.getY());
        
        int n = p.getVertices();
        double angle = (n-2)*Math.PI/n; //external angle
                        
        //draw n-2 points
        for(int i=2; i<n; i++)
        {
            a = GeoMetry.rotation(b, a, -1.0*angle);
            p.points.addAll(a.getX(), a.getY());
            showPoint(a, p.getLayout());
                            
            //swap
            Point tt = b;
            b = a;
            a = tt;
        }
        setCircle(p);
        a = p.center();
        p.menu.set(a.getX(), a.getY());
        layout.chld.getChildren().addAll(p.poly, p.menu.getLabel());
    }
    
    /**
     * Take new no of Vertices of Regular Polygon
     * @param p
     */
    public static void EditVertices(GeoPoly p)
    {
        temp.clear();
        txt.setText("Enter New no of Vertices of regular polygon");
        window.showAndWait();
        //double h = (new Double(input))*MainMenu.factor;
        int n = new Integer(input);
        System.out.println("Old Vertices: "+p.getVertices()+", new set "+n);
        System.out.println("Length: "+p.getLength());
        p.setVertices(n);
        GeoPane layout = p.getLayout();
        
        //1st, 2nd point unchanged
        Point a = new Point(p.points.get(0), p.points.get(1));
        showPoint(a, layout);
        Point b = new Point(p.points.get(2), p.points.get(3));
        showPoint(b, layout);
        
        // remove old points
        Delete(p, true);
        p.points.clear();
        
        p.points.addAll(a.getX(), a.getY(), b.getX(), b.getY());
        
        double h = p.getLength();
        double angle = (n-2)*Math.PI/n; //external angle
                        
        //draw n-2 points
        for(int i=2; i<n; i++)
        {
            a = GeoMetry.rotation(b, a, -1.0*angle);
            p.points.addAll(a.getX(), a.getY());
            showPoint(a, p.getLayout());
                            
            //swap
            Point tt = b;
            b = a;
            a = tt;
        }
        setCircle(p);
        a = p.center();
        p.menu.set(a.getX(), a.getY());
        layout.chld.getChildren().addAll(p.poly, p.menu.getLabel());
    }

    static void Take_Input()
    {
        window.initStyle(StageStyle.UNDECORATED);
        TextField height = new TextField();
        Button submit = new Button("Submit");

        GridPane root = new GridPane();
        root.addRow(0,txt);
        root.addRow(1,height);
        root.addRow(2,submit);

        Scene scene = new Scene(root, 300,100);
        window.setScene(scene);
        
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                input=height.getText();
                if(input==null || input.isEmpty())
                {
                    Alert.display("Error..!", "Please "+txt.getText());
                }
                else
                {   
                    window.close();
                }
            }
        });
        
    }
    
}
