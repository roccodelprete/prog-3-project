package observer.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import command.pattern.Route;

/**
 * Infraction class to represent an infraction
 * @author Rocco Del Prete
 */
public class Infraction {
    /**
     * The vehicle license plate
     */
    private SimpleStringProperty vehicleLicensePlate;

    /**
     * The vehicle speed
     */
    private SimpleDoubleProperty speed;

    /**
     * The infraction message
     */
    private SimpleStringProperty message;

    /**
     * The route where the infraction was committed
     */
    private Route route;

    public Infraction(String vehicleLicensePlate, Double speed, String message, Route route) {
        this.vehicleLicensePlate = new SimpleStringProperty(vehicleLicensePlate);
        this.speed = new SimpleDoubleProperty(speed);
        this.message = new SimpleStringProperty(message);
        this.route = route;
    }

    /**
     * function to get the vehicle license plate
     * @return The vehicle license plate
     */
    public String getVehicleLicensePlate() {
        return vehicleLicensePlate.get();
    }

    /**
     * function to get the vehicle speed
     * @return The vehicle speed
     */
    public Double getSpeed() {
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