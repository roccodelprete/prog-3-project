package command.pattern;

import observer.pattern.Route;

/**
 * Command to remove a route
 */
public class RemoveRouteCommand implements Command {
    /**
     * The tutor system
     */
    private TutorSystem tutorSystem;

    /**
     * The route to remove
     */
    private Route route;

    /**
     * RemoveRouteCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to remove
     */
    public RemoveRouteCommand(TutorSystem tutorSystem, Route route) {
        this.tutorSystem = tutorSystem;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.removeRoute(route);
    }
}
