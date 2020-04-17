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
import static geocal.GeoCircle.click;
import static geocal.GeoCircle.point;
import static geocal.GeoCircle.showPoint;
import static geocal.GeoCircle.temp;
import static geocal.Triangle.TriangleMap;
import static geocal.Triangle.id;
import static geocal.Triangle.temp;
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
    public static Map<Integer, GeoCircle> TriangleMap = new HashMap();
    public static List<Integer> temp = new ArrayList<Integer>();
    public List<Integer> ids = new ArrayList<Integer>();
    public static Integer id =0;
    double c;
    static Point p1=new Point();
    static Point p2=new Point();
    GeoLine(double a,double b,double C)
    {
//        menu.add(perp_through);
        v.setX(b);
        v.setY(-a);
        c=C*MainMenu.factor;
        menu.addForLine();
        calibrate();
        
    }
    GeoLine(Point p,double C)
    {
//        menu.add(perp_through);
        v=p;
        c=C;
        menu.addForLine();
        calibrate();
    }
    GeoLine(Point p,Point q)
    {
        System.out.println(p.getX()+" "+p.getY());
        System.out.println(q.getX()+" "+q.getY());
        p= GeoMetry.sub(q,p);
        v=p;
        c=GeoMetry.cross(p,q);
        menu.addForLine();
        calibrate();
    }
    GeoLine() {
        c=0;
    }
    static void showPoint(Point pp,Pane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
        TriangleMap.put(id, cc);
        temp.add(id++);

        layout.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    }
    public GeoLine perpThrough(Point p)
    {
        Point q=GeoMetry.add(p,v.perp());
        System.out.println(p.getX()+" "+q.getX()+" "+v.perp().getX());
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
            ((Pane)this.getParent()).getChildren().remove((TriangleMap.get(x)).menu.getLabel());
            ((Pane)this.getParent()).getChildren().remove(TriangleMap.get(x));
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
        layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            
            public void handle(MouseEvent event) {
                if(MainMenu.move)
                    return ;
                GeoCircle cl=new GeoCircle();
                if(event.getButton()!=MouseButton.SECONDARY) 
                {
                    click++;
                    if(click%2==1)
                    {
                        point[click]=new Point(event.getX(),event.getY());
                        showPoint(point[click],layout);
//                        GeoCircle.showPoint(pp, (GeoPane) layout.getParent(),true);
                    }
                    else
                    {
                        point[click]=new Point(event.getX(),event.getY());
                        showPoint(point[click],layout);
                        double mx,my;
                        mx=(point[click].getX()+point[click-1].getX())/2;
                        my=(point[click].getY()+point[click-1].getY())/2;
                        //Draw Line                        
                        GeoLine l = new GeoLine(point[click],point[click-1]); 
                        l.menu.set(mx, my);
                        setPoints(l);
                        layout.getChildren().add(l.menu.getLabel());
                        l.calibrate();
                        
                        layout.getChildren().add(l);
                        //Event handler to remove it
                        SmallMenu.menuSet(l);
                       
                    }
                }
                event.consume();
                return ;
            }
            });
        return ;
    }
    static void drawPerp(Pane layout,GeoLine L)
    {
        layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            
            public void handle(MouseEvent event) {
                if(MainMenu.move)
                    return ;
                System.out.println("CHaal");
                Point p = new Point(event.getX(),event.getY());
                GeoLine l= L.perpThrough(p);
                SmallMenu.menuSet(l);
                l.calibrate();
                l.menu.set(event.getX(), event.getY());
                layout.getChildren().add(l.menu.getLabel());
                layout.getChildren().add(l);
                MainMenu.move=true;
                event.consume();
            }
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

        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                String X = A.getText();
                String Y = B.getText();
                String Z = C.getText();
                double a = new Double(X);
                double b = new Double(Y);
                double c = new Double(Z);
                GeoLine l = new GeoLine(a,b,c);
                l.calibrate();
                layout.getChildren().add(l);
                if(b!=0)
                l.menu.set(0, c/b);
                else
                    l.menu.set(c,c);
                layout.getChildren().add(l.menu.lbl);
                setPoints(l);
                SmallMenu.menuSet(l);
                
            }
        });


    }

    
    
}
