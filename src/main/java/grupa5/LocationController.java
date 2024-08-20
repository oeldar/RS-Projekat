package grupa5;

import java.util.ArrayList;
import java.util.List;
import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.MjestoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LocationController {

    @FXML
    private VBox vboxContainer;

    @FXML
    private TextField searchbarMjesta;

    private EntityManagerFactory entityManagerFactory;
    private MjestoService mjestoService;
    private MainScreenController mainScreenController;

    private List<Mjesto> mjesta;

    public LocationController() {
        // Initialize EntityManagerFactory and MjestoService
        this.entityManagerFactory = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        this.mjestoService = new MjestoService(entityManagerFactory);
    }

    @FXML
    public void initialize() {
        mjesta = mjestoService.filtrirajMjesta("");
        prikaziMjesta(mjesta);

        searchbarMjesta.textProperty().addListener((observable, oldValue, newValue) -> {
            mjesta = mjestoService.filtrirajMjesta(newValue);
            prikaziMjesta(mjesta);
        });
    }

    private void prikaziMjesta(List<Mjesto> mjesta) {
        vboxContainer.getChildren().clear();

        // Create and add new CheckBox elements
        for (Mjesto mjesto : mjesta) {
            CheckBox checkBox = new CheckBox(mjesto.getNaziv());
            checkBox.getStyleClass().add("custom-checkbox");
            checkBox.setUserData(mjesto.getMjestoID());
            vboxContainer.getChildren().add(checkBox);
        }
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.equals(KeyCode.ENTER)) handleDodaj();
        else if (keyCode.equals(KeyCode.ESCAPE)) closeWindow();
    }

    @FXML
    private void handleDodaj() {
        List<String> selectedPlaceNames = new ArrayList<>();
        for (var node : vboxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    String placeName = checkBox.getText();
                    selectedPlaceNames.add(placeName);
                }
            }
        }
        mainScreenController.updateSelectedLocations(selectedPlaceNames);
        closeWindow();
    }

    private void closeWindow() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close(); // Close EntityManagerFactory when the window is closed
        }
        Stage stage = (Stage) vboxContainer.getScene().getWindow();
        stage.close();
    }
}