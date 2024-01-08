package singleton.pattern;

import command.pattern.Admin;
import utils.User;

/**
 * Class that represents the logged user
 * Singleton pattern
 * @author Rocco Del Prete
 */
public class LoggedUser {
    /**
     * The logged user
     */
    private static LoggedUser loggedUser;

    /**
     * The user logged
     */
    private User user;

    /**
     * The admin logged
     */
    private Admin admin;

    private LoggedUser() {}

    /**
     * Getter for the logged user
     * @return The logged user
     */
    public static synchronized LoggedUser getInstance() {
        if (loggedUser == null) {
            loggedUser = new LoggedUser();
        }

        return loggedUser;
    }

    /**
     * Getter for the user
     * @return The user logged
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the user
     * @param user The user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Getter for the admin
     * @return The admin logged
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * Setter for the admin
     * @param admin The admin to set
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
