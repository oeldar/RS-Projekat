package grupa5;

import grupa5.support_classes.FilterService;

// import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
// import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PriceController {

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }
        
    @FXML
    private TextField startPriceField, endPriceField;

    @FXML
    private ImageView priceErrorIcon;

    @FXML
    private Label priceErrorText;

    @FXML
    void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.equals(KeyCode.ENTER)) handleDodaj();
        else if (keyCode.equals(KeyCode.ESCAPE)) closeWindow();
    }

    @FXML
    private void handleDodaj() {
        String startPrice, endPrice;
        startPrice = startPriceField.getText();
        endPrice = endPriceField.getText();
        
        if (areValidPrices(startPrice, endPrice)) {
            resetError();

            FilterService filterService = FilterService.getInstance();
            filterService.setStartPrice(startPrice);
            filterService.setEndPrice(endPrice);

            // Zatavaranje prozora
            Stage stage = (Stage) startPriceField.getScene().getWindow();
            stage.close();

            mainScreenController.updatePrice(startPrice, endPrice);

        } else showError();

    }

    private boolean areValidPrices(String startPrice, String endPrice) {
        try {
            Integer lowerPrice = Integer.parseInt(startPrice), higherPrice = Integer.parseInt(endPrice);
            return (lowerPrice < 0 || higherPrice <= 0 || (higherPrice <= lowerPrice)) ? false : true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showError() {
        startPriceField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
        endPriceField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
        priceErrorIcon.setVisible(true);
        priceErrorText.setVisible(true);
    }

    private void resetError() {
        startPriceField.setStyle("-fx-border-width: 0px;");
        endPriceField.setStyle("-fx-border-width: 0px;");
        priceErrorIcon.setVisible(false);
        priceErrorText.setVisible(false);
    }

    private void closeWindow() {
        Stage stage = (Stage) startPriceField.getScene().getWindow();
        stage.close();
    }

}
