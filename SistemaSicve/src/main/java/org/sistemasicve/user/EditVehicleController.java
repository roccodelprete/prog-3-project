package org.sistemasicve.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import javafx.stage.Stage;
import utils.LoggerClass;

import java.io.IOException;

import static utils.CursorStyle.setCursorStyleOnHover;
import static database.operations.VehicleTableOperations.updateVehicleInDb;

public class EditVehicleController {
    /**
     * The vehicle to edit
     */
    private Vehicle vehicle;

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
     * The edit vehicle button
     */
    @FXML
    private Button editVehicleButton;

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
     * Function to perform when the scene is initialized
     */
    @FXML
    void initialize() {
        setCursorStyleOnHover(cancelButton, Cursor.HAND);
        setCursorStyleOnHover(editVehicleButton, Cursor.HAND);
    }

    /**
     * Function to handle the edit vehicle button
     * @param event The event that triggered the function
     */
    @FXML
    void handleEditVehicle(ActionEvent event) {
        String vehiclePlate = this.vehiclePlate.getText();
        String vehicleBrand = this.vehicleBrand.getText();
        String vehicleModel = this.vehicleModel.getText();

        try {
            updateVehicleInDb(vehicle, vehiclePlate, vehicleBrand, vehicleModel);
            handleOpenVehiclesView(event);
        } catch (Exception e) {
            LoggerClass.log("Error updating vehicle in database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        } finally {
            this.vehiclePlate.clear();
            this.vehicleBrand.clear();
            this.vehicleModel.clear();
        }
    }

    /**
     * Function to handle the cancel button
     * @param event The event that triggered the function
     */
    @FXML
    void handleCancelButton(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/all-vehicles-view.fxml"));
        Parent viewAllVehiclesView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(viewAllVehiclesView));

        stage.show();
    }

    /**
     * Setter for the vehicle
     * @param vehicle The vehicle to set
     */
    public void setVehicle(@NotNull Vehicle vehicle) {
        this.vehicle = vehicle;

        vehicleBrand.setText(vehicle.getBrand());
        vehicleModel.setText(vehicle.getModel());
        vehiclePlate.setText(vehicle.getPlate());
    }

    /**
     * Function to open the vehicles view
     * @param event The event that triggered the function
     */
    private void handleOpenVehiclesView(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/all-vehicles-view.fxml"));
        Parent viewAllVehiclesView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(viewAllVehiclesView));

        stage.show();
    }
}
