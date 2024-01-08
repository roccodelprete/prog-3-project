package command.pattern;

import observer_memento.pattern.User;
import utils.LoggerClass;

import java.util.ArrayList;

/**
 * Class to represent the admin
 * Invoker in the Command Pattern
 * @author Rocco Del Prete
 */
public class Admin {
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
}
