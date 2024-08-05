package grupa5;


import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Mjesto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LocationController {
     @FXML
    private VBox vboxContainer;

    @FXML
    public void initialize() {
        // Priprema podataka
        String[] labels = {"Brcko", "Tuzla", "Lukavac", "Sarajevo", "Orasje", "Nekobasdugoimenekoggradadavidimstacebit", "josjednodugoime", "jednomalokraceime", "aliipakjeduze", "alidobrostacessadkadjetakvasituacija", "basdavidimhocelsepojka"};

        // Kreiranje i dodavanje više CheckBox elemenata
        for (String label : labels) {
            CheckBox checkBox = new CheckBox(label);
            checkBox.getStyleClass().add("custom-checkbox");
            vboxContainer.getChildren().add(checkBox);
        }
    }

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    @FXML
    private void handleDodaj() {
        List<String> selectedLocations = new ArrayList<>();
        for (var node : vboxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedLocations.add(checkBox.getText());
                }
            }
        }
        // Prijenos selektovanih lokacija u MainScreenController
        mainScreenController.updateSelectedLocations(selectedLocations);

        // Zatvaranje prozora
        Stage stage = (Stage) vboxContainer.getScene().getWindow();
        stage.close();
    }
    
}