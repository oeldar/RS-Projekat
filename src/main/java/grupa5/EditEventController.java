package grupa5;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
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
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private List<KartaPrijedlog> prijedloziKarata = new ArrayList<>();
    private boolean dogadjajPromijenjen = false;
    private boolean lokacijaPromjenjena = false;
    private boolean samoKartePromijenjene = false;

    private MojiDogadjajiController mojiDogadjajiController;

    private String naziv;
    private String opis;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private Map<String, List<String>> subcategoriesMap = new HashMap<>() {{
        put("Muzika", Arrays.asList("Koncert", "Festival", "Svirka", "Mjuzikl", "Ostalo"));
        put("Kultura", Arrays.asList("Pozorište", "Izložba", "Kino", "Književnost", "Ostalo"));
        put("Sport", Arrays.asList("Fudbal", "Košarka", "Odbojka", "Ostalo"));
    }};

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

        String selectedVrsta = vrstaCombo.getSelectionModel().getSelectedItem();

        List<String> podvrste = subcategoriesMap.getOrDefault(selectedVrsta, Collections.emptyList());
        podvrstaCombo.getItems().addAll(podvrste);

        podvrstaCombo.getSelectionModel().select(dogadjaj.getPodvrstaDogadjaja());

        vrstaCombo.setOnAction(event -> changePodvrstaOptions());

        pocetniDatum.setValue(dogadjaj.getPocetakDogadjaja().toLocalDate());
        krajnjiDatum.setValue(dogadjaj.getKrajDogadjaja().toLocalDate());
        vrijemePocetka.setText(dogadjaj.getPocetakDogadjaja().toLocalTime().toString());
        vrijemeKraja.setText(dogadjaj.getKrajDogadjaja().toLocalTime().toString());

        configureMjestoComboBox();
        configureLokacija(dogadjaj.getMjesto());

        List<Karta> karte = dogadjaj.getKarte();
        for (Karta karta : karte) {
            Sektor sektor = karta.getSektor();
            addSektor(sektor, sektoriBox, karta);
        }

        showEventImage();
    }

    private void changePodvrstaOptions() {
        String selectedVrsta = vrstaCombo.getSelectionModel().getSelectedItem();
        podvrstaCombo.getItems().clear();
        List<String> podvrste = subcategoriesMap.getOrDefault(selectedVrsta, Collections.emptyList());
        podvrstaCombo.getItems().addAll(podvrste);
    }

    private void configureMjestoComboBox() {
        List<Mjesto> mjesta = mjestoService.pronadjiSvaMjesta();
        for (Mjesto mjesto : mjesta) {
            mjestoCombo.getItems().add(mjesto.getNaziv());
        }
        mjestoCombo.getSelectionModel().select(dogadjaj.getMjesto().getNaziv());

        mjestoCombo.setOnAction(event -> {
            sektoriBox.getChildren().clear();

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
                    addSektor(sektor, sektoriBox, null);
                }
            }

        });
    }

    private void addSektor(Sektor sektor, VBox sektoriVBox, Karta karta) {
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

        if (karta != null) {
            cijenaInput.setText(karta.getCijena().toString());
            maxBrojKartiInput.setText(String.valueOf(karta.getMaxBrojKartiPoKorisniku()));
            naplataRezervacijeInput.setText(karta.getNaplataOtkazivanjaRezervacije().toString());
            LocalDateTime poslednjiDatumZaRezervaciju = karta.getPoslednjiDatumZaRezervaciju();
            LocalDateTime pocetakDogadjaja = dogadjaj.getPocetakDogadjaja();
            long brojSati = Duration.between(poslednjiDatumZaRezervaciju, pocetakDogadjaja).toHours();
            brojSatiInput.setText(String.valueOf(brojSati));
            
            cijenaInput.setDisable(true);
            maxBrojKartiInput.setDisable(true);
            naplataRezervacijeInput.setDisable(true);
            brojSatiInput.setDisable(true);
        }
    
        HBox firstRow = new HBox(10, cijenaInput, maxBrojKartiInput);
        HBox secondRow = new HBox(10, naplataRezervacijeInput, brojSatiInput);
    
        HBox.setHgrow(cijenaInput, Priority.ALWAYS);
        HBox.setHgrow(maxBrojKartiInput, Priority.ALWAYS);
        HBox.setHgrow(naplataRezervacijeInput, Priority.ALWAYS);
        HBox.setHgrow(brojSatiInput, Priority.ALWAYS);
    
        Image editImage = new Image(getClass().getResourceAsStream("assets/icons/edit.png"));
        Image confirmImage = new Image(getClass().getResourceAsStream("assets/icons/confirm.png"));

        ImageView editImageView = new ImageView(editImage);
        editImageView.setFitWidth(25);
        editImageView.setFitHeight(19);
        editImageView.setPreserveRatio(true);

        ImageView confirmImageView = new ImageView(confirmImage);
        confirmImageView.setFitWidth(25);
        confirmImageView.setFitHeight(19);
        confirmImageView.setPreserveRatio(true);

        Button editButton = new Button();
        editButton.setGraphic(editImageView);
        editButton.getStyleClass().add("delete-buttom"); 
        editButton.setOnAction(event -> editBox(editButton));

        Button confirmButton = new Button();
        confirmButton.setGraphic(confirmImageView);
        confirmButton.getStyleClass().add("delete-buttom"); 
        confirmButton.setStyle("-fx-background-color: #3875ce;");
        confirmButton.setOnAction(event -> {
            if (areFieldsDisabled(cijenaInput, maxBrojKartiInput, naplataRezervacijeInput, brojSatiInput)) {
                return;
            }
            updateKarte(confirmButton);

            Double cijena = Double.parseDouble(cijenaInput.getText());
            Double naplataRezervacije = naplataRezervacijeInput.getText().isEmpty() ? 0.0 : Double.parseDouble(naplataRezervacijeInput.getText());
            Integer maxBrojKarti = Integer.parseInt(maxBrojKartiInput.getText());
            Integer brojSati = Integer.parseInt(brojSatiInput.getText());

            LocalDateTime poslednjiDatumZaRezervaciju = dogadjaj.getPocetakDogadjaja().minusHours(brojSati);
            KartaPrijedlog kartaPrijedlog = new KartaPrijedlog();
            kartaPrijedlog.setCijena(cijena);
            kartaPrijedlog.setDogadjajPrijedlog(dogadjajPrijedlog);
            kartaPrijedlog.setMaxBrojKartiPoKorisniku(maxBrojKarti);
            kartaPrijedlog.setNaplataOtkazivanjaRezervacije(naplataRezervacije);
            kartaPrijedlog.setPoslednjiDatumZaRezervaciju(poslednjiDatumZaRezervaciju);
            kartaPrijedlog.setSektor(sektor);

            prijedloziKarata.add(kartaPrijedlog);

            cijenaInput.setDisable(true);
            maxBrojKartiInput.setDisable(true);
            naplataRezervacijeInput.setDisable(true);
            brojSatiInput.setDisable(true);
            updateConfirmIcon(confirmButton);
        });

        HBox buttonsRow = new HBox(10, editButton, confirmButton);
        buttonsRow.setAlignment(Pos.CENTER_RIGHT);
    
        VBox sektorVBox = new VBox(10, sektorLabel, firstRow, secondRow, buttonsRow);
        sektoriVBox.getChildren().add(sektorVBox);
    
        Platform.runLater(() -> {
            sektoriVBox.requestLayout();
            sektoriVBox.layout();
            postaviVisinuVBox(sektoriVBox);
        });
    }    

    private boolean areFieldsDisabled(TextField... fields) {
        for (TextField field : fields) {
            if (!field.isDisabled()) {
                return false;
            }
        }
        return true;
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
        Image image = ImageSelector.selectEventImage(getStage());
        if (image != null) {
            VBox parentVbox = (VBox) eventImage.getParent();
            HBox parentBox = (HBox) parentVbox.getChildren().get(1);
            Button button = (Button) parentBox.getChildren().get(1);
            resetConfirmIcon(button);
            eventImage.setDisable(false);
            eventImage.setImage(image);
        }
    }

    @FXML
    void editInput(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();

        if (!((Button) event.getSource()).getId().equals("editOpisButton")) {
            HBox parentBox = (HBox) sourceButton.getParent();
            Button confirmButton = (Button) parentBox.getChildren().get(2);   
            resetConfirmIcon(confirmButton);
        }

        switch (sourceButton.getId()) {
            case "editNazivButton" -> nazivText.setDisable(false);
            case "editOpisButton" -> opisTextArea.setDisable(false);
            case "editVrstaButton" -> {
                vrstaCombo.setDisable(false);

                HBox parentBox = (HBox) podvrstaCombo.getParent();
                Button confirmButton = (Button) parentBox.getChildren().get(2);   
                resetConfirmIcon(confirmButton);
                podvrstaCombo.setDisable(false);
            }
            case "editPodvrstaButton" -> podvrstaCombo.setDisable(false);
            case "editPocetniDatumButton" -> {
                pocetniDatum.setDisable(false);

                HBox parentBox = (HBox) vrijemePocetka.getParent();
                Button confirmButton = (Button) parentBox.getChildren().get(2);   
                resetConfirmIcon(confirmButton);
                vrijemePocetka.setDisable(false);

                parentBox = (HBox) krajnjiDatum.getParent();
                confirmButton = (Button) parentBox.getChildren().get(2);   
                resetConfirmIcon(confirmButton);
                krajnjiDatum.setDisable(false);

                parentBox = (HBox) vrijemeKraja.getParent();
                confirmButton = (Button) parentBox.getChildren().get(2);   
                resetConfirmIcon(confirmButton);
                vrijemeKraja.setDisable(false);
            }
            case "editPocetnoVrijemeButton" -> vrijemePocetka.setDisable(false);
            case "editKrajnjiDatumButton" -> {
                krajnjiDatum.setDisable(false);

                HBox parentBox = (HBox) vrijemeKraja.getParent();
                Button confirmButton = (Button) parentBox.getChildren().get(2);   
                resetConfirmIcon(confirmButton);
                vrijemeKraja.setDisable(false);
            }
            case "editVrijemeKrajaButton" -> vrijemeKraja.setDisable(false);
            case "editMjestoButton" -> {
                mjestoCombo.setDisable(false);

                HBox parentBox = (HBox) lokacijaCombo.getParent();
                Button confirmButton = (Button) parentBox.getChildren().get(2);   
                resetConfirmIcon(confirmButton);
                lokacijaCombo.setDisable(false);
                enableKarte();
            }
            case "editLokacijaButton" -> {
                lokacijaCombo.setDisable(false);
                enableKarte();
            }
            default -> {
            }
        }
    }

    private void enableKarte() {
        for (Node sektorNode : sektoriBox.getChildren()) {
            if (sektorNode instanceof VBox sektorVBox) {
                for (Node child : sektorVBox.getChildren()) {
                    if (child instanceof HBox hBox) {
                        for (Node hBoxChild : hBox.getChildren()) {
                            if (hBoxChild instanceof TextField textField) {
                                textField.setDisable(false);
                            }
                        }
                    }
                }
            }
        }
    }
    

    @FXML
    void updateInputs(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();

        switch (sourceButton.getId()) {
            case "nazivButton" -> updateNaziv(sourceButton);
            case "opisButton" -> updateOpis(sourceButton);
            case "vrstaButton" -> updateVrsta(sourceButton);
            case "podvrstaButton" -> updatePodvrsta(sourceButton);
            case "datumPocetkaButton" -> updateDatumPocetka(sourceButton);
            case "datumKrajaButton" -> updateDatumKraja(sourceButton);
            case "vrijemePocetkaButton" -> updateVrijemePocetka(sourceButton);
            case "vrijemeKrajaButton" -> updateVrijemeKraja(sourceButton);
            case "mjestoButton" -> updateMjesto(sourceButton);
            case "lokacijaButton" -> updateLokacija(sourceButton);
            case "updateImageButton" -> updateImage(sourceButton);
            default -> {
            }
        }
    }

 
    private void updateNaziv(Button button) {
        if (nazivText.isDisabled()) return;
        if (!validateNaziv()) {
            return;
        }

        dogadjajPrijedlog.setNaziv(nazivText.getText());
        dogadjajPromijenjen = true;
        nazivText.setDisable(true);
        updateConfirmIcon(button);
    }

    private void updateOpis(Button button) {
        if (opisTextArea.isDisabled()) return;

        dogadjajPrijedlog.setOpis(opisTextArea.getText());
        dogadjajPromijenjen = true;
        opisTextArea.setDisable(true);
        updateConfirmIcon(button);
    }

    private void updateVrsta(Button button) {
        if (vrstaCombo.isDisabled()) return;
        if (!validateVrsta()) {
            return;
        }

        dogadjajPrijedlog.setVrstaDogadjaja(vrstaCombo.getSelectionModel().getSelectedItem());
        dogadjajPrijedlog.setPodvrstaDogadjaja(podvrstaCombo.getSelectionModel().getSelectedItem());
        dogadjajPromijenjen = true;
        vrstaCombo.setDisable(true);
        updateConfirmIcon(button);
    }

    private void updatePodvrsta(Button button) {
        if (podvrstaCombo.isDisabled()) return;
        if (!validatePodvrsta()) {
            return;
        }

        dogadjajPrijedlog.setPodvrstaDogadjaja(podvrstaCombo.getSelectionModel().getSelectedItem());
        dogadjajPromijenjen = true;
        podvrstaCombo.setDisable(true);
        updateConfirmIcon(button);
    }

    private void updateDatumPocetka(Button button) {
        if (pocetniDatum.isDisabled()) return;
    
        if (pocetniDatum.getValue() == null) {
            resetConfirmIcon(button);
            return;
        }
    
        if (!validateDatumPocetka()) {
            resetConfirmIcon(button);
            return;
        }
    
        if (krajnjiDatum.isDisabled() && vrijemePocetka.isDisabled() && vrijemeKraja.isDisabled() && lokacijaCombo.isDisabled()) {
            LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
            LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
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
    
            if (lokacija != null && postojiPreklapanje(pocetak, kraj, lokacija)) {
                resetConfirmIcon(button);
                showError(pocetniDatum, "Preklapanje sa drugim događajem...");
                return;
            }
        }
    
        LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
        dogadjajPrijedlog.setPocetakDogadjaja(pocetak);
        dogadjajPromijenjen = true;
        pocetniDatum.setDisable(true);
        updateConfirmIcon(button);
    }    

    private void updateDatumKraja(Button button) {
        if (krajnjiDatum.isDisabled()) return;
    
        if (krajnjiDatum.getValue() == null) {
            resetConfirmIcon(button);
            return;
        }
    
        if (!validateDatumKraja()) {
            resetConfirmIcon(button);
            return;
        }
    
        if (pocetniDatum.isDisabled() && vrijemePocetka.isDisabled() && vrijemeKraja.isDisabled() && lokacijaCombo.isDisabled()) {
            LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
            LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
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
    
            if (lokacija != null && postojiPreklapanje(pocetak, kraj, lokacija)) {
                resetConfirmIcon(button);
                showError(krajnjiDatum, "Preklapanje sa drugim događajem.");
                return;
            }
        }
    
        LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
        dogadjajPrijedlog.setKrajDogadjaja(kraj); // Assuming this is the correct field to set
        dogadjajPromijenjen = true;
        krajnjiDatum.setDisable(true);
        updateConfirmIcon(button);
    }
    

    private void updateVrijemePocetka(Button button) {
        if (vrijemePocetka.isDisabled()) return;
    
        if (vrijemePocetka.getText().isEmpty()) {
            resetConfirmIcon(button);
            return;
        }
    
        if (!validateVrijemePocetka()) {
            resetConfirmIcon(button);
            return;
        }
    
        if (pocetniDatum.isDisabled() && krajnjiDatum.isDisabled() && vrijemeKraja.isDisabled() && lokacijaCombo.isDisabled()) {
            LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
            LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
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
    
            if (lokacija != null && postojiPreklapanje(pocetak, kraj, lokacija)) {
                resetConfirmIcon(button);
                showError(vrijemePocetka, "Preklapanje sa drugim događajem.");
                return;
            }
        }
    
        LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
        dogadjajPrijedlog.setPocetakDogadjaja(pocetak);
        dogadjajPromijenjen = true;
        vrijemePocetka.setDisable(true);
        updateConfirmIcon(button);
    }
    

    private void updateVrijemeKraja(Button button) {
        if (vrijemeKraja.isDisabled()) return;
    
        if (vrijemeKraja.getText().isEmpty()) {
            resetConfirmIcon(button);
            return;
        }
    
        if (!validateVrijemeKraja()) {
            resetConfirmIcon(button);
            return;
        }
    
        if (pocetniDatum.isDisabled() && krajnjiDatum.isDisabled() && vrijemePocetka.isDisabled() && lokacijaCombo.isDisabled()) {
            LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
            LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
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
    
            if (lokacija != null && postojiPreklapanje(pocetak, kraj, lokacija)) {
                resetConfirmIcon(button);
                showError(vrijemeKraja, "Preklapanje sa drugim događajem.");
                return;
            }
        }
    
        LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
        dogadjajPrijedlog.setKrajDogadjaja(kraj); // Assuming this is the correct field to set
        dogadjajPromijenjen = true;
        vrijemeKraja.setDisable(true);
        updateConfirmIcon(button);
    }
    

    private void updateMjesto(Button button) {
        if (mjestoCombo.isDisabled()) return;
        if (!validateMjesto()) {
            resetConfirmIcon(button);
            return;
        }

        String selectedMjesto = mjestoCombo.getSelectionModel().getSelectedItem();

        Mjesto mjesto = mjestoService.pronadjiSvaMjesta().stream()
                                        .filter(m -> m.getNaziv().equals(selectedMjesto))
                                        .findFirst()
                                        .orElse(null);

        dogadjajPrijedlog.setMjesto(mjesto);
        dogadjajPromijenjen = true;
        mjestoCombo.setDisable(true);
        updateConfirmIcon(button);
    }

    private void updateLokacija(Button button) {
        if (lokacijaCombo.isDisabled()) return;
        if (!validateLokacija()) {
            resetConfirmIcon(button);
            return;
        }

        if (!mjestoCombo.isDisabled()) {
            showError(mjestoCombo, "Morate prvo sačuvati mjesto!");
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
    
        dogadjajPrijedlog.setLokacija(lokacija);
        dogadjajPromijenjen = true;
        lokacijaPromjenjena = true;
        prijedloziKarata.clear();
        lokacijaCombo.setDisable(true);
        updateConfirmIcon(button);
    }

    private void updateKarte(Button button) {
        if (!validateKarte(button)) {
            resetConfirmIcon(button);
            return;
        }

        if (!lokacijaPromjenjena) {
            samoKartePromijenjene = true;
        }
    }

    private void updateImage(Button button) {
        if (eventImage.isDisabled()) return;

        Image image = eventImage.getImage();
        if (image != null) {
            String imageUrl = image.getUrl();
            if (imageUrl != null) {
                dogadjajPrijedlog.setPutanjaDoSlike(imageUrl);
            }
        }
        
        dogadjajPromijenjen = true;
        eventImage.setDisable(true);
        updateConfirmIcon(button);
    }

   
    @FXML
    void potvrdiDogadjaj(ActionEvent event) {
        if (!areAllFieldsDisabled()) {
            showError(null, "Sva polja moraju biti sačuvana prije potvrde.");
            return;
        }

        if (!validateKarte(sektoriBox)) {
            return;
        }

        if (dogadjajPromijenjen) {
            dogadjajPrijedlog.setOriginalniDogadjaj(dogadjaj);
            dogadjajPrijedlogService.kreirajDogadjajPrijedlog(dogadjajPrijedlog);
            
            if (lokacijaPromjenjena) {
                prijedloziKarata.sort(Comparator.comparing(prijedlog -> prijedlog.getSektor().getSektorID()));
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

    private boolean areAllFieldsDisabled() {
        return nazivText.isDisabled() && opisTextArea.isDisabled() && vrstaCombo.isDisabled() && podvrstaCombo.isDisabled() && 
                pocetniDatum.isDisabled() && krajnjiDatum.isDisabled() && vrijemePocetka.isDisabled() && eventImage.isDisabled() &&
               vrijemeKraja.isDisabled() && lokacijaCombo.isDisabled() && mjestoCombo.isDisabled() &&
               sektoriBox.getChildren().stream().allMatch(node -> {
                   if (node instanceof VBox) {
                       VBox sektorVBox = (VBox) node;
                       HBox firstRow = (HBox) sektorVBox.getChildren().get(1);
                       TextField cijenaInput = (TextField) firstRow.getChildren().get(0);
                       TextField maxBrojKartiInput = (TextField) firstRow.getChildren().get(1);
                       HBox secondRow = (HBox) sektorVBox.getChildren().get(2);
                       TextField naplataRezervacijeInput = (TextField) secondRow.getChildren().get(0);
                       TextField brojSatiInput = (TextField) secondRow.getChildren().get(1);
                       return cijenaInput.isDisabled() && maxBrojKartiInput.isDisabled() &&
                              naplataRezervacijeInput.isDisabled() && brojSatiInput.isDisabled();
                   }
                   return true;
               });
    }

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
        if (pocetniDatum.getValue() == null || vrijemePocetka.getText().isEmpty()) return false;
    
        LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
        if (pocetak.isBefore(LocalDateTime.now())) {
            showError(pocetniDatum, "Datum početka ne može biti u prošlosti!");
            return false;
        }
    
        if (krajnjiDatum.getValue() != null && vrijemeKraja.getText() != null) {
            LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
            if (pocetak.isAfter(kraj)) {
                showError(pocetniDatum, "Datum početka ne može biti nakon datuma kraja!");
                return false;
            }
        }
    
        return true;
    }    

    private boolean validateDatumKraja() {
        if (krajnjiDatum.getValue() == null || vrijemeKraja.getText().isEmpty()) return false;
    
        LocalDateTime kraj = LocalDateTime.of(krajnjiDatum.getValue(), LocalTime.parse(vrijemeKraja.getText()));
        if (kraj.isBefore(LocalDateTime.now())) {
            showError(krajnjiDatum, "Datum kraja ne može biti u prošlosti!");
            return false;
        }
    
        if (pocetniDatum.getValue() != null && vrijemePocetka.getText() != null) {
            LocalDateTime pocetak = LocalDateTime.of(pocetniDatum.getValue(), LocalTime.parse(vrijemePocetka.getText()));
            if (kraj.isBefore(pocetak)) {
                showError(krajnjiDatum, "Datum kraja ne može biti prije datuma početka!");
                return false;
            }
        }
    
        return true;
    }    

    private boolean validateVrijemePocetka() {
        if (vrijemePocetka.getText().isEmpty()) return false;

        try {
            LocalTime.parse(vrijemePocetka.getText());
        } catch (DateTimeParseException e) {
            showError(vrijemePocetka, "Unos mora biti u formatu HH:mm!");
            return false;
        }

        return true;
    }


    private boolean validateVrijemeKraja() {
        if (vrijemeKraja.getText().isEmpty()) return false;
    
        try {
            LocalTime.parse(vrijemeKraja.getText());
        } catch (DateTimeParseException e) {
            showError(vrijemeKraja, "Unos mora biti u formatu HH:mm!");
            return false;
        }
    
        return true;
    }    

    private boolean validateMjesto() {
        if (mjestoCombo.getSelectionModel().getSelectedItem() == null) {
            showError(mjestoCombo, "Morate izabrati mjesto!");
            return false;
        }
    
        return true;
    }    

    private boolean validateLokacija() {
        if (lokacijaCombo.getSelectionModel().getSelectedItem() == null) {
            showError(lokacijaCombo, "Morate izabrati lokaciju!");
            return false;
        }
    
        return true;
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
    private boolean postojiPreklapanje(LocalDateTime pocetak, LocalDateTime kraj, Lokacija lokacija) {
        List<Dogadjaj> preklapanja = dogadjajService.pronadjiPreklapanja(pocetak, kraj, lokacija);
        if (preklapanja.isEmpty()) {
            return false;
        }
        if (preklapanja.size() == 1 && preklapanja.get(0).getDogadjajID().equals(dogadjaj.getDogadjajID())){
            return false;
        }
        return true;
    }

    private void editBox(Button button) {
        if (button.getParent() instanceof HBox) {
            HBox buttonsRow = (HBox) button.getParent();
            if (buttonsRow.getParent() instanceof VBox) {
                VBox vbox = (VBox) buttonsRow.getParent();
                for (Node node : vbox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hBox = (HBox) node;
                        for (Node childNode : hBox.getChildren()) {
                            if (childNode instanceof TextField) {
                                ((TextField) childNode).setDisable(false);
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
        if (control != null) {
            control.setStyle("-fx-border-color: red;" +
                "-fx-border-width: 2px;");
        }
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
