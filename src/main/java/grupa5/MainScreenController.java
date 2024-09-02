package grupa5;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajScheduler;
import grupa5.baza_podataka.DogadjajService;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Korisnik.TipKorisnika;
import grupa5.support_classes.ImageSelector;
import grupa5.baza_podataka.KorisnikService;
import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.KupovinaService;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.MjestoService;
import grupa5.baza_podataka.Novcanik;
import grupa5.baza_podataka.NovcanikService;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.RezervacijaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainScreenController {
    private static final String PERSISTENCE_UNIT_NAME = "HypersistenceOptimizer";
    private static final String EVENT_CARD_FXML = "views/event-card.fxml";
    // private static final String EVENT_DETAILS_FXML = "views/event-details.fxml";

    private EntityManagerFactory emf;
    private DogadjajService dogadjajService;
    private DogadjajScheduler dogadjajScheduler;
    private MjestoService mjestoService;
    private KorisnikService korisnikService;
    private NovcanikService novcanikService;
    private RezervacijaService rezervacijaService;
    private KupovinaService kupovinaService;

    @FXML
    private Label testLabel;
    private Stage stage;

    @FXML
    private Button sviDogadjajiBtn, muzikaBtn, kulturaBtn, sportBtn, ostaloBtn;
    @FXML
    private ImageView sviDogadjajiImg, muzikaImg, kulturaImg, sportImg, ostaloImg;
    @FXML
    private StackPane contentStackPane;
    @FXML
    private AnchorPane menuAnchorPane;
    @FXML
    private GridPane eventsGridPane;
    @FXML
    private Button goBackBtn;
    @FXML
    private ImageView backIcon;
    @FXML
    private Label categoryTitle;
    @FXML
    private FlowPane filtersFlowPane;
    @FXML
    private AnchorPane searchBarPane, eventDetailsPane;
    @FXML
    private VBox eventsVBox;
    @FXML
    private TextField searchInput;
    @FXML
    private Label imeKorisnikaLbl;
    @FXML
    private Label korisnickoImeLbl;
    @FXML
    private Label tipKorisnikaLbl;
    @FXML
    private ImageView korisnikImg;
    @FXML
    private HBox korisnikPodaci;
    @FXML
    private Button novcanikKupcaLbl;
    @FXML
    private ImageView novcanikKupcaImg;
    @FXML
    private Button odjavaBtn;
    @FXML
    private Button prijavaBtn;
    @FXML
    private Button registracijaBtn;
    @FXML
    private Label mojProfilLbl;
    @FXML
    private VBox mojProfilVbox;
    @FXML
    private Button kupljeneKarteBtn;
    @FXML
    private Button rezervisaneKarteBtn;
    @FXML
    private Button profilBtn, rezervisaneBtn, kupljeneBtn, dogadjajiBtn, korisniciBtn, lokacijeBtn, mojiDogadjajiBtn;
    @FXML
    private ImageView profilImg, rezervisaneImg, kupljeneImg, dogadjajiImg, korisniciImg, lokacijeImg, mojiDogadjajiImg;

    private Stack<Node> viewHistory = new Stack<>();
    private List<Button> categoryButtons;
    private List<Button> userProfileButtons;
    private Map<Button, ImageView> buttonToImageMap;
    //public boolean hasViewHistory() { return !viewHistory.isEmpty(); }

    private String currentButton;
    private Button currentCategoryButton;

    private String currentCategory;
    int brojSvihDogadjaja;
    int brojDogadjajaPoStranici = 6;
    List<List<Dogadjaj>> pages = new ArrayList<>();
    @FXML
    private Button prevPageBtn, nextPageBtn;

    int currentPage = 0;
    List<Dogadjaj> currentDogadjaji;

    private List<Mjesto> selectedLocations = new ArrayList<>();
    private LocalDate selectedStartDate;
    private LocalDate selectedEndDate;
    private BigDecimal selectedStartPrice;
    private BigDecimal selectedEndPrice;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    TipKorisnika tipKorisnika = null;
    Korisnik korisnik = null;
    Double stanjeNovcanika;
    private String loggedInUsername;
    

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setTipKorisnika(String tipKorisnikaString) {
        if (tipKorisnikaString != null && !tipKorisnikaString.equals("")) {
            this.tipKorisnika = TipKorisnika.valueOf(tipKorisnikaString);
        }
    }

    public void setStanjeNovcanika(Double stanjeNovcanika) {
        this.stanjeNovcanika = stanjeNovcanika;
        novcanikKupcaLbl.setText(String.format("Novčanik: %.2f KM", stanjeNovcanika));
    }

    public void setImage(ImageView imageView) {
        korisnikImg = imageView;
    }

    @FXML
    public void initialize(){
        currentCategoryButton = sviDogadjajiBtn;
        currentButton = "";
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            dogadjajService = new DogadjajService(emf);
            dogadjajScheduler = new DogadjajScheduler(dogadjajService);
            mjestoService = new MjestoService(emf);
            korisnikService = new KorisnikService(emf);
            novcanikService = new NovcanikService(emf);
            rezervacijaService = new RezervacijaService(emf);
            kupovinaService = new KupovinaService(emf);      
        } catch (Exception e) {
            System.err.println("Failed to initialize persistence unit: " + e.getMessage());
            return;
        }

        currentDogadjaji = dogadjajService.pronadjiDogadjajeSaFilterom(null, null, selectedStartDate, selectedEndDate, selectedStartPrice, selectedEndPrice, selectedLocations);
        loadInitialEvents();

        if (tipKorisnika == null) {
            initializePosjetitelja();
        } else if (tipKorisnika.equals(TipKorisnika.KORISNIK)){
            initializePosjetitelja();
            prikaziKorisnika();
        } else if (tipKorisnika.equals(TipKorisnika.ORGANIZATOR)) {
            // initializeOrganizatora();
        } else {
            // initializeAdministratora();
        }
    }


    public void initializePosjetitelja() {
        buttonToImageMap = new HashMap<>();

        currentCategory = "Svi događaji";

        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            prikaziDogadjajePoFilteru();
        });

        filtersFlowPane.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button filterButton = (Button) node;
                filterButton.setOnAction(event -> prikaziDogadjajePoFilteru());
            }
        });

        currentDogadjaji = dogadjajService.pronadjiDogadjajeSaFilterom(null, currentCategory, selectedStartDate, selectedEndDate, selectedStartPrice, selectedEndPrice, selectedLocations);
       

        setupUserProfileButtons();
        setupCategoryButtons();
        setupIcons();
        loadInitialEvents();

        Tooltip nextTooltip = new Tooltip("Pritisni za još događaja.");
        nextPageBtn.setTooltip(nextTooltip);

        Tooltip prevTooltip = new Tooltip("Pritisni za prethodne događaje.");
        prevPageBtn.setTooltip(prevTooltip);
        
        nextPageBtn.setOnAction(event -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                prikaziStranicu(currentPage);
            }
        });
        
        prevPageBtn.setOnAction(event -> {
            if (currentPage > 0) {
                currentPage--;
                prikaziStranicu(currentPage);
            }
        });
    }

    public void prikaziKorisnika() {
        Korisnik korisnik = korisnikService.pronadjiKorisnika(loggedInUsername);
    
        if (korisnik != null) {
            imeKorisnikaLbl.setText(korisnik.getIme() + " " + korisnik.getPrezime());
            korisnickoImeLbl.setText("@" + korisnik.getKorisnickoIme());
            tipKorisnikaLbl.setText(tipKorisnika.toString());
    
            if (tipKorisnika.equals(TipKorisnika.KORISNIK)) {
                Novcanik novcanik = novcanikService.pronadjiNovcanik(korisnik.getKorisnickoIme());
                stanjeNovcanika = novcanik.getStanje();
                novcanikKupcaLbl.setText(String.format("Novčanik: %.2f KM", stanjeNovcanika));
            }
    
            String imagePath = korisnik.getPutanjaDoSlike();
            if (imagePath == null || imagePath.isEmpty()) {
                // Postavi default sliku na osnovu tipa korisnika
                imagePath = "/grupa5/assets/users_photos/" + tipKorisnika.toString().toLowerCase() + ".png";
            }
    
            try (InputStream inputStream = getClass().getResourceAsStream(imagePath)) {
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    korisnikImg.setImage(image);
                    korisnikImg = ImageSelector.clipToCircle(korisnikImg, 35);
                } else {
                    // Postavi default sliku ako resurs nije pronađen
                    korisnikImg.setImage(new Image("/grupa5/assets/users_photos/" + tipKorisnika.toString().toLowerCase() + ".png"));
                    korisnikImg = ImageSelector.clipToCircle(korisnikImg, 35);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Postavi default sliku u slučaju greške pri učitavanju
                korisnikImg.setImage(new Image("/grupa5/assets/users_photos/" + tipKorisnika.toString().toLowerCase() + ".png"));
                korisnikImg = ImageSelector.clipToCircle(korisnikImg, 35);
            }
        } else {
            imeKorisnikaLbl.setText("N/A");
            korisnickoImeLbl.setText("N/A");
            tipKorisnikaLbl.setText("N/A");
            // Postavi default sliku u slučaju da korisnik ne postoji
            korisnikImg.setImage(new Image("/grupa5/assets/users_photos/" + tipKorisnika.toString().toLowerCase() + ".png"));
            korisnikImg = ImageSelector.clipToCircle(korisnikImg, 35);

            novcanikKupcaLbl.setText("N/A");
        }
    }    


    private void prikaziDogadjajePoFilteru() {
        String naziv = searchInput.getText().trim();
        String vrstaDogadjaja = currentCategory.equals("Svi događaji") ? null : currentCategory;
        LocalDate datumOd = null;
        LocalDate datumDo = null;
        List<Mjesto> mjesta = new ArrayList<>();
        BigDecimal cijenaOd = null;
        BigDecimal cijenaDo = null;
        
        for (Node node : filtersFlowPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                String buttonId = button.getId();
                String buttonText = extractText(buttonId);
                
                if (buttonId != null) {
                    try {
                        if (buttonId.startsWith("dateButton")) {
                            String[] dates = buttonText.split(" - ");
                            datumOd = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                            datumDo = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                        } else if (buttonId.startsWith("priceButton")) {
                            String[] prices = buttonText.replace("KM", "").replace("od ", "").split(" do ");
                            cijenaOd = new BigDecimal(prices[0].trim());
                            cijenaDo = new BigDecimal(prices[1].trim());
                        } else if (buttonId.startsWith("locationButton")) {
                            Mjesto mjesto = mjestoService.pronadjiMjestoPoNazivu(buttonText);
                            if (mjesto != null) {
                                mjesta.add(mjesto);
                            }
                        }
                    } catch (DateTimeParseException | NumberFormatException e) {
                        System.err.println("Error parsing filter value: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error processing filter button: " + e.getMessage());
                    }
                }
            }
        }

        pages.clear();
        currentPage = 0;
        currentDogadjaji = dogadjajService.pronadjiDogadjajeSaFilterom(
                naziv, vrstaDogadjaja, datumOd, datumDo, cijenaOd, cijenaDo, mjesta);
        //clearFilters();

        if(currentDogadjaji.size() == 0) {
            eventsGridPane.getChildren().clear();
            return;
        }

        for (int i = 0; i < currentDogadjaji.size(); i += brojDogadjajaPoStranici) {
            pages.add(currentDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, currentDogadjaji.size())));
        }

        prikaziStranicu(0);
    }

    public void clearFilters(){
        selectedStartDate = null;
        selectedEndDate = null;
        selectedStartPrice = null;
        selectedEndPrice = null;
        selectedLocations = null;
    }

    public String extractText(String input) {
        int hyphenIndex = input.indexOf('-');
        if (hyphenIndex != -1 && hyphenIndex + 1 < input.length()) {
            return input.substring(hyphenIndex + 1);
        }
        return "";
    }

    private void setupCategoryButtons() {
        categoryButtons = List.of(sviDogadjajiBtn, muzikaBtn, kulturaBtn, sportBtn, ostaloBtn);
        categoryButtons.forEach(button -> button.setOnAction(this::handleCategoryButtonAction));
        setActiveButton(sviDogadjajiBtn);
    }

    private void setupUserProfileButtons() {
        userProfileButtons = List.of(profilBtn, rezervisaneBtn, kupljeneBtn, dogadjajiBtn, korisniciBtn, lokacijeBtn, mojiDogadjajiBtn);
        userProfileButtons.forEach(button -> button.setOnAction(this::handleUserProfileButtonAction));
    }

    // Ova fja setupuje sve ikonice iako nisu kategorija.
    private void setupIcons() {
        buttonToImageMap.put(sviDogadjajiBtn, sviDogadjajiImg);
        buttonToImageMap.put(muzikaBtn, muzikaImg);
        buttonToImageMap.put(kulturaBtn, kulturaImg);
        buttonToImageMap.put(sportBtn, sportImg);
        buttonToImageMap.put(ostaloBtn, ostaloImg);
        buttonToImageMap.put(profilBtn, profilImg);
        buttonToImageMap.put(rezervisaneBtn, rezervisaneImg);
        buttonToImageMap.put(kupljeneBtn, kupljeneImg);
        buttonToImageMap.put(dogadjajiBtn, dogadjajiImg);
        buttonToImageMap.put(korisniciBtn, korisniciImg);
        buttonToImageMap.put(lokacijeBtn, lokacijeImg);
        buttonToImageMap.put(mojiDogadjajiBtn, mojiDogadjajiImg);
    }

    private void loadInitialEvents() {
        currentPage = 0;
        pages.clear();
        brojSvihDogadjaja = currentDogadjaji.size();
        System.out.println(brojSvihDogadjaja);

        for (int i = 0; i < brojSvihDogadjaja; i += brojDogadjajaPoStranici) {
            pages.add(currentDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, brojSvihDogadjaja)));
        }

        prikaziStranicu(0);
    }


    @FXML
    void loginBtnClicked(ActionEvent event) {
        openModal("login", "Prijava", 950, 700);
    }

    @FXML
    void signUpBtnClicked(ActionEvent event) {
        openModal("registration-view", "Registracija", 1000, 700);
    }

    @FXML
    void organizujDogadjaj(ActionEvent event) {
        openModal("organizacija", "Organizacija", 1100, 687);
    }

    @FXML
    void dodajLokaciju(ActionEvent event) {
        openModal("dodajLokaciju", "Dodavanje lokacije", 795, 432);
    }

    @FXML
    void openUserInfo(MouseEvent event) {
        openModal("userInfo", "Korisničke informacije", 800, 600);
    }

    @FXML
    void logoutBtnClicked(ActionEvent event) {
        loggedInUsername = null;
        tipKorisnika = null;
        korisnik = null;
        updateUIForLoggedOutUser();
    }

    private void openModal(String fxmlFile, String title, double width, double height) {

        if (stage == null || !stage.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/" + fxmlFile + ".fxml"));
                Parent root = loader.load();

                switch (fxmlFile) {
                    case "login" -> {
                        LoginController loginController = loader.getController();
                        loginController.setMainController(this);
                    }
                    case "organizacija" -> {
                        OrganizacijaController organizacijaController = loader.getController();
                        organizacijaController.setEntityManagerFactory(emf);
                        organizacijaController.setKorisnik(korisnik);
                    }
                    case "userInfo" -> {
                        UserInformationController userInformationController = loader.getController();
                        userInformationController.setKorisnik(korisnik);
                        userInformationController.setMainScreenController(this);
                    }
                }

                stage = new Stage();
                stage.setTitle(title);
                stage.setScene(new Scene(root, width, height));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOnCloseRequest(e -> stage = null);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            stage.toFront();
        }
    }

    private void handleUserProfileButtonAction(ActionEvent event) {
        showBackButton();
        Button clickedButton = (Button) event.getSource();
        clickedButton.setDisable(true);
        setActiveUserProfileButton(clickedButton);
        String profileOption = clickedButton.getText();
        currentButton = profileOption;

        openProfileOptionScreen(profileOption, event);

        Platform.runLater(() -> clickedButton.setDisable(false));

    }    

    private void openProfileOptionScreen(String option, ActionEvent event) {
        switch (option) {
            case "Rezervisane karte" -> openReservedCards(event);
            case "Kupljene karte" -> openBoughtCards(event);
            case "Moji događaji" -> openMojiDogadjaji(event);
            case "Događaji" -> openEventsRequests(event);
            case "Korisnici" -> openUsersRequests(event);
            case "Lokacije" -> {}
            default -> {}
        }
    }

    private void handleCategoryButtonAction(ActionEvent event) {
        //hansearchInput.clear();
        Button clickedButton = (Button) event.getSource();
        String category = clickedButton.getText();
        currentCategory = category;
        currentCategoryButton = clickedButton;

        // clearFilters(); 

        if (category.equals("Svi događaji")) {
            loadInitialEvents();
            prikaziDogadjajePoFilteru();
            setActiveButton(clickedButton);
            while (viewHistory.size() > 1){
                viewHistory.pop();
            }
            goBack();
            return;
        }

        currentDogadjaji = dogadjajService.pronadjiDogadjajeSaFilterom(null, category, selectedStartDate, selectedEndDate, selectedStartPrice, selectedEndPrice, selectedLocations);
        // System.out.println("Ovoliko je dogadjaja:" + currentDogadjaji.size());
        pages.clear();
        currentPage = 0;

        for (int i = 0; i < currentDogadjaji.size(); i += brojDogadjajaPoStranici) {
            pages.add(currentDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, currentDogadjaji.size())));
        }

        prikaziDogadjajePoFilteru();
        setActiveButton(clickedButton);

        while (viewHistory.size() > 1){
            viewHistory.pop();
        }
        goBack();
        viewHistory.clear();
    }

    @FXML
    private void openMojiDogadjaji(ActionEvent event) {
        goBackBtn.setVisible(true);
        backIcon.setVisible(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mojiDogadjaji.fxml"));
            Parent view = loader.load();

            // Dodaj trenutni prikaz u historiju
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            MojiDogadjajiController mojiDogadjajiController = loader.getController();
            mojiDogadjajiController.setMainScreenController(this);
            mojiDogadjajiController.setDogadjajService(dogadjajService);
            Korisnik korisnik = korisnikService.pronadjiKorisnika(loggedInUsername);
            List<Dogadjaj> dogadjaji = dogadjajService.pronadjiDogadjajePoKorisniku(korisnik);
    
            mojiDogadjajiController.setDogadjaji(dogadjaji);
    
    
            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEventsRequests(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/requests-events.fxml"));
            Parent view = loader.load();

            // Store the current view before switching
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            EventsRequestsController eventsRequestsController = loader.getController();
            eventsRequestsController.setMainScreenController(this);

            // Fetch the list of event requests (assuming a method exists in your service)
            List<Dogadjaj> zahtjeviZaDogadjaje = dogadjajService.pronadjiNeodobreneDogadjaje();

            eventsRequestsController.setNeodobreniDogadjaji(zahtjeviZaDogadjaje);
            eventsRequestsController.setDogadjajService(dogadjajService);

            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUsersRequests(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/users-requests.fxml"));
            Parent view = loader.load();

            // Store the current view before switching
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            RequestsForUsersController requestsForUsersController = loader.getController();
            requestsForUsersController.setMainScreenController(this);

            // Fetch the list of user requests (assuming a method exists in your service)
            List<Korisnik> zahtjeviZaKorisnike = korisnikService.pronadjiNeodobreneKorisnike();

            requestsForUsersController.setNeodobreniKorisnici(zahtjeviZaKorisnike);
            requestsForUsersController.setKorisnikService(korisnikService);
            requestsForUsersController.setNovcanikService(novcanikService);

            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void setActiveUserProfileButton(Button activeButton) {
        userProfileButtons.forEach(button -> {
            ImageView img = buttonToImageMap.get(button);
            if (img != null) {
                img.setVisible(false);
            }
            button.getStyleClass().setAll("category-button");
        });

        categoryButtons.forEach(button -> {
            ImageView img = buttonToImageMap.get(button);
            if (img != null) {
                img.setVisible(false);
            }
            button.getStyleClass().setAll("category-button");
        });

        ImageView activeImg = buttonToImageMap.get(activeButton);
        if (activeImg != null) {
            activeImg.setVisible(true);
        }
        activeButton.getStyleClass().setAll("category-button-active");
    }

    private void setActiveButton(Button activeButton) {

        userProfileButtons.forEach(button -> {
            ImageView img = buttonToImageMap.get(button);
            if (img != null) {
                img.setVisible(false);
            }
            button.getStyleClass().setAll("category-button");
        });

        categoryTitle.setText(activeButton.getText());
        categoryButtons.forEach(button -> {
            ImageView img = buttonToImageMap.get(button);
            if (img != null) {
                img.setVisible(false);
            }
            button.getStyleClass().setAll("category-button");
        });

        ImageView activeImg = buttonToImageMap.get(activeButton);
        if (activeImg != null) {
            activeImg.setVisible(true);
        }
        activeButton.getStyleClass().setAll("category-button-active");
    }

    @FXML
    private ImageView nextIcon, prevIcon;

    private void prikaziStranicu(int pageIndex) {

        if (pages == null || pages.isEmpty() || pageIndex < 0 || pageIndex >= pages.size()) {
            System.err.println("Stranica je prazna");
            return;
        }

        if (pageIndex < pages.size() - 1) {
            nextPageBtn.setVisible(true);  // Prikazivanje dugmeta
            nextIcon.setVisible(true);
        } else {
            nextPageBtn.setVisible(false); // Sakrivanje dugmeta
            nextIcon.setVisible(false);
        }
    
        // Provera da li postoji prethodna stranica
        if (pageIndex > 0) {
            prevPageBtn.setVisible(true);  // Prikazivanje dugmeta
            prevIcon.setVisible(true);
        } else {
            prevPageBtn.setVisible(false); // Sakrivanje dugmeta
            prevIcon.setVisible(false);
        }

        prikaziDogadjaje(pages.get(pageIndex));
    }

    private void prikaziDogadjaje(List<Dogadjaj> dogadjaji) {
        eventsGridPane.getChildren().clear(); // Očistiti prethodne događaje

    
        if (dogadjaji == null || dogadjaji.isEmpty()) {
            System.out.println("Nema događaja za prikaz.");
            return;
        }

        int row = 0;
        int col = 0;
    
        for (Dogadjaj dogadjaj : dogadjaji) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(EVENT_CARD_FXML));
                AnchorPane eventCard = loader.load();
                EventCardController controller = loader.getController();
                controller.setDogadjaj(dogadjaj);
                controller.setMainScreenController(this);
    
                eventsGridPane.add(eventCard, col, row);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        
    void loadDogadjajView(Dogadjaj dogadjaj) {
        if (dogadjaj == null) {
            System.out.println("Dogadjaj je null.");
            return;
        }
    
        showBackButton();
        loadView("event-details.fxml", dogadjaj);
    }
    
    private void loadView(String fxmlFile, Dogadjaj dogadjaj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/" + fxmlFile));
            Parent view = loader.load();
            
            // Store the current view before switching
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }
            EventDetailsController eventDetailsController = loader.getController();
            eventDetailsController.setParentController(this);

            // Configure the controller
            if (dogadjaj != null) {
                eventDetailsController.setDogadjaj(dogadjaj);
                eventDetailsController.setKorisnik(korisnikService.pronadjiKorisnika(loggedInUsername));
            } else {
                System.out.println("Dogadjaj je null u loadView.");
            }
    
            // Add the view with slide transition
            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openReservedCards(ActionEvent event) {

        Button sourceButton = (Button) event.getSource();
        sourceButton.setDisable(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/reserved-cards.fxml"));
            Parent view = loader.load();
            
            // Store the current view before switching
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            ReservedCardsController reservedCardsController = loader.getController();
            reservedCardsController.setMainScreenController(this);
            Korisnik korisnik = korisnikService.pronadjiKorisnika(loggedInUsername);
            List<Rezervacija> rezervacije = rezervacijaService.pronadjiAktivneRezervacijePoKorisniku(korisnik);

            reservedCardsController.setRezervacije(rezervacije);


            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

        goBackBtn.setVisible(true);
        backIcon.setVisible(true);

        Platform.runLater(() -> sourceButton.setDisable(false));
    }

    @FXML
    private void openBoughtCards(ActionEvent event) {

        Button sourceButton = (Button) event.getSource();
        sourceButton.setDisable(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/bought-cards.fxml"));
            Parent view = loader.load();
            
            // Store the current view before switching
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            BoughtCardsController boughtCardsController = loader.getController();
            boughtCardsController.setMainScreenController(this);
            Korisnik korisnik = korisnikService.pronadjiKorisnika(loggedInUsername);
            List<Kupovina> kupovine = kupovinaService.pronadjiKupovinePoKorisniku(korisnik);

            boughtCardsController.setKupovine(kupovine);


            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

        goBackBtn.setVisible(true);
        backIcon.setVisible(true);

        Platform.runLater(() -> sourceButton.setDisable(false));
    }

    @FXML
    private void addWithSlideTransition(Node newNode) {
        Node oldNode = contentStackPane.getChildren().isEmpty() ? null : contentStackPane.getChildren().get(0);
        double transitionDistance = contentStackPane.getWidth() - menuAnchorPane.getWidth();

        if (oldNode != null) {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), oldNode);
            slideOut.setFromX(0);
            slideOut.setToX(-transitionDistance);
            slideOut.setOnFinished(event -> {
                contentStackPane.getChildren().remove(oldNode);
                contentStackPane.getChildren().add(newNode);
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newNode);
                slideIn.setFromX(transitionDistance);
                slideIn.setToX(0);
                slideIn.play();
            });
            slideOut.play();
        } else {
            contentStackPane.getChildren().add(newNode);
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newNode);
            slideIn.setFromX(transitionDistance);
            slideIn.setToX(0);
            slideIn.play();
        }
    }
    
    @FXML
    void loadEventView(Dogadjaj dogadjaj) {

        goBackBtn.setVisible(true);
        backIcon.setVisible(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/event-details.fxml"));
            Parent view = loader.load();

            // Dodaj trenutni prikaz u historiju
            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            EventDetailsController controller = loader.getController();
            controller.setParentController(this);
            controller.setDogadjaj(dogadjaj);
            controller.setKorisnik(korisnikService.pronadjiKorisnika(loggedInUsername));
    
            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) goBack();
    }


    @FXML
    void goBack() {

        if (currentButton.equals("Kupljene karte") || currentButton.equals("Rezervisane karte") || currentButton.equals("Korisnički profil")) {
            setActiveButton(currentCategoryButton);
        }
        if (currentButton.equals("Događaji") || currentButton.equals("Korisnici") || currentButton.equals("Lokacije") || currentButton.equals("Moji događaji")) {
            setActiveButton(currentCategoryButton);
        }

        if (viewHistory.size() == 1)
            hideBackButton();
        if (!viewHistory.isEmpty()) {
            Node previousView = viewHistory.pop();
            addWithSlideTransition(previousView);
        }
    }

    private void showBackButton() {
        goBackBtn.setVisible(true);
        backIcon.setVisible(true);
    }

    private void hideBackButton() {
        goBackBtn.setVisible(false);
        backIcon.setVisible(false);
    }

    @FXML
    void lokacijaBtnClicked(ActionEvent event) {
        if (viewHistory.isEmpty())
            startFilterView("location");
    }

    @FXML
    void dateBtnClicked(ActionEvent event) {
        if (viewHistory.isEmpty())
            startFilterView("date");
    }

    @FXML
    void priceBtnClicked(ActionEvent event) {
        if (viewHistory.isEmpty())
            startFilterView("price");
    }

    void startFilterView(String filter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/" + filter + "-filter.fxml"));
            Parent root = loader.load();

            switch (filter) {
                case "location" -> {
                    LocationController filterController = loader.getController();
                    filterController.setMainScreenController(this);
                }
                case "date" -> {
                    DatesController filterController = loader.getController();
                    filterController.setMainScreenController(this);
                }
                case "price" -> {
                    PriceController filterController = loader.getController();
                    filterController.setMainScreenController(this);
                }
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFilterButton(String idPrefix, String buttonTextValue) {
        Button filterButton = new Button();
        filterButton.setId(idPrefix + "-" + buttonTextValue);
        filterButton.getStyleClass().add("filter-button");
        filterButton.setMinHeight(24);
        filterButton.setMaxHeight(24);

        if ("dateButton".equals(idPrefix)) {
            filterButton.getStyleClass().add("date-button");
        } else if ("priceButton".equals(idPrefix)) {
            filterButton.getStyleClass().add("price-button");
        }

        Tooltip tooltip = new Tooltip("Pritisni za uklanjanje filtera");
        filterButton.setTooltip(tooltip);

        Text buttonText = new Text(buttonTextValue);
        buttonText.getStyleClass().add("text");
        buttonText.setFill(Color.WHITE);

        ImageView removeIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/icons/close_white.png")));
        removeIcon.setFitHeight(15);
        removeIcon.setFitWidth(18);
        removeIcon.getStyleClass().add("x-icon");

        filterButton.setOnAction(e -> {
            filtersFlowPane.getChildren().remove(filterButton);
            prikaziDogadjajePoFilteru();
        });

        HBox content = new HBox(buttonText, removeIcon);
        content.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(buttonText, Priority.ALWAYS);
        content.setSpacing(10);

        filterButton.setGraphic(content);
        filtersFlowPane.getChildren().add(filterButton);
    }

    // Ažuriranje filtera za lokaciju
    public void updateSelectedLocations(List<String> locations) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && node.getId() != null && node.getId().startsWith("locationButton"));
        for (String location : locations) {
            createFilterButton("locationButton", location);
        }
        prikaziDogadjajePoFilteru();
    }

    // Ažuriranje filtera za datum
    public void updateDates(String startDate, String endDate) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && node.getId() != null && node.getId().startsWith("dateButton"));
        createFilterButton("dateButton", startDate + " - " + endDate);
        prikaziDogadjajePoFilteru();
    }

    // Ažuriranje filtera za cijenu
    public void updatePrice(String startPrice, String endPrice) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && node.getId() != null && node.getId().startsWith("priceButton"));
        createFilterButton("priceButton", "od " + startPrice + " KM do " + endPrice + " KM");
        prikaziDogadjajePoFilteru();
    }

    @FXML
    private AnchorPane mojProfilPaneKorisnik, mojProfilPaneOrganizator, mojProfilPaneAdministrator, userPane;

    @FXML
    private Button dodajDogadjajBtn, dodajLokacijuBtn;

    public void updateUIForLoggedInUser() {
        prijavaBtn.setVisible(false);
        odjavaBtn.setVisible(true);
        registracijaBtn.setVisible(false);
      //  korisnikPodaci.setVisible(true);
       // mojProfilLbl.setVisible(true);
       // mojProfilVbox.setVisible(true);
        if (tipKorisnika.equals(TipKorisnika.KORISNIK)) {
            novcanikKupcaLbl.setVisible(true);
            novcanikKupcaImg.setVisible(true);
            mojProfilPaneKorisnik.setVisible(true);
            userPane.setVisible(true);
        }
        if (tipKorisnika.equals(TipKorisnika.ORGANIZATOR)) {
            novcanikKupcaLbl.setVisible(false);
            novcanikKupcaImg.setVisible(false);
            mojProfilPaneOrganizator.setVisible(true);
            userPane.setVisible(true);
            dodajDogadjajBtn.setVisible(true);
            dodajLokacijuBtn.setVisible(true);
         }
         if (tipKorisnika.equals(TipKorisnika.ADMINISTRATOR)) {
            novcanikKupcaLbl.setVisible(false);
            novcanikKupcaImg.setVisible(false);
            mojProfilPaneAdministrator.setVisible(true);
            userPane.setVisible(true);
         }
    }
    
    public void updateUIForLoggedOutUser() {
        prijavaBtn.setVisible(true);
        odjavaBtn.setVisible(false);
        registracijaBtn.setVisible(true);
       // korisnikPodaci.setVisible(false);
      //  novcanikKupcaLbl.setVisible(false);
        mojProfilPaneKorisnik.setVisible(false);
        mojProfilPaneOrganizator.setVisible(false);
        mojProfilPaneAdministrator.setVisible(false);
        userPane.setVisible(false);
        dodajDogadjajBtn.setVisible(false);
        dodajLokacijuBtn.setVisible(false);
    }

   
    public void showViewWithTransition(String fileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/" + fileName + ".fxml"));
            Parent view = loader.load();

            if (!contentStackPane.getChildren().isEmpty()) {
                viewHistory.push(contentStackPane.getChildren().get(0));
            }

            addWithSlideTransition(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     

}