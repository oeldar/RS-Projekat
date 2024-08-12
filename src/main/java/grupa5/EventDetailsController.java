package grupa5;

import java.io.InputStream;

import grupa5.baza_podataka.Dogadjaj;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventDetailsController {
    
    @FXML
    private Label eventDate, eventTitle;

    @FXML
    private ImageView eventImageView;

    @FXML
    private Label locationLabel, placeLabel, eventDescriptionLabel;

    @FXML
    public void initialize() {
        eventDescriptionLabel.setWrapText(true);
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        if (dogadjaj != null) {
            eventTitle.setText(dogadjaj.getNaziv());
            eventDate.setText(dogadjaj.getDatum().toString());
            locationLabel.setText(dogadjaj.getLokacija().getNaziv());
            placeLabel.setText(dogadjaj.getMjesto().getNaziv());
            eventDescriptionLabel.setText(dogadjaj.getOpis());

            // Postavljanje slike događaja, ako postoji
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
            // U slučaju da je dogadjaj null, možete postaviti podrazumevane vrednosti ili ostaviti prazno
            eventTitle.setText("Naziv nije dostupan");
            eventDate.setText("Datum nije dostupan");
            locationLabel.setText("Lokacija nije dostupna");
            placeLabel.setText("Mesto nije dostupno");
            eventDescriptionLabel.setText("Opis nije dostupan");
            Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
            eventImageView.setImage(defaultImage);  
        }
    }
}