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
    private SimpleStringProperty code;

    /**
     * The police station name
     */
    private SimpleStringProperty name;

    /**
     * Constructor
     * @param code The police station code
     * @param name The police station name
     */
    public PoliceStation(String code, String name) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
    }

    /**
     * function to get the police station code
     * @return The police station code
     */
    public String getCode() {
        return code.get();
    }

    /**
     * function to set the police station code
     * @param code The police station code
     */
    public void setCode(String code) {
        this.code.set(code);
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
