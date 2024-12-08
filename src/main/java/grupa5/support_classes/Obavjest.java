package grupa5.support_classes;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Obavjest {

    public static void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    
        ButtonType proceedButton = new ButtonType("Nastavi");
        ButtonType cancelButton = new ButtonType("Odustani");
    
        alert.getButtonTypes().setAll(proceedButton, cancelButton);
    
        Optional<ButtonType> result = alert.showAndWait();
    
        if (result.isPresent()) {
            if (result.get() == proceedButton) {
                return true;
            } else if (result.get() == cancelButton) {
                return false;
            }
        }

        return false;
    }    
}
