package grupa5;

import java.io.File;
import java.io.InputStream;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.Dogadjaj.Status;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

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
    private Button preuzmiBtn;

    @FXML
    private Button refundirajBtn;

    private static final String DEFAULT_IMAGE_PATH = "/grupa5/assets/events_photos/default-event.png";

    private MainScreenController mainScreenController;
    private BoughtCardsController boughtCardsController;
    private Kupovina kupovina;


    @FXML
    public void initialize() {
        // Any initialization logic, if needed
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

            // Set data labels
            nameLbl.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            locationLbl.setText(dogadjaj.getMjesto().getNaziv() + ", " + dogadjaj.getLokacija().getNaziv());
            eventLNameLbl.setText(dogadjaj.getNaziv());
            priceLbl.setText(String.format("%.2f", kupovina.getKonacnaCijena()));
            ticketsNumberLbl.setText(String.valueOf(kupovina.getBrojKarata()));
            sectorLbl.setText(kupovina.getKarta().getSektorNaziv());

            if (kupovina.getStatus().equals(Kupovina.Status.NEAKTIVNA)) {
                refundirajBtn.setVisible(true);
                preuzmiBtn.setText("Zamijeni");
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
        if (preuzmiBtn.getText().equals("Zamijeni")) {
            // TODO: napisati logiku za zamijenu kupovine
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Izaberite Folder");

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            File pdfFile = new File(selectedDirectory, kupovina.getKupovinaID() + "_karta.pdf");

            try {
                PdfGenerator.generatePdf(pdfFile, kupovina);
                String message = "PDF karta je uspešno preuzeta i smeštena u: " + pdfFile.getAbsolutePath();
                Obavjest.showAlert("PDF Generisan", message);
            } catch (Exception e) {
                Obavjest.showAlert("Greška", "Došlo je do greške pri generisanju PDF-a: " + e.getMessage());
            }
        } else {
            Obavjest.showAlert("Greška", "Ne možete sačuvati PDF jer nije izabran folder.");
        }
    }
    
    @FXML
    void handleRefundiraj(ActionEvent event) {
        // TODO: napisati logiku za refundaciju
    }
    
}