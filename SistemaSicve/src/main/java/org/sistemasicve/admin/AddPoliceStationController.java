package org.sistemasicve.admin;

import utils.PoliceStation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static database.operations.PoliceStationTableOperations.getPoliceStationFromDb;
import static database.operations.PoliceStationTableOperations.insertPoliceStationIntoDb;

public class AddPoliceStationController {
    /**
     * The cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * The police station name text field
     */
    @FXML
    private TextField policeStationName;
    /**
     * The add police station button
     */
    @FXML
    private Button addPoliceStationButton;

    /**
     * Function to initialize the add police station controller
     */
    @FXML
    void initialize() {
        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(addPoliceStationButton, Cursor.HAND);
    }

    /**
     * Function to handle the add police station button
     * @param event The event to handle
     */
    @FXML
    void handleAddPoliceStation(ActionEvent event) throws IOException {
        if (policeStationName.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Police station name cannot be empty");
        }else if (getPoliceStationFromDb(policeStationName.getText()) != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Police station already exists");
        } else {
            PoliceStation policeStation = new PoliceStation(policeStationName.getText());
            insertPoliceStationIntoDb(policeStation);
            openAllRoutesView(event);
        }

        policeStationName.clear();
    }

    /**
     * Function to handle the cancel button
     * @param event The event to handle
     */
    @FXML
    void handleCancelButton(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/all-routes-view.fxml"));
        Parent allRoutesView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(allRoutesView));

        stage.show();
    }

    /**
     * Function to open the all routes view
     * @param event The event to handle
     */
    void openAllRoutesView(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/all-routes-view.fxml"));
        Parent allRoutesView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(allRoutesView));

        stage.show();
    }
}

