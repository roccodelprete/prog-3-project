package command.pattern;

/**
 * Command to delete a route
 * @author Rocco Del Prete
 */
public class DeleteRouteCommand implements Command {
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
    public DeleteRouteCommand(TutorSystem tutorSystem, Route route) {
        this.tutorSystem = tutorSystem;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.deleteRoute(route);
    }
}
