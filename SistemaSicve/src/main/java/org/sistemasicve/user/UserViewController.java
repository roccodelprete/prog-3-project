package org.sistemasicve.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import observer_memento.pattern.LoggedUser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.UserTableOperations.updateUserChoiceInDb;

public class UserViewController {
    /**
     * The view vehicles list button
     */
    @FXML
    private Button viewVehiclesList;

    /**
     * The user label
     */
    @FXML
    private Label userLabel;

    /**
     * The send me notification check box
     */
    @FXML
    private CheckBox sendMeNotification;

    /**
     * The logout button
     */
    @FXML
    private Button logoutButton;

    /**
     * The add vehicle button
     */
    @FXML
    private Button addVehicleButton;

    /**
     * The exit route button
     */
    @FXML
    private Button exitrouteButton;

    /**
     * The enter route button
     */
    @FXML
    private Button enterRouteButton;

    /**
     * Function to initialize the user view
     */
    @FXML
    void initialize() {
        userLabel.setText("Hello " + LoggedUser.getInstance().getUser().getUserName());
        sendMeNotification.setSelected(LoggedUser.getInstance().getUser().getSendMeNotification());

        setCursorStyleOnHover(logoutButton, Cursor.HAND);
        setCursorStyleOnHover(addVehicleButton, Cursor.HAND);
        setCursorStyleOnHover(exitrouteButton, Cursor.HAND);
        setCursorStyleOnHover(enterRouteButton, Cursor.HAND);
        setCursorStyleOnHover(viewVehiclesList, Cursor.HAND);
        sendMeNotification.setCursor(Cursor.HAND);
    }

    /**
     * Function to handle the notification choice
     */
    @FXML
    void handleNotificationChoice() {
        if (sendMeNotification.isSelected()) {
            try {
                updateUserChoiceInDb(LoggedUser.getInstance().getUser(), true);
            } catch (Exception e) {
                System.out.println("[" + new Date() + "] ERROR: ");
                e.printStackTrace();
            }
        } else {
            try {
                updateUserChoiceInDb(LoggedUser.getInstance().getUser(), false);
            } catch (Exception e) {
                System.out.println("[" + new Date() + "] ERROR: ");
                e.printStackTrace();
            }
        }
    }

    /**
     * Function to handle the add vehicle button
     * @param event The event to handle
     */
    @FXML
    void handleAddVehicle(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/add-vehicle-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    /**
     * Function to log out the user
     */
    @FXML
    void handleLogout(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/home-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    /**
     * Function to handle the enter route button
     * @param event The event to handle
     */
    @FXML
    void handleEnterRoute(ActionEvent event) {

    }

    /**
     * Function to handle the exit route button
     * @param event The event to handle
     */
    @FXML
    void handleExitRouteButton(ActionEvent event) {

    }

    /**
     * Function to handle the view vehicles list button
     * @param event The event to handle
     */
    @FXML
    void handleOpenViewVehiclesList(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/all-vehicles-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }
}
