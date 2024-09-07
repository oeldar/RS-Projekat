package grupa5.support_classes;

import javafx.scene.control.Alert;

public class Obavjest {

    public static void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
