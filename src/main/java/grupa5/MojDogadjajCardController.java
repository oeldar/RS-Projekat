package grupa5;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Dogadjaj.Status;
import grupa5.baza_podataka.services.DogadjajService;
import grupa5.baza_podataka.services.KartaService;
import grupa5.baza_podataka.services.MjestoService;
import jakarta.persistence.EntityManagerFactory;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// @SuppressWarnings({"exports", "unused"})
public class MojDogadjajCardController {

    @FXML
    private Label datumLbl;

    @FXML
    private Button otkaziBtn;

    @FXML
    private ImageView dogadjajImg;

    @FXML
    private Label mjestoLbl;

    @FXML
    private Label nazivLbl;

    @FXML
    private Label statusLbl;

    @FXML
    private Button urediBtn;

    private Dogadjaj dogadjaj;
    private MainScreenController mainScreenController;
    private DogadjajService dogadjajService;
    private KartaService kartaService;
    private MojiDogadjajiController mojiDogadjajiController;
    
    private EntityManagerFactory entityManagerFactory;

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

    public void setKartaService(KartaService kartaService) {
        this.kartaService = kartaService;
    }

    public void setMojiDogadjajiController(MojiDogadjajiController mojiDogadjajiController) {
        this.mojiDogadjajiController = mojiDogadjajiController;
    }

      

    private void updateUI() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");
        nazivLbl.setText(dogadjaj.getNaziv());
        datumLbl.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
        mjestoLbl.setText(dogadjaj.getMjesto().getNaziv());
        statusLbl.setText(dogadjaj.getStatus().toString());
        if (dogadjaj.getStatus().equals(Status.OTKAZAN) || dogadjaj.getStatus().equals(Status.ZAVRSEN)) {
            otkaziBtn.setVisible(false);
            urediBtn.setVisible(false);
        }
        if (dogadjaj.getPrijedlogDogadjaja() != null) {
            statusLbl.setText(dogadjaj.getStatus().toString() + ", UREĐEN");
            urediBtn.setVisible(false);
        }

        loadEventImageLazy(dogadjaj.getPutanjaDoSlike());
    }

    private void loadEventImageLazy(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));
        dogadjajImg.setImage(defaultImage);

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
                Image eventImage = loadImageTask.getValue();
                if (eventImage != null) {
                    Platform.runLater(() -> dogadjajImg.setImage(eventImage));
                }
            });

            new Thread(loadImageTask).start();
        }
    }

    @FXML
    private void otkaziDogadjaj(ActionEvent event) {
        if (dogadjaj.getStatus().equals(Status.ODBIJEN)) {
            List<Karta> karte = dogadjaj.getKarte();
            for (Karta karta : karte){
                kartaService.obrisiKartu(karta.getKartaID());
            }
            dogadjajService.obrisiDogadjaj(dogadjaj.getDogadjajID());
        } else {
            dogadjajService.otkaziDogadjaj(dogadjaj.getDogadjajID());
        }
        
        mojiDogadjajiController.refreshDogadjaji();
    }

    @FXML
    private void urediDogadjaj(ActionEvent event) {
        System.out.println(dogadjaj.getNaziv());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/edit-event.fxml"));
            Parent root = loader.load();

            EditEventController editEventController = loader.getController();
            editEventController.setDogadjaj(dogadjaj);
            editEventController.setDogadjajService(dogadjajService);



            Stage stage = new Stage();
            stage.setTitle("Uredi Događaj");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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