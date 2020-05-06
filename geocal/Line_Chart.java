package geocal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Lenovo
 */
public class Line_Chart {
    
    private static LineChart<String,Number> lineChart;
    private static int id=1,i;
    private static Vector<String> X = new Vector<>();
    private static Vector<String> Y = new Vector<>();
    private static Vector<String> input = new Vector<>();
    private static Boolean on = false;
    
    public static void line(Pane layout)
    {
        if(on)
        {
            Add();
            return;
        }
        lineChart = new LineChart(new CategoryAxis(), new NumberAxis());
        lineChart.getXAxis().setAutoRanging(true);
        lineChart.getYAxis().setAutoRanging(true);
        //lineChart.setTitle("Line Chart");
        Take_Input();
        layout.getChildren().add(lineChart);
        on=true;
    }
    
    public static void Take_Input()
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
                
                if(s==null || s.isEmpty() || val==null || val.isEmpty())
                {
                    Alert.display("Erros...!", "Input data is empty!");
                }
                else
                {
                    name.setText("");
                    value.setText("");
                    System.out.println(s+","+val);
                    X.add(s);
                    Y.add(val);
                    input.add(String.format("| %35s |%15s |",s,val));
                    window.close();
                    Draw();
                }
            }
        });
        next.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                String s = name.getText();
                String val = value.getText();
                
                if(s==null || s.isEmpty() || val==null || val.isEmpty())
                {
                    Alert.display("Erros...!", "Input data is empty!");
                }
                else
                {
                    name.setText("");
                    value.setText("");
                    System.out.println(s+","+val);
                    X.add(s);
                    Y.add(val);
                    input.add(String.format("| %35s |%15s |",s,val));
                    window.close();
                    window.show();
                }
            }
        });


    }
    
    public static void Draw()
    {
        XYChart.Series series = new XYChart.Series();
        series.setName("input: "+(id++));
        
        for(int i =0; i<X.size(); i++)
        {
            series.getData().add(new XYChart.Data(X.get(i), new Double(Y.get(i))));
            System.out.println(X.get(i)+"_:_"+new Double(Y.get(i)));
        }
     
        lineChart.getData().add(series);
    }
    
    public static void Add()
    {
        if(!on)
            return;
        i=0;
        Stage window = new Stage();
        window.setTitle("Give input data");

        Label txt1 = new Label("Item name: ");
        Label txt2 = new Label("Value");
        
        Label name = new Label();
        name.setText(X.get(i++));
        name.setTextAlignment(TextAlignment.CENTER);
        
        
        TextField value = new TextField();
        value.setPromptText("Enter value");
        
        Button next = new Button("Next");

        GridPane root = new GridPane();
        root.addRow(0,txt1,name);
        root.addRow(1,txt2, value);
        root.addRow(2,next);

        Scene scene = new Scene(root, 500,200);
        window.setScene(scene);
        window.show();
        
        Y.clear();
 
        next.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                
                String val = value.getText();
                
                if(val==null || val.isEmpty())
                {
                    Alert.display("Erros...!", "Input data is empty!");
                }
                else
                {
                    value.setText("");
                    Y.add(val);
                    input.set(i-1, input.get(i-1)+String.format("%15s |",val));
                    window.close();
                    if(i<X.size())
                    {
                        name.setText(X.get(i++));
                        window.show();
                    }
                    else
                    {
                        Draw();
                    }
                }
            }
        });
        
    }
    
    public static void Delete()
    {
        X.clear();
        Y.clear();
        input.clear();
        id=1;
        if(on)
        {
            on=false;
            ((Pane)lineChart.getParent()).getChildren().remove(lineChart);
        }
    }
    
    public static void Download() throws IOException
    {
        String done="Download Successful";
        String s = "+-------------------------------------+";
        String s2= "|-------------------------------------|";
        String time = new SimpleDateFormat("HHmmss_yyyyMMdd").format(Calendar.getInstance().getTime());
      
        //Table making
        for(int i=1; i<id; i++)
        {
            s+= "----------------+";
            s2+="----------------|";
        }
        s+='\n';
        s2+='\n';
        String info = s + String.format("| %35s |","Item Name");
        for(int i=1; i<id; i++)
        {
            String ss = "Value "+i;
            info+=String.format("%15s |",ss);
        }
        info+='\n';
        for(String now : input)
        {
            info=info+s2+now+'\n';
        }
        info+=s;
        System.out.println(info);
        
        WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
        File file = new File(time+"_lineChart.png");
        
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
        String f2 = f1.replace("lineChart.png", "Data.txt");
        Alert.display(done, "Successfully saved\n"+f1+"\n"+f2);
        
    }
}
