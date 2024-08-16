package grupa5;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Rezervacija;

import java.io.InputStream;

public class ReservedCardController {

    @FXML
    private Label descriptionLbl;

    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventLNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label ticketsNumberLbl;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    public void setReservationData(Rezervacija rezervacija) {
        if (rezervacija != null) {
            Dogadjaj dogadjaj = rezervacija.getDogadjaj();

            descriptionLbl.setText(dogadjaj.getOpis());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.valueOf(rezervacija.getUkupnaCijena()));
            ticketsNumberLbl.setText(String.valueOf(rezervacija.getBrojKarata()));

            loadEventImage(dogadjaj.getPutanjaDoSlike());
        }
    }

    private void loadEventImage(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));

        if (imagePath != null && !imagePath.isEmpty()) {
            try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                if (imageStream != null) {
                    Image eventImage = new Image(imageStream);
                    eventImg.setImage(eventImage);
                } else {
                    eventImg.setImage(defaultImage);
                }
            } catch (Exception e) {
                System.err.println("Error loading image from path: " + imagePath);
                e.printStackTrace();
                eventImg.setImage(defaultImage);
            }
        } else {
            eventImg.setImage(defaultImage);
        }
    }
}
