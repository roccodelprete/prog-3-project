package command.pattern;

import observer.pattern.Route;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Class to represent the add route command
 */
public class TutorSystem {
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
    public void addNewRoute (@NotNull Route route) {
        routes.add(route);
        System.out.println("Adding new route " + route.getRoute().get("name"));
    }

    /**
     * function to remove a route
     * @param route The route to remove
     */
    public void removeRoute (@NotNull Route route) {
        routes.remove(route);
        System.out.println("Removing route " + route.getRoute().get("name"));
    }

    /**
     * function to edit a route
     * @param existingRoute The existing route to edit
     * @param editedRoute The edited route
     */
    public void editRoute (@NotNull Route existingRoute, @NotNull Route editedRoute) {
        int index = routes.indexOf(existingRoute);

        if (index != -1) {
            routes.set(index, editedRoute);
            System.out.println("Route " + editedRoute.getRoute().get("name") + " parameters edited: " + editedRoute.getRoute());

            editedRoute.getRoute().forEach((key, value) -> {
                if (key.equals("name")) {
                    existingRoute.setName((String) value);
                } else if (key.equals("speedLimit")) {
                    existingRoute.setSpeedLimit((Integer) value);
                }
            });
        } else {
            System.out.println("Error editing route. Route not found in the system.");
        }
    }

    /**
     * function to get speed statistics of a route
     * @param route The route to get the speed statistics
     */
    public void getSpeedStatistics (@NotNull Route route) {
        System.out.println("Getting speed statistics of route " + route.getRoute().get("name"));
    }
}
