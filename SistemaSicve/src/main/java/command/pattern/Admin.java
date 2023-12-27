package command.pattern;

import java.util.ArrayList;

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
     * function to execute all commands
     */
    public void executeCommands() {
        for (Command command : commands) {
            command.execute();
        }
    }

    /**
     * function to execute a command
     * @param command The command to execute
     */
    public void executeCommand(Command command) {
        if (commands.contains(command)) {
            command.execute();
        } else {
            System.out.println("Error executing command! Command not found in the system.");
        }
    }
}
