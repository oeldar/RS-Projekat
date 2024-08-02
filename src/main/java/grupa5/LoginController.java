package grupa5;

import jakarta.persistence.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel, errorLabel1;

    @FXML
    private ImageView errorIcon;

    // @FXML
    // private HBox usernameBox;

    // @FXML
    // private HBox passwordBox;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Jednostavna provjera unosa (npr. provjera da li polja nisu prazna)
        boolean isValid = !username.isEmpty() && !password.isEmpty();

        if (isValid) {
            errorLabel.setVisible(false);
            errorLabel1.setVisible(false);
            errorIcon.setVisible(false);
            usernameField.setStyle("-fx-border-width: 0px;");
            passwordField.setStyle("-fx-border-width: 0px;");
            // Resetirajte stilove i poruke
            //  usernameField.setStyle("");
            //  usernameField.setStyle("");
            //  errorLabel.setText("");


            // Nastavite sa validnim unosom (npr. otvaranje nove scene)
            System.out.println("Uspješna prijava");
        } else {
            System.out.println("Neuspjesna prijava");
            // Postavite crveni okvir i prikažite poruku o grešci
            usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            errorLabel.setVisible(true);
            errorLabel1.setVisible(true);
            errorIcon.setVisible(true);
        }
    }
}
