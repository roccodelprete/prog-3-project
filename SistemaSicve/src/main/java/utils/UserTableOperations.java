package utils;

import observer_memento.pattern.User;
import org.mindrot.jbcrypt.BCrypt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static utils.Alert.showAlert;

public class UserTableOperations {
    /**
     * The database connection
     */
    private static final Database db = new Database();

    /**
     * function to insert a user into the database
     * @param user The user to insert
     * @return The user inserted into the database
     */
    public static @NotNull User insertUserIntoDb(@NotNull User user) {
        String insertQuery = "INSERT INTO user (email, password, name, surname) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getSurname());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("User " + user.getEmail() + " inserted in the database");
        } catch (Exception e) {
            System.out.println("Error in insert vehicle into database: " + e.getMessage());
        }

        return user;
    }

    /**
     * function to get a user from the database
     * @param email The email of the user to get
     * @return The user from the database if exists, null otherwise
     */
    public static @Nullable User getUserFromDb(String email) {
        String selectQuery = "SELECT * FROM user WHERE email = '" + email + "'";

        try (ResultSet resultSet = db.query(selectQuery)) {
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String storedPassword = resultSet.getString("password");
                boolean sendMeNotification = resultSet.getBoolean("receive_notification");

                return new User(name, surname, email, storedPassword, sendMeNotification);
            }

        } catch (Exception e) {
            System.out.println("Error in getting user from database: " + e.getMessage());
        }

        return null;
    }

    /**
     * function to get all the users from the database
     * @return The users from the database
     */
    public static @NotNull ArrayList<User> getUsersFromDb() {
        String selectQuery = "SELECT * FROM user";

        ArrayList<User> users = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                String storedPassword = resultSet.getString("password");
                boolean sendMeNotification = resultSet.getBoolean("receive_notification");

                users.add(new User(name, surname, email, storedPassword, sendMeNotification));
            }

        } catch (Exception e) {
            System.out.println("Error in getting user from database: " + e.getMessage());
        }

        return users;
    }

    /**
     * function to update the user choice to receive notification
     * @param user The user to update
     * @param choice The new choice to receive notification
     * @return The route updated
     */
    public static @NotNull User updateUserChoiceInDb(@NotNull User user, boolean choice) {
        String updateQuery = "UPDATE user SET receive_notification = " + choice + " WHERE email = '" + user.getEmail() + "'";
        try {
            db.updateOrDelete(updateQuery);

            System.out.println("[" + new Date() + "] User " + user.getEmail() + " updated in the database");
        } catch (SQLException e) {
            System.out.println("[" + new Date() + "] Error in updating user: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot update user: " + e.getMessage());
        } finally {
            user.setSendMeNotification(choice);
        }

        return user;
    }
}
