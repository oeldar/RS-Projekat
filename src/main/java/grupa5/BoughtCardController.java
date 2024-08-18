package grupa5;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import grupa5.baza_podataka.*;
import jakarta.persistence.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class BoughtCardController {

    @FXML
    private Label locationLbl;

    @FXML
    private Label nameLbl;

    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventLNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label sectorLbl;

    @FXML
    private Label ticketsNumberLbl;

    @FXML
    private Button refundirajBtn;

    @FXML
    private Button preuzmiBtn;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    private MainScreenController mainScreenController;
    private BoughtCardsController boughtCardsController;
    private PopustService popustService;
    private NovcanikService novcanikService;
    private KupovinaService kupovinaService;
    private KartaService kartaService;
    private Kupovina kupovina;


    @FXML
    public void initialize() {
        popustService = new PopustService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        kartaService = new KartaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        novcanikService = new NovcanikService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
        kupovinaService = new KupovinaService(Persistence.createEntityManagerFactory("HypersistenceOptimizer"));
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setBoughtCardsController(BoughtCardsController boughtCardsController) {
        this.boughtCardsController = boughtCardsController;
    }

    public void setPurchaseData(Kupovina kupovina) {
        if (kupovina != null) {
            this.kupovina = kupovina;
            Dogadjaj dogadjaj = kupovina.getDogadjaj();
            Korisnik korisnik = kupovina.getKorisnik();

            nameLbl.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", kupovina.getKonacnaCijena()));
            ticketsNumberLbl.setText(String.valueOf(kupovina.getBrojKarata()));
            sectorLbl.setText(kupovina.getKarta().getSektorNaziv());

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
    

    public void handlePreuzmi() {
        // Kreirajte FileChooser instancu za izbor foldera
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Izaberite Folder");
    
        // Prikazivanje dijaloga i dobijanje odabranog foldera
        File selectedDirectory = directoryChooser.showDialog(null);
    
        if (selectedDirectory != null) {
            // Kreirajte putanju za PDF u odabranom folderu
            File pdfFile = new File(selectedDirectory, kupovina.getKupovinaID() + "_ticket.pdf");
            
            // Generišite PDF koristeći PdfGenerator
            try {
                PdfGenerator.generatePdf(pdfFile, kupovina);
                // PdfGenerator.generateSimplePdf(pdfFile);
                // Prikazivanje obaveštenja korisniku
                String message = "PDF karta je uspešno preuzeta i smeštena u: " + pdfFile.getAbsolutePath();
                showAlert("PDF Generisan", message);
            } catch (Exception e) {
                showAlert("Greška", "Došlo je do greške pri generisanju PDF-a: " + e.getMessage());
            }
        } else {
            // Korisnik je otkazao izbor foldera
            showAlert("Greška", "Ne možete sačuvati PDF jer nije izabran folder.");
        }
    }

    
    @FXML
    void handleRefundiraj(ActionEvent event) {
        // Implementing Refund Logic in Your Application

        // Refund Eligibility Check:
        // Before processing a refund, the system should check whether the purchase meets the criteria for a refund.
        // For example, ensure the refund request is within the allowed refund period, or check if the event was canceled.

        // Partial vs. Full Refund:
        // Determine whether the refund should be partial or full.
        // For instance, service fees might not be refundable, resulting in a partial refund, while a full refund would include the entire purchase amount.

        // Processing the Refund:
        // Update the user's wallet balance to reflect the refunded amount.
        // Additionally, update the database to mark the purchase as refunded, and reverse any associated operations, such as increasing the number of available tickets for the event.

        // Notification:
        // Notify the user about the refund via email or within the app, confirming the refund amount and any other relevant details.
        // This ensures transparency and helps maintain trust between the user and the system.

        // By clearly defining and implementing these scenarios in your system, you can manage ticket refunds in a way that is fair to both users and event organizers.
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}