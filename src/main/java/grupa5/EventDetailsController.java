package grupa5;

import grupa5.baza_podataka.Dogadjaj;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventDetailsController {
    
    @FXML
    private Label datumLabel;

    @FXML
    private ImageView dogadjajImg;

    @FXML
    private Label lokacijaLabel;

    @FXML
    private Label mjestoLabel;

    @FXML
    private Label nazivLabel;

    @FXML
    private Label opisDogadjajaLabel;

    @FXML
    public void initialize() {
        opisDogadjajaLabel.setWrapText(true);
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        // Postavite podatke o događaju u odgovarajuće elemente UI-a
        nazivLabel.setText(dogadjaj.getNaziv());
        opisDogadjajaLabel.setText(dogadjaj.getOpis());
        datumLabel.setText(dogadjaj.getDatum().toString());
        lokacijaLabel.setText(dogadjaj.getLokacija().getNaziv());
        mjestoLabel.setText(dogadjaj.getMjesto().getNaziv());

        if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
            Image image = new Image(dogadjaj.getPutanjaDoSlike());
            dogadjajImg.setImage(image);
        } else {
            // Postavite podrazumevanu sliku ako nema slike za događaj
            dogadjajImg.setImage(new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png")));
        }
    }
    
}
