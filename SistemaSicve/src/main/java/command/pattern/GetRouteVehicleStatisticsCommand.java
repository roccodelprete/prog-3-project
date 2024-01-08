package command.pattern;

import observer_memento.pattern.Vehicle;
import utils.Route;

/**
 * Command to get route speed statistics by vehicle
 * @author Rocco Del Prete
 */
public class GetRouteVehicleStatisticsCommand implements Command {
    /**
     * The admin that will execute the command
     */
    private Admin admin;

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
     * @param admin The admin that will execute the command
     * @param route The route to get the speed statistics
     * @param vehicle The vehicle which traveled the route
     */
    public GetRouteVehicleStatisticsCommand(Admin admin, Route route, Vehicle vehicle) {
        this.admin = admin;
        this.route = route;
        this.vehicle = vehicle;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        admin.getRouteVehicleStatistics(route, vehicle);
    }
}
