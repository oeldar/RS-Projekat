package grupa5;

// import org.mindrot.jbcrypt.BCrypt;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private EntityManager entityManager;
    private MainScreenController mainScreenController;

    public void setMainController(MainScreenController mainController) {
        this.mainScreenController = mainController;
    }

    public LoginController() {
        // Initialize the EntityManagerFactory and EntityManager
        entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.equals(KeyCode.ENTER)) handleLoginButtonAction(null);
        else if (keyCode.equals(KeyCode.ESCAPE)) closeWindow();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        boolean isNotEmpty = !username.isEmpty() && !password.isEmpty();
        boolean isInDatabase = isUserInDatabase(username, password);
        boolean isValid = isValidated(username);

        if (isValid && isInDatabase && isNotEmpty) {
            resetErrorStyles();
            System.out.println("Uspješna prijava");
            mainScreenController.setLoggedInUsername(username);
            Korisnik user = entityManager.find(Korisnik.class, username);
            if (user != null) {
                mainScreenController.setTipKorisnika(user.getTipKorisnika().toString());
                mainScreenController.setKorisnik(user);
                mainScreenController.updateUIForLoggedInUser();
                mainScreenController.prikaziKorisnika();
            }
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        } else {
            showErrorStyles();
            System.out.println("Neuspješna prijava");
            if (!isNotEmpty) {
                errorLabel.setText("Polja ne smiju biti prazna.");
                errorLabel1.setText("");
            } else if (!isInDatabase) {
                errorLabel.setText("Korisničko ime ili lozinka nisu ispravni.");
                errorLabel1.setText("");
            } else if (!isValid) {
                errorLabel.setText("Čeka se na verifikaciju.");
                errorLabel1.setText("Pokušajte kasnije.");
            }
            errorLabel.setVisible(true);
            errorLabel1.setVisible(true);
            errorIcon.setVisible(true);
        }
    }

    private boolean isValidated(String username) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            Korisnik user = entityManager.find(Korisnik.class, username);
            return user != null && user.getStatusVerifikacije().equals(Korisnik.StatusVerifikacije.VERIFIKOVAN);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private boolean isUserInDatabase(String username, String password) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            Korisnik user = entityManager.find(Korisnik.class, username);
            return user != null && password.equals(user.getLozinka());
            // return user != null && BCrypt.checkpw(password, user.getLozinka());
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

    private void closeWindow() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}