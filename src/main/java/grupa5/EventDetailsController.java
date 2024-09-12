package grupa5;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Korisnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// @SuppressWarnings({"exports", "unused"})
public class EventDetailsController {
    
    @FXML
    private Label eventDate, eventTitle, eventTime;

    @FXML
    private ImageView eventImageView;

    @FXML
    private ImageView locationImageView;

    @FXML
    private Label locationLabel, placeLabel, eventDescriptionLabel;

    private Dogadjaj dogadjaj;
    private Korisnik korisnik;
    private MainScreenController parentController;

    @FXML
    private AnchorPane opcijePane; // ovdje su dugmad Rezervisi kartu i Kupi kartu

    @FXML
    private Label porukaZaOrgAdmin;

    @FXML
    private VBox sektoriCijeneVBox;

    @FXML
    public void initialize() {
        eventDescriptionLabel.setWrapText(true);
       
    }

    private void loadSectorsAndPrices() {
        if (dogadjaj != null) {
            List<Karta> karte = dogadjaj.getKarte();
            sektoriCijeneVBox.getChildren().clear();

            for (Karta karta : karte) {

                // Kreiraj HBox za naziv i cijenu sektora
                HBox sectorHBox = new HBox(10);
                Label sectorNameLabel = new Label(karta.getSektor().getNaziv() + ":");
               // sectorNameLabel.getStyleClass().add("opis");
              //  sectorNameLabel.setStyle("-fx-font-weight: bold;");
                Label sectorPriceLabel = new Label(String.format("%.2f KM", karta.getCijena()));
                sectorPriceLabel.getStyleClass().add("opis");
                sectorHBox.getChildren().addAll(sectorNameLabel, sectorPriceLabel);
                sektoriCijeneVBox.getChildren().add(sectorHBox);
            }
        }
    }

    // Ovdje usput i onemogucim organizatoru i administratoru da rezervisu i kupe kartu.
    public void setParentController(MainScreenController parentController) {
        System.out.println("--------------------------------- Postavljam ga");
        this.parentController = parentController;
        boolean isOrgOrAdmin = Korisnik.TipKorisnika.ORGANIZATOR.equals(this.parentController.tipKorisnika) || Korisnik.TipKorisnika.ADMINISTRATOR.equals(this.parentController.tipKorisnika);
        if (isOrgOrAdmin) {
            opcijePane.setVisible(false);
            porukaZaOrgAdmin.setVisible(true);
        }
        
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
        if (dogadjaj != null) {
            eventTitle.setText(dogadjaj.getNaziv());
            // Formatiranje datuma
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formatiranDatum = dogadjaj.getPocetakDogadjaja().format(dateFormatter);
            eventDate.setText(formatiranDatum);

            // Formatiranje vremena
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formatiranoVrijeme = dogadjaj.getPocetakDogadjaja().format(timeFormatter);
            eventTime.setText(formatiranoVrijeme + "h");

            locationLabel.setText(dogadjaj.getLokacija().getNaziv());
            placeLabel.setText(dogadjaj.getMjesto().getNaziv());
            eventDescriptionLabel.setText(dogadjaj.getOpis());

            if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
                InputStream imageStream = getClass().getResourceAsStream(dogadjaj.getPutanjaDoSlike());
                if (imageStream != null) {
                    Image eventImage = new Image(imageStream);
                    eventImageView.setImage(eventImage);
                } else {
                    // System.err.println("Slika nije pronađena: " + dogadjaj.getPutanjaDoSlike());
                    Image defaultImage = new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png"));
                    eventImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png"));
                eventImageView.setImage(defaultImage);
            }

            if (dogadjaj.getLokacija().getPutanjaDoSlike() != null && !dogadjaj.getLokacija().getPutanjaDoSlike().isEmpty()) {
                InputStream imageStream = getClass().getResourceAsStream(dogadjaj.getLokacija().getPutanjaDoSlike());
                if (imageStream != null) {
                    Image locationImage = new Image(imageStream);
                    locationImageView.setImage(locationImage);
                } else {
                    Image defaultImage = new Image(getClass().getResourceAsStream("assets/locations_photos/default-location.png"));
                    locationImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("assets/locations_photos/default-location.png"));
                locationImageView.setImage(defaultImage);
            }

 // postavljam prikaz za sektore i njihove cijene:
            List<Karta> karte = dogadjaj.getKarte();
            sektoriCijeneVBox.getChildren().clear();

            for (Karta karta : karte) {

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

    private void showWindow(String title) {
        try {
            // Učitavanje FXML fajla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reservation.fxml"));
            Parent root = loader.load();
    
            ReservationBuyController reservationBuyController = loader.getController();
            reservationBuyController.setTip(title);
            reservationBuyController.setEvent(dogadjaj);
            reservationBuyController.setLoggedInUser(parentController.korisnik);
            reservationBuyController.setMainScreenController(parentController);
    
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
    

    public void handleRezervacija(ActionEvent event) {
       showWindow("Rezervacija");
    }

    @FXML
    void handleKupovina(ActionEvent event) {
        showWindow("Kupovina");
    }

}