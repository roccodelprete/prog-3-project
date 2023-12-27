package command.pattern;

import observer.pattern.Route;

/**
 * Command to get speed statistics
 */
public class GetSpeedStatisticsCommand implements Command {
    /**
     * The tutor system
     */
    private TutorSystem tutorSystem;

    /**
     * The route to get the speed statistics
     */
    private Route route;

    /**
     * GetSpeedStatisticsCommand constructor
     * @param tutorSystem The tutor system
     */
    public GetSpeedStatisticsCommand(TutorSystem tutorSystem, Route route) {
        this.tutorSystem = tutorSystem;
        this.route = route;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.getSpeedStatistics(route);
    }
}
