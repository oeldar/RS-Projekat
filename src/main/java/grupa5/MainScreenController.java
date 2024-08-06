package grupa5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Mjesto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
// import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
// import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainScreenController {
    @FXML
    private Label testLabel;
    private Stage stage;

    @FXML
    void loginBtnClicked(ActionEvent event) {
         try {
            if (stage == null || !stage.isShowing()) {
                Parent root = App.loadFXML("login");
                setStage(root, "Prijava", 950,  700);
            } else {
                stage.toFront();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signUpBtnClicked(ActionEvent event) {
         try {
            if (stage == null || !stage.isShowing()) {
                Parent root = App.loadFXML("registration-view");
                setStage(root, "Registacija", 1000, 750);
            } else {
                stage.toFront();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStage(Parent root, String title, double sizeX, double sizeY) {
        stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, sizeX, sizeY));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        stage.setOnCloseRequest(e -> stage = null);
    }

    @FXML private Button sviDogadjajiBtn;
    @FXML private Button muzikaBtn;
    @FXML private Button kulturaBtn;
    @FXML private Button sportBtn;
    @FXML private Button ostaloBtn;

    private List<Button> categoryButtons;

    @FXML private ImageView sviDogadjajiImg;
    @FXML private ImageView muzikaImg;
    @FXML private ImageView kulturaImg;
    @FXML private ImageView sportImg;
    @FXML private ImageView ostaloImg;

    private List<ImageView> categoryIcons;

    @FXML
    public void initialize() {
        categoryButtons = new ArrayList<>();
        categoryButtons.add(sviDogadjajiBtn);
        categoryButtons.add(muzikaBtn);
        categoryButtons.add(sportBtn);
        categoryButtons.add(kulturaBtn);
        categoryButtons.add(ostaloBtn);

        setActiveButton(sviDogadjajiBtn);

        categoryIcons = new ArrayList<>();
        categoryIcons.add(sviDogadjajiImg);
        categoryIcons.add(muzikaImg);
        categoryIcons.add(sportImg);
        categoryIcons.add(kulturaImg);
        categoryIcons.add(ostaloImg);

        // Dodaj početni stil za sva dugmad
       // buttons.forEach(button -> button.getStyleClass().add("cattegory-button"));

        // Postavi akcije za klik na dugmad
        sviDogadjajiBtn.setOnAction(event -> setActiveButton(sviDogadjajiBtn));
        muzikaBtn.setOnAction(event -> setActiveButton(muzikaBtn));
        sportBtn.setOnAction(event -> setActiveButton(sportBtn));
        kulturaBtn.setOnAction(event -> setActiveButton(kulturaBtn));
        ostaloBtn.setOnAction(event -> setActiveButton(ostaloBtn));
    }

    private ImageView imageFromButton(Button button) {
        ImageView result;
        String buttonName = button.getId();
        switch (buttonName) {
            case "sviDogadjajiBtn":
                result = sviDogadjajiImg;
                break;
            case "muzikaBtn":
                result = muzikaImg;
                break;
            case "kulturaBtn":
                result = kulturaImg;
                break;
            case "sportBtn":
                result = sportImg;
                break;
            case "ostaloBtn":
                result = ostaloImg;
                break;
            default:
                result = ostaloImg;
                break;
        }

        return result;
    }

    @FXML
    private Label categoryTitle;

    private void setActiveButton(Button activeButton) {
        if (activeButton.equals(sviDogadjajiBtn)) {
            categoryTitle.setText("Svi događaji");
        } else if (activeButton.equals(muzikaBtn)) {
            categoryTitle.setText("Muzika");
        } else if (activeButton.equals(kulturaBtn)) {
            categoryTitle.setText("Kultura");
        } else if (activeButton.equals(sportBtn)) {
            categoryTitle.setText("Sport");
        } else if (activeButton.equals(ostaloBtn)) {
            categoryTitle.setText("Ostalo");
        } 
        // Resetuj sve dugmadi na osnovni stil
        ImageView currentImg;
        for (Button button : categoryButtons) {
            currentImg = imageFromButton(button);
            currentImg.setVisible(false);
            button.getStyleClass().removeAll("category-button-active");
            if (!button.getStyleClass().contains("category-button")) {
                button.getStyleClass().add("category-button");
            }
        }

        // Postavi aktivni stil za kliknuto dugme
        ImageView activeImg = imageFromButton(activeButton);
        activeImg.setVisible(true);
        activeButton.getStyleClass().remove("category-button");
        activeButton.getStyleClass().add("category-button-active");
    }


    void startFilterView(String filter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/" + filter + "-filter.fxml"));
            Parent root = loader.load();

            if(filter.equals("location")) {
                LocationController filterController = loader.getController();
                filterController.setMainScreenController(this);
            } else if (filter.equals("date")) {
                DatesController filterController = loader.getController();
                filterController.setMainScreenController(this);
            } else {
                PriceController filterController = loader.getController();
                filterController.setMainScreenController(this);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private FlowPane filtersFlowPane;

    private void createFilterButton(String idPrefix, String buttonTextValue) {
        Button filterButton = new Button();
        filterButton.setId(idPrefix + "-" + buttonTextValue);
        filterButton.getStyleClass().add("filter-button");
        filterButton.setMinHeight(24);
        filterButton.setMaxHeight(24);

        if (idPrefix.equals("dateButton")) {
            filterButton.getStyleClass().add("date-button");
        } else if (idPrefix.equals("priceButton")) {
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
            System.out.println("Button clicked, removing filter");
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
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getId() != null && ((Button) node).getId().startsWith("locationButton"));
        for (String location : locations) {
            createFilterButton("locationButton", location);
        }
    }

    // Ažuriranje filtera za datum
    public void updateDates(String startDate, String endDate) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getId() != null && ((Button) node).getId().startsWith("dateButton"));
        createFilterButton("dateButton", startDate + " - " + endDate);
    }

    // Ažuriranje filtera za cijenu
    public void updatePrice(String startPrice, String endPrice) {
        filtersFlowPane.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getId() != null && ((Button) node).getId().startsWith("priceButton"));
        createFilterButton("priceButton", "od " + startPrice + " KM do " + endPrice + " KM");
    }
        
    @FXML
    private AnchorPane searchBarPane;

    @FXML
    private VBox eventsVBox;

    @FXML
    private Button eventButton;

    @FXML
    void eventButtonClicked(ActionEvent event) throws IOException {
        searchBarPane.setVisible(false);
        eventsVBox.setVisible(false);
    }
    


 



}
