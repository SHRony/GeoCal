/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import static geocal.GeoPoint.st;
import java.awt.Color;
import java.io.IOException;
import static java.lang.reflect.Array.set;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import static javafx.beans.property.BooleanProperty.booleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Administrator
 */
public class GraphHome {
    static boolean lineAllowed = false;
    static ArrayList<GeoPoint> currentPoints = new ArrayList();//List of currently displayed points
    static Label contextCor = new Label();
    static IntegerProperty vSize =new SimpleIntegerProperty();
    static Vector<GeoPoint> clickedPoints = new Vector<GeoPoint>();
    static IntegerProperty lSize =new SimpleIntegerProperty();
    static Vector clickedLines = new Vector();
    static int cnt=0;
    static String inp;
    static JFXTabPane tab = new JFXTabPane();//Tab container in leftbar
    static Tab points,shapes;//Tabs in leftbar
    static Set<Node> Items = new TreeSet<Node>();
    static Vector temporary=new Vector<>();//Vector containing unfinished temporary objects
    static BorderPane container = new BorderPane();
    static VBox leftBar = new VBox(10);
    static VBox subMenuBar= new VBox(10);
    static ScrollPane stkContainer = new ScrollPane();
    static VBox stkBar=new VBox(5);
    static ScrollPane pointsContainer = new ScrollPane();
    static VBox stkPt = new VBox(5);
    static VBox rightBar = new VBox(10);
    static VBox detailsBar = new VBox(10);
    static GridPane topContainer = new GridPane();
    static HBox topBar = new HBox(10);
    public static GeoPane gPaper=new GeoPane();
    static Label xCor = new Label();
    static Label yCor = new Label();
    static FlowPane pointMenu,circleMenu,triMenu,rectMenu,polyMenu,clearMenu,lineMenu;
    static JFXToggleButton oneTouch = new JFXToggleButton();
    
    static ChangeListener<Number> nothing = new ChangeListener<Number>(){
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        }
    };
    static EventHandler<MouseEvent> eventCor = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev)
            {
                showCor(ev.getX(),ev.getY());
                
            }
        };
    static EventHandler<MouseEvent> cursorHand = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev)
            {
                gPaper.getScene().setCursor(Cursor.HAND);
            }
    };
    
    static EventHandler<MouseEvent> cursorClosedHand = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev)
            {
                gPaper.getScene().setCursor(Cursor.CLOSED_HAND);
            }
    };
    
    static EventHandler<MouseEvent> cursorDefault = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev)
            {
                gPaper.getScene().setCursor(Cursor.DEFAULT);
            }
    };
    
    
    static EventHandler<MouseEvent> eventDetails = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev)
            {
                if(ev.getClickCount()==2)
                {
                    ((GeoCircle)ev.getSource()).showDetails();
                }
                
            }
        };
    public static void init(Pane root)
    {
        VBox mouseCor = new VBox();
        FlowPane Title = new FlowPane();
        FlowPane xT = new FlowPane();
        FlowPane yT= new FlowPane();
        
        Title.getStyleClass().add("corItem");
        xT.getStyleClass().add("corItem");
        yT.getStyleClass().add("corItem");
        mouseCor.getStyleClass().add("corBar");
        Title.prefWidthProperty().bind(rightBar.widthProperty());
        xT.prefWidthProperty().bind(rightBar.widthProperty());
        yT.prefWidthProperty().bind(rightBar.widthProperty());
        Label title = new Label("Mouse Cordinate:");
        Title.getChildren().add(title);
        Label titleX = new Label("X:");
        Label titleY = new Label("Y:");
        titleX.getStyleClass().add("white");
        titleY.getStyleClass().add("white");
        xCor.getStyleClass().add("white");
        yCor.getStyleClass().add("white");
        title.getStyleClass().add("white");
        xT.getChildren().add(titleX);
        xT.getChildren().add(xCor);
        yT.getChildren().add(titleY);
        yT.getChildren().add(yCor);
        mouseCor.getChildren().addAll(Title,xT,yT);
        Label oneLabel = new Label("One touch?");
        oneLabel.setFont(new Font(20));
        oneLabel.setTextAlignment(TextAlignment.CENTER);
        rightBar.getChildren().add(oneLabel);
        rightBar.getChildren().add(oneTouch);
        rightBar.getChildren().add(mouseCor);
        rightBar.getChildren().add(detailsBar);
        detailsBar.prefWidthProperty().bind(rightBar.widthProperty().subtract(5));
        container.maxHeightProperty().bind(root.getScene().heightProperty());
        container.maxWidthProperty().bind(root.getScene().widthProperty());
        JFXButton home = new JFXButton();
        Image img = new Image("img/home.png");
        home.setGraphic(new ImageView (img));
        JFXButton circle = new JFXButton();
        img = new Image("img/circle.jpg");
        circle.setGraphic(new ImageView (img));
        JFXButton point = new JFXButton();
        img = new Image("img/point.png");
        point.setGraphic(new ImageView (img));
        JFXButton rect = new JFXButton();
        img = new Image("img/rect.jpg");
        rect.setGraphic(new ImageView (img));
        JFXButton line = new JFXButton();
        img = new Image("img/line.png");
        line.setGraphic(new ImageView (img));
        JFXButton poly = new JFXButton();
        img = new Image("img/poly.png");
        poly.setGraphic(new ImageView (img));
        JFXButton clear = new JFXButton();
        img = new Image("img/clear.jpg");
        clear.setGraphic(new ImageView (img));
        topBar.getChildren().addAll(home,point,line,circle,rect,poly,clear);
        container.setCenter(gPaper);
        topContainer.getChildren().add(topBar);
        container.setTop(topContainer);
        container.setLeft(leftBar);
        container.setRight(rightBar);
        container.getStyleClass().add("graphContainer");
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getStylesheets().add("img/style.css");
        topContainer.getStyleClass().add("topBar");
        leftBar.getStyleClass().add("sideBar");
        rightBar.getStyleClass().add("sideBar");
        points = new Tab();
        shapes = new Tab();
        shapes.setText("Shapes");
        points.setText("Points");
        stkBar.getStyleClass().add("itemList");
        stkPt.getStyleClass().add("itemList");
        
        pointsContainer.setContent(stkPt);
        stkContainer.setContent(stkBar);
        points.setContent(pointsContainer);
        shapes.setContent(stkContainer);
        tab.getTabs().addAll(shapes,points);
        leftBar.setPrefWidth(200);
        rightBar.setPrefWidth(300);
        leftBar.getChildren().addAll(subMenuBar,tab);
        createMenu(root);
        tab.prefHeightProperty().bind(gPaper.heightProperty().subtract(subMenuBar.heightProperty()));
        
        //Mouse Clicks
        home.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               FadeTransition fadeOut = new FadeTransition(Duration.millis(500), container);
               fadeOut.setFromValue(1);
               fadeOut.setToValue(0);
               fadeOut.setCycleCount(1);
               fadeOut.play();
               fadeOut.setOnFinished((e) -> {
                HomePage.load(root);
                });
           }
       });
       circle.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               buttonPressed();
               gPaper.chld.setOnMouseClicked(null);
               if(circleMenu.getParent()==null)
               subMenuBar.getChildren().setAll(circleMenu);
           }
       });
       line.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae){
               buttonPressed();
               if(lineMenu.getParent()==null)
               subMenuBar.getChildren().setAll(lineMenu);
           }
       });
       point.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae){
               buttonPressed();
               if(pointMenu.getParent()==null)
               subMenuBar.getChildren().setAll(pointMenu);
           }
       });
       poly.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoGon.draw(gPaper.chld);
            }
        });
       clear.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae){
               buttonPressed();
               allClear();
           }
       });
       
        
    }
    static void createMenu(Pane root)
    {
        //Point submenu
        pointMenu = new FlowPane();
        pointMenu.setVgap(10);
        pointMenu.setHgap(30);
        pointMenu.setMaxWidth(200);
        pointMenu.getStyleClass().add("sidemenu");
        JFXButton pt =  new JFXButton();
        Image img = new Image("img/point.png");
        pt.setGraphic(new ImageView(img));
        JFXButton mid = new JFXButton();
        img=new Image("img/mid.png");
        mid.setGraphic(new ImageView(img));
        JFXButton proj = new JFXButton();
        img= new Image("img/proj.png");
        proj.setGraphic(new ImageView(img));
        JFXButton refl = new JFXButton();
        img = new Image("img/refl.png");
        refl.setGraphic(new ImageView(img));
        JFXButton pivot = new JFXButton();
        img = new Image("img/pivot.png");
        pivot.setGraphic(new ImageView(img));
        pointMenu.getChildren().addAll(pt,mid,proj,refl,pivot);
        pt.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoPoint.draw(gPaper.chld);
            }
        });
        mid.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoPoint.drawMid(gPaper.chld);
            }
        });
        
        proj.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoPoint.drawProj(gPaper.chld);
            }
        });
        
        refl.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoPoint.drawRefl(gPaper.chld);
            }
        });
        
        pivot.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoPoint.draw_pivot(gPaper.chld);
            }
        });
        
        
        
        
        
        
        //Circle submenu
        circleMenu = new FlowPane();
        circleMenu.setVgap(10);
        circleMenu.setHgap(30);
        circleMenu.setMaxWidth(200);
        circleMenu.getStyleClass().add("sidemenu");
        JFXButton CTP = new JFXButton();
        img = new Image("img/circle_1.png");
        CTP.setGraphic(new ImageView (img));
        JFXButton PTP = new JFXButton();
        img = new Image("img/circle (1).jpg");
        PTP.setGraphic(new ImageView (img));
        JFXButton PPP = new JFXButton();
        img = new Image("img/circle_3.png");
        PPP.setGraphic(new ImageView (img));
        JFXButton INP = new JFXButton();
        img = new Image("img/circle (3).jpg");
        INP.setGraphic(new ImageView (img));
        circleMenu.getChildren().addAll(CTP,PTP,PPP,INP);
        CTP.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoCircle.drawCenterRadius(gPaper.chld);
            }
        });
        PTP.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoCircle.Draw_E_P(gPaper.chld);
            }
        });
        PPP.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoCircle.Draw_3_P(gPaper.chld);
            }
        });
        INP.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoCircle.Draw_C_R(gPaper.chld);
            }
        });
        
        
        //Line submenu
        lineMenu = new FlowPane();
        lineMenu.setVgap(10);
        lineMenu.setHgap(30);
        lineMenu.setMaxWidth(200);
        lineMenu.getStyleClass().add("sidemenu");
        JFXButton seg = new JFXButton();
        img = new Image("img/segment.png");
        seg.setGraphic(new ImageView (img));
        JFXButton lne = new JFXButton();
        img= new Image("img/line.png");
        lne.setGraphic(new ImageView(img));
        JFXButton ray = new JFXButton();
        img= new Image("img/ray.png");
        ray.setGraphic(new ImageView(img));
        lineMenu.getChildren().addAll(seg,lne,ray);
        seg.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoSegment.draw(gPaper.chld);
            }
        });
        lne.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoLine.draw(gPaper.chld);
            }
        });
        ray.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                buttonPressed();
                GeoRay.draw(gPaper.chld);
            }
        });
        
        
        
        
    }
    public static void show(Pane root)
    {
        
        rightBar.setMinWidth(100);
        container.setOpacity(0);
        topContainer.minWidthProperty().bind(root.getScene().widthProperty());
        container.minWidthProperty().bind(root.getScene().widthProperty());
        container.minHeightProperty().bind(root.getScene().heightProperty());        
        root.getChildren().setAll(container);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000),container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        gPaper.chld.setOnMouseMoved(eventCor);
    }
    public static void showCor(double X,double Y)
    {
                
                X=Math.round(X);
                Y=Math.round(Y);
                X*=gPaper.Unit.getValue()*1000;
                Y*=gPaper.Unit.getValue()*1000;
                X=Math.round(X);
                Y=Math.round(Y);
                X/=1000;
                Y/=1000;
                xCor.setText((String.valueOf(X)));
                yCor.setText(String.valueOf(Y));
    }
    public static void cleanTemp()
    {
//        System.out.println("temp clear "+temporary.size());
        for(int i=0;i<temporary.size();i++)
        {
            if(temporary.get(i) instanceof GeoCircle)
                ((GeoCircle)temporary.get(i)).erase();
            else if(temporary.get(i) instanceof GeoSegment)
                ((GeoSegment)temporary.get(i)).erase();
            else if(temporary.get(i) instanceof GeoRay)
                ((GeoRay)temporary.get(i)).erase();
            else if(temporary.get(i) instanceof GeoLine)
                ((GeoLine)temporary.get(i)).erase();
            else if(temporary.get(i) instanceof Line)
            {
                Pane par=(Pane) ((Line)temporary.get(i)).getParent();
                if(par!=null)
                    par.getChildren().remove(temporary.get(i));
            }
            else if(temporary.get(i) instanceof GeoPoint)
            {
                ((GeoPoint)temporary.get(i)).remove();
            }
        }
        temporary.clear();
    }
    public static String genName()
    {
        String s;
        s=String.valueOf((char)('a'+cnt%26));
        cnt++;
        return s;
    }
    public static void buttonPressed()
    {
        vSize=new SimpleIntegerProperty();
        lSize = new SimpleIntegerProperty();
        gPaper.chld.setOnMouseMoved(eventCor);
        gPaper.chld.setOnMouseClicked(null);
        cleanTemp();
    }
    public static void allClear()
    {
        for(int i=0;i<currentPoints.size();i++)
        {
//            System.out.println(i);
            temporary.add(currentPoints.get(i));
        }
        cleanTemp();
    }
    
}
