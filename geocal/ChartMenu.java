package geocal;

import static geocal.MainMenu.move;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class ChartMenu extends Application{
 
    GridPane root = new GridPane();
    Pane layout = new Pane();

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage ps) throws Exception {
        
        Menu pie = new Menu("Pie Chart");
        MenuItem input = new MenuItem("Input Data");
        MenuItem add = new MenuItem("Add Data");
        MenuItem down = new MenuItem("Download Data");
        MenuItem delete = new MenuItem("Delete Chart");
        pie.getItems().addAll(input, add, down, delete);
        
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
        
        Menu line = new Menu("Line Chart");
        MenuItem input_line = new MenuItem("Input Data");
        MenuItem add_series = new MenuItem("Add another series");
        MenuItem down_line = new MenuItem("Download Data");
        MenuItem delete_line = new MenuItem("Delete Chart");
        line.getItems().addAll(input_line, add_series, down_line, delete_line);
        
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
        
        
        Menu bar = new Menu("Bar Chart");
        MenuItem input_bar = new MenuItem("Input Data");
        MenuItem add_bar = new MenuItem("Add another data");
        MenuItem down_bar = new MenuItem("Download Data");
        MenuItem delete_bar = new MenuItem("Delete Chart");
        bar.getItems().addAll(input_bar, add_bar, down_bar, delete_bar);
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
        
        Menu area = new Menu("Area Chart");
        Menu bubble = new Menu("Bubble Chart");
        Menu scatter = new Menu("Scatter Chart");
        
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(pie, line, bar, area, bubble, scatter);
        
        root.addRow(0, menuBar);
        root.addRow(1, layout);
        Scene scene = new Scene(root, 1000, 800);
        ps.setScene(scene);
        ps.show();
    }
    
}
