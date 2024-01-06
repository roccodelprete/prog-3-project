package command.pattern;

import observer_memento.pattern.Vehicle;

/**
 * Command to get speed statistics
 * @author Rocco Del Prete
 */
public class GetRouteVehicleStatisticsCommand implements Command {
    /**
     * The tutor system
     */
    private TutorSystem tutorSystem;

    /**
     * The route to get the speed statistics
     */
    private Route route;

    /**
     * The vehicle which traveled the route
     */
    private Vehicle vehicle;

    /**
     * GetRouteVehicleSpeedStatisticsCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to get the speed statistics
     * @param vehicle The vehicle which traveled the route
     */
    public GetRouteVehicleStatisticsCommand(TutorSystem tutorSystem, Route route, Vehicle vehicle) {
        this.tutorSystem = tutorSystem;
        this.route = route;
        this.vehicle = vehicle;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.getRouteVehicleStatistics(route, vehicle);
    }
}
