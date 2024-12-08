package grupa5;

import java.io.InputStream;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajPrijedlog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class AllEventDetailsController {

    @FXML
    private ImageView dogadjajImg;

    @FXML
    private ImageView dogadjajImg1;

    @FXML
    private Label korisnikLbl;

    @FXML
    private Label korisnikLbl1;

    @FXML
    private Label krajLbl;

    @FXML
    private Label krajLbl1;

    @FXML
    private Label lokacijaLbl;

    @FXML
    private Label lokacijaLbl1;

    @FXML
    private Label mjestoLbl;

    @FXML
    private Label mjestoLbl1;

    @FXML
    private Label nazivLbl;

    @FXML
    private Label nazivLbl1;

    @FXML
    private VBox newValuesVBox;

    @FXML
    private Label noveVrijednostiLbl;

    @FXML
    private VBox oldValuesVBox;

    @FXML
    private Label opisLbl;

    @FXML
    private Label opisLbl1;

    @FXML
    private Label pocetakLbl;

    @FXML
    private Label pocetakLbl1;

    @FXML
    private Label podvrstaLbl;

    @FXML
    private Label podvrstaLbl1;

    @FXML
    private Label stareVrijednostiLbl;

    @FXML
    private Label vrstaLbl;

    @FXML
    private Label vrstaLbl1;

    public void setDogadjaj(Dogadjaj dogadjaj) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");
    
        // Prvo sakrijete VBox za nove vrijednosti
        newValuesVBox.setVisible(false);
        newValuesVBox.setManaged(false);
    
        // Postavite stare vrijednosti iz dogadjaja
        korisnikLbl.setText(dogadjaj.getKorisnik().getKorisnickoIme());
        nazivLbl.setText(dogadjaj.getNaziv());
        opisLbl.setText(dogadjaj.getOpis());
        lokacijaLbl.setText(dogadjaj.getLokacija().getNaziv() + ", " + dogadjaj.getLokacija().getAdresa());
        mjestoLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getMjesto().getPostanskiBroj());
        pocetakLbl.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
        krajLbl.setText(dogadjaj.getKrajDogadjaja().format(formatter));
        vrstaLbl.setText(dogadjaj.getVrstaDogadjaja());
        podvrstaLbl.setText(dogadjaj.getPodvrstaDogadjaja());
    
        // Postavljanje slike starog događaja
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
    
        // Prikaz prijedloga ako postoji
        DogadjajPrijedlog dogadjajPrijedlog = dogadjaj.getPrijedlogDogadjaja();
        if (dogadjajPrijedlog != null) {
            newValuesVBox.setVisible(true);
            newValuesVBox.setManaged(true);

            korisnikLbl1.setText(korisnikLbl.getText());
            
            // Provera i naglašavanje novih vrijednosti
            if (dogadjajPrijedlog.getNaziv() != null && !dogadjajPrijedlog.getNaziv().equals(dogadjaj.getNaziv())) {
                nazivLbl1.setText(dogadjajPrijedlog.getNaziv());
                nazivLbl1.getStyleClass().add("changed"); // Dodaj CSS klasu za naglašavanje
            } else {
                nazivLbl1.setText(dogadjaj.getNaziv());
            }

            if (dogadjajPrijedlog.getOpis() != null && !dogadjajPrijedlog.getOpis().equals(dogadjaj.getOpis())) {
                opisLbl1.setText(dogadjajPrijedlog.getOpis());
                opisLbl1.getStyleClass().add("changed");
            } else {
                opisLbl1.setText(dogadjaj.getOpis());
            }

            if (dogadjajPrijedlog.getLokacija() != null && !dogadjajPrijedlog.getLokacija().getNaziv().equals(dogadjaj.getLokacija().getNaziv())) {
                lokacijaLbl1.setText(dogadjajPrijedlog.getLokacija().getNaziv() + ", " 
                                    + dogadjajPrijedlog.getLokacija().getAdresa());
                lokacijaLbl1.getStyleClass().add("changed");
            } else {
                lokacijaLbl1.setText(dogadjaj.getLokacija().getNaziv() + ", " + dogadjaj.getLokacija().getAdresa());
            }

            if (dogadjajPrijedlog.getMjesto() != null && !dogadjajPrijedlog.getMjesto().getNaziv().equals(dogadjaj.getMjesto().getNaziv())) {
                mjestoLbl1.setText(dogadjajPrijedlog.getMjesto().getNaziv() + ", " + dogadjajPrijedlog.getMjesto().getPostanskiBroj());
                mjestoLbl1.getStyleClass().add("changed");
            } else {
                mjestoLbl1.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getMjesto().getPostanskiBroj());
            }

            if (dogadjajPrijedlog.getPocetakDogadjaja() != null && !dogadjajPrijedlog.getPocetakDogadjaja().equals(dogadjaj.getPocetakDogadjaja())) {
                pocetakLbl1.setText(dogadjajPrijedlog.getPocetakDogadjaja().format(formatter));
                pocetakLbl1.getStyleClass().add("changed");
            } else {
                pocetakLbl1.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
            }

            if (dogadjajPrijedlog.getKrajDogadjaja() != null && !dogadjajPrijedlog.getKrajDogadjaja().equals(dogadjaj.getKrajDogadjaja())) {
                krajLbl1.setText(dogadjajPrijedlog.getKrajDogadjaja().format(formatter));
                krajLbl1.getStyleClass().add("changed");
            } else {
                krajLbl1.setText(dogadjaj.getKrajDogadjaja().format(formatter));
            }

            if (dogadjajPrijedlog.getVrstaDogadjaja() != null && !dogadjajPrijedlog.getVrstaDogadjaja().equals(dogadjaj.getVrstaDogadjaja())) {
                vrstaLbl1.setText(dogadjajPrijedlog.getVrstaDogadjaja());
                vrstaLbl1.getStyleClass().add("changed");
            } else {
                vrstaLbl1.setText(dogadjaj.getVrstaDogadjaja());
            }

            if (dogadjajPrijedlog.getPodvrstaDogadjaja() != null && !dogadjajPrijedlog.getPodvrstaDogadjaja().equals(dogadjaj.getPodvrstaDogadjaja())) {
                podvrstaLbl1.setText(dogadjajPrijedlog.getPodvrstaDogadjaja());
                podvrstaLbl1.getStyleClass().add("changed");
            } else {
                podvrstaLbl1.setText(dogadjaj.getPodvrstaDogadjaja());
            }
            // Prikaz nove slike ili stare ako prijedlog nema
            if (dogadjajPrijedlog.getPutanjaDoSlike() != null && !dogadjajPrijedlog.getPutanjaDoSlike().isEmpty()) {
                InputStream imageStream = getClass().getResourceAsStream(dogadjajPrijedlog.getPutanjaDoSlike());
                if (imageStream != null) {
                    Image eventImage = new Image(imageStream);
                    dogadjajImg1.setImage(eventImage);
                } else {
                    Image defaultImage = new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png"));
                    dogadjajImg1.setImage(defaultImage);
                }
            } else {
                dogadjajImg1.setImage(dogadjajImg.getImage());
            }
        }
    }    
}