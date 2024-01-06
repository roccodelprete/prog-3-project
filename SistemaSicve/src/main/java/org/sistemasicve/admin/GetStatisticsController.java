package org.sistemasicve.admin;

import command.pattern.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.FormatNumber.formatNumber;
import static utils.UserTableOperations.getUserFromDb;
import static utils.VehicleTableOperations.getVehicleFromDb;
import static utils.VehicleTableOperations.getVehiclesFromDb;

public class GetStatisticsController {
    /**
     * The average vehicle speed text field
     */
    @FXML
    private TextField avgVehicleSpeed;

    /**
     * The minimum vehicle speed text field
     */
    @FXML
    private TextField minVehicleSpeed;

    /**
     * The maximum vehicle speed text field
     */
    @FXML
    private TextField maxVehicleSpeed;

    /**
     * The maximum speed route text field
     */
    @FXML
    private TextField maxSpeed;

    /**
     * The minimum speed route text field
     */
    @FXML
    private TextField minSpeed;

    /**
     * The statistics title label
     */
    @FXML
    private Label statisticsTitleLabel;

    /**
     * The vehicles list combo box
     */
    @FXML
    private ComboBox<String> vehiclesList;

    /**
     * The average speed route text field
     */
    @FXML
    private TextField avgSpeed;

    /**
     * The go back button
     */
    @FXML
    private Button goBackButton;

    /**
     * The calculate statistics button
     */
    @FXML
    private Button calculateStatistics;

    /**
     * The route
     */
    private Route route;

    @FXML
    void initialize() {
        for (Vehicle vehicle : getVehiclesFromDb()) {
            vehiclesList.getItems().add(vehicle.getPlate());
        }

        setCursorStyleOnHover(goBackButton, Cursor.HAND);
        setCursorStyleOnHover(calculateStatistics, Cursor.HAND);
    }

    /**
     * Function to calculate the statistics of the route
     * filtered by vehicle
     */
    @FXML
    void handleCalculateStatistics() {
        String selectedVehicle = vehiclesList.getSelectionModel().getSelectedItem();

        if (selectedVehicle != null) {
            try {
                Admin admin = new Admin(getUserFromDb("admin@admin.com"));
                TutorSystem tutorSystem = new TutorSystem();

                Command getRouteVehicleStatisticsCommand = new GetRouteVehicleStatisticsCommand(tutorSystem, route, getVehicleFromDb(selectedVehicle));
                admin.addCommand(getRouteVehicleStatisticsCommand);

                admin.executeCommand(getRouteVehicleStatisticsCommand);

                avgVehicleSpeed.setText(formatNumber(tutorSystem.getRouteVehicleStatistics().get("avgSpeed")));
                minVehicleSpeed.setText(formatNumber(tutorSystem.getRouteVehicleStatistics().get("minSpeed")));
                maxVehicleSpeed.setText(formatNumber(tutorSystem.getRouteVehicleStatistics().get("maxSpeed")));
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error in getting route vehicle statistics: " + e.getMessage());
            }
        }
    }

    /**
     * Setter for the route
     * @param route The route to set
     */
    public void setRoute(Route route) {
        this.route = route;

        Admin admin = new Admin(getUserFromDb("admin@admin.com"));
        TutorSystem tutorSystem = new TutorSystem();
        Command getRouteStatisticsCommand = new GetRouteStatisticsCommand(tutorSystem, route);

        admin.addCommand(getRouteStatisticsCommand);
        admin.executeCommand(getRouteStatisticsCommand);

        avgSpeed.setText(formatNumber(tutorSystem.getRouteStatistics().get("avgSpeed")));
        minSpeed.setText(formatNumber(tutorSystem.getRouteStatistics().get("minSpeed")));
        maxSpeed.setText(formatNumber(tutorSystem.getRouteStatistics().get("maxSpeed")));
    }

    /**
     * Setter for the label title
     * @param title The title to set
     */
    public void setLabelTitle(String title) {
        this.statisticsTitleLabel.setText(title);
    }

    /**
     * Setter for the route average speed
     * @param avgSpeed The average speed to set
     */
    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed.setText(String.valueOf(avgSpeed));
    }

    /**
     * Setter for the route minimum speed
     * @param minSpeed The minimum speed to set
     */
    public void setMinSpeed(double minSpeed) {
        this.minSpeed.setText(String.valueOf(minSpeed));
    }

    /**
     * Setter for the route maximum speed
     * @param maxSpeed The maximum speed to set
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed.setText(String.valueOf(maxSpeed));
    }

    /**
     * Setter for the vehicle average speed
     * @param avgVehicleSpeed The average speed to set
     */
    public void setAvgVehicleSpeed(double avgVehicleSpeed) {
        this.avgVehicleSpeed.setText(String.valueOf(avgVehicleSpeed));
    }

    /**
     * Setter for the vehicle minimum speed
     * @param minVehicleSpeed The minimum speed to set
     */
    public void setMinVehicleSpeed(double minVehicleSpeed) {
        this.minVehicleSpeed.setText(String.valueOf(minVehicleSpeed));
    }

    /**
     * Setter for the vehicle maximum speed
     * @param maxVehicleSpeed The maximum speed to set
     */
    public void setMaxVehicleSpeed(double maxVehicleSpeed) {
        this.maxVehicleSpeed.setText(String.valueOf(maxVehicleSpeed));
    }

    /**
     * Function to handle the go back button
     * @param event The event to handle
     */
    @FXML
    void handleGoBack(@NotNull ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/all-routes-view.fxml"));
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
