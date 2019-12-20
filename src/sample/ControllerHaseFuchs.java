package sample;



import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

public class ControllerHaseFuchs {


@FXML private LineChart<Double, Double> linechart1;
//@FXML private LogarithmicAxis xAxis;
//@FXML private NumberAxis yAxis;
@FXML private TextField fieldAnzahlHase,fieldAnzahlFuchs, fieldTime, fieldStep, fieldGgewichtHase, fieldZuwachs, fieldVerlust,
        fieldGgewichtFuchs, fieldWeideAnfang, fieldWeideEnde;


public void handleButtonClear(){
    linechart1.getData().clear();
}

    public void handleButtonStart(){
    double haseBiomasse = Double.parseDouble(fieldAnzahlHase.getText());
    double fuchsBiomasse = Double.parseDouble(fieldAnzahlFuchs.getText());
    double zuwachsRate = Double.parseDouble(fieldZuwachs.getText());
    double verlustRate = Double.parseDouble(fieldVerlust.getText());
    double ggewichtHase = Double.parseDouble(fieldGgewichtHase.getText());
    double ggewichtFuchs = Double.parseDouble(fieldGgewichtFuchs.getText());
    double abBegrenzung = Double.parseDouble(fieldWeideAnfang.getText());
    double bisBegrenzung = Double.parseDouble(fieldWeideEnde.getText());
    int time = Integer.parseInt(fieldTime.getText());
    double step = Double.parseDouble(fieldStep.getText());

        Hase hase = new Hase(haseBiomasse,zuwachsRate, ggewichtHase);
        Fuchs fuchs = new Fuchs(fuchsBiomasse,verlustRate,ggewichtFuchs);
        Weide weide = new Weide(1000,abBegrenzung,bisBegrenzung);

        Double[][] haseGraph = SimHaseFuchs.getHaseGraph(time,step,hase,fuchs,weide);
        Double[][] fuchsGraph = SimHaseFuchs.getFuchsGraph(time,step,hase,fuchs,weide);

        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
//        ObservableList<XYChart.Series> dataset = FXCollections.observableArrayList();


        int length = SimHaseFuchs.getTime(time, step);
        for(int i = 0; i < length; i++)
        {
            series.getData().add(new XYChart.Data(haseGraph[i][0], haseGraph[i][1]));
        }
        XYChart.Series<Double, Double> series1 = new XYChart.Series<Double, Double>();

        for(int i = 0; i < length; i++)
        {
            series1.getData().add(new XYChart.Data(fuchsGraph[i][0], fuchsGraph[i][1]));
        }
//        dataset.addAll(series,series1);
        linechart1.getData().addAll(series, series1);
//        LineChart chart = new LineChart(xAxis, yAxis, dataset);

    }
}

