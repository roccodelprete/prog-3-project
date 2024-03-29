package utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Infraction class to represent an infraction
 * @author Rocco Del Prete
 */
public class Infraction {
    /**
     * The vehicle license plate
     */
    private SimpleStringProperty vehiclePlate;

    /**
     * The vehicle speed
     */
    private SimpleIntegerProperty speed;

    /**
     * The infraction message
     */
    private SimpleStringProperty message;

    /**
     * The route where the infraction was committed
     */
    private Route route;

    public Infraction(String vehicleLicensePlate, int speed, String message, Route route) {
        this.vehiclePlate = new SimpleStringProperty(vehicleLicensePlate);
        this.speed = new SimpleIntegerProperty(speed);
        this.message = new SimpleStringProperty(message);
        this.route = route;
    }

    /**
     * function to get the vehicle license plate
     * @return The vehicle license plate
     */
    public String getVehiclePlate() {
        return vehiclePlate.get();
    }

    /**
     * function to get the route
     * @return The route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * function to get the vehicle speed
     * @return The vehicle speed
     */
    public Integer getSpeed() {
        return speed.get();
    }

    /**
     * function to get the infraction message
     * @return The infraction message
     */
    public String getMessage() {
        return message.get();
    }
}