package command.pattern;

import observer.pattern.Vehicle;

/**
 * Command to get speed statistics
 */
public class GetRouteSpeedStatisticsByVehicleCommand implements Command {
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
     * GetSpeedStatisticsCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to get the speed statistics
     * @param vehicle The vehicle which traveled the route
     */
    public GetRouteSpeedStatisticsByVehicleCommand(TutorSystem tutorSystem, Route route, Vehicle vehicle) {
        this.tutorSystem = tutorSystem;
        this.route = route;
        this.vehicle = vehicle;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.getRouteSpeedStatisticsByVehicle(route, vehicle);
    }
}
