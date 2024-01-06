package command.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import static utils.FormatNumber.formatNumber;

/**
 * The detection class
 * This class is used to represent a detection
 * @author Rocco Del Prete
 */
public class Detection {
    /**
     * The vehicle plate
     */
    private SimpleStringProperty plate;

    /**
     * The route name
     */
    private SimpleStringProperty routeName;

    /**
     * The speed of the vehicle detected
     */
    private SimpleDoubleProperty speed;

    /**
     * Constructor
     * @param plate The vehicle plate
     * @param routeName The route name
     * @param speed The speed of the vehicle detected
     */
    public Detection(String plate, String routeName, double speed) {
        this.plate = new SimpleStringProperty(plate);
        this.routeName = new SimpleStringProperty(routeName);
        this.speed = new SimpleDoubleProperty(speed);
    }

    /**
     * Getter for the vehicle plate
     * @return The vehicle plate
     */
    public String getPlate() {
        return plate.get();
    }

    /**
     * Getter for the route name
     * @return The route name
     */
    public String getRouteName() {
        return routeName.get();
    }

    /**
     * Getter for the speed of the vehicle detected
     * @return The speed of the vehicle detected
     */
    public double getSpeed() {
        return speed.get();
    }
}
