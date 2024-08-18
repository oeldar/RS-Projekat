package grupa5;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

import grupa5.baza_podataka.Popust;

public class DiscountDialog {
    public static Task<Popust> promptForDiscountTask(List<Popust> dostupniPopusti) {
        Task<Popust> task = new Task<>() {
            @Override
            protected Popust call() {
                final Popust[] selectedDiscount = new Popust[1];
                Platform.runLater(() -> {
                    Dialog<Popust> dialog = new Dialog<>();
                    dialog.setTitle("Izaberite Popust");
                    dialog.setHeaderText("Izaberite popust koji želite da iskoristite.");
    
                    ButtonType okButtonType = new ButtonType("Potvrdi", ButtonType.OK.getButtonData());
                    ButtonType cancelButtonType = new ButtonType("Otkaži", ButtonType.CANCEL.getButtonData());
                    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
    
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
    
                    VBox vbox = new VBox(listView);
                    dialog.getDialogPane().setContent(vbox);
    
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == okButtonType) {
                            return listView.getSelectionModel().getSelectedItem();
                        }
                        return null;
                    });
    
                    Optional<Popust> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        selectedDiscount[0] = result.get();
                        updateValue(selectedDiscount[0]); // Postavi rezultat Task-a
                    } else {
                        updateValue(null); // Ako nije izabran popust, postavi null
                    }
                });
    
                // Očekujte da se rezultat postavi izvan call metode
                return null;
            }
        };
    
        return task;
    }    

    public static Popust promptForDiscount(List<Popust> dostupniPopusti) {
        Dialog<Popust> dialog = new Dialog<>();
        dialog.setTitle("Izaberite Popust");
        dialog.setHeaderText("Izaberite popust koji želite da iskoristite.");

        ButtonType okButtonType = new ButtonType("Potvrdi", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Otkaži", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

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

        VBox vbox = new VBox(listView);
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Popust> result = dialog.showAndWait();
        return result.orElse(null); // Return selected discount or null if none selected
    }     
}