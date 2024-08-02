module grupa5 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive jakarta.persistence;
    requires transitive org.hibernate.orm.core;

    opens grupa5 to javafx.fxml, org.hibernate.orm.core;
    exports grupa5;
}
