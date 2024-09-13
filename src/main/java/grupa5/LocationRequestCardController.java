package grupa5;

import java.io.InputStream;
import java.util.Optional;

import grupa5.baza_podataka.LokacijaPrijedlog;
import grupa5.baza_podataka.services.LokacijaPrijedlogService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LocationRequestCardController {

    @FXML
    private Label adresaLabel;

    @FXML
    private ImageView locationImage;

    @FXML
    private Label mjestoLabel;

    @FXML
    private Label nazivLabel;

    @FXML
    private Button odbijiButton;

    @FXML
    private Button odobriButton;

    @FXML
    private Label pbrLabel;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/locations_photos/default-location.png";

    private LokacijaPrijedlog lokacijaPrijedlog;
    private LokacijaPrijedlogService lokacijaPrijedlogService;
    private MainScreenController mainScreenController;
    private LocationsRequestsController locationsRequestsController;

    public void setLokacijaPrijedlog(LokacijaPrijedlog lokacijaPrijedlog) {
        this.lokacijaPrijedlog = lokacijaPrijedlog;
        updateUI();
    }

    public void setLokacijaPrijedlogService(LokacijaPrijedlogService lokacijaPrijedlogService) {
        this.lokacijaPrijedlogService = lokacijaPrijedlogService;
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setLocationsRequestsController(LocationsRequestsController locationsRequestsController) {
        this.locationsRequestsController = locationsRequestsController;
    }

    public void obrisiZahtjev(Integer prijedlogLokacijeID) {
        lokacijaPrijedlogService.obrisiPrijedlogLokacije(prijedlogLokacijeID);
        locationsRequestsController.refreshRequests();
    }

    private void updateUI() {
        nazivLabel.setText(lokacijaPrijedlog.getNazivLokacije());
        mjestoLabel.setText(lokacijaPrijedlog.getNazivMjesta());
        adresaLabel.setText(lokacijaPrijedlog.getAdresa());
        pbrLabel.setText(lokacijaPrijedlog.getPostanskiBroj().toString());

        loadLocationImageLazy(lokacijaPrijedlog.getPutanjaDoSlike());
    }

    private void loadLocationImageLazy(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));
        locationImage.setImage(defaultImage);

        if (imagePath != null && !imagePath.isEmpty()) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                        if (imageStream != null) {
                            return new Image(imageStream);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading image from path: " + imagePath);
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                Image locationImageObj = loadImageTask.getValue();
                if (locationImageObj != null) {
                    Platform.runLater(() -> locationImage.setImage(locationImageObj));
                }
            });

            new Thread(loadImageTask).start();
        }
    }

    @FXML
    private void odbijLokaciju(ActionEvent event) {
        Integer prijedlogID = lokacijaPrijedlog.getPrijedlogLokacijeID();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Razlog odbijanja");
        dialog.setHeaderText("Unesite razlog odbijanja lokacije:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String razlogOdbijanja = result.get();
            lokacijaPrijedlogService.odbijPrijedlogLokacije(prijedlogID, razlogOdbijanja);
            if (locationsRequestsController != null) {
                locationsRequestsController.refreshRequests();
            }
        }
    }

    @FXML
    private void odobriLokaciju(ActionEvent event) {
        otvoriProzor();
        if (locationsRequestsController != null) {
            locationsRequestsController.refreshRequests();
        }
    }

    private void otvoriProzor(){
        try {
            // Učitavanje FXML fajla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/dodajMjestoLokacijuSektore.fxml"));
            Parent root = loader.load();
    
            LokacijaController lokacijaController = loader.getController();
            lokacijaController.setLocationRequestCardController(this);
            lokacijaController.setLokacijaPrijedlog(lokacijaPrijedlog);
    
            Stage stage = new Stage();
            stage.setTitle("Lokacija");
            stage.setResizable(false); 
            stage.setScene(new Scene(root));

            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
