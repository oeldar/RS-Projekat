package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.services.KartaService;
import grupa5.baza_podataka.services.KupovinaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BoughtCardsController {

    @FXML
    private VBox boughtCardsVBox;

    @FXML
    private AnchorPane nemaKupljenihPane;

    @FXML
    private ImageView loading;  // Dodano za loading sliku

    private MainScreenController mainScreenController;
    private List<Kupovina> kupovine;
    private KupovinaService kupovinaService;
    private KartaService kartaService;

    private RotateTransition rotateTransition;  // Animacija za rotiranje slike

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
            nemaKupljenihPane.setVisible(true);
            loading.setVisible(false);  // Sakrij loading ako nema kupovina
            stopLoadingAnimation();     // Zaustavi animaciju
            return;
        } else {
            nemaKupljenihPane.setVisible(false);
        }

        boughtCardsVBox.getChildren().clear();
        loading.setVisible(true);  // Prikaži loading dok se kupovine učitavaju
        startLoadingAnimation();   // Pokreni animaciju

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
                    Platform.runLater(() -> {
                        boughtCardsVBox.getChildren().addAll(nodesToAdd);
                        loading.setVisible(false);  // Sakrij loading kada se sve učita
                        stopLoadingAnimation();    // Zaustavi animaciju
                    });
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
            loading.setVisible(true);  // Prikaži loading dok se kupovine osvježavaju
            startLoadingAnimation();   // Pokreni animaciju

            Task<Void> refreshTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        List<Kupovina> noveKupovine = kupovinaService.pronadjiKupovinePoKorisniku(mainScreenController.korisnik);
                        List<AnchorPane> nodesToAdd = new ArrayList<>();

                        if (noveKupovine.isEmpty()) {
                            nemaKupljenihPane.setVisible(true);
                            loading.setVisible(false);  // Sakrij loading ako nema kupovina
                            stopLoadingAnimation();     // Zaustavi animaciju
                        } else {
                            nemaKupljenihPane.setVisible(false);

                            for (Kupovina kupovina : noveKupovine) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/bought-card.fxml"));
                                AnchorPane boughtCardNode = loader.load();

                                BoughtCardController controller = loader.getController();
                                controller.setPurchaseData(kupovina);
                                controller.setMainScreenController(mainScreenController);
                                controller.setBoughtCardsController(BoughtCardsController.this);

                                nodesToAdd.add(boughtCardNode);
                            }

                            Platform.runLater(() -> {
                                boughtCardsVBox.getChildren().addAll(nodesToAdd);
                                loading.setVisible(false);  // Sakrij loading kada se sve učita
                                stopLoadingAnimation();    // Zaustavi animaciju
                            });
                        }

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

    // Metoda za pokretanje rotacije loading GIF-a
    private void startLoadingAnimation() {
        if (rotateTransition == null) {
            rotateTransition = new RotateTransition();
            rotateTransition.setNode(loading);
            rotateTransition.setDuration(Duration.seconds(2));  // Trajanje rotacije (2 sekunde)
            rotateTransition.setByAngle(360);  // Rotira za 360 stepeni
            rotateTransition.setCycleCount(RotateTransition.INDEFINITE);  // Animacija se ponavlja beskonačno
        }

        rotateTransition.play();  // Pokreni animaciju
    }

    // Metoda za zaustavljanje rotacije loading GIF-a
    private void stopLoadingAnimation() {
        if (rotateTransition != null) {
            rotateTransition.stop();  // Zaustavi animaciju
        }
    }

    @FXML
    public void initialize() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        kupovinaService = new KupovinaService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
    }

}
