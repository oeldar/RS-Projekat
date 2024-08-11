package grupa5;

import grupa5.baza_podataka.Dogadjaj;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class EventCardController {
    @FXML
    private Text nazivText;

    @FXML
    private Text datumText;

    @FXML
    private ImageView eventImageView;

    private MainScreenController mainScreenController;
    private Dogadjaj dogadjaj;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    @FXML
    public void initialize() {
        Image originalImage = eventImageView.getImage();

        System.out.println(originalImage.getHeight());
        // int cropX = 50;
        // int cropY = 50;
        // int cropWidth = 10;
        // int cropHeight = 10;

        // PixelReader pixelReader = originalImage.getPixelReader();
        // WritableImage croppedImage = new WritableImage(pixelReader, cropX, cropY, cropWidth, cropHeight);

        // eventImageView.setImage(croppedImage);
    }

    @FXML
    private Rectangle rectangleClip;

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    
        if (dogadjaj != null) {
            nazivText.setText(dogadjaj.getNaziv());
            datumText.setText(dogadjaj.getDatum().toString());
    
            String imagePath = dogadjaj.getPutanjaDoSlike();
            if (imagePath != null && !imagePath.isEmpty()) {
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    Image eventImage = new Image(imageUrl.toString());
                    eventImageView.setImage(eventImage);
                    
                    // Optional: Set viewport if needed
                    Rectangle2D viewportRect = new Rectangle2D(0, 0, 500, 300); // Example dimensions
                    eventImageView.setViewport(viewportRect);
                } else {
                    // Log the error if the image URL is null
                    System.err.println("Slika nije pronadena: " + imagePath);
                    Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
                    eventImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
                eventImageView.setImage(defaultImage);
            }
        } else {
            nazivText.setText("Naziv nije dostupan");
            datumText.setText("Datum nije dostupan");
            Image defaultImage = new Image(getClass().getResourceAsStream("/grupa5/assets/events_photos/default-event.png"));
            eventImageView.setImage(defaultImage);
        }
    }    

    public void eventClicked(MouseEvent event) throws IOException {
        if (dogadjaj != null) {
            System.out.println("Kliknuli ste na događaj: " + dogadjaj.getNaziv());
            if (mainScreenController != null) {
                mainScreenController.loadEventView(dogadjaj);
            } else {
                System.err.println("MainScreenController nije inicijalizovan.");
            }
        } else {
            System.err.println("Dogadjaj nije dostupan.");
        }
    }
    
}