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
import static utils.RouteTableOperations.getRouteFromDb;
import static utils.UserTableOperations.getUserFromDb;

public class AddRouteController {
    /**
     * The route speed limit text field
     */
    @FXML
    private TextField routeSpeedLimit;

    /**
     * The route name text field
     */
    @FXML
    private TextField routeName;

    /**
     * The route length text field
     */
    @FXML
    private TextField routeLength;

    /**
     * The cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * The add route button
     */
    @FXML
    private Button addButton;

    /**
     * The result of the add route operation
     */
    private Route addedRoute;

    @FXML
    void initialize() {
        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(addButton, Cursor.HAND);
    }

    /**
     * function to handle the add route button click
     * This function will add a route to the database
     * @param event The event to handle
     */
    @FXML
    public void handleAddRoute(ActionEvent event) throws IOException {
        try {
            Admin admin = new Admin(getUserFromDb("admin@admin.com"));
            TutorSystem tutorSystem = new TutorSystem();

            Command addRouteCommand = new AddRouteCommand(
                    tutorSystem,
                    new Route(
                            routeName.getText(),
                            Double.parseDouble(routeSpeedLimit.getText()),
                            Double.parseDouble(routeLength.getText())
                    )
            );

            admin.addCommand(addRouteCommand);

            if (getRouteFromDb(routeName.getText()) == null) {
                admin.executeCommand(addRouteCommand);
                addedRoute = getRouteFromDb(routeName.getText());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Route already exists!");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid route length or route speed limit!");
        } finally {
            if (addedRoute != null) {
                Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/all-routes-view.fxml"));
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
            }
        }
    }

    /**
     * function to handle the cancel button click
     * This function will close the add route window
     * @param event The event to handle
     */
    @FXML
    public void handleCancelAction(@NotNull ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/all-routes-view.fxml"));
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * function to get the added route
     * @return The added route
     */
    public Route getAddedRoute() {
        return addedRoute;
    }
}
