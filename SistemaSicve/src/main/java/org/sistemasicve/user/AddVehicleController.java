package org.sistemasicve.user;

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
import observer_memento.pattern.TutorStation;
import singleton.pattern.LoggedUser;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import utils.LoggerClass;

import java.io.IOException;
import java.util.ArrayList;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static database.operations.VehicleTableOperations.getUserVehiclesFromDb;
import static database.operations.VehicleTableOperations.insertVehicleIntoDb;

public class AddVehicleController {
    /**
     * The vehicle brand text field
     */
    @FXML
    private TextField vehicleBrand;

    /**
     * The cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * The vehicle plate text field
     */
    @FXML
    private TextField vehiclePlate;

    /**
     * The vehicle model text field
     */
    @FXML
    private TextField vehicleModel;

    /**
     * The add vehicle button
     */
    @FXML
    private Button addVehicleButton;

    /**
     * The result of the add vehicle operation
     */
    private Vehicle addedVehicle;

    /**
     * Function to perform when the controller is initialized
     */
    @FXML
    void initialize() {
        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(addVehicleButton, Cursor.HAND);

        addVehicleButton.setDisable(true);

        vehiclePlate.textProperty().addListener((observable, oldValue, newValue) -> updateAddButtonState());
        vehicleBrand.textProperty().addListener((observable, oldValue, newValue) -> updateAddButtonState());
        vehicleModel.textProperty().addListener((observable, oldValue, newValue) -> updateAddButtonState());
    }

    /**
     * Function to handle the add vehicle button
     * @param event The event to handle
     */
    @FXML
    void handleAddVehicle(ActionEvent event) {
        ArrayList<Vehicle> vehicles = getUserVehiclesFromDb(LoggedUser.getInstance().getUser());

        Vehicle vehicleToAdd = new Vehicle(new TutorStation(), vehiclePlate.getText(), vehicleBrand.getText(), vehicleModel.getText(), LoggedUser.getInstance().getUser().getEmail());

        if (!vehicles.contains(vehicleToAdd)) {
            try {
                insertVehicleIntoDb(vehicleToAdd);
                addedVehicle = vehicleToAdd;
            } catch (Exception e) {
                LoggerClass.log("Error in inserting vehicle: " + e.getMessage(), LoggerClass.LogType.ERROR);
            } finally {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/all-vehicles-view.fxml"));
                    Parent root = loader.load();

                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();

                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                   LoggerClass.log("Error in loading all vehicles view: " + e.getMessage(), LoggerClass.LogType.ERROR);
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Vehicle " + vehicleToAdd.getPlate() + " already exists!");
            LoggerClass.log("Vehicle " + vehicleToAdd.getPlate() + " already exists!", LoggerClass.LogType.ERROR);
        }
    }

    /**
     * Function to handle the cancel button
     * @param event The event to handle
     */
    @FXML
    void handleCancelButton(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/user-view.fxml"));
        Parent root = loader.load();

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    /**
     * Function to return the added vehicle
     * @return The added vehicle
     */
    public Vehicle getAddedVehicle() {
        return addedVehicle;
    }

    /**
     * Function to update the add button state
     */
    private void updateAddButtonState() {
        boolean isAnyEmpty = vehiclePlate.getText().isEmpty() || vehicleBrand.getText().isEmpty() || vehicleModel.getText().isEmpty();
        addVehicleButton.setDisable(isAnyEmpty);
    }
}
