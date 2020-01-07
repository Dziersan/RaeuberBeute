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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import javafx.util.Duration;

import sample.Core.Beute;
import sample.Core.GuiUtil;
import sample.Core.Raeuber;
import sample.Core.Sim;
import sample.DB.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

import static sample.DB.JDBC.getConnection;
import static sample.Core.GuiUtil.setTextield;

public class ControllerBeuteRaeuber implements Initializable
{
    @FXML
    boolean isSelected;
    @FXML
    private TableView<Sim> tableView;
    @FXML
    private TableColumn<Sim, Double> tableColumnTimestep, tableColumnBeute, tableColumnRaeuber;
    @FXML
    private CheckBox checkVorlaufend;
    @FXML
    private LineChart<Double, Double> linechart1;
    @FXML
    private TextField fieldAnzahlBeute, fieldAnzahlRaeuber, fieldTime, fieldStep, fieldGgewichtBeute, fieldZuwachs, fieldVerlust,
            fieldGgewichtRaeuber;

    Timer timer = new Timer();
    PauseTransition wait = new PauseTransition(Duration.millis(100));
    Double[][] graph;
    double beuteBiomasse, step, ggewichtRaeuber, ggewichtBeute, verlustRate, zuwachsRate, raeuberBiomasse;
    int time, length;

    /** initialize
     * Constraints der Textfelder und Zuweisung der Spalten von dem Tableview
     * setTextfield ist eine Methode aus der GuiUtil Klasse, die die Textfelder dadurch beschränkt,
     * dass keine Buchstaben, maximal ein Punkt (Für double) und vier Nachkommastellen erlaubt.
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextield(fieldAnzahlBeute);
        setTextield(fieldAnzahlRaeuber);
        setTextield(fieldGgewichtBeute);
        setTextield(fieldGgewichtRaeuber);
        setTextield(fieldStep);
        setTextield(fieldTime);
        setTextield(fieldVerlust);
        setTextield(fieldZuwachs);

        //Der Name muss mit der Variablen aus der Klasse übereinstimmen, da er auf die Getter zugreift.
        tableColumnTimestep.setCellValueFactory(new PropertyValueFactory<Sim, Double>("timestep"));
        tableColumnBeute.setCellValueFactory(new PropertyValueFactory<Sim, Double>("beuteBiomasse"));
        tableColumnRaeuber.setCellValueFactory(new PropertyValueFactory<Sim, Double>("raeuberBiomasse"));

    }

    public void handleButtonStart(ActionEvent actionEvent) {
        //Zuweisung der Textfeld-Werten in die zuvor deklarierten Variablen
        beuteBiomasse = Double.parseDouble(fieldAnzahlBeute.getText());
        raeuberBiomasse = Double.parseDouble(fieldAnzahlRaeuber.getText());
        zuwachsRate = Double.parseDouble(fieldZuwachs.getText());
        verlustRate = Double.parseDouble(fieldVerlust.getText());
        ggewichtBeute = Double.parseDouble(fieldGgewichtBeute.getText());
        ggewichtRaeuber = Double.parseDouble(fieldGgewichtRaeuber.getText());
        time = Integer.parseInt(fieldTime.getText());
        step = Double.parseDouble(fieldStep.getText());
        isSelected = checkVorlaufend.isSelected();

        Beute beute = new Beute(beuteBiomasse, zuwachsRate, ggewichtBeute);
        Raeuber raeuber = new Raeuber(raeuberBiomasse, verlustRate, ggewichtRaeuber);

        graph = Sim.getGraphBH(time, step, beute, raeuber);

        XYChart.Series<Double, Double> beuteSeries = new XYChart.Series<Double, Double>();
        XYChart.Series<Double, Double> raeuberSeries = new XYChart.Series<Double, Double>();

        beuteSeries.setName("Beute");
        raeuberSeries.setName("Räuber");

        //Die Schritte (Steps) sind x < 1, daher werden diese für die For-Schleife in ein Int-Wert berechnet
        //zb: Step = 0.5; Grenze = 100; DH scheife muss 200x durchlaufen (100/0.5)
        length = GuiUtil.getTime(time, step);

        /** if / else Darstellung Diagramm:
         * Wenn Checkbox = false ist, werden alle Daten aus dem Array in eine Series übergeben und
         * nachdem es vollständig durchgelaufen ist, ausgegeben.
         * Wenn Chechbox = false ist, werden alle 100 millisekunden ein weiterer Wert im Diagramm dargestellt.
         */
        if (!isSelected)
        {
            for (int i = 0; i < length; i++)
            {
                beuteSeries.getData().add(new XYChart.Data(graph[i][0], graph[i][1]));
                raeuberSeries.getData().add(new XYChart.Data(graph[i][0], graph[i][2]));
            }

            linechart1.getData().addAll(beuteSeries, raeuberSeries);
        }
        else
        {
            /**
             * Änhlich wie eine Schleife, nur dass eine Iteration 100 Millisekunden warten soll.
             */
            graph = Sim.getGraphBH(10000, step, beute, raeuber);
            var ref = new Object() {
                int i = 0;
        };
            Double[][] finalGraph = graph;
            wait.setOnFinished((e) -> {
                beuteSeries.getData().add(new XYChart.Data(finalGraph[ref.i][0], finalGraph[ref.i][1]));
                raeuberSeries.getData().add(new XYChart.Data(finalGraph[ref.i][0], finalGraph[ref.i][2]));
                linechart1.getData().addAll(beuteSeries, raeuberSeries);
                ref.i++;
                wait.playFromStart();
            });
            wait.play();
        }
        Beute beuteData = new Beute(beuteBiomasse, zuwachsRate, ggewichtBeute);
        Raeuber raeuberData = new Raeuber(raeuberBiomasse, verlustRate, ggewichtRaeuber);

        Sim sim = new Sim(step, beuteBiomasse, raeuberBiomasse);

        tableView.getColumns().clear();
        tableView.setItems(sim.getTableDataBH(time, beuteData, raeuberData));
        tableView.getColumns().addAll(tableColumnTimestep, tableColumnBeute, tableColumnRaeuber);

    }

    /**
     * Stopt die 100 Millisekunden Scheife, setzt den Graphen und das TableView zurück auf Standard
     */
    public void handleButtonClear() {
        wait.stop();
        linechart1.getData().clear();
        tableView.getColumns().clear();
    }

    /**
     * Schließt die aktuelle Stage und öffnet die Mainstage
     *
     */
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

    /** Speichern der Daten in die Datenbank
     * Methoden aus dem DB.Package zur Erstellung des SQL-Codes Verbindung der Datenbank.
     * Erstellung der Datenbank, Tabellen und Spalten folgt statisch, das Hinzufügen der Daten jedoch dynamisch.
     */
    public void handleButtonDaten(ActionEvent actionEvent) {
        //Tabellenname ist durch die Zeit der Erstellung ergänzt
        Date zeitstempel = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH_mm_dd_MM_yy");
    try {
        Connection connection = getConnection();
        Schema schema = new Schema("raeuber_biomasse" + simpleDateFormat.format(zeitstempel));
        System.out.println(schema.drop());
        System.out.println(schema.create());
        System.out.println(schema.use());
//        updateStatement(connection, schema.drop());
//        updateStatement(connection, schema.create());
//        updateStatement(connection, schema.use());

        Table biomasse = new Table("Biomasse");

        biomasse.addPrimaryKey("Timestep", Type.DECIMAL);
        biomasse.addAttr("Biomasse_Beute", Type.DECIMAL);
        biomasse.addAttr("Biomasse_Räuber", Type.DECIMAL);

        System.out.println(biomasse.create());
//        updateStatement(connection, biomasse.create());

        for (int i = 0; i < length; i++)
        {
            Update biomasseUpdate = new Update("Biomasse");
//            updateStatement(connection, biomasseUpdate.insertData(graph[i][0], graph[i][1],graph[i][2]));
            System.out.println(biomasseUpdate.insertData(graph[i][0], graph[i][1],graph[i][2]));
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


