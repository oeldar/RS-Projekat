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
import java.util.List;
import java.util.Optional;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajService;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaService;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.LokacijaService;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.MjestoService;
import grupa5.baza_podataka.Sektor;
import grupa5.baza_podataka.SektorService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private TextField maxBrojKartiText;
    @FXML
    private TextArea opisTextArea;
    @FXML
    private DatePicker pocetakDatum;
    @FXML
    private TextField pocetakVrijeme;

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
            // Prikupljanje podataka iz GUI
            String naziv = nazivTextField.getText();
            String opis = opisTextArea.getText();
            String vrsta = vrstaCombo.getSelectionModel().getSelectedItem();
            String podvrsta = podvrstaCombo.getSelectionModel().getSelectedItem();
            String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
            String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();
            LocalDateTime pocetak = LocalDateTime.of(pocetakDatum.getValue(), LocalTime.parse(pocetakVrijeme.getText()));
            LocalDateTime kraj = LocalDateTime.of(krajDatum.getValue(), LocalTime.parse(krajVrijeme.getText()));
            Integer maxBrojKarti = Integer.parseInt(maxBrojKartiText.getText());

            Mjesto mjesto = mjestoService.pronadjiSvaMjesta().stream()
                                        .filter(m -> m.getNaziv().equals(selectedMjesto))
                                        .findFirst()
                                        .orElse(null);

            Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                                            .filter(l -> l.getNaziv().equals(selectedLokacija))
                                            .findFirst()
                                            .orElse(null);

            if (mjesto != null && lokacija != null) {
                // Kreiranje događaja bez slike
                dogadjaj = dogadjajService.kreirajDogadjaj(naziv, opis, korisnik, mjesto, lokacija, pocetak, kraj, vrsta, podvrsta, null, maxBrojKarti);
                idDogadjaja = dogadjaj.getDogadjajID();

                // Kopiranje slike i postavljanje putanje
                if (selectedFile != null && idDogadjaja != null) {
                    copyAndSetImage(selectedFile, idDogadjaja);
                    dogadjaj.setPutanjaDoSlike(imagePath);
                }
    
                // Ažuriranje događaja sa putanjom slike
                dogadjajService.azurirajDogadjaj(dogadjaj);

                for (Node node : sektoriVBox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox sektorHBox = (HBox) node;
                        Label sektorLabel = (Label) sektorHBox.getChildren().get(0);
                        TextField cijenaInput = (TextField) sektorHBox.getChildren().get(1);
                        TextField uslovKupovineInput = (TextField) sektorHBox.getChildren().get(2);
                        TextField naplataKupovineInput = (TextField) sektorHBox.getChildren().get(3);
                        TextField uslovRezervacijeInput = (TextField) sektorHBox.getChildren().get(4);
                        TextField naplataRezervacijeInput = (TextField) sektorHBox.getChildren().get(5);

                        String sektorNaziv = sektorLabel.getText();
                        Double cijena;
                        Double naplataKupovine = 0.0;
                        Double naplataRezervacije = 0.0;
                        String uslovKupovine, uslovRezervacije;

                        // Pretvorba unosa u odgovarajuće tipove podataka
                        if (cijenaInput.getText() != null && !cijenaInput.getText().trim().isEmpty()) {
                            cijena = Double.parseDouble(cijenaInput.getText());
                        } else {
                            throw new IllegalArgumentException("Cijena ne može biti null.");
                        }

                        uslovKupovine = uslovKupovineInput.getText();
                        if (uslovKupovine != null && uslovKupovine.trim().isEmpty()) {
                            uslovKupovine = null;
                        }

                        if (naplataKupovineInput.getText() != null && !naplataKupovineInput.getText().trim().isEmpty()) {
                            naplataKupovine = Double.parseDouble(naplataKupovineInput.getText());
                        }

                        uslovRezervacije = uslovRezervacijeInput.getText();
                        if (uslovRezervacije != null && uslovRezervacije.trim().isEmpty()) {
                            uslovRezervacije = null;
                        }

                        if (naplataRezervacijeInput.getText() != null && !naplataRezervacijeInput.getText().trim().isEmpty()) {
                            naplataRezervacije = Double.parseDouble(naplataRezervacijeInput.getText());
                        }

                        // Pronađi sektor
                        Sektor sektor = sektorService.pronadjiSektorPoNazivuILokaciji(sektorNaziv, lokacija);

                        // Kreiranje karte
                        kartaService.kreirajKartu(dogadjaj, sektor, cijena, uslovKupovine, naplataKupovine, uslovRezervacije, naplataRezervacije, Karta.Status.DOSTUPNA);
                    }
                }

                // Zatvori prozor nakon spremanja
                Stage stage = (Stage) nazivTextField.getScene().getWindow();
                stage.close();

            } else {
                throw new IllegalArgumentException("Mjesto ili lokacija nisu pronađeni.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Možete dodati prikaz poruke o grešci korisniku ako je potrebno
        }
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
        TextField uslovKupovineInput = new TextField();
        uslovKupovineInput.setPromptText("Uslov otkazivanja kupovine");
        TextField naplataKupovineInput = new TextField();
        naplataKupovineInput.setPromptText("Naplata otkazivanja kupovine");
        TextField uslovRezervacijeInput = new TextField();
        uslovRezervacijeInput.setPromptText("Uslov otkazivanja rezervacije");
        TextField naplataRezervacijeInput = new TextField();
        naplataRezervacijeInput.setPromptText("Naplata otkazivanja rezervacije");

        HBox sektorHBox = new HBox(sektorLabel, cijenaInput, uslovKupovineInput, naplataKupovineInput, uslovRezervacijeInput, naplataRezervacijeInput);
        sektoriVBox.getChildren().add(sektorHBox);
    }
}
