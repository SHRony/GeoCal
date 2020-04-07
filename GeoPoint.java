/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;


import static geocal.GeoCircle.click;
import static geocal.GeoCircle.point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Lenovo
 */
public class GeoPoint extends Point {
    
    /**
     * Select & draw point
     * @param layout 
     */
    public static void Draw_Point(GeoPane layout)
    {
        layout.chld.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Point pp = new Point(e.getX(), e.getY());
                GeoCircle.showPoint(pp, layout);
            }
        });
    }
    
    
    /**
     * Select two point & find mid point
     * @param layout 
     */
    
    public static void Mid_Point(GeoPane layout)
    {
        click = 0;
        
        layout.chld.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(MainMenu.move)
                    return;
                click++;
                if(click%2==1)
                {
                    point[click].setX(event.getX());
                    point[click].setY(event.getY());
                    GeoCircle.showPoint(point[click], layout);
                }
                else
                {
                    point[click].setX(event.getX());
                    point[click].setY(event.getY());
                    GeoCircle.showPoint(point[click], layout);

                    Point pp = GeoMetry.mid(point[click], point[click-1]);
                    GeoPoint.showPoint(pp, layout);
                }
            }
        });
    }
    
    /**
     * Show a point using circle
     * 
     * @param pp
     * @param layout 
     */
    static void showPoint(Point pp, GeoPane layout)
    {
        Circle cc = new Circle();
        cc.setRadius(3);
        cc.setCenterX(pp.getX());
        cc.setCenterY(pp.getY());
        cc.setFill(Color.RED);
        layout.chld.getChildren().add(cc);
        cc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            if(event.getButton()==MouseButton.SECONDARY)
                ((Pane)cc.getParent()).getChildren().remove(cc);
            }
        } );
        

    }
}
