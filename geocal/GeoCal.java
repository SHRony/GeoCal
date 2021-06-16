/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class GeoCal extends Application {
    static Pane root = new Pane();
    public static Scene scene;
    @Override
    
    public void start(Stage primaryStage) {
        scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add("img/style.css");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(700);
        //INIT
        GraphHome.init(root);
        HomePage.splash(root);
        ChartMenu.init(root);
        GraphMenu.init(root);
        primaryStage.setTitle("GeoCal");
        GraphHome.gPaper.chld.getChildren().add(GraphHome.contextCor);//Add mouse cordinates to sidebar
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
