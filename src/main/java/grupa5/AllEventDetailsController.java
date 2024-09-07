package grupa5;

import java.io.InputStream;

import grupa5.baza_podataka.Dogadjaj;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.format.DateTimeFormatter;

public class AllEventDetailsController {

    @FXML
    private ImageView dogadjajImg;

    @FXML
    private Label korisnikLbl;

    @FXML
    private Label krajLbl;

    @FXML
    private Label lokacijaLbl;

    @FXML
    private Label mjestoLbl;

    @FXML
    private Label nazivLbl;

    @FXML
    private Label opisLbl;

    @FXML
    private Label pocetakLbl;

    @FXML
    private Label podvrstaLbl;

    @FXML
    private Label vrstaLbl;

    public void setDogadjaj(Dogadjaj dogadjaj) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");

        korisnikLbl.setText(dogadjaj.getKorisnik().getKorisnickoIme());
        nazivLbl.setText(dogadjaj.getNaziv());
        opisLbl.setText(dogadjaj.getOpis());
        lokacijaLbl.setText(dogadjaj.getLokacija().getNaziv() + ", " + dogadjaj.getLokacija().getAdresa());
        mjestoLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getMjesto().getPostanskiBroj());
        pocetakLbl.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
        krajLbl.setText(dogadjaj.getKrajDogadjaja().format(formatter));
        vrstaLbl.setText(dogadjaj.getVrstaDogadjaja());
        podvrstaLbl.setText(dogadjaj.getPodvrstaDogadjaja());

        if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
            InputStream imageStream = getClass().getResourceAsStream(dogadjaj.getPutanjaDoSlike());
            if (imageStream != null) {
                Image eventImage = new Image(imageStream);
                dogadjajImg.setImage(eventImage);
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png"));
                dogadjajImg.setImage(defaultImage);
            }
        } else {
            Image defaultImage = new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png"));
            dogadjajImg.setImage(defaultImage);
        }
    }

}