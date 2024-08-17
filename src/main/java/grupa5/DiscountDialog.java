package grupa5;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import grupa5.baza_podataka.Popust;

public class DiscountDialog {

    public static Popust promptForDiscount(List<Popust> dostupniPopusti) {
        // Create a new dialog
        Dialog<Popust> dialog = new Dialog<>();
        dialog.setTitle("Izaberite Popust");
        dialog.setHeaderText("Izaberite popust koji želite da iskoristite.");

        // Set the button types
        ButtonType okButtonType = new ButtonType("Potvrdi", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Otkaži", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        // Create a ListView to display the discounts
        ListView<Popust> listView = new ListView<>();
        ObservableList<Popust> observablePopusti = FXCollections.observableArrayList(dostupniPopusti);
        listView.setItems(observablePopusti);
        listView.setCellFactory(param -> new ListCell<Popust>() {
            @Override
            protected void updateItem(Popust item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getTipPopusta() + ": " + item.getVrijednostPopusta() + " (ID: " + item.getPopustID() + ")");
            }
        });

        // Add the ListView to the dialog
        VBox vbox = new VBox(listView);
        dialog.getDialogPane().setContent(vbox);

        // Convert the result to the selected discount
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        // Show the dialog and wait for a response
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        return dialog.showAndWait().orElse(null);
    }
}
