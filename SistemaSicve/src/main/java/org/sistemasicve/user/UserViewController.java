package org.sistemasicve.user;

import utils.Infraction;
import utils.Route;
import javafx.application.Platform;
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
import singleton.pattern.LoggedUser;
import singleton.pattern.Trip;
import observer_memento.pattern.TutorStation;
import org.jetbrains.annotations.NotNull;
import utils.LoggerClass;

import java.io.IOException;
import static utils.CursorStyle.setCursorStyleOnHover;
import static database.operations.UserTableOperations.updateUserChoiceInDb;


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
                LoggerClass.log("Error updating user choice in database: " + e.getMessage(), LoggerClass.LogType.ERROR);
            }
        } else {
            try {
                updateUserChoiceInDb(LoggedUser.getInstance().getUser(), false);
            } catch (Exception e) {
                LoggerClass.log("Error updating user choice in database: " + e.getMessage(), LoggerClass.LogType.ERROR);
            }
        }
    }

    /**
     * Function to handle the add vehicle button
     * @param event The event to handle
     */
    @FXML
    void handleAddVehicle(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/add-vehicle-view.fxml"));
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
    void handleEnterRoute(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/enter-route-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setTitle("System SICVE - Enter Route");
        stage.setScene(new Scene(root));
    }

    /**
     * Function to handle the exit route button
     */
    @FXML
    void handleExitRouteButton() {
        new Thread(() -> {
            Trip.getInstance().setContinueMoving(false);
            LoggerClass.log("Vehicle " + Trip.getInstance().getVehicle().getPlate() + " exited the route " + Trip.getInstance().getRoute().getName(), LoggerClass.LogType.INFO);

            TutorStation tutorStation = Trip.getInstance().getVehicle().getTutorStation();
            tutorStation.detach(Trip.getInstance().getVehicle());

            Route route = Trip.getInstance().getRoute();

            if (Trip.getInstance().getInfractions().get(route) == null || Trip.getInstance().getInfractions().get(route).isEmpty()) {
                LoggerClass.log("No infractions committed on route " + route.getName() + " by vehicle " + Trip.getInstance().getVehicle().getPlate(), LoggerClass.LogType.INFO);
            } else {
                Infraction mostSeriousInfraction = Trip.getInstance().getInfractions().get(route).getFirst();

                for (Infraction inf : Trip.getInstance().getInfractions().get(route)) {
                    if (inf.getSpeed() > mostSeriousInfraction.getSpeed()) {
                        mostSeriousInfraction = inf;
                    }
                }

                LoggerClass.log("Send most serious infraction to police station " + route.getPoliceStation() + " - Infraction: " + mostSeriousInfraction.getMessage(), LoggerClass.LogType.INFO);
            }

            Platform.runLater(() -> {
                if (LoggedUser.getInstance().getUser().getSendMeNotification()) {
                    String messageText = "Your vehicle " + Trip.getInstance().getVehicle().getPlate() + " has exited the route " + Trip.getInstance().getRoute().getName();
                    tutorStation.notifyObserver(Trip.getInstance().getVehicle(), messageText);
                }
            });
        }).start();
    }

    /**
     * Function to handle the view vehicles list button
     * @param event The event to handle
     */
    @FXML
    void handleOpenViewVehiclesList(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/all-vehicles-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }
}
