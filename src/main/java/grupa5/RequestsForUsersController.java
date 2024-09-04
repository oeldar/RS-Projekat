package grupa5;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.services.KorisnikService;
import grupa5.baza_podataka.services.NovcanikService;
import grupa5.baza_podataka.services.StatistikaKupovineService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Scale;

public class RequestsForUsersController implements Initializable {
    private static final String USERS_REQUEST_CARD = "views/user-request-card.fxml";

    @FXML
    private FlowPane requestsFlowPane;

    private List<Korisnik> neodobreniKorisnici;
    private KorisnikService korisnikService;
    private NovcanikService novcanikService;
    private StatistikaKupovineService statistikaKupovineService;
    private MainScreenController mainScreenController;

    public void setKorisnikService(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    public void setNovcanikService(NovcanikService novcanikService) {
        this.novcanikService = novcanikService;
    }

    public void setStatistikaKupovineService(StatistikaKupovineService statistikaKupovineService) {
        this.statistikaKupovineService = statistikaKupovineService;
    }

    public void setNeodobreniKorisnici(List<Korisnik> neodobreniKorisnici) {
        this.neodobreniKorisnici = neodobreniKorisnici;
        showRequests();
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
    }

    private void showRequests() {
        requestsFlowPane.getChildren().clear();

        if (neodobreniKorisnici == null || neodobreniKorisnici.isEmpty()) {
            System.err.println("Nema neodobrenih korisnika za prikaz.");
            return;
        }

        Task<Void> showRequestsTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<Parent> nodesToAdd = new ArrayList<>();
                    for (Korisnik korisnik : neodobreniKorisnici) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(USERS_REQUEST_CARD));
                        Parent userRequestCard = loader.load();

                        UsersRequestCardController controller = loader.getController();
                        controller.setKorisnik(korisnik);
                        controller.setKorisnikService(korisnikService);
                        controller.setNovcanikService(novcanikService);
                        controller.setStatistikaKupovineService(statistikaKupovineService);
                        controller.setMainScreenController(mainScreenController);
                        controller.setRequestsForUsersController(RequestsForUsersController.this);

                        nodesToAdd.add(userRequestCard);
                    }

                    Platform.runLater(() -> requestsFlowPane.getChildren().addAll(nodesToAdd));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom učitavanja zahtjeva za korisnike.");
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
                    // Fetch updated list of unapproved users
                    List<Korisnik> updatedUsers = korisnikService.pronadjiNeodobreneKorisnike();
                    Platform.runLater(() -> {
                        setNeodobreniKorisnici(updatedUsers);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom osvežavanja zahtjeva za korisnike.");
                }
                return null;
            }
        };

        new Thread(refreshTask).start();
    }
}