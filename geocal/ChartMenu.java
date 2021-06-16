package geocal;

import com.jfoenix.controls.JFXButton;
import static geocal.GraphHome.topContainer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Lenovo
 */
public class ChartMenu{
 
    static Pane layout = new Pane();
    static BorderPane container=new BorderPane();
    static VBox topContainer= new VBox(10);
    static HBox topBar = new HBox(25);
    static HBox secondBar = new HBox(25);
   
        public static void init(Pane root){
        JFXButton home = new JFXButton();
        Image imgHome = new Image("img/home.png");
        home.setGraphic(new ImageView (imgHome));
        
        JFXButton pie = new JFXButton();
        Image imgPie = new Image("img/pie_chart.png");
        pie.setGraphic(new ImageView (imgPie));
        MenuItem delete = new MenuItem("Delete Chart");
        
        
        
        
        JFXButton line = new JFXButton();
        Image imgLine = new Image("img/line_chart.png");
        line.setGraphic(new ImageView (imgLine));
        
        JFXButton bar = new JFXButton();
        Image imgBar = new Image("img/bar_chart.png");
        bar.setGraphic(new ImageView (imgBar));
        
        topBar.getChildren().addAll(home,pie,line,bar);
        topContainer.getChildren().add(topBar);
        
        container.setTop(topContainer);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getStylesheets().add("img/style.css");
        topBar.getStyleClass().add("topBar");
        secondBar.getStyleClass().add("secondBar");
        topBar.prefWidthProperty().bind(topContainer.widthProperty());
        topBar.setAlignment(Pos.CENTER);
        topContainer.getChildren().add(secondBar);
        secondBar.setAlignment(Pos.CENTER);
        container.setCenter(layout);
        home.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
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
        pie.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               loadPieMenu();
           }
       });
       
        line.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               loadLineMenu();
           }
       });
        bar.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent ae)
           {
               loadBarMenu();
           }
       });
    }
    static void loadPieMenu(){
        JFXButton input = new JFXButton("Input Data");
        JFXButton add = new JFXButton("Add Data");
        JFXButton down = new JFXButton("Download Data");
        JFXButton delete = new JFXButton("Delete");
        
        secondBar.getChildren().setAll(input,add,down,delete);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500),secondBar);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        input.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Pie.Pie_Chart(layout);
            }
            
        });
        add.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Pie.Add(layout);
            }
            
        });
        delete.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Pie.Delete();
            }
            
        });
        down.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pie.Download();
                } catch (IOException ex) {
                    Logger.getLogger(ChartMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
    static void loadLineMenu(){
        JFXButton input_line = new JFXButton("Input Data");
        JFXButton add_series = new JFXButton("Add another series");
        JFXButton down_line = new JFXButton("Download Data");
        JFXButton delete_line = new JFXButton("Delete Chart");
        secondBar.getChildren().setAll(input_line, add_series, down_line, delete_line);
         FadeTransition fadeIn = new FadeTransition(Duration.millis(500),secondBar);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        input_line.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Line_Chart.line(layout);
            }
            
        });
        add_series.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Line_Chart.Add();
            }
            
        });
        down_line.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                try {
                    Line_Chart.Download();
                } catch (IOException ex) {
                    Logger.getLogger(ChartMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        delete_line.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Line_Chart.Delete();
            }
            
        });
    }
    static void loadBarMenu(){
        JFXButton input_bar = new JFXButton("Input Data");
        JFXButton add_bar = new JFXButton("Add another data");
        JFXButton down_bar = new JFXButton("Download Data");
        JFXButton delete_bar = new JFXButton("Delete Chart");
        secondBar.getChildren().setAll(input_bar,add_bar,down_bar,delete_bar);
         FadeTransition fadeIn = new FadeTransition(Duration.millis(500),secondBar);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        input_bar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Bar_Chart.bar(layout);
            }
            
        });
        add_bar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Bar_Chart.Add();
            }
            
        });
        down_bar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                try {
                    Bar_Chart.Download();
                } catch (IOException ex) {
                    Logger.getLogger(ChartMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        delete_bar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Bar_Chart.Delete();
            }
            
        });
    }
    
    public static void show(Pane root)
    {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        topContainer.prefWidthProperty().bind(root.getScene().widthProperty());
        container.prefWidthProperty().bind(root.getScene().widthProperty());
        root.getChildren().setAll(container);
        
    }
    
    
}
