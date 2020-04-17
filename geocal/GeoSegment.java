/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import static geocal.GeoLine.click;
import static geocal.GeoLine.point;
import static geocal.GeoLine.showPoint;
import static geocal.GeoLine.temp;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class GeoSegment extends GeoLine{
    @Override
    Void calibrate()
    {
        this.setStrokeWidth(2);
        return null;
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
                        GeoSegment l = new GeoSegment(point[click],point[click-1]); 
                        
                        l.setStartX(point[click].getX());
                        l.setStartY(point[click].getY());
                        l.setEndX(point[click-1].getX());
                        l.setEndY(point[click-1].getY());
                        l.menu.set(mx, my);
                        l.calibrate();
                        setPoints(l);
                        layout.getChildren().add(l.menu.getLabel());                        
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

    private GeoSegment(Point a, Point b) {
        super(a,b);
        this.setStartX(a.getX());
        this.setStartY(a.getY());
        this.setEndX(b.getX());
        this.setEndY(b.getY());
    }
}
