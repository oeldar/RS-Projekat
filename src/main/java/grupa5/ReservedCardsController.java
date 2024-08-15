package grupa5;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ReservedCardsController {

    @FXML
    private VBox reservedCardsVBox;

    public void initialize() {
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                AnchorPane reservedCardNode = loader.load();

                reservedCardsVBox.getChildren().add(reservedCardNode);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
