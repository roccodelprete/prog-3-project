package observer.pattern;

import javafx.beans.property.SimpleStringProperty;

/**
 * Interface to represent a tutor station and check if the autos speed is correct
 */
public abstract class Tutor {
    /**
     * function to update the auto speed
     * @param auto The auto that triggered the update
     * @param newSpeed The new auto speed
     */
    public abstract void updateSpeed(AutoVehicle auto, SimpleStringProperty newSpeed);
}
