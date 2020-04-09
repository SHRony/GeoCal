/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;


import static geocal.GeoCircle.click;
import static geocal.GeoCircle.point;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
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
                
                if(e.getButton()!=MouseButton.SECONDARY) 
                {
                    Point pp = new Point(e.getX(), e.getY());
                    GeoCircle.showPoint(pp, layout, true);
                }
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

                if(event.getButton()!=MouseButton.SECONDARY) 
                {
                    click++;
                    if(click%2==1)
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        GeoCircle.showPoint(point[click], layout,true);
                    }
                    else
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        GeoCircle.showPoint(point[click], layout ,true);

                        //Find Mid point
                        Point pp = GeoMetry.mid(point[click], point[click-1]);
                        GeoPoint.showPoint(pp, layout);
                    }
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
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.GREEN);
        cc.menu.setName("Mid");
        layout.chld.getChildren().addAll(cc, cc.menu.getLabel());
        cc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            if(event.getButton()==MouseButton.SECONDARY)
               cc.menu.list.show(cc.menu.lbl, Side.BOTTOM, 0, 0);
            }
        });
        cc.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ((Pane)cc.getParent()).getChildren().remove(cc.menu.getLabel());
                ((Pane)cc.getParent()).getChildren().remove(cc);
            }
        });
        cc.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                cc.menu.Rename();
            }
        });

    }

    
}
