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
        // Proverava i postavlja naziv događaja
        if (dogadjaj.getNaziv() != null && !dogadjaj.getNaziv().isEmpty()) {
            nazivLabel.setText(dogadjaj.getNaziv());
        } else {
            nazivLabel.setText("Nepoznati događaj");
        }

        // Proverava i postavlja opis događaja
        if (dogadjaj.getOpis() != null && !dogadjaj.getOpis().isEmpty()) {
            opisDogadjajaLabel.setText(dogadjaj.getOpis());
        } else {
            opisDogadjajaLabel.setText("Opis nije dostupan.");
        }

        // Postavljanje ostalih podataka
        datumLabel.setText(dogadjaj.getDatum().toString());
        lokacijaLabel.setText(dogadjaj.getLokacija().getNaziv());
        mjestoLabel.setText(dogadjaj.getMjesto().getNaziv());

        // Postavljanje slike događaja
        if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
            try {
                Image image = new Image(dogadjaj.getPutanjaDoSlike());
                dogadjajImg.setImage(image);
            } catch (Exception e) {
                System.err.println("Greška pri učitavanju slike: " + e.getMessage());
                postaviPodrazumevanuSliku();
            }
        } else {
            postaviPodrazumevanuSliku();
        }
    }

    private void postaviPodrazumevanuSliku() {
        dogadjajImg.setImage(new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png")));
    }
}
