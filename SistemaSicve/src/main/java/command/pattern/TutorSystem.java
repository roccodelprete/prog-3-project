package command.pattern;

import singleton.pattern.Trip;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import utils.*;

import java.util.*;

import static database.operations.CommitInfractionTableOperations.insertCommitInfractionIntoDb;
import static database.operations.DetectionTableOperations.*;
import static utils.FormatSpeed.formatSpeed;

/**
 * Class to represent the tutor system
 * Invoker in the Command Pattern
 */
public class TutorSystem {
    /**
     * Commands list
     */
    private ArrayList<Command> commands = new ArrayList<>();

    /**
     * function to add a new command
     * @param command The command to add
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    /**
     * function to execute a command
     * @param command The command to execute
     */
    public void executeCommand(Command command) {
        if (commands.contains(command)) {
            command.execute();
            commands.remove(command);
        } else {
            LoggerClass.log("Command not found", LoggerClass.LogType.ERROR);
        }
    }

    /**
     * function to detect vehicle speeding
     * @param detectedSpeed The speed detected
     * @param route The route where the vehicle passed
     * @param vehicle The vehicle which passed
     */
    public void detectSpeed(int detectedSpeed, @NotNull Route route, @NotNull Vehicle vehicle) {
        insertDetectionIntoDb(new Detection(vehicle.getPlate(), route.getName(), detectedSpeed));
        String message = "Speeding detected: " + formatSpeed(detectedSpeed, FormatSpeed.SpeedUnit.KMH) + " - Vehicle: " + vehicle.getPlate() + " - Route: " + route.getName() + " - Speed limit: " + formatSpeed(route.getSpeedLimit(), FormatSpeed.SpeedUnit.KMH);
        LoggerClass.log(message, LoggerClass.LogType.INFO);

        if (detectedSpeed > route.getSpeedLimit()) {
            Infraction infraction = new Infraction(vehicle.getPlate(), detectedSpeed, message, route);
            Trip.getInstance().setInfractions(infraction, route);
            insertCommitInfractionIntoDb(infraction);
        }
    }
}
