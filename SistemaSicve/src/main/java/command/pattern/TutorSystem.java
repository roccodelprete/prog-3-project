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
    public void getRouteSpeedStatisticsByVehicle(Route route, @NotNull Vehicle vehicle) {
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
     * function to send the most severe infraction to the police station
     */
    public void sendMostSevereInfraction(Vehicle vehicle, PoliceStation policeStation) {
        if (vehicle.getInfractions().size() >= 3) {
            Infraction mostSevereInfraction = vehicle.getMostSevereInfraction();
            System.out.println("Sending most severe infraction committed by " + mostSevereInfraction.getVehicleLicensePlate() + " to police station " + policeStation.getName() + ". " + mostSevereInfraction.getMessage());
        }
    }

    /**
     * function to send all infractions to the police station
     * @param vehicle The vehicle which committed the infractions
     * @param policeStation The police station to send the infractions
     * @param route The route where the infractions were committed
     */
    public void sendAllInfractions(Vehicle vehicle, PoliceStation policeStation, Route route) {
        for (Infraction infraction : vehicle.getInfractions()) {
            System.out.println("Infraction committed by " + infraction.getVehicleLicensePlate() + " on route " + route.getName() + ": " + infraction.getMessage());
        }
        System.out.println("Sending all infractions committed by " + vehicle.getPlate() + " to police station " + policeStation.getName());
    }
}
