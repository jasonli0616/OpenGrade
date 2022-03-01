module com.opengrade.opengrade {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens com.opengrade.opengrade to javafx.fxml;
    exports com.opengrade.opengrade;
    exports com.opengrade.opengrade.controllers;
    exports com.opengrade.opengrade.models;
    opens com.opengrade.opengrade.controllers to javafx.fxml;
}