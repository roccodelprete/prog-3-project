package command.pattern;

import observer.pattern.Route;

/**
 * Command to edit a route
 */
public class EditRouteCommand implements Command {
    /**
     * The tutor system
     */
    private TutorSystem tutorSystem;

    /**
     * The route to edit
     */
    private Route route;

    /**
     * The new route
     */
    private Route newRoute;

    /**
     * EditRouteCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to edit
     * @param newRoute The new route
     */
    public EditRouteCommand(TutorSystem tutorSystem, Route route, Route newRoute) {
        this.tutorSystem = tutorSystem;
        this.route = route;
        this.newRoute = newRoute;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.editRoute(route, newRoute);
    }
}
