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
import sample.Core.*;
import sample.DB.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static sample.Core.GuiUtil.*;
import static sample.DB.JDBC.getConnection;
import static sample.Core.GuiUtil.setTextield;
import static sample.DB.JDBC.updateStatement;

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

    /** initialize
     * Constraints der Textfelder und Zuweisung der Spalten von dem Tableview
     * setTextfield ist eine Methode aus der GuiUtil Klasse, die die Textfelder dadurch beschränkt,
     * dass keine Buchstaben, maximal ein Punkt (Für double) und vier Nachkommastellen erlaubt.
     */
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

        //Der Name muss mit der Variablen aus der Klasse übereinstimmen, da er auf die Getter zugreift.
        tableColumnTimestep.setCellValueFactory(new PropertyValueFactory<Sim, Double>("timestep"));
        tableColumnHase.setCellValueFactory(new PropertyValueFactory<Sim, Double>("beuteBiomasse"));
        tableColumnFuchs.setCellValueFactory(new PropertyValueFactory<Sim, Double>("raeuberBiomasse"));
        tableColumnWeide.setCellValueFactory(new PropertyValueFactory<Sim, Double>("weideData"));
    }

    public void handleButtonStart(){
        //Zuweisung der Textfeld-Werten in die zuvor deklarierten Variablen
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

    //Die Schritte (Steps) sind x < 1, daher werden diese für die For-Schleife in ein Int-Wert berechnet
    //zb: Step = 0.5; Grenze = 100; DH scheife muss 200x durchlaufen (100/0.5)
    length = getTime(time, step);

    /** if / else Darstellung Diagramm:
     * Wenn Checkbox = false ist, werden alle Daten aus dem Array in eine Series übergeben und
     * nachdem es vollständig durchgelaufen ist, ausgegeben.
     * Wenn Chechbox = false ist, werden alle 100 millisekunden ein weiterer Wert im Diagramm dargestellt.
     */
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
        /**
         * Änhlich wie eine Schleife, nur dass eine Iteration 100 Millisekunden warten soll.
         */
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

    //Erstellen der Klassen für die Werte des TableViews
    Hase haseData = new Hase(haseBiomasse,zuwachsRate, ggewichtHase);
    Fuchs fuchsData = new Fuchs(fuchsBiomasse,verlustRate,ggewichtFuchs);
    Weide weideData = new Weide(1000,abBegrenzung,bisBegrenzung);

    Sim sim = new Sim(step, haseBiomasse, fuchsBiomasse, 1000);

/**
 *
 */
    tableView.getColumns().clear();
    tableView.setItems(sim.getTableData(time, haseData, fuchsData, weideData));
    tableView.getColumns().addAll(tableColumnTimestep, tableColumnHase, tableColumnFuchs, tableColumnWeide);
    }

    /**
     * Stopt die 100 Millisekunden Scheife, setzt den Graphen und das TableView zurück auf Standard
     */
    public void handleButtonClear(){
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

    /** Speichern der Daten in die Datenbank
     * Methoden aus dem DB.Package zur Erstellung des SQL-Codes Verbindung der Datenbank.
     * Erstellung der Datenbank, Tabellen und Spalten folgt statisch, das Hinzufügen der Daten jedoch dynamisch.
     */
    public void handleButtonDaten(ActionEvent event) {
        Date zeitstempel = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH_mm_dd_MM_yy");
        try {
            Connection connection = getConnection();
            Schema schema = new Schema("hase_fuchs" + simpleDateFormat.format(zeitstempel));

            updateStatement(connection, schema.drop());
            updateStatement(connection, schema.create());
            updateStatement(connection, schema.use());

            Table biomasse_fuchs = new Table("Biomasse_Fuchs");
            Table biomasse_hase = new Table("Biomasse_Hase");
            Table weide = new Table("Weide");

            biomasse_fuchs.addPrimaryKey("Timestep_Fuchs", Type.DECIMAL);
            biomasse_fuchs.addAttr("Biomasse_Fuchs", Type.DECIMAL);
            biomasse_fuchs.addAttr("Treffen_Value", Type.DECIMAL);

            biomasse_hase.addPrimaryKey("Timestep_Hase", Type.DECIMAL);
            biomasse_hase.addAttr("Biomasse_Hase", Type.DECIMAL);
            biomasse_hase.addAttr("Treffen_Value", Type.DECIMAL);

            weide.addPrimaryKey("Timestep_Weide", Type.DECIMAL);
            weide.addAttr("Weideflaeche", Type.DECIMAL);

            updateStatement(connection, biomasse_fuchs.create());
            updateStatement(connection, biomasse_hase.create());
            updateStatement(connection, weide.create());

            for (int i = 0; i < length; i++)
            {
                Update biomasseFuchsUpdate = new Update("Biomasse_Fuchs");
                updateStatement(connection, biomasseFuchsUpdate.insertData(graph[i][0], graph[i][2],graph[i][5]));
                Update biomasseHaseUpdate = new Update("Biomasse_Hase");
                updateStatement(connection, biomasseHaseUpdate.insertData(graph[i][0], graph[i][1],graph[i][4]));
                Update biomasseWeideUpdate = new Update("Weide");
                updateStatement(connection, biomasseWeideUpdate.insertData(graph[i][0], graph[i][3]));
            }

//            System.out.println((createView.getSqlStatement()));

        }

        catch(SQLException e)
        {
            System.out.println("Fehlermeldung: " + e.getMessage());
            System.out.println("SQL State : " + e.getSQLState());
            e.printStackTrace();
        }
    }
}

