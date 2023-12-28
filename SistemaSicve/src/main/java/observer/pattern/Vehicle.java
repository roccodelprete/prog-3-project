package observer.pattern;

import command.pattern.Route;
import command.pattern.TutorSystem;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Vehicle (Concrete observer) that receives
 * notifications about events from a TutorStation
 * @author Rocco Del Prete
 */
public class Vehicle implements VehicleObserver {
    /**
     * The vehicle plate
     */
    private SimpleStringProperty plate;

    /**
     * The vehicle speeds detected on a route
     */
    private Map<Vehicle, Map<Route, ArrayList<SimpleDoubleProperty>>> vehicleRouteSpeeds = new HashMap<>();

    /**
     * Constructor
     */
    public Vehicle(String plate) {
        this.plate = new SimpleStringProperty(plate);
    }

    /**
     * Getter for the plate
     * @return The plate
     */
    public String getPlate() {
        return plate.get();
    }

    /**
     * Getter for the speed
     * @return The speed
     */
    public Map<Vehicle, Map<Route, ArrayList<SimpleDoubleProperty>>> getVehicleRouteSpeeds() {
        return vehicleRouteSpeeds;
    }

    /**
     * Setter for the speed
     * @param speed The speed
     */
    public void setVehicleRouteSpeed(Double speed, Route route) {
        if (!this.vehicleRouteSpeeds.containsKey(this)) {
            this.vehicleRouteSpeeds.put(this, new HashMap<>());
        }

        if (!this.vehicleRouteSpeeds.get(this).containsKey(route)) {
            this.vehicleRouteSpeeds.get(this).put(route, new ArrayList<>());
        }

        this.vehicleRouteSpeeds.get(this).get(route).add(new SimpleDoubleProperty(speed));
    }

    /**
     * function to notify the observer
     * @param message The message to send to the observer
     */
    @Override
    public void update(SimpleStringProperty message) {
        System.out.println("Vehicle " + this.plate.get() + " received: " + message.get());
    }

    /**
     * function to handle a pass under the tutor sensor
     * @param detectedSpeed The speed detected
     * @param route The route where the vehicle passed
     * @param tutorSystem The tutor system
     */
    public void handleSpeeding(Double detectedSpeed, @NotNull Route route, @NotNull TutorSystem tutorSystem) {
        this.setVehicleRouteSpeed(detectedSpeed, route);
        tutorSystem.setRouteSpeeds(detectedSpeed, route);

        String message = "Speeding detected: " + detectedSpeed + " km/h - Vehicle: " + this.plate.get() + " - Route: " + route.getName() + " - Speed limit: " + route.getSpeedLimit() + " km/h";
        System.out.println(message);
        if (detectedSpeed > route.getSpeedLimit()) {
            Infraction infraction = new Infraction(this.plate.get(), detectedSpeed, message, route);
            tutorSystem.addInfraction(infraction, route, this);
        }
    }
}
