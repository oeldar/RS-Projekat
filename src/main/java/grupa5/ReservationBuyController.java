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
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
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
    private Button reservationBtn;

    private KartaService kartaService;
    private RezervacijaService rezervacijaService;
    private Korisnik korisnik;
    private Dogadjaj dogadjaj;
    private Button activeSectorButton;
    private EventDetailsController eventDetailsController;

    @FXML
    public void initialize() {
        brojKarti.setText("1");
        configureBrojKartiTextField();
        cijena.setText("0.00");
        kartaService = new KartaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        rezervacijaService = new RezervacijaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        
        brojKarti.textProperty().addListener((observable, oldValue, newValue) -> updatePriceAndTotal(newValue));
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
                sectorButton.setOnAction(e -> handleSectorButtonClick(sectorButton));
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

                if (brojKarata < 1) brojKarata = 1;
                if (brojKarata > karta.getMaxBrojKartiPoKorisniku()) brojKarata = karta.getMaxBrojKartiPoKorisniku();
                
                double ukupnaCijena = karta.getCijena() * brojKarata;
                cijena.setText(String.format("%.2f KM", ukupnaCijena));
            }
        }
    }
    

    @FXML
    void handleReservationBtnAction(ActionEvent event) {
        if (korisnik == null) {
            showAlert("Niste prijavljeni", "Morate se prijaviti ili registrovati da biste napravili rezervaciju.");
            return;
        }

        try {
            int brojKarata = Integer.parseInt(brojKarti.getText());
            if (brojKarata < 1) {
                showAlert("Nevalidan unos", "Broj karata mora biti između 1 i 20.");
                return;
            }

            int maxBrojKarti = getMaxBrojKartiPoSektoru();
            if (brojKarata > maxBrojKarti) {
                showAlert("Nevalidan unos", "Broj karata ne može biti veći od " + maxBrojKarti + ".");
                return;
            }

            double ukupnaCijena = calculateTotalPrice(brojKarata);
            rezervacijaService.kreirajRezervaciju(dogadjaj, korisnik, LocalDateTime.now(), brojKarata, ukupnaCijena);
            String id = activeSectorButton.getId();
            int kartaId = Integer.parseInt(id.replace("btn", ""));
            Karta karta = kartaService.pronadjiKartuPoID(kartaId);
            karta.setBrojRezervisanih(karta.getBrojRezervisanih() + 1);
            showAlert("Rezervacija uspešna", "Vaša rezervacija je uspešno sačuvana.");
        } catch (NumberFormatException e) {
            showAlert("Greška", "Unesite validan broj karata.");
        } catch (Exception e) {
            showAlert("Greška", "Došlo je do greške pri rezervaciji.");
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

    int getMaxBrojKartiPoSektoru(){
        if (activeSectorButton == null) {
            return 1;
        }

        String id = activeSectorButton.getId();
        int kartaId = Integer.parseInt(id.replace("btn", ""));
        Karta karta = kartaService.pronadjiKartuPoID(kartaId);

        return karta != null ? karta.getMaxBrojKartiPoKorisniku() : 1;
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
}