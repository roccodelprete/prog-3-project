package org.sistemasicve.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import strategy.pattern.TableType;
import strategy.pattern.VehicleTable;
import utils.LoggerClass;

import java.io.IOException;

import static utils.Alert.showConfirmationAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.VehicleTableOperations.deleteVehicleFromDb;

public class ViewAllVehiclesController {
    /**
     * The vehicle brand column
     */
    @FXML
    private TableColumn<Vehicle, String> vehicleBrandColumn;

    /**
     * The edit vehicle button
     */
    @FXML
    private Button editVehicleButton;

    /**
     * The vehicle table
     */
    @FXML
    private TableView<Vehicle> vehicleTable;

    /**
     * The delete vehicle button
     */
    @FXML
    private Button deleteVehicleButton;

    /**
     * The vehicle model column
     */
    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;

    /**
     * The vehicle plate column
     */
    @FXML
    private TableColumn<Vehicle, String> vehiclePlateColumn;

    /**
     * The add vehicle button
     */
    @FXML
    private Button addVehicleButton;

    /**
     * The selected vehicle
     */
    private Vehicle selectedVehicle;

    /**
     * The go back button
     */
    @FXML
    private Button goBackButton;


    /**
     * Function to perform when the controller is initialized
     */
    @FXML
    void initialize() {
        editVehicleButton.setVisible(false);
        deleteVehicleButton.setVisible(false);

        TableType<Vehicle> vehicleTableType = new TableType<>(new VehicleTable());
        vehicleTable.setItems(vehicleTableType.getTableElements());

        vehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("plate"));
        vehicleBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        setCursorStyleOnHover(addVehicleButton, Cursor.HAND);
        setCursorStyleOnHover(editVehicleButton, Cursor.HAND);
        setCursorStyleOnHover(deleteVehicleButton, Cursor.HAND);
        setCursorStyleOnHover(goBackButton, Cursor.HAND);

        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedVehicle = newSelection;
                editVehicleButton.setVisible(true);
                deleteVehicleButton.setVisible(true);
            } else {
                selectedVehicle = null;
            }
        });
    }

    /**
     * Function to perform when the delete vehicle button is pressed
     */
    @FXML
    void handleDeleteVehicle() {
        boolean confirmation = showConfirmationAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Are you sure you want to delete this vehicle?");

        if (selectedVehicle != null && confirmation) {
            try {
                deleteVehicleFromDb(selectedVehicle);
            } catch (Exception e) {
                LoggerClass.log("Error deleting vehicle: " + e.getMessage(), LoggerClass.LogType.ERROR);
            } finally {
                vehicleTable.getItems().remove(selectedVehicle);
                selectedVehicle = null;
                editVehicleButton.setVisible(false);
                deleteVehicleButton.setVisible(false);

            }
        }
    }

    /**
     * Function to perform when the edit vehicle button is pressed
     * @param event The event that triggered the function
     */
    @FXML
    void handleEditVehicle(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/edit-vehicle-view.fxml"));
        Parent editVehicleView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        EditVehicleController controller = loader.getController();
        if (selectedVehicle != null) {
            controller.setVehicle(selectedVehicle);
        }

        stage.setTitle("SICVE System - Edit Vehicle");
        stage.setScene(new Scene(editVehicleView));
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Function to perform when the add vehicle button is pressed
     * @param event The event that triggered the function
     */
    @FXML
    void handleAddVehicle(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/vehicle-views/add-vehicle-view.fxml"));
        Parent addRouteView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        AddVehicleController controller = loader.getController();

        stage.setTitle("SICVE System - Add Route");
        stage.setScene(new Scene(addRouteView));
        stage.setResizable(false);

       Vehicle addedVehicle = controller.getAddedVehicle();
        if (addedVehicle != null) {
            setVehicleTableItem(addedVehicle);
        }
    }

    /**
     * Function to perform when the go back button is pressed
     * @param event The event that triggered the function
     */
    @FXML
    void handleGoBack(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/user-view.fxml"));
        Parent root = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    /**
     * Function to set the vehicle table item
     * @param vehicle The vehicle to set
     */
    private void setVehicleTableItem(Vehicle vehicle) {
        vehicleTable.getItems().add(vehicle);
    }
}

