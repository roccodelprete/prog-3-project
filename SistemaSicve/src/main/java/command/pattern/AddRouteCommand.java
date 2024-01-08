package command.pattern;

import utils.Route;

/**
 * Command to add a route
 * @author Rocco Del Prete
 */
public class AddRouteCommand implements Command {
    /**
     * The admin that will execute the command
     */
    private Admin admin;

    /**
     * The route to add
     */
    private Route route;

    /**
     * AddRouteCommand constructor
     * @param admin The admin that will execute the command
     * @param route The route to add
     */
    public AddRouteCommand(Admin admin, Route route) {
        this.admin = admin;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        admin.addNewRoute(route);
    }
}
