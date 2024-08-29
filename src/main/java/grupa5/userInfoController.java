package grupa5;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.KorisnikService;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserInfoController {
    private static final String PERSISTENCE_UNIT_NAME = "HypersistenceOptimizer";

    @FXML
    private Button addImageButton;

    @FXML
    private PasswordField firstTryPasswordField;

    @FXML
    private Label mailLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private AnchorPane newProfileImage;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private ImageView profileImage;

    @FXML
    private AnchorPane removeImgaePane;

    @FXML
    private Label roleLabel;

    @FXML
    private PasswordField secondTryPasswordField;

    @FXML
    private Label usernameLabel;

    private String pathToImage;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private KorisnikService korisnikService;
    private Korisnik korisnik;

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
            usernameLabel.setText("@" + korisnik.getKorisnickoIme());
            mailLabel.setText(korisnik.getEmail());
            roleLabel.setText(korisnik.getTipKorisnika().toString());
            
        } else {
            nameLabel.setText("N/A");
            usernameLabel.setText("N/A");
            mailLabel.setText("N/A");
        }
    }

    private void showProfileImage() {
        pathToImage = korisnik.getPutanjaDoSlike();
        if (pathToImage == null || pathToImage.isEmpty()) {
            pathToImage = "/grupa5/assets/users_photos/" + roleLabel.getText().toLowerCase() + ".png";
        }
        
        try (InputStream inputStream = getClass().getResourceAsStream(pathToImage)) {
            if (inputStream != null) {
                Image image = new Image(inputStream);
                profileImage.setImage(image);
            } else {
                profileImage.setImage(new Image("/grupa5/assets/users_photos/" + roleLabel.toString().toLowerCase() + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            profileImage.setImage(new Image("/grupa5/assets/users_photos/" + roleLabel.toString().toLowerCase() + ".png"));
        }
    }

    @FXML
    void applyChanglesButtonClicked(ActionEvent event) {

    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case ENTER -> applyChanglesButtonClicked(null);
            case ESCAPE -> closeWindow();
            default -> {}
        }
    }

    @FXML
    void removeImage(ActionEvent event) {

    }

    @FXML
    void selectImage(ActionEvent event) {

    }

    private void closeWindow() {
        Stage stage = (Stage) addImageButton.getScene().getWindow();
        stage.close();
    }
}
