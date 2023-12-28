package command.pattern;

/**
 * Command to add a route
 */
public class AddRouteCommand implements Command {
    /**
     * The tutor system
     */
    private TutorSystem tutorSystem;

    /**
     * The route to add
     */
    private Route route;

    /**
     * AddRouteCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to add
     */
    public AddRouteCommand(TutorSystem tutorSystem, Route route) {
        this.tutorSystem = tutorSystem;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.addNewRoute(route);
    }
}
