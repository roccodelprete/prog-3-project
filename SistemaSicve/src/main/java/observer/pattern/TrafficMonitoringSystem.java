package observer.pattern;

import java.util.ArrayList;
import java.util.Comparator;

public class TrafficMonitoringSystem {
    /**
     * The infractions in the route
     */
    private ArrayList<Infraction> infractions = new ArrayList<>();

    /**
     * function to report an infraction
     * @param infraction The infraction to report
     */
    public void reportInfraction(Infraction infraction) {
        infractions.add(infraction);
    }

    /**
     * function to get the infractions
     * @return The infractions
     */
    public ArrayList<Infraction> getInfractions() {
        return infractions;
    }

    /**
     * function to send the most severe infraction to the police
     * @param policeStation The police station
     */
    public void sendInfractionToPolice(PoliceStation policeStation) {
        if (!infractions.isEmpty()) {
            Infraction mostSevereInfraction = infractions.stream()
                    .max(Comparator.comparing(Infraction::getDescription))
                    .orElse(null);

            policeStation.transcribeInfraction(mostSevereInfraction);
        }
    }
}
