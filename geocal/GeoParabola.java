package geocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Lenovo
 */
public class GeoParabola {
    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL]; 
    
    static void showPoint(Point pp, GeoPane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);

        layout.chld.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    }
     static {
        for(int i=0; i<TOTAL; i++)
        {
            point[i] = new Point();
        }    
    }
     
    /**
     * Select 1st point as Vertex & 2nd point as Focus
     * then draw the parabola
     * 
     * @param layout 
     */
    public static void Parabola(GeoPane layout)
    {
        click=0;
        
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
                        
                        double a = GeoMetry.distance(point[click], point[click-1]);
                        //Considering x^2=4ay
                        String fun = String.format("( (x - %f)^2 + %f )/ %f", point[click-1].getX(), 4.0*a*point[click-1].getY(), 4.0*a);
                        
                        System.out.println("Fun: y="+fun);
                        System.out.println(point[click-1].getX()+","+point[click-1].getY());
                        System.out.println(point[click].getX()+","+point[click].getY());
                                                
                        Expression ex = new Expression(fun);
                        Graph g = new Graph();
                        for(int x =-7500; x<=7500; x+=1)
                        {
                            double y=ex.getValue(x+0.0);
                            if(Double.isNaN(y)) continue;
                            g.points.add(x+0.0);
                            g.points.add(y);
                        }
                        double m = Math.atan2((point[click].getY() - point[click-1].getY()), (point[click].getX()-point[click-1].getX()));
                        Rotate rotate = new Rotate(Math.toDegrees(m)-90.0);
                        rotate.setPivotX(point[click-1].getX());
                        rotate.setPivotY(point[click-1].getY());
                        g.polyline.getTransforms().add(rotate);
                        layout.chld.getChildren().addAll(g.polyline);
                      
                    }
                }
            }
        });
    }
}
