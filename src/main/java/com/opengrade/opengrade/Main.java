package com.opengrade.opengrade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    /**
     * Main app method
     * Handles stage
     * @param stage The stage of the application
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OpenGrade");
        stage.setScene(scene);
        stage.show();
    }

    public static void connectToDatabase() {
        Connection conn = null;

        try {
            String url =  "jdbc:sqlite:site.db";

            conn = DriverManager.getConnection(url);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        connectToDatabase();
        launch();
    }
}