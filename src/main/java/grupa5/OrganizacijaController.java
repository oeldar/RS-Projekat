package grupa5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

public class OrganizacijaController {
    @FXML
    private ComboBox<String> vrstaCombo, podvrstaCombo, mjestoCombo, lokacijaCombo;
    @FXML
    private Label podvrstaLabel;
    @FXML
    private ImageView eventImage;
    @FXML
    private AnchorPane imageContainer, roundedCorners, removeImgPane;
    @FXML
    private Button dodajSlikuBtn;
    @FXML
    private VBox sektoriVBox;

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
        mjestoCombo.getItems().addAll("Brcko", "Tuzla", "Sarajevo");

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

        mjestoCombo.setOnAction(event -> {
            String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
            lokacijaCombo.getItems().clear();

            if ("Brcko".equals(selectedMjesto)) {
                lokacijaCombo.getItems().addAll("Muzicka arena", "Ficibajer");
            }
            if ("Tuzla".equals(selectedMjesto)) {
                lokacijaCombo.getItems().addAll("Muzicka", "nesto za Tuzlu");
            }
            if ("Sarajevo".equals(selectedMjesto)) {
                lokacijaCombo.getItems().addAll("nesto drugo", "nesto trece");
            }
        });



        lokacijaCombo.setOnAction(event -> {
            String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();
            sektoriVBox.getChildren().clear();  // Očisti prethodno dodane sektore

            if ("Muzicka arena".equals(selectedLokacija) && "Brcko".equals(mjestoCombo.getValue())) {
                addSektor("Parter");
                addSektor("VIP");
                addSektor("VIP Exclusive");
            } else if ("Ficibajer".equals(selectedLokacija) && "Brcko".equals(mjestoCombo.getValue())) {
                addSektor("Parter");
                addSektor("Parter3");
                addSektor("Parte3r");
                addSektor("Parte3222r");
            } else if ("Muzicka".equals(selectedLokacija) && "Tuzla".equals(mjestoCombo.getValue())) {
                addSektor("Tribina");
                addSektor("Parter");
                addSektor("Parter");
                addSektor("Parter");
                addSektor("Parter");
            }
        });

       
    }

    private void addSektor(String sektorName) {
        Label sektorLabel = new Label(sektorName);
        sektorLabel.setMinWidth(100);
        TextField cijenaInput = new TextField();
        cijenaInput.setPromptText("Unesite cijenu");

        HBox sektorHBox = new HBox(sektorLabel, cijenaInput);
        sektoriVBox.getChildren().add(sektorHBox);
    }
}
