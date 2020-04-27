package geocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Lenovo
 */
public class GeoEllipse extends Ellipse{
    
    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL];
    public static Map<Integer, GeoCircle> EllipseMap = new HashMap();
    public static List<Integer> temp = new ArrayList<Integer>();
    public List<Integer> ids = new ArrayList<Integer>();
    public static Integer id =0;
    SmallMenu menu;
    
    GeoEllipse()
    {
        super();
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1.5);
        this.setRadiusX(0.0);
        this.setRadiusY(0.0);
        this.setCenterX(0.0);
        this.setCenterY(0.0);
        this.menu = new SmallMenu();
        this.menu.HideLabel();
        menu.addForEllipse(); //MenuItem Add
        
    }
    
    GeoEllipse(Point a)
    {
        this();
        this.setCenterX(a.getX());
        this.setCenterY(a.getY());
        this.menu.set(a.getX(), a.getY());        
    }
    public static void setAxis(GeoEllipse geo, Point a, Point b)
    {
        double m = Math.atan2((b.getY() - a.getY()), (b.getX()-a.getX()));
        Rotate rotate = new Rotate(Math.toDegrees(m));
        rotate.setPivotX(geo.getCenterX());
        rotate.setPivotY(geo.getCenterY());
        geo.getTransforms().add(rotate);
    }
    static void showPoint(Point pp, GeoPane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
        EllipseMap.put(id, cc);
        temp.add(id++);
        layout.chld.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    } 
    
    public static void Delete(GeoEllipse e)
    {
        e.ids.forEach((Integer x) -> {
            ((Pane)e.getParent()).getChildren().remove((EllipseMap.get(x)).menu.getLabel());
            ((Pane)e.getParent()).getChildren().remove(EllipseMap.get(x));
        });
        
        ((Pane)e.getParent()).getChildren().remove(e.menu.getLabel());
        ((Pane)e.getParent()).getChildren().remove(e);
    }
    
    static {
        for(int i=0; i<TOTAL; i++)
        {
            point[i] = new Point();
        }    
    }
    
    public static void setCircle(GeoEllipse geo)
    {
        geo.ids.addAll(temp);
        temp.clear();
    }
    
    public static void Draw_Ellipse(GeoPane layout)
    {
        temp.clear();
        click = 0;
        GeoEllipse ellipse = new GeoEllipse();
        layout.chld.getChildren().addAll(ellipse, ellipse.menu.getLabel());
        
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
                        
                        Point mid = GeoMetry.mid(point[click], point[click-1]);
                                                
                        ellipse.setCenterX(mid.getX());
                        ellipse.setCenterY(mid.getY());
                        setAxis(ellipse, point[click-1], point[click]);
                        ellipse.menu.set(mid.getX(), mid.getY());
                        ellipse.menu.ShowLabel();
                        ellipse.setRadiusX(GeoMetry.distance(mid, point[click]));
                        
                        layout.chld.setOnMouseMoved((MouseEvent move) -> {
                            ellipse.setRadiusY(GeoMetry.distance(mid, new Point(move.getX(),move.getY())));
                        });
                        layout.chld.setOnMouseClicked((MouseEvent ev) -> {
                            layout.chld.setOnMouseClicked(null);
                            layout.chld.setOnMouseMoved(null);
                            click++;
                            point[click].setX(ev.getX());
                            point[click].setY(ev.getY());
                            
                            double r = GeoMetry.distance(point[click], mid);
                            
                            setCircle(ellipse);
                            ellipse.setRadiusY(r);
                            
                            //Event handler to remove it
                            SmallMenu.menuSet(ellipse);
                        });
                    }
                }
            }
        });
    }
    
}
