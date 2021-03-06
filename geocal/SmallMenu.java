package geocal;
import static geocal.GeoCircle.CircleMap;
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
    public MenuItem delete, rename, inward, circled,perp,editHeight,editWidth,showhull,hidehull, editLength, editVertices,showR,hideR,rotate;
    
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
        
        lbl.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent Event) {
                if (!Event.isPrimaryButtonDown()) {
                    lbl.getScene().setCursor(Cursor.HAND);
                }
            }
        });
        lbl.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent Event) {
                if (!Event.isPrimaryButtonDown()) {
                    lbl.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
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
        lbl.setDisable(true);
    }
    public void ShowLabel()
    {    
        this.lbl.setText(name);
        this.lbl.setTextFill(Color.BROWN);
        this.lbl.setDisable(false);
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
        showhull=new MenuItem("Show Convex Hull");
        hidehull=new MenuItem("Hide Convex Hull");
        editLength = new MenuItem("Edit Length of Side");
        editVertices = new MenuItem("Edit no of Vertices");
        this.list.getItems().addAll(showhull,hidehull,editLength, editVertices);

    }
    
    //Menu Item adding for Line
    public void addForLine()
    {
        perp=new MenuItem("Perpendicular Line through Selected Point");
        this.list.getItems().add(perp);
    }
    
    //Menu Item adding for Line
    public void addForVector()
    {
        showR=new MenuItem("Show Resultant");
        hideR=new MenuItem("Hide Resultant");
        rotate=new MenuItem("Rotate");
        this.list.getItems().add(showR);
        this.list.getItems().add(hideR);
        this.list.getItems().add(rotate);
    }
    //Menu Item adding for Ellipse
    public void addForEllipse()
    {
        rename = new MenuItem("Rename");
        this.list.getItems().add(rename);
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
                   GeoRect.Delete(rect);
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
        p.hull.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {
                    p.menu.list.show(p.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        //Action setup    
        
        //Sow Convex hull
        
        p.menu.showhull.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                p.showHull(((Pane)p.poly.getParent()));
                p.hull.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton()==MouseButton.SECONDARY)
                    {
                        p.menu.list.show(p.menu.lbl, Side.BOTTOM, 0, 0);
                    }
                }
            });
            }
        });
        //Hide Convex Hull
        p.menu.hidehull.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                p.hideHull(((Pane)p.poly.getParent()));
                
            }
        });
        //Delete it
        p.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoPoly.Delete(p, false);
            }
        });
        //Edit Length of Regular Polygon
        p.menu.editLength.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 GeoPoly.EditLength(p);
            }
        });
        //Edit No of Vertices of regular Polygon
        p.menu.editVertices.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 GeoPoly.EditVertices(p);
            }
        });
    }
    
    //Menu setting for Line
    static public void menuSet(GeoLine l)
    {
        //show menu
        
        l.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {   
//                    l.menu.lbl.setLayoutY(event.getY());
//                    l.menu.lbl.setLayoutX(event.getX());
                    l.menu.list.show(l.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        l.menu.delete.setOnAction((ActionEvent e) -> {
            l.remove();
        });
        l.menu.perp.setOnAction((ActionEvent e) -> {
            MainMenu.move=false;
            e.consume();
            GeoLine.drawPerp((Pane) l.getParent(),l);
        });
        
    }
    //Menu setting for Vector
    static public void menuSet(GeoVector l)
    {
        //show menu
        
        l.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {   
//                    l.menu.lbl.setLayoutY(event.getY());
//                    l.menu.lbl.setLayoutX(event.getX());
                    l.menu.list.show(l.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        l.menu.delete.setOnAction((ActionEvent e) -> {
            l.remove();
        });
        l.menu.showR.setOnAction((ActionEvent e) -> {
            GeoVector.showRes((Pane) l.getParent());
            
            e.consume();
        });
        
        l.menu.hideR.setOnAction((ActionEvent e) -> {
            GeoVector.hideRes((Pane) l.getParent());
            e.consume();
        });
        l.menu.rotate.setOnAction(( ActionEvent  ev) -> {
            l.rotate();
        });
        
        
    }
    
    /**
     * Menu setting for Ellipse 
     */
    public static void menuSet(GeoEllipse ellipse)
    {
        //Menu showing
        ellipse.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY)
                {
                    ellipse.menu.list.show(ellipse.menu.lbl, Side.BOTTOM, 0, 0);
                }
            }
        });
        //Action setup            
        ellipse.menu.rename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ellipse.menu.Rename();
            }
        });
        ellipse.menu.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GeoEllipse.Delete(ellipse);
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
