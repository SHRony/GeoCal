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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
                GeoCircle.Draw_C_R(layout);
            }
        });
        
        cAndOnePoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoCircle.Draw_C_P(layout);
            }
        });
        
        ThreePoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
               // System.out.println("Here i am");
                GeoCircle.Draw_3_Point(layout);
            }
        });
        
        TwoPoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoCircle.Draw_E_P(layout);
            }
        });
        
        //point menu
        Menu pt = new Menu("Point");
        MenuItem mid = new MenuItem("Mid Point");
        pt.getItems().addAll(mid);
        
        //vector menu
        Menu vec = new Menu("Vector");
        
        //Line menu
        Menu line = new Menu("Line");
        
        //Polygon
        Menu poly = new Menu("Polygon");
        
        //Graphs
        Menu graph = new Menu("Graph");
        
        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(circle,pt,vec,line,poly,graph);
        
        root.addRow(1, layout);
        root.addRow(0,menuBar);
        scene = new Scene(root, 800, 700);
        
        //Graph Paper Control
        layout.setOpacity(0.8);

        {
            Scale scale = new Scale();
            layout.getTransforms().add(scale);
        }
        
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
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
                    
                    //for (Node component : layout.chld.getChildren()) {
                    //                        
                    //}
                    layout.chld.setTranslateX(2*layout.chld.getTranslateX()-event.getX());
                    layout.chld.setTranslateY(2*layout.chld.getTranslateY()-event.getY());

                }
                else if(current_zoom<1)
                {
                    zoomFactor = 2/current_zoom;
                    layout.chld.setTranslateX(layout.chld.getTranslateX()/2+event.getX()/2);
                    layout.chld.setTranslateY(layout.chld.getTranslateY()/2+event.getY()/2);
                    
                }
                System.out.println(current_zoom+"    "+zoomFactor);
                current_zoom = zoomFactor*current_zoom;
                Scale scale = new Scale();
                scale.setPivotX(event.getX());
                scale.setPivotY(event.getY());
                System.out.println("Pre scale x "+layout.getScaleX());
                scale.setX(layout.getScaleX() * zoomFactor);
                scale.setY(layout.getScaleY() * zoomFactor);
                System.out.println(scale.getX());
                layout.getTransforms().add(scale);
                System.out.println("Cur scale x "+layout.getScaleX());
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
        ps.setTitle("My GeoCalulator");
        ps.show();
        
    }
}