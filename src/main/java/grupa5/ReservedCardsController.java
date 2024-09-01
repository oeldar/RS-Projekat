package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ReservedCardsController {

    @FXML
    private VBox reservedCardsVBox;

    private EntityManagerFactory emf;
    private MainScreenController mainScreenController;
    private List<Rezervacija> rezervacije;
    private RezervacijaService rezervacijaService;
    private KartaService kartaService;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setRezervacije(List<Rezervacija> rezervacije) {
        this.rezervacije = rezervacije;
        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        if (rezervacije == null || rezervacije.isEmpty()) {
            System.err.println("Rezervacije su null ili prazne u updateUI.");
            return;
        }

        // Lazy load and UI update in a background thread
        Task<Void> updateTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<AnchorPane> nodesToAdd = new ArrayList<>();
                    for (Rezervacija rezervacija : rezervacije) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                        AnchorPane reservedCardNode = loader.load();

                        ReservedCardController controller = loader.getController();
                        controller.setReservationData(rezervacija);
                        controller.setMainScreenController(mainScreenController);
                        controller.setReservedCardsController(ReservedCardsController.this);

                        nodesToAdd.add(reservedCardNode);
                    }
                    // Update UI in the JavaFX Application Thread
                    Platform.runLater(() -> reservedCardsVBox.getChildren().addAll(nodesToAdd));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom učitavanja rezervacija.");
                }
                return null;
            }
        };

        new Thread(updateTask).start();
    }

    public void refreshReservations() {
        Platform.runLater(() -> {
            reservedCardsVBox.getChildren().clear();

            Task<Void> refreshTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        List<Rezervacija> noveRezervacije = rezervacijaService.pronadjiRezervacijePoKorisniku(mainScreenController.korisnik);
                        List<AnchorPane> nodesToAdd = new ArrayList<>();

                        for (Rezervacija rezervacija : noveRezervacije) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                            AnchorPane reservedCardNode = loader.load();

                            ReservedCardController controller = loader.getController();
                            controller.setReservationData(rezervacija);
                            controller.setMainScreenController(mainScreenController);
                            controller.setReservedCardsController(ReservedCardsController.this);

                            nodesToAdd.add(reservedCardNode);
                        }

                        Platform.runLater(() -> reservedCardsVBox.getChildren().addAll(nodesToAdd));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Greška prilikom učitavanja rezervacija.");
                    }
                    return null;
                }
            };

            new Thread(refreshTask).start();
        });
    }

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        rezervacijaService = new RezervacijaService(emf);
        kartaService = new KartaService(emf);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
        AnchorPane reservedCardNode;
        try {
            reservedCardNode = loader.load();
            reservedCardsVBox.getChildren().add(reservedCardNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    void handleOtkaziSve(ActionEvent event) {
        List<Rezervacija> sveRezervacije = rezervacijaService.pronadjiRezervacijePoKorisniku(mainScreenController.korisnik);

        if (sveRezervacije.isEmpty()) {
            Obavjest.showAlert("Obaveštenje", "Nemate aktivne rezervacije za otkazivanje.");
            return;
        }

        // Otkazi sve rezervacije
        for (Rezervacija rezervacija : sveRezervacije) {
            rezervacijaService.obrisiRezervaciju(rezervacija.getRezervacijaID());
        }

        // Osveži prikaz rezervacija
        refreshReservations();

        Obavjest.showAlert("Otkazivanje uspešno", "Sve rezervacije su uspešno otkazane.");
    }

    @FXML
    public void close() {
        // Close EntityManagerFactory if open
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}