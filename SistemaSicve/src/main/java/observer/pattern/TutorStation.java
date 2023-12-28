package observer.pattern;

import command.pattern.Route;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

/**
 * TutorStation (Concrete subject) that notifies observers about events
 * @see VehicleObserver
 * @author Rocco Del Prete
 */
public class TutorStation {
    private ArrayList<VehicleObserver> observers = new ArrayList<>();

    /**
     * function to add an observer
     * @param observer The observer to add
     */
    public void attach(VehicleObserver observer) {
        observers.add(observer);
    }

    /**
     * function to remove an observer
     * @param observer The observer to remove
     */
    public void detach(VehicleObserver observer) {
        observers.remove(observer);
    }

    /**
     * function to notify all observers
     * @param message The message to send to the observers
     */
    public void notifyObservers(String message) {
        for (VehicleObserver observer : observers) {
            observer.update(new SimpleStringProperty(message));
        }
    }

    /**
     * function to get the vehicle observers
     * @return The vehicle observers
     */
    public ArrayList<VehicleObserver> getObservers() {
        return this.observers;
    }
}
