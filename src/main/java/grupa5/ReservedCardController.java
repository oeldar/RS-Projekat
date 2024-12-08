package grupa5;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.services.KupovinaService;
import grupa5.baza_podataka.services.RezervacijaService;
import grupa5.support_classes.Obavjest;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;

// @SuppressWarnings({"exports", "unused"})
public class ReservedCardController {

    @FXML
    private Label dateTimeLbl;

    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventLNameLbl;

    @FXML
    private Label istekRezervacijeLbl;

    @FXML
    private Button kupiBtn;

    @FXML
    private Label locationLbl;

    @FXML
    private Button otkaziBtn;

    @FXML
    private Label priceLbl;

    @FXML
    private Label sectorLbl;

    @FXML
    private Label ticketsNumberLbl;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    private MainScreenController mainScreenController;
    private ReservedCardsController reservedCardsController;
    private ReservationBuyController reservationBuyController;

    private EntityManagerFactory emf;
    private KupovinaService kupovinaService;
    private RezervacijaService rezervacijaService;

    private Rezervacija rezervacija;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");

        rezervacijaService = new RezervacijaService(emf);
        kupovinaService = new KupovinaService(emf);
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setReservationBuyController(ReservationBuyController reservationBuyController) {
        this.reservationBuyController = reservationBuyController;
    }

    public void setReservedCardsController(ReservedCardsController reservedCardsController) {
        this.reservedCardsController = reservedCardsController;
    }

    public void setReservationData(Rezervacija rezervacija) {
        if (rezervacija != null) {
            this.rezervacija = rezervacija;
            Dogadjaj dogadjaj = rezervacija.getDogadjaj();
            Korisnik korisnik = rezervacija.getKorisnik();

            // Set data labels
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");
            dateTimeLbl.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", rezervacija.getUkupnaCijena()) + " KM");
            ticketsNumberLbl.setText(String.valueOf(rezervacija.getBrojKarata()));
            sectorLbl.setText(rezervacija.getKarta().getSektorNaziv());
            istekRezervacijeLbl.setText(rezervacija.getKarta().getPoslednjiDatumZaRezervaciju().format(formatter));

            if (rezervacija.getStatus().equals(Rezervacija.Status.NEAKTIVNA)) {
                kupiBtn.setText("Zamijeni");
            }

            // Load event image lazily
            loadEventImageLazy(dogadjaj.getPutanjaDoSlike());
        }
    }

    private void loadEventImageLazy(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));
        eventImg.setImage(defaultImage); // Set default image first

        // Load the event image lazily in a background thread
        if (imagePath != null && !imagePath.isEmpty()) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                        if (imageStream != null) {
                            return new Image(imageStream);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading image from path: " + imagePath);
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                Image eventImage = loadImageTask.getValue();
                if (eventImage != null) {
                    eventImg.setImage(eventImage);
                }
            });
            new Thread(loadImageTask).start();
        }
    }

    @FXML
    void handleKupi(ActionEvent event) {
        if (kupiBtn.getText().equals("Zamijeni")) {
            boolean zamjenaUspjesna = showWindow("Zamjena rezervacije");

            if (zamjenaUspjesna) {
                rezervacijaService.refundirajRezervacijuKarte(rezervacija, mainScreenController);
                rezervacijaService.obrisiRezervaciju(rezervacija.getRezervacijaID());
            }
        } else {
            if (rezervacija.getDatumDogadjajaPromijenjen()) {
                rezervacija.setDatumDogadjajaPromijenjen(false);
                rezervacijaService.azurirajRezervaciju(rezervacija);
            }
            kupovinaService.kupiKartu(rezervacija, rezervacija.getKarta(), rezervacija.getBrojKarata(), rezervacija.getUkupnaCijena(), rezervacija.getKorisnik(), mainScreenController);
        }
        reservedCardsController.refreshReservations();
    }

    @FXML
    void handleOtkazi(ActionEvent event) {
        if (rezervacija.getStatus().equals(Rezervacija.Status.NEAKTIVNA) || rezervacija.getDatumDogadjajaPromijenjen()) {
            rezervacijaService.refundirajRezervacijuKarte(rezervacija, mainScreenController);
        }
        rezervacijaService.otkaziRezervaciju(rezervacija);
        Obavjest.showAlert(Alert.AlertType.INFORMATION,"Uspjeh", "Uspješno otkazana rezervacija", "Uspješno ste otkazali rezervaciju.");
        reservedCardsController.refreshReservations();
    }

    private boolean showWindow(String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reservation.fxml"));
            Parent root = loader.load();
    
            ReservationBuyController reservationBuyController = loader.getController();
            reservationBuyController.setTip(title);
            reservationBuyController.setEvent(rezervacija.getDogadjaj());
            reservationBuyController.setLoggedInUser(mainScreenController.korisnik);
            reservationBuyController.setMainScreenController(mainScreenController);
    
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
    
            stage.setMinWidth(871);
            stage.setMaxWidth(880);
            stage.setMinHeight(568);
    
            stage.showAndWait(); // Waits for the window to close
    
            return reservationBuyController.isZamjenaUspjesna(); // Check if the exchange was successful
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }    
}