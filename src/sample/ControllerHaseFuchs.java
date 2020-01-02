package sample;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ControllerHaseFuchs
{
    public CheckBox checkVorlaufend;
@FXML private LineChart<Double, Double> linechart1;
@FXML private TextField fieldAnzahlHase,fieldAnzahlFuchs, fieldTime, fieldStep, fieldGgewichtHase, fieldZuwachs, fieldVerlust,
        fieldGgewichtFuchs, fieldWeideAnfang, fieldWeideEnde;
    PauseTransition wait = new PauseTransition(Duration.millis(100));


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
    boolean isSelected = checkVorlaufend.isSelected();

        Hase hase = new Hase(haseBiomasse,zuwachsRate, ggewichtHase);
        Fuchs fuchs = new Fuchs(fuchsBiomasse,verlustRate,ggewichtFuchs);
        Weide weide = new Weide(1000,abBegrenzung,bisBegrenzung);

        Double[][][] graph = SimHaseFuchs.getGraph(time,step,hase,fuchs,weide);

        XYChart.Series<Double, Double> beuteSeries = new XYChart.Series<Double, Double>();
        XYChart.Series<Double, Double> raeuberSeries = new XYChart.Series<Double, Double>();

        int length = XYGraph.getTime(time, step);

        if(!isSelected)
        {
            for(int i = 0; i < length; i++)
            {
                beuteSeries.getData().add(new XYChart.Data(graph[i][0][0], graph[i][1][0]));
                raeuberSeries.getData().add(new XYChart.Data(graph[i][0][0], graph[i][0][1]));
            }

            linechart1.getData().addAll(beuteSeries, raeuberSeries);
        }
        else
        {
            var ref = new Object() {
                int i = 0;
            };
            wait.setOnFinished((e) -> {
                beuteSeries.getData().add(new XYChart.Data(graph[ref.i][0][0], graph[ref.i][1][0]));
                raeuberSeries.getData().add(new XYChart.Data(graph[ref.i][0][0], graph[ref.i][0][1]));
                linechart1.getData().addAll(beuteSeries, raeuberSeries);
                ref.i++;
                wait.playFromStart();
            });
            wait.play();
        }
    }

    public void handleButtonClear(){
        wait.stop();
        linechart1.getData().clear();
    }

    public void handleButtonBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("guiStart.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
        }
    }
}