package command.pattern;

import utils.Route;

/**
 * Command to delete a route
 * @author Rocco Del Prete
 */
public class DeleteRouteCommand implements Command {
    /**
     * The admin that will execute the command
     */
    private Admin admin;

    /**
     * The route to remove
     */
    private Route route;

    /**
     * RemoveRouteCommand constructor
     * @param admin The admin that will execute the command
     * @param route The route to remove
     */
    public DeleteRouteCommand(Admin admin, Route route) {
        this.admin = admin;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        admin.deleteRoute(route);
    }
}
