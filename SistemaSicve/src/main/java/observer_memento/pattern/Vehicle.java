package observer_memento.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Vehicle (Concrete observer) that receives
 * notifications about events from a TutorStation
 * This class also implements the Memento pattern
 * @author Rocco Del Prete
 */
public class Vehicle implements VehicleObserver {
    /**
     * The vehicle plate
     */
    private SimpleStringProperty plate;

    /**
     * The vehicle brand
     */
    private SimpleStringProperty brand;

    /**
     * The vehicle model
     */
    private SimpleStringProperty model;

    /**
     * The last distance traveled by the vehicle
     */
    private SimpleDoubleProperty lastDistanceTraveled = new SimpleDoubleProperty(0);

    /**
     * The current milestone of the vehicle
     */
    private SimpleDoubleProperty currentMilestone = new SimpleDoubleProperty(0);

    /**
     * The user email who owns the vehicle
     */
    private SimpleStringProperty userEmail;

    /**
     * Constructor
     */
    public Vehicle(String plate, String brand, String model, String userEmail) {
        this.plate = new SimpleStringProperty(plate);
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.userEmail = new SimpleStringProperty(userEmail);
    }

    /**
     * Getter for the user
     * @return The user
     */
    public String getUserEmail() {
        return userEmail.get();
    }

    /**
     * Getter for the plate
     * @return The plate
     */
    public String getPlate() {
        return plate.get();
    }

    /**
     * Setter for the plate
     * @param plate The plate to set
     */
    public void setPlate(String plate) {
        this.plate = new SimpleStringProperty(plate);
    }

    /**
     * Getter for the brand
     * @return The brand
     */
    public String getBrand() {
        return brand.get();
    }

    /**
     * Setter for the brand
     * @param brand The brand to set
     */
    public void setBrand(String brand) {
        this.brand = new SimpleStringProperty(brand);
    }

    /**
     * Getter for the model
     * @return The model
     */
    public String getModel() {
        return model.get();
    }

    /**
     * Setter for the model
     * @param model The model to set
     */
    public void setModel(String model) {
        this.model = new SimpleStringProperty(model);
    }

    /**
     * Getter for the current milestone
     * @return The current milestone
     */
    public Double getCurrentMilestone() {
        return currentMilestone.get();
    }

    /**
     * function to notify the observer
     * @param message The message to send to the observer
     */
    @Override
    public void update(@NotNull SimpleStringProperty message) {
        System.out.println("Vehicle " + this.plate.get() + " received: " + message.get());
    }

    /**
     * function to emulate the vehicle movement
     */
    public void moveVehicle() {
        this.lastDistanceTraveled = new SimpleDoubleProperty(Math.random() * 100);
        this.currentMilestone = new SimpleDoubleProperty(this.currentMilestone.get() + this.lastDistanceTraveled.get());
    }

    /**
     * function to create the vehicle memento
     * @return The vehicle memento
     */
    public Memento createMemento() {
        return new VehicleMemento();
    }

    /**
     * The vehicle memento class
     */
    class VehicleMemento implements Memento {
        /**
         * The memento last distance traveled of the vehicle
         */
        private SimpleDoubleProperty mementoLastDistanceTraveled;

        /**
         * The memento current milestone of the vehicle
         */
        private SimpleDoubleProperty mementoCurrentMilestone;

        /**
         * Constructor
         */
        public VehicleMemento() {
            this.mementoLastDistanceTraveled = new SimpleDoubleProperty(lastDistanceTraveled.get());
            this.mementoCurrentMilestone = new SimpleDoubleProperty(currentMilestone.get());
        }

        /**
         * function to restore the state of the vehicle
         */
        @Override
        public void restore() {
            lastDistanceTraveled = new SimpleDoubleProperty(mementoLastDistanceTraveled.get());
            currentMilestone = new SimpleDoubleProperty(mementoCurrentMilestone.get());
        }
    }
}
