package grupa5;

import grupa5.baza_podataka.Korisnik;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

    private EntityManagerFactory entityManagerFactory;

    public LoginController() {
        entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Provjera unosa
        boolean isValid = !username.isEmpty() && !password.isEmpty();

        // Provjerava da li je korisnik u bazi podataka
        boolean isInDatabase = isUserInDatabase(username, password);

        if (isValid && isInDatabase) {
            resetErrorStyles();
            System.out.println("Uspješna prijava");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        } else {
            showErrorStyles();
            System.out.println("Neuspješna prijava");
            // Dodatna logika za prikaz specifičnih grešaka
            if (!isValid) {
                errorLabel.setText("Polja ne smiju biti prazna.");
            } else if (!isInDatabase) {
                errorLabel.setText("Korisničko ime ili lozinka nisu ispravni.");
            }
            errorLabel.setVisible(true);
            errorLabel1.setVisible(true);
            errorIcon.setVisible(true);
        }
    }

    private boolean isUserInDatabase(String username, String password) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            
            // Pronađite korisnika po korisničkom imenu
            Korisnik user = entityManager.find(Korisnik.class, username);
            
            // Proverite da li je korisnik pronađen i da li lozinka odgovara
            return user != null && user.getLozinka().equals(password) && user.getStatusVerifikacije().equals(Korisnik.StatusVerifikacije.VERIFIKOVAN);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void resetErrorStyles() {
        errorLabel.setVisible(false);
        errorLabel1.setVisible(false);
        errorIcon.setVisible(false);
        usernameField.setStyle("-fx-border-width: 0px;");
        passwordField.setStyle("-fx-border-width: 0px;");
    }

    private void showErrorStyles() {
        usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
        passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
    }
}