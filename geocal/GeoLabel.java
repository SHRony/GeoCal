/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocal;

import java.text.DecimalFormat;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 *
 * @author Administrator
 */
public class GeoLabel extends Label {
    double t;
    GeoLabel(double x,double y,double T,DoubleProperty Unit)
    {
        super();
        this.setTranslateX(x);
        this.setTranslateY(y);
        t=T;
        Unit.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                calibrate(newValue.doubleValue());
                
            }});
    }
    void calibrate(double newValue)
    {
                double tmp=t*newValue;
                DecimalFormat df=new DecimalFormat();
                if(Math.abs(tmp)>=100)
                {
                    df=new DecimalFormat("0.##E0");
                }
                else if(Math.abs(tmp)<=0.01)
                {
                    df=new DecimalFormat("0.##E0");
                }
                else if(Math.abs(tmp)>=1)
                {
                    df=new DecimalFormat("0");
                }
                else
                {
                    df=new DecimalFormat("#.##");
                }
                this.setText(df.format(tmp));
                
    }
}
