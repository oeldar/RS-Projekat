package grupa5;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventDetailsController {
    @FXML
    private Label opisDogadjajaLbl;

    @FXML
    public void initialize() {
        opisDogadjajaLbl.setWrapText(true);
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
