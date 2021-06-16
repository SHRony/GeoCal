/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import java.util.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class GeoRay extends Line{
    Vector<GeoPoint> v = new Vector<GeoPoint>();
    Vector dependent = new Vector();
    Label name = new Label();
    double sx,sy,ex,ey;
    GeoRay()
    {
        sx=sy=ex=ey=0;
        Tooltip tt= new Tooltip("Ray");
        Tooltip.install(this, tt);
        tt.setOpacity(0.5);
        this.setOnMouseEntered(GraphHome.cursorHand);
        this.setOnMouseExited(GraphHome.cursorDefault);
        this.setOnMousePressed((MouseEvent ev)->{
            GraphHome.gPaper.getScene().setCursor(Cursor.CLOSED_HAND);
            this.setOnMouseEntered(null);
            this.setOnMouseExited(null);
        });
        
        this.setOnMouseReleased((MouseEvent ev)->{
            GraphHome.gPaper.getScene().setCursor(Cursor.HAND);
            this.setOnMouseEntered(GraphHome.cursorHand);
            this.setOnMouseExited(GraphHome.cursorDefault);
        });
        name.setText(GraphHome.genName());
        strokeWidthProperty().bind(GraphHome.gPaper.BalanceFactor.multiply(4));
    }
    void calibrate()
    {
        
        int dx=(int) (ex-sx);
        int dy=(int) (ey-sy);
        double numb=1;
//      
//        if(Math.max(dx, dy)>0.0000000000000001)
//        {
        try{
            int x=1/(Math.max(dx, dy));
            if(dx==0)
                numb=(5000-ey)/Math.abs(dy);
            else if(dy==0)
                numb=(5000-ex)/Math.abs(dx);
            else
                numb=Math.min((5000-ey)/Math.abs(dy),(5000-ex)/Math.abs(dx));
//            System.out.println(dx+" "+dy+" "+numb);
            this.setEndX(ex+(dx)*numb);
            this.setEndY(ey+(dy)*numb);
//            System.out.println("baal "+this.getEndX()+" "+this.getEndY());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
//        }
    }
    double side(Point p)
    {
        Point vec = new Point(ex-sx,ey-sy);
        return GeoMetry.cross(vec,p)-GeoMetry.cross(vec, new Point(sx,sy));
    }
    Point project(Point p)
    {
        Point vec = new Point(ex-sx,ey-sy);
       
        return GeoMetry.sub(p,GeoMetry.mul(vec.perp(),side(p)/vec.sq()));
    }
    Point reflection(Point p)
    {
        Point vec = new Point(ex-sx,ey-sy);  
        return GeoMetry.sub(p,GeoMetry.mul(vec.perp(),2*side(p)/vec.sq()));
    }
    
    void baalchaal(Pane layout)
    {
        layout.setOnMouseClicked((MouseEvent event)->{
            GraphHome.buttonPressed();
            Point p = project(new Point(event.getX(),event.getY()));
            System.out.println(event.getX()+" "+p.getX());
            GeoPoint q = new GeoPoint(p.getX(),p.getY(),GraphHome.gPaper.BalanceFactor);
            q.add(layout);
        });
    }
    public static void draw(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
        GeoRay ray = new GeoRay();
        layout.getChildren().add(ray);
        GraphHome.temporary.add(ray);
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
                    if(GraphHome.clickedPoints.get(0)==GraphHome.clickedPoints.get(1))
                    {
                        GraphHome.clickedPoints.remove(1);
                        GraphHome.vSize.set(GraphHome.vSize.get()-1);
                    }
                    else
                    {
                        layout.setOnMouseMoved(null);
                        ray.ex=(GraphHome.clickedPoints.get(1).getCenterX());
                        ray.ey=(GraphHome.clickedPoints.get(1).getCenterY());

                        ray.v.addAll(GraphHome.clickedPoints);
                        ray.v.get(0).flicker();
                        ray.v.get(1).flicker();
                        ray.v.get(1).dependent.add(ray);
                        ray.v.get(0).dependent.add(ray);
                        GraphHome.vSize.addListener(GraphHome.nothing);
                        layout.setOnMouseClicked(null);
                        ray.calibrate();
                        ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                                   ray.sx=(ray.v.get(0).getCenterX());
                                   ray.sy=(ray.v.get(0).getCenterY());
                                   ray.ex=(ray.v.get(1).getCenterX());
                                   ray.ey=(ray.v.get(1).getCenterY());
                                   ray.calibrate();
                                }
                            };

                        ray.v.get(0).centerXProperty().addListener(change);
                        ray.v.get(0).centerYProperty().addListener(change);
                        ray.v.get(1).centerYProperty().addListener(change);
                        ray.v.get(1).centerXProperty().addListener(change);
                        GraphHome.temporary.clear();
                        GraphHome.buttonPressed();
                        ray.addToStack();

                        if(GraphHome.oneTouch.isSelected())
                        draw(layout);
                    }
                    
                }
                else if(newValue.equals(1))
                {
                    ray.ex=ray.sx=GraphHome.clickedPoints.get(0).getCenterX();
                    ray.ex=ray.sy=(GraphHome.clickedPoints.get(0).getCenterY());
                    ray.startXProperty().bind(GraphHome.clickedPoints.get(0).centerXProperty());
                    ray.startYProperty().bind(GraphHome.clickedPoints.get(0).centerYProperty());
                    
                    layout.setOnMouseMoved((MouseEvent move) -> {
                        GraphHome.showCor(move.getX(),move.getY());
                        ray.ex=move.getX();
                        ray.ey=move.getY();
                        ray.calibrate();
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
                    GraphHome.temporary.add(C);
                    GraphHome.clickedPoints.add(C);
                    ray.dependent.add(GraphHome.clickedPoints.lastElement());
                    GraphHome.vSize.set(GraphHome.vSize.get()+1);
                    
                }
        });
        
    }
    void showDetails()
    {
        Vector<Node> temp = new Vector<Node>();
        for (Node component : GraphHome.detailsBar.getChildren()) {
            temp.add(component);
        }
        for(Node component:temp)
        GraphHome.detailsBar.getChildren().remove(component);
        GraphHome.detailsBar.getChildren().add(new Label("Segment: "+name.getText()));
        GraphHome.detailsBar.getChildren().add(new Label("Start:"));
        GraphHome.detailsBar.getChildren().add(new Label(String.valueOf(sx)+" "+String.valueOf(sy)));
        GraphHome.detailsBar.getChildren().add(new Label("End:"));
        GraphHome.detailsBar.getChildren().add(new Label(String.valueOf(ex)+" "+String.valueOf(ey)));
        
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
        MenuItem baal = new MenuItem("Baalchaal");
        Image img = new Image("img/context_clear.jpg");
        delete.setGraphic(new ImageView(img));
        img = new Image("img/context_rename.png");
        reName.setGraphic(new ImageView(img));
        delete.getStyleClass().add("cMenu");
        reName.getStyleClass().add("cMenu");
        baal.getStyleClass().add("cMenu");
        cp.setPrefWidth(40);
        cp.setValue(Color.BLACK);
        cp.getCustomColors().add(Color.RED);
        cp.getCustomColors().add(Color.BLUE);
        cp.getCustomColors().add(Color.GREEN);
        cp.getCustomColors().add(Color.rgb(0, 255, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 0, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 255, 0, 1));
        name.setPrefWidth(150);
        this.strokeProperty().bind(cp.valueProperty());
        this.strokeProperty().bind(cp.valueProperty());
        cMenu.getItems().addAll(reName,delete,baal);
        hb.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent ae)
            {
                if(ae.getButton()==MouseButton.SECONDARY)
                {
                    cMenu.show(name,Side.BOTTOM,0,0);
                    
                }
                else
                {
                    showDetails();
                }
            }
        });

        
        this.setOnMouseClicked((MouseEvent ev)->{
            if(ev.getButton()==MouseButton.PRIMARY)
            {
                GraphHome.clickedLines.add(this);
                GraphHome.lSize.unbind();
                GraphHome.lSize.set(GraphHome.lSize.get()+1);
                ev.consume();
            }
            else
            {
                GraphHome.contextCor.setLayoutX(ev.getX());
                GraphHome.contextCor.setLayoutY(ev.getY());
                cMenu.show(GraphHome.contextCor,Side.BOTTOM,0,0);
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
        baal.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                baalchaal(GraphHome.gPaper.chld);
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
                }
                else
                {
                    String s = x.getText();
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
            if(dependent.get(i) instanceof GeoPoint)
            ((GeoPoint)(dependent.get(i))).remove();
        }
        GraphHome.stkBar.getChildren().remove(this.name.getParent());
    }
}
