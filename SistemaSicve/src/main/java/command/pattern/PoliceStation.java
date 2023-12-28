package command.pattern;

import javafx.beans.property.SimpleStringProperty;

/**
 * PoliceStation class to represent a police station
 * @author Rocco Del Prete
 */
public class PoliceStation {
    /**
     * The police station name
     */
    private SimpleStringProperty name;

    /**
     * PoliceStation constructor
     * @param name The police station name
     */
    public PoliceStation(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * function to get the police station name
     */
    public String getName() {
        return name.get();
    }
}
