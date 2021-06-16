/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.Queue;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Administrator
 */
public class HomePage {
    static GridPane home = new GridPane();
    static Image img; 
    static JFXButton graph = new JFXButton();
    static JFXButton function = new JFXButton();
    static JFXButton stat = new JFXButton();
    
    static void splash(Pane root) {
        //Load splash screen
        
        GridPane splash = new GridPane();
        splash.getStylesheets().add("img/style.css");
        splash.getStyleClass().add("splash");
        splash.minHeightProperty().bind(root.getScene().heightProperty());
        splash.minWidthProperty().bind(root.getScene().widthProperty());
        root.getChildren().setAll(splash);
        //Load splash screen with fade in effect
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), splash);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        //Finish splash with fade out effect
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), splash);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeIn.play();
        
        //After fade in, start fade out
        fadeIn.setOnFinished((e) -> {
            fadeOut.play();
        });
        
        
        //Homescreen buttons        
        //After fade out, load actual content
        
        fadeOut.setOnFinished((e) -> {
                
                load(root);
        });

       
}
    static void load(Pane root)
    {
        //Experiments using triangles
        //(100,100,700,800,1200,1700);
        
        
        
        
        
        
        
        
        
        
        
        
        //Home screen
        home.getStyleClass().add("home");
        home.setOpacity(0);
        home.minHeightProperty().bind(root.getScene().heightProperty());
        home.minWidthProperty().bind(root.getScene().widthProperty());
        FadeTransition homeIn = new FadeTransition(Duration.millis(500),home);
        homeIn.setFromValue(0);
        homeIn.setToValue(1);
        
        graph.getStyleClass().add("mainButton");
        function.getStyleClass().add("mainButton");
        stat.getStyleClass().add("mainButton");
        img = new Image("img/graph.png");
        graph.setGraphic(new ImageView (img));
        
        img = new Image("img/function.png");
        function.setGraphic(new ImageView (img));
        img = new Image("img/stat.png");
        stat.setGraphic(new ImageView (img));
        HBox menu=new HBox(10);
        menu.getChildren().addAll(graph,function,stat);
        home.getChildren().add(menu);
        home.setAlignment(Pos.CENTER);
                //Button action
       graph.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               GraphHome.show(root);
           }
       });
       
       function.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               GraphMenu.show(root);
           }
       });
       
       stat.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae){
               ChartMenu.show(root);
           }
       });
       root.getChildren().setAll(home);
       homeIn.play();
    }
}
