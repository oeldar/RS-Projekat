package grupa5;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.itextpdf.io.exceptions.IOException;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajPrijedlog;
import grupa5.baza_podataka.KartaPrijedlog;
import grupa5.baza_podataka.services.DogadjajPrijedlogService;
import grupa5.baza_podataka.services.DogadjajService;
import grupa5.baza_podataka.services.KartaPrijedlogService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

// @SuppressWarnings({"exports", "unused"})
public class EventRequestCardController {

    @FXML
    private Label datumLabel;

    @FXML
    private ImageView eventImage;

    @FXML
    private Label lokacijaLabel;

    @FXML
    private Label mjestoLabel;

    @FXML
    private Label nazivLabel;

    @FXML
    private Button odbijButton;

    @FXML
    private Button odobriButton;

    @FXML
    private Label tipLabel;

    private EntityManagerFactory emf;
    private Dogadjaj dogadjaj;
    private MainScreenController mainScreenController;
    private EventsRequestsController eventsRequestsController;
    private DogadjajService dogadjajService;
    private DogadjajPrijedlogService dogadjajPrijedlogService;
    private KartaPrijedlogService kartaPrijedlogService;
    private String tip;
    private DogadjajPrijedlog dogadjajPrijedlog;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;

        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        dogadjajPrijedlogService = new DogadjajPrijedlogService(emf);
        kartaPrijedlogService = new KartaPrijedlogService(emf);

        updateUI();
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setDogadjajPrijedlog(DogadjajPrijedlog dogadjajPrijedlog) {
        this.dogadjajPrijedlog = dogadjajPrijedlog;
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setDogadjajService(DogadjajService dogadjajService) {
        this.dogadjajService = dogadjajService;
    }

    public void setEventsRequestsController(EventsRequestsController eventsRequestsController) {
        this.eventsRequestsController = eventsRequestsController; // Set this controller
    }


    private void updateUI() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");
        nazivLabel.setText(dogadjaj.getNaziv());
        datumLabel.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
        mjestoLabel.setText(dogadjaj.getMjesto().getNaziv());
        lokacijaLabel.setText(dogadjaj.getLokacija().getNaziv());
        tipLabel.setText(tip.toUpperCase());
        tipLabel.setStyle("-fx-text-fill: blue;");

        loadEventImageLazy(dogadjaj.getPutanjaDoSlike());
    }

    private void loadEventImageLazy(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));
        eventImage.setImage(defaultImage);

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
                Image eventImageObj = loadImageTask.getValue();
                if (eventImage != null) {
                    Platform.runLater(() -> eventImage.setImage(eventImageObj));
                }
            });

            new Thread(loadImageTask).start();
        }
    }

    @FXML
    private void odbijDogadjaj(ActionEvent event) {
        if (tip.equals("Uređen")) {
            Integer prijedlogID = dogadjajPrijedlog.getPrijedlogDogadjajaID();
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Razlog odbijanja");
            dialog.setHeaderText("Unesite razlog odbijanja promjena događaja:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String razlogOdbijanja = result.get();
                for (KartaPrijedlog kartaPrijedlog : dogadjajPrijedlog.getKartePrijedlozi()) {
                    kartaPrijedlogService.obrisiKartaPrijedlog(kartaPrijedlog.getPrijedlogKarteID());
                }
                dogadjajPrijedlogService.odbijPrijedlog(prijedlogID, razlogOdbijanja);
                if (eventsRequestsController != null) {
                    eventsRequestsController.refreshRequests();
                }
            }
            return;
        }

        Integer dogadjajID = dogadjaj.getDogadjajID();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Razlog odbijanja");
        dialog.setHeaderText("Unesite razlog odbijanja događaja:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String razlogOdbijanja = result.get();
            dogadjajService.odbijDogadjaj(dogadjajID, razlogOdbijanja);
            if (eventsRequestsController != null) {
                eventsRequestsController.refreshRequests();
            }
        }
        eventsRequestsController.refreshRequests();
    }

    @FXML
    private void odobriDogadjaj(ActionEvent event) {
        if (tip.equals("Uređen")) {
            dogadjajPrijedlogService.odobriPrijedlog(dogadjajPrijedlog.getPrijedlogDogadjajaID());
            eventsRequestsController.refreshRequests();
            return;
        }

        Integer dogadjajID = dogadjaj.getDogadjajID();
        dogadjajService.odobriDogadjaj(dogadjajID);

        if (eventsRequestsController != null) {
            eventsRequestsController.refreshRequests();
        }
    }

    @FXML
    void prikaziSvePodatke(MouseEvent event) {
        try {
            // Učitavanje FXML fajla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/all-event-details.fxml"));
            Parent root = loader.load();
    
            AllEventDetailsController allEventDetailsController = loader.getController();
            allEventDetailsController.setDogadjaj(dogadjaj);
    
            Stage stage = new Stage();
            stage.setTitle("Detalji događaja");
            stage.setResizable(false); 
            stage.setScene(new Scene(root));

            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
