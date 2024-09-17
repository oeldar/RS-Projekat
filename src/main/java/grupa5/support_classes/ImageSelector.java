package grupa5.support_classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.itextpdf.kernel.geom.Rectangle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageSelector {
    private static String imagePath;
    private static String eventImagePath;
    public static File selectedFile;

    public static Image selectImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
                return new Image(inputStream);
            } catch (IOException e) {
                System.out.println("Error selecting image: " + e.getMessage());
            }
        } else {
            imagePath = null;
        }
        return null;
    }

    public static Image selectEventImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            eventImagePath = selectedFile.getAbsolutePath();
            try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
                return new Image(inputStream);
            } catch (IOException e) {
                System.out.println("Error selecting image: " + e.getMessage());
            }
        } else {
            eventImagePath = null;
        }
        return null;
    }

    public static File selectEventImageFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters()
            .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    
        return fileChooser.showOpenDialog(stage);
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

    public static void copyImageTo(String destinationPath) {
        String userDirictory = System.getProperty("user.dir") + "/src/main/resources/";

        Path source = Paths.get(imagePath);
        Path destination = Paths.get(userDirictory, destinationPath);

        try {
            if (Files.exists(destination)) {
                Files.delete(destination);
            }

            Files.copy(source, destination);
        } catch (IOException e) {
            System.out.println("Error copying image: " + e.getMessage());
        }
    }

    public static void copyEventImageTo(String destinationPath) {
        String userDirictory = System.getProperty("user.dir") + "/src/main/resources/";

        Path source = Paths.get(eventImagePath);
        Path destination = Paths.get(userDirictory, destinationPath);

        try {
            if (Files.exists(destination)) {
                Files.delete(destination);
            }

            Files.copy(source, destination);
        } catch (IOException e) {
            System.out.println("Error copying image: " + e.getMessage());
        }
    }

    public String moveImageFromProposal(String sourcePath, Integer dogadjajId) throws IOException {
        Path sourceFilePath = Paths.get("src/main/resources/grupa5/" + sourcePath);
        if (Files.exists(sourceFilePath)) {
            Path destinationDir = Paths.get("src/main/resources/grupa5/assets/events_photos/");
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            // Izdvajanje ekstenzije fajla
            String fileExtension = "";
            int i = sourcePath.lastIndexOf('.');
            if (i >= 0) {
                fileExtension = sourcePath.substring(i);
            }

            // Novi naziv fajla zasnovan na ID-u događaja
            String newFileName = dogadjajId + fileExtension;
            Path destinationPath = destinationDir.resolve(newFileName);

            // Premestanje fajla sa zamjenom ako već postoji
            Files.move(sourceFilePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Kreiranje nove putanje koju treba vratiti
            return "assets/events_photos/" + newFileName;
        } else {
            throw new FileNotFoundException("Source image file does not exist: " + sourcePath);
        }
    }

}
