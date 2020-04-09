/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class SmallMenu {

    private static int cnt=1;
    private static char ch ='A';
    private String name;
    Label lbl = new Label();
    
    final public ContextMenu list;
    public MenuItem delete, rename;

    public SmallMenu() {
        name=String.valueOf(ch).concat(String.valueOf(cnt));
        lbl.setText(name);
        lbl.setTextFill(Color.BROWN);
        increase();
        list = new ContextMenu();
        delete = new MenuItem("Delete");
        rename = new MenuItem("Rename");
        list.getItems().addAll(delete, rename);
        lbl.setContextMenu(list);
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
    
    //Something error?
    public void set(double x, double y)
    {
        lbl.setLayoutX(x);
        lbl.setLayoutY(y);
        //lbl.setScaleX(x);
        //lbl.setScaleY(y);
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
    
//    public void setMenu(double x, double y)
//    {
//        list.setX(x);
//        list.setY(y);
//    }
    
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
