package grupa5;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.KorisnikService;
import grupa5.baza_podataka.TipKorisnika;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SigninController {

    @FXML
    private Button SigninButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField usernameField;

    @FXML
    void handleSigninButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String password2 = passwordField2.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();

        // Jednostavna provjera unosa (npr. provjera da li polja nisu prazna)
        boolean isValid = !username.isEmpty() && !email.isEmpty() && !name.isEmpty()
             && !surname.isEmpty() && !password.isEmpty() && password.equals(password2);

        if (isValid) {
            //usernameField.setStyle("-fx-border-width: 0px;");
           // passwordField.setStyle("-fx-border-width: 0px;");
            // Resetirajte stilove i poruke
            //  usernameField.setStyle("");
            //  usernameField.setStyle("");
            //  errorLabel.setText("");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
            KorisnikService korisnikService = new KorisnikService(emf);
            korisnikService.kreirajKorisnika(username, email, name, surname, password, TipKorisnika.ADMINISTRATOR);
            emf.close();

            // Nastavite sa validnim unosom (npr. otvaranje nove scene)
            System.out.println("Uspješna registracija");
        } else {
            System.out.println("Neuspjesna registracija");
            // Postavite crveni okvir i prikažite poruku o grešci
            // usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            // passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
        }
    }
}
