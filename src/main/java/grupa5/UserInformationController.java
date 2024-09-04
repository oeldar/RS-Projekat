package grupa5;

import java.io.IOException;
import java.io.InputStream;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.services.KorisnikService;
import grupa5.support_classes.ImageSelector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

@SuppressWarnings("exports")
public class UserInformationController {
    private static final String PERSISTENCE_UNIT_NAME = "HypersistenceOptimizer";

    @FXML
    private HBox currentPasswordError;

    @FXML
    private PasswordField firstTryPasswordField;

    @FXML
    private Label mailLabel;

    @FXML
    private HBox mainError;

    @FXML
    private Label nameLabel;

    @FXML
    private HBox newPasswrodError;

    @FXML
    private AnchorPane newProfileImage;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private ImageView profileImage;

    @FXML
    private Label roleLabel;

    @FXML
    private PasswordField secondTryPasswordField;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label mainErrorLabel;

    @FXML
    private Label currentPasswordErrorLabel;

    @FXML
    private Label newPasswordErrorLabel;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private KorisnikService korisnikService;
    private Korisnik korisnik;
    private String username;

    private String pathToImage;
    private String currentPassword;
    private String enteredCurrentPassword;
    private String enteredFirstTryPassword;
    private String enteredSecondTryPassword;

    private boolean changingPicture = false;

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            initializePersistence();
            showUserInfo();
            showProfileImage();
        });
    }

    private void initializePersistence() {
        try {
            if (entityManagerFactory == null) {
                System.out.println("ne prenosi se entity manager");
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
            entityManager = entityManagerFactory.createEntityManager();
            korisnikService = new KorisnikService(entityManagerFactory);
        } catch (Exception e) {
            System.err.println("Failed to initialize persistence unit: " + e.getMessage());
            closeWindow();
        }
    }

    private void showUserInfo() {
        if (korisnik != null) {
            nameLabel.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            usernameLabel.setText("Korisničko ime: " + "@" + korisnik.getKorisnickoIme());
            mailLabel.setText("Email: " + korisnik.getEmail());
            roleLabel.setText(korisnik.getTipKorisnika().toString());
            currentPassword = korisnik.getLozinka();
            username = korisnik.getKorisnickoIme();
        } else {
            nameLabel.setText("N/A");
            usernameLabel.setText("N/A");
            mailLabel.setText("N/A");
        }
    }

    private void showProfileImage() {
        pathToImage = korisnik.getPutanjaDoSlike();
        if (pathToImage == null || pathToImage.isEmpty()) {
            pathToImage = "/grupa5/assets/users_photos/" + roleLabel.getText() + ".png";
        }

        try (InputStream inputStream = getClass().getResourceAsStream(pathToImage)) {
            if (inputStream != null) {
                Image image = new Image(inputStream);
                profileImage.setImage(image);
            } else {
                profileImage.setImage(
                        new Image("/grupa5/assets/users_photos/" + roleLabel.toString() + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            profileImage
                    .setImage(new Image("/grupa5/assets/users_photos/" + roleLabel.toString() + ".png"));
        }
        profileImage = ImageSelector.clipToCircle(profileImage, 75);
    }

    @FXML
    void editPicture(MouseEvent event) {
        System.out.println("editing picture");
        Image image = ImageSelector.selectImage(getStage());

        if (image != null) {
            changingPicture = true;
            profileImage.setImage(image);
            profileImage = ImageSelector.clipToCircle(profileImage, 75);
        }
    }

    @FXML
    void applyChanglesButtonClicked(ActionEvent event) {
        hideErrors();
        if (isChangingPassword() && isChangingPicture()) {
            changePassword();
            changeProfilePicture();
        } else if (isChangingPassword())
            changePassword();
        else if (isChangingPicture())
            changeProfilePicture();
        else
            showErrorForNothingChanged();
    }

    private boolean isChangingPassword() {
        enteredCurrentPassword = oldPasswordField.getText();
        enteredFirstTryPassword = firstTryPasswordField.getText();
        enteredSecondTryPassword = secondTryPasswordField.getText();

        return !enteredCurrentPassword.isEmpty() ||
                !enteredFirstTryPassword.isEmpty() ||
                !enteredSecondTryPassword.isEmpty();
    }

    private boolean isChangingPicture() {
        return changingPicture;
    }

    private void changePassword() {
        if (arePasswordsValid()) {
            korisnikService.promijeniLozinku(username, enteredFirstTryPassword);
        }
        if (mainScreenController != null) {
            korisnik = korisnikService.pronadjiKorisnika(username);
            mainScreenController.setKorisnik(korisnik);
        }
    }

    private boolean arePasswordsValid() {
        if (!areAllFieldsFilled())
            return false;
        if (!isValidCurrentPassword())
            return false;
        if (isNewPasswordSame())
            return false;
        if (newPasswordTooShort())
            return false;
        if (!areNewPasswordsEqual())
            return false;
        return true;
    }

    private boolean areAllFieldsFilled() {
        boolean returnValue = true;

        if (enteredCurrentPassword.isEmpty()) {
            showError(oldPasswordField, currentPasswordErrorLabel, currentPasswordError);
            returnValue = false;
        }

        if (enteredFirstTryPassword.isEmpty()) {
            showError(firstTryPasswordField, newPasswordErrorLabel, newPasswrodError);
            returnValue = false;
        }

        if (enteredSecondTryPassword.isEmpty()) {
            showError(secondTryPasswordField, newPasswordErrorLabel, newPasswrodError);
            returnValue = false;
        }

        return returnValue;
    }

    private boolean isValidCurrentPassword() {
        if (currentPassword.equals(enteredCurrentPassword))
            return true;
        else {
            setErrorBorder(oldPasswordField);
            currentPasswordErrorLabel.setText("Neodgovarajuca lozinka");
            currentPasswordError.setVisible(true);
            return false;
        }
    }

    private boolean isNewPasswordSame() {
        if (currentPassword.equals(enteredFirstTryPassword)) {
            setErrorBorder(firstTryPasswordField);
            newPasswordErrorLabel.setText("Trenutna i nova lozinka ne mogu biti iste!");
            newPasswrodError.setVisible(true);
            return true;
        }
        return false;
    }

    private boolean newPasswordTooShort() {
        if (enteredFirstTryPassword.length() < 8) {
            setErrorBorder(firstTryPasswordField);
            newPasswordErrorLabel.setText("Lozinka mora imati najmanje 8 znakova!");
            newPasswrodError.setVisible(true);
            return true;
        }
        return false;
    }

    private boolean areNewPasswordsEqual() {
        if (enteredFirstTryPassword.equals(enteredSecondTryPassword))
            return true;
        else {
            setErrorBorder(secondTryPasswordField);
            newPasswordErrorLabel.setText("Neispravan unos");
            newPasswrodError.setVisible(true);
            return false;
        }
    }

    private void changeProfilePicture() {
        String imagePath = "/grupa5/assets/users_photos/" + username + ".png";
        ImageSelector.copyImageTo(imagePath);

        korisnikService.postaviSliku(username, imagePath);
        korisnik = korisnikService.pronadjiKorisnika(username);

        mainScreenController.setKorisnik(korisnik);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainScreenController.setUpdatedImage(profileImage.getImage());

    }

    private void showErrorForNothingChanged() {
        mainErrorLabel.setText("Nema promjena");
        mainError.setVisible(true);
    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case ENTER -> applyChanglesButtonClicked(null);
            case ESCAPE -> closeWindow();
            default -> {
            }
        }
    }

    private void closeWindow() {
        Stage stage = getStage();
        stage.close();
    }

    private Stage getStage() {
        return (Stage) oldPasswordField.getScene().getWindow();
    }

    private void setErrorBorder(PasswordField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2.5px;");
    }

    private void removeErrorBorder(PasswordField field) {
        field.setStyle("-fx-border-width: 0px;");
    }

    private void showError(PasswordField passwordField, Label errorLabel, HBox error) {
        setErrorBorder(passwordField);
        errorLabel.setText("Nedostaje unos!");
        error.setVisible(true);
    }

    private void hideErrors() {
        removeErrorBorder(oldPasswordField);
        removeErrorBorder(firstTryPasswordField);
        removeErrorBorder(secondTryPasswordField);
        mainError.setVisible(false);
        currentPasswordError.setVisible(false);
        newPasswrodError.setVisible(false);
    }
}
