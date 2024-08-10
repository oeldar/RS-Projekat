package grupa5;

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

    public void setDogadjajMoj(DogadjajMoj dogadjajMoj) {
        dogadjajMojTrenutni = dogadjajMoj;
        nazivText.setText(dogadjajMoj.getNaziv());
        datumText.setText(dogadjajMoj.getDatum().toString());
        Image eventImage = new Image(getClass().getResource(dogadjajMoj.getImagePath()).toString());
        eventImageView.setImage(eventImage);
        eventImageView.setPreserveRatio(true);

        Rectangle2D viewportRect = new Rectangle2D(0, 0, 500, 300);
        eventImageView.setViewport(viewportRect);
    }

    public void eventClicked(MouseEvent event) throws IOException {
        // Ovdje ide kod koji želite da se izvrši
        System.out.println("AnchorPane clicked!");
        if (mainScreenController != null) {
            mainScreenController.loadEventView(dogadjajMojTrenutni); // ili drugi view koji želite
        }
    }
    
}