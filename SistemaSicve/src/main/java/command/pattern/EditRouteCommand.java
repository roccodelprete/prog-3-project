package command.pattern;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.Nullable;

/**
 * Command to edit a route
 * @author Rocco Del Prete
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
     * The new route name
     */
    private @Nullable String newRouteName;

    /**
     * The new route speed limit
     */
    private @Nullable Double newRouteSpeedLimit;

    /**
     * The new route length
     */
    private @Nullable Double newRouteLength;

    /**
     * EditRouteCommand constructor
     * @param tutorSystem The tutor system
     * @param route The route to edit
     * @param newRouteName The new route name
     * @param newRouteSpeedLimit The new route speed limit
     * @param newRouteLength The new route length
     */
    public EditRouteCommand(TutorSystem tutorSystem, Route route, @Nullable String newRouteName, @Nullable Double newRouteSpeedLimit, @Nullable Double newRouteLength) {
        this.tutorSystem = tutorSystem;
        this.route = route;
        this.newRouteName = newRouteName;
        this.newRouteSpeedLimit = newRouteSpeedLimit;
        this.newRouteLength = newRouteLength;
    }

    /**
     * function to execute the command
     */
    @Override
    public void execute() {
        tutorSystem.editRoute(route, newRouteName, newRouteSpeedLimit, newRouteLength);
    }
}
