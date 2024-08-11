package grupa5;

import grupa5.baza_podataka.Dogadjaj;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML
    private Label eventTitle, eventDate;

    @FXML
    private ImageView eventImageView;

    public void setDogadjaj(DogadjajMoj dogadjajMoj) {
        eventTitle.setText(dogadjajMoj.getNaziv());
        eventDate.setText(dogadjajMoj.getDatum().toString());
        Image eventImage = new Image(getClass().getResource(dogadjajMoj.getImagePath()).toString());
        eventImageView.setImage(eventImage);
    }
    


}
