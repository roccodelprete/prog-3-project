package database.operations;

import singleton.pattern.ConnectionClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to represent the database operations
 * @author Rocco Del Prete
 */
public class Database {
    private final ConnectionClass connection;

    /**
     * Database constructor
     * @see ConnectionClass
     */
    public Database() {
        connection = ConnectionClass.getInstance();
    }

    /**
     * function to do a query
     * @param query The query to do
     * @return The query result
     * @throws SQLException if the query is not valid
     */
    public ResultSet query(String query) throws SQLException {
        Statement statement = connection.getConnection().createStatement();
        return statement.executeQuery(query);
    }

    /**
     * function to do an insert
     * @param query The insert query to do
     * @return The prepared statement to do the insert
     * @throws SQLException if the query is not valid
     */
    public PreparedStatement insert(String query) throws SQLException {
        return connection.getConnection().prepareStatement(query);
    }

    /**
     * function to do an update or delete
     * @param query The update query to do
     * @throws SQLException if the query is not valid
     */
    public void updateOrDelete(String query) throws SQLException {
        try (Statement statement = connection.getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
