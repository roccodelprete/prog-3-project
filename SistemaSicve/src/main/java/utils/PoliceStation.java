package utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Police station class to represent a police station
 * @author Rocco Del Prete
 */
public class PoliceStation {
    /**
     * The police station name
     */
    private SimpleStringProperty name;

    /**
     * Constructor
     * @param name The police station name
     */
    public PoliceStation(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * function to get the police station name
     * @return The police station name
     */
    public String getName() {
        return name.get();
    }

    /**
     * function to set the police station name
     * @param name The police station name
     */
    public void setName(String name) {
        this.name.set(name);
    }
}
