package grupa5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.KorisnikService;
import grupa5.baza_podataka.KupovinaService;
import grupa5.baza_podataka.Novcanik;
import grupa5.baza_podataka.NovcanikService;
import grupa5.baza_podataka.Popust;
import grupa5.baza_podataka.PopustService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ReservationBuyController {

    @FXML
    private TextField brojKarti;

    @FXML
    private AnchorPane brojKartiPane, cijenaPane, mainAnchorPane;

    @FXML
    private Label cijena, nazivLbl;

    @FXML
    private VBox sektoriVBox;
    @FXML
    private VBox sektoriCijeneVBox;

    @FXML
    private Button reservationBuyBtn;

    private EntityManagerFactory emf;
    private KartaService kartaService;
    private RezervacijaService rezervacijaService;
    private KupovinaService kupovinaService;
    private NovcanikService novcanikService;
    private PopustService popustService;
    private String tip;
    private Korisnik korisnik;
    private Dogadjaj dogadjaj;
    private Button activeSectorButton;
    private EventDetailsController eventDetailsController;
    private MainScreenController mainScreenController;

    @FXML
    public void initialize() {
        brojKarti.setText("1");
        configureBrojKartiTextField();
        cijena.setText("0.00");
        reservationBuyBtn.setText(tip);

        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");

        kartaService = new KartaService(emf);
        rezervacijaService = new RezervacijaService(emf);
        kupovinaService = new KupovinaService(emf);
        popustService = new PopustService(emf);
        novcanikService = new NovcanikService(emf);
        
        brojKarti.textProperty().addListener((observable, oldValue, newValue) -> updatePriceAndTotal(newValue));
    }

    public void setTip(String tip) {
        this.tip = tip;
        if (tip.equals("Kupovina")) {
            reservationBuyBtn.setText("Kupi");
        } else if (tip.equals("Rezervacija")) {
            reservationBuyBtn.setText("Rezerviši");
        }
    }

    public void setEvent(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
        nazivLbl.setText(dogadjaj.getNaziv());
        loadSectorsAndPrices();
    }

    public void setLoggedInUser(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public void setEventDetailsController(EventDetailsController eventDetailsController) {
        this.eventDetailsController = eventDetailsController;
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public static boolean jeRezervacijaDozvoljena(LocalDateTime datumDogadjaja) {
        LocalDateTime sada = LocalDateTime.now();
        LocalDateTime datumKrajaRezervacije = datumDogadjaja.minusDays(4);
        return sada.isBefore(datumKrajaRezervacije);
    }

    private void loadSectorsAndPrices() {
        if (dogadjaj != null) {
            List<Karta> karte = kartaService.pronadjiKartePoDogadjaju(dogadjaj);

            if (!sektoriVBox.getChildren().isEmpty()) {
                sektoriVBox.getChildren().remove(1, sektoriVBox.getChildren().size());
            }    
            sektoriCijeneVBox.getChildren().clear(); // Očisti HBox-ove za cijene sektora

            double buttonWidth = 200;

            for (Karta karta : karte) {
                // Kreiraj dugme za sektor
                Button sectorButton = new Button(karta.getSektor().getNaziv());
                sectorButton.setId("btn" + karta.getKartaID());
                sectorButton.getStyleClass().add("sektor");
                sectorButton.setPrefWidth(buttonWidth);

                if (karta.getDostupneKarte() <= 0) {
                    sectorButton.setDisable(true);
                    sectorButton.getStyleClass().add("sektor-rasprodat");
                } else {
                    sectorButton.setOnAction(e -> handleSectorButtonClick(sectorButton));
                }
                
                sektoriVBox.getChildren().add(sectorButton);

                // Kreiraj HBox za naziv i cijenu sektora
                HBox sectorHBox = new HBox(10);
                Label sectorNameLabel = new Label(karta.getSektor().getNaziv() + ":");
                sectorNameLabel.getStyleClass().add("opis");
                sectorNameLabel.setStyle("-fx-font-weight: bold;");
                Label sectorPriceLabel = new Label(String.format("%.2f KM", karta.getCijena()));
                sectorPriceLabel.getStyleClass().add("opis");
                sectorHBox.getChildren().addAll(sectorNameLabel, sectorPriceLabel);
                sektoriCijeneVBox.getChildren().add(sectorHBox);
            }
        }
    }

    private void handleSectorButtonClick(Button clickedButton) {
        if (activeSectorButton != null) {
            activeSectorButton.getStyleClass().remove("sektor-aktivni");
        }

        clickedButton.getStyleClass().add("sektor-aktivni");
        activeSectorButton = clickedButton;
        updatePriceAndTotal(brojKarti.getText());
        Integer maxBrojKarti = getMaxBrojKartiPoSektoru();
        if (Integer.parseInt(brojKarti.getText()) > maxBrojKarti) {
            brojKarti.setText(maxBrojKarti.toString());
        }
        moveComponentsTo(clickedButton.getId());
    }

    private void updatePriceAndTotal(String brojKartiValue) {
        if (activeSectorButton != null) {
            String buttonId = activeSectorButton.getId();
            int kartaId = Integer.parseInt(buttonId.replace("btn", ""));
            Karta karta = kartaService.pronadjiKartuPoID(kartaId);
    
            if (karta != null) {
                int brojKarata;
                try {
                    brojKarata = Integer.parseInt(brojKartiValue);
                } catch (NumberFormatException e) {
                    brojKarata = 1;
                }
                
                int maxBrojKarata = karta.getDostupneKarte() < karta.getMaxBrojKartiPoKorisniku() ? karta.getDostupneKarte() : karta.getMaxBrojKartiPoKorisniku();
                if (brojKarata < 1) brojKarata = 1;
                if (brojKarata > maxBrojKarata) brojKarata = maxBrojKarata;
                
                double ukupnaCijena = karta.getCijena() * brojKarata;
                cijena.setText(String.format("%.2f KM", ukupnaCijena));
            }
        }
    }
    

    @FXML
    void handleReservationBuyBtnAction(ActionEvent event) {
        if (korisnik == null) {
            Stage stage = (Stage) reservationBuyBtn.getScene().getWindow();
            stage.close();
            showAlert("Niste prijavljeni", "Morate se prijaviti ili registrovati.");
            return;
        }

        try {
            int brojKarata = Integer.parseInt(brojKarti.getText());
            int maxBrojKarti = getMaxBrojKartiPoSektoru();
            if (brojKarata > maxBrojKarti) {
                showAlert("Nevalidan unos", "Broj karata ne može biti veći od " + maxBrojKarti + ".");
                return;
            }

            int maxTicketsPerUser = getMaxBrojKartiZaDogadjaj();

            int reservedTickets = rezervacijaService.pronadjiBrojAktivnihRezervisanihKarata(dogadjaj, korisnik);
            int purchasedTickets = kupovinaService.pronadjiBrojKupljenihKarata(dogadjaj, korisnik);
            int totalTickets = reservedTickets + purchasedTickets;

            if (totalTickets + brojKarata > maxTicketsPerUser) {
                showAlert("Nevalidan unos", "Ne možete rezervisati ili kupiti više od " + maxTicketsPerUser + " karata za ovaj događaj.");
                return;
            }

            if (activeSectorButton != null) {
                String id = activeSectorButton.getId();
                int kartaId = Integer.parseInt(id.replace("btn", ""));
                Karta karta = kartaService.pronadjiKartuPoID(kartaId);

                if ("Rezervacija".equals(tip)) {
                    if (!jeRezervacijaDozvoljena(dogadjaj.getDatum().atTime(dogadjaj.getVrijeme()))) {
                        showAlert("Prošlo vreme za rezervaciju", "Ne možete rezervisati, kupite kartu.");
                        return;
                    }

                    double ukupnaCijena = calculateTotalPrice(brojKarata);
                    rezervacijaService.kreirajRezervaciju(dogadjaj, korisnik, karta, LocalDateTime.now(), brojKarata, ukupnaCijena);

                    karta.setBrojRezervisanih(karta.getBrojRezervisanih() + brojKarata);
                    karta.setDostupneKarte(karta.getDostupneKarte() - brojKarata);
                    kartaService.azurirajKartu(karta);

                    showAlert("Rezervacija uspešna", "Vaša rezervacija je uspešno sačuvana.");

                } else if ("Kupovina".equals(tip)) {
                    if (karta.getDostupneKarte() < brojKarata) {
                        showAlert("Greška", "Nema dovoljno dostupnih karata.");
                        return;
                    }

                    double ukupnaCijena = calculateTotalPrice(brojKarata);
                    
                    List<Popust> dostupniPopusti = popustService.pronadjiPopustePoKorisniku(korisnik.getKorisnickoIme());
                    double popust = 0;
                    if (!dostupniPopusti.isEmpty()) {
                        Popust odabraniPopust = DiscountDialog.promptForDiscount(dostupniPopusti);
                        if (odabraniPopust != null) {
                            popust = odabraniPopust.getVrijednostPopusta();
                            popustService.iskoristiPopust(odabraniPopust.getPopustID());
                        }
                    }

                    double konacnaCijena = ukupnaCijena - popust;

                    Novcanik novcanik = novcanikService.pronadjiNovcanik(korisnik.getKorisnickoIme());

                    if (novcanik.getStanje() < konacnaCijena) {
                        showAlert("Greška", "Nemate dovoljno sredstava u novčaniku za ovu kupovinu.");
                        return;
                    }

                    kupovinaService.kreirajKupovinu(dogadjaj, korisnik, karta, null, LocalDateTime.now(), brojKarata, ukupnaCijena, popust, konacnaCijena);

                    karta.setBrojKupljenih(karta.getBrojKupljenih() + brojKarata);
                    karta.setDostupneKarte(karta.getDostupneKarte() - brojKarata);
                    kartaService.azurirajKartu(karta);

                    novcanik.setStanje(novcanik.getStanje() - konacnaCijena);
                    novcanikService.azurirajNovcanik(novcanik);

                    mainScreenController.setStanjeNovcanika(novcanik.getStanje());

                    showAlert("Kupovina uspešna", "Vaša kupovina je uspešno sačuvana.");
                }

                Stage stage = (Stage) reservationBuyBtn.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Niste odabrali sektor", "Izaberite sektor i broj karti.");
            }
        } catch (NumberFormatException e) {
            showAlert("Greška", "Unesite validan broj karata.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška", "Došlo je do greške pri rezervaciji ili kupovini.");
        }
    }

    private double calculateTotalPrice(int brojKarata) {
        if (activeSectorButton == null) {
            return 0;
        }

        String id = activeSectorButton.getId();
        int kartaId = Integer.parseInt(id.replace("btn", ""));
        Karta karta = kartaService.pronadjiKartuPoID(kartaId);
    
        if (karta != null) {
            int maxBrojKarti = karta.getMaxBrojKartiPoKorisniku();
            if (brojKarata > maxBrojKarti) {
                brojKarata = maxBrojKarti;
            }
            return karta.getCijena() * brojKarata;
        }
        return 0;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void moveComponentsTo(String buttonId) {
        Button selectedButton = (Button) mainAnchorPane.lookup("#" + buttonId);
        double newY = selectedButton.getLayoutY();

        // Animiraj pomeranje komponenti
        animirajKomponentu(brojKartiPane, brojKartiPane.getLayoutX(), newY - 50);
        animirajKomponentu(cijenaPane, cijenaPane.getLayoutX(), newY - 50);
    }

    private void animirajKomponentu(javafx.scene.Node node, double newX, double newY) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(500));
        transition.setNode(node);
        transition.setToX(newX);
        transition.setToY(newY);
        transition.play();
    }

    private void configureBrojKartiTextField() {
        brojKarti.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();

            if (!character.matches("\\d")) {
                event.consume(); // Blokiraj unos ako nije cifra
            } else {
                try {
                    String currentText = brojKarti.getText() + character;
                    int currentValue = Integer.parseInt(currentText);
                    int maxBrojKarti = getMaxBrojKartiPoSektoru();
                    if (currentValue > maxBrojKarti || currentValue < 1) {
                        event.consume();
                    }
                } catch (NumberFormatException e) {
                    event.consume();
                }
            }
        });
    }

    private int getMaxBrojKartiPoSektoru(){
        if (activeSectorButton == null) {
            return 1;
        }

        String id = activeSectorButton.getId();
        int kartaId = Integer.parseInt(id.replace("btn", ""));
        Karta karta = kartaService.pronadjiKartuPoID(kartaId);
        int maxBrojKarti = karta.getDostupneKarte() < karta.getMaxBrojKartiPoKorisniku() ? karta.getDostupneKarte() : karta.getMaxBrojKartiPoKorisniku();

        return karta != null ? maxBrojKarti : 1;
    }

    private int getMaxBrojKartiZaDogadjaj() {
        if (dogadjaj == null) {
            return 0;
        }
    
        // Get any one sector associated with the event
        Karta karta = kartaService.pronadjiKartePoDogadjaju(dogadjaj).stream().findFirst().orElse(null);
        if (karta != null) {
            return karta.getMaxBrojKartiPoKorisniku();
        }
    
        return 0;
    }    

    @FXML
    public void incBrojKarti() {
        int currentValue = Integer.parseInt(brojKarti.getText());
        int maxBrojKarti = getMaxBrojKartiPoSektoru();
        if (currentValue < maxBrojKarti) {
            brojKarti.setText(String.valueOf(currentValue + 1));
        }
    }

    @FXML
    public void decBrojKarti() {
        int currentValue = Integer.parseInt(brojKarti.getText());
        if (currentValue > 1) {
            brojKarti.setText(String.valueOf(currentValue - 1));
        }
    }

    @FXML
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @FXML
    public void onCloseRequest(WindowEvent event) {
        close();
    }
}