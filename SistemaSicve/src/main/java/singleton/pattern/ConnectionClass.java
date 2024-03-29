package singleton.pattern;

import utils.LoggerClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to represent the connection to the database
 * Singleton pattern
 * @author Rocco Del Prete
 */

public class ConnectionClass {
    /**
     * Static reference to itself
     */
    private static ConnectionClass instance;

    /**
     * Reference to the connection
     */
    private Connection connection;

    /**
     * Private constructor to avoid client applications to use constructor
     */
    private ConnectionClass() {}

    /**
     * function to get the instance of the class
     * @return The instance of the class
     */
    public static ConnectionClass getInstance() {
        if (instance == null) {
            instance = new ConnectionClass();
            instance.connection = instance.connect();
        }

        return instance;
    }

    /**
     * function to get the connection
     */
    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicve","root", "prog-3");
            LoggerClass.log("Database connected", LoggerClass.LogType.INFO);
        }
        catch (SQLException | ClassNotFoundException e) {
            LoggerClass.log("Database connection error: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
