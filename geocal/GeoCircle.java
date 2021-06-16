/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import java.util.Vector;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class GeoCircle extends Circle{
    double r,X,Y;
    Label name = new Label();
    Vector<GeoPoint> v=new Vector<GeoPoint>();
    Vector<Point> p = new Vector<Point>();
    Vector dependent = new Vector();
    GeoCircle()
    {
        super(0,0,0);
        r=0;
        X=0;
        Y=0;
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.strokeWidthProperty().bind(GraphHome.gPaper.BalanceFactor.multiply(4));
        this.setOnMouseClicked(GraphHome.eventDetails);
        String s=GraphHome.genName();
        while(s.length()<20)
            s+=" ";
        name.setText(s);
    }
    GeoCircle(GeoPoint c,double radius)
    {
        super(c.getCenterX(),c.getCenterY(),radius);
        GeoPoint g=c;
        v.add(g);
    }
    void init()
    {
        X=this.getCenterX()/GraphHome.gPaper.pUnit.getValue();
        Y=this.getCenterY()/GraphHome.gPaper.pUnit.getValue();
        this.centerXProperty().bind(GraphHome.gPaper.pUnit.multiply(X));
        this.centerYProperty().bind(GraphHome.gPaper.pUnit.multiply(Y));
        r=this.getRadius()/GraphHome.gPaper.pUnit.get();
        this.radiusProperty().bind(GraphHome.gPaper.pUnit.multiply(r));
        for(int i=0;i<v.size();i++)
        {
            p.add(new Point(v.get(i).getCenterX()/GraphHome.gPaper.pUnit.get(),v.get(i).getCenterY()/GraphHome.gPaper.pUnit.get()));
            v.get(i).calibrate();
        }
    }
    void calibrate()
    {
        X=this.getCenterX()/GraphHome.gPaper.pUnit.getValue();
        Y=this.getCenterY()/GraphHome.gPaper.pUnit.getValue();
        this.centerXProperty().bind(GraphHome.gPaper.pUnit.multiply(X));
        this.centerYProperty().bind(GraphHome.gPaper.pUnit.multiply(Y));
        r=this.getRadius()/GraphHome.gPaper.pUnit.get();
        this.radiusProperty().bind(GraphHome.gPaper.pUnit.multiply(r));
    }
    GeoCircle(GeoPoint a, GeoPoint b)
    {
        super();
        GeoPoint C=new GeoPoint((a.getCenterX()+b.getCenterX())/2,(a.getCenterY()+b.getCenterY())/2.0,GraphHome.gPaper.BalanceFactor);
        this.setCenterX(C.getCenterX());
        this.setCenterY(C.getCenterY());
        GeoPoint A=a;
        GeoPoint B=b;
        r=Math.sqrt((A.getCenterX()-C.getCenterX())*(A.getCenterX()-C.getCenterX())+(A.getCenterY()-C.getCenterY())*(A.getCenterY()-C.getCenterY()));
        v.add(A);
        v.add(B);
        v.add(C);
        this.setRadius(r);
    }
    void showDetails()
    {
        GraphHome.detailsBar.getChildren().clear();
        VBox par = new VBox(10);
        par.setPadding(new Insets(20,2,2,2));
        par.getStyleClass().add("details");
        Label type = new Label("Type : Circle");
        Label naMe = new Label("Name : ");
        naMe.setText(naMe.getText()+name.getText());
        Label center = new Label("Center : "+String.valueOf(X)+" "+String.valueOf(Y));
        Label Radius = new Label("Radius : "+String.valueOf(r));
        Label Area = new Label("Area : "+String.valueOf(Math.PI*r*r));
        type.prefWidthProperty().bind(par.widthProperty());
        naMe.prefWidthProperty().bind(par.widthProperty());
        center.prefWidthProperty().bind(par.widthProperty());
        Radius.prefWidthProperty().bind(par.widthProperty());
        Area.prefWidthProperty().bind(par.widthProperty());
        type.getStyleClass().add("dText");
        naMe.getStyleClass().add("dText");
        center.getStyleClass().add("dText");
        Radius.getStyleClass().add("dText");
        Area.getStyleClass().add("dText");
        
        
        par.getChildren().addAll(type,naMe,center,Radius,Area);
        GraphHome.detailsBar.getChildren().add(par);
    }
//    static void drawCircleRadius(Pane layout)
    static void drawCenterRadius(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
        GeoCircle circle = new GeoCircle();
        circle.setRadius(0);
        layout.getChildren().add(circle);
        GraphHome.temporary.add(circle);
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(!demo.independent)
                demo.flicker();
        }
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(demo.independent)
                demo.flicker();
        }
        GraphHome.vSize.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.equals(2))
                {
                        GraphHome.vSize.removeListener(this);
                        GraphHome.vSize = new SimpleIntegerProperty();
                        circle.v.add(GraphHome.clickedPoints.get(0));
                        circle.v.add(GraphHome.clickedPoints.get(1));
                        circle.v.get(0).dependent.add(circle);
                        circle.v.get(1).dependent.add(circle);
                        ((GeoPoint)(circle.v.get(0))).flicker();
                        ((GeoPoint)(circle.v.get(1))).flicker();
                        layout.setOnMouseClicked(null);
                        layout.setOnMouseMoved(GraphHome.eventCor);
                        double r = GeoMetry.distance(new Point(circle.getCenterX(),circle.getCenterY()),new Point((circle.v.get(1).getCenterX()),(circle.v.get(1).getCenterY())));
//                      
                        circle.setRadius(r);
//                        System.out.println("r is "+GraphHome.clickedPoints.get(1).getCenterX()+" "+circle.v.get(1).getCenterY());
                        for(int i=0;i<circle.v.size();i++)
                        {
                            circle.v.get(i).add(layout);
                        }
                        circle.init();
                        GraphHome.temporary.clear();
                        circle.addToStack();
                        ChangeListener centerChange = new ChangeListener<Number>(){
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                               circle.radiusProperty().unbind();
                               circle.centerXProperty().unbind();
                               circle.centerYProperty().unbind();
                               circle.setCenterX(circle.v.get(0).getCenterX());
                               circle.setCenterY(circle.v.get(0).getCenterY());
                               circle.setRadius(GeoMetry.distance(circle.v.get(1).getCenterX(),circle.v.get(1).getCenterY(),circle.getCenterX(),circle.getCenterY()));
                               circle.calibrate();
                            }
                        };
                        circle.v.get(0).centerXProperty().addListener(centerChange);
                        circle.v.get(0).centerYProperty().addListener(centerChange);
                        ChangeListener radiChange = new ChangeListener<Number>(){
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                               circle.radiusProperty().unbind();
                               circle.setRadius(GeoMetry.distance(circle.v.get(1).getCenterX(),circle.v.get(1).getCenterY(),circle.getCenterX(),circle.getCenterY()));
                               circle.calibrate();
                            }
                        };
                        circle.v.get(1).centerXProperty().addListener(centerChange);
                        circle.v.get(1).centerYProperty().addListener(centerChange);
                        GraphHome.temporary.clear();
                        if(GraphHome.oneTouch.isSelected())
                                drawCenterRadius(layout);
                        
//                        circle.v.get(0).setOnMousePressed((MouseEvent press)->{
//                               circle.radiusProperty().unbind();
//                               circle.centerXProperty().unbind();
//                               circle.centerYProperty().unbind();
//                               circle.v.get(0).centerXProperty().unbind();
//                               circle.v.get(0).centerYProperty().unbind();
//                               layout.getParent().setOnMouseDragged(null);
//                        });
//                        circle.v.get(0).setOnMouseDragged((MouseEvent drag)->{
//                               drag.consume();
//                               circle.setCenterX(drag.getX());
//                               circle.setCenterY(drag.getY());
//                               circle.v.get(0).setCenterX(drag.getX());
//                               circle.v.get(0).setCenterY(drag.getY());
//                               circle.setRadius(GeoMetry.distance(circle.v.get(1).getCenterX(),circle.v.get(1).getCenterY(),drag.getX(),drag.getY()));
//                        });
//                        circle.v.get(0).setOnMouseReleased((MouseEvent release)->{
//                            circle.p.clear();
//                            circle.init();
//                            layout.getParent().setOnMouseDragged(((GeoPane)(layout.getParent())).drag);
//                        });
//                        circle.v.get(1).setOnMousePressed((MouseEvent press)->{
//                               circle.radiusProperty().unbind();
//                               layout.getParent().setOnMouseDragged(null);
//                        });
//                        circle.v.get(1).setOnMouseDragged((MouseEvent drag)->{
//                               drag.consume();
//                               circle.v.get(1).setCenterX(drag.getX());
//                               circle.v.get(1).setCenterY(drag.getY());
//                               circle.setRadius(GeoMetry.distance(circle.v.get(0).getCenterX(),circle.v.get(0).getCenterY(),drag.getX(),drag.getY()));
//                        });
//                        circle.v.get(1).setOnMouseReleased((MouseEvent release)->{
//                            circle.p.clear();
//                            circle.init();
//                            layout.getParent().setOnMouseDragged(((GeoPane)(layout.getParent())).drag);
//                        });
                }
                else if(newValue.equals(1))
                {
                    
                    circle.setCenterX(GraphHome.clickedPoints.get(0).getCenterX());
                    circle.setCenterY(GraphHome.clickedPoints.get(0).getCenterY());
                    layout.setOnMouseMoved((MouseEvent move) -> {
                        GraphHome.showCor(move.getX(),move.getY());
                        circle.setRadius(GeoMetry.distance(new Point(circle.getCenterX(),circle.getCenterY()), new Point(move.getX(),move.getY())));
                    });
                }
               
            }
        });
        layout.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton()!=MouseButton.SECONDARY)
            {
                Point p=new Point();
                p.setX(Math.round(event.getX()/5)*5);
                p.setY(Math.round(event.getY()/5)*5);
                GeoPoint C = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
                C.add(layout);
                GraphHome.clickedPoints.add(C);
                circle.dependent.add(GraphHome.clickedPoints.lastElement());
                GraphHome.vSize.set(GraphHome.vSize.get()+1);
            }
        });
        

    }
    public static void Draw_E_P(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
        GeoCircle circle = new GeoCircle();
        circle.setRadius(0);
        layout.getChildren().add(circle);
        GraphHome.temporary.add(circle);
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(!demo.independent)
                demo.flicker();
        }
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(demo.independent)
                demo.flicker();
        }
        GraphHome.vSize.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.equals(2))
                {
                    layout.setOnMouseClicked(null);
                    layout.setOnMouseMoved(GraphHome.eventCor);
                    GraphHome.vSize.removeListener(this);
                    circle.v.addAll(GraphHome.clickedPoints);
                    circle.v.get(0).flicker();
                    circle.v.get(1).flicker();
                    circle.v.get(0).dependent.add(circle);
                    circle.v.get(1).dependent.add(circle);

                        double r = GeoMetry.distance(new Point((circle.v.get(0).getCenterX()/2+circle.v.get(1).getCenterX()/2),(circle.v.get(0).getCenterY()/2)+circle.v.get(1).getCenterY()/2),new Point(circle.v.get(0).getCenterX(),circle.v.get(0).getCenterY()));
                        
                        circle.setRadius(r);
                        for(int i=0;i<circle.v.size();i++)
                        {
                            circle.v.get(i).add(layout);
                        }
                        circle.init();
                        GraphHome.temporary.clear();
                        circle.addToStack();
                        ChangeListener change = new ChangeListener<Number>(){
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                               circle.radiusProperty().unbind();
                               circle.centerXProperty().unbind();
                               circle.centerYProperty().unbind();
                               circle.setCenterX(circle.v.get(0).getCenterX()/2+circle.v.get(1).getCenterX()/2);
                               circle.setCenterY(circle.v.get(0).getCenterY()/2+circle.v.get(1).getCenterY()/2);
                               circle.setRadius(GeoMetry.distance(circle.v.get(1).getCenterX(),circle.v.get(1).getCenterY(),circle.getCenterX(),circle.getCenterY()));
                               circle.calibrate();
                            }
                        };
                        circle.v.get(0).centerXProperty().addListener(change);
                        circle.v.get(0).centerYProperty().addListener(change);
                        circle.v.get(1).centerXProperty().addListener(change);
                        circle.v.get(1).centerYProperty().addListener(change);
                        GraphHome.temporary.clear();
                        if(GraphHome.oneTouch.isSelected())
                                Draw_E_P(layout);
                        
                }
                else if(newValue.equals(1))
                {
                    layout.setOnMouseMoved((MouseEvent move) -> {
                        GraphHome.showCor(move.getX(),move.getY());
                        circle.setCenterX(GraphHome.clickedPoints.get(0).getCenterX()/2+move.getX()/2);
                        circle.setCenterY(GraphHome.clickedPoints.get(0).getCenterY()/2+move.getY()/2);
                        circle.setRadius(GeoMetry.distance(new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY()), new Point((GraphHome.clickedPoints.get(0).getCenterX()/2+move.getX()/2),(GraphHome.clickedPoints.get(0).getCenterY()/2+move.getY()/2))));
                    });
                }
            }
        });
        layout.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton()!=MouseButton.SECONDARY)
            {
                Point p=new Point();
                p.setX(Math.round(event.getX()/5)*5);
                p.setY(Math.round(event.getY()/5)*5);
                GeoPoint C = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
                C.add(layout);
                GraphHome.clickedPoints.add(C);
                circle.dependent.add(GraphHome.clickedPoints.lastElement());
                GraphHome.temporary.add(C);
                GraphHome.vSize.set(GraphHome.vSize.get()+1);
                
            }
        });

    }
    public static void Draw_3_P(Pane layout)
    {
        {
            GraphHome.clickedPoints.clear();
            GraphHome.vSize.set(0);
            GeoCircle circle = new GeoCircle();
            circle.setRadius(0);
            layout.getChildren().add(circle);
            GraphHome.temporary.add(circle);
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(!demo.independent)
                demo.flicker();
        }
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            if(demo.independent)
                demo.flicker();
        }
            GraphHome.vSize.addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(newValue.equals(3))
                    {
                        layout.setOnMouseClicked(null);
                        layout.setOnMouseMoved(GraphHome.eventCor);
                        GraphHome.vSize.removeListener(this);
                        circle.v.addAll(GraphHome.clickedPoints);
                        circle.v.get(0).flicker();
                        circle.v.get(1).flicker();
                        circle.v.get(2).flicker();
                        circle.v.get(0).dependent.add(circle);
                        circle.v.get(1).dependent.add(circle);
                        circle.v.get(2).dependent.add(circle);

                        Circle c1 = GeoMetry.Circle3Point(new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY()),new Point(GraphHome.clickedPoints.get(1).getCenterX(),GraphHome.clickedPoints.get(1).getCenterY()),new Point(GraphHome.clickedPoints.get(2).getCenterX(),GraphHome.clickedPoints.get(2).getCenterY()));

                            circle.setRadius(c1.getRadius());
                            circle.setCenterX(c1.getCenterX());
                            circle.setCenterY(c1.getCenterY());
                            for(int i=0;i<circle.v.size();i++)
                            {
                                circle.v.get(i).add(layout);
                            }
                            circle.init();
                            
                            circle.addToStack();
                            ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                   circle.radiusProperty().unbind();
                                   circle.centerXProperty().unbind();
                                   circle.centerYProperty().unbind();
                                   Circle c1 = GeoMetry.Circle3Point(new Point(circle.v.get(0).getCenterX(),circle.v.get(0).getCenterY()),new Point(circle.v.get(1).getCenterX(),circle.v.get(1).getCenterY()),new Point(circle.v.get(2).getCenterX(),circle.v.get(2).getCenterY()));
                                   circle.setCenterX(c1.getCenterX());
                                   circle.setCenterY(c1.getCenterY());
                                   circle.setRadius(c1.getRadius());
                                   circle.calibrate();
                                }
                            };
                            circle.v.get(0).centerXProperty().addListener(change);
                            circle.v.get(0).centerYProperty().addListener(change);
                            circle.v.get(1).centerXProperty().addListener(change);
                            circle.v.get(1).centerYProperty().addListener(change);
                            circle.v.get(2).centerXProperty().addListener(change);
                            circle.v.get(2).centerYProperty().addListener(change);
                            GraphHome.temporary.clear();
                            System.out.println("temp cleared "+GraphHome.temporary.size());
                            if(GraphHome.oneTouch.isSelected())
                                Draw_3_P(layout);


                    }
                    else if(newValue.equals(2))
                    {
                        layout.setOnMouseMoved((MouseEvent move) -> {
                            GraphHome.showCor(move.getX(),move.getY());
                            
                            Circle c1 = GeoMetry.Circle3Point(new Point(GraphHome.clickedPoints.get(0).getCenterX(),GraphHome.clickedPoints.get(0).getCenterY()),new Point(GraphHome.clickedPoints.get(1).getCenterX(),GraphHome.clickedPoints.get(1).getCenterY()),new Point(move.getX(),move.getY()));
                            circle.setCenterX(c1.getCenterX());
                            circle.setCenterY(c1.getCenterY());
                            circle.setRadius(c1.getRadius());
                        });
                    }
                }
            });
            layout.setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton()!=MouseButton.SECONDARY)
                {
                    Point p=new Point();
                    p.setX(Math.round(event.getX()/5)*5);
                    p.setY(Math.round(event.getY()/5)*5);
                    GeoPoint C = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
                    C.add(layout);
                    GraphHome.clickedPoints.add(C);
                    circle.dependent.add(GraphHome.clickedPoints.lastElement());
                    GraphHome.temporary.add(C);
                    GraphHome.vSize.set(GraphHome.vSize.get()+1);
                    
                }
            });
    }
    }
        /**
     * 
     * Take input: center & radius
     * Then Draw the circle
     * 
     * @param layout of primary stage
     */
    public static void Draw_C_R(Pane layout){
        Stage window = new Stage();
        window.setTitle("Give input");
        
        Label txt1 = new Label("Center");
        Label txt2 = new Label("Radius");
        txt1.setTextFill(Color.BLUE);
        txt2.setTextFill(Color.BLUE);
        JFXTextField radius = new JFXTextField();
        window.setResizable(false);
        window.setAlwaysOnTop(true);
        radius.setPromptText("Enter Radius");
        JFXTextField x = new JFXTextField();
        x.setPromptText("Enter X");
        JFXTextField y = new JFXTextField(); 
        y.setPromptText("Enter Y");
        JFXButton submit = new JFXButton("Submit");
        submit.getStyleClass().add("submit");
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.addRow(0,txt2,radius);
        root.add(txt1, 0, 1);
        root.add(x, 1, 1);
        root.add(y, 2, 1);
        root.add(submit, 1, 2);
        root.setVgap(5);
        root.setHgap(15);
        Scene scene = new Scene(root, 420,120);
        root.getStyleClass().add("pop");
        scene.getStylesheets().add("img/style.css");
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
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
                    Point pp = new Point((new Double(X)), (new Double(Y)));
                    GeoPoint A = new GeoPoint(pp.getX()*GraphHome.gPaper.pUnit.getValue(),pp.getY()*GraphHome.gPaper.pUnit.getValue(),GraphHome.gPaper.BalanceFactor);
                    A.add(layout);
                    double rr=new Double(r);
                    GeoCircle circle = new GeoCircle();
                    for(GeoPoint demo: GraphHome.currentPoints)
                    {
                        demo.flicker();
                    }
                    circle.v.add(A);
                    circle.dependent.add(A);
                    circle.setCenterX(pp.getX()*GraphHome.gPaper.pUnit.getValue());
                    circle.setCenterY(pp.getY()*GraphHome.gPaper.pUnit.getValue());
                    circle.setRadius(rr*GraphHome.gPaper.pUnit.getValue());
                    layout.getChildren().add(circle);
                    for(int i=0;i<circle.v.size();i++)
                    {
                        circle.v.get(i).add(layout);
                    }
                    circle.init();
                    GraphHome.temporary.clear();
                    circle.addToStack();
                    //Event handler to remove it                    
                    window.close();
                }
            }
        });


    }
    void addToStack()
    {
        HBox hb = new HBox(10);
        hb.setPrefWidth(190);
        hb.setPadding(new Insets(3));
        ColorPicker cp = new ColorPicker();
        hb.getChildren().addAll(name,cp);
        hb.getStyleClass().add("stklist");
        GraphHome.stkBar.getChildren().add(hb);
        ContextMenu cMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Remove");
        MenuItem reName = new MenuItem("Rename");
        Image img = new Image("img/context_clear.jpg");
        delete.setGraphic(new ImageView(img));
        img = new Image("img/context_rename.png");
        reName.setGraphic(new ImageView(img));
        delete.getStyleClass().add("cMenu");
        reName.getStyleClass().add("cMenu");
        cp.setValue(Color.BLACK);
        cp.setPrefWidth(40);
        cp.getCustomColors().add(Color.RED);
        cp.getCustomColors().add(Color.BLUE);
        cp.getCustomColors().add(Color.GREEN);
        cp.getCustomColors().add(Color.rgb(0, 255, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 0, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 255, 0, 1));
        name.setPrefWidth(150);
        this.strokeProperty().bind(cp.valueProperty());
        cMenu.getItems().addAll(reName,delete);
        hb.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent ae)
            {
                if(ae.getButton()==MouseButton.SECONDARY)
                {
                    cMenu.show(name,Side.BOTTOM,0,0);;
                }
                else
                {
                    showDetails();
                }
            }
        });
//        up.setOnAction(new EventHandler<ActionEvent>(){
//            @Override
//            public void handle(ActionEvent ae)
//            {
//                up();
//            }
//        });
//        
//        down.setOnAction(new EventHandler<ActionEvent>(){
//            @Override
//            public void handle(ActionEvent ae)
//            {
//                down();
//            }
//        });
        
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent ae)
            {
                if(ae.getButton()==MouseButton.SECONDARY)
                {
                    GraphHome.contextCor.setLayoutX(ae.getX());
                    GraphHome.contextCor.setLayoutY(ae.getY());
                    cMenu.show(GraphHome.contextCor,Side.BOTTOM,0,0);
                    
                }
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                erase();
            }
        });
        reName.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                inputName();
            }
        });
        
        
    }
    public void inputName()
    {
        Stage window = new Stage();
        window.setTitle("Rename");
        TextField x = new TextField();
        GridPane root = new GridPane();
        Button submit = new Button("Submit");
        root.addRow(0,x);
        root.addRow(1,submit);
        Scene scene = new Scene(root,200,60);
        window.setScene(scene);
        window.show();
        
        submit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                if(x.getText().isEmpty()||x.getText().length()>20)
                {
                    System.out.println("");
                    Alert.display("Error...!","Name Must be between 1 to 20 charecters");
                }
                else
                {
                    String s = x.getText();
                    while(s.length()<20)
                        s+=" ";
                    name.setText(s);
                    window.close();
                }
            }
        });
    }
    void flicker()
    {
        Pane par = (Pane) this.getParent();
        if(par==null) return ;
        par.getChildren().remove(this);
        par.getChildren().add(this);
    }
    void erase()
    {
        Node par = this.getParent();
        if(par!=null) 
            ((Pane)par).getChildren().remove(this);
        for(int i=0;i<dependent.size();i++)
        {
            ((GeoPoint)(dependent.get(i))).remove();
            
        }
        GraphHome.stkBar.getChildren().remove(this.name.getParent());
    }
    void up()
    {
        System.out.println(this.getTranslateZ());
    }
    void down()
    {
//        this.setTranslateZ(this.getTranslateZ()-1);
    }
    
}
