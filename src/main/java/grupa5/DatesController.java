package grupa5;

import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class DatesController {

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }
        
    @FXML
    private DatePicker startDatePicker, endDatePicker;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");




    @FXML
    private void handleDodaj() {
        String startDate;
        String endDate;

        startDate = startDatePicker.getValue().format(formatter);
        endDate = endDatePicker.getValue().format(formatter);


        mainScreenController.updateDates(startDate, endDate);

        // Zatvaranje prozora
        Stage stage = (Stage) startDatePicker.getScene().getWindow();
        stage.close();

    }
}
