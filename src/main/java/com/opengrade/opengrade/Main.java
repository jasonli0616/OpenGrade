package com.opengrade.opengrade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.IOException;

public class Main extends Application {
    /**
     * Main app method
     * Handles stage
     *
     * @param stage The stage of the application
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OpenGrade");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        // Close database on app close
        Database.connect().close();
    }
}