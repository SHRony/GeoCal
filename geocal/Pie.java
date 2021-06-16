package geocal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Lenovo
 */
public class Pie{
 
    public static ObservableList<PieChart.Data> input = FXCollections.observableArrayList();
    private static PieChart piechart=new PieChart();
    private static Boolean on = false;
    
    public static void Pie_Chart(Pane layout)
    {
        Delete();
        
        piechart = new PieChart();
        //piechart.setLegendSide(Side.LEFT);
        Take_Input(layout);
        piechart.setData(input);
        layout.getChildren().addAll(piechart);
        piechart.prefHeightProperty().bind(GeoCal.scene.heightProperty().subtract(70));
        piechart.prefWidthProperty().bind(GeoCal.scene.widthProperty());
        on=true;
    }
    public static void Percentage(Pane layout)
    {
        Label val = new Label("");
        val.setTextFill(Color.AQUA);

        for (final PieChart.Data data : piechart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                e -> {
                    double total = 0;
                    for (PieChart.Data d : piechart.getData()) {
                        total += d.getPieValue();
                    }
                    val.setTranslateX(e.getSceneX());
                    val.setTranslateY(e.getSceneY());

                    String text = String.format("%.2f%%", 100*data.getPieValue()/total) ;
                    //System.out.println(text);
                    val.setText(text);
                 }
                );
            
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,e -> { val.setText(""); });
        }
        layout.getChildren().addAll(val);
    }
    public static void Delete()
    {
        input.clear();
        if(on)
        {
            on=false;
            ((Pane)piechart.getParent()).getChildren().remove(piechart);
        }
    }
    public static void Add(Pane layout)
    {
        if(!on)
            return;
        Stage window = new Stage();
        window.setTitle("Add data");

        Label txt1 = new Label("Item name");
        Label txt2 = new Label("Value");
        
        TextField name = new TextField();
        name.setPromptText("Enter Item name");
        
        TextField value = new TextField();
        value.setPromptText("Enter value");
        
        Button submit = new Button("Add");

        GridPane root = new GridPane();
        root.addRow(0,txt1,name);
        root.addRow(1,txt2, value);
        root.addRow(2, submit);

        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        window.show();


        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                String s = name.getText();
                String val = value.getText();
                Boolean error = val.indexOf("%", 0)!=-1;
                if(error)
                {
                    Alert.display("Error..!"," Dont give %, just put value");
                }
                else if(s==null || s.isEmpty() || val==null || val.isEmpty())
                {
                    Alert.display("Erros...!", "Input data is empty!");
                }
                else
                {
                    name.setText("");
                    value.setText("");
                    System.out.println(s+","+val);
                    input.add(new PieChart.Data(s, new Double(val)));
                    window.close();
                    Percentage(layout);
                }
            }
        });
       
    }
    public static void Take_Input(Pane layout)
    {
        Stage window = new Stage();
        window.setTitle("Give input data");

        Label txt1 = new Label("Item name");
        Label txt2 = new Label("Value");
        
        TextField name = new TextField();
        name.setPromptText("Enter Item name");
        
        TextField value = new TextField();
        value.setPromptText("Enter value");
        
        Button submit = new Button("Finish");
        Button next = new Button("Next");

        GridPane root = new GridPane();
        root.addRow(0,txt1,name);
        root.addRow(1,txt2, value);
        root.addRow(2,next, submit);

        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        window.show();


        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                String s = name.getText();
                String val = value.getText();
                Boolean error = val.indexOf("%", 0)!=-1;
                if(error)
                {
                    Alert.display("Error..!"," Dont give %, just put value");
                }
                else if(s==null || s.isEmpty() || val==null || val.isEmpty())
                {
                    Alert.display("Erros...!", "Input data is empty!");
                }
                else
                {
                    name.setText("");
                    value.setText("");
                    System.out.println(s+","+val);
                    input.add(new PieChart.Data(s, new Double(val)));
                    window.close();
                    Percentage(layout);
                }
            }
        });
        next.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                String s = name.getText();
                String val = value.getText();
                Boolean error = val.indexOf("%", 0)!=-1;
                if(error)
                {
                    Alert.display("Error..!"," Don't give %, just put value");
                }
                else if(s==null || s.isEmpty() || val==null || val.isEmpty())
                {
                    Alert.display("Erros...!", "Input data is empty!");
                }
                else
                {
                    name.setText("");
                    value.setText("");
                    System.out.println(s+","+val);
                    input.add(new PieChart.Data(s, new Double(val)));
                    window.close();
                    window.show();
                }
            }
        });


    }
    public static void Download() throws IOException
    {
        String done="Download Successful";
        String s = "+-------------------------------------+---------------------+----------------+\n";
        String s2= "|-------------------------------------|---------------------|----------------|\n";
        String info = s + String.format("| %35s |%20s |%15s |\n","Item Name","Value", "Percentage");
        String time = new SimpleDateFormat("HHmmss_yyyyMMdd").format(Calendar.getInstance().getTime());
      
        double total = 0.0;
        for(PieChart.Data d : piechart.getData())
            total += d.getPieValue();
        //Table making
        for(final PieChart.Data data : piechart.getData())
        {            
            String percen = String.format("%.2f%%", 100*data.getPieValue()/total) ;
            String now = String.format("| %35s |%20s |%15s |\n", data.getName(), data.getPieValue(), percen);
            info=info+s2+now;
        }
        info+=s;
        System.out.println(info);
        WritableImage image = piechart.snapshot(new SnapshotParameters(), null);
        File file = new File(time+"_pieChart.png");
        
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        }
        catch(IOException e)
        {
            Alert.display("Error...", e.toString());
            done="Error in fine name";
        }
        
        try
        {
            FileWriter fw = new FileWriter(time+"_Data.txt");
            fw.write(info);
            fw.close();
        }
        catch(IOException e)
        {
            Alert.display("Error...", e.toString());
            done="Error in fine name";
        }
        System.out.println("Saved...");
        
        String f1 = file.getAbsolutePath();
        String f2 = f1.replace("pieChart.png", "Data.txt");
        Alert.display(done, "Successfully saved\n"+f1+"\n"+f2);
        
    }
    
}
