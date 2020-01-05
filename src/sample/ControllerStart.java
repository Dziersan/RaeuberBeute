package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ControllerStart {


    public void gotoHaseFuchs(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("HaseFuchsLineGraph.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Hase und Fuchs mit begrenzter Weidekapazität");
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
        }
    }

    public void gotoBeuteRaeuber(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader();
            fxmlLoader1.setLocation(getClass().getResource("BeuteRaeuberLineGraph.fxml"));
            Scene scene = new Scene(fxmlLoader1.load());
            Stage stage = new Stage();
            stage.setTitle("Beute und Räuber mit unbegrenzter Weidekapazität");
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
        }
    }
}
