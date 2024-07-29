module grupa5 {
    requires javafx.controls;
    requires javafx.fxml;

    opens grupa5 to javafx.fxml;
    exports grupa5;
}
