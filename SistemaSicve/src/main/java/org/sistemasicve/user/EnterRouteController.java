package org.sistemasicve.user;

import command.pattern.PoliceStation;
import command.pattern.Route;
import command.pattern.TutorSystem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import javafx.stage.Modality;
import javafx.stage.Stage;
import observer_memento.pattern.LoggedUser;
import observer_memento.pattern.Trip;
import observer_memento.pattern.TutorStation;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import utils.LoggerClass;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.PoliceStationTableOperations.getPoliceStationFromDb;
import static utils.RouteTableOperations.getAllRoutesFromDb;
import static utils.RouteTableOperations.getRouteFromDb;
import static utils.VehicleTableOperations.getUserVehiclesFromDb;
import static utils.VehicleTableOperations.getVehicleFromDb;

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
        String twilioSID = "AC2eb490c92c14b104496daf9bbc8f681d";
        String twilioToken = "768f4a6fad548897bf23b30ad20a7c82";

        Twilio.init(twilioSID, twilioToken);

        String fromPhoneNumber = "+12019043530";
        String toPhoneNumber = LoggedUser.getInstance().getUser().getPhoneNumber();
        String messageText = "Your vehicle " + vehiclesList.getSelectionModel().getSelectedItem() + " has entered the route " + routesList.getSelectionModel().getSelectedItem();

        try {
            Trip.getInstance().setRoute(getRouteFromDb(routesList.getSelectionModel().getSelectedItem()));
            Trip.getInstance().setVehicle(getVehicleFromDb(vehiclesList.getSelectionModel().getSelectedItem()));

            currentRoute = Trip.getInstance().getRoute();

            TutorStation tutorStation = new TutorStation();
            tutorStation.attach(Trip.getInstance().getVehicle());

            if (LoggedUser.getInstance().getUser().getSendMeNotification()) {
                Message message = Message.creator(
                                new PhoneNumber(toPhoneNumber),
                                new PhoneNumber(fromPhoneNumber),
                                messageText)
                        .create();

                tutorStation.notifyObserver(Trip.getInstance().getVehicle(), "Your vehicle " + Trip.getInstance().getVehicle().getPlate() + " has entered the route " + Trip.getInstance().getRoute().getName());
                showAlert(Alert.AlertType.CONFIRMATION, "Message sent", "Message sent to " + message.getTo());
                LoggerClass.log("Message sent to " + message.getTo(), LoggerClass.LogType.INFO);
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
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error sending message to " + toPhoneNumber);
            LoggerClass.log("Error sending message to " + toPhoneNumber + ": " + e.getMessage(), LoggerClass.LogType.ERROR);
        }
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

