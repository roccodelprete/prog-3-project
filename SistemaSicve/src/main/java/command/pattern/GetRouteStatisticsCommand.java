package command.pattern;

/**
 * Command to get speed statistics
 * @author Rocco Del Prete
 */
public class GetRouteStatisticsCommand implements Command {
    /**
     * The tutor system
     */
    private TutorSystem tutorSystem;

    /**
     * The route to get the speed statistics
     */
    private Route route;

    /**
     * GetRouteSpeedStatisticsCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to get the speed statistics
     */
    public GetRouteStatisticsCommand(TutorSystem tutorSystem, Route route) {
        this.tutorSystem = tutorSystem;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.getRouteStatistics(route);
    }
}