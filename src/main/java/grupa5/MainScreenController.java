package grupa5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainScreenController {
     @FXML
    private Label testLabel;

    @FXML
    void testBtnClicked(ActionEvent event) {
        testLabel.setText("radi");
    }
}
