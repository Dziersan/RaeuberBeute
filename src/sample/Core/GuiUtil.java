package sample.Core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class GuiUtil {

    public static Double[][] getGraphxAxis(int length, double timestep) {

        length = getTime(length, timestep);

        Double[][] graphxAxis = new Double[length][9];

        double time = 0;
        for (int i = 0; i < length; i++) {
            graphxAxis[i][0] = time;
            time += timestep;
            time = Math.round(100.0 * time) / 100.0;
        }
        return graphxAxis;
    }

    /** Methode für das anpassen der x < 1 Timesteps
     */
    public static int getTime(double length, double timestep) {
        return (int) (length = (length / timestep));
    }

    public static double round(double value) {
        return Math.round(100.0 * value) / 100.0;
    }

    /** Constraints für die Textfelder. Damit soll vermieden werden, dass Buchstaben; Mehr als 4 Nachkommastellen;
     * Mehr als ein "." eingegeben werden können.
     */
    public static void setTextield(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
                    textField.setText(oldValue);
                }
            }
        });
    }
}