package sample;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import sample.DB.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

import static sample.DB.JDBC.getConnection;
import static sample.DB.JDBC.updateStatement;

public class ControllerBeuteRaeuber implements Initializable {

    @FXML
    private TableView<Double []> tableView;
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
    Double[][][] graph;
    double beuteBiomasse, step, ggewichtRaeuber, ggewichtBeute, verlustRate, zuwachsRate, raeuberBiomasse;
    int time, length;


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
                if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?"))
                {
                    textField.setText(oldValue);
                }
            }
        });
    }

    public void handleButtonStart(ActionEvent actionEvent) {
        beuteBiomasse = Double.parseDouble(fieldAnzahlBeute.getText());
        raeuberBiomasse = Double.parseDouble(fieldAnzahlRaeuber.getText());
        zuwachsRate = Double.parseDouble(fieldZuwachs.getText());
        verlustRate = Double.parseDouble(fieldVerlust.getText());
        ggewichtBeute = Double.parseDouble(fieldGgewichtBeute.getText());
        ggewichtRaeuber = Double.parseDouble(fieldGgewichtRaeuber.getText());
        time = Integer.parseInt(fieldTime.getText());
        step = Double.parseDouble(fieldStep.getText());
        boolean isSelected = checkVorlaufend.isSelected();

        Beute beute = new Beute(beuteBiomasse, zuwachsRate, ggewichtBeute);
        Raeuber raeuber = new Raeuber(raeuberBiomasse, verlustRate, ggewichtRaeuber);

        graph = Sim.getGraph(time, step, beute, raeuber);

        ObservableList<String[]> data = FXCollections.observableArrayList();


        XYChart.Series<Double, Double> beuteSeries = new XYChart.Series<Double, Double>();
        XYChart.Series<Double, Double> raeuberSeries = new XYChart.Series<Double, Double>();

        length = XYGraph.getTime(time, step);

        if (!isSelected)
        {
            for (int i = 0; i < length; i++)
            {
                beuteSeries.getData().add(new XYChart.Data(graph[i][0][0], graph[i][1][0]));
                raeuberSeries.getData().add(new XYChart.Data(graph[i][0][0], graph[i][0][1]));
            }

            linechart1.getData().addAll(beuteSeries, raeuberSeries);
        } else
            {
            try
            {
                graph = Sim.getGraph(1000000, step, beute, raeuber);
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
        Date zeitstempel = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH_mm_dd_MM_yy");
        System.out.println(simpleDateFormat.format(zeitstempel));
    try {
        Connection connection = getConnection();
        Schema schema = new Schema("raeuber_biomasse" + simpleDateFormat.format(zeitstempel));
//        System.out.println(schema.drop());
//        System.out.println(schema.create());
//        System.out.println(schema.use());
        updateStatement(connection, schema.drop());
        updateStatement(connection, schema.create());
        updateStatement(connection, schema.use());

        Table biomasse = new Table("Biomasse");

        biomasse.addPrimaryKey("Timestep", Type.DECIMAL);
        biomasse.addAttr("Biomasse_Beute", Type.DECIMAL);
        biomasse.addAttr("Biomasse_Räuber", Type.DECIMAL);

//        System.out.println(biomasse.create());
        updateStatement(connection, biomasse.create());


        for (int i = 0; i < length; i++)
        {
            Update biomasseUpdate = new Update("Biomasse");
            updateStatement(connection, biomasseUpdate.insertData(graph[i][0][0], graph[i][1][0],graph[i][0][1]));
//            System.out.println(biomasseUpdate.insertData(graph[i][0][0], graph[i][1][0],graph[i][0][1]));
        }
    }

    catch(SQLException e)
    {
        System.out.println("Fehlermeldung: " + e.getMessage());
        System.out.println("SQL State : " + e.getSQLState());
        e.printStackTrace();
    }
    }
}
