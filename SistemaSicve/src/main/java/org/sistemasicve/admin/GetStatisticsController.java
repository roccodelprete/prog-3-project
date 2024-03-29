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
import singleton.pattern.LoggedUser;
import utils.FormatSpeed;
import utils.Route;

import java.io.IOException;
import java.text.DecimalFormat;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.FormatSpeed.formatSpeed;
import static database.operations.VehicleTableOperations.getVehicleFromDb;
import static database.operations.VehicleTableOperations.getVehiclesFromDb;

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
                TutorSystem tutorSystem = new TutorSystem();

                Command getRouteVehicleStatisticsCommand = new GetRouteVehicleStatisticsCommand(LoggedUser.getInstance().getAdmin(), route, getVehicleFromDb(selectedVehicle));
                tutorSystem.addCommand(getRouteVehicleStatisticsCommand);

                tutorSystem.executeCommand(getRouteVehicleStatisticsCommand);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                String formattedAvgSpeed = decimalFormat.format(LoggedUser.getInstance().getAdmin().getRouteVehicleStatistics().get("avgSpeed")).replace(",", ".");
                String formattedMinSpeed = decimalFormat.format(LoggedUser.getInstance().getAdmin().getRouteVehicleStatistics().get("minSpeed")).replace(",", ".");
                String formattedMaxSpeed = decimalFormat.format(LoggedUser.getInstance().getAdmin().getRouteVehicleStatistics().get("maxSpeed")).replace(",", ".");

                avgVehicleSpeed.setText(formatSpeed(Double.parseDouble(formattedAvgSpeed), FormatSpeed.SpeedUnit.KMH).replace(".", ","));
                minVehicleSpeed.setText(formatSpeed(Double.parseDouble(formattedMinSpeed), FormatSpeed.SpeedUnit.KMH).replace(".", ","));
                maxVehicleSpeed.setText(formatSpeed(Double.parseDouble(formattedMaxSpeed), FormatSpeed.SpeedUnit.KMH).replace(".", ","));
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

        TutorSystem tutorSystem = new TutorSystem();
        Command getRouteStatisticsCommand = new GetRouteStatisticsCommand(LoggedUser.getInstance().getAdmin(), route);

        tutorSystem.addCommand(getRouteStatisticsCommand);
        tutorSystem.executeCommand(getRouteStatisticsCommand);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String formattedAvgSpeed = decimalFormat.format(LoggedUser.getInstance().getAdmin().getRouteStatistics().get("avgSpeed")).replace(",", ".");
        String formattedMinSpeed = decimalFormat.format(LoggedUser.getInstance().getAdmin().getRouteStatistics().get("minSpeed")).replace(",", ".");
        String formattedMaxSpeed = decimalFormat.format(LoggedUser.getInstance().getAdmin().getRouteStatistics().get("maxSpeed")).replace(",", ".");

        avgSpeed.setText(formatSpeed(Double.parseDouble(formattedAvgSpeed), FormatSpeed.SpeedUnit.KMH).replace(".", ","));
        minSpeed.setText(formatSpeed(Double.parseDouble(formattedMinSpeed), FormatSpeed.SpeedUnit.KMH).replace(".", ","));
        maxSpeed.setText(formatSpeed(Double.parseDouble(formattedMaxSpeed), FormatSpeed.SpeedUnit.KMH).replace(".", ","));
    }

    /**
     * Setter for the label title
     * @param title The title to set
     */
    public void setLabelTitle(String title) {
        this.statisticsTitleLabel.setText(title);
    }

    /**
     * Function to handle the go back button
     * @param event The event to handle
     */
    @FXML
    void handleGoBack(@NotNull ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/sistemasicve/route-views/all-routes-view.fxml"));
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
