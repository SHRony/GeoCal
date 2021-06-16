/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import java.util.Vector;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author pc
 */
public class GraphMenu {
    public static Vector currentFunctions = new Vector();
    static GeoPane layout = new GeoPane();
    static BorderPane container=new BorderPane();
    static VBox lftBar= new VBox(5);
    public static void init(Pane root){
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.chld.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox ownGraph = new HBox();
        Label ownLabel=new Label("f(x)= ");
        JFXButton ownBtn= new JFXButton("Draw");
        TextField ownField= new TextField();
        ownGraph.getChildren().addAll(ownLabel,ownField,ownBtn);
        ownField.setText("Cos (tan(x )  )");
        JFXButton sinx=new JFXButton("sin(x)");
        JFXButton cosx= new JFXButton("cos(x)");
        JFXButton tanx= new JFXButton("tan(x)");
        JFXButton cotx= new JFXButton("cot(x)");
        JFXButton secx= new JFXButton("sec(x)");
        JFXButton cosecx= new JFXButton("cosec(x)");
        JFXButton log2= new JFXButton("log2(x)");
        JFXButton log10= new JFXButton("log10(x)");
        JFXButton ln= new JFXButton("ln(x)");
        JFXButton sqr= new JFXButton("x^2");
        JFXButton sqrt= new JFXButton("sqrt(x)");
        JFXButton cube= new JFXButton("x^3");
        JFXButton abs= new JFXButton("abs(x)");
        JFXButton exp= new JFXButton("e^x");     
        JFXButton clear= new JFXButton("Clear");     
        JFXButton exit= new JFXButton("Exit");     
        prepareBtn(sinx);
        prepareBtn(cosx);
        prepareBtn(tanx);
        prepareBtn(cotx);
        prepareBtn(secx);
        prepareBtn(cosecx);
        prepareBtn(log2);
        prepareBtn(log10);
        prepareBtn(ln);
        prepareBtn(sqr);
        prepareBtn(sqrt);
        prepareBtn(cube);
        prepareBtn(abs);
        prepareBtn(exp);
        prepareBtn(clear);
        prepareBtn(exit);
        lftBar.getChildren().addAll(ownGraph,sinx,cosx,tanx,cotx,secx,cosecx,log2,log10,ln,sqr,sqrt,cube,abs,exp,clear,exit);
        ownBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Graph.Own_graph(layout,ownField.getText());
            }
        });
        sinx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("sin(x)");
            }
        });
        cosx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("cos(x)");
            }
        });
        tanx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("tan(x)");
            }
        });
        cotx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("cot(x)");;
            }
        });
        secx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("sec(x)");
            }
        });
        cosecx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("cosec(x)");
            }
        });
        sqr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("x^2");
            }
        });
        sqrt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("sqrt(x)");
            }
        });
        cube.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("x^3");
            }
        });
        log2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("log2(x)");
            }
        });
        log10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("log10(x)");
            }
        });
        ln.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("ln(x)");
            }
        });
        abs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("abs(x)");
            }
        });
        exp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ownField.setText("exp(x)");
            }
        });
         clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for(int i=0;i<currentFunctions.size();i++){
                    layout.chld.getChildren().remove(currentFunctions.get(i));
                }
                currentFunctions.clear();
            }
        });
         exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
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
        container.setCenter(layout);
        container.setLeft(lftBar);
        lftBar.getStyleClass().add("sideBar");
    }
    static void prepareBtn(JFXButton btn){
        btn.prefWidthProperty().bind(lftBar.widthProperty());
    }
    public static void show(Pane root){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500),container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        root.getChildren().setAll(container);
    }
}
