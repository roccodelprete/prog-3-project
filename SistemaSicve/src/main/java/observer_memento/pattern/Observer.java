package observer_memento.pattern;

import javafx.beans.property.SimpleStringProperty;

/**
 * Vehicle observer interface that receives
 * notifications about events from a TutorStation
 * @see TutorStation
 * @author Rocco Del Prete
 */
public abstract class Observer {
    /**
     * The TutorStation which the observer is subscribed to
     */
    protected TutorStation tutorStation;

    /**
     * function to notify the observer
     * @param message The message to send to the observer
     */
    public abstract void update(SimpleStringProperty message);

    /**
     * Getter for the tutor station
     * @return The user
     */
    public TutorStation getTutorStation() {
        return this.tutorStation;
    }

    /**
     * Setter for the tutor station
     * @param tutorStation The tutor station to set
     */
    public void setTutorStation(TutorStation tutorStation) {
        this.tutorStation = tutorStation;
    }
}
