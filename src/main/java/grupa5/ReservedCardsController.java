package grupa5;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ReservedCardsController {

    @FXML
    private VBox reservedCardsVBox;

    public void initialize() {
        try {
            // Dodajemo više kartica u VBox
            for (int i = 0; i < 5; i++) {  // Dodajemo 5 kartica (ovo je samo primjer)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                AnchorPane reservedCardNode = loader.load();

                // Ovde možeš postaviti specifične podatke za svaku karticu koristeći njen kontroler ako je potrebno
                ReservedCardController controller = loader.getController();
                // controller.setEventData(event); // Možeš dodati metodu za postavljanje podataka za događaj

                reservedCardsVBox.getChildren().add(reservedCardNode);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
