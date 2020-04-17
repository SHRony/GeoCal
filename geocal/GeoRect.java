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
public class GeoRect{
    
    public Polygon poly;
    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL];
    private Point p[] = new Point[4];
    public static Map<Integer, GeoCircle> RectMap = new HashMap();
    public static List<Integer> temp = new ArrayList<Integer>();
    public List<Integer> ids = new ArrayList<Integer>();
    public static Integer id =0;
    private static String input="";
    private static Stage window = new Stage();
    private static Label txt = new Label();
    SmallMenu menu;
    private GeoPane layout;
    
    GeoRect(Point a, Point b, Point c , Point d)
    {
        poly = new Polygon(new double[]{
            a.getX(), a.getY(),
            b.getX(), b.getY(),
            c.getX(), c.getY(),
            d.getX(), d.getY()
        });
        this.poly.setFill(Color.TRANSPARENT);
        this.poly.setStroke(Color.BLACK);
        this.poly.setStrokeWidth(1.5);
        menu = new SmallMenu(this.center(a, c));
        menu.addForRectangle();
        p[0]=a;
        p[1]=b;
        p[2]=c;
        p[3]=d;
    }

    public GeoPane getLayout() {
        return layout;
    }

    public void setLayout(GeoPane layout) {
        this.layout = layout;
    }

    public void setPoint(int i, Point pp)
    {
        p[i] = pp;
    }
    public Point getPoint(int i)
    {
        return p[i];
    }
    private Point center(Point a, Point c)
    {
        return GeoMetry.mid(a,c);
    }
    
    public static void setCircle(GeoRect geo)
    {
        geo.ids.addAll(temp);
        temp.clear();
    }
    static void showPoint(Point pp, GeoPane layout)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
        RectMap.put(id, cc);
        temp.add(id++);
        layout.chld.getChildren().addAll(cc, cc.menu.getLabel());

        //Event handler to remove it
        SmallMenu.menuSet(cc);
    }
    
    {
        for(int i=0; i<4; i++)
            p[i]= new Point();
    }
    static {
        for(int i=0; i<TOTAL; i++)
        {
            point[i] = new Point();
        }    
        Take_Height();
    }
    
    public static void Delete(GeoRect rect)
    {
        rect.ids.forEach((Integer x) -> {
                    ((Pane)rect.poly.getParent()).getChildren().remove((RectMap.get(x)).menu.getLabel());
                    ((Pane)rect.poly.getParent()).getChildren().remove(RectMap.get(x));
            });
            ((Pane)rect.poly.getParent()).getChildren().remove(rect.menu.getLabel());
            ((Pane)rect.poly.getParent()).getChildren().remove(rect.poly);
    }
    
    public static void Draw_Rect(GeoPane layout)
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
                        txt.setText("Enter Height of Rectangle");
                        window.showAndWait(); //wait untill give input!
                        double h = (new Double(input))*MainMenu.factor;
                        input="";
                        
                        //find 4th point
                        Point d = GeoMetry.ab_to_d(point[click-1], point[click], h);
                        d = GeoMetry.rotation(point[click-1], d, Math.PI/2.0);
                        showPoint(d, layout);
                        
                        //find 3rd point
                        Point c = GeoMetry.ab_to_d(point[click], point[click-1], h);
                        c = GeoMetry.rotation(point[click], c, -Math.PI/2.0);
                        showPoint(c, layout);
                        
                        //Draw Rectangle
                        GeoRect rect = new GeoRect(point[click-1], point[click], c, d);
                        setCircle(rect);
                        rect.setLayout(layout);
                        layout.chld.getChildren().addAll(rect.poly, rect.menu.getLabel());

                        //Event handler to remove it
                        SmallMenu.menuSet(rect);
                    }
                }
            }
        });
    }
    
    /**
     * Take new Height of rectangle 
     * & redraw the rectangle
     * @param rect 
     */
    
    public static void EditHeight(GeoRect rect)
    {
        temp.clear();
        txt.setText("Enter New Height of Rectangle");
        window.showAndWait();
        double h = (new Double(input))*MainMenu.factor;
        input="";
        
        GeoPane layout = rect.getLayout();
        Delete(rect);
        
        //1st,2nd point unchanged
        Point a = rect.getPoint(0);
        Point b = rect.getPoint(1);
        showPoint(a, layout);
        showPoint(b, layout);
        
        //find 3rd point
        Point c = GeoMetry.ab_to_d(b, a, h);
        c = GeoMetry.rotation(b, c, -Math.PI/2.0);
        rect.setPoint(2, c);
        showPoint(c, layout);
        
        //find 4th point
        Point d = GeoMetry.ab_to_d(a, b, h);
        d = GeoMetry.rotation(a, d, Math.PI/2.0);
        rect.setPoint(3, d);
        showPoint(d, layout);
                        
        List<Double> newPoints = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            newPoints.add(rect.getPoint(i).getX());
            newPoints.add(rect.getPoint(i).getY());
        }
        rect.poly.getPoints().clear();
        rect.poly.getPoints().addAll(newPoints);
        setCircle(rect);
        layout.chld.getChildren().addAll(rect.poly, rect.menu.getLabel());

    }
    
    /**
     * Take new Width of rectangle 
     * & redraw the rectangle
     * @param rect 
     */
    
    public static void EditWidth(GeoRect rect)
    {
        temp.clear();
        txt.setText("Enter New Width of Rectangle");
        window.showAndWait();
        double h = (new Double(input))*MainMenu.factor;
        input="";
        
        //1st,4th point unchanged
        Point a = rect.getPoint(0);
        Point d = rect.getPoint(3);
        
        //find 2nd point
        Point b = GeoMetry.ab_to_d(a, d, h);
        b = GeoMetry.rotation(a, b, -Math.PI/2.0);
        rect.setPoint(1, b);
                        
        //find 3rd point
        Point c = GeoMetry.ab_to_d(d, a, h);
        c = GeoMetry.rotation(d, c, Math.PI/2.0);
        rect.setPoint(2, c);
        
        Delete(rect);
        showPoint(a, rect.layout);
        showPoint(b, rect.layout);
        showPoint(c, rect.layout);
        showPoint(d, rect.layout);
        
        List<Double> newPoints = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            newPoints.add(rect.getPoint(i).getX());
            newPoints.add(rect.getPoint(i).getY());
        }
        

        rect.poly.getPoints().clear();
        rect.poly.getPoints().addAll(newPoints);
        setCircle(rect);
        rect.layout.chld.getChildren().addAll(rect.poly, rect.menu.getLabel());

    }
    
    /**
     * Take input from user
     */
    static void Take_Height()
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
