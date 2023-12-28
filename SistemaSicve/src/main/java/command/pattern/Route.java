package command.pattern;

import javafx.beans.property.SimpleDoubleProperty;
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
    private SimpleDoubleProperty speedLimit;

    /**
     * Route constructor
     * @param name The route name
     * @param speedLimit The route speed limit
     */
    public Route(String name, Double speedLimit) {
        this.name = new SimpleStringProperty(name);
        this.speedLimit = new SimpleDoubleProperty(speedLimit);
    }

    /**
     * function to get the route name
     */
    public String getName() {
        return name.get();
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
    public Double getSpeedLimit() {
        return speedLimit.get();
    }

    /**
     * function to set the route speed limit
     * @param speedLimit The route speed limit to set
     */
    public void setSpeedLimit(Double speedLimit) {
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
}
