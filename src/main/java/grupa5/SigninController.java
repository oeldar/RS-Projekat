package grupa5;

import grupa5.baza_podataka.Korisnik.TipKorisnika;
import grupa5.baza_podataka.KorisnikService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class SigninController {
    @FXML
    private Button organizatorBtn;

    @FXML
    private TextField emailField;

    @FXML
    private ImageView errorIcon;

    @FXML
    private Label errorLabel;

    @FXML
    private Button kupacBtn;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private Button signinButton;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField usernameField;

    private TipKorisnika selectedTipKorisnika = null;
    EntityManagerFactory emf;
    private KorisnikService korisnikService;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        korisnikService = new KorisnikService(emf);
    }


    @FXML
    void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.equals(KeyCode.ENTER)) handleSigninButtonAction(null);
        else if (keyCode.equals(KeyCode.ESCAPE)) closeWindow();
    }

    @FXML
    void handleSigninButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String password2 = passwordField2.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();

        boolean isValid = validateInput(username, email, password, password2, name, surname);

        if (isValid && selectedTipKorisnika != null) {
            korisnikService.kreirajKorisnika(username, email, name, surname, password, selectedTipKorisnika);

            System.out.println("Uspješna registracija. Čekanje na verifikaciju od strane admina.");

            // Sakrivanje indikatora greške
            errorLabel.setVisible(false);
            errorIcon.setVisible(false);
            closeWindow();
        } else {
            errorLabel.setVisible(true);
            errorIcon.setVisible(true);
        }
    }

    @FXML
    void handleOrganizatorButtonAction(ActionEvent event) {
        selectedTipKorisnika = TipKorisnika.ORGANIZATOR;
        organizatorBtn.setStyle("-fx-background-color: #3875ce; -fx-text-fill: #fcfefa;");
        kupacBtn.setStyle("");  // Resetiranje stila za Kupac dugme
    }

    @FXML
    void handleKupacButtonAction(ActionEvent event) {
        selectedTipKorisnika = TipKorisnika.KORISNIK;
        kupacBtn.setStyle("-fx-background-color: #3875ce; -fx-text-fill: #fcfefa;");
        organizatorBtn.setStyle("");  // Resetiranje stila za Organizator dugme
    }

    private boolean validateInput(String username, String email, String password, String password2, String name, String surname) {
        boolean isValid = true;

        // Provjera da li su sva polja popunjena
        if (username.isEmpty() || email.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            isValid = false;
            errorLabel.setText("Sva polja moraju biti popunjena.");
            return isValid;
        }

        // Provjera da li se lozinke podudaraju
        if (!password.equals(password2)) {
            isValid = false;
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            passwordField2.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            errorLabel.setText("Lozinke se ne podudaraju.");
            return isValid;
        } else {
            passwordField.setStyle("");
            passwordField2.setStyle("");
        }

        // Provjera validnosti email adrese
        if (!isValidEmail(email)) {
            isValid = false;
            emailField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            errorLabel.setText("Neispravan format email adrese.");
            return isValid;
        } else {
            emailField.setStyle("");
        }

        // Provjera snage lozinke
        if (!isStrongPassword(password)) {
            isValid = false;
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            errorLabel.setPrefWidth(600);
            errorLabel.setText("Lozinka mora imati najmanje 8 znakova, uključujući slova i brojeve.");
            return isValid;
        } else {
            passwordField.setStyle("");
        }

        if (korisnikService.pronadjiKorisnika(username) != null) {
            isValid = false;
            usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            errorLabel.setText("Korisničko ime je već zauzeto.");
            return isValid;
        } else {
            usernameField.setStyle("");
        }

        if (korisnikService.pronadjiKorisnikaPoEmailu(email) != null) {  // You need to implement this method in KorisnikService
            isValid = false;
            emailField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
            errorLabel.setText("Email adresa je već u upotrebi.");
            return isValid;
        } else {
            emailField.setStyle("");
        }

        // Dodatne provjere mogu se dodati ovdje...

        return isValid;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isStrongPassword(String password) {
        // Lozinka mora imati najmanje 8 znakova, uključujući barem jedno slovo i jedan broj
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    private void closeWindow() {
        Stage stage = (Stage) signinButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void close() {
        emf.close();
    }
}