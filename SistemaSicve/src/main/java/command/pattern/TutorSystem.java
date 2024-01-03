package command.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;
import utils.CommitInfractionTableOperations;
import utils.FormatNumber;
import utils.RouteTableOperations;

import java.util.*;

/**
 * Class to represent the add route command
 */
public class TutorSystem {
    /**
     * The database operations on the route table
     */
    RouteTableOperations routeTableOperations = new RouteTableOperations();

    /**
     * The database operations on the commit_infraction table
     */
    CommitInfractionTableOperations commitInfractionTableOperations = new CommitInfractionTableOperations();

    /**
     * The routes list
     */
    private ArrayList<Route> routes = new ArrayList<>();

    /**
     * The speeds list for each route
     */
    private Map<Route, ArrayList<SimpleDoubleProperty>> routeSpeeds = new HashMap<>();

    /**
     * The vehicle speeds detected on a route
     */
    private Map<Vehicle, Map<Route, ArrayList<SimpleDoubleProperty>>> vehicleRouteSpeeds = new HashMap<>();

    /**
     * The infractions committed by the vehicle
     */
    private Map<Vehicle, Map<Route, ArrayList<Infraction>>> infractions = new HashMap<>();

    /**
     * function to get the route speeds
     * @return The route speeds
     */
    public Map<Route, ArrayList<SimpleDoubleProperty>> getRouteSpeeds() {
        return routeSpeeds;
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

        commitInfractionTableOperations.insertCommitInfractionIntoDb(infraction);
    }

    /**
     * function to set the route speeds
     * @param routeSpeed The route speed detected
     * @param route The route where the speed was detected
     */
    public void setRouteSpeeds(Double routeSpeed, @NotNull Route route) {
        if (!this.routeSpeeds.containsKey(route)) {
            this.routeSpeeds.put(route, new ArrayList<>());
        }

        this.routeSpeeds.get(route).add(new SimpleDoubleProperty(routeSpeed));
    }

    /**
     * Getter for the speed
     * @return The speed
     */
    public Map<Vehicle, Map<Route, ArrayList<SimpleDoubleProperty>>> getVehicleRouteSpeeds() {
        return vehicleRouteSpeeds;
    }

    /**
     * Setter for the speed of a vehicle on a route
     * @param speed The speed
     * @param route The route
     * @param vehicle The vehicle
     */
    public void setVehicleRouteSpeed(Double speed, Route route, Vehicle vehicle) {
        if (!this.vehicleRouteSpeeds.containsKey(vehicle)) {
            this.vehicleRouteSpeeds.put(vehicle, new HashMap<>());
        }

        if (!this.vehicleRouteSpeeds.get(vehicle).containsKey(route)) {
            this.vehicleRouteSpeeds.get(vehicle).put(route, new ArrayList<>());
        }

        this.vehicleRouteSpeeds.get(vehicle).get(route).add(new SimpleDoubleProperty(speed));
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
        if (routeTableOperations.getRouteFromDb(route.getName()) == null) {
            routeTableOperations.insertRouteIntoDb(route);
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
        Route routeToUpdate = routeTableOperations.getRouteFromDb(existingRoute.getName());

        if (routeToUpdate == null) {
            System.out.println("Error editing route. Route not found in the database.\n");
            return;
        }

        Route editedRoute = routeTableOperations.updateRouteInDb(routeToUpdate, routeName, speedLimit, length);

        routes.remove(existingRoute);
        routes.add(editedRoute);

        System.out.println("Route " + existingRoute.getRoute().get("name") + " parameters edited: " + editedRoute.getRoute() + "\n");
    }

    /**
     * function to get speed statistics for a specific route
     * @param route The route for which speed statistics are required
     * @param vehicle The vehicle for which speed statistics are required
     */
    public void getRouteVehicleSpeedStatistics(@NotNull Route route, @NotNull Vehicle vehicle) {
        Map<Route, ArrayList<SimpleDoubleProperty>> routeSpeeds = this.getVehicleRouteSpeeds().get(vehicle);

        if (routeSpeeds == null || routeSpeeds.get(route) == null || routeSpeeds.get(route).isEmpty()) {
            System.out.println("No vehicles with plate " + vehicle.getPlate() + " on route " + route.getName() + "\n");
            return;
        }

        ArrayList<SimpleDoubleProperty> savedSpeeds = this.getVehicleRouteSpeeds().get(vehicle).get(route);

        double totalSpeed = 0.0;
        double maxSpeed = Double.MIN_VALUE;
        double minSpeed = Double.MAX_VALUE;

        for (SimpleDoubleProperty speed : savedSpeeds) {
            totalSpeed += speed.get();

            if (speed.get() > maxSpeed) {
                maxSpeed = speed.get();
            }

            if (speed.get() < minSpeed) {
                minSpeed = speed.get();
            }
        }

        double averageSpeed = totalSpeed / savedSpeeds.size();

        System.out.println("Speed statistics for route " + route.getName() + " references vehicle with plate " + vehicle.getPlate() + ":");
        System.out.println("Average Speed: " + FormatNumber.formatNumber(averageSpeed) + " km/h");
        System.out.println("Maximum Speed: " + FormatNumber.formatNumber(maxSpeed) + " km/h");
        System.out.println("Minimum Speed: " + FormatNumber.formatNumber(minSpeed) + " km/h");
        System.out.println("\n");
    }

    /**
     * function to get speed statistics for a specific route
     * @param route The route for which speed statistics are required
     */
    public void getRouteSpeedStatistics(@NotNull Route route) {
        ArrayList<SimpleDoubleProperty> savedSpeeds = this.routeSpeeds.get(route);

        if (savedSpeeds == null || savedSpeeds.isEmpty()) {
            System.out.println("No statistics for route " + route.getName());
            return;
        }

        double totalSpeed = 0.0;
        double maxSpeed = Double.MIN_VALUE;
        double minSpeed = Double.MAX_VALUE;

        for (SimpleDoubleProperty speed : savedSpeeds) {
            totalSpeed += speed.get();

            if (speed.get() > maxSpeed) {
                maxSpeed = speed.get();
            }

            if (speed.get() < minSpeed) {
                minSpeed = speed.get();
            }
        }

        double averageSpeed = totalSpeed / savedSpeeds.size();

        System.out.println("Speed statistics for route " + route.getName() + ":");
        System.out.println("Average Speed: " + FormatNumber.formatNumber(averageSpeed) + " km/h");
        System.out.println("Maximum Speed: " + FormatNumber.formatNumber(maxSpeed) + " km/h");
        System.out.println("Minimum Speed: " + FormatNumber.formatNumber(minSpeed) + " km/h");
        System.out.println("\n");
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
        this.setVehicleRouteSpeed(detectedSpeed, route, vehicle);
        this.setRouteSpeeds(detectedSpeed, route);

        String message = "Speeding detected: " + FormatNumber.formatNumber(detectedSpeed) + " km/h - Vehicle: " + vehicle.getPlate() + " - Route: " + route.getName() + " - Speed limit: " + FormatNumber.formatNumber(route.getSpeedLimit()) + " km/h\n";
        System.out.println(message);
        if (detectedSpeed > route.getSpeedLimit()) {
            Infraction infraction = new Infraction(vehicle.getPlate(), detectedSpeed, message, route);
            sendInfractionToPoliceStation(policeStation, infraction);
            this.addInfraction(infraction, route, vehicle);
        }
    }
}
