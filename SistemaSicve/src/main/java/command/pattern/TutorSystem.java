package command.pattern;

import javafx.scene.control.Alert;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sistemasicve.admin.GetStatisticsController;
import utils.FormatNumber;

import java.util.*;

import static utils.Alert.showAlert;
import static utils.CommitInfractionTableOperations.insertCommitInfractionIntoDb;
import static utils.DetectionTableOperations.*;
import static utils.FormatNumber.formatNumber;
import static utils.RouteTableOperations.*;

/**
 * Class to represent the add route command
 */
public class TutorSystem {
    /**
     * The routes list
     */
    private ArrayList<Route> routes = new ArrayList<>();

    /**
     * The infractions committed by the vehicle
     */
    private Map<Vehicle, Map<Route, ArrayList<Infraction>>> infractions = new HashMap<>();

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
     * function to get the infractions
     * @return The infractions
     */
    public Map<Vehicle, Map<Route, ArrayList<Infraction>>> getInfractions() {
        return infractions;
    }

    /**
     * function to add a new infraction
     * @param infraction The infraction to add
     * @param route The route where the infraction was committed
     * @param vehicle The vehicle which committed the infraction
     */
    public void addInfraction(@NotNull Infraction infraction, @NotNull Route route, @NotNull Vehicle vehicle) {
        if (!this.infractions.containsKey(vehicle)) {
            this.infractions.put(vehicle, new HashMap<>());
        }

        if (!this.infractions.get(vehicle).containsKey(route)) {
            this.infractions.get(vehicle).put(route, new ArrayList<>());
        }

        this.infractions.get(vehicle).get(route).add(infraction);

        insertCommitInfractionIntoDb(infraction);
    }

    /**
     * function to get the routes
     * @return The routes
     */
    public ArrayList<Route> getRoutes() {
        return routes;
    }

    /**
     * function to add a new route
     * @param route The route to add
     */
    public void addNewRoute(@NotNull Route route) {
        if (getRouteFromDb(route.getName()) == null) {
            insertRouteIntoDb(route);
            System.out.println("Adding route " + route.getRoute().get("name") + "\n");
        }

        routes.add(route);
    }

    /**
     * function to remove a route
     * @param route The route to remove
     */
    public void removeRoute(@NotNull Route route) {
        routes.remove(route);
        System.out.println("Removing route " + route.getRoute().get("name") + "\n");
    }

    /**
     * function to edit a route
     * @param existingRoute The existing route to edit
     * @param routeName The new route name
     * @param speedLimit The new speed limit
     * @param length The new length
     */
    public void editRoute(@NotNull Route existingRoute, @Nullable String routeName, @Nullable Double speedLimit, @Nullable Double length) {
        Route routeToUpdate = getRouteFromDb(existingRoute.getName());

        if (routeToUpdate == null) {
            System.out.println("Error editing route. Route not found in the database.\n");
            return;
        }

        try {
            Route editedRoute = updateRouteInDb(routeToUpdate, routeName, speedLimit, length);

            routes.remove(existingRoute);
            routes.add(editedRoute);

            System.out.println("Route " + existingRoute.getRoute().get("name") + " parameters edited: " + editedRoute.getRoute() + "\n");
        } catch (Exception e) {
            System.out.println("Error editing route: " + e.getMessage() + "\n");
        }
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
     * function to get the most severe infraction
     * @param route The route where the infraction was committed
     * @return The most severe infraction
     */
    public Infraction getMostSevereInfraction(@NotNull Route route, @NotNull Vehicle vehicle) {
        if (this.getInfractions().isEmpty()) {
            return null;
        }

        if (this.getInfractions().get(vehicle) == null || this.getInfractions().get(vehicle).get(route).isEmpty()) {
            System.out.println("No infractions committed on route " + route.getName() + " by vehicle " + vehicle.getPlate() + "\n");
            return null;
        }

        Infraction mostSevereInfraction = this.getInfractions().get(vehicle).get(route).getFirst();
        for (Infraction inf : this.infractions.get(vehicle).get(route)) {
            if (inf.getSpeed() > mostSevereInfraction.getSpeed()) {
                mostSevereInfraction = inf;
            }
        }

        return mostSevereInfraction;
    }

    /**
     * function to send the most severe infraction to the police station
     * @param policeStation The police station to send the infraction
     * @param route The route where the infraction was committed
     * @param vehicle The vehicle which committed the infraction
     */
    public void sendMostSevereInfraction(@NotNull PoliceStation policeStation, @NotNull Route route, @NotNull Vehicle vehicle) {
        if (this.getInfractions().get(vehicle) == null || this.getInfractions().get(vehicle).isEmpty()) {
            System.out.println("No infractions by vehicle " + vehicle.getPlate() + "\n");
            return;
        }

        if (this.getInfractions().get(vehicle).get(route) == null || this.getInfractions().get(vehicle).get(route).isEmpty()) {
            System.out.println("No infractions committed on route " + route.getName() + " by vehicle " + vehicle.getPlate() + "\n");
        }

        if (this.infractions.get(vehicle).get(route).size() >= 3) {
            Infraction mostSevereInfraction = this.getMostSevereInfraction(route, vehicle);
            System.out.println("Sending most severe infraction to police station " + policeStation.getName() + ": " + mostSevereInfraction.getMessage() + "\n");
        }
    }

    /**
     * function to send an infraction to the police station
     * @param policeStation The police station to send the infractions
     * @param infraction The infractions committed by the vehicle
     */
    public void sendInfractionToPoliceStation(@NotNull PoliceStation policeStation, @NotNull Infraction infraction) {
        System.out.println("Sending infraction to police station " + policeStation.getName() + ": " + infraction.getMessage() + "\n");
    }

    /**
     * function to detect vehicle speeding
     * @param detectedSpeed The speed detected
     * @param route The route where the vehicle passed
     * @param vehicle The vehicle which passed
     * @param policeStation The police station to send the infractions
     */
    public void detectSpeed(Double detectedSpeed, @NotNull Route route, @NotNull Vehicle vehicle, @NotNull PoliceStation policeStation) {
        insertDetectionIntoDb(new Detection(vehicle.getPlate(), route.getName(), detectedSpeed));

        String message = "Speeding detected: " + formatNumber(detectedSpeed) + " km/h - Vehicle: " + vehicle.getPlate() + " - Route: " + route.getName() + " - Speed limit: " + formatNumber(route.getSpeedLimit()) + " km/h\n";
        System.out.println(message);
        if (detectedSpeed > route.getSpeedLimit()) {
            Infraction infraction = new Infraction(vehicle.getPlate(), detectedSpeed, message, route);
            sendInfractionToPoliceStation(policeStation, infraction);
            this.addInfraction(infraction, route, vehicle);
        }
    }
}
