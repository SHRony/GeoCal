/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class Graph {
    
    Polyline polyline;
    ObservableList<Double> points;
    public static final double ZOOM = 48.0;
    
    Graph()
    {
        polyline = new Polyline();
        polyline.setFill(Color.TRANSPARENT);
        polyline.setStroke(Color.BLACK);
        polyline.setStrokeWidth(1.5);
        points = polyline.getPoints();
    }
    
    /**
     * 
     * Take input: user function for draw graph
     * 
     * @param layout of primary stage
     */
    public static void Own_graph(GeoPane layout){
        
        Stage window = new Stage();
        //window.initStyle(StageStyle.UTILITY);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setTitle("Enter your function");

        Label txt = new Label("f(x) = ");
        Label txt2 = new Label("");
        TextField function = new TextField();
        function.setText("Cos(Tan( x ))");
        Button submit = new Button("Submit");
        Label example = new Label();
        example.setText("Example:\nf(x)=(Sin(x^2))^2\nf(x)=Sin(Cos(Tan(x^2)))\nf(x)=-5*x^2-3*x/2.0+x%10\nf(x)=Cosec(x)+sec(2*x)+cot(x)-exp(x/10)\nf(x)=10\nf(x)=Log10(x+5)+Ln(x)-log2(x)\nf(x)=abs(x-5)-sqrt(x)");
        example.setTextFill(Color.RED);

        GridPane root = new GridPane();
        root.addRow(1, txt, function, submit);
        //root.addRow(3, txt2,example);
        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        window.show();


        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                
                String fun = function.getText();

                if(fun==null || fun.isEmpty())
                {
                    Alert.display("Error..!", "Please enter your function");
                }
                else
                {
                    Expression ex = new Expression(fun);
                    
                    double prex=-7499,prey=ZOOM*ex.getValue(-7499.0/ZOOM);
                    for(int x =-7500; x<=7500; x+=1)
                    {
                        double y=ex.getValue(x/ZOOM);

                        if(Double.isNaN(y)){
                            continue;
                        }
                        y*=ZOOM;
                        if(diffSign(y, prey) && Math.abs(y-prey)>100.0){
                            prex=x; prey=y;
                            continue;
                        }
                        

                        Line line = MyLine(prex, prey, x, y);
                        prex=x; prey=y;
                        layout.chld.getChildren().addAll(line);
            
                    }
                    
                    window.close();
                }
            }
        });


    }
    
    public static void Sin(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.sin(x/ZOOM);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Cos(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.cos(x/ZOOM);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Tan(GeoPane pane)
    {
        double prex=-7499,prey=ZOOM*Math.tan(-7499.0/ZOOM);
        for(int x =-7500; x<=7500; x+=1)
        {
            double y=ZOOM*Math.tan(x/ZOOM);
            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
        }
    }
    
    public static void Cot(GeoPane pane)
    {
        double prex=-7499,prey=ZOOM*(1.0/Math.tan(-7499.0/ZOOM));
        for(int x =-7500; x<=7500; x+=1)
        {
            if(Math.tan(x/ZOOM)==0.0)
            {
                continue;
            }
            double y=ZOOM*(1.0/Math.tan(x/ZOOM));

            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
            
        }
    }
    public static void Sec(GeoPane pane)
    {
        double prex=-7499,prey=ZOOM*(1.0/Math.cos(-7499.0/ZOOM));
        for(int x =-7500; x<=7500; x+=1)
        {
            if(Math.cos(x/ZOOM)==0.0)
            {
                continue;
            }
            double y=ZOOM*(1.0/Math.cos(x/ZOOM));

            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
            
        }
    }
    
    public static void Cosec(GeoPane pane)
    {
        double prex=-7499,prey=ZOOM*(1.0/Math.sin(-7499.0/ZOOM));
        for(int x =-7500; x<=7500; x+=1)
        {
            if(Math.sin(x/ZOOM)==0.0)
            {
                continue;
            }
            double y=ZOOM*(1.0/Math.sin(x/ZOOM));

            if(diffSign(y, prey)){
                prex=x; prey=y;
                continue;
            }
            
            Line line = MyLine(prex, prey, x, y);
            prex=x; prey=y;
            pane.chld.getChildren().addAll(line);
            
        }
    }
    
    public static void Square(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=x*x/ZOOM;
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Sqrt(GeoPane pane)
    {
        Graph g = new Graph();
        final double ZOOM = 30.0;
        for(int x =0; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.sqrt(x/ZOOM);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    public static void Ln(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =1; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.log(x);
            g.points.add(y);
            //System.out.println(x+"..."+y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Log10(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =1; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.log10(x);
            g.points.add(y);
            //System.out.println(x+"..."+y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Log2(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =1; x<=7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.log10(x)/Math.log10(2.0);
            g.points.add(y);
            //System.out.println(x+"..."+y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Abs(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<7500; x+=1)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.abs(x/ZOOM);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Ex(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-1500; x<=500; x++)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.exp(x/ZOOM);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static void Cubic(GeoPane pane)
    {
        Graph g = new Graph();
        for(int x =-7500; x<=7500; x++)
        {
            g.points.add(x+0.0);
            double y=ZOOM*Math.pow(x/ZOOM, 3.0);
            g.points.add(y);
        }
        pane.chld.getChildren().addAll(g.polyline);
    }
    
    public static boolean diffSign(double x, double y)
    {
        return ((x<0.0)&&(y>=0.0)) || ((y<0.0)&&(x>=0.0));
    }
    
    private static Line MyLine(double startX, double startY, double endX, double endY)
    {
        Line line = new Line(startX, startY, endX, endY);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1.15);
        //Rotate rotate = new Rotate(180, Rotate.X_AXIS);
        //line.getTransforms().add(rotate);
        return line;
    }
    
}
