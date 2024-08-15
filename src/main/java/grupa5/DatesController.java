package grupa5;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DatesController {

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }
        
    @FXML
    private DatePicker startDatePicker, endDatePicker;

    @FXML
    private ImageView priceErrorIcon;

    @FXML
    private Label priceErrorText;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private void handleDodaj() {

        if (areValidDates(startDatePicker, endDatePicker)) {
            resetError();
            String startDate;
            String endDate;
            
            startDate = startDatePicker.getValue().format(formatter);
            endDate = endDatePicker.getValue().format(formatter);
        
            // Zatvaranje prozora
            Stage stage = (Stage) startDatePicker.getScene().getWindow();
            stage.close();

            mainScreenController.updateDates(startDate, endDate);

        } else showError();
    }

    private boolean areValidDates(DatePicker startDatePicker, DatePicker endDatePicker) {
        LocalDate startDate = startDatePicker.getValue(), endDate = endDatePicker.getValue();
        LocalDate today = LocalDate.now();

        return startDate == null || endDate == null || 
            startDate.isAfter(endDate) || !startDate.isAfter(today)
            ? false : true;
    }

    private void showError() {
        startDatePicker.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
        endDatePicker.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
        priceErrorIcon.setVisible(true);
        priceErrorText.setVisible(true);
    }

    private void resetError() {
        startDatePicker.setStyle("-fx-border-width: 0px;");
        endDatePicker.setStyle("-fx-border-width: 0px;");
        priceErrorIcon.setVisible(false);
        priceErrorText.setVisible(false);
    }
}
