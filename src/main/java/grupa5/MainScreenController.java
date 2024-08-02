package grupa5;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainScreenController {
    @FXML
    private Label testLabel;
    private Stage stage;

    @FXML
    void loginBtnClicked(ActionEvent event) {
         try {
            if (stage == null || !stage.isShowing()) {
                Parent root = App.loadFXML("login");
                setStage(root, "Prijava", 950,  700);
            } else {
                stage.toFront();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signUpBtnClicked(ActionEvent event) {
         try {
            if (stage == null || !stage.isShowing()) {
                Parent root = App.loadFXML("registration-view");
                setStage(root, "Registacija", 1000, 750);
            } else {
                stage.toFront();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStage(Parent root, String title, double sizeX, double sizeY) {
        stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, sizeX, sizeY));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        stage.setOnCloseRequest(e -> stage = null);
    }

    @FXML private Button sviDogadjajiBtn;
    @FXML private Button muzikaBtn;
    @FXML private Button kulturaBtn;
    @FXML private Button sportBtn;
    @FXML private Button ostaloBtn;

    private List<Button> categoryButtons;

    @FXML private ImageView sviDogadjajiImg;
    @FXML private ImageView muzikaImg;
    @FXML private ImageView kulturaImg;
    @FXML private ImageView sportImg;
    @FXML private ImageView ostaloImg;

    private List<ImageView> categoryIcons;

    @FXML
    public void initialize() {
        categoryButtons = new ArrayList<>();
        categoryButtons.add(sviDogadjajiBtn);
        categoryButtons.add(muzikaBtn);
        categoryButtons.add(sportBtn);
        categoryButtons.add(kulturaBtn);
        categoryButtons.add(ostaloBtn);

        setActiveButton(sviDogadjajiBtn);

        categoryIcons = new ArrayList<>();
        categoryIcons.add(sviDogadjajiImg);
        categoryIcons.add(muzikaImg);
        categoryIcons.add(sportImg);
        categoryIcons.add(kulturaImg);
        categoryIcons.add(ostaloImg);

        // Dodaj početni stil za sva dugmad
       // buttons.forEach(button -> button.getStyleClass().add("cattegory-button"));

        // Postavi akcije za klik na dugmad
        sviDogadjajiBtn.setOnAction(event -> setActiveButton(sviDogadjajiBtn));
        muzikaBtn.setOnAction(event -> setActiveButton(muzikaBtn));
        sportBtn.setOnAction(event -> setActiveButton(sportBtn));
        kulturaBtn.setOnAction(event -> setActiveButton(kulturaBtn));
        ostaloBtn.setOnAction(event -> setActiveButton(ostaloBtn));
    }

    private ImageView imageFromButton(Button button) {
        ImageView result;
        String buttonName = button.getId();
        switch (buttonName) {
            case "sviDogadjajiBtn":
                result = sviDogadjajiImg;
                break;
            case "muzikaBtn":
                result = muzikaImg;
                break;
            case "kulturaBtn":
                result = kulturaImg;
                break;
            case "sportBtn":
                result = sportImg;
                break;
            case "ostaloBtn":
                result = ostaloImg;
                break;
            default:
                result = ostaloImg;
                break;
        }

        return result;
    }

    private void setActiveButton(Button activeButton) {
        // Resetuj sve dugmadi na osnovni stil
        ImageView currentImg;
        for (Button button : categoryButtons) {
            currentImg = imageFromButton(button);
            currentImg.setVisible(false);
            button.getStyleClass().removeAll("category-button-active");
            if (!button.getStyleClass().contains("category-button")) {
                button.getStyleClass().add("category-button");
            }
        }

        // Postavi aktivni stil za kliknuto dugme
        ImageView activeImg = imageFromButton(activeButton);
        activeImg.setVisible(true);
        activeButton.getStyleClass().remove("category-button");
        activeButton.getStyleClass().add("category-button-active");
    }

    



}
