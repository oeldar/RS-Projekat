package grupa5;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajService;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Dogadjaj.Status;
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
import javafx.stage.Stage;

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

    @FXML
    private Label razlogOdbijanjaLbl;

    private Dogadjaj dogadjaj;
    private MainScreenController mainScreenController;
    private DogadjajService dogadjajService;
    private KartaService kartaService;
    private MojiDogadjajiController mojiDogadjajiController;

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
        nazivLbl.setText(dogadjaj.getNaziv());
        datumLbl.setText(dogadjaj.getPocetakDogadjaja().toString());
        mjestoLbl.setText(dogadjaj.getMjesto().getNaziv());
        statusLbl.setText(dogadjaj.getStatus().toString());
        if (dogadjaj.getStatus().equals(Status.OTKAZAN) || dogadjaj.getStatus().equals(Status.ZAVRSEN)) {
            otkaziBtn.setVisible(false);
            urediBtn.setVisible(false);
        }
        if (dogadjaj.getStatus().equals(Status.ODBIJEN)) {
            razlogOdbijanjaLbl.setText(dogadjaj.getRazlogOdbijanja());
            otkaziBtn.setText("Odustani");
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
        if (otkaziBtn.getText().equals("Odustani")) {
            List<Karta> karte = dogadjaj.getKarte();
            for (Karta karta : karte){
                kartaService.obrisiKartu(karta.getKartaID());
            }
            dogadjajService.obrisiDogadjaj(dogadjaj.getDogadjajID());
        } else {
            dogadjajService.otkaziDogadjaj(dogadjaj.getDogadjajID());
        }
        
        if (mojiDogadjajiController != null) {
            mojiDogadjajiController.refreshDogadjaji();
        }
    }

    @FXML
    private void urediDogadjaj(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/urediDogadjaj.fxml"));
            Parent root = loader.load();

            // UrediDogadjajController urediDogadjajController = loader.getController();
            // urediDogadjajController.setDogadjaj(dogadjaj);

            Stage stage = new Stage();
            stage.setTitle("Uredi Događaj");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}