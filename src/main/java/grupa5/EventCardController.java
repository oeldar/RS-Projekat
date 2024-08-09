package grupa5;

import grupa5.baza_podataka.Dogadjaj;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class EventCardController {
    @FXML
    private ImageView dogadjajImg;

    @FXML
    private Text nazivText;

    @FXML
    private Text datumText;

    private MainScreenController mainScreenController;
    private Dogadjaj dogadjaj;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
        nazivText.setText(dogadjaj.getNaziv());
        datumText.setText(dogadjaj.getDatum().toString());

        if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
            Image image = new Image(dogadjaj.getPutanjaDoSlike());
            dogadjajImg.setImage(image);
        } else {
            // Postavite podrazumevanu sliku ako nema slike za događaj
            dogadjajImg.setImage(new Image(getClass().getResourceAsStream("assets/events_photos/default-event.png")));
        }
    }


    public void eventClicked(MouseEvent event) {
        // Ovdje ide kod koji želite da se izvrši
        System.out.println("AnchorPane clicked!" + dogadjaj.getNaziv());
        if (mainScreenController != null) {
            mainScreenController.loadDogadjajView(dogadjaj); // ili drugi view koji želite
        }
    }
    
}