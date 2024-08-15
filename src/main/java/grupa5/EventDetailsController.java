package grupa5;

import java.io.InputStream;
import java.util.stream.Collectors;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Sektor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

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

    private static final int ROW_HEIGHT = 37;

    @FXML
    public void initialize() {
        eventDescriptionLabel.setWrapText(true);

        sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sektorNaziv"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        if (dogadjaj != null) {
            eventTitle.setText(dogadjaj.getNaziv());
            eventDate.setText(dogadjaj.getDatum().toString());
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

            sectorsTable.setPrefHeight(kartaData.size() * ROW_HEIGHT);
            sectorsTable.setMaxHeight(kartaData.size() * ROW_HEIGHT);
            sectorsTable.setMaxWidth(300);

            if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
                InputStream imageStream = getClass().getResourceAsStream(dogadjaj.getPutanjaDoSlike());
                if (imageStream != null) {
                    Image eventImage = new Image(imageStream);
                    eventImageView.setImage(eventImage);
                } else {
                    System.err.println("Slika nije pronađena: " + dogadjaj.getPutanjaDoSlike());
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
}