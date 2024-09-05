package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.services.RezervacijaService;
import grupa5.support_classes.Obavjest;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

// @SuppressWarnings("exports")
public class ReservedCardsController {

    @FXML
    private VBox reservedCardsVBox;

    @FXML
    private AnchorPane nemaRezervisanihPane;

    @FXML
    private ImageView loadingGif;  // Dodaj ovo za GIF

    private EntityManagerFactory emf;
    private MainScreenController mainScreenController;
    private List<Rezervacija> rezervacije;
    private RezervacijaService rezervacijaService;


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
            nemaRezervisanihPane.setVisible(true);
            return;
        } else {
            nemaRezervisanihPane.setVisible(false);
        }

        
        loadingGif.setVisible(true);  // Prikaži GIF

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
                    Platform.runLater(() -> {
                        reservedCardsVBox.getChildren().addAll(nodesToAdd);
                        loadingGif.setVisible(false);  // Sakrij GIF
                    });
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
            loadingGif.setVisible(true);  // Prikaži GIF

            Task<Void> refreshTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        List<Rezervacija> noveRezervacije = rezervacijaService.pronadjiRezervacijePoKorisniku(mainScreenController.korisnik);
                        List<AnchorPane> nodesToAdd = new ArrayList<>();

                        if (noveRezervacije.isEmpty()) {
                            nemaRezervisanihPane.setVisible(true);
                        } else {
                            nemaRezervisanihPane.setVisible(false);
                        }

                        for (Rezervacija rezervacija : noveRezervacije) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                            AnchorPane reservedCardNode = loader.load();

                            ReservedCardController controller = loader.getController();
                            controller.setReservationData(rezervacija);
                            controller.setMainScreenController(mainScreenController);
                            controller.setReservedCardsController(ReservedCardsController.this);

                            nodesToAdd.add(reservedCardNode);
                        }

                        Platform.runLater(() -> {
                            reservedCardsVBox.getChildren().addAll(nodesToAdd);
                            loadingGif.setVisible(false);  // Sakrij GIF
                        });
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
        Image image = new Image(getClass().getResource("/grupa5/assets/icons/user.png").toString());
        ImageView imageView = new ImageView(image);
        imageView.setLayoutX(200);
        imageView.setLayoutY(200);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setVisible(true);

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
            rezervacijaService.otkaziRezervaciju(rezervacija);
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
