package grupa5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.LokacijaPrijedlog;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.services.LokacijaPrijedlogService;
import grupa5.baza_podataka.services.LokacijaService;
import grupa5.baza_podataka.services.MjestoService;
import grupa5.baza_podataka.services.SektorService;
import grupa5.support_classes.EmailService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
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

public class LokacijaController {

    @FXML
    private TextField adresaLokacijeTextField;

    @FXML
    private Button dodajSlikuBtn;

    @FXML
    private ImageView errorIcon;

    @FXML
    private Label errorLbl;

    @FXML
    private AnchorPane imageContainer;

    @FXML
    private ImageView lokacijaImg;

    @FXML
    private TextField nazivLokacijeTextField;

    @FXML
    private TextField nazivMjestaTextField;

    @FXML
    private TextField pbrMjestaTextField;

    @FXML
    private AnchorPane removeImgPane;

    @FXML
    private AnchorPane roundedCorners;

    @FXML
    private VBox sektoriVBox;

    @FXML
    private TextField vrijemeZaCiscenjeTextField;

    private List<String> sektoriList = new ArrayList<>();
    private LokacijaPrijedlogService lokacijaPrijedlogService;
    private MjestoService mjestoService;
    private LokacijaService lokacijaService;
    private SektorService sektorService;
    private String imagePath;
    private File selectedFile;

    private LokacijaPrijedlog lokacijaPrijedlog;
    private LocationRequestCardController locationRequestCardController;

    public void setLocationRequestCardController(LocationRequestCardController locationRequestCardController) {
        this.locationRequestCardController = locationRequestCardController;
    }

    public void setLokacijaPrijedlog(LokacijaPrijedlog lokacijaPrijedlog) {
        this.lokacijaPrijedlog = lokacijaPrijedlog;
        nazivLokacijeTextField.setText(lokacijaPrijedlog.getNazivLokacije());
        adresaLokacijeTextField.setText(lokacijaPrijedlog.getAdresa());
        nazivMjestaTextField.setText(lokacijaPrijedlog.getNazivMjesta());
        pbrMjestaTextField.setText(lokacijaPrijedlog.getPostanskiBroj().toString());
        if (lokacijaPrijedlog.getPutanjaDoSlike() != null) {
            Image image = new Image(getClass().getResourceAsStream("/grupa5/" + lokacijaPrijedlog.getPutanjaDoSlike()));
            lokacijaImg.setImage(image);
            imageContainer.setVisible(false);
            roundedCorners.setVisible(true);
            removeImgPane.setVisible(true);
            dodajSlikuBtn.setVisible(false);
        }
        vrijemeZaCiscenjeTextField.requestFocus();
    }

    @FXML
    public void initialize() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        lokacijaPrijedlogService = new LokacijaPrijedlogService(entityManagerFactory);
        mjestoService = new MjestoService(entityManagerFactory);
        lokacijaService = new LokacijaService(entityManagerFactory);
        sektorService = new SektorService(entityManagerFactory);
    }

    @FXML
    void dodajLokaciju(ActionEvent event) {
        boolean isValid = true;
        
        // Reset styles for all fields at the beginning
        nazivMjestaTextField.setStyle("");
        nazivLokacijeTextField.setStyle("");
        adresaLokacijeTextField.setStyle("");
        pbrMjestaTextField.setStyle("");
        vrijemeZaCiscenjeTextField.setStyle("");
        
        // Validate required fields
        if (nazivMjestaTextField.getText().trim().isEmpty()) {
            markFieldAsInvalid(nazivMjestaTextField);
            isValid = false;
        }

        try {
            Integer.parseInt(pbrMjestaTextField.getText().trim());
        } catch (NumberFormatException e) {
            markFieldAsInvalid(pbrMjestaTextField);
            showErrorMessage("Poštanski broj mora biti broj.");
            isValid = false;
        }

        if (nazivLokacijeTextField.getText().trim().isEmpty()) {
            markFieldAsInvalid(nazivLokacijeTextField);
            isValid = false;
        }

        if (adresaLokacijeTextField.getText().trim().isEmpty()) {
            markFieldAsInvalid(adresaLokacijeTextField);
            isValid = false;
        }

        if (pbrMjestaTextField.getText().trim().isEmpty()) {
            markFieldAsInvalid(pbrMjestaTextField);
            isValid = false;
        }

        if (vrijemeZaCiscenjeTextField.getText().trim().isEmpty()) {
            markFieldAsInvalid(vrijemeZaCiscenjeTextField);
            isValid = false;
        }

        if (!isValid) {
            showErrorMessage("Sva polja moraju biti ispravno popunjena.");
            return;
        }

        try {
            Integer.parseInt(pbrMjestaTextField.getText().trim());
        } catch (NumberFormatException e) {
            markFieldAsInvalid(pbrMjestaTextField);
            showErrorMessage("Poštanski broj mora biti broj.");
            return;
        }

        if (sektoriVBox.getChildren().isEmpty()) {
            showErrorMessage("Morate dodati barem jedan sektor.");
            return;
        }

        // Validate sectors
        Set<String> sectorsSet = new HashSet<>();
        for (Node node : sektoriVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                TextField sektorInput = (TextField) hbox.getChildren().get(0);
                TextField kapacitetInput = (TextField) hbox.getChildren().get(1);

                String nazivSektora = sektorInput.getText().trim();
                String kapacitet = kapacitetInput.getText().trim();

                if (nazivSektora.isEmpty() || kapacitet.isEmpty()) {
                    markFieldAsInvalid(sektorInput);
                    markFieldAsInvalid(kapacitetInput);
                    isValid = false;
                } else if (sectorsSet.contains(nazivSektora)) {
                    markFieldAsInvalid(sektorInput);
                    showErrorMessage("Nazivi sektora moraju biti jedinstveni.");
                    return;
                } else {
                    sectorsSet.add(nazivSektora);
                }

                try {
                    Integer.parseInt(kapacitet);
                } catch (NumberFormatException e) {
                    markFieldAsInvalid(kapacitetInput);
                    showErrorMessage("Kapacitet mora biti broj.");
                    isValid = false;
                }
            }
        }

        if (!isValid) {
            showErrorMessage("Sva polja moraju biti ispravno popunjena.");
            return;
        }

        try {
            Mjesto mjesto = mjestoService.pronadjiMjestoPoPostanskomBroju(Integer.parseInt(pbrMjestaTextField.getText().trim()));

            if (mjesto == null) {
                mjesto = mjestoService.kreirajMjesto(Integer.parseInt(pbrMjestaTextField.getText().trim()), nazivMjestaTextField.getText().trim());
            }

            Lokacija lokacija = lokacijaService.kreirajLokaciju(
                    nazivLokacijeTextField.getText().trim(),
                    mjesto,
                    adresaLokacijeTextField.getText().trim(),
                    sektoriVBox.getChildren().size(),
                    null,
                    Integer.parseInt(vrijemeZaCiscenjeTextField.getText().trim())
            );

            if (selectedFile != null) {
                // User-selected image
                copyAndSetImage(selectedFile, lokacija.getLokacijaID());
                lokacija.setPutanjaDoSlike(imagePath);
            } else if (lokacijaPrijedlog.getPutanjaDoSlike() != null) {
                // Use image from location proposal
                moveImageFromProposal(lokacijaPrijedlog.getPutanjaDoSlike(), lokacija.getLokacijaID());
                lokacija.setPutanjaDoSlike(imagePath);
            }

            lokacijaService.azurirajLokaciju(lokacija);

            // Save sectors
            for (Node node : sektoriVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    TextField sektorInput = (TextField) hbox.getChildren().get(0);
                    TextField kapacitetInput = (TextField) hbox.getChildren().get(1);

                    String nazivSektora = sektorInput.getText().trim();
                    Integer kapacitet = Integer.parseInt(kapacitetInput.getText().trim());

                    if (!nazivSektora.isEmpty() && kapacitet != null) {
                        sektorService.kreirajSektor(lokacija, nazivSektora, null, kapacitet);
                    }
                }
            }

            // Remove location proposal
            EmailService emailService = new EmailService();
            emailService.obavjestiOrganizatoraZaOdobravanjeLokacije(lokacija, lokacijaPrijedlog.getKorisnik().getEmail());
            locationRequestCardController.obrisiZahtjev(lokacijaPrijedlog.getPrijedlogLokacijeID());

            Stage stage = (Stage) nazivLokacijeTextField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showErrorMessage("Došlo je do greške pri dodavanju lokacije: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void copyAndSetImage(File selectedFile, Integer lokacijaId) throws IOException {
        Path destinationDir = Paths.get("src/main/resources/grupa5/assets/locations_photos/");
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
        }

        String fileName = selectedFile.getName();
        String fileExtension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            fileExtension = fileName.substring(i);
        }

        String newFileName = lokacijaId + fileExtension;
        Path destinationPath = destinationDir.resolve(newFileName);

        Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        imagePath = "assets/locations_photos/" + newFileName;
    }

    private void moveImageFromProposal(String sourcePath, Integer lokacijaId) throws IOException {
        Path sourceFilePath = Paths.get("src/main/resources/grupa5/" + sourcePath);
        if (Files.exists(sourceFilePath)) {
            Path destinationDir = Paths.get("src/main/resources/grupa5/assets/locations_photos/");
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            String fileExtension = "";
            int i = sourcePath.lastIndexOf('.');
            if (i >= 0) {
                fileExtension = sourcePath.substring(i);
            }

            String newFileName = lokacijaId + fileExtension;
            Path destinationPath = destinationDir.resolve(newFileName);

            Files.move(sourceFilePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            imagePath = "assets/locations_photos/" + newFileName;
        } else {
            throw new FileNotFoundException("Source image file does not exist: " + sourcePath);
        }
    }

    @FXML
    private void dodajSektor() {
        TextField sektorInput = new TextField();
        sektorInput.setPromptText("Naziv sektora");
        sektorInput.getStyleClass().add("input");

        TextField kapacitetInput = new TextField();
        kapacitetInput.setPromptText("Kapacitet");
        kapacitetInput.getStyleClass().add("input");
        kapacitetInput.setPrefWidth(135); 

        ImageView ukloniIkonica = new ImageView(new Image(getClass().getResourceAsStream("assets/icons/close_white.png")));
        ukloniIkonica.setFitWidth(10);
        ukloniIkonica.setFitHeight(10);

        Button ukloniButton = new Button();
        ukloniButton.setGraphic(ukloniIkonica);
        ukloniButton.getStyleClass().add("button-custom");

        HBox sektorHBox = new HBox(5);
        sektorHBox.getChildren().addAll(sektorInput, kapacitetInput, ukloniButton);

        sektoriVBox.getChildren().add(sektorHBox);
        VBox.setMargin(sektorHBox, new Insets(0, 0, 5, 0));

        sektorInput.requestFocus();

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

    private void showErrorMessage(String message) {
        errorLbl.setText(message);
        errorLbl.setVisible(true);
        errorIcon.setVisible(true);
    }

    private void markFieldAsInvalid(Control control) {
        control.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }    

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
                File file = dragboard.getFiles().get(0);
                imagePath = file.getAbsolutePath();
                lokacijaImg.setImage(new Image(file.toURI().toString()));
                imageContainer.setVisible(false);
                roundedCorners.setVisible(true);
                removeImgPane.setVisible(true);
                dodajSlikuBtn.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void ukloniSliku(ActionEvent event) {
        lokacijaImg.setImage(null);
        imagePath = null;
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

        Stage stage = (Stage) lokacijaImg.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage); // Save reference to the selected file

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            lokacijaImg.setImage(image);
            dodajSlikuBtn.setVisible(false);
            removeImgPane.setVisible(true);
            imageContainer.setVisible(false);
            roundedCorners.setVisible(true);
        }
    }

}