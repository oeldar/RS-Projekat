package grupa5;

import java.io.File;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.services.KupovinaService;
import grupa5.support_classes.Obavjest;
import grupa5.support_classes.PdfGenerator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

// @SuppressWarnings({"exports", "unused"})
public class BoughtCardController {

    @FXML
    private Label dateTimeLbl;

    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventLNameLbl;

    @FXML
    private Label locationLbl;

    @FXML
    private Button preuzmiBtn;

    @FXML
    private Label priceLbl;

    @FXML
    private Label sectorLbl;

    @FXML
    private Label ticketsNumberLbl;

    @FXML
    private Button zamijeniBtn;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    private MainScreenController mainScreenController;
    private BoughtCardsController boughtCardsController;
    private EntityManagerFactory emf;
    private KupovinaService kupovinaService;
    private Kupovina kupovina;


    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");

        kupovinaService = new KupovinaService(emf);
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

            // Set data labels
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");
            dateTimeLbl.setText(dogadjaj.getPocetakDogadjaja().format(formatter));
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", kupovina.getUkupnaCijena()) + " KM");
            ticketsNumberLbl.setText(String.valueOf(kupovina.getBrojKarata()));
            sectorLbl.setText(kupovina.getKarta().getSektorNaziv());

            if (kupovina.getStatus().equals(Kupovina.Status.NEAKTIVNA) || kupovina.getDatumDogadjajaPromjenjen()) {
                zamijeniBtn.setVisible(true);
                preuzmiBtn.setText("Otkaži");
            }

            // Load event image lazily
            loadEventImageLazy(dogadjaj.getPutanjaDoSlike());
        }
    }

    private void loadEventImageLazy(String imagePath) {
        Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));
        eventImg.setImage(defaultImage); // Set default image first

        // Load the event image lazily in a background thread
        if (imagePath != null && !imagePath.isEmpty()) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                        if (imageStream != null) {
                            return new Image(imageStream);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading image from path: " + imagePath);
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                Image eventImage = loadImageTask.getValue();
                if (eventImage != null) {
                    eventImg.setImage(eventImage);
                }
            });

            new Thread(loadImageTask).start();
        }
    }

    @FXML
    public void handlePreuzmi(ActionEvent event) {
        if (preuzmiBtn.getText().equals("Otkaži")) {
            kupovinaService.refundirajKartu(kupovina, mainScreenController);
            kupovinaService.otkaziKupovinu(kupovina);
            boughtCardsController.refreshKupovine();
            return;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Izaberite Folder");

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            File pdfFile = new File(selectedDirectory, kupovina.getKupovinaID() + "_karta.pdf");

            try {
                PdfGenerator.generatePdf(pdfFile, kupovina);
                String message = "PDF karta je uspešno preuzeta i smeštena u: " + pdfFile.getAbsolutePath();
                Obavjest.showAlert(Alert.AlertType.INFORMATION, "PDF Generisan", "Uspešno generisanje PDF-a", message);
            } catch (Exception e) {
                Obavjest.showAlert(Alert.AlertType.ERROR, "Greška", "Greška pri generisanju PDF-a", "Došlo je do greške pri generisanju PDF-a: " + e.getMessage());
            }
        }

    }
    
    @FXML
    void handleZamijeni(ActionEvent event) {
        if (kupovina.getDatumDogadjajaPromjenjen()) {
            kupovina.setDatumDogadjajaPromjenjen(false);
            kupovinaService.azurirajKupovinu(kupovina);
            return;
        }
        boolean zamjenaUspjesna = showWindow("Zamjena kupovine"); // Wait for the result of the exchange

        if (zamjenaUspjesna) {
            kupovinaService.refundirajKartu(kupovina, mainScreenController);
            kupovinaService.obrisiKupovinu(kupovina);
        }
        boughtCardsController.refreshKupovine();
    }

    private boolean showWindow(String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reservation.fxml"));
            Parent root = loader.load();
    
            ReservationBuyController reservationBuyController = loader.getController();
            reservationBuyController.setTip(title);
            reservationBuyController.setEvent(kupovina.getDogadjaj());
            reservationBuyController.setLoggedInUser(mainScreenController.korisnik);
            reservationBuyController.setMainScreenController(mainScreenController);
    
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
    
            stage.setMinWidth(871);
            stage.setMaxWidth(880);
            stage.setMinHeight(568);
    
            stage.showAndWait(); // Waits for the window to close
    
            return reservationBuyController.isZamjenaUspjesna(); // Check if the exchange was successful
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }    
    
}