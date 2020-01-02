package sample;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class ControllerBeuteRaeuber implements Initializable {
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tableColumnTimestep, tableColumnBeute, tableColumnRaeuber;
    @FXML
    private CheckBox checkVorlaufend;
    @FXML
    private LineChart<Double, Double> linechart1;
    @FXML
    private TextField fieldAnzahlBeute, fieldAnzahlRaeuber, fieldTime, fieldStep, fieldGgewichtBeute, fieldZuwachs, fieldVerlust,
            fieldGgewichtRaeuber;
    Timer timer = new Timer();
    PauseTransition wait = new PauseTransition(Duration.millis(100));

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextield(fieldAnzahlBeute);
        setTextield(fieldAnzahlRaeuber);
        setTextield(fieldGgewichtBeute);
        setTextield(fieldGgewichtRaeuber);
        setTextield(fieldStep);
        setTextield(fieldTime);
        setTextield(fieldVerlust);
        setTextield(fieldZuwachs);


    }

    public void setTextield(TextField textField) {
        double value = 0;
        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?"))
                {
                    textField.setText(oldValue);
                }
            }
        });
    }

    public void handleButtonStart(ActionEvent actionEvent) {
        double beuteBiomasse = Double.parseDouble(fieldAnzahlBeute.getText());
        double raeuberBiomasse = Double.parseDouble(fieldAnzahlRaeuber.getText());
        double zuwachsRate = Double.parseDouble(fieldZuwachs.getText());
        double verlustRate = Double.parseDouble(fieldVerlust.getText());
        double ggewichtBeute = Double.parseDouble(fieldGgewichtBeute.getText());
        double ggewichtRaeuber = Double.parseDouble(fieldGgewichtRaeuber.getText());
        int time = Integer.parseInt(fieldTime.getText());
        double step = Double.parseDouble(fieldStep.getText());
        boolean isSelected = checkVorlaufend.isSelected();

        Beute beute = new Beute(beuteBiomasse, zuwachsRate, ggewichtBeute);
        Raeuber raeuber = new Raeuber(raeuberBiomasse, verlustRate, ggewichtRaeuber);

        Double[][][] graph = Sim.getGraph(time, step, beute, raeuber);

        XYChart.Series<Double, Double> beuteSeries = new XYChart.Series<Double, Double>();
        XYChart.Series<Double, Double> raeuberSeries = new XYChart.Series<Double, Double>();

        int length = XYGraph.getTime(time, step);

        if (!isSelected) {
            for (int i = 0; i < length; i++) {
                beuteSeries.getData().add(new XYChart.Data(graph[i][0][0], graph[i][1][0]));
                raeuberSeries.getData().add(new XYChart.Data(graph[i][0][0], graph[i][0][1]));
            }

            linechart1.getData().addAll(beuteSeries, raeuberSeries);
        } else {
            try {
                graph = Sim.getGraph(10000, step, beute, raeuber);
                var ref = new Object() {
                    int i = 0;
                };
                Double[][][] finalGraph = graph;
                wait.setOnFinished((e) -> {
                    beuteSeries.getData().add(new XYChart.Data(finalGraph[ref.i][0][0], finalGraph[ref.i][1][0]));
                    raeuberSeries.getData().add(new XYChart.Data(finalGraph[ref.i][0][0], finalGraph[ref.i][0][1]));
                    linechart1.getData().addAll(beuteSeries, raeuberSeries);
                    ref.i++;
                    wait.playFromStart();
                });
                wait.play();
            } catch (Exception e) {
            }
        }

    }

    public void handleButtonClear() {
        wait.stop();
        linechart1.getData().clear();
    }

    public void handleButtonBack(ActionEvent event) {
        try {
            timer.cancel();
            timer.purge();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("guiStart.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
        }
    }

    public void handleButtonDaten(ActionEvent actionEvent) {

    }
}
