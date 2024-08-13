package grupa5;

// import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
// import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PriceController {

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }
        
    @FXML
    private TextField startPriceField, endPriceField;

    

    @FXML
    private void handleDodaj() {
        String startPrice, endPrice;
        startPrice = startPriceField.getText();
        endPrice = endPriceField.getText();
        

        mainScreenController.updatePrice(startPrice, endPrice);

        // Zatvaranje prozora
        Stage stage = (Stage) startPriceField.getScene().getWindow();
        stage.close();

    }
}
