package observer.pattern;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class to represent a route with tutor stations
 */
public class Route {
    /**
     * The route name
     */
    private SimpleStringProperty name;
    /**
     * The route speed limit
     */
    private SimpleIntegerProperty speedLimit;

    /**
     * The traffic monitoring system
     */
    private TrafficMonitoringSystem trafficMonitoringSystem;

    /**
     * The tutor stations in the route
     */
    private ArrayList<Tutor> stations = new ArrayList<>();

    /**
     * Route constructor
     * @param name The route name
     * @param speedLimit The route speed limit
     */
    public Route(String name, Integer speedLimit, TrafficMonitoringSystem trafficMonitoringSystem) {
        this.name = new SimpleStringProperty(name);
        this.speedLimit = new SimpleIntegerProperty(speedLimit);
        this.trafficMonitoringSystem = trafficMonitoringSystem;
    }

    /**
     * function to set the route name
     * @param name The route name to set
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * function to get the route speed limit
     */
    public Integer getSpeedLimit() {
        return speedLimit.get();
    }

    /**
     * function to set the route speed limit
     * @param speedLimit The route speed limit to set
     */
    public void setSpeedLimit(int speedLimit) {
        this.speedLimit.set(speedLimit);
    }

    /**
     * function to get the route parameters
     */
    public Map<String, Object> getRoute() {
        return Map.of(
                "name", name.get(),
                "speedLimit", speedLimit.get()
        );
    }

    /**
     * function to add a tutor station
     * @param newTutor The tutor station to add
     */
    public void addTutor(Tutor newTutor) {
        stations.add(newTutor);
    }

    /**
     * function to remove a tutor station
     * @param tutorToRemove The tutor station to remove
     */
    public void removeTutor(Tutor tutorToRemove) {
        stations.remove(tutorToRemove);
    }

    /**
     * function to notify autos when entering the route with a tutor station
     * @param auto The auto that is entering the route
     * @param newSpeed The new speed
     */
    public void notifyAuto(AutoVehicle auto, SimpleStringProperty newSpeed) {
        auto.updateSpeed(auto, newSpeed);
    }
}
