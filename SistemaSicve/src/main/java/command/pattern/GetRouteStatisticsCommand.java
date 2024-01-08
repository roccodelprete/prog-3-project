package command.pattern;

import utils.Route;

/**
 * Command to route get speed statistics
 * @author Rocco Del Prete
 */
public class GetRouteStatisticsCommand implements Command {
    /**
     * The admin that will execute the command
     */
    private Admin admin;

    /**
     * The route to get the speed statistics
     */
    private Route route;

    /**
     * GetRouteSpeedStatisticsCommand constructor
     * @param admin The admin that will execute the command
     * @param route The route to get the speed statistics
     */
    public GetRouteStatisticsCommand(Admin admin, Route route) {
        this.admin = admin;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        admin.getRouteStatistics(route);
    }
}
