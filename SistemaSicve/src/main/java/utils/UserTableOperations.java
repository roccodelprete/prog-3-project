package utils;

import observer_memento.pattern.LoggedUser;
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
        String insertQuery = "INSERT INTO user (email, password, name, surname, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getSurname());
            preparedStatement.setString(5, user.getPhoneNumber());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            LoggedUser.getInstance().setUser(user);

            showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "User created", "User created successfully!");
            LoggerClass.log("User " + user.getEmail() + " inserted into database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot insert user");
            LoggerClass.log("Error in insert user into database: " + e.getMessage(), LoggerClass.LogType.ERROR);
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
                String phoneNumber = resultSet.getString("phone_number");

                return new User(name, surname, email, storedPassword, sendMeNotification, phoneNumber);
            }

        } catch (Exception e) {
            LoggerClass.log("Error in getting user from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
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
                String phoneNumber = resultSet.getString("phone_number");

                users.add(new User(name, surname, email, storedPassword, sendMeNotification, phoneNumber));
            }

        } catch (Exception e) {
            LoggerClass.log("Error in getting all users from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
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
            LoggerClass.log(user.getEmail() + " user choice updated in database", LoggerClass.LogType.INFO);
        } catch (SQLException e) {
            LoggerClass.log("Error in updating user: " + e.getMessage(), LoggerClass.LogType.ERROR);
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot update user: " + e.getMessage());
        } finally {
            user.setSendMeNotification(choice);
        }

        return user;
    }
}
