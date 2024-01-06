package org.sistemasicve.user;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import observer_memento.pattern.LoggedUser;
import observer_memento.pattern.Trip;
import observer_memento.pattern.TutorStation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import command.pattern.PoliceStation;
import command.pattern.Route;
import observer_memento.pattern.Vehicle;
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
    void handleEnterRoute(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/enter-route-view.fxml"));
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

            String twilioSID = "AC2eb490c92c14b104496daf9bbc8f681d";
            String twilioToken = "768f4a6fad548897bf23b30ad20a7c82";

            Twilio.init(twilioSID, twilioToken);

            String fromPhoneNumber = "+12019043530";
            String toPhoneNumber = LoggedUser.getInstance().getUser().getPhoneNumber();
            String messageText = "Your vehicle " + Trip.getInstance().getVehicle().getPlate() + " has exited the route " + Trip.getInstance().getRoute().getName();
            TutorStation tutorStation = new TutorStation();
            tutorStation.detach(Trip.getInstance().getVehicle());


            try {
                Platform.runLater(() -> {
                    if (LoggedUser.getInstance().getUser().getSendMeNotification()) {
                        Message message = Message.creator(
                                        new PhoneNumber(toPhoneNumber),
                                        new PhoneNumber(fromPhoneNumber),
                                        messageText)
                                .create();

                        tutorStation.notifyObserver(Trip.getInstance().getVehicle(), "Your vehicle " + Trip.getInstance().getVehicle().getPlate() + " has exited the route " + Trip.getInstance().getRoute().getName());
                        showAlert(Alert.AlertType.CONFIRMATION, "Message sent", "Message sent to " + toPhoneNumber);
                        System.out.println("[" + new Date() + "] INFO: Message sent to " + toPhoneNumber + " with SID " + message.getSid() + " and status " + message.getStatus());
                    }
                });
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error sending message to " + toPhoneNumber);
                System.out.println("[" + new Date() + "] ERROR: Error sending message to " + toPhoneNumber);
                e.printStackTrace();
            }
        }).start();
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
