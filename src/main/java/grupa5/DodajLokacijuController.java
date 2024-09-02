package grupa5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DodajLokacijuController {

    @FXML
    private TextField imeLokacijeInput;

    @FXML
    private VBox sektoriVBox;

    private List<String> sektoriList = new ArrayList<>();

    @FXML
    private void dodajSektor() {
        if (!sektoriVBox.getChildren().isEmpty()) {
            HBox lastHBox = (HBox) sektoriVBox.getChildren().get(sektoriVBox.getChildren().size() - 1);
            TextField lastTextField = (TextField) lastHBox.getChildren().get(0);
            if (lastTextField.getText().trim().isEmpty()) {
                lastTextField.requestFocus();
                return;
            }
        }

        TextField sektorInput = new TextField();
        sektorInput.setPromptText("Ime sektora");

        Button ukloniButton = new Button("Ukloni");
        HBox sektorHBox = new HBox(sektorInput, ukloniButton);
        sektoriVBox.getChildren().add(sektorHBox);
        sektorInput.requestFocus(); // Prebacivanje fokusa na novi TextField

        ukloniButton.setOnAction(event -> {
            sektoriVBox.getChildren().remove(sektorHBox);
            sektoriList.remove(sektorInput.getText());
        });

        sektorInput.setOnAction(event -> {
            String imeSektora = sektorInput.getText().trim();
            if (!imeSektora.isEmpty() && !sektoriList.contains(imeSektora)) {
                sektoriList.add(imeSektora);
            }
        });
    }




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
}
