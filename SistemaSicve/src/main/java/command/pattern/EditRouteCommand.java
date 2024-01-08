package command.pattern;

import org.jetbrains.annotations.Nullable;
import utils.Route;

/**
 * Command to edit a route
 * @author Rocco Del Prete
 */
public class EditRouteCommand implements Command {
    /**
     * The admin that will execute the command
     */
    private Admin admin;

    /**
     * The route to edit
     */
    private Route route;

    /**
     * The new route name
     */
    private String newRouteName;

    /**
     * The new route speed limit
     */
    private Integer newRouteSpeedLimit;

    /**
     * The new route length
     */
    private Integer newRouteLength;

    /**
     * The new route police station
     */
    private String newRoutePoliceStation;

    /**
     * EditRouteCommand constructor
     * @param admin The admin that will execute the command
     * @param route The route to edit
     * @param newRouteName The new route name
     * @param newRouteSpeedLimit The new route speed limit
     * @param newRouteLength The new route length
     * @param newRoutePoliceStation The new route police station
     */
    public EditRouteCommand(
            Admin admin,
            Route route,
            String newRouteName,
            Integer newRouteSpeedLimit,
            Integer newRouteLength,
            String newRoutePoliceStation
    ) {
        this.admin = admin;
        this.route = route;
        this.newRouteName = newRouteName;
        this.newRouteSpeedLimit = newRouteSpeedLimit;
        this.newRouteLength = newRouteLength;
        this.newRoutePoliceStation = newRoutePoliceStation;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        admin.editRoute(route, newRouteName, newRouteSpeedLimit, newRouteLength, newRoutePoliceStation);
    }
}
