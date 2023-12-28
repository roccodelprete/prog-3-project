package command.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import observer.pattern.Infraction;
import observer.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class to represent the add route command
 */
public class TutorSystem {
    /**
     * The routes list
     */
    private ArrayList<Route> routes = new ArrayList<>();

    /**
     * The speeds list for each route
     */
    private Map<Route, ArrayList<SimpleDoubleProperty>> routeSpeeds = new HashMap<>();

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
        routes.add(route);
        System.out.println("Adding new route " + route.getRoute().get("name"));
    }

    /**
     * function to remove a route
     * @param route The route to remove
     */
    public void removeRoute(@NotNull Route route) {
        routes.remove(route);
        System.out.println("Removing route " + route.getRoute().get("name"));
    }

    /**
     * function to edit a route
     * @param existingRoute The existing route to edit
     * @param editedRoute The edited route
     */
    public void editRoute(@NotNull Route existingRoute, @NotNull Route editedRoute) {
        int index = routes.indexOf(existingRoute);

        if (index != -1) {
            routes.set(index, editedRoute);
            System.out.println("Route " + existingRoute.getRoute().get("name") + " parameters edited: " + editedRoute.getRoute());

            editedRoute.getRoute().forEach((key, value) -> {
                if (key.equals("name")) {
                    existingRoute.setName((String) value);
                } else if (key.equals("speedLimit")) {
                    existingRoute.setSpeedLimit((Double) value);
                }
            });
        } else {
            System.out.println("Error editing route. Route not found in the system.");
        }
    }

    /**
     * function to get speed statistics for a specific route
     * @param route The route for which speed statistics are required
     * @param vehicle The vehicle for which speed statistics are required
     */
    public void getRouteVehicleSpeedStatistics(@NotNull Route route, @NotNull Vehicle vehicle) {
        Map<Route, ArrayList<SimpleDoubleProperty>> routeSpeeds = vehicle.getVehicleRouteSpeeds().get(vehicle);

        if (routeSpeeds == null || routeSpeeds.get(route) == null || routeSpeeds.get(route).isEmpty()) {
            System.out.println("No vehicles with plate " + vehicle.getPlate() + " on route " + route.getName());
            return;
        }

        ArrayList<SimpleDoubleProperty> savedSpeeds = vehicle.getVehicleRouteSpeeds().get(vehicle).get(route);

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
        System.out.println("Average Speed: " + averageSpeed + " km/h");
        System.out.println("Maximum Speed: " + maxSpeed + " km/h");
        System.out.println("Minimum Speed: " + minSpeed + " km/h");
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
        System.out.println("Average Speed: " + averageSpeed + " km/h");
        System.out.println("Maximum Speed: " + maxSpeed + " km/h");
        System.out.println("Minimum Speed: " + minSpeed + " km/h");
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
            System.out.println("No infractions committed on route " + route.getName());
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
        if (this.getInfractions().get(vehicle).get(route) == null || this.getInfractions().get(vehicle).get(route).isEmpty()) {
            return;
        }

        if (this.getInfractions().get(vehicle).get(route).size() >= 3) {
            Infraction mostSevereInfraction = this.getMostSevereInfraction(route, vehicle);
            System.out.println("Sending most severe infraction committed by " + mostSevereInfraction.getVehicleLicensePlate() + " to police station " + policeStation.getName() + ". " + mostSevereInfraction.getMessage());
        }
    }

    /**
     * function to send all infractions to the police station
     * @param vehicle The vehicle which committed the infractions
     * @param policeStation The police station to send the infractions
     * @param route The route where the infractions were committed
     */
    public void sendAllInfractions(@NotNull Vehicle vehicle, @NotNull PoliceStation policeStation, @NotNull Route route) {
        if (this.getInfractions().get(vehicle).get(route) == null || this.getInfractions().get(vehicle).get(route).isEmpty()) {
            System.out.println("No infractions committed on route " + route.getName() + " by vehicle " + vehicle.getPlate());
            return;
        }

        for (Infraction infraction : this.getInfractions().get(vehicle).get(route)) {
            System.out.println("Sending infraction committed by " + infraction.getVehicleLicensePlate() + " to police station " + policeStation.getName() + ". " + infraction.getMessage());
        }
    }
}
