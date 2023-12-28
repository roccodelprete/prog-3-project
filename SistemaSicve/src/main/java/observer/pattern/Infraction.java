package observer.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Infraction class to represent an infraction
 */
public class Infraction {
    private SimpleStringProperty vehicleLicensePlate;
    private SimpleDoubleProperty speed;
    private SimpleStringProperty message;

    public Infraction(String vehicleLicensePlate, Double speed, String message) {
        this.vehicleLicensePlate = new SimpleStringProperty(vehicleLicensePlate);
        this.speed = new SimpleDoubleProperty(speed);
        this.message = new SimpleStringProperty(message);
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