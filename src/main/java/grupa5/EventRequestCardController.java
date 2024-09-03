package grupa5;

import java.io.InputStream;
import java.util.Optional;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

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
    private Button odbaciButton;

    @FXML
    private Button odobriButton;

    private Dogadjaj dogadjaj;
    private MainScreenController mainScreenController;
    private EventsRequestsController eventsRequestsController;
    private DogadjajService dogadjajService;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
        updateUI();
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
        nazivLabel.setText(dogadjaj.getNaziv());
        datumLabel.setText(dogadjaj.getPocetakDogadjaja().toString());
        mjestoLabel.setText(dogadjaj.getMjesto().getNaziv());
        lokacijaLabel.setText(dogadjaj.getLokacija().getNaziv());

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
    private void odbaciDogadjaj(ActionEvent event) {
        Integer dogadjajID = dogadjaj.getDogadjajID();  // Implementirajte ovu metodu da dobijete ID selektiranog događaja

        // Prikazivanje dijaloga za unos razloga odbijanja
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Razlog odbijanja");
        dialog.setHeaderText("Unesite razlog odbijanja događaja:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String razlogOdbijanja = result.get();
            dogadjajService.odbaciDogadjaj(dogadjajID, razlogOdbijanja);
            if (eventsRequestsController != null) {
                eventsRequestsController.refreshRequests(); // Refresh the list of requests
            }
        }
        eventsRequestsController.refreshRequests();
    }

    // TODO: napisati odbaciPrijedlogDogadjaja(ActionEvent event)

    @FXML
    private void odobriDogadjaj(ActionEvent event) {
        Integer dogadjajID = dogadjaj.getDogadjajID();  // Implementirajte ovu metodu da dobijete ID selektiranog događaja
        dogadjajService.odobriDogadjaj(dogadjajID);

        if (eventsRequestsController != null) {
            eventsRequestsController.refreshRequests(); // Refresh the list of requests
        }
        eventsRequestsController.refreshRequests();
    }

    // TODO: napisati odobriPrijedlogDogadjaja(ActionEvent event)
}
