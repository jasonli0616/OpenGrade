package com.opengrade.opengrade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        // Create tables (if not exists) on app launch
        Database.createTables();

        launch();
    }

    /**
     * Shows the welcome view.
     *
     * @param stage the stage of the application
     * @throws IOException JavaFX error
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OpenGrade");
        stage.setScene(scene);
        stage.show();
    }
}