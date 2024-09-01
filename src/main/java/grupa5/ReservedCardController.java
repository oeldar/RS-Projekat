package grupa5;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import grupa5.baza_podataka.Popust.TipPopusta;
import grupa5.baza_podataka.PopustService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
import grupa5.baza_podataka.StatistikaKupovine;
import grupa5.baza_podataka.StatistikaKupovineService;
import grupa5.baza_podataka.TransakcijaService;
import grupa5.baza_podataka.Kupovina.Status;
import grupa5.baza_podataka.Transakcija.TipTransakcije;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

    private KupovinaService kupovinaService;
    private PopustService popustService;
    private NovcanikService novcanikService;
    private KartaService kartaService;
    private RezervacijaService rezervacijaService;
    private TransakcijaService transakcijaService;
    private StatistikaKupovineService statistikaKupovineService;

    private Rezervacija rezervacija;

    // Lazy initialization of services
    private KupovinaService getKupovinaService() {
        if (kupovinaService == null) {
            kupovinaService = new KupovinaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        }
        return kupovinaService;
    }

    private PopustService getPopustService() {
        if (popustService == null) {
            popustService = new PopustService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        }
        return popustService;
    }

    private NovcanikService getNovcanikService() {
        if (novcanikService == null) {
            novcanikService = new NovcanikService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        }
        return novcanikService;
    }

    private KartaService getKartaService() {
        if (kartaService == null) {
            kartaService = new KartaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        }
        return kartaService;
    }

    private RezervacijaService getRezervacijaService() {
        if (rezervacijaService == null) {
            rezervacijaService = new RezervacijaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        }
        return rezervacijaService;
    }

    public TransakcijaService getTransakcijaService() {
        if (transakcijaService == null) {
            transakcijaService = new TransakcijaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
            
        }
        return transakcijaService;
    }

    public StatistikaKupovineService getStatistikaKupovineService() {
        if (statistikaKupovineService == null) {
            statistikaKupovineService = new StatistikaKupovineService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        }
        return statistikaKupovineService;
    }

    @FXML
    public void initialize() {
        // Initialize only UI components, services are initialized lazily
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
            Karta karta = rezervacija.getKarta();

            // Set data labels
            nameLbl.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", rezervacija.getUkupnaCijena()));
            ticketsNumberLbl.setText(String.valueOf(rezervacija.getBrojKarata()));
            sectorLbl.setText(rezervacija.getKarta().getSektorNaziv());

            if (rezervacija.getStatus().equals(Rezervacija.Status.NEAKTIVNA)) {
                kupiBtn.setText("Zamijeni");
                if (karta.getNaplataOtkazivanjaRezervacije() != null && karta.getNaplataOtkazivanjaRezervacije() > 0.0) {
                    otkaziBtn.setText("Refundiraj");
                } else {
                    otkaziBtn.setText("Odustani");
                }
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
            // TODO: napisati logiku za zamijenu rezervacija i provjeriti da li su dostupne
            return;
        }
        //System.out.println("Handle Kupi button clicked");

        // Create an object for synchronization
        final Object syncObject = new Object();

        // Create and start the purchase task
        Task<Void> purchaseTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                //System.out.println("Fetching discounts...");
                List<Popust> dostupniPopusti = getPopustService().pronadjiPopustePoKorisniku(rezervacija.getKorisnik().getKorisnickoIme());
                //System.out.println("Discounts fetched: " + dostupniPopusti.size());

                final Popust[] odabraniPopust = {null};

                // Display the discount dialog on the JavaFX Application Thread
                Platform.runLater(() -> {
                    odabraniPopust[0] = DiscountDialog.promptForDiscount(dostupniPopusti);
                    synchronized (syncObject) {
                        syncObject.notify(); // Notify the task after the dialog has been handled
                    }
                });

                // Wait for the dialog to be handled
                synchronized (syncObject) {
                    syncObject.wait();
                }
                

                double popust = 0;
                if (odabraniPopust[0] != null) {
                    popust = odabraniPopust[0].getVrijednostPopusta();
                    // System.out.println("Applying discount: " + popust);
                    getPopustService().iskoristiPopust(odabraniPopust[0].getPopustID());
                }

                double konacnaCijena = rezervacija.getUkupnaCijena() - popust;
                //System.out.println("Final price after discount: " + konacnaCijena);

                if (konacnaCijena < 0.0) {
                    konacnaCijena = 0.0;
                }

                Novcanik novcanik = getNovcanikService().pronadjiNovcanik(rezervacija.getKorisnik().getKorisnickoIme());
                //System.out.println("Wallet balance: " + novcanik.getStanje());

                novcanik.setStanje(novcanik.getStanje() + rezervacija.getKarta().getNaplataOtkazivanjaRezervacije() * rezervacija.getBrojKarata());
                getNovcanikService().azurirajNovcanik(novcanik);

                if (novcanik.getStanje() < konacnaCijena) {
                    Platform.runLater(() -> Obavjest.showAlert("Greška", "Nemate dovoljno sredstava u novčaniku za ovu kupovinu."));
                    return null;
                }

                Karta karta = rezervacija.getKarta();
                //System.out.println("Creating purchase...");

                // Kreiraj kupovinu
                getKupovinaService().kreirajKupovinu(rezervacija.getDogadjaj(), rezervacija.getKorisnik(), rezervacija.getKarta(), rezervacija, LocalDateTime.now(),
                        rezervacija.getBrojKarata(), rezervacija.getUkupnaCijena(), popust, konacnaCijena);

                // System.out.println("Updating ticket...");
                // Ažuriraj kartu
                karta.setBrojRezervisanih(karta.getBrojRezervisanih() - rezervacija.getBrojKarata());
                if (karta.getDostupneKarte() <= 0 && karta.getBrojRezervisanih() <= 0) {
                    karta.setStatus(Karta.Status.PRODATA);
                } else if (karta.getDostupneKarte() <= 0) {
                    karta.setStatus(Karta.Status.REZERVISANA);
                }
                getKartaService().azurirajKartu(karta);

                // System.out.println("Updating wallet...");
                // Ažuriraj novčanik
                novcanik.setStanje(novcanik.getStanje() - konacnaCijena);
                getNovcanikService().azurirajNovcanik(novcanik);

                StatistikaKupovine statistikaKupovine = getStatistikaKupovineService().pronadjiStatistikuKupovineZaKorisnika(rezervacija.getKorisnik().getKorisnickoIme());
                int n = statistikaKupovine.getUkupnoKupljenihKarata() % 10;
                int brojPopusta = (n + rezervacija.getBrojKarata()) / 10;
                while (brojPopusta != 0) {
                    popustService.kreirajPopust(rezervacija.getKorisnik().getKorisnickoIme(), TipPopusta.BROJ_KUPOVINA, 10.0, "Svaka 10-ta kupljena karta", LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
                    --brojPopusta;
                }

                int s = (int) Math.floor(statistikaKupovine.getUkupnoPotrosenNovac()) % 200;
                brojPopusta = (int)(s + rezervacija.getUkupnaCijena()) / 200;
                while (brojPopusta != 0) {
                    popustService.kreirajPopust(rezervacija.getKorisnik().getKorisnickoIme(), TipPopusta.POTROSENI_IZNOS, 10.0, "Svakih potrošenih 200 KM", LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
                    --brojPopusta;
                }

                statistikaKupovine.setUkupnoKupljenihKarata(statistikaKupovine.getUkupnoKupljenihKarata() + rezervacija.getBrojKarata());
                statistikaKupovine.setUkupnoPotrosenNovac(statistikaKupovine.getUkupnoPotrosenNovac() + konacnaCijena);
                getStatistikaKupovineService().azurirajStatistiku(statistikaKupovine);

                getTransakcijaService().kreirajTransakciju(rezervacija.getKorisnik().getKorisnickoIme(), konacnaCijena, TipTransakcije.ISPLATA, LocalDateTime.now(), "Izvršila se kupnja karte za događaj: " + rezervacija.getDogadjaj().getNaziv());


                // System.out.println("Updating reservation status...");
                // Ažuriraj status rezervacije na KUPLJENA
                rezervacija.setStatus(Rezervacija.Status.KUPLJENA);
                getRezervacijaService().azurirajRezervaciju(rezervacija);

                Platform.runLater(() -> {
                    reservedCardsController.refreshReservations();
                    mainScreenController.setStanjeNovcanika(novcanik.getStanje());
                    Obavjest.showAlert("Kupovina uspešna", "Vaša kupovina je uspešno sačuvana.");
                });

                return null;
            }
        };

        new Thread(purchaseTask).start();
    }


    @FXML
    void handleOtkazi(ActionEvent event) {
        if (otkaziBtn.getText().equals("Odustani")) {
            // TODO: samo obrisati rezervaciju
            return;
        } else if (otkaziBtn.getText().equals("Refundiraj")) {
            // TODO: refundirati rezervaciju i obrisi rezervaciju
            return;
        }

        if (rezervacija == null) {
            Obavjest.showAlert("Greška", "Nema rezervacije za otkazivanje.");
            return;
        }

        Karta karta = rezervacija.getKarta();
        if (karta == null) {
            Obavjest.showAlert("Greška", "Nije moguće pronaći kartu za ovu rezervaciju.");
            return;
        }

        // Ažuriraj kartu
        karta.setDostupneKarte(karta.getDostupneKarte() + rezervacija.getBrojKarata());
        karta.setBrojRezervisanih(karta.getBrojRezervisanih() - rezervacija.getBrojKarata());
        getKartaService().azurirajKartu(karta);

        getRezervacijaService().obrisiRezervaciju(rezervacija.getRezervacijaID());

        reservedCardsController.refreshReservations();
        Obavjest.showAlert("Otkazivanje uspešno", "Vaša rezervacija je uspešno otkazana.");
    }

}