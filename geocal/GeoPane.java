/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import static javafx.print.PrintColor.COLOR;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;

/**
 *
 * @author Administrator
 */
public class GeoPane extends GridPane{
    Pane chld = new Pane();
    Delta delta = new Delta();
    GeoPane()
    {
        super();
        chld.setTranslateX(this.getWidth()/2);
        chld.setTranslateY(this.getHeight()/2);
        this.getChildren().add(chld);
         for(int i=-7500;i<=7500;i+=15)
        {
            Line lx =new Line(i,-7500,i,7500);
            Line ly =new Line(-7500,i,7500,i);
            lx.setStroke(Color.BLACK);
            ly.setStroke(Color.BLACK);
            if(i==0)
            {
                lx.setStrokeWidth(1.2);
                ly.setStrokeWidth(1.2);
                lx.setOpacity(0.8);
                ly.setOpacity(0.8);
            }
            else if(i%75==0)
            {
                lx.setStrokeWidth(1.2);
                ly.setStrokeWidth(1.2);
                lx.setOpacity(0.3);
                ly.setOpacity(0.3);
            }
            else
            {
                lx.setOpacity(0.1);
                ly.setOpacity(0.1);
            }
            chld.getChildren().add(lx);
            chld.getChildren().add(ly);
            
        }
        
    }
    void apply_zoom(double factor)
    {
        Scale scale = new Scale();

        for (Node component : chld.getChildren()) {
            if (component instanceof Line)
            {
                ;
            }
            else if(component instanceof Circle)
            {
                scale.setX(component.getScaleX() * factor);
                scale.setY(component.getScaleY() * factor);
                component.getTransforms().add(scale);
                if(((Circle) component).getFill()!=Color.BLUE)
                {
                    ((Circle) component).setStrokeWidth(((Circle) component).getStrokeWidth()/factor);
                }
                else
                {
                    ((Circle) component).setRadius(((Circle) component).getRadius()/factor);
                }
                
            }
        }
    }
    
    void mouse_click(MouseEvent ev)
    {
        
        delta = new Delta(ev.getX()-chld.getTranslateX(),ev.getY()-chld.getTranslateY());
    }
    void mouse_drag(MouseEvent ev)
    {
        if(ev.getX()-delta.x>-5000&&ev.getX()-delta.x<5000)
        chld.setTranslateX(ev.getX()-delta.x);
        if(ev.getY()-delta.y>-5000&&ev.getY()-delta.y<5000)
        chld.setTranslateY(ev.getY()-delta.y);
    }

    private void scanInputControls(Pane pane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
