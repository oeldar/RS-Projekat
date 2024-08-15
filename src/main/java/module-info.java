module grupa5 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive jakarta.persistence;
    requires transitive org.hibernate.orm.core;
    requires javafx.graphics;
    requires mysql.connector.j;
    requires javafx.base;
    // requires jbcrypt;

    opens grupa5 to javafx.fxml, org.hibernate.orm.core;
    opens grupa5.baza_podataka to org.hibernate.orm.core, javafx.base;
    exports grupa5;
}
