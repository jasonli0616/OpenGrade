module com.opengrade.opengrade {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.opengrade.opengrade to javafx.fxml;
    exports com.opengrade.opengrade;
}