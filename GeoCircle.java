package geocal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class GeoCircle extends Circle{
    
    static final int TOTAL = 600;
    static private int i_c = 1, click;
    static Point point[] = new Point[TOTAL];
    int ID;

    
    GeoCircle()
    {
        super();
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1.5);
        this.ID=i_c;
    }
    GeoCircle(Point pp)
    {
        this();
        this.setCenterX(pp.getX());
        this.setCenterY(pp.getY());
    }
    GeoCircle(Point pp, double r)
    {
        this(pp);
        this.setRadius(r);
    }
    GeoCircle(Circle c1)
    {
        this(new Point(c1.getCenterX(), c1.getCenterY()), c1.getRadius());
    }
    
    
    static void showPoint(Point pp, GeoPane layout)
    {
        Circle cc = new Circle();
        cc.setRadius(3);
        cc.setCenterX(pp.getX());
        cc.setCenterY(pp.getY());
        cc.setFill(Color.BLUE);
        layout.chld.getChildren().add(cc);
        
    }

    public static int getI_c() {
        return i_c;
    }

    public int getID() {
        return ID;
    }
    
    
    
    static {
        for(int i=0; i<TOTAL; i++)
        {
            point[i] = new Point();
        }    
    }
    
    
    
    /**
     * Select 3 point for drawing circle
     * and draw circle
     * 
     * @param layout of primary stage
     * @param scene of primary stage
     */
    public static void Draw_3_Point(GeoPane layout)
    {
        
         click = 0;
        
        //selecting points
        layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
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
                    
                    //Draw circle from last 3 points
                    Circle c1 = GeoMetry.Circle3Point(point[click], point[click-1], point[click-2]);
                    GeoCircle circle = new GeoCircle(c1);
                    showPoint(new Point(c1.getCenterX(), c1.getCenterY()), layout);
                    layout.chld.getChildren().add(circle);

                    i_c++;
                }
                
            }
        });
    }
    
    
    /**
     *  Select 1st point as Center & 2nd point as passing point
     *  then draw the circle
     *
     * @param layout
     * @param scene
     */
    public static void Draw_C_P(GeoPane layout)
    {
        click = 0;
        
        layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
                    
                    //Draw circle
                    double r = GeoMetry.distance(point[click], point[click-1]);
                    GeoCircle circle = new GeoCircle(point[click-1], r);
                    layout.chld.getChildren().add(circle);

                    i_c++;
                }
            }
        });
    }
    
    /**
     * Select two end points of diameter
     * then draw the circle
     * 
     * @param layout
     * @param scene 
     */
    public static void Draw_E_P(GeoPane layout)
    {
        click = 0;
        
        layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
                    
                    //Draw circle
                    Point pp = GeoMetry.mid(point[click], point[click-1]);
                    showPoint(pp, layout);
                    double r = GeoMetry.distance(point[click], pp);
                    GeoCircle circle = new GeoCircle(pp, r);
                    layout.chld.getChildren().add(circle);

                    i_c++;
                }
            }
        });
    }
    
    
    /**
     * 
     * Take input: center & radius
     * Then Draw the circle
     * 
     * @param layout of primary stage
     */
    public static void Draw_C_R(GeoPane layout){

        GeoCircle circle = new GeoCircle();
        layout.chld.getChildren().add(circle);
        
        Stage window = new Stage();
        window.setTitle("Give input");
        
        Label txt1 = new Label("Center");
        Label txt2 = new Label("Radius");
        TextField radius = new TextField();
        radius.setPromptText("Enter Radius");
        TextField x = new TextField();
        x.setPromptText("Enter X");
        TextField y = new TextField(); 
        y.setPromptText("Enter Y");
        Button submit = new Button("Submit");
 
        GridPane root = new GridPane();
        root.addRow(0,txt2,radius);
        root.addRow(1,txt1,x,y);
        root.addRow(2,submit);

        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        //window.showAndWait();
        window.show();
        
        
        submit.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent ae) {
                String X = x.getText();
                String Y = y.getText();
                String r = radius.getText();
                
                if(r==null || r.isEmpty())
                {
                    Alert.display("Error..!", "Please enter Radius");
                }
                else if(X==null || X.isEmpty())
                {
                    Alert.display("Error..!", "Please enter X cordinate of center");
                }
                else if(Y==null || Y.isEmpty())
                {
                    Alert.display("Error..!", "Please enter Y cordinate of center");
                }
                else
                {
                    Point pp = new Point(new Double(X), new Double(Y));
                    showPoint(pp, layout);
                    circle.setRadius(new Double(r));
                    circle.setCenterX(pp.getX());
                    circle.setCenterY(pp.getY());

                    window.close();
                }
            }
        });
        
//            layout.setOnMouseDragged(new EventHandler<MouseEvent>(){
//            @Override
//            public void handle(MouseEvent event) {
//
//                double d = GeoMetry.distance( new Point(circle.getCenterX(), circle.getCenterY() ), new Point(event.getX(), event.getY()) );
//                circle.setRadius(d);
//            }
//            
//        });

    }
    
}