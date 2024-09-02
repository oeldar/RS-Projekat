package grupa5.support_classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageSelector {
    public static Image selectImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
                return new Image(inputStream);
            } catch (IOException e) {
                System.out.println("Error selecting image: " + e.getMessage());
            }
        }
        return null;
    }

    public static ImageView clipToCircle(ImageView imageView, double radius) {
        Circle clip = new Circle(radius);

        imageView.setClip(clip);

        imageView.setFitWidth(radius * 2);
        imageView.setFitHeight(radius * 2);

        clip.setCenterX(radius);
        clip.setCenterY(radius);

        return imageView;  
    }
}
