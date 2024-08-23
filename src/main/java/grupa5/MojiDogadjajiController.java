package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class MojiDogadjajiController {

    @FXML
    private FlowPane dogadjajiFlowPane;

    private MainScreenController mainScreenController;

    private List<Dogadjaj> dogadjajiList;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setDogadjaji(List<Dogadjaj> dogadjajiList) {
        this.dogadjajiList = dogadjajiList;
        populateDogadjaji();
    }

    private void populateDogadjaji() {
        dogadjajiFlowPane.getChildren().clear();

        if (dogadjajiList == null || dogadjajiList.isEmpty()) {
            System.err.println("Dogadjaji su null ili prazni u populateDogadjaji.");
            return;
        }

        Task<Void> populateTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<AnchorPane> nodesToAdd = new ArrayList<>();
                    for (Dogadjaj dogadjaj : dogadjajiList) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mojDogadjajCard.fxml"));
                        AnchorPane eventCard = loader.load();

                        MojDogadjajCardController controller = loader.getController();
                        controller.setDogadjaj(dogadjaj);
                        controller.setMainScreenController(mainScreenController);

                        nodesToAdd.add(eventCard);
                    }

                    Platform.runLater(() -> dogadjajiFlowPane.getChildren().addAll(nodesToAdd));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom učitavanja dogadjaja.");
                }
                return null;
            }
        };

        new Thread(populateTask).start();
    }
}
