package grupa5;

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
            // Postavljanje teksta za naziv događaja
            eventTitle.setText(dogadjaj.getNaziv());
    
            // Postavljanje teksta za datum događaja (pretvaranje datuma u String)
            eventDate.setText(dogadjaj.getDatum().toString());
    
            // Postavljanje lokacije
            locationLabel.setText(dogadjaj.getLokacija().getNaziv());
    
            // Postavljanje mesta
            placeLabel.setText(dogadjaj.getMjesto().getNaziv());
    
            // Postavljanje opisa događaja
            eventDescriptionLabel.setText(dogadjaj.getOpis());
    
            // Postavljanje slike događaja, ako postoji
            if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
                Image eventImage = new Image(getClass().getResource(dogadjaj.getPutanjaDoSlike()).toString());
                eventImageView.setImage(eventImage);
            } else {
                // Ako slika ne postoji, možete postaviti podrazumevanu sliku ili ostaviti prazno
                eventImageView.setImage(null);
            }
        } else {
            // U slučaju da je dogadjaj null, možete postaviti podrazumevane vrednosti ili ostaviti prazno
            eventTitle.setText("Naziv nije dostupan");
            eventDate.setText("Datum nije dostupan");
            locationLabel.setText("Lokacija nije dostupna");
            placeLabel.setText("Mesto nije dostupno");
            eventDescriptionLabel.setText("Opis nije dostupan");
            eventImageView.setImage(null);
        }
    }
}