package grupa5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.KupovinaService;
import grupa5.baza_podataka.Novcanik;
import grupa5.baza_podataka.NovcanikService;
import grupa5.baza_podataka.Popust;
import grupa5.baza_podataka.PopustService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.Rezervacija.RezervacijaStatus;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

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

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    private MainScreenController mainScreenController;
    private ReservedCardsController reservedCardsController;
    private PopustService popustService;
    private NovcanikService novcanikService;
    private KupovinaService kupovinaService;
    private KartaService kartaService;
    private RezervacijaService rezervacijaService;
    private Rezervacija rezervacija;


    @FXML
    public void initialize(){
        popustService = new PopustService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        kartaService = new KartaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        novcanikService = new NovcanikService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        kupovinaService = new KupovinaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        rezervacijaService = new RezervacijaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setReservedCardsController(ReservedCardsController reservedCardsController) {
        this.reservedCardsController = reservedCardsController;
    }

    public void setReservationData(Rezervacija rezervacija) {
        if (rezervacija != null) {
            this.rezervacija = rezervacija;
            Dogadjaj dogadjaj = rezervacija.getDogadjaj();
            Korisnik korisnik = rezervacija.getKorisnik();

            nameLbl.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", rezervacija.getUkupnaCijena()));
            ticketsNumberLbl.setText(String.valueOf(rezervacija.getBrojKarata()));
            sectorLbl.setText(rezervacija.getKarta().getSektorNaziv());

            loadEventImage(dogadjaj.getPutanjaDoSlike());
        }
    }

    private void loadEventImage(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));

        if (imagePath != null && !imagePath.isEmpty()) {
            try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                if (imageStream != null) {
                    Image eventImage = new Image(imageStream);
                    eventImg.setImage(eventImage);
                } else {
                    eventImg.setImage(defaultImage);
                }
            } catch (Exception e) {
                System.err.println("Error loading image from path: " + imagePath);
                e.printStackTrace();
                eventImg.setImage(defaultImage);
            }
        } else {
            eventImg.setImage(defaultImage);
        }
    }

    @FXML
    void handleKupi(ActionEvent event) {
        List<Popust> dostupniPopusti = popustService.pronadjiPopustePoKorisniku(rezervacija.getKorisnik().getKorisnickoIme());
        double popust = 0;
        if (!dostupniPopusti.isEmpty()) {
            Popust odabraniPopust = DiscountDialog.promptForDiscount(dostupniPopusti);
            if (odabraniPopust != null) {
                popust = odabraniPopust.getVrijednostPopusta();
                popustService.iskoristiPopust(odabraniPopust.getPopustID());
            }
        }

        double konacnaCijena = rezervacija.getUkupnaCijena() - popust;

        Novcanik novcanik = novcanikService.pronadjiNovcanik(rezervacija.getKorisnik().getKorisnickoIme());

        if (novcanik.getStanje() < konacnaCijena) {
            showAlert("Greška", "Nemate dovoljno sredstava u novčaniku za ovu kupovinu.");
            return;
        }

        Karta karta = rezervacija.getKarta();

        // Kreiraj kupovinu
        kupovinaService.kreirajKupovinu(rezervacija.getDogadjaj(), rezervacija.getKorisnik(), rezervacija.getKarta(), rezervacija, LocalDateTime.now(),
                                        rezervacija.getBrojKarata(), rezervacija.getUkupnaCijena(), popust, konacnaCijena, null);

        // Ažuriraj kartu
        karta.setBrojKupljenih(karta.getBrojKupljenih() + rezervacija.getBrojKarata());
        karta.setDostupneKarte(karta.getDostupneKarte() - rezervacija.getBrojKarata());
        kartaService.azurirajKartu(karta);

        // Ažuriraj novčanik
        novcanik.setStanje(novcanik.getStanje() - konacnaCijena);
        novcanikService.azurirajNovcanik(novcanik);

        // Ažuriraj status rezervacije na KUPLJENA
        rezervacija.setStatus(RezervacijaStatus.KUPLJENA);
        rezervacijaService.azurirajRezervaciju(rezervacija);

        reservedCardsController.refreshReservations();

        mainScreenController.setStanjeNovcanika(novcanik.getStanje());

        // reservedCardsController.setRezervacije(rezervacijaService.pronadjiAktivneRezervacijePoKorisniku(rezervacija.getKorisnik()));
        showAlert("Kupovina uspešna", "Vaša kupovina je uspešno sačuvana.");
    }

    @FXML
    void handleOtkazi(ActionEvent event) {
        if (rezervacija == null) {
            showAlert("Greška", "Nema rezervacije za otkazivanje.");
            return;
        }

        Karta karta = rezervacija.getKarta();
        if (karta == null) {
            showAlert("Greška", "Nije moguće pronaći kartu za ovu rezervaciju.");
            return;
        }

        // Ažuriraj kartu
        karta.setDostupneKarte(karta.getDostupneKarte() + rezervacija.getBrojKarata());
        karta.setBrojRezervisanih(karta.getBrojRezervisanih() - rezervacija.getBrojKarata());
        kartaService.azurirajKartu(karta);

        // Ažuriraj status rezervacije na OTKAZANA
        rezervacija.setStatus(RezervacijaStatus.OTKAZANA);
        rezervacijaService.azurirajRezervaciju(rezervacija);

        // reservedCardsController.setRezervacije(rezervacijaService.pronadjiAktivneRezervacijePoKorisniku(rezervacija.getKorisnik()));
        reservedCardsController.refreshReservations();
        showAlert("Otkazivanje uspešno", "Vaša rezervacija je uspešno otkazana.");
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
