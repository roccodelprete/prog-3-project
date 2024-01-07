package command.pattern;

import javafx.scene.control.Alert;
import observer_memento.pattern.Trip;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.FormatSpeed;
import utils.LoggerClass;

import java.util.*;

import static utils.Alert.showAlert;
import static utils.CommitInfractionTableOperations.insertCommitInfractionIntoDb;
import static utils.DetectionTableOperations.*;
import static utils.FormatSpeed.formatSpeed;
import static utils.RouteTableOperations.*;

/**
 * Class to represent the tutor system
 */
public class TutorSystem {
    /**
     * Map to store the route statistics filtered by vehicle
     */
    private Map<String, Double> routeVehicleStatistics = new HashMap<>();

    /**
     * Map to store the route statistics
     */
    private Map<String, Double> routeStatistics = new HashMap<>();

    /**
     * function to get the route vehicle statistics
     * @return The route vehicle statistics
     */
    public Map<String, Double> getRouteVehicleStatistics() {
        return routeVehicleStatistics;
    }

    /**
     * function to get the route statistics
     * @return The route statistics
     */
    public Map<String, Double> getRouteStatistics() {
        return routeStatistics;
    }

    /**
     * function to add a new route
     * @param route The route to add
     */
    public void addNewRoute(@NotNull Route route) {
        if (getRouteFromDb(route.getName()) == null) {
            insertRouteIntoDb(route);
        }
    }

    /**
     * function to remove a route
     * @param route The route to remove
     */
    public void deleteRoute(@NotNull Route route) {
        deleteRouteFromDb(route);
    }

    /**
     * function to edit a route
     * @param existingRoute The existing route to edit
     * @param routeName The new route name
     * @param speedLimit The new speed limit
     * @param length The new length
     * @param policeStation The new police station
     */
    public void editRoute(
            @NotNull Route existingRoute,
            @Nullable String routeName,
            @Nullable Integer speedLimit,
            @Nullable Integer length,
            @Nullable String policeStation
    ) {
        Route routeToUpdate = getRouteFromDb(existingRoute.getName());

        if (routeToUpdate == null) {
            LoggerClass.log("Route " + existingRoute.getName() + " does not exist in the database", LoggerClass.LogType.ERROR);
            return;
        }

        updateRouteInDb(routeToUpdate, routeName, speedLimit, length, policeStation);
    }

    /**
     * function to get speed statistics for a specific route
     * @param route The route for which speed statistics are required
     * @param vehicle The vehicle for which speed statistics are required
     */
    public void getRouteVehicleStatistics(@NotNull Route route, @NotNull Vehicle vehicle) {
        ArrayList<Detection> detections = getDetectionByRouteAndVehicleFromDb(vehicle.getPlate(), route.getName());

        if (detections.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No statistics for route " + route.getName() + " and vehicle " + vehicle.getPlate());

            routeVehicleStatistics.put("avgSpeed", 0.0);
            routeVehicleStatistics.put("maxSpeed", 0.0);
            routeVehicleStatistics.put("minSpeed", 0.0);

            return;
        }

        double totalSpeed = 0.0;
        double maxSpeed = Double.MIN_VALUE;
        double minSpeed = Double.MAX_VALUE;

        for (Detection detection : detections) {
            totalSpeed += detection.getSpeed();

            if (detection.getSpeed() > maxSpeed) {
                maxSpeed = detection.getSpeed();
            }

            if (detection.getSpeed() < minSpeed) {
                minSpeed = detection.getSpeed();
            }
        }

        double averageSpeed = totalSpeed / detections.size();

        routeVehicleStatistics.put("avgSpeed", averageSpeed);
        routeVehicleStatistics.put("maxSpeed", maxSpeed);
        routeVehicleStatistics.put("minSpeed", minSpeed);
    }

    /**
     * function to get speed statistics for a specific route
     * @param route The route for which speed statistics are required
     */
    public void getRouteStatistics(@NotNull Route route) {
        ArrayList<Detection> detections = getAllDetectionsByRouteFromDb(route.getName());

        if (detections.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No statistics for route " + route.getName());
            routeStatistics.put("avgSpeed", 0.0);
            routeStatistics.put("maxSpeed", 0.0);
            routeStatistics.put("minSpeed", 0.0);

            return;
        }

        double totalSpeed = 0.0;
        double maxSpeed = Double.MIN_VALUE;
        double minSpeed = Double.MAX_VALUE;

        for (Detection detection : detections) {
            totalSpeed += detection.getSpeed();

            if (detection.getSpeed() > maxSpeed) {
                maxSpeed = detection.getSpeed();
            }

            if (detection.getSpeed() < minSpeed) {
                minSpeed = detection.getSpeed();
            }
        }

        double averageSpeed = totalSpeed / detections.size();

        routeStatistics.put("avgSpeed", averageSpeed);
        routeStatistics.put("maxSpeed", maxSpeed);
        routeStatistics.put("minSpeed", minSpeed);
    }

    /**
     * function to detect vehicle speeding
     * @param detectedSpeed The speed detected
     * @param route The route where the vehicle passed
     * @param vehicle The vehicle which passed
     */
    public void detectSpeed(int detectedSpeed, @NotNull Route route, @NotNull Vehicle vehicle) {
        insertDetectionIntoDb(new Detection(vehicle.getPlate(), route.getName(), detectedSpeed));
        String message = "Speeding detected: " + formatSpeed(detectedSpeed, FormatSpeed.SpeedUnit.KMH) + " - Vehicle: " + vehicle.getPlate() + " - Route: " + route.getName() + " - Speed limit: " + formatSpeed(route.getSpeedLimit(), FormatSpeed.SpeedUnit.KMH);
        LoggerClass.log(message, LoggerClass.LogType.INFO);

        if (detectedSpeed > route.getSpeedLimit()) {
            Infraction infraction = new Infraction(vehicle.getPlate(), detectedSpeed, message, route);
            Trip.getInstance().setInfractions(infraction, route);
            insertCommitInfractionIntoDb(infraction);
        }
    }
}
