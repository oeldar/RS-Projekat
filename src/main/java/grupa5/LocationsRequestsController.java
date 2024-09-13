package grupa5;

import grupa5.baza_podataka.LokacijaPrijedlog;
import grupa5.baza_podataka.services.LokacijaPrijedlogService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import java.util.*;

public class LocationsRequestsController {

    private static final String LOCATION_REQUEST_CARD = "views/location-request-card.fxml";

    @FXML
    private FlowPane requestsFlowPane;

    private MainScreenController mainScreenController;

    private List<LokacijaPrijedlog> prijedloziLokacija;
    private EntityManagerFactory emf;
    private LokacijaPrijedlogService lokacijaPrijedlogService;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        lokacijaPrijedlogService = new LokacijaPrijedlogService(emf);
    }

    public void setPrijedloziLokacija() {
        this.prijedloziLokacija = lokacijaPrijedlogService.pronadjiSvePrijedlogeLokacija();
        showRequests();
    }

    private void showRequests() {
        requestsFlowPane.getChildren().clear();

        if (prijedloziLokacija == null || prijedloziLokacija.isEmpty()){
            return;
        }

        Task<Void> showRequestsTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<AnchorPane> nodesToAdd = new ArrayList<>();

                    for (LokacijaPrijedlog lokacijaPrijedlog : prijedloziLokacija) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_REQUEST_CARD));
                        AnchorPane locationRequestCard = loader.load();

                        LocationRequestCardController controller = loader.getController();
                        controller.setLokacijaPrijedlogService(lokacijaPrijedlogService);
                        controller.setLokacijaPrijedlog(lokacijaPrijedlog);
                        controller.setMainScreenController(mainScreenController);
                        controller.setLocationsRequestsController(LocationsRequestsController.this);

                        nodesToAdd.add(locationRequestCard);
                    }

                    Platform.runLater(() -> requestsFlowPane.getChildren().addAll(nodesToAdd));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom učitavanja zahtjeva za lokacije.");
                }
                return null;
            }
        };

        new Thread(showRequestsTask).start();
    }

    public void refreshRequests() {
        Task<Void> refreshTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Platform.runLater(() -> {
                        setPrijedloziLokacija();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom osvežavanja zahtjeva za lokacije.");
                }
                return null;
            }
        };

        new Thread(refreshTask).start();
    }
}
