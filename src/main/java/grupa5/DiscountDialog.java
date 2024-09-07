package grupa5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import grupa5.baza_podataka.Popust;

@SuppressWarnings("exports")
public class DiscountDialog {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");

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
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane grid = new GridPane();

                    Label discountLabel = new Label("Popust: " + item.getVrijednostPopusta() + "%");
                    Label conditionLabel = new Label("Uslov: " + item.getUslov());
                    Label expirationDateLabel = new Label("Ističe: " + item.getDatumIsteka().format(formatter));

                    grid.add(discountLabel, 0, 0);
                    grid.add(conditionLabel, 0, 1);
                    grid.add(expirationDateLabel, 0, 2);

                    grid.setHgap(0);
                    grid.setVgap(0);
                    grid.setStyle("-fx-padding: 10px;");

                    // Dodavanje vizualnog razdvajanja između popusta
                    Line separator = new Line(0, 0, 275, 0);
                    separator.setStroke(Color.LIGHTGRAY);
                    separator.setStrokeWidth(1.5);

                    VBox vbox = new VBox(grid, separator);
                    //vbox.setStyle("-fx-padding: 10px;");
                    
                    setGraphic(vbox);
                }
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
        return result.orElse(null);
    }
}