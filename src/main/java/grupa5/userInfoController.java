package grupa5;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class userInfoController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Hello from userinfo");
    }

    @FXML
    void applyChanglesButtonClicked(ActionEvent event) {

    }

    @FXML
    void removeImage(ActionEvent event) {

    }

    @FXML
    void selectImage(ActionEvent event) {

    }

}
