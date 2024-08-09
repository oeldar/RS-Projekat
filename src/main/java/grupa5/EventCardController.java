package grupa5;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;

import grupa5.DogadjajMoj;

public class EventCardController {
    DogadjajMoj dogadjajMojTrenutni;

    @FXML
    private Text nazivText;

    @FXML
    private Text datumText;

    @FXML
    private ImageView eventImageView;

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setDogadjajMoj(DogadjajMoj dogadjajMoj) {
        dogadjajMojTrenutni = dogadjajMoj;
        nazivText.setText(dogadjajMoj.getNaziv());
        datumText.setText(dogadjajMoj.getDatum().toString());
        Image eventImage = new Image(getClass().getResource(dogadjajMoj.getImagePath()).toString());
        eventImageView.setImage(eventImage);

    }

    public void eventClicked(MouseEvent event) throws IOException {
        // Ovdje ide kod koji želite da se izvrši
        System.out.println("AnchorPane clicked!");
        if (mainScreenController != null) {
            mainScreenController.loadEventView(dogadjajMojTrenutni); // ili drugi view koji želite
        }
    }
    
}