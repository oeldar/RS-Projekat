package grupa5;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajPrijedlog;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.KartaPrijedlog;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.Sektor;
import grupa5.baza_podataka.services.DogadjajPrijedlogService;
import grupa5.baza_podataka.services.DogadjajService;
import grupa5.baza_podataka.services.KartaPrijedlogService;
import grupa5.baza_podataka.services.KartaService;
import grupa5.baza_podataka.services.LokacijaService;
import grupa5.baza_podataka.services.MjestoService;
import grupa5.baza_podataka.services.SektorService;
import grupa5.support_classes.ImageSelector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditEventController {
    private static final String PERSISTENCE_UNIT_NAME = "HypersistenceOptimizer";

    @FXML
    private DatePicker krajnjiDatum;

    @FXML
    private TextField nazivText;

    @FXML
    private TextArea opisTextArea;

    @FXML
    private DatePicker pocetniDatum;

    @FXML
    private ComboBox<String> podvrstaCombo;

    @FXML
    private TextField vrijemeKraja;

    @FXML
    private TextField vrijemePocetka;

    @FXML
    private ComboBox<String> vrstaCombo;

    @FXML
    private ComboBox<String> lokacijaCombo;

    @FXML
    private ComboBox<String> mjestoCombo;

    @FXML
    private VBox sektoriBox;

    @FXML
    private ImageView errorImage;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView warningImage;

    @FXML
    private Label warningLabel;

    @FXML
    private ImageView eventImage;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private DogadjajService dogadjajService;
    private DogadjajPrijedlogService dogadjajPrijedlogService;
    private LokacijaService lokacijaService;
    private MjestoService mjestoService;
    private SektorService sektorService;
    private KartaService kartaService;
    private KartaPrijedlogService kartaPrijedlogService;
    private Korisnik korisnik;
    private Dogadjaj dogadjaj;
    private DogadjajPrijedlog dogadjajPrijedlog = new DogadjajPrijedlog();
    private List<KartaPrijedlog> prijedloziKarata;
    private boolean dogadjajPromijenjen = false;
    private boolean lokacijaPromjenjena = false;
    private boolean samoKartePromijenjene = false;

    private MojiDogadjajiController mojiDogadjajiController;

    private String naziv;
    private String opis;
    private final String opcijaNovePodvrste = "Unesite novu podvrstu...";

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public void setDogadjajService(DogadjajService dogadjajService) {
        this.dogadjajService = dogadjajService;
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    }

    public void setMojiDogadjajiController(MojiDogadjajiController mojiDogadjajiController) {
        this.mojiDogadjajiController = mojiDogadjajiController;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            createServices();
            initView();
        });
    }

    private void createServices() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        entityManager = entityManagerFactory.createEntityManager();
        mjestoService = new MjestoService(entityManagerFactory);
        lokacijaService = new LokacijaService(entityManagerFactory);
        sektorService = new SektorService(entityManagerFactory);
        dogadjajPrijedlogService = new DogadjajPrijedlogService(entityManagerFactory);
        kartaPrijedlogService = new KartaPrijedlogService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
    }

    private void initView() {
        nazivText.setText(dogadjaj.getNaziv());
        opisTextArea.setText(dogadjaj.getOpis());

        vrstaCombo.getItems().addAll("Muzika", "Kultura", "Sport", "Ostalo");
        vrstaCombo.getSelectionModel().select(dogadjaj.getVrstaDogadjaja());
        podvrstaCombo.getItems().addAll(dogadjajService.getPodvrsteDogadjaja(dogadjaj.getVrstaDogadjaja()));
        podvrstaCombo.getItems().add(opcijaNovePodvrste);
        podvrstaCombo.getSelectionModel().select(dogadjaj.getPodvrstaDogadjaja());

        vrstaCombo.setOnAction(event -> changePodvrstaOptions());
        podvrstaCombo.setOnAction(event -> dodajNovuPodvrstu());

        pocetniDatum.setValue(dogadjaj.getPocetakDogadjaja().toLocalDate());
        krajnjiDatum.setValue(dogadjaj.getKrajDogadjaja().toLocalDate());
        vrijemePocetka.setText(dogadjaj.getPocetakDogadjaja().toLocalTime().toString());
        vrijemeKraja.setText(dogadjaj.getKrajDogadjaja().toLocalTime().toString());

        configureMjestoComboBox();
        configureLokacija(dogadjaj.getMjesto());
        List<Sektor> sektori = sektorService.pronadjiSveSektoreZaLokaciju(dogadjaj.getLokacija());
        for (Sektor sektor : sektori)
            addSektor(sektor, sektoriBox);

        showEventImage();
    }

    private void changePodvrstaOptions() {
        String selectedVrsta = vrstaCombo.getSelectionModel().getSelectedItem();
        podvrstaCombo.getItems().clear();
        podvrstaCombo.getItems().addAll(dogadjajService.getPodvrsteDogadjaja(selectedVrsta));
        podvrstaCombo.getItems().add(opcijaNovePodvrste);
    }

    private void dodajNovuPodvrstu() {
        String selectedPodvrsta = podvrstaCombo.getSelectionModel().getSelectedItem();
        if (selectedPodvrsta.equals(opcijaNovePodvrste)) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nova podvrsta");
            dialog.setHeaderText("Unos nove podvrste");
            dialog.setContentText("Molimo unesite novu podvrstu: ");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(novaPodvrsta -> {
                podvrstaCombo.getItems().add(novaPodvrsta);
                podvrstaCombo.getSelectionModel().select(novaPodvrsta);
            });
        }
    }

    private void configureMjestoComboBox() {
        List<Mjesto> mjesta = mjestoService.pronadjiSvaMjesta();
        for (Mjesto mjesto : mjesta) {
            mjestoCombo.getItems().add(mjesto.getNaziv());
        }
        mjestoCombo.getSelectionModel().select(dogadjaj.getMjesto().getNaziv());

        mjestoCombo.setOnAction(event -> {
            String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
            Mjesto mjesto = mjesta.stream()
                    .filter(m -> m.getNaziv().equals(selectedMjesto))
                    .findFirst()
                    .orElse(null);

            configureLokacija(mjesto);
        });
    }

    private void configureLokacija(Mjesto mjesto) {
        lokacijaCombo.getItems().clear();
        
        if (mjesto != null) {
            List<Lokacija> lokacije = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto);
            for (Lokacija lokacija : lokacije) {
                lokacijaCombo.getItems().add(lokacija.getNaziv());
            }
        }
        lokacijaCombo.getSelectionModel().select(dogadjaj.getLokacija().getNaziv());

        lokacijaCombo.setOnAction(newEvent -> {
            String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();
            Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                    .filter(loc -> loc.getNaziv().equals(selectedLokacija))
                    .findFirst().orElse(null);

            sektoriBox.getChildren().clear();
            if (lokacija != null) {
                List<Sektor> sektori = sektorService.pronadjiSveSektoreZaLokaciju(lokacija);
                for (Sektor sektor : sektori) {
                    addSektor(sektor, sektoriBox);
                }
            }

        });
    }

    private void addSektor(Sektor sektor, VBox sektoriVBox) {
        // TODO: popuniti polja u sektorima
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

        HBox firstRow = new HBox(10, cijenaInput, maxBrojKartiInput);
        HBox secondRow = new HBox(10, naplataRezervacijeInput, brojSatiInput);

        HBox.setHgrow(cijenaInput, Priority.ALWAYS);
        HBox.setHgrow(maxBrojKartiInput, Priority.ALWAYS);
        HBox.setHgrow(naplataRezervacijeInput, Priority.ALWAYS);
        HBox.setHgrow(brojSatiInput, Priority.ALWAYS);

        Button clearButton = new Button("Očisti");
        clearButton.getStyleClass().add("clear-button");
        clearButton.setOnAction(event -> clearBox(clearButton));

        Button confirmButton = new Button("Potvrdi");
        confirmButton.getStyleClass().add("confirm-button");
        confirmButton.setOnAction(event -> updateKarte(confirmButton));
        
        HBox buttonsRow = new HBox(10, clearButton, confirmButton);
        buttonsRow.setAlignment(Pos.CENTER_RIGHT);

        VBox sektorVBox = new VBox(10, sektorLabel, firstRow, secondRow, buttonsRow);
        sektoriVBox.getChildren().add(sektorVBox);

        Platform.runLater(() -> {
            sektoriVBox.requestLayout();
            sektoriVBox.layout();
            postaviVisinuVBox(sektoriVBox);
        });
    }

    private void postaviVisinuVBox(VBox sektoriVBox) {
        Platform.runLater(() -> {
            double visina = sektoriVBox.getChildren().stream()
                    .mapToDouble(node -> node.prefHeight(-1))
                    .sum();
            sektoriVBox.setPrefHeight(visina);
            sektoriVBox.setMinHeight(visina);
            sektoriVBox.setMaxHeight(visina);
            sektoriVBox.requestLayout();
            sektoriVBox.layout();
        });
    }

    private void showEventImage() {
        String pathToImage = dogadjaj.getPutanjaDoSlike();
        String defaultImgagePath = "assets/events_photos/default-event.png";

        if (dogadjaj.getPutanjaDoSlike() == null || dogadjaj.getPutanjaDoSlike().isEmpty()) {
            pathToImage = defaultImgagePath;
        }

        try (InputStream inputStream = getClass().getResourceAsStream(pathToImage)) {
            if (inputStream != null) {
                Image image = new Image(inputStream);
                eventImage.setImage(image);
            } else {
                eventImage.setImage(
                        new Image(defaultImgagePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
            eventImage
                    .setImage(new Image(defaultImgagePath));
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        eventImage.setImage(ImageSelector.selectEventImage(getStage()));
    }

    @FXML
    void clearInput(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();

        switch (sourceButton.getId()) {
            case "clearNazivButton" -> nazivText.clear();
            case "clearOpisButton" -> opisTextArea.clear();
            case "clearVrstaButton" -> vrstaCombo.setValue(null);
            case "clearPodvrstaButton" -> podvrstaCombo.setValue(null);
            case "clearPocetniDatumButton" -> pocetniDatum.setValue(null);
            case "clearPocetnoVrijemeButton" -> vrijemePocetka.clear();
            case "clearKrajnjiDatumButton" -> krajnjiDatum.setValue(null);
            case "clearVrijemeKrajaButton" -> vrijemeKraja.clear();
            case "clearMjestoButton" -> mjestoCombo.setValue(null);
            case "clearLokacijaButton" -> lokacijaCombo.setValue(null);
            default -> {
            }
        }
    }

    @FXML
    void updateInputs(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        updateConfirmIcon(sourceButton);

        switch (sourceButton.getId()) {
            case "nazivButton" -> updateNaziv(sourceButton);
            case "opisButton" -> updateOpis();
            case "vrstaButton" -> updateVrsta(sourceButton);
            case "podvrstaButton" -> updatePodvrsta(sourceButton);
            case "datumPocetkaButton" -> updateDatumPocetka(sourceButton);
            case "datumKrajaButton" -> updateDatumKraja(sourceButton);
            case "vrijemePocetkaButton" -> updateVrijemePocetka(sourceButton);
            case "vrijemeKrajaButton" -> updateVrijemeKraja(sourceButton);
            case "mjestoButton" -> updateMjesto(sourceButton);
            case "lokacijaButton" -> updateLokacija(sourceButton);
            case "updateImageButton" -> updateImage();
            default -> {
            }
        }
    }

    private void updateNaziv(Button button) {
        if (!validateNaziv()) {
            resetConfirmIcon(button);
            return;
        }

        dogadjajPrijedlog.setNaziv(nazivText.getText());
        dogadjajPromijenjen = true;
    }

    private void updateOpis() {
        dogadjajPrijedlog.setOpis(opisTextArea.getText());
        dogadjajPromijenjen = true;
    }

    private void updateVrsta(Button button) {
        if (!validateVrsta()) {
            resetConfirmIcon(button);
            return;
        }

        dogadjajPrijedlog.setVrstaDogadjaja(vrstaCombo.getSelectionModel().getSelectedItem());
        dogadjajPrijedlog.setPodvrstaDogadjaja(podvrstaCombo.getSelectionModel().getSelectedItem());
        dogadjajPromijenjen = true;
    }

    private void updatePodvrsta(Button button) {
        if (!validatePodvrsta()) {
            resetConfirmIcon(button);
            return;
        }

        dogadjajPrijedlog.setPodvrstaDogadjaja(podvrstaCombo.getSelectionModel().getSelectedItem());
        dogadjajPromijenjen = true;
    }

    private void updateDatumPocetka(Button button) {
        if (!validateDatumPocetka()) {
            resetConfirmIcon(button);
            return;
        }

        LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
        dogadjajPrijedlog.setPocetakDogadjaja(pocetak);
        dogadjajPromijenjen = true;
    }

    private void updateDatumKraja(Button button) {
        if (!validateDatumKraja()) {
            resetConfirmIcon(button);
            return;
        }

        LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
        dogadjajPrijedlog.setPocetakDogadjaja(kraj);
        dogadjajPromijenjen = true;
    }

    private void updateVrijemePocetka(Button button) {
        //if (!validateVrijemePocetka()) {
        //    resetConfirmIcon(button);
        //    return;
        //}

        LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
        dogadjajPrijedlog.setPocetakDogadjaja(pocetak);
        dogadjajPromijenjen = true;
    }

    private void updateVrijemeKraja(Button button) {
        if (!validateVrijemePocetka()) {
            resetConfirmIcon(button);
            return;
        }

        LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
        dogadjajPrijedlog.setPocetakDogadjaja(kraj);
        dogadjajPromijenjen = true;
    }

    private void updateMjesto(Button button) {
        if (!validateMjesto()) {
            resetConfirmIcon(button);
            return;
        }

        String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
        String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();

        Mjesto mjesto = mjestoService.pronadjiSvaMjesta().stream()
                                    .filter(m -> m.getNaziv().equals(selectedMjesto))
                                    .findFirst()
                                    .orElse(null);
        Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                                    .filter(l -> l.getNaziv().equals(selectedLokacija))
                                    .findFirst()
                                    .orElse(null);

        List<KartaPrijedlog> kartaPrijedlogList = new ArrayList<>();
        for (Node node : sektoriBox.getChildren()) {
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
                KartaPrijedlog kartaPrijedlog = new KartaPrijedlog();
                kartaPrijedlog.setCijena(cijena);
                kartaPrijedlog.setDogadjajPrijedlog(dogadjajPrijedlog);
                kartaPrijedlog.setMaxBrojKartiPoKorisniku(maxBrojKarti);
                kartaPrijedlog.setNaplataOtkazivanjaRezervacije(naplataRezervacije);
                kartaPrijedlog.setPoslednjiDatumZaRezervaciju(poslednjiDatumZaRezervaciju);
                kartaPrijedlog.setSektor(sektor);

                kartaPrijedlogList.add(kartaPrijedlog);
            }
        }

        dogadjajPrijedlog.setMjesto(mjesto);
        dogadjajPrijedlog.setLokacija(lokacija);
        prijedloziKarata = kartaPrijedlogList;
        dogadjajPromijenjen = true;
        lokacijaPromjenjena = true;
    }

    private void updateLokacija(Button button) {
        if (!validateLokacija()) {
            resetConfirmIcon(button);
            return;
        }

        String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
        String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();

        Mjesto mjesto = mjestoService.pronadjiSvaMjesta().stream()
                                    .filter(m -> m.getNaziv().equals(selectedMjesto))
                                    .findFirst()
                                    .orElse(null);
        Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                                    .filter(l -> l.getNaziv().equals(selectedLokacija))
                                    .findFirst()
                                    .orElse(null);

        List<KartaPrijedlog> kartaPrijedlogList = new ArrayList<>();
        for (Node node : sektoriBox.getChildren()) {
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
                KartaPrijedlog kartaPrijedlog = new KartaPrijedlog();
                kartaPrijedlog.setCijena(cijena);
                kartaPrijedlog.setDogadjajPrijedlog(dogadjajPrijedlog);
                kartaPrijedlog.setMaxBrojKartiPoKorisniku(maxBrojKarti);
                kartaPrijedlog.setNaplataOtkazivanjaRezervacije(naplataRezervacije);
                kartaPrijedlog.setPoslednjiDatumZaRezervaciju(poslednjiDatumZaRezervaciju);
                kartaPrijedlog.setSektor(sektor);

                kartaPrijedlogList.add(kartaPrijedlog);
            }
        }

        dogadjajPrijedlog.setLokacija(lokacija);
        prijedloziKarata = kartaPrijedlogList;
        dogadjajPromijenjen = true;
        lokacijaPromjenjena = true;
    }

    private void updateKarte(Button button) {
        updateConfirmIcon(button);
        if (!validateKarte(button)) {
            resetConfirmIcon(button);
            return;
        }

        if (!lokacijaPromjenjena) {
            samoKartePromijenjene = true;
        }
    }

    private void updateImage() {
        // TODO: update slika dogadjaja
        // dogadjajPrijedlog.setPutanjaDoSlike();
        dogadjajPromijenjen = true;
    }

   
    @FXML
    void potvrdiDogadjaj(ActionEvent event) {
        if (dogadjajPromijenjen) {
            dogadjajPrijedlog.setOriginalniDogadjaj(dogadjaj);
            dogadjajPrijedlogService.kreirajDogadjajPrijedlog(dogadjajPrijedlog);
            if (lokacijaPromjenjena) {
                for (KartaPrijedlog kartaPrijedlog : prijedloziKarata) {
                    kartaPrijedlogService.kreirajKartaPrijedlog(kartaPrijedlog);
                }
            }
        }

        if (samoKartePromijenjene) {
            String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();
            String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();
    
            Mjesto mjesto = mjestoService.pronadjiSvaMjesta().stream()
                                        .filter(m -> m.getNaziv().equals(selectedMjesto))
                                        .findFirst()
                                        .orElse(null);
            Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(mjesto).stream()
                                        .filter(l -> l.getNaziv().equals(selectedLokacija))
                                        .findFirst()
                                        .orElse(null);
            for (Node node : sektoriBox.getChildren()) {
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

                    Karta karta = kartaService.pronadjiKartuPoSektoruIDogadjaju(sektor, dogadjaj);
                    karta.setCijena(cijena);
                    karta.setMaxBrojKartiPoKorisniku(maxBrojKarti);
                    karta.setNaplataOtkazivanjaRezervacije(naplataRezervacije);
                    karta.setPoslednjiDatumZaRezervaciju(poslednjiDatumZaRezervaciju);

                    kartaService.azurirajKartu(karta);
                }
            }
        }

        Stage stage = (Stage) nazivText.getScene().getWindow();
        stage.close();

        mojiDogadjajiController.refreshDogadjaji();
    }

    // MARK: - Validations

    private boolean validateNaziv() {
        removeError(nazivText);
        if (nazivText.getText().isEmpty()) {
            showError(nazivText, "Nedostaje unos!");
            return false;
        }

        return true;
    }

    private boolean validateVrsta() {
        removeError(vrstaCombo);
        removeWarning(podvrstaCombo);

        if (vrstaCombo.getSelectionModel().getSelectedItem() == null) {
            showError(vrstaCombo, "Nedostaje unos!");
            return false;
        }

        if (podvrstaCombo.getSelectionModel().getSelectedItem() == null) {
            showWarning(podvrstaCombo, "Podvrsta izbrisana!");
        }

        return true;
    }

    private boolean validatePodvrsta() {
        removeError(podvrstaCombo);
        removeWarning(podvrstaCombo);

        if (podvrstaCombo.getSelectionModel().getSelectedItem() == null) {
            showError(podvrstaCombo, "Nedostaje unos!");
            return false;
        }

        return true;
    }

    private boolean validateDatumPocetka() {
        removeError(pocetniDatum);

        if (pocetniDatum.getValue() == null) {
            showError(pocetniDatum, "Nedostaje unos");
            return false;
        }

        if (pocetniDatum.getValue().isAfter(dogadjaj.getKrajDogadjaja().toLocalDate())) {
            showError(errorLabel, "Datum početka poslije datuma kraja!");
            return false;
        }

        LocalDateTime pocetak = pocetniDatum.getValue().atTime(dogadjaj.getPocetakDogadjaja().toLocalTime());
        if (postojiPreklpanje(pocetak, dogadjaj.getKrajDogadjaja(), dogadjaj.getLokacija())) {
            showError(pocetniDatum, "Već postoji događaj na istoj lokaciji");
            return false;
        }

        return true;
    }

    private boolean validateDatumKraja() {
        removeError(krajnjiDatum);

        if (krajnjiDatum.getValue() == null) {
            showError(krajnjiDatum, "Nedostaje unos!");
            return false;
        }

        if (krajnjiDatum.getValue().isBefore(dogadjaj.getPocetakDogadjaja().toLocalDate())) {
            showError(krajnjiDatum, "Datum kraj prije datuma početka!");
            return false;
        }

        LocalDateTime kraj = krajnjiDatum.getValue().atTime(dogadjaj.getKrajDogadjaja().toLocalTime());
        if (postojiPreklpanje(dogadjaj.getPocetakDogadjaja(), kraj, dogadjaj.getLokacija())) {
            showError(krajnjiDatum, "Već postoji događaj na istoj lokaciji");
            return false;
        }

        return true;
    }

    private boolean validateVrijemePocetka() {
        removeError(vrijemePocetka);

        if (vrijemePocetka.getText().isEmpty()) {
            showError(vrijemePocetka, "Nedostaje unos!");
            return false;
        }

        try {
            LocalTime pocetak = LocalTime.parse(vrijemePocetka.getText(), timeFormatter);

            if (!dogadjaj.getPocetakDogadjaja().toLocalDate().isBefore(dogadjaj.getKrajDogadjaja().toLocalDate())) {
                if (pocetak.isAfter(dogadjaj.getKrajDogadjaja().toLocalTime())) {
                    showError(vrijemePocetka, "Početak događaja ne može biti poslije kraja!");
                    return false;
                }
                
                LocalDateTime pocetakDogadjaja = dogadjaj.getPocetakDogadjaja().toLocalDate().atTime(pocetak);
                if (postojiPreklpanje(pocetakDogadjaja, dogadjaj.getKrajDogadjaja(), dogadjaj.getLokacija())) {
                    showError(vrijemePocetka, "Već postoji događaj na istoj lokaciji");
                    return false;
                }
            }

        } catch (Exception e) {
            System.out.println("Error parsing vrijeme pocetka" + e.getMessage());
            showError(vrijemePocetka, "Unos mora biti u formatu HH:mm");
            return false;
        }

        return true;
    }

    private boolean validateVrijemeKraja() {
        removeError(vrijemeKraja);

        if (vrijemeKraja.getText().isBlank()) {
            showError(vrijemeKraja, "Nedostaje unos!");
            return false;
        }

        try {
            LocalTime kraj = LocalTime.parse(vrijemeKraja.getText(), timeFormatter);

            if (!dogadjaj.getPocetakDogadjaja().toLocalDate().isBefore(dogadjaj.getKrajDogadjaja().toLocalDate())) {
                if (kraj.isBefore(dogadjaj.getPocetakDogadjaja().toLocalTime())) {
                    showError(vrijemeKraja, "Kraj događaja ne može biti prije početka!");
                }

                LocalDateTime krajDogadjaja = dogadjaj.getKrajDogadjaja().toLocalDate().atTime(kraj);
                if (postojiPreklpanje(dogadjaj.getPocetakDogadjaja(), krajDogadjaja, dogadjaj.getLokacija())) {
                    showError(vrijemeKraja, "Već postoji događaj na istoj lokaciji");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing vrijeme kraja: " + e.getMessage());
            showError(vrijemeKraja, "Unos mora biti u formatu HH:mm");
            return false;
        }

        return true;
    }

    private boolean validateMjesto() {
        removeError(mjestoCombo);
        removeError(lokacijaCombo);
        removeErrors(sektoriBox);

        if (mjestoCombo.getSelectionModel().getSelectedItem() == null) {
            showError(mjestoCombo, "Nedostaje unos!");
            return false;
        }

        if (!mjestoCombo.getSelectionModel().getSelectedItem().equals(dogadjaj.getMjesto())) {
            return validateLokacija();
        }

        return validateKarte(sektoriBox);
    }

    private boolean validateLokacija() {
        removeError(lokacijaCombo);
        removeErrors(sektoriBox);

        String selectedLokacija = lokacijaCombo.getSelectionModel().getSelectedItem();

        if (selectedLokacija == null) {
            showError(lokacijaCombo, "Nedostaje unos!");
            return false;
        } else {
            Lokacija lokacija = lokacijaService.pronadjiSveLokacijeZaMjesto(dogadjaj.getMjesto())
            .stream()
            .filter(l -> l.getNaziv().equals(selectedLokacija))
            .findFirst()
            .orElse(null);

            if (lokacija != null) {
                if (postojiPreklpanje(dogadjaj.getPocetakDogadjaja(), dogadjaj.getKrajDogadjaja(), lokacija)) {
                    showError(lokacijaCombo, "Postoji kolizija sa drugim događajem u istom terminu!");
                    return false;
                }
            }
        }
        
        return validateKarte(sektoriBox);
    }

    private boolean validateKarte(Button button) {
        HBox buttonRow = (HBox) button.getParent();
        VBox sektor = (VBox) buttonRow.getParent();
        return validateSektor(sektor);
    }

    private boolean validateKarte(VBox sektori) {
        boolean returnValue = true;

        for (Node node : sektori.getChildren()) {
            if (node instanceof VBox) {
                VBox sektor = (VBox) node;
                if (!validateSektor(sektor)) returnValue = false;
            }
        }
        return returnValue;
    }

    private boolean validateSektor(VBox sektor) {
        removeErrors(sektor);
        boolean returnValue = true;
        for (Node node : sektor.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                for (Node element : row.getChildren()) {
                    if (element instanceof TextField) {
                        TextField tf = (TextField) element;
                        if (tf.getText().isBlank()) {
                            showError(tf, "Nedostaje unos");
                            returnValue = false;
                        }
                    }
                }
            }
        } 
        return returnValue;
    }

    // MARK: - Support functions
    private boolean postojiPreklpanje(LocalDateTime pocetak, LocalDateTime kraj, Lokacija lokacija) {
        List<Dogadjaj> preklapanja = dogadjajService.pronadjiPreklapanja(pocetak, kraj, lokacija);
        return preklapanja.isEmpty() ? false : true;
    }

    private void clearBox(Button button) {
        if (button.getParent() instanceof HBox) {
            HBox buttonsRow = (HBox) button.getParent();
            if (buttonsRow.getParent() instanceof VBox) {
                VBox vbox = (VBox) buttonsRow.getParent();
                for (Node node : vbox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hBox = (HBox) node;
                        for (Node childNode : hBox.getChildren()) {
                            if (childNode instanceof TextField) {
                                ((TextField) childNode).setText("");
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateConfirmIcon(Button button) {
        button.setStyle("-fx-background-color: #0fdb68;");
    }

    private void resetConfirmIcon(Button button) {
        button.setStyle("-fx-background-color: #3875ce;");
    }

    private void showError(Control control, String errMessage) {
        control.setStyle("-fx-border-color: red;" +
                "-fx-border-width: 2px;");
        errorLabel.setText(errMessage);
        errorImage.setVisible(true);
        errorLabel.setVisible(true);
    }

    private void removeErrors(VBox sektor) {
        for (Node node : sektor.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                for (Node childNode : row.getChildren()) {
                    if (childNode instanceof TextField) {
                        removeError((TextField) childNode);
                    } 
                }
            } else if (node instanceof VBox) {
                removeErrors((VBox) node);
            }
        }
    }

    private void removeError(Control control) {
        control.setStyle("-fx-border-width: 0px;");
        errorLabel.setVisible(false);
        errorImage.setVisible(false);
    }

    private void showWarning(Control control, String warnMessage) {
        control.setStyle("-fx-border-color: #FFC107;" +
                "-fx-border-width: 2px;");
        warningLabel.setText(warnMessage);
        warningLabel.setVisible(true);
        warningImage.setVisible(true);
    }

    private void removeWarning(Control control) {
        control.setStyle("-fx-border-width: 0px;");
        warningLabel.setText("");
        warningImage.setVisible(false);
        warningLabel.setVisible(false);
    }

    private Stage getStage() {
        return (Stage) eventImage.getScene().getWindow();
    }
}
