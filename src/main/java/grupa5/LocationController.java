package grupa5;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grupa5.baza_podataka.Mjesto;
import grupa5.baza_podataka.services.MjestoService;
import grupa5.support_classes.FilterService;
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
    private Set<Integer> selectedMjestoIDs = new HashSet<>();

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

            // Mark the checkbox as selected if it's in the selectedMjestoIDs set
            if (selectedMjestoIDs.contains(mjesto.getMjestoID())) {
                checkBox.setSelected(true);
            }

            // Add a listener to update the set of selected IDs
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectedMjestoIDs.add(mjesto.getMjestoID());
                } else {
                    selectedMjestoIDs.remove(mjesto.getMjestoID());
                }
            });

            vboxContainer.getChildren().add(checkBox);
        }

        // Re-add the previously selected places that are not in the current search results
        for (Integer mjestoID : selectedMjestoIDs) {
            boolean isAlreadyDisplayed = mjesta.stream()
                                               .anyMatch(mjesto -> mjesto.getMjestoID().equals(mjestoID));
                                               Mjesto mjesto = mjestoService.pronadjiMjestoPoID(mjestoID);
            if (!isAlreadyDisplayed) {
                CheckBox checkBox = new CheckBox(mjesto.getNaziv());
                checkBox.getStyleClass().add("custom-checkbox");
                checkBox.setSelected(true);
                checkBox.setUserData(mjestoID);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        selectedMjestoIDs.add(mjestoID);
                    } else {
                        selectedMjestoIDs.remove(mjestoID);
                    }
                });
                vboxContainer.getChildren().add(checkBox);
            }
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
        FilterService.getInstance().setSelectedLocations(selectedPlaceNames);
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