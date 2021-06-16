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
public class GeoSegment extends Line{
    double sx,sy,ex,ey;
    Vector dependent = new Vector();
    Label name = new Label();
    Vector<GeoPoint> v = new Vector<GeoPoint>();
    GeoSegment()
    {
        
        super();
        Tooltip tt= new Tooltip("Segment");
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
        sx=sy=ex=ey=0;
        String s=GraphHome.genName();
        name.setText(s);
        strokeWidthProperty().bind(GraphHome.gPaper.BalanceFactor.multiply(4));
    }
    void init()
    {
        
        try{
            sx=getStartX()/GraphHome.gPaper.pUnit.get();
            sy=getStartY()/GraphHome.gPaper.pUnit.get();
            ex=getEndX()/GraphHome.gPaper.pUnit.get();
            ey=getEndY()/GraphHome.gPaper.pUnit.get();
//            startXProperty().bind(GraphHome.gPaper.pUnit.multiply(sx));
//            startYProperty().bind(GraphHome.gPaper.pUnit.multiply(sy));
//            endXProperty().bind(GraphHome.gPaper.pUnit.multiply(ex));
//            endYProperty().bind(GraphHome.gPaper.pUnit.multiply(ey));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    double side(Point p)
    {
        Point vec = new Point(this.getEndX()-this.getStartX(),this.getEndY()-this.getStartY());
        return GeoMetry.cross(vec,p)-GeoMetry.cross(vec, new Point(getStartX(),getStartY()));
    }
    Point project(Point p)
    {
        Point vec = new Point(this.getEndX()-this.getStartX(),this.getEndY()-this.getStartY());
        return GeoMetry.sub(p,GeoMetry.mul(vec.perp(),side(p)/vec.sq()));
    }
    Point reflection(Point p)
    {
        Point vec = new Point(this.getEndX()-this.getStartX(),this.getEndY()-this.getStartY());
        return GeoMetry.sub(p,GeoMetry.mul(vec.perp(),2*side(p)/vec.sq()));
    }
    public static void draw(Pane layout)
    {
        GraphHome.clickedPoints.clear();
        GraphHome.vSize.set(0);
        GeoSegment seg = new GeoSegment();
        layout.getChildren().add(seg);
        GraphHome.temporary.add(seg);
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
                    GraphHome.temporary.clear();
                    if(GraphHome.clickedPoints.get(0)==GraphHome.clickedPoints.get(1))
                    {
                        GraphHome.clickedPoints.remove(1);
                        GraphHome.vSize.set(GraphHome.vSize.get()-1);
                    }
                    else
                    {
                        layout.setOnMouseMoved(null);
                        
                        seg.setEndX(GraphHome.clickedPoints.get(1).getCenterX());
                        seg.setEndY(GraphHome.clickedPoints.get(1).getCenterY());
                        seg.v.addAll(GraphHome.clickedPoints);
                        seg.v.get(0).flicker();
                        seg.v.get(1).flicker();
                        seg.v.get(1).dependent.add(seg);
                        seg.v.get(0).dependent.add(seg);
                        GraphHome.vSize.addListener(GraphHome.nothing);
                        layout.setOnMouseClicked(null);
                        seg.init();
                        ;
    //                    ChangeListener change = new ChangeListener<Number>(){
    //                            @Override
    //                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
    //                               seg.startXProperty().unbind();
    //                               seg.startYProperty().unbind();
    //                               seg.endXProperty().unbind();
    //                               seg.endYProperty().unbind();
    //                               seg.setStartX(seg.v.get(0).getCenterX());
    //                               seg.setStartY(seg.v.get(0).getCenterY());
    //                               seg.setEndX(seg.v.get(1).getCenterX());
    //                               seg.setEndY(seg.v.get(1).getCenterY());
    //                               seg.init();
    //                            }
    //                        };
    //                    
    //                    seg.v.get(0).centerXProperty().addListener(change);
    //                    seg.v.get(0).centerYProperty().addListener(change);
    //                    seg.v.get(1).centerYProperty().addListener(change);
    //                    seg.v.get(1).centerXProperty().addListener(change);
                        seg.startXProperty().bind(seg.v.get(0).centerXProperty());
                        seg.startYProperty().bind(seg.v.get(0).centerYProperty());
                        seg.endXProperty().bind(seg.v.get(1).centerXProperty());
                        seg.endYProperty().bind(seg.v.get(1).centerYProperty());
                        
                        GraphHome.buttonPressed();
                        seg.addToStack();
                        if(GraphHome.oneTouch.isSelected())
                        draw(layout);
                    }
                    
                }
                else if(oldValue.equals(0))
                {
                    seg.setStartX(GraphHome.clickedPoints.get(0).getCenterX());
                    seg.setStartY(GraphHome.clickedPoints.get(0).getCenterY());
                    seg.setEndX(GraphHome.clickedPoints.get(0).getCenterX());
                    seg.setEndY(GraphHome.clickedPoints.get(0).getCenterY());
                    
                    layout.setOnMouseMoved((MouseEvent move) -> {
                        GraphHome.showCor(move.getX(),move.getY());
                        double dx,dy;
                        dx=move.getX()-GraphHome.clickedPoints.get(0).getCenterX();
                        dy=move.getY()-GraphHome.clickedPoints.get(0).getCenterY();
                        double theta = Math.atan2(dy,dx);
                        double dis = Math.sqrt((dx*dx)+(dy*dy));
                        dis=(dis-5)/dis;
                        seg.setEndX(GraphHome.clickedPoints.get(0).getCenterX()+dx*dis);
                        seg.setEndY(GraphHome.clickedPoints.get(0).getCenterY()+dy*dis);
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
                    GraphHome.temporary.add(C);
                    GraphHome.vSize.set(GraphHome.vSize.get()+1);
                    seg.dependent.add(C);
                    
                    
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
        MenuItem biSector = new MenuItem("Bisector");
        Image img = new Image("img/context_clear.jpg");
        delete.setGraphic(new ImageView(img));
        img = new Image("img/context_rename.png");
        reName.setGraphic(new ImageView(img));
        delete.getStyleClass().add("cMenu");
        reName.getStyleClass().add("cMenu");
        biSector.getStyleClass().add("cMenu");
        cp.setValue(Color.BLACK);
        cp.getCustomColors().add(Color.RED);
        cp.getCustomColors().add(Color.BLUE);
        cp.getCustomColors().add(Color.GREEN);
        cp.getCustomColors().add(Color.rgb(0, 255, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 0, 255, 1));
        cp.getCustomColors().add(Color.rgb(255, 255, 0, 1));
        cp.setPrefWidth(40);
        name.setPrefWidth(150);
        this.strokeProperty().bind(cp.valueProperty());
        this.strokeProperty().bind(cp.valueProperty());
        cMenu.getItems().addAll(reName,delete,biSector);
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

        
        this.setOnMouseClicked((MouseEvent ev)->{
            if(ev.getButton()==MouseButton.PRIMARY)
            {
                if(GraphHome.lineAllowed)
                {
                    GraphHome.clickedLines.add(this);
                    GraphHome.lSize.unbind();
                    GraphHome.lSize.set(GraphHome.lSize.get()+1);
                    ev.consume();
                }
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
        biSector.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ae)
            {
                addPerpBisector();
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
    void addPerpBisector()
    {
        GeoLine line= new GeoLine();
        double dx=ex-sx;
        double dy=ey-sy;
        GeoPoint P = new GeoPoint((this.v.get(0).getCenterX()+this.v.get(1).getCenterX())/2,(this.v.get(0).getCenterY()+this.v.get(1).getCenterY())/2,GraphHome.gPaper.BalanceFactor);
//        P.centerXProperty().bind(this.v.get(0).centerXProperty().divide(2).add(v.get(1).centerXProperty()).divide(2));
//        P.centerYProperty().bind(this.v.get(0).centerXProperty().divide(2).add(v.get(1).centerXProperty()).divide(2));
        P.add(GraphHome.gPaper.chld);
        P.centerXProperty().unbind();
        P.centerYProperty().unbind();
        P.setOnMouseDragged(null);
        line.sx=(P.getCenterX());
                line.sy=(P.getCenterY());
                line.ex=(P.getCenterX()-dy);
                line.ey=(P.getCenterY()+dx);
                line.calibrate();
        ChangeListener change = new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                line.sx=(P.getCenterX());
                line.sy=(P.getCenterY());
                double dy=v.get(0).getCenterY()-v.get(1).getCenterY();
                double dx=v.get(0).getCenterX()-v.get(1).getCenterX();
                line.ex=(P.getCenterX()-dy);
                line.ey=(P.getCenterY()+dx);
                line.calibrate();
            }
        };
        
        ChangeListener change2 =new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    P.centerXProperty().unbind();
                    P.centerYProperty().unbind();
                    P.setCenterX(v.get(0).getCenterX()/2+v.get(1).getCenterX()/2);
                    P.setCenterY(v.get(0).getCenterY()/2+v.get(1).getCenterY()/2);
            }
        };
        v.get(0).centerXProperty().addListener(change2);
        v.get(0).centerYProperty().addListener(change2);
        v.get(1).centerXProperty().addListener(change2);
        v.get(1).centerYProperty().addListener(change2);
        P.centerXProperty().addListener(change);
        P.centerYProperty().addListener(change);
        
        
        GraphHome.gPaper.chld.getChildren().add(line);
        line.addToStack();
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            demo.flicker();
        }
        
    }
    void addPerp()
    {
        GeoLine line= new GeoLine();
        double dx=ex-sx;
        double dy=ey-sy;
        GeoPoint P = new GeoPoint((this.v.get(0).getCenterX()+this.v.get(1).getCenterX())/2,(this.v.get(0).getCenterY()+this.v.get(1).getCenterY())/2,GraphHome.gPaper.BalanceFactor);
//        P.centerXProperty().bind(this.v.get(0).centerXProperty().divide(2).add(v.get(1).centerXProperty()).divide(2));
//        P.centerYProperty().bind(this.v.get(0).centerXProperty().divide(2).add(v.get(1).centerXProperty()).divide(2));
        P.add(GraphHome.gPaper.chld);
        P.centerXProperty().unbind();
        P.centerYProperty().unbind();
        P.setOnMouseDragged(null);
        line.sx=(P.getCenterX());
                line.sy=(P.getCenterY());
                line.ex=(P.getCenterX()-dy);
                line.ey=(P.getCenterY()+dx);
                line.calibrate();
        ChangeListener change = new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                line.sx=(P.getCenterX());
                line.sy=(P.getCenterY());
                double dy=v.get(0).getCenterY()-v.get(1).getCenterY();
                double dx=v.get(0).getCenterX()-v.get(1).getCenterX();
                line.ex=(P.getCenterX()-dy);
                line.ey=(P.getCenterY()+dx);
                line.calibrate();
            }
        };
        
        ChangeListener change2 =new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    P.centerXProperty().unbind();
                    P.centerYProperty().unbind();
                    P.setCenterX(v.get(0).getCenterX()/2+v.get(1).getCenterX()/2);
                    P.setCenterY(v.get(0).getCenterY()/2+v.get(1).getCenterY()/2);
            }
        };
        v.get(0).centerXProperty().addListener(change2);
        v.get(0).centerYProperty().addListener(change2);
        v.get(1).centerXProperty().addListener(change2);
        v.get(1).centerYProperty().addListener(change2);
        P.centerXProperty().addListener(change);
        P.centerYProperty().addListener(change);
        
        
        GraphHome.gPaper.chld.getChildren().add(line);
        line.addToStack();
        for(GeoPoint demo: GraphHome.currentPoints)
        {
            demo.flicker();
        }
        
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
