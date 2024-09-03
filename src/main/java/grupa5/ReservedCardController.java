package grupa5;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.KupovinaService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;

public class ReservedCardController {

    @FXML
    private Label locationLbl;

    @FXML
    private Label nameLbl;

    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventLNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label sectorLbl;

    @FXML
    private Label ticketsNumberLbl;

    @FXML
    private Button kupiBtn, otkaziBtn;

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
            nameLbl.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", rezervacija.getUkupnaCijena()));
            ticketsNumberLbl.setText(String.valueOf(rezervacija.getBrojKarata()));
            sectorLbl.setText(rezervacija.getKarta().getSektorNaziv());

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
            rezervacijaService.obrisiRezervaciju(rezervacija.getRezervacijaID());
            // TODO: kada budemo imali DogadjajPrijedlog i na osnovu toga da li je promijenjeno vrijeme ili lokacija radimo drugacije
            // ako je vrijeme onda ne raditi nista vec samo obavjestiti korisnika i korisnik moze refundirati kartu
            // ako je lokacija onda mora izabrati novi sektor i karte 
            // i skontati sta raditi ako se cijena promijeni
            // showWindow("Rezervacija");
        } else {
            kupovinaService.kupiKartu(rezervacija, rezervacija.getKarta(), rezervacija.getBrojKarata(), rezervacija.getUkupnaCijena(), rezervacija.getKorisnik(), mainScreenController);
        }
        reservedCardsController.refreshReservations();
    }


    @FXML
    void handleOtkazi(ActionEvent event) {
        rezervacijaService.refundirajRezervacijuKarte(rezervacija);
        rezervacijaService.otkaziRezervaciju(rezervacija);
        Obavjest.showAlert("Uspješno otkazana rezervacija", "Uspješno ste otkazali rezervaciju");
        reservedCardsController.refreshReservations();
    }

     private void showWindow(String title) {
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

            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}