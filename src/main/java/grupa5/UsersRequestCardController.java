package grupa5;

import grupa5.baza_podataka.Dogadjaj;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class UsersRequestCardController {

    @FXML
    private Text mailLabel;

    @FXML
    private Text nameLabel;

    @FXML
    private Button odbaciButton;

    @FXML
    private Button odobriButton;

    @FXML
    private Text roleLabel;

    @FXML
    private ImageView userImage;

    @FXML
    private Text usernameLabel;

    @FXML
    void odbaciKorisnika(ActionEvent event) {

    }

    @FXML
    void odobriKorisnika(ActionEvent event) {

    }

    private RequestsForUsersController parentController;

    public void setParentController(RequestsForUsersController parentController) {
        this.parentController = parentController;
    }

}