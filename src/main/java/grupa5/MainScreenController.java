package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajService;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.util.Duration;

public class MainScreenController {
    private static final String PERSISTENCE_UNIT_NAME = "HypersistenceOptimizer";
    private static final String EVENT_CARD_FXML = "views/event-card.fxml";
    // private static final String EVENT_DETAILS_FXML = "views/event-details.fxml";

    private EntityManagerFactory emf;
    private DogadjajService dogadjajService;

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

    private Stack<Node> viewHistory = new Stack<>();
    private List<Button> categoryButtons;
    private Map<Button, ImageView> buttonToImageMap;

    private String currentCategory;
    int brojSvihDogadjaja;
    int brojDogadjajaPoStranici = 6;
    List<List<Dogadjaj>> pages = new ArrayList<>();
    @FXML
    private Button prevPageBtn, nextPageBtn;

    int currentPage = 0;
    List<Dogadjaj> sviDogadjaji;
    List<Dogadjaj> currentDogadjaji;

    @FXML
    public void initialize() {
        buttonToImageMap = new HashMap<>();

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            dogadjajService = new DogadjajService(emf);
        } catch (Exception e) {
            System.err.println("Failed to initialize persistence unit: " + e.getMessage());
            return;
        }

        currentCategory = "Svi događaji";


        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if ("Svi događaji".equals(currentCategory)) {
                pages.clear();
                currentPage = 0;
                currentDogadjaji = dogadjajService.pronadjiDogadjajePoNazivu(newValue);

                if(currentDogadjaji.size() == 0) {
                    eventsGridPane.getChildren().clear();
                    return;
                }

                for (int i = 0; i < currentDogadjaji.size(); i += brojDogadjajaPoStranici) {
                    pages.add(currentDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, currentDogadjaji.size())));
                }

                prikaziStranicu(0);
            } else {
                pages.clear();
                currentPage = 0;
                currentDogadjaji = dogadjajService.pronadjiDogadjajePoNazivuIKategoriji(newValue, currentCategory);

                if(currentDogadjaji.size() == 0) {
                    eventsGridPane.getChildren().clear();
                    return;
                }

                for (int i = 0; i < currentDogadjaji.size(); i += brojDogadjajaPoStranici) {
                    pages.add(currentDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, currentDogadjaji.size())));
                }

                prikaziStranicu(0);
            }
        });

        sviDogadjaji = dogadjajService.pronadjiSveDogadjaje();

       
        setupCategoryButtons();
        setupCategoryIcons();
        loadInitialEvents();

        
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





    private void setupCategoryButtons() {
        categoryButtons = List.of(sviDogadjajiBtn, muzikaBtn, kulturaBtn, sportBtn, ostaloBtn);
        categoryButtons.forEach(button -> button.setOnAction(this::handleCategoryButtonAction));
        setActiveButton(sviDogadjajiBtn);
    }

    private void setupCategoryIcons() {
        buttonToImageMap.put(sviDogadjajiBtn, sviDogadjajiImg);
        buttonToImageMap.put(muzikaBtn, muzikaImg);
        buttonToImageMap.put(kulturaBtn, kulturaImg);
        buttonToImageMap.put(sportBtn, sportImg);
        buttonToImageMap.put(ostaloBtn, ostaloImg);
    }

    

    private void loadInitialEvents() {
        currentPage = 0;
        pages.clear();
        brojSvihDogadjaja = sviDogadjaji.size();
        System.out.println(brojSvihDogadjaja);

        for (int i = 0; i < brojSvihDogadjaja; i += brojDogadjajaPoStranici) {
            pages.add(sviDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, brojSvihDogadjaja)));
        }

        prikaziDogadjaje(pages.get(0));
    }


    @FXML
    void loginBtnClicked(ActionEvent event) {
        openModal("login", "Prijava", 950, 700);
    }

    @FXML
    void signUpBtnClicked(ActionEvent event) {
        openModal("registration-view", "Registracija", 1000, 700);
    }

    private void openModal(String fxmlFile, String title, double width, double height) {
        if (stage == null || !stage.isShowing()) {
            try {
                Parent root = App.loadFXML(fxmlFile);
                stage = new Stage();
                stage.setTitle(title);
                stage.setScene(new Scene(root, width, height));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                stage.setOnCloseRequest(e -> stage = null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            stage.toFront();
        }
    }

    private void handleCategoryButtonAction(ActionEvent event) {
        searchInput.clear();
        Button clickedButton = (Button) event.getSource();
        String category = clickedButton.getText();
        currentCategory = category;
        if (category.equals("Svi događaji")) {
            loadInitialEvents();
            setActiveButton(clickedButton);
            return;
        }

        currentDogadjaji = dogadjajService.pronadjiDogadjajePoVrsti(category);
        System.out.println("Ovoliko je dogadjaja:" + currentDogadjaji.size());
        pages.clear();
        currentPage = 0;

        for (int i = 0; i < currentDogadjaji.size(); i += brojDogadjajaPoStranici) {
            pages.add(currentDogadjaji.subList(i, Math.min(i + brojDogadjajaPoStranici, currentDogadjaji.size())));
        }

        prikaziStranicu(0);
        setActiveButton(clickedButton);
    }

    private void setActiveButton(Button activeButton) {
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

    private void prikaziStranicu(int pageIndex) {
        prikaziDogadjaje(pages.get(pageIndex));
    }

    

    private void prikaziDogadjaje(List<Dogadjaj> dogadjaji) {
        eventsGridPane.getChildren().clear();
        int row = 0;
        int col = 0;
    
        if (dogadjaji == null || dogadjaji.isEmpty()) {
            System.out.println("Nema događaja za prikaz.");
            return;
        }
    
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
    
            // Configure the controller
            if (dogadjaj != null) {
                EventDetailsController eventDetailsController = loader.getController();
                eventDetailsController.setDogadjaj(dogadjaj);
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
            controller.setDogadjaj(dogadjaj);
    
            addWithSlideTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goBack() {
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
        startFilterView("location");
    }

    @FXML
    void dateBtnClicked(ActionEvent event) {
        startFilterView("date");
    }

    @FXML
    void priceBtnClicked(ActionEvent event) {
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
    }

    // Ažuriranje filtera za datum
    public void updateDates(String startDate, String endDate) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && node.getId() != null && node.getId().startsWith("dateButton"));
        createFilterButton("dateButton", startDate + " - " + endDate);
    }

    // Ažuriranje filtera za cijenu
    public void updatePrice(String startPrice, String endPrice) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && node.getId() != null && node.getId().startsWith("priceButton"));
        createFilterButton("priceButton", "od " + startPrice + " KM do " + endPrice + " KM");
    }

    @FXML
    private Button eventButton;

    @FXML
    void eventButtonClicked(ActionEvent event) {
        changeVisibilityOnMainScreen();
    }

    @FXML
    void goBackButtonClicked(ActionEvent event) {
        changeVisibilityOnMainScreen();
    }

    private void changeVisibilityOnMainScreen() {
        searchBarPane.setVisible(!searchBarPane.isVisible());
        eventsVBox.setVisible(!eventsVBox.isVisible());
        eventDetailsPane.setVisible(!eventDetailsPane.isVisible());
    }
}