/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

/**
 *
 * @author Administrator
 */
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class GeoLine extends Line{
    Point v=new Point();
    SmallMenu menu=new SmallMenu(0,0);
//    MenuItem perp_through=new MenuItem("Perpendicular Line Through a Point");
    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL];
    public static Map<Integer, GeoCircle> LineMap = new HashMap();
    public static List<Integer> temp = new ArrayList<Integer>();
    public List<Integer> ids = new ArrayList<Integer>();
    public static Integer id =0;
    double c;
    static Point p1=new Point();
    static Point p2=new Point();
    GeoLine(double a,double b,double C)
    {
//        menu.add(perp_through);
        this();
        v.setX(b);
        v.setY(-a);
        c=C*MainMenu.factor;
        calibrate();
        
    }
    GeoLine(Point p,double C)
    {
//        menu.add(perp_through);
        v=p;
        c=C;
        calibrate();
    }
    GeoLine(Point p,Point q)
    {
        p= GeoMetry.sub(q,p);
        v=p;
        c=GeoMetry.cross(p,q);
        calibrate();
    }
    GeoLine() {
        menu.addForLine();
        setStartX(0);
        setStartY(0);
        setEndX(0);
        setEndY(0);
        c=0;
    }
    static void showPoint(Point pp,Pane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
        LineMap.put(id, cc);
        temp.add(id++);

        layout.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    }
    private GeoLine perpThrough(Point p)
    {
        Point q=GeoMetry.add(p,v.perp());
        return new GeoLine(p,q);
    }
   
    Void calibrate()
    {
        this.setStrokeWidth(2);
        this.setStartX(-7500);
        this.setStartX(-10000*v.getX());
        this.setStartY(-10000*v.getY());
        this.setEndX(10000*v.getX());
        this.setEndY(10000*v.getY());
        if(v.getY()!=0)
        {
            this.setTranslateX(-c/v.getY());
        }
        else
        {
            this.setStartY(c);
            this.setEndY(c);
        }
        return null;
    }
    public static void setPoints(GeoLine geo)
    {
        geo.ids.addAll(temp);
        temp.clear();
    }
    void remove()
    {
        this.ids.forEach((Integer x) -> {
            ((Pane)this.getParent()).getChildren().remove((LineMap.get(x)).menu.getLabel());
            ((Pane)this.getParent()).getChildren().remove(LineMap.get(x));
        });
        ((Pane)this.getParent()).getChildren().remove(this.menu.lbl);
        ((Pane)this.getParent()).getChildren().remove(this);
    }
    
    static void draw(Pane layout)
    {
        click=0;
        temp.clear();
        if(MainMenu.move)
            return ;
//        GeoLine l = new GeoLine();
        GeoLine l = new GeoLine();
        layout.getChildren().add(l);
        layout.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event)
            {
                Point p = new Point(event.getX(),event.getY());
                showPoint(p,layout);
                layout.setOnMouseMoved(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event)
                    {
                        Point q = new Point(event.getX(),event.getY());
                        l.v=new GeoLine(p,q).v;
                        l.c=new GeoLine(p,q).c;
                        l.calibrate();
                    }
                });
                layout.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event)
                        {
                            layout.setOnMouseMoved(null);
                            layout.setOnMouseClicked(null);
                            Point q = new Point(event.getX(),event.getY());
                            showPoint(q,layout);
                            l.v=new GeoLine(p,q).v;
                            l.c=new GeoLine(p,q).c;
                            l.calibrate();
                            double mx,my;
                            mx=(p.getX()+q.getX())/2;
                            my=(p.getY()+q.getY())/2;
                            l.menu.set(mx, my);
                            setPoints(l);
                            layout.getChildren().add(l.menu.getLabel());
                            l.calibrate();
                            //Event handler to remove it
                            SmallMenu.menuSet(l);
                            
                            
                            
                        }

                });
            }
            
            
        });
        
    }
    static void drawPerp(Pane layout,GeoLine L)
    {
        GeoLine l=new GeoLine();
        layout.getChildren().add(l);
        layout.setOnMouseMoved((MouseEvent event) -> {
            if(MainMenu.move)
                return ;
            Point p = new Point(event.getX(),event.getY());
            GeoLine temp1 = L.perpThrough(p);
            l.v = temp1.v;
            l.c = temp1.c;
            l.calibrate();
        });
        layout.setOnMouseClicked((MouseEvent event) -> {
            if(MainMenu.move)
                return ;
            layout.setOnMouseMoved(null);
            layout.setOnMouseClicked(null);
            
            Point p = new Point(event.getX(),event.getY());
            GeoLine temp1 = L.perpThrough(p);
            l.c = temp1.c;
            l.v = temp1.v;
            SmallMenu.menuSet(l);
            l.calibrate();
            l.menu.set(event.getX(), event.getY());
            layout.getChildren().add(l.menu.getLabel());
            MainMenu.move=true;
            event.consume();
        });
    }
    public static void DrawFromEquation(Pane layout){
        if(MainMenu.move)
            return ;

        temp.clear();
        Stage window = new Stage();
        window.setTitle("Give input");

        TextField A = new TextField();
        A.setPromptText("Enter A");
        TextField B = new TextField();
        B.setPromptText("Enter B");
        TextField C = new TextField(); 
        C.setPromptText("Enter C");
        Button submit = new Button("Submit");

        GridPane root = new GridPane();
        root.addRow(0,A,B,C);
        root.addRow(1,submit);

        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        //window.showAndWait();
        window.show();
        
        submit.setOnAction((ActionEvent ae) -> {
            window.close();
            String X = A.getText();
            String Y = B.getText();
            String Z = C.getText();
            double a = new Double(X);
            double b = new Double(Y);
            double c1 = new Double(Z);
            GeoLine l = new GeoLine(a, b, c1);
            l.calibrate();
            layout.getChildren().add(l);
            if (b!=0) {
                l.menu.set(0, c1 / b);
            } else {
                l.menu.set(c1, c1);
            }
            layout.getChildren().add(l.menu.lbl);
            setPoints(l);
            SmallMenu.menuSet(l);
        });


    }

    
    
}
