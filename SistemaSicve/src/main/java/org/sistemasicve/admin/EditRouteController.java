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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
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
     * Function to perform when the scene is initialized
     */
    @FXML
    void initialize() {
        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(editButton, Cursor.HAND);
    }

    /**
     * Function to handle the cancel action
     */
    @FXML
    void handleCancelAction() {
        cancelButton.getScene().getWindow().hide();
    }

    /**
     * Function to handle the edit route action
     * @param event The event to handle
     */
    @FXML
    void handleEditRoute(ActionEvent event) throws IOException {
        try {
            Admin admin = new Admin(getUserFromDb("admin@admin.com"));
            TutorSystem tutorSystem = new TutorSystem();

            Command editCommand = new EditRouteCommand(
                tutorSystem,
                route,
                routeName.getText(),
                Double.parseDouble(routeSpeedLimit.getText()),
                Double.parseDouble(routeLength.getText()
            ));

            admin.addCommand(editCommand);
            admin.executeCommand(editCommand);
        }  catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid route length or route speed limit!");
        } finally {
            Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/all-routes-view.fxml"));
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
    }
}
