package grupa5;

import java.beans.PersistenceDelegate;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.Sektor;
import grupa5.baza_podataka.services.DogadjajService;
import grupa5.baza_podataka.services.KartaService;
import grupa5.baza_podataka.services.LokacijaService;
import grupa5.baza_podataka.services.MjestoService;
import grupa5.baza_podataka.services.SektorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private DogadjajService dogadjajService;
    private LokacijaService lokacijaService;
    private MjestoService mjestoService;
    private SektorService sektorService;
    private KartaService kartaService;
    private Korisnik korisnik;
    private Dogadjaj dogadjaj;

    private String naziv;
    private String opis;
    private final String opcijaNovePodvrste = "Unesite novu podvrstu...";

    public void setDogadjajService(DogadjajService dogadjajService) {
        this.dogadjajService = dogadjajService;
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
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
        ;
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

        // Create and style the buttons
        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("clear-button");

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("confirm-button");

        // Create HBox for buttons
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
    void updateKrajniDatum(ActionEvent event) {

    }

    @FXML
    void updateInputs(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        updateConfirmIcon(sourceButton);

        switch (sourceButton.getId()) {
            case "nazivButton" -> updateNaziv();
            // case "opisButton" -> updateOpis();
            // case "vrstaButton" -> updateVrsta();
            // case "podvrstaButton" -> upatePodvrsta();
            // case "datumPocetkaButton" -> updateDatumPocetka();
            // case "datumKrajaButton" -> updateDatumKraja();
            // case "vrijemePocetkaButton" -> updateVrijemePocetka();
            // case "vrijemeKrajaButton" -> updateVrijemeKraja();
            // case "mjestoButton" -> updateMjesto();
            // case "lokacijaButton" -> updateLokacija();
            default -> {}
        }

    }

    private void updateNaziv() {
        showErrorBorder(nazivText);
    }

    // MARK: - Support functions
    private void updateConfirmIcon(Button button) {
        button.setStyle("-fx-background-color: #0fdb68;");
    }

    private void showErrorBorder(Control control) {
        control.setStyle("-fx-border-color: red;" +
                "-fx-border-width: 2px;");
    }

}