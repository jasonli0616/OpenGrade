module com.opengrade.opengrade {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.opengrade.opengrade to javafx.fxml;
    exports com.opengrade.opengrade;
    exports com.opengrade.opengrade.controllers;
    opens com.opengrade.opengrade.controllers to javafx.fxml;
}