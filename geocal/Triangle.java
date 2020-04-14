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
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Lenovo
 */
public class Triangle extends Polygon{
    
    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL];
    public static Map<Integer, GeoCircle> TriangleMap = new HashMap();
    public static List<Integer> temp = new ArrayList<Integer>();
    public List<Integer> ids = new ArrayList<Integer>();
    public static Integer id =0;
    public GeoCircle out,in;
    SmallMenu menu;
    
    Triangle()
    {
        super();
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1.5);
    }
    Triangle(double... points)
    {
        this();
        if (points != null)
        {
            for(double p : points) {
                this.getPoints().add(p);
            }
        }
    }
    Triangle(Point a, Point b, Point c)
    {
        this(new double[]{
            a.getX(), a.getY(),
            b.getX(), b.getY(),
            c.getX(), c.getY()
        });
        menu = new SmallMenu(this.centroidPoint(a, b, c)); 
        menu.addForTriangle(); //MenuItems add
        setOut(a,b,c); //Circled draw
        setIn(a,b,c); //Inward 
    }
    
    public void setOut(Point a, Point b, Point c)
    {
        Circle c1 = GeoMetry.Circle3Point(a,b,c);
        this.out = new GeoCircle(c1);
        out.setVisible(false);
       // out.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
    }
    
    public void setIn(Point A, Point B, Point C)
    {
        Point a,b,c;
        if(GeoMetry.orientation(A, B, C)==1)
        {
            a=A; b=B; c=C;
        }
        else
        {
            a=C; b=B; c=A;
        }

        double titha1 = angle(a,b,c);
        double titha2 = angle(a,c,b);
        
        Point d = GeoMetry.rotation(b, c, -titha1/2.0);
        Point e = GeoMetry.rotation(c, b, +titha2/2.0);
        Point o = GeoMetry.solve_eqn(GeoMetry.make_eqn(b, d), GeoMetry.make_eqn(c, e));
        Equation one = GeoMetry.make_eqn(b, c);
        Point p = GeoMetry.solve_eqn(one , GeoMetry.perpendicularEqn(one, o));
        
        this.in = new GeoCircle(o, GeoMetry.distance(o, p));
        in.setVisible(false);
        System.out.println(in.toString());
        
    }
    
    public Point centroidPoint(Point a, Point b, Point c)
    {
        Point p = new Point();
        p.setX((a.getX()+b.getX()+c.getX())/3.0);
        p.setY((a.getY()+b.getY()+c.getY())/3.0);
        return p;
    }
    
    static void showPoint(Point pp, GeoPane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
        TriangleMap.put(id, cc);
        temp.add(id++);

        layout.chld.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    }
    
    public static void setCircle(Triangle geo)
    {
        geo.ids.addAll(temp);
        temp.clear();
    }
    
    /**
     *  Find angle
     * @param a
     * @param b
     * @param c
     * @return angle between abc in radian
     */
    public double angle(Point a, Point b, Point c)
    {
        Point BA = GeoMetry.sub(a, b);
        Point BC = GeoMetry.sub(c, b);
        double thita = Math.acos(GeoMetry.dot(BA, BC)/((GeoMetry.val(BA))*(GeoMetry.val(BC))));
        return thita;
    }
    
    static {
        for(int i=0; i<TOTAL; i++)
        {
            point[i] = new Point();
        }    
    }
    
    
    /**
     * Select 3 point for drawing triangle
     * and draw triangle
     * 
     * @param layout of primary stage
     */
    public static void Draw_3_Point(GeoPane layout)
    {

        temp.clear();
        click = 0;

        //selecting points
        layout.chld.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(MainMenu.move)
                    return ;
                if(event.getButton()!=MouseButton.SECONDARY) 
                {
                    click++;
                    if((click%3)!=0)
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
                        
                        //Draw Triangle from last 3 points
                        Triangle tri = new Triangle(point[click], point[click-1], point[click-2]);
                        TriangleMap.put(id, tri.in);
                        temp.add(id++);
                        TriangleMap.put(id, tri.out);
                        temp.add(id++);
                        setCircle(tri);
                        
                        layout.chld.getChildren().addAll(tri.in,tri.out,tri,tri.menu.getLabel());

                        //Event handler to remove it
                        SmallMenu.menuSet(tri);
                        
                    }

                }
            }
        });
    }
    
    
}
