package observer.pattern;

import javafx.beans.property.SimpleStringProperty;

public class PoliceStation extends Tutor {
    /**
     * The police station name
     */
    private String name;

    /**
     * PoliceStation constructor
     * @param name The police station name
     */
    public PoliceStation(String name) {
        this.name = name;
    }

    /**
     * function to get the police station name
     */
    public String getName() {
        return name;
    }

    /**
     * function to set the police station name
     * @param name The police station name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * function to update the auto speed
     * @param newSpeed The new auto speed
     */
    @Override
    public void updateSpeed(AutoVehicle auto, SimpleStringProperty newSpeed) {
        System.out.println("\nThe police station " + name + " has been notified that the auto speed is " + newSpeed.get() + "\n");
    }

    /**
     * function to transcribe the infraction
     * @param infraction The infraction to transcribe
     */
    public void transcribeInfraction(Infraction infraction) {
        System.out.println("Transcribing infraction: " + infraction.getDescription());
    }
}
