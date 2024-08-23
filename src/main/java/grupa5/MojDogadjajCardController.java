package grupa5;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MojDogadjajCardController {
    @FXML
private void urediDogadjaj(ActionEvent event) {
    try {
        // Učitaj FXML datoteku
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/urediDogadjaj.fxml"));
        Parent root = loader.load();

        // Kreiraj novi Stage
        Stage stage = new Stage();
        stage.setTitle("Uredi Događaj");
        stage.setScene(new Scene(root));

        // Prikaži novi prozor
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
