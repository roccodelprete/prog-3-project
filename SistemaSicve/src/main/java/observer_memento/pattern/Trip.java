package observer_memento.pattern;

import command.pattern.Route;

/**
 * Class that represents a trip
 * Singleton pattern
 * @author Rocco Del Prete
 */
public class Trip {
    private boolean continueMoving = true;

    /**
     * The trip instance
     */
    private static Trip tripInstance = null;

    /**
     * The start location
     */
    private String startLocation;

    /**
     * The destination location
     */
    private String destinationLocation;

    /**
     * The route
     */
    private Route route;

    /**
     * The vehicle
     */
    private Vehicle vehicle;

    /**
     * The trip constructor
     */
    private Trip() {}

    /**
     * Method that returns the trip instance
     * @return the trip instance
     */
    public static Trip getInstance() {
        if (tripInstance == null) {
            tripInstance = new Trip();
        }
        return tripInstance;
    }

    /**
     * Method that returns the start location
     * @return the start location
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     * Method that sets the start location
     * @param startLocation the start location
     */
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Method that returns the destination location
     * @return the destination location
     */
    public String getDestinationLocation() {
        return destinationLocation;
    }

    /**
     * Method that sets the destination location
     * @param destinationLocation the destination location
     */
    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    /**
     * Method that returns the route
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Method that sets the route
     * @param route the route
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Method that returns the vehicle
     * @return the vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Method that sets the vehicle
     * @param vehicle the vehicle
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Method that returns the continue moving flag
     * @return the continue moving flag
     */
    public boolean getContinueMoving() {
        return continueMoving;
    }

    /**
     * Method that sets the continue moving flag
     * @param continueMoving the continue moving flag
     */
    public void setContinueMoving(boolean continueMoving) {
        this.continueMoving = continueMoving;
    }
}
