package grupa5;

import java.io.IOException;
import java.util.List;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.KorisnikService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class ReservedCardsController {

    @FXML
    private VBox reservedCardsVBox;

    private MainScreenController mainScreenController;
    private List<Rezervacija> rezervacije;
    private RezervacijaService rezervacijaService;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setRezervacije(List<Rezervacija> rezervacije) {
        this.rezervacije = rezervacije;
        // Schedule the UI update after the FXML has been initialized
        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        if (rezervacije == null) {
            System.err.println("Rezervacije su null u updateUI.");
            return;
        }

        try {
            for (Rezervacija rezervacija : rezervacije) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                AnchorPane reservedCardNode = loader.load();

                ReservedCardController controller = loader.getController();
                controller.setReservationData(rezervacija);
                controller.setMainScreenController(mainScreenController);
                controller.setReservedCardsController(this);

                reservedCardsVBox.getChildren().add(reservedCardNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Greška prilikom učitavanja rezervacija.");
        }
    }

    public void refreshReservations() {
        Platform.runLater(() -> {
            // Očistite trenutne prikazane rezervacije
            reservedCardsVBox.getChildren().clear();
            
            // Ponovo učitajte rezervacije
            try {
                List<Rezervacija> noveRezervacije = rezervacijaService.pronadjiAktivneRezervacijePoKorisniku(mainScreenController.korisnik);
                for (Rezervacija rezervacija : noveRezervacije) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-card.fxml"));
                    AnchorPane reservedCardNode = loader.load();
    
                    ReservedCardController controller = loader.getController();
                    controller.setReservationData(rezervacija);
                    controller.setMainScreenController(mainScreenController);
                    controller.setReservedCardsController(this);
    
                    reservedCardsVBox.getChildren().add(reservedCardNode);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Greška prilikom učitavanja rezervacija.");
            }
        });
    }    

    @FXML
    public void initialize() {
        rezervacijaService = new RezervacijaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        // System.out.println("initialize() called");
    }
}