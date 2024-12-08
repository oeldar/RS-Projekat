package grupa5;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajPrijedlog;
import grupa5.baza_podataka.services.DogadjajPrijedlogService;
import grupa5.baza_podataka.services.DogadjajService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

// @SuppressWarnings("exports")
public class EventsRequestsController implements Initializable {
    private static final String EVENT_REQUEST_CARD = "views/eventRequestCard.fxml";



    @FXML
    private FlowPane requestsFlowPane;

    private MainScreenController mainScreenController;

    private List<Dogadjaj> neodobreniDogadjaji;
    private List<DogadjajPrijedlog> prijedloziDogadjaja;
    private EntityManagerFactory emf;
    private DogadjajPrijedlogService dogadjajPrijedlogService;
    private DogadjajService dogadjajService;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setDogadjajService(DogadjajService dogadjajService) {
        this.dogadjajService = dogadjajService;
        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        dogadjajPrijedlogService = new DogadjajPrijedlogService(emf);
    }


    public void setNeodobreniDogadjaji(List<Dogadjaj> neodobreniDogadjaji) {
        this.neodobreniDogadjaji = neodobreniDogadjaji;
        showRequests();
    }

    public void setPrijedloziDogadjaja(List<DogadjajPrijedlog> prijedloziDogadjaja) {
        this.prijedloziDogadjaja = prijedloziDogadjaja;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
    }

    private void showRequests() {
        requestsFlowPane.getChildren().clear();

        if ((neodobreniDogadjaji == null || neodobreniDogadjaji.isEmpty()) && (prijedloziDogadjaja == null || prijedloziDogadjaja.isEmpty())) {
            System.err.println("Nema neodobrenih događaja za prikaz.");
            return;
        }

        Task<Void> showRequestsTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<AnchorPane> nodesToAdd = new ArrayList<>();
                    System.out.println("Velicina liste prijedloziDogadjaja: " + prijedloziDogadjaja.size());
                    for (DogadjajPrijedlog dogadjajPrijedlog : prijedloziDogadjaja) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(EVENT_REQUEST_CARD));
                        AnchorPane eventRequestCard = loader.load();

                        EventRequestCardController controller = loader.getController();
                        controller.setTip("Uređen");
                        controller.setDogadjajPrijedlog(dogadjajPrijedlog);
                        controller.setDogadjaj(dogadjajPrijedlog.getOriginalniDogadjaj());
                        controller.setMainScreenController(mainScreenController);
                        controller.setDogadjajService(dogadjajService);
                        controller.setEventsRequestsController(EventsRequestsController.this);

                        nodesToAdd.add(eventRequestCard);
                    }

                    for (Dogadjaj dogadjaj : neodobreniDogadjaji) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(EVENT_REQUEST_CARD));
                        AnchorPane eventRequestCard = loader.load();

                        EventRequestCardController controller = loader.getController();
                        controller.setTip("Kreiran");
                        controller.setDogadjaj(dogadjaj);
                        controller.setMainScreenController(mainScreenController);
                        controller.setDogadjajService(dogadjajService);
                        controller.setEventsRequestsController(EventsRequestsController.this);

                        nodesToAdd.add(eventRequestCard);
                    }

                    Platform.runLater(() -> requestsFlowPane.getChildren().addAll(nodesToAdd));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom učitavanja zahtjeva za događaje.");
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
                    // Fetch updated list of unapproved events
                    List<Dogadjaj> updatedEvents = dogadjajService.pronadjiNeodobreneDogadjaje();
                    List<DogadjajPrijedlog> updatedProposals = dogadjajPrijedlogService.pronadjiSvePrijedlogeDogadjaja();
                    Platform.runLater(() -> {
                        setNeodobreniDogadjaji(updatedEvents);
                        setPrijedloziDogadjaja(updatedProposals);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom osvežavanja zahtjeva za događaje.");
                }
                return null;
            }
        };

        new Thread(refreshTask).start();
    }
}

