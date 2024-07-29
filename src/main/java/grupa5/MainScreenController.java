package grupa5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainScreenController {
     @FXML
    private Label testLabel;

    private Stage loginStage; // Referenca na prozor za prijavu

    @FXML
    void testBtnClicked(ActionEvent event) {
         try {
            if (loginStage == null || !loginStage.isShowing()) {
                // Učitajte login.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/login.fxml"));
                Parent root = fxmlLoader.load();

                // Kreirajte novi Stage
                loginStage = new Stage();
                loginStage.setTitle("Login Window");
                loginStage.setScene(new Scene(root, 800, 600));
                loginStage.setResizable(false); // Onemogućite promenu veličine

                // Postavite modalnost
                loginStage.initModality(Modality.APPLICATION_MODAL);

                // Prikažite prozor
                loginStage.showAndWait(); // showAndWait() čeka da se prozor zatvori

                // Dodajte slušaoca događaja za zatvaranje prozora
                loginStage.setOnCloseRequest(e -> loginStage = null);
            } else {
                // Fokusirajte prozor ako je već otvoren
                loginStage.toFront();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
