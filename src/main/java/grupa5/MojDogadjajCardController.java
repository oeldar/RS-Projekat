package grupa5;

import java.io.IOException;
import java.io.InputStream;

import grupa5.baza_podataka.Dogadjaj;
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
    private Button deaktivirajBtn;

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
    
    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
        updateUI();
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    private void updateUI() {
        nazivLbl.setText(dogadjaj.getNaziv());
        datumLbl.setText(dogadjaj.getPocetakDogadjaja().toString());
        mjestoLbl.setText(dogadjaj.getMjesto().getNaziv());
        statusLbl.setText(dogadjaj.getStatus().toString());

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
    private void deaktivirajDogadjaj(ActionEvent event) {
        // Logic to deactivate the event
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