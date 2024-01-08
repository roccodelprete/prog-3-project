package command.pattern;

import javafx.scene.control.Alert;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import utils.Detection;
import utils.Route;
import utils.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.Alert.showAlert;
import static database.operations.DetectionTableOperations.getAllDetectionsByRouteFromDb;
import static database.operations.DetectionTableOperations.getDetectionByRouteAndVehicleFromDb;
import static database.operations.RouteTableOperations.*;

/**
 * Class to represent the admin
 * Receiver in the Command Pattern
 * @author Rocco Del Prete
 */
public class Admin {
    /**
     * The user who is admin
     */
    private User user;

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
     * Constructor for the admin
     * @param user The user who is admin
     */
    public Admin(@NotNull User user) {
        this.user = user;
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
     *
     * @param existingRoute The existing route to edit
     * @param routeName     The new route name
     * @param speedLimit    The new speed limit
     * @param length        The new length
     * @param policeStation The new police station
     */
    public void editRoute(
            @NotNull Route existingRoute,
            String routeName,
            Integer speedLimit,
            Integer length,
            String policeStation
    ) {
        updateRouteInDb(existingRoute, routeName, speedLimit, length, policeStation);
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
     * function to get the user who is admin
     * @return The user who is admin
     */
    public User getUser() {
        return user;
    }

    /**
     * function to set the user who is admin
     * @param user The user who is admin
     */
    public void setUser(User user) {
        this.user = user;
    }
}
