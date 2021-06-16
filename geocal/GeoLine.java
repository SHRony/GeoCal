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
public class GeoLine extends Line{
    Vector<GeoPoint> v = new Vector<GeoPoint>();
    Vector dependent = new Vector();
    Label name = new Label();
    double sx,sy,ex,ey;
    GeoLine()
    {
        sx=sy=ex=ey=0;
        Tooltip tt= new Tooltip("Line");
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
                numb=(5000-ey)/dy;
            else if(dy==0)
                numb=(5000-ex)/dx;
            else
                numb=Math.min((5000-ey)/dy,(5000-ex)/dx);
            this.setEndX(ex+(dx)*numb);
            this.setEndY(ey+(dy)*numb);
            numb=1;
            if(dy==0)
                numb=(sx+5000)/dx;
            else if(dx==0)
                numb=(sy+5000)/dy;
            else
                numb=Math.min((5000+sy)/dy,(5000+sx)/dx);
            this.setStartX(sx-dx*numb);
            this.setStartY(sy-dy*numb);
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
        GeoLine line = new GeoLine();
        layout.getChildren().add(line);
        GraphHome.temporary.add(line);
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

                        line.ex=(GraphHome.clickedPoints.get(1).getCenterX());
                        line.ey=(GraphHome.clickedPoints.get(1).getCenterY());

                        line.v.addAll(GraphHome.clickedPoints);
                        line.v.get(0).flicker();
                        line.v.get(1).flicker();
                        line.v.get(1).dependent.add(line);
                        line.v.get(0).dependent.add(line);
                        GraphHome.vSize.addListener(GraphHome.nothing);
                        layout.setOnMouseClicked(null);
                        line.calibrate();
                        ChangeListener change = new ChangeListener<Number>(){
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                                   line.sx=(line.v.get(0).getCenterX());
                                   line.sy=(line.v.get(0).getCenterY());
                                   line.ex=(line.v.get(1).getCenterX());
                                   line.ey=(line.v.get(1).getCenterY());
                                   line.calibrate();
                                }
                            };

                        line.v.get(0).centerXProperty().addListener(change);
                        line.v.get(0).centerYProperty().addListener(change);
                        line.v.get(1).centerYProperty().addListener(change);
                        line.v.get(1).centerXProperty().addListener(change);
                        GraphHome.temporary.clear();
                        GraphHome.buttonPressed();
                        line.addToStack();
                        if(GraphHome.oneTouch.isSelected())
                        draw(layout);
                    }
                    
                }
                else if(newValue.equals(1))
                {
                    line.ex=line.sx=GraphHome.clickedPoints.get(0).getCenterX();
                    line.ex=line.sy=(GraphHome.clickedPoints.get(0).getCenterY());
                    
                    layout.setOnMouseMoved((MouseEvent move) -> {
                        GraphHome.showCor(move.getX(),move.getY());
                        line.ex=move.getX();
                        line.ey=move.getY();
                        line.calibrate();
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
                    line.dependent.add(GraphHome.clickedPoints.lastElement());
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
                    Alert.display("Error...!","Name Must be between 1 to 20 charecters");
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
