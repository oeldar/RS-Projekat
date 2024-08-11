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
    Dogadjaj dogadjajMojTrenutni;

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

    //public void setDogadjajMoj(DogadjajMoj dogadjajMoj) {
    //    dogadjajMojTrenutni = dogadjajMoj;
    //    nazivText.setText(dogadjajMoj.getNaziv());
    //    datumText.setText(dogadjajMoj.getDatum().toString());
    //    Image eventImage = new Image(getClass().getResource(dogadjajMoj.getImagePath()).toString());
    //    eventImageView.setImage(eventImage);
    //    eventImageView.setPreserveRatio(true);

    //    Rectangle2D viewportRect = new Rectangle2D(0, 0, 500, 300);
    //    eventImageView.setViewport(viewportRect);
    // }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    
        if (dogadjaj != null) {
            // Postavljanje teksta za naziv događaja
            nazivText.setText(dogadjaj.getNaziv());
    
            // Postavljanje teksta za datum događaja (pretvaranje datuma u String)
            datumText.setText(dogadjaj.getDatum().toString());
    
            // Postavljanje slike događaja, ako postoji
            if (dogadjaj.getPutanjaDoSlike() != null && !dogadjaj.getPutanjaDoSlike().isEmpty()) {
                URL imageUrl = getClass().getResource(dogadjaj.getPutanjaDoSlike());
                if (imageUrl != null) {
                    Image eventImage = new Image(imageUrl.toString());
                    eventImageView.setImage(eventImage);
    
                    // Opcionalno: Ako želite da isecete sliku
                    Rectangle2D viewportRect = new Rectangle2D(0, 0, 500, 300); // Primer dimenzija
                    eventImageView.setViewport(viewportRect);
                } else {
                    System.err.println("Slika nije pronađena: " + dogadjaj.getPutanjaDoSlike());
                    eventImageView.setImage(null);
                }
            } else {
                // Ako slika ne postoji, možete postaviti podrazumevanu sliku ili ostaviti prazno
                eventImageView.setImage(null);
            }
        } else {
            // U slučaju da je dogadjaj null, možete postaviti podrazumevane vrednosti ili ostaviti prazno
            nazivText.setText("Naziv nije dostupan");
            datumText.setText("Datum nije dostupan");
            eventImageView.setImage(null);
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