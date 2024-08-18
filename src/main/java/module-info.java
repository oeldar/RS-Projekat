module grupa5 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive jakarta.persistence;
    requires transitive org.hibernate.orm.core;
    requires javafx.graphics;
    requires mysql.connector.j;
    requires javafx.base;
    requires itextpdf;
    requires java.desktop;
    requires transitive com.google.zxing;
    requires transitive com.google.zxing.javase;
    requires org.apache.pdfbox;
    requires javafx.swing;
    requires kernel;
    requires io;
    requires transitive layout;
    // requires jbcrypt;

    opens grupa5 to javafx.fxml, org.hibernate.orm.core, com.google.zxing, com.google.zxing.javase;
    opens grupa5.baza_podataka to org.hibernate.orm.core, javafx.base;
    exports grupa5;
}
