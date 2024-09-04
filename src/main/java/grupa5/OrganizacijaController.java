package grupa5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.Sektor;
import grupa5.baza_podataka.services.DogadjajService;
import grupa5.baza_podataka.services.KartaService;
import grupa5.baza_podataka.services.LokacijaService;
import grupa5.baza_podataka.services.MjestoService;
import grupa5.baza_podataka.services.SektorService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OrganizacijaController {
    @FXML
    private ComboBox<String> vrstaCombo, podvrstaCombo, mjestoCombo, lokacijaCombo;
    @FXML
    private Label podvrstaLabel;
    @FXML
    private ImageView eventImage;
    @FXML
    private AnchorPane imageContainer, roundedCorners, removeImgPane;
    @FXML
    private Button dodajSlikuBtn;
    @FXML
    private VBox sektoriVBox;
    @FXML
    private DatePicker krajDatum;
    @FXML
    private TextField krajVrijeme;
    @FXML
    private TextField nazivTextField;
    @FXML
    private TextArea opisTextArea;
    @FXML
    private DatePicker pocetakDatum;
    @FXML
    private TextField pocetakVrijeme;
    @FXML
    private ImageView errorIcon;
    @FXML
    private Label errorLbl;

    private DogadjajService dogadjajService;
    private LokacijaService lokacijaService;
    private MjestoService mjestoService;
    private SektorService sektorService;
    private KartaService kartaService;
    private EntityManagerFactory entityManagerFactory;
    private Korisnik korisnik;
    private Dogadjaj dogadjaj;

    private String imagePath;
    private File selectedFile;
    private Integer idDogadjaja;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    
    public void initialize() {

        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        }

        dodajSlikuBtn.toFront();
        
        // Inicijalizacija servisa
        dogadjajService = new DogadjajService(entityManagerFactory);
        lokacijaService = new LokacijaService(entityManagerFactory);
        mjestoService = new MjestoService(entityManagerFactory);
        sektorService = new SektorService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);

        // Popunjavanje vrsta događaja
        vrstaCombo.getItems().addAll("Muzika", "Kultura", "Sport", "Ostalo");

        vrstaCombo.setOnAction(event -> {
            String selectedVrsta = vrstaCombo.getSelectionModel().getSelectedItem();
            podvrstaCombo.getItems().clear();
            podvrstaCombo.getItems().addAll(dogadjajService.getPodvrsteDogadjaja(selectedVrsta));
            podvrstaCombo.getItems().add("Unesite novu podvrstu...");
        });

        podvrstaCombo.setOnAction(event -> {
            String selectedPodvrsta = podvrstaCombo.getSelectionModel().getSelectedItem();
            if ("Unesite novu podvrstu...".equals(selectedPodvrsta)) {
                // Omogućiti unos nove podvrste
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Nova podvrsta");
                dialog.setHeaderText("Unos nove podvrste");
                dialog.setContentText("Molimo unesite novu podvrstu:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newPodvrsta -> {
                    podvrstaCombo.getItems().add(newPodvrsta);
                    podvrstaCombo.getSelectionModel().select(newPodvrsta);
                });
            }
        });

        // Popunjavanje mjesta
        mjestoCombo.getItems().clear();
        List<Mjesto> mjesta = mjestoService.pronadjiSvaMjesta();
        for (Mjesto mjesto : mjesta) {
            mjestoCombo.getItems().add(mjesto.getNaziv());
        }

        mjestoCombo.setOnAction(event -> {
            String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
            Mjesto mjesto = mjesta.stream()
                                .filter(m -> m.getNaziv().equals(selectedMjesto))
                                .findFirst()
                                .orElse(null);

            lokacijaCombo.getItems().clear();
            if (mjesto != null) {
                List<Lokacija> lokacije = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto);
                for (Lokacija lokacija : lokacije) {
                    lokacijaCombo.getItems().add(lokacija.getNaziv());
                }
            }

            lokacijaCombo.setOnAction(event2 -> {
                String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();
                Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                                        .filter(l -> l.getNaziv().equals(selectedLokacija))
                                        .findFirst()
                                        .orElse(null);
    
                sektoriVBox.getChildren().clear();
                if (lokacija != null) {
                    List<Sektor> sektori = sektorService.pronadjiSveSektoreZaLokaciju(lokacija);
                    for (Sektor sektor : sektori) {
                        addSektor(sektor);
                    }
                }
            });
        });
    }

    @FXML
    void handleSpremi(ActionEvent event) {
        try {
            // Reset styles and clear previous errors
            boolean valid = true;
            resetFieldStyles();
            clearError();
    
            // 1. Provjera obaveznih polja
            if (nazivTextField.getText().trim().isEmpty()) {
                markFieldAsInvalid(nazivTextField);
                // showError("Naziv događaja ne smije biti prazan.");
                valid = false;
            }
    
            if (vrstaCombo.getSelectionModel().getSelectedItem() == null) {
                markFieldAsInvalid(vrstaCombo);
                // showError("Vrsta događaja nije odabrana.");
                valid = false;
            }
    
            if (mjestoCombo.getSelectionModel().getSelectedItem() == null) {
                markFieldAsInvalid(mjestoCombo);
                // showError("Mjesto događaja nije odabrano.");
                valid = false;
            }
    
            if (lokacijaCombo.getSelectionModel().getSelectedItem() == null) {
                markFieldAsInvalid(lokacijaCombo);
                // showError("Lokacija događaja nije odabrana.");
                valid = false;
            }
    
            if (pocetakDatum.getValue() == null) {
                markFieldAsInvalid(pocetakDatum);
                // showError("Datum početka nije odabran.");
                valid = false;
            }
    
            if (krajDatum.getValue() == null) {
                markFieldAsInvalid(krajDatum);
                // showError("Datum kraja nije odabran.");
                valid = false;
            }
    
            if (pocetakVrijeme.getText().trim().isEmpty()) {
                markFieldAsInvalid(pocetakVrijeme);
                // showError("Vrijeme početka nije uneseno.");
                valid = false;
            }
    
            if (krajVrijeme.getText().trim().isEmpty()) {
                markFieldAsInvalid(krajVrijeme);
                // showError("Vrijeme kraja nije uneseno.");
                valid = false;
            }

            if (!valid) {
                showError("Nisu popunjena sva potrebna polja");
                return;
            }
    
            // 2. Provjera formata vremena i specifičnih grešaka
            LocalTime pocetakVrijemeParsed;
            LocalTime krajVrijemeParsed;
            try {
                pocetakVrijemeParsed = LocalTime.parse(pocetakVrijeme.getText());
            } catch (DateTimeParseException e) {
                markFieldAsInvalid(pocetakVrijeme);
                showError("Unesite ispravno vrijeme početka u formatu HH:mm.");
                return;
            }
    
            try {
                krajVrijemeParsed = LocalTime.parse(krajVrijeme.getText());
            } catch (DateTimeParseException e) {
                markFieldAsInvalid(krajVrijeme);
                showError("Unesite ispravno vrijeme kraja u formatu HH:mm.");
                return;
            }
    
            // Provjera da li je kraj događaja prije početka
            LocalDateTime pocetak1 = LocalDateTime.of(pocetakDatum.getValue(), pocetakVrijemeParsed);
            LocalDateTime kraj1 = LocalDateTime.of(krajDatum.getValue(), krajVrijemeParsed);
            if (kraj1.isBefore(pocetak1)) {
                markFieldAsInvalid(pocetakVrijeme);
                markFieldAsInvalid(pocetakDatum);
                markFieldAsInvalid(krajDatum);
                markFieldAsInvalid(krajVrijeme);
                showError("Datum i vrijeme kraja događaja ne mogu biti prije početka događaja.");
                return;
            }
    
            // 3. Provjera preklapanja događaja i validacija mjesta i lokacije
            String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
            String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();
    
            Mjesto mjesto = mjestoService.pronadjiSvaMjesta().stream()
                                        .filter(m -> m.getNaziv().equals(selectedMjesto))
                                        .findFirst()
                                        .orElse(null);
    
            if (mjesto == null) {
                showError("Odabrano mjesto nije pronađeno.");
                return;
            }
    
            Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                                                .filter(l -> l.getNaziv().equals(selectedLokacija))
                                                .findFirst()
                                                .orElse(null);
    
            if (lokacija == null) {
                showError("Odabrana lokacija nije pronađena.");
                return;
            }
    
            // Provjera preklapanja događaja
            List<Dogadjaj> preklapanja = dogadjajService.pronadjiPreklapanja(pocetak1, kraj1, lokacija);
            if (!preklapanja.isEmpty()) {
                markFieldAsInvalid(pocetakVrijeme);
                markFieldAsInvalid(pocetakDatum);
                markFieldAsInvalid(krajDatum);
                markFieldAsInvalid(krajVrijeme);
                showError("Period održavanja događaja se preklapa sa već postojećim događajem.");
                return;
            }
    
            // 4. Provjera da li je događaj u budućnosti
            if (pocetak1.isBefore(LocalDateTime.now())) {
                markFieldAsInvalid(pocetakVrijeme);
                markFieldAsInvalid(pocetakDatum);
                markFieldAsInvalid(krajDatum);
                markFieldAsInvalid(krajVrijeme);
                showError("Datum i vrijeme događaja ne mogu biti u prošlosti.");
                return;
            }
    
            // Provjera podataka za sve karte
            for (Node node : sektoriVBox.getChildren()) {
                if (node instanceof VBox) {
                    VBox sektorVBox = (VBox) node;
                    HBox firstRow = (HBox) sektorVBox.getChildren().get(1);
                    TextField cijenaInput = (TextField) firstRow.getChildren().get(0);
                    TextField maxBrojKartiInput = (TextField) firstRow.getChildren().get(1);
                    HBox secondRow = (HBox) sektorVBox.getChildren().get(2);
                    TextField naplataRezervacijeInput = (TextField) secondRow.getChildren().get(0);
                    TextField brojSatiInput = (TextField) secondRow.getChildren().get(1);
    
                    try {
                        Double.parseDouble(cijenaInput.getText());
                    } catch (NumberFormatException e) {
                        markFieldAsInvalid(cijenaInput);
                        showError("Unesite ispravnu cijenu za sektor.");
                        return;
                    }
    
                    try {
                        Integer.parseInt(maxBrojKartiInput.getText());
                    } catch (NumberFormatException e) {
                        markFieldAsInvalid(maxBrojKartiInput);
                        showError("Unesite ispravan broj karata za sektor.");
                        return;
                    }
    
                    try {
                        Integer.parseInt(brojSatiInput.getText());
                    } catch (NumberFormatException e) {
                        markFieldAsInvalid(brojSatiInput);
                        showError("Unesite ispravan broj sati za sektor.");
                        return;
                    }
    
                    if (!naplataRezervacijeInput.getText().isEmpty()) {
                        try {
                            Double.parseDouble(naplataRezervacijeInput.getText());
                        } catch (NumberFormatException e) {
                            markFieldAsInvalid(naplataRezervacijeInput);
                            showError("Unesite ispravnu naplatu za otkazivanje rezervacije.");
                            return;
                        }
                    }
                }
            }
    
            // Prikupljanje podataka iz GUI nakon što su svi validirani
            String naziv = nazivTextField.getText();
            String opis = opisTextArea.getText();
            String vrsta = vrstaCombo.getSelectionModel().getSelectedItem();
            String podvrsta = podvrstaCombo.getSelectionModel().getSelectedItem();
    
            // Kreiranje događaja bez slike
            dogadjaj = dogadjajService.kreirajDogadjaj(naziv, opis, korisnik, mjesto, lokacija, pocetak1, kraj1, vrsta, podvrsta, null);
            idDogadjaja = dogadjaj.getDogadjajID();
    
            // Kopiranje slike i postavljanje putanje
            if (selectedFile != null && idDogadjaja != null) {
                copyAndSetImage(selectedFile, idDogadjaja);
                dogadjaj.setPutanjaDoSlike(imagePath);
            }
    
            // Ažuriranje događaja sa putanjom slike
            dogadjajService.azurirajDogadjaj(dogadjaj);
    
            // Kreiranje svih karata nakon kreiranja događaja
            for (Node node : sektoriVBox.getChildren()) {
                if (node instanceof VBox) {
                    VBox sektorVBox = (VBox) node;
                    Label sektorLabel = (Label) sektorVBox.getChildren().get(0);
                    HBox firstRow = (HBox) sektorVBox.getChildren().get(1);
                    TextField cijenaInput = (TextField) firstRow.getChildren().get(0);
                    TextField maxBrojKartiInput = (TextField) firstRow.getChildren().get(1);
                    HBox secondRow = (HBox) sektorVBox.getChildren().get(2);
                    TextField naplataRezervacijeInput = (TextField) secondRow.getChildren().get(0);
                    TextField brojSatiInput = (TextField) secondRow.getChildren().get(1);
    
                    String sektorNaziv = sektorLabel.getText();
                    Double cijena = Double.parseDouble(cijenaInput.getText());
                    Double naplataRezervacije = naplataRezervacijeInput.getText().isEmpty() ? 0.0 : Double.parseDouble(naplataRezervacijeInput.getText());
                    Integer maxBrojKarti = Integer.parseInt(maxBrojKartiInput.getText());
                    Integer brojSati = Integer.parseInt(brojSatiInput.getText());
    
                    // Pronađi sektor
                    Sektor sektor = sektorService.pronadjiSektorPoNazivuILokaciji(sektorNaziv, lokacija);
    
                    LocalDateTime poslednjiDatumZaRezervaciju = dogadjaj.getPocetakDogadjaja().minusHours(brojSati);
                    // Kreiranje karte
                    kartaService.kreirajKartu(dogadjaj, sektor, cijena, poslednjiDatumZaRezervaciju, naplataRezervacije, maxBrojKarti, Karta.Status.DOSTUPNA);
                }
            }
    
            // Zatvaranje prozora
            Stage stage = (Stage) nazivTextField.getScene().getWindow();
            stage.close();
    
        } catch (Exception e) {
            showError("Dogodila se greška pri spremanju događaja. " + e.getMessage());
        }
    }    

    private void showError(String message) {
        errorLbl.setText(message);
        errorLbl.setVisible(true);
        errorIcon.setVisible(true);
    }

    private void clearError() {
        errorLbl.setText("");
        errorLbl.setVisible(false);
        errorIcon.setVisible(false);
    }

    private void markFieldAsInvalid(Control control) {
        control.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }
    
    private void resetFieldStyles() {
        nazivTextField.setStyle(null);
        vrstaCombo.setStyle(null);
        mjestoCombo.setStyle(null);
        lokacijaCombo.setStyle(null);
        pocetakDatum.setStyle(null);
        krajDatum.setStyle(null);
        pocetakVrijeme.setStyle(null);
        krajVrijeme.setStyle(null);
    }
    

    @FXML
    void imageDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasImage() || dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }

        event.consume();
    }

    @FXML
    void imageDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            selectedFile = dragboard.getFiles().get(0); // Sačuvajte referencu na izabrani fajl
            
            Image image = new Image(selectedFile.toURI().toString());
            eventImage.setImage(image);
            dodajSlikuBtn.setVisible(false);
            removeImgPane.setVisible(true);
            imageContainer.setVisible(false);
            roundedCorners.setVisible(true);
        }
        event.consume();
    }



    @FXML
    void ukloniSliku(ActionEvent event) {
        eventImage.setImage(null);
        removeImgPane.setVisible(false);
        imageContainer.setVisible(true);
        roundedCorners.setVisible(false);
        dodajSlikuBtn.setVisible(true);

        // Resetuj imagePath
        imagePath = null;
    }

    @FXML
    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage stage = (Stage) eventImage.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage); // Sačuvajte referencu na izabrani fajl
        
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            eventImage.setImage(image);
            dodajSlikuBtn.setVisible(false);
            removeImgPane.setVisible(true);
            imageContainer.setVisible(false);
            roundedCorners.setVisible(true);
        }
    }


    private void copyAndSetImage(File selectedFile, Integer idDogadjaja) throws IOException {
        // Definišite direktorijum gde će se slike čuvati
        Path destinationDir = Paths.get("src/main/resources/grupa5/assets/events_photos/");
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
        }
    
        // Dobijanje ekstenzije originalne slike
        String fileName = selectedFile.getName();
        String fileExtension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            fileExtension = fileName.substring(i); // uključiće i tačku, npr. ".jpg"
        }
    
        // Kreiranje novog naziva fajla koristeći id događaja
        String newFileName = idDogadjaja + fileExtension;
        Path destinationPath = destinationDir.resolve(newFileName);
    
        // Kopirajte sliku u direktorijum pod novim nazivom
        Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    
        // Postavite relativnu putanju za čuvanje u bazu podataka
        imagePath = "assets/events_photos/" + newFileName;
    
        // Postavite sliku u ImageView
        Image image = new Image(destinationPath.toUri().toString());
        eventImage.setImage(image);
        dodajSlikuBtn.setVisible(false);
        removeImgPane.setVisible(true);
        imageContainer.setVisible(false);
        roundedCorners.setVisible(true);
    }    

    private void addSektor(Sektor sektor) {
        Label sektorLabel = new Label(sektor.getNaziv());
        sektorLabel.setMinWidth(100);
    
        TextField cijenaInput = new TextField();
        cijenaInput.setPromptText("Unesite cijenu");
        cijenaInput.getStyleClass().add("input");
    
        TextField maxBrojKartiInput = new TextField();
        maxBrojKartiInput.setPromptText("Maksimalan broj karti po korisniku");
        maxBrojKartiInput.getStyleClass().add("input");
    
        TextField naplataRezervacijeInput = new TextField();
        naplataRezervacijeInput.setPromptText("Naplata otkazivanja rezervacije");
        naplataRezervacijeInput.getStyleClass().add("input");
    
        TextField brojSatiInput = new TextField();
        brojSatiInput.setPromptText("Broj sati prije događaja za kupovinu rezervacije");
        brojSatiInput.getStyleClass().add("input");
    
        // Postavljanje razmaka između elemenata u HBox
        HBox firstRow = new HBox(10, cijenaInput, maxBrojKartiInput);  // 10 je razmak u pikselima
        HBox secondRow = new HBox(10, naplataRezervacijeInput, brojSatiInput);
    
        // Postavljanje Hgrow za TextField-ove da popune ceo prostor
        HBox.setHgrow(cijenaInput, Priority.ALWAYS);
        HBox.setHgrow(maxBrojKartiInput, Priority.ALWAYS);
        HBox.setHgrow(naplataRezervacijeInput, Priority.ALWAYS);
        HBox.setHgrow(brojSatiInput, Priority.ALWAYS);
    
        VBox sektorVBox = new VBox(10, sektorLabel, firstRow, secondRow);  // 10 je razmak između redova
        sektoriVBox.getChildren().add(sektorVBox);
    
        // Odloženo ažuriranje visine
        Platform.runLater(() -> {
            // Prvo osveži raspored
            sektoriVBox.requestLayout();
            sektoriVBox.layout();
    
            // Sada ažuriraj visinu
            postaviVisinuVBox();
            postaviVisinuAnchorPane();
        });
    }
    
    private void postaviVisinuVBox() {
        Platform.runLater(() -> {
            double visina = sektoriVBox.getChildren().stream()
                                .mapToDouble(node -> node.prefHeight(-1))
                                .sum();
            sektoriVBox.setPrefHeight(visina);
            sektoriVBox.setMinHeight(visina);
            sektoriVBox.setMaxHeight(visina);
            sektoriVBox.requestLayout();
            sektoriVBox.layout(); // Osveži raspored
            // System.out.println("Visina VBox: " + sektoriVBox.getHeight());
        });
    }
    
    private void postaviVisinuAnchorPane() {
        Platform.runLater(() -> {
            Parent roditelj = sektoriVBox.getParent();
            if (roditelj instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) roditelj;
                double visinaSektoriVBox = sektoriVBox.getHeight();
                anchorPane.setPrefHeight(visinaSektoriVBox);
                anchorPane.setMinHeight(visinaSektoriVBox);
                anchorPane.setMaxHeight(visinaSektoriVBox);
                anchorPane.requestLayout();
                anchorPane.layout(); // Osveži raspored
               // System.out.println("Visina AnchorPane: " + anchorPane.getHeight());
            }
        });
    }
    
    
}
