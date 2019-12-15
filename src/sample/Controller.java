package sample;


import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

public class Controller {

@FXML private Button buttonStart1;
@FXML private LineChart<Number, Number> linechart1;
@FXML private TextField fieldAnzahlBeute;
@FXML private TextField fieldAnzahlRaeuber;


    public void handleButtonStart(){
        System.out.println(fieldAnzahlBeute.getText());



//        NumberAxis xAxis = new NumberAxis(0.1,10,1);
//        xAxis.setLabel("Month");
//
//        NumberAxis yAxis = new NumberAxis(0,10,1);
//        yAxis.setLabel("Anzahl Tiere");
//
//        linechart1 = new LineChart<>(xAxis,yAxis);
//
//        XYChart.Series series = new XYChart.Series();
//        series.setName("Test");
//
//        series.getData().add(new XYChart.Data(0.2,2));
//        linechart1.getData().add(series);

}

}
