package grupa5;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.event.ActionEvent;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import grupa5.baza_podataka.*;
import grupa5.baza_podataka.services.*;
import grupa5.support_classes.Obavjest;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// @SuppressWarnings("exports")
public class ReservationBuyController {

    @FXML
    private TextField brojKarti;

    @FXML
    private AnchorPane brojKartiPane, cijenaPane, mainAnchorPane;

    @FXML
    private Label cijena, nazivLbl, opisLbl;

    @FXML
    private ImageView lokacijaImg;

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
    private String tip;
    private Korisnik korisnik;
    private Dogadjaj dogadjaj;
    private Button activeSectorButton;
    private MainScreenController mainScreenController;

    @FXML
    public void initialize() {
        brojKarti.setText("0");
        configureBrojKartiTextField();
        cijena.setText("0.00");
        reservationBuyBtn.setText(tip);

        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");

        kartaService = new KartaService(emf);
        rezervacijaService = new RezervacijaService(emf);
        kupovinaService = new KupovinaService(emf);
        
        brojKarti.textProperty().addListener((observable, oldValue, newValue) -> updatePriceAndTotal(newValue));
    }

    public void setTip(String tip) {
        this.tip = tip;
        if (tip.equals("Kupovina")) {
            reservationBuyBtn.setText("Kupi");
            opisLbl.setText("Odaberite zonu i broj karti koje želite kupiti.");
        } else if (tip.equals("Rezervacija")) {
            reservationBuyBtn.setText("Rezerviši");
        }
    }

    public void setEvent(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
        nazivLbl.setText(dogadjaj.getNaziv());

        if (dogadjaj.getLokacija().getPutanjaDoSlike() != null && !dogadjaj.getLokacija().getPutanjaDoSlike().isEmpty()) {
            InputStream imageStream = getClass().getResourceAsStream(dogadjaj.getLokacija().getPutanjaDoSlike());
            if (imageStream != null) {
                Image locationImage = new Image(imageStream);
                lokacijaImg.setImage(locationImage);
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("assets/locations_photos/default-location.png"));
                lokacijaImg.setImage(defaultImage);
            }
        } else {
            Image defaultImage = new Image(getClass().getResourceAsStream("assets/locations_photos/default-location.png"));
            lokacijaImg.setImage(defaultImage);
        }

        loadSectorsAndPrices();
    }

    public void setLoggedInUser(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
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
        brojKarti.setText("1");
        activeSectorButton = clickedButton;
        updatePriceAndTotal(brojKarti.getText());
        updateDescription();
        Integer maxBrojKarti = getMaxBrojKartiPoSektoru();
        if (Integer.parseInt(brojKarti.getText()) > maxBrojKarti) {
            brojKarti.setText(maxBrojKarti.toString());
        }
        moveComponentsTo(clickedButton.getId());
    }

    private void updateDescription(){
        if (activeSectorButton != null) {
            String buttonId = activeSectorButton.getId();
            int kartaId = Integer.parseInt(buttonId.replace("btn", ""));
            Karta karta = kartaService.pronadjiKartuPoID(kartaId);
    
            if (karta != null) {
                if (tip.equals("Rezervacija")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");
                    opisLbl.setText("Rezervisane karte za sektor " + karta.getSektorNaziv() + " je moguće kupiti do "+ karta.getPoslednjiDatumZaRezervaciju().format(formatter) +".");
                    if (karta.getNaplataOtkazivanjaRezervacije() != null && karta.getNaplataOtkazivanjaRezervacije() > 0) {
                        opisLbl.setText(opisLbl.getText() + "\nNaplata rezervacije je " + karta.getNaplataOtkazivanjaRezervacije() + "KM po karti.");
                    }
                }
            }
        }
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
            Obavjest.showAlert("Niste prijavljeni", "Morate se prijaviti ili registrovati.");
            return;
        }

        try {
            int brojKarata = Integer.parseInt(brojKarti.getText());
            int maxBrojKarti = getMaxBrojKartiPoSektoru();
            if (brojKarata > maxBrojKarti) {
                Obavjest.showAlert("Nevalidan unos", "Broj karata ne može biti veći od " + maxBrojKarti + ".");
                return;
            }

            if (activeSectorButton != null) {
                String id = activeSectorButton.getId();
                int kartaId = Integer.parseInt(id.replace("btn", ""));
                Karta karta = kartaService.pronadjiKartuPoID(kartaId);

                if ("Rezervacija".equals(tip)) {
                    int maxTicketsPerUser = karta.getMaxBrojKartiPoKorisniku();

                    int reservedTickets = rezervacijaService.pronadjiBrojAktivnihRezervisanihKarata(karta, korisnik);
                    int purchasedTickets = kupovinaService.pronadjiBrojKupljenihKarata(karta, korisnik);
                    int totalTickets = reservedTickets + purchasedTickets;
            
                    if (totalTickets + brojKarata > maxTicketsPerUser) {
                        Obavjest.showAlert("Nevalidan unos", "Ne možete rezervisati ili kupiti više od " + maxTicketsPerUser + " karata za ovaj sektor.");
                        return;
                    }

                    double ukupnaCijena = calculateTotalPrice(brojKarata);
                    rezervacijaService.rezervisiKartu(karta, brojKarata, ukupnaCijena, korisnik, mainScreenController);
                } else if ("Kupovina".equals(tip)) {
                    int maxTicketsPerUser = karta.getMaxBrojKartiPoKorisniku();

                    int reservedTickets = rezervacijaService.pronadjiBrojAktivnihRezervisanihKarata(karta, korisnik);
                    int purchasedTickets = kupovinaService.pronadjiBrojKupljenihKarata(karta, korisnik);
                    int totalTickets = reservedTickets + purchasedTickets;
            
                    if (totalTickets + brojKarata > maxTicketsPerUser) {
                        Obavjest.showAlert("Nevalidan unos", "Ne možete rezervisati ili kupiti više od " + maxTicketsPerUser + " karata za ovaj sektor.");
                        return;
                    }
                    
                    double ukupnaCijena = calculateTotalPrice(brojKarata);
                    kupovinaService.kupiKartu(null, karta, brojKarata, ukupnaCijena, korisnik, mainScreenController);
                }

                Stage stage = (Stage) reservationBuyBtn.getScene().getWindow();
                stage.close();
            } else {
                Obavjest.showAlert("Niste odabrali sektor", "Izaberite sektor i broj karti.");
            }
        } catch (NumberFormatException e) {
            Obavjest.showAlert("Greška", "Unesite validan broj karata.");
        } catch (Exception e) {
            e.printStackTrace();
            Obavjest.showAlert("Greška", "Došlo je do greške pri rezervaciji ili kupovini.");
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
                    int totalTickets = 0;
                    if (activeSectorButton != null) {
                        String buttonId = activeSectorButton.getId();
                        int kartaId = Integer.parseInt(buttonId.replace("btn", ""));
                        Karta karta = kartaService.pronadjiKartuPoID(kartaId);

                        int reservedTickets = rezervacijaService.pronadjiBrojAktivnihRezervisanihKarata(karta, korisnik);
                        int purchasedTickets = kupovinaService.pronadjiBrojKupljenihKarata(karta, korisnik);
                        totalTickets = reservedTickets + purchasedTickets;
                    }

                    String currentText = brojKarti.getText() + character;
                    int currentValue = Integer.parseInt(currentText);
                    int maxBrojKarti = getMaxBrojKartiPoSektoru();
                    if (currentValue + totalTickets > maxBrojKarti || currentValue < 1) {
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


    @FXML
    public void incBrojKarti() {
        int totalTickets = 0;
        if (activeSectorButton != null) {
            String buttonId = activeSectorButton.getId();
            int kartaId = Integer.parseInt(buttonId.replace("btn", ""));
            Karta karta = kartaService.pronadjiKartuPoID(kartaId);

            int reservedTickets = rezervacijaService.pronadjiBrojAktivnihRezervisanihKarata(karta, korisnik);
            int purchasedTickets = kupovinaService.pronadjiBrojKupljenihKarata(karta, korisnik);
            totalTickets = reservedTickets + purchasedTickets;
        }
        
        int currentValue = Integer.parseInt(brojKarti.getText());
        int maxBrojKarti = getMaxBrojKartiPoSektoru();
        if (currentValue + totalTickets < maxBrojKarti) {
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