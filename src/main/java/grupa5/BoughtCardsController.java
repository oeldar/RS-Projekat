package grupa5;

import java.io.IOException;
import java.util.List;

import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.KupovinaService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.Rezervacija.RezervacijaStatus;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
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
        if (kupovine == null) {
            System.err.println("Kupovine su null u updateUI.");
            return;
        }

        try {
            for (Kupovina kupovina : kupovine) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/bought-card.fxml"));
                AnchorPane boughtCardNode = loader.load();

                BoughtCardController controller = loader.getController();
                controller.setPurchaseData(kupovina);
                controller.setMainScreenController(mainScreenController);
                controller.setBoughtCardsController(this);

                boughtCardsVBox.getChildren().add(boughtCardNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Greška prilikom učitavanja kupovina.");
        }
    }

    public void refreshKupovine() {
        Platform.runLater(() -> {
            boughtCardsVBox.getChildren().clear();

            try {
                List<Kupovina> noveKupovine = kupovinaService.pronadjiKupovinePoKorisniku(mainScreenController.korisnik);
                for (Kupovina kupovina : noveKupovine) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/bought-card.fxml"));
                    AnchorPane boughtCardNode = loader.load();

                    BoughtCardController controller = loader.getController();
                    controller.setPurchaseData(kupovina);
                    controller.setMainScreenController(mainScreenController);
                    controller.setBoughtCardsController(this);

                    boughtCardsVBox.getChildren().add(boughtCardNode);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Greška prilikom učitavanja kupovina.");
            }
        });
    }

    @FXML
    public void initialize() {
        kupovinaService = new KupovinaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        kartaService = new KartaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handlePreuzmiSve(ActionEvent event) {

    }

}
