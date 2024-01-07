package org.sistemasicve.admin;

import command.pattern.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import observer_memento.pattern.LoggedUser;
import org.jetbrains.annotations.NotNull;
import utils.LoggerClass;

import java.io.IOException;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.PoliceStationTableOperations.getAllPoliceStationsFromDb;
import static utils.UserTableOperations.getUserFromDb;

public class EditRouteController {
    /**
     * The route to edit
     */
    private Route route;

    /**
     * The route speed limit text field
     */
    @FXML
    private TextField routeSpeedLimit;

    /**
     * The cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * The edit route button
     */
    @FXML
    private Button editButton;

    /**
     * The route length text field
     */
    @FXML
    private TextField routeLength;

    /**
     * The route name text field
     */
    @FXML
    private TextField routeName;

    /**
     * The police stations list
     */
    @FXML
    private ComboBox<String> policeStationsList;

    /**
     * Function to perform when the scene is initialized
     */
    @FXML
    void initialize() {
        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(editButton, Cursor.HAND);

        for (PoliceStation policeStation : getAllPoliceStationsFromDb()) {
            policeStationsList.getItems().add(policeStation.getName());
        }
    }

    /**
     * Function to handle the cancel action
     * @param event The event to handle
     */
    @FXML
    void handleCancelAction(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/all-routes-view.fxml"));
        Parent allRoutesView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(allRoutesView));

        stage.show();
    }

    /**
     * Function to handle the edit route action
     * @param event The event to handle
     */
    @FXML
    void handleEditRoute(ActionEvent event) throws IOException {
        try {
            Admin admin = new Admin(LoggedUser.getInstance().getUser());
            TutorSystem tutorSystem = new TutorSystem();

            String policeStationName = policeStationsList.getSelectionModel().getSelectedItem();

            Command editCommand = new EditRouteCommand(
                tutorSystem,
                route,
                routeName.getText(),
                Integer.parseInt(routeSpeedLimit.getText()),
                Integer.parseInt(routeLength.getText()),
                policeStationName
            );

            admin.addCommand(editCommand);
            admin.executeCommand(editCommand);
        }  catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid route length or route speed limit!");
        } finally {
            Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/route-views/all-routes-view.fxml"));
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
        }
    }

    /**
     * Function to set the route to edit
     * @param route The route to edit
     */
    public void setRoute(@NotNull Route route) {
        this.route = route;

        routeName.setText(route.getName());
        routeSpeedLimit.setText(route.getSpeedLimit().toString());
        routeLength.setText(route.getLength().toString());
        policeStationsList.getSelectionModel().select(route.getPoliceStation());
    }
}
