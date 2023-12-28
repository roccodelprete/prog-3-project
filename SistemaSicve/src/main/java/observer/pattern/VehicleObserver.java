package observer.pattern;

import javafx.beans.property.SimpleStringProperty;

/**
 * Vehicle observer interface that receives
 * notifications about events from a TutorStation
 */
public interface VehicleObserver {
    /**
     * function to notify the observer
     * @param message The message to send to the observer
     */
    void update(SimpleStringProperty message);
}
