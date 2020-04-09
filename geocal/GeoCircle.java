package geocal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class GeoCircle extends Circle{

    static final int TOTAL = 600;
    public static int click;
    public static Point point[] = new Point[TOTAL];
    SmallMenu menu;
    
    GeoCircle()
    {
        super();
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1.5);
    }
    GeoCircle(Point pp)
    {
        this();
        this.setCenterX(pp.getX());
        this.setCenterY(pp.getY());
        menu = new SmallMenu(pp);
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
    
    static void showPoint(Point pp, GeoPane layout, boolean show)
    {
        GeoCircle cc = new GeoCircle(pp);
        cc.setRadius(2);
        cc.setFill(Color.BLUE);
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
        if(!show)
        {
            cc.menu.HideLabel();
        }
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
     */
    public static void Draw_3_Point(GeoPane layout)
    {


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
                        showPoint(point[click], layout, true);
                    }
                    else
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        showPoint(point[click], layout, true);
                        
                        //Draw circle from last 3 points
                        Circle c1 = GeoMetry.Circle3Point(point[click], point[click-1], point[click-2]);
                         GeoCircle circle = new GeoCircle(c1);
                     
                        showPoint(new Point(c1.getCenterX(), c1.getCenterY()), layout, false);
                        layout.chld.getChildren().addAll(circle,circle.menu.getLabel());

                        //Event handler to remove it
                        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton()==MouseButton.SECONDARY)
                                {
                                    circle.menu.list.show(circle.menu.lbl, Side.BOTTOM, 0, 0);
                                }
                            }
                        });
                    
                        circle.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                circle.menu.Rename();
                            }
                        });
                        circle.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                ((Pane)circle.getParent()).getChildren().remove(circle.menu.getLabel());
                                ((Pane)circle.getParent()).getChildren().remove(circle);
                            }
                        });
                    }

                }
            }
        });
    }


    /**
     *  Select 1st point as Center & 2nd point as passing point
     *  then draw the circle
     *
     * @param layout
     */
    public static void Draw_C_P(GeoPane layout)
    {
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
                        showPoint(point[click], layout, false);
                    }
                    else
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        showPoint(point[click], layout, true);

                        //Draw circle
                        double r = GeoMetry.distance(point[click], point[click-1]);
                        GeoCircle circle = new GeoCircle(point[click-1], r);
                        layout.chld.getChildren().addAll(circle,circle.menu.getLabel());
 
                        //Event handler to remove it
                        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton()==MouseButton.SECONDARY)
                                {
                                    circle.menu.list.show(circle.menu.lbl, Side.BOTTOM, 0, 0);
                                }
                            }
                        });
                    
                        circle.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                circle.menu.Rename();
                            }
                        });
                        circle.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                ((Pane)circle.getParent()).getChildren().remove(circle.menu.getLabel());
                                ((Pane)circle.getParent()).getChildren().remove(circle);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Select two end points of diameter
     * then draw the circle
     * 
     * @param layout
     */
    public static void Draw_E_P(GeoPane layout)
    {
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
                        showPoint(point[click], layout,true);
                    }
                    else
                    {
                        point[click].setX(event.getX());
                        point[click].setY(event.getY());
                        showPoint(point[click], layout ,true);

                        //Draw circle
                        Point pp = GeoMetry.mid(point[click], point[click-1]);
                        showPoint(pp, layout, false);
                        double r = GeoMetry.distance(point[click], pp);
                        GeoCircle circle = new GeoCircle(pp, r);
                        layout.chld.getChildren().addAll(circle, circle.menu.getLabel());

                         //Event handler to remove it
                        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton()==MouseButton.SECONDARY)
                                {
                                    circle.menu.list.show(circle.menu.lbl, Side.BOTTOM, 0, 0);
                                }
                            }
                        });
                    
                        circle.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                circle.menu.Rename();
                            }
                        });
                        circle.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                ((Pane)circle.getParent()).getChildren().remove(circle.menu.getLabel());
                                ((Pane)circle.getParent()).getChildren().remove(circle);
                            }
                        });
                    }
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
        if(MainMenu.move)
            return ;

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
                    GeoCircle circle = new GeoCircle(pp, new Double(r));
                    showPoint(pp, layout, false);
                    layout.chld.getChildren().addAll(circle, circle.menu.getLabel());
                    //Event handler to remove it
                    circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(event.getButton()==MouseButton.SECONDARY)
                            {
                                circle.menu.list.show(circle.menu.lbl, Side.BOTTOM, 0, 0);
                            }
                        }
                    });
                    
                    circle.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            circle.menu.Rename();
                        }
                    });
                    circle.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            ((Pane)circle.getParent()).getChildren().remove(circle.menu.getLabel());
                            ((Pane)circle.getParent()).getChildren().remove(circle);
                        }
                    });
                    
                    window.close();
                }
            }
        });


    }

} 