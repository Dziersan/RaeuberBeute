package sample;

import javafx.animation.PauseTransition;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.DB.Schema;
import sample.DB.Table;
import sample.DB.Type;
import sample.DB.Update;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static sample.DB.JDBC.getConnection;
import static sample.GuiUtil.setTextield;

public class ControllerHaseFuchs implements Initializable
{
    @FXML
    private CheckBox checkVorlaufend;
    @FXML
    private TableView<Sim> tableView;
    @FXML
    private TableColumn<Sim, Double> tableColumnTimestep, tableColumnHase, tableColumnFuchs, tableColumnWeide;
    @FXML
    private LineChart<Double, Double> linechart1;
    @FXML
    private TextField fieldAnzahlHase,fieldAnzahlFuchs, fieldTime, fieldStep, fieldGgewichtHase, fieldZuwachs, fieldVerlust,
            fieldGgewichtFuchs, fieldWeideAnfang, fieldWeideEnde;

    double haseBiomasse, step, ggewichtFuchs, ggewichtHase, verlustRate, zuwachsRate, fuchsBiomasse, abBegrenzung, bisBegrenzung;
    boolean isSelected;
    int time, length;
    Double[][] graph;
    PauseTransition wait = new PauseTransition(Duration.millis(100));

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextield(fieldAnzahlHase);
        setTextield(fieldAnzahlFuchs);
        setTextield(fieldGgewichtHase);
        setTextield(fieldGgewichtFuchs);
        setTextield(fieldStep);
        setTextield(fieldTime);
        setTextield(fieldVerlust);
        setTextield(fieldZuwachs);
        setTextield(fieldWeideAnfang);
        setTextield(fieldWeideEnde);
        tableColumnTimestep.setCellValueFactory(new PropertyValueFactory<Sim, Double>("timestep"));
        tableColumnHase.setCellValueFactory(new PropertyValueFactory<Sim, Double>("beuteBiomasse"));
        tableColumnFuchs.setCellValueFactory(new PropertyValueFactory<Sim, Double>("raeuberBiomasse"));
        tableColumnWeide.setCellValueFactory(new PropertyValueFactory<Sim, Double>("weideData"));
    }

    public void handleButtonStart(){
    haseBiomasse = Double.parseDouble(fieldAnzahlHase.getText());
    fuchsBiomasse = Double.parseDouble(fieldAnzahlFuchs.getText());
    zuwachsRate = Double.parseDouble(fieldZuwachs.getText());
    verlustRate = Double.parseDouble(fieldVerlust.getText());
    ggewichtHase = Double.parseDouble(fieldGgewichtHase.getText());
    ggewichtFuchs = Double.parseDouble(fieldGgewichtFuchs.getText());
    abBegrenzung = Double.parseDouble(fieldWeideAnfang.getText());
    bisBegrenzung = Double.parseDouble(fieldWeideEnde.getText());
    time = Integer.parseInt(fieldTime.getText());
    step = Double.parseDouble(fieldStep.getText());
    isSelected = checkVorlaufend.isSelected();

    Hase hase = new Hase(haseBiomasse,zuwachsRate, ggewichtHase);
    Fuchs fuchs = new Fuchs(fuchsBiomasse,verlustRate,ggewichtFuchs);
    Weide weide = new Weide(1000,abBegrenzung,bisBegrenzung);

    graph = Sim.getGraph(time,step,hase,fuchs,weide);

    XYChart.Series<Double, Double> haseSeries = new XYChart.Series<Double, Double>();
    XYChart.Series<Double, Double> fuchsSeries = new XYChart.Series<Double, Double>();

    haseSeries.setName("Hase");
    fuchsSeries.setName("Fuchs");

    length = GuiUtil.getTime(time, step);

    if(!isSelected)
    {
        for(int i = 0; i < length; i++)
        {
            haseSeries.getData().add(new XYChart.Data(graph[i][0], graph[i][1]));
            fuchsSeries.getData().add(new XYChart.Data(graph[i][0], graph[i][2]));
        }

        linechart1.getData().addAll(haseSeries, fuchsSeries);
    }
    else
    {
        graph = Sim.getGraph(10000, step, hase, fuchs, weide);
        var ref = new Object() {
            int i = 0;
        };
        wait.setOnFinished((e) -> {
            haseSeries.getData().add(new XYChart.Data(graph[ref.i][0], graph[ref.i][1]));
            fuchsSeries.getData().add(new XYChart.Data(graph[ref.i][0], graph[ref.i][2]));
            linechart1.getData().addAll(haseSeries, fuchsSeries);
            ref.i++;
            wait.playFromStart();
        });
        wait.play();
    }
        Hase haseData = new Hase(haseBiomasse,zuwachsRate, ggewichtHase);
        Fuchs fuchsData = new Fuchs(fuchsBiomasse,verlustRate,ggewichtFuchs);
        Weide weideData = new Weide(1000,abBegrenzung,bisBegrenzung);

        Sim sim = new Sim(step, haseBiomasse, fuchsBiomasse, 1000);

        tableView.getColumns().clear();
        tableView.setItems(sim.getTableData(time, haseData, fuchsData, weideData));
        tableView.getColumns().addAll(tableColumnTimestep, tableColumnHase, tableColumnFuchs, tableColumnWeide);
    }

    public void handleButtonClear(){
        wait.stop();
        linechart1.getData().clear();
        tableView.getColumns().clear();
    }

    public void handleButtonBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("guiStart.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
        }
    }

    public void handleButtonDaten(ActionEvent event) {
        Date zeitstempel = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH_mm_dd_MM_yy");
        try {
            Connection connection = getConnection();
            Schema schema = new Schema("hase_fuchs" + simpleDateFormat.format(zeitstempel));
            System.out.println(schema.drop());
            System.out.println(schema.create());
            System.out.println(schema.use());
//        updateStatement(connection, schema.drop());
//        updateStatement(connection, schema.create());
//        updateStatement(connection, schema.use());

            Table biomasse = new Table("Biomasse");

            biomasse.addPrimaryKey("Timestep", Type.DECIMAL);
            biomasse.addAttr("Biomasse_Hase", Type.DECIMAL);
            biomasse.addAttr("Biomasse_Fuchs", Type.DECIMAL);
            biomasse.addAttr("Weide", Type.DECIMAL);

            System.out.println(biomasse.create());
//        updateStatement(connection, biomasse.create());

            for (int i = 0; i < length; i++)
            {
                Update biomasseUpdate = new Update("Biomasse");
//            updateStatement(connection, biomasseUpdate.insertData(graph[i][0], graph[i][1],graph[i][2],graph[i][3]));
                System.out.println(biomasseUpdate.insertData(graph[i][0], graph[i][1],graph[i][2],graph[i][3]));
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

