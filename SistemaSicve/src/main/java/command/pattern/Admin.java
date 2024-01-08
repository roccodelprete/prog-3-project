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
     * The user associated to the admin
     */
    private User user;

    /**
     * Commands list
     */
    private ArrayList<Command> commands = new ArrayList<>();

    /**
     * Constructor
     * @param user The user associated to the admin
     */
    public Admin(User user) {
        this.user = user;
    }

    /**
     * function to add a new command
     * @param command The command to add
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    /**
     * function to execute all commands
     */
    public void executeCommands() {
        for (Command command : commands) {
            command.execute();
            commands.remove(command);
        }
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
