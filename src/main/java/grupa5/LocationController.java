package grupa5;

import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.MjestoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LocationController {

    @FXML
    private VBox vboxContainer;

    private EntityManager entityManager;
    private MjestoService mjestoService; // Service for handling places
    private MainScreenController mainScreenController;

    public LocationController() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HypersistenceOptimizer");
        this.entityManager = emf.createEntityManager();
        this.mjestoService = new MjestoService(entityManager.getEntityManagerFactory());
    }

    @FXML
    public void initialize() {
        // Load places from the database
        List<Mjesto> mjesta = mjestoService.pronadjiSvaMjesta();

        // Create and add CheckBox elements
        for (Mjesto mjesto : mjesta) {
            CheckBox checkBox = new CheckBox(mjesto.getNaziv());
            checkBox.getStyleClass().add("custom-checkbox");
            checkBox.setUserData(mjesto.getMjestoID()); // Set ID as user data
            vboxContainer.getChildren().add(checkBox);
        }
    }

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
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
        // Pass selected place names to MainScreenController
        mainScreenController.updateSelectedLocations(selectedPlaceNames);

        // Close the window
        Stage stage = (Stage) vboxContainer.getScene().getWindow();
        stage.close();
    }
}