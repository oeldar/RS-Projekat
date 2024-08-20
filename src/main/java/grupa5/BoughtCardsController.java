package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.KupovinaService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.Rezervacija.RezervacijaStatus;
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

public class BoughtCardsController {

    @FXML
    private VBox boughtCardsVBox;

    private MainScreenController mainScreenController;
    private List<Kupovina> kupovine;
    private KupovinaService kupovinaService;
    private KartaService kartaService;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setKupovine(List<Kupovina> kupovine) {
        this.kupovine = kupovine;
        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        if (kupovine == null || kupovine.isEmpty()) {
            System.err.println("Kupovine su null ili prazne u updateUI.");
            return;
        }

        boughtCardsVBox.getChildren().clear();

        // Lazy load and UI update in a background thread
        Task<Void> updateTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<AnchorPane> nodesToAdd = new ArrayList<>();
                    for (Kupovina kupovina : kupovine) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/bought-card.fxml"));
                        AnchorPane boughtCardNode = loader.load();

                        BoughtCardController controller = loader.getController();
                        controller.setPurchaseData(kupovina);
                        controller.setMainScreenController(mainScreenController);
                        controller.setBoughtCardsController(BoughtCardsController.this);

                        nodesToAdd.add(boughtCardNode);
                    }
                    // Update UI in the JavaFX Application Thread
                    Platform.runLater(() -> boughtCardsVBox.getChildren().addAll(nodesToAdd));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom učitavanja kupovina.");
                }
                return null;
            }
        };

        new Thread(updateTask).start();
    }

    public void refreshKupovine() {
        Platform.runLater(() -> {
            boughtCardsVBox.getChildren().clear();

            Task<Void> refreshTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        List<Kupovina> noveKupovine = kupovinaService.pronadjiKupovinePoKorisniku(mainScreenController.korisnik);
                        List<AnchorPane> nodesToAdd = new ArrayList<>();

                        for (Kupovina kupovina : noveKupovine) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/bought-card.fxml"));
                            AnchorPane boughtCardNode = loader.load();

                            BoughtCardController controller = loader.getController();
                            controller.setPurchaseData(kupovina);
                            controller.setMainScreenController(mainScreenController);
                            controller.setBoughtCardsController(BoughtCardsController.this);

                            nodesToAdd.add(boughtCardNode);
                        }

                        Platform.runLater(() -> boughtCardsVBox.getChildren().addAll(nodesToAdd));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Greška prilikom učitavanja kupovina.");
                    }
                    return null;
                }
            };

            new Thread(refreshTask).start();
        });
    }

    @FXML
    public void initialize() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        kupovinaService = new KupovinaService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
    }

    @FXML
    void handlePreuzmiSve(ActionEvent event) {
        // Implement batch download logic here
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
