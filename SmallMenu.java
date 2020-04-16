/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;
import static geocal.GeoCircle.CircleMap;
import static geocal.GeoPoly.PolyMap;
import static geocal.GeoRect.RectMap;
import static geocal.Triangle.TriangleMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
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
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class SmallMenu {
    
    private static int cnt=1;
    private static char ch ='A';
    private String name;
    public Label lbl = new Label();
    
    final public ContextMenu list;
    public MenuItem delete, rename, inward, circled, editHeight, editWidth;

    public SmallMenu()
    {
        name=String.valueOf(ch).concat(String.valueOf(cnt));
        lbl.setText(name);
        lbl.setTextFill(Color.BROWN);
        increase();
        list = new ContextMenu();
        delete = new MenuItem("Delete");
        list.getItems().addAll(delete); //Delete for all
        lbl.setContextMenu(list);
        Rotate rotate = new Rotate(180, Rotate.X_AXIS);
        lbl.getTransforms().add(rotate);
    }
    public SmallMenu(double x, double y)
    {
        this();
        set(x,y);
    }
    public SmallMenu(Point p)
    {
        this(p.getX(), p.getY());
    }
    
    public void set(double x, double y)
    {
        lbl.setLayoutX(x);
        lbl.setLayoutY(y);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        lbl.setText(name);
    }

    public Label getLabel() {
        return lbl;
    }   
    public void HideLabel()
    {
        lbl.setText("");
        name="";
        decrease();
        lbl.setDisable(true);
    }
    private void increase()
    {
        ch++;
        if(ch>'Z')
        {
            ch='A';
            cnt++;
        }
    }
    private void decrease()
    {
        ch--;
        if(ch<'A')
        {
            ch='Z';
            cnt--;
        }
    }
    
    //Menu Item adding for Circle
    public void addForCircle()
    {
        rename = new MenuItem("Rename");
        this.list.getItems().add(rename);
    }
    //Menu Item adding for Triangle
    public void addForTriangle()
    {
        circled = new MenuItem("Circled of Triangle");
        inward = new MenuItem("Inward of Triangle");
        this.list.getItems().addAll(circled,inward);
        this.HideLabel();
    }
    //Menu Item adding for Rectangle
    public void addForRectangle()
    {
        this.HideLabel();
        editHeight = new MenuItem("Edit Height");
        editWidth = new MenuItem("Edit Width");
        this.list.getItems().addAll(editHeight, editWidth);
    }
    //Menu Item adding for PlyGon
    public void addForPolygon()
    {
        this.HideLabel();
        
    }
    
    /**
     * Menu setting for GeoCircle
     * @param circle 
     */
    public static void menuSet(GeoCircle circle)
    {
        //Menu showing
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {
                    circle.menu.list.show(circle.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        //Action setup            
        circle.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                circle.menu.Rename();
            }
        });
        circle.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                circle.ids.forEach((Integer x) -> {
                    ((Pane)circle.getParent()).getChildren().remove((CircleMap.get(x)).menu.getLabel());
                    ((Pane)circle.getParent()).getChildren().remove(CircleMap.get(x));
                });
                ((Pane)circle.getParent()).getChildren().remove(circle.menu.getLabel());
                ((Pane)circle.getParent()).getChildren().remove(circle);
            }
        });
    }
    
    /**
     * Menu setting for Triangle
     * @param tri 
     */
    public static void menuSet(Triangle tri)
    {   
        //Menu showing
        tri.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {
                    tri.menu.list.show(tri.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        
        //action setup
        tri.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                tri.ids.forEach((Integer x) -> {
                    ((Pane)tri.getParent()).getChildren().remove((TriangleMap.get(x)).menu.getLabel());
                    ((Pane)tri.getParent()).getChildren().remove(TriangleMap.get(x));
                });
                ((Pane)tri.getParent()).getChildren().remove(tri.menu.getLabel());
                ((Pane)tri.getParent()).getChildren().remove(tri);
            }
        });
        
        tri.menu.circled.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
           
                //System.out.println("Init vis:"+tri.out.isVisible()+" ,dis:"+tri.out.isDisable());
                if(tri.out.isVisible())
                {
                    tri.out.setDisable(true);
                    tri.out.setVisible(false);
                }
                else
                {
                    tri.out.setDisable(false);
                    tri.out.setVisible(true);
                }
                //System.out.println("Present vis:"+tri.out.isVisible()+" ,dis:"+tri.out.isDisable());
            }
        });
        tri.menu.inward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
           
                //System.out.println("Init (in) vis:"+tri.in.isVisible()+" ,dis:"+tri.in.isDisable());
                if(tri.in.isVisible())
                {
                    tri.in.setDisable(true);
                    tri.in.setVisible(false);
                }
                else
                {
                    tri.in.setDisable(false);
                    tri.in.setVisible(true);
                }
                //System.out.println("Present (in) vis:"+tri.in.isVisible()+" ,dis:"+tri.in.isDisable());
            }
        });
    }
    
    /**
     * Menu setting for Rectangle 
     * @param rect
     */
    public static void menuSet(GeoRect rect)
    {
        //Menu showing
        rect.poly.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {
                    rect.menu.list.show(rect.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        //Action setup            
        rect.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                rect.ids.forEach((Integer x) -> {
                    ((Pane)rect.poly.getParent()).getChildren().remove((RectMap.get(x)).menu.getLabel());
                    ((Pane)rect.poly.getParent()).getChildren().remove(RectMap.get(x));
                });
                ((Pane)rect.poly.getParent()).getChildren().remove(rect.menu.getLabel());
                ((Pane)rect.poly.getParent()).getChildren().remove(rect.poly);
            }
        });
        rect.menu.editHeight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoRect.EditHeight(rect);
            }
        });
        rect.menu.editWidth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoRect.EditWidth(rect);
            }
        });
    }
    
    /**
     * Menu setting for PolyGon
     * @param p 
     */
    public static void menuSet(GeoPoly p)
    {
        //Menu showing
        p.poly.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {
                    p.menu.list.show(p.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        //Action setup            
        p.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                p.ids.forEach((Integer x) -> {
                    ((Pane)p.poly.getParent()).getChildren().remove((PolyMap.get(x)).menu.getLabel());
                    ((Pane)p.poly.getParent()).getChildren().remove(PolyMap.get(x));
                });
                //((Pane)p.poly.getParent()).getChildren().remove(p.menu.getLabel());
                ((Pane)p.poly.getParent()).getChildren().remove(p.poly);
                p.points.clear();
                p.ids.clear();
                p.poly=new Polygon();
            }
        });
    }
    
    
    /**
     * Rename Label
     */
    public void Rename()
    {
        Stage window = new Stage();
        window.setTitle("Rename");
        
        Label txt1 = new Label("New Name");
        TextField input = new TextField();
        input.setPromptText("Enter new name");
        input.setText(name);
        Button submit = new Button("Submit");

        GridPane root = new GridPane();
        root.addRow(1,txt1,input);
        root.addRow(2,submit);

        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        window.show();
        
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String s = input.getText();
                setName(s);
                window.close();
                //((Node)(e.getSource())).getScene().getWindow().hide();
                //window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        });
        
    }
}
