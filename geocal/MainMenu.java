package geocal;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class MainMenu extends Application{
    public static boolean move=true;
    static double factor=15;
    GridPane root=new GridPane();
    GeoPane layout=new GeoPane();    
    Scene scene;
    
    static double current_zoom=1.0;
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage ps) throws Exception {


        //Circle menu
        Menu circle = new Menu("Circle");
        MenuItem cAndR = new MenuItem("Circle: Center & Radius");
        MenuItem cAndOnePoint = new MenuItem("Circle with Center through Point");
        MenuItem ThreePoint = new MenuItem("Circle through 3 Point");
        MenuItem TwoPoint = new MenuItem("Two endpoints of Diameter");
        circle.getItems().addAll(cAndR,cAndOnePoint,ThreePoint,TwoPoint);
        
        cAndR.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                move=false;
                GeoCircle.Draw_C_R(layout);
            }
        });

        cAndOnePoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                move=false;
                GeoCircle.Draw_C_P(layout);
            }
        });

        ThreePoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
               // System.out.println("Here i am");
                move=false;
                GeoCircle.Draw_3_Point(layout);
                e.consume();
            }
        });

        TwoPoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                move=false;
                GeoCircle.Draw_E_P(layout);
            }
        });

        //point menu
        Menu pt = new Menu("Point");
        MenuItem mid = new MenuItem("Mid Point");
        MenuItem draw = new MenuItem("Draw Points");
        pt.getItems().addAll(draw,mid);
        
        draw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                move=false;
                GeoPoint.Draw_Point(layout);
                e.consume();
            }
        });
        
        mid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoPoint.Mid_Point(layout);
            }
        });

        //vector menu
        Menu vec = new Menu("Vector");

        //Line menu
        Menu line = new Menu("Line");
        MenuItem line_p = new MenuItem();
        line.getItems().add(line_p);
        //Polygon
        Menu poly = new Menu("Polygon");
        MenuItem draw_triangle = new MenuItem("Triangle Draw");
        poly.getItems().addAll(draw_triangle);
        
        draw_triangle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                move=false;
                Triangle.Draw_3_Point(layout);
                e.consume();
            }
        });

        //Graphs
        Menu graph = new Menu("Graph");
        //Drag Graph Paper
        Menu mv = new Menu("Move");
        MenuItem Move_graph = new MenuItem("Move Graph");
        //clear all
        Menu clear= new Menu("Clear");
        MenuItem all_clr = new MenuItem("Clear All");
        clear.getItems().add(all_clr);
        mv.getItems().add(Move_graph);
        //Main menu bar
        MenuBar menuBar = new MenuBar();
        Move_graph.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                move=true;
            }
            
        });
        all_clr.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                move=true;
                layout.clear();
            }
            
        });
        menuBar.getMenus().addAll(circle,pt,vec,line,poly,graph,mv,clear);
        //add graph paper
        root.addRow(1, layout);
        //add menubar
        root.addRow(0,menuBar);
        scene = new Scene(root, 800, 700);

        //Graph Paper Control
        layout.setOpacity(0.8);
        {
            Scale scale = new Scale();
            layout.getTransforms().add(scale);
        }
        
        //Backgrounds of all the panes
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
        layout.chld.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));

        //Handling Mouse Scroll and its Effects on the GraphPaper
        layout.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.01;
                double deltaY = event.getDeltaY()*1.5;

                if (deltaY < 0)
                    zoomFactor = 0.99;

                if(current_zoom>2)
                {
                    zoomFactor=1.0/current_zoom;
                    Scale scale= new Scale();
                    layout.chld.setTranslateX(2*layout.chld.getTranslateX()-event.getX());
                    layout.chld.setTranslateY(2*layout.chld.getTranslateY()-event.getY());
                    layout.apply_zoom(2);
                    factor*=2;
                    
                }
                else if(current_zoom<1)
                {
                    zoomFactor = 2/current_zoom;
                    
                    layout.chld.setTranslateX(layout.chld.getTranslateX()/2+event.getX()/2);
                    layout.chld.setTranslateY(layout.chld.getTranslateY()/2+event.getY()/2);
                    layout.apply_zoom(0.5);
                    factor*=0.5;

                }
                current_zoom = zoomFactor*current_zoom;
                Scale scale = new Scale();
                scale.setPivotX(event.getX());
                scale.setPivotY(event.getY());
                scale.setX(layout.getScaleX() * zoomFactor);
                scale.setY(layout.getScaleY() * zoomFactor);
                System.out.println(scale.getX());
                layout.getTransforms().add(scale);
                event.consume();
            }
        });
        layout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                layout.mouse_click(ev);
            }
        });

        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                layout.mouse_drag(ev);
            }
        });
        //Changes during resizing window
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                layout.chld.setTranslateX(ps.getWidth()/2);
                layout.chld.setTranslateY(ps.getHeight()/2);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                layout.chld.setTranslateX(ps.getWidth()/2);
                layout.chld.setTranslateY(ps.getHeight()/2);
            }
        });

        ps.setScene(scene);
       
        layout.chld.setTranslateX(scene.getWidth()/2);
        layout.chld.setTranslateY(scene.getHeight()/2);
        ps.setTitle("My GeoCalulator");
        ps.show();

    }
} 
