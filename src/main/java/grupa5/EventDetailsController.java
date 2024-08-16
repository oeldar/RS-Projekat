package grupa5;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EventDetailsController {
    
    @FXML
    private Label eventDate, eventTitle, eventTime;

    @FXML
    private ImageView eventImageView;

    @FXML
    private TableView<Karta> sectorsTable;

    @FXML
    private TableColumn<Karta, String> sectorColumn;

    @FXML
    private TableColumn<Karta, Double> priceColumn;

    @FXML
    private Label locationLabel, placeLabel, eventDescriptionLabel;

    @FXML
    public void initialize() {
        eventDescriptionLabel.setWrapText(true);

        sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sektorNaziv"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        if (dogadjaj != null) {
            eventTitle.setText(dogadjaj.getNaziv());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formatiranText = dogadjaj.getDatum().format(formatter);
            eventDate.setText(formatiranText);
            eventTime.setText(dogadjaj.getVrijeme().toString() + "h");
            locationLabel.setText(dogadjaj.getLokacija().getNaziv());
            placeLabel.setText(dogadjaj.getMjesto().getNaziv());
            eventDescriptionLabel.setText(dogadjaj.getOpis());
            
            ObservableList<Karta> kartaData = FXCollections.observableArrayList(
                dogadjaj.getKarte().stream()
                        .filter(karta -> karta.getDogadjaj().equals(dogadjaj))
                        .collect(Collectors.toList())
            );
            sectorsTable.setItems(kartaData);

            if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
                InputStream imageStream = getClass().getResourceAsStream(dogadjaj.getPutanjaDoSlike());
                if (imageStream != null) {
                    Image eventImage = new Image(imageStream);
                    eventImageView.setImage(eventImage);
                } else {
                    // System.err.println("Slika nije pronađena: " + dogadjaj.getPutanjaDoSlike());
                    Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
                    eventImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
                eventImageView.setImage(defaultImage);
            }
        } else {
            eventTitle.setText("Naziv nije dostupan");
            eventDate.setText("Datum nije dostupan");
            locationLabel.setText("Lokacija nije dostupna");
            placeLabel.setText("Mjesto nije dostupno");
            eventDescriptionLabel.setText("Opis nije dostupan");
            Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
            eventImageView.setImage(defaultImage);  
        }
    }



    public void handleRezervacija(ActionEvent event) {
        try {
            // Učitavanje FXML fajla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reservation.fxml"));
            Parent root = loader.load();
            
            // Kreiranje novog prozora
            Stage stage = new Stage();
            stage.setTitle("Rezervacija");
            stage.setScene(new Scene(root));

            stage.setMinWidth(871);
            stage.setMaxWidth(880);
            stage.setMinHeight(568);
            
            // Prikaz novog prozora
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}