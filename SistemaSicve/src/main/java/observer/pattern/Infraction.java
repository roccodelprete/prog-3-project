package observer.pattern;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class to represent an infraction
 */
public class Infraction {
    /**
     * The infraction description
     */
    private SimpleStringProperty description;

    /**
     * Infraction constructor
     * @param description The infraction description
     */
    public Infraction(String description) {
        this.description = new SimpleStringProperty(description);
    }

    /**
     * function to get the infraction description
     */
    public String getDescription() {
        return description.get();
    }
}
