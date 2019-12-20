package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class Main extends Application{



    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("guiStart.fxml"));
        primaryStage.setTitle("Raeuber und Beute");
        primaryStage.setScene(new Scene(root, 480, 320));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
