package grupa5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.LokacijaPrijedlog;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.services.LokacijaPrijedlogService;
import grupa5.baza_podataka.services.LokacijaService;
import grupa5.baza_podataka.services.MjestoService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class DodajLokacijuController {

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

    private LokacijaPrijedlogService lokacijaPrijedlogService;
    private MjestoService mjestoService;
    private LokacijaService lokacijaService;
    private String imagePath;
    private File selectedFile;

    private Korisnik korisnik;

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @FXML
    public void initialize() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        lokacijaPrijedlogService = new LokacijaPrijedlogService(entityManagerFactory);
        mjestoService = new MjestoService(entityManagerFactory);
        lokacijaService = new LokacijaService(entityManagerFactory);
        nazivMjestaTextField.requestFocus();
    }

    @FXML
    void dodajLokaciju(ActionEvent event) {
        boolean isValid = true;
    
        // Resetovanje stilova polja na početku
        nazivMjestaTextField.setStyle("");
        nazivLokacijeTextField.setStyle("");
        adresaLokacijeTextField.setStyle("");
        pbrMjestaTextField.setStyle("");
    
        // Proveravanje da li su sva polja popunjena
        if (nazivMjestaTextField.getText().trim().isEmpty()) {
            markFieldAsInvalid(nazivMjestaTextField);
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

        if (!isValid) {
            showErrorMessage("Sva polja su obavezna.");
            return;
        }

        try {
            Integer.parseInt(pbrMjestaTextField.getText().trim());
        } catch (NumberFormatException e) {
            markFieldAsInvalid(pbrMjestaTextField);
            showErrorMessage("Poštanski broj mora biti broj.");
            return;
        }


        // Ako su sva polja popunjena, sada pokušavamo parsirati poštanski broj
        try {
            Integer postanskiBroj = Integer.parseInt(pbrMjestaTextField.getText().trim());
            String nazivMjesta = nazivMjestaTextField.getText().trim();

             Mjesto existingMjesto = mjestoService.pronadjiMjestoPoPostanskomBroju(postanskiBroj);

            if (existingMjesto != null) {
                if (!existingMjesto.getNaziv().equalsIgnoreCase(nazivMjesta)) {
                    showErrorMessage("Poštanski broj već postoji sa drugim nazivom mjesta: " + existingMjesto.getNaziv() + ".");
                    markFieldAsInvalid(nazivMjestaTextField);
                    return;
                } else {
                    // Ako se naziv mesta razlikuje samo po velikim/malim slovima, koristimo naziv iz baze
                    nazivMjesta = existingMjesto.getNaziv();
                }
            }

            String nazivLokacije = nazivLokacijeTextField.getText().trim();
            String adresa = adresaLokacijeTextField.getText().trim();

            Lokacija existingLokacija = lokacijaService.pronadjiLokaciju(existingMjesto, nazivLokacije, adresa);
            if (existingLokacija != null) {
                showErrorMessage("Lokacija već postoji.");
                markFieldAsInvalid(nazivLokacijeTextField);
                markFieldAsInvalid(adresaLokacijeTextField);
                markFieldAsInvalid(nazivMjestaTextField);
                markFieldAsInvalid(pbrMjestaTextField);
                return;
            }
    
            String putanjaDoSlike = imagePath;  // Putanja se generiše prilikom dodavanja slike
    
            // Kreirajte prijedlog lokacije
            LokacijaPrijedlog prijedlog = lokacijaPrijedlogService.kreirajPrijedlogLokacije(
                korisnik, postanskiBroj, nazivMjesta, nazivLokacije, adresa, putanjaDoSlike
            );
    
            if (prijedlog != null) {
                if (selectedFile != null) {
                    copyAndSetImage(selectedFile, prijedlog.getPrijedlogLokacijeID());
                    prijedlog.setPutanjaDoSlike(imagePath); // Update the image path in the proposal
                    lokacijaPrijedlogService.azurirajPrijedlogLokacije(prijedlog);
                }
    
                Stage stage = (Stage) nazivLokacijeTextField.getScene().getWindow();
                stage.close();
            }
    
        } catch (Exception e) {
            showErrorMessage("Došlo je do greške prilikom dodavanja lokacije.");
            e.printStackTrace();
        }
    }    

    private void copyAndSetImage(File selectedFile, Integer prijedlogId) throws IOException {
        // Define the directory where images will be stored
        Path destinationDir = Paths.get("src/main/resources/grupa5/assets/locations_photos/suggestions/");
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
        }
    
        // Get the file extension of the original image
        String fileName = selectedFile.getName();
        String fileExtension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            fileExtension = fileName.substring(i); // includes the dot, e.g., ".jpg"
        }
    
        // Create a new file name using the proposal ID
        String newFileName = prijedlogId + fileExtension;
        Path destinationPath = destinationDir.resolve(newFileName);
    
        // Copy the image to the directory under the new name
        Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    
        // Set the relative path for storing in the database
        imagePath = "assets/locations_photos/suggestions/" + newFileName;
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