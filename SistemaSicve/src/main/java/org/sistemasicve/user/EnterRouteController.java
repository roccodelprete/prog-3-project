package org.sistemasicve.user;

import command.pattern.PoliceStation;
import command.pattern.Route;
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
import java.util.Date;

import javafx.stage.Stage;
import observer_memento.pattern.LoggedUser;
import observer_memento.pattern.Trip;
import observer_memento.pattern.TutorStation;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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
        String toPhoneNumber = "+393349109813";
        String messageText = "Your vehicle " + vehiclesList.getSelectionModel().getSelectedItem() + " has entered the route " + routesList.getSelectionModel().getSelectedItem();

        try {
            Trip.getInstance().setRoute(getRouteFromDb(routesList.getSelectionModel().getSelectedItem()));
            Trip.getInstance().setVehicle(getVehicleFromDb(vehiclesList.getSelectionModel().getSelectedItem()));

            TutorStation tutorStation = new TutorStation();
            tutorStation.attach(Trip.getInstance().getVehicle());

            if (LoggedUser.getInstance().getUser().getSendMeNotification()) {
                Message message = Message.creator(
                                new PhoneNumber(toPhoneNumber),
                                new PhoneNumber(fromPhoneNumber),
                                messageText)
                        .create();

                tutorStation.notifyObserver(Trip.getInstance().getVehicle(), "Your vehicle " + Trip.getInstance().getVehicle().getPlate() + " has entered the route " + Trip.getInstance().getRoute().getName());
                showAlert(Alert.AlertType.CONFIRMATION, "Message sent", "Message sent to " + toPhoneNumber);
                System.out.println("[" + new Date() + "] INFO: Message sent to " + toPhoneNumber + " with SID " + message.getSid() + " and status " + message.getStatus());
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/user-view.fxml"));
                Parent root = loader.load();
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();

                stage.setScene(new Scene(root));
            } catch (IOException e) {
                System.out.println("[" + new Date() + "] ERROR: ");
                e.printStackTrace();
            }

            Trip.getInstance().setContinueMoving(true);
            new Thread(() -> {
                while (Trip.getInstance().getContinueMoving()) {
                    moveVehicleContinuously(Trip.getInstance().getVehicle(), Trip.getInstance().getRoute(), getPoliceStationFromDb("Police Station 1"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("[" + new Date() + "] ERROR: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error sending message to " + toPhoneNumber);
            System.out.println("[" + new Date() + "] ERROR: Error sending message to " + toPhoneNumber);
            e.printStackTrace();
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
     * @param vehicle The vehicle to move
     * @param route The route where the vehicle is moving
     * @param policeStation The police station to send the infraction if committed
     */
    private void moveVehicleOnRoute(
            @NotNull Vehicle vehicle,
            @NotNull Route route,
            @NotNull PoliceStation policeStation
    ) {
        Double detectedSpeed = Math.random() * 130.0 + 60.0;

        vehicle.moveVehicle();

        TutorSystem tutorSystem = new TutorSystem();
        tutorSystem.detectSpeed(detectedSpeed, route, vehicle, getPoliceStationFromDb("Police Station 1"));
    }

    /**
     * Function to move a vehicle continuously
     * @param vehicle The vehicle to move
     * @param route The route where the vehicle is moving
     * @param policeStation The police station to send the infraction if committed
     */
    private void moveVehicleContinuously(Vehicle vehicle, Route route, PoliceStation policeStation) {
        while (Trip.getInstance().getContinueMoving()) {
            EnterRouteController controller = new EnterRouteController();
            controller.moveVehicleOnRoute(vehicle, route, policeStation);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("[" + new Date() + "] ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

