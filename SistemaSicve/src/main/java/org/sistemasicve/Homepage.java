package org.sistemasicve;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Homepage extends Application {
    @Override
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/home-view.fxml"));
        stage.setTitle("SICVE System");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}