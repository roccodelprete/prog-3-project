package org.sistemasicve.user;

import utils.Route;
import command.pattern.TutorSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

import javafx.stage.Stage;
import singleton.pattern.LoggedUser;
import singleton.pattern.Trip;
import observer_memento.pattern.TutorStation;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import utils.LoggerClass;

import static utils.CursorStyle.setCursorStyleOnHover;
import static database.operations.RouteTableOperations.getAllRoutesFromDb;
import static database.operations.RouteTableOperations.getRouteFromDb;
import static database.operations.VehicleTableOperations.getUserVehiclesFromDb;
import static database.operations.VehicleTableOperations.getVehicleFromDb;

public class EnterRouteController {
    /**
     * The cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * The enter route button
     */
    @FXML
    private Button enterRouteButton;

    /**
     * The routes list combo box
     */
    @FXML
    private ComboBox<String> routesList;

    /**
     * The vehicles list combo box
     */
    @FXML
    private ComboBox<String> vehiclesList;

    /**
     * The current route which the vehicle is moving
     */
    private Route currentRoute;

    /**
     * Function to perform when the controller is initialized
     */
    @FXML
    void initialize() {
        for (Vehicle vehicle : getUserVehiclesFromDb(LoggedUser.getInstance().getUser())) {
            vehiclesList.getItems().add(vehicle.getPlate());
        }

        for (Route route : getAllRoutesFromDb()) {
            routesList.getItems().add(route.getName());
        }

        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(enterRouteButton, Cursor.HAND);
    }

    /**
     * Function to handle the enter route button
     * @param event The event that triggered the function
     */
    @FXML
    void handleEnterRoute(ActionEvent event) {
        Trip.getInstance().setRoute(getRouteFromDb(routesList.getSelectionModel().getSelectedItem()));
        Trip.getInstance().setVehicle(getVehicleFromDb(vehiclesList.getSelectionModel().getSelectedItem()));

        TutorStation tutorStation = Trip.getInstance().getVehicle().getTutorStation();

        currentRoute = Trip.getInstance().getRoute();

        tutorStation.attach(Trip.getInstance().getVehicle());

        if (LoggedUser.getInstance().getUser().getSendMeNotification()) {
            String messageText = "Your vehicle " + vehiclesList.getSelectionModel().getSelectedItem() + " has entered the route " + routesList.getSelectionModel().getSelectedItem();
            tutorStation.notifyObserver(Trip.getInstance().getVehicle(), messageText);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/user-view.fxml"));
            Parent root = loader.load();
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            LoggerClass.log("Error loading user view: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        Trip.getInstance().setContinueMoving(true);

        new Thread(() -> {
            while (Trip.getInstance().getContinueMoving()) {
                try {
                    moveVehicleContinuously();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LoggerClass.log("Error moving vehicle continuously: " + e.getMessage(), LoggerClass.LogType.ERROR);
                }
            }
        }).start();
    }

    /**
     * Function to handle the cancel button
     * @param event The event that triggered the function
     */
    @FXML
    void handleCancel(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/user-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    /**
     * function to move a vehicle on a route
     */
    private void moveVehicleOnRoute() {
        int detectedSpeed = (int) (Math.random() * 130 + 60);

        Trip.getInstance().getVehicle().moveVehicle();

        TutorSystem tutorSystem = new TutorSystem();
        tutorSystem.detectSpeed(detectedSpeed, Trip.getInstance().getRoute(), Trip.getInstance().getVehicle());

        if (Trip.getInstance().getVehicle().getCurrentMilestone() >= currentRoute.getLength()) {
            Trip.getInstance().getVehicle().setCurrentMilestone(0);
        }
    }

    /**
     * Function to move a vehicle continuously
     */
    private void moveVehicleContinuously() {
        while (Trip.getInstance().getContinueMoving()) {
            try {
                moveVehicleOnRoute();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LoggerClass.log("Error moving vehicle continuously: " + e.getMessage(), LoggerClass.LogType.ERROR);
                e.printStackTrace();
            }
        }
    }
}

