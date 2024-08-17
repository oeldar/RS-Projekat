package grupa5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OrganizacijaController {
    @FXML
    private ComboBox<String> vrstaCombo, podvrstaCombo;
    @FXML
    private Label podvrstaLabel;
    @FXML
    private ImageView eventImage;
    @FXML
    private AnchorPane imageContainer, roundedCorners, removeImgPane;
    @FXML
    private Button dodajSlikuBtn;

    @FXML
    void imageDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasImage() || dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }

        event.consume();
    }

    @FXML
    void imageDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasImage() || dragboard.hasFiles()) {
            try {
                eventImage.setImage(new Image(new FileInputStream(dragboard.getFiles().get(0))));
                imageContainer.setVisible(false);
                roundedCorners.setVisible(true);
                removeImgPane.setVisible(true);
                dodajSlikuBtn.setVisible(false);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @FXML
    void ukloniSliku(ActionEvent event) {
        eventImage.setImage(null);
        removeImgPane.setVisible(false);
        imageContainer.setVisible(true);
        roundedCorners.setVisible(false);
        dodajSlikuBtn.setVisible(true);

    }

    @FXML
    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage stage = (Stage) eventImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            eventImage.setImage(image);
            dodajSlikuBtn.setVisible(false);
            removeImgPane.setVisible(true);
            imageContainer.setVisible(false);
            roundedCorners.setVisible(true);
        }
    }


    public void initialize() {

        dodajSlikuBtn.toFront();

        vrstaCombo.getItems().addAll("Muzika", "Kultura", "Sport", "Ostalo");

        vrstaCombo.setOnAction(event -> {
            String selectedVrsta = vrstaCombo.getSelectionModel().getSelectedItem();
            podvrstaCombo.getItems().clear();

            if ("Muzika".equals(selectedVrsta)) {
                podvrstaCombo.setVisible(true);
                podvrstaLabel.setVisible(true);
                podvrstaCombo.getItems().addAll("Koncert", "Opera", "Svirka", "Mjuzikl", "Ostalo");
            }
            if ("Sport".equals(selectedVrsta)) {
                podvrstaCombo.setVisible(true);
                podvrstaLabel.setVisible(true);
                podvrstaCombo.getItems().addAll("Košarka", "Fudbal", "Odbojka", "Boks", "Ostalo");
            }
            if ("Kultura".equals(selectedVrsta)) {
                podvrstaCombo.setVisible(true);
                podvrstaLabel.setVisible(true);
                podvrstaCombo.getItems().addAll("Predstava", "Film", "Promocija", "Ostalo");
            }
            if ("Ostalo".equals(selectedVrsta)) {
                podvrstaCombo.setVisible(false);
                podvrstaLabel.setVisible(false);
            }
        });
    }
}
