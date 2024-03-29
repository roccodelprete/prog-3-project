package database.operations;

import observer_memento.pattern.TutorStation;
import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.Alert;
import utils.LoggerClass;
import utils.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import static utils.Alert.showAlert;

/**
 * Class to represent the database operations on the vehicle table
 */
public class VehicleTableOperations {
    /**
     * The database connection
     */
    private static final Database db = new Database();

    /**
     * function to insert a vehicle in the database
     * @param vehicle The vehicle to insert
     * @return The vehicle inserted
     */
    public static @NotNull Vehicle insertVehicleIntoDb(@NotNull Vehicle vehicle) {
        String insertQuery = "INSERT INTO vehicle (plate, brand, model, user_email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery)) {
            preparedStatement.setString(1, vehicle.getPlate());
            preparedStatement.setString(2, vehicle.getBrand());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setString(4, vehicle.getUserEmail());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Success", "Vehicle " + vehicle.getPlate() + " added successfully!");
            LoggerClass.log("Vehicle " + vehicle.getPlate() + " inserted into database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            LoggerClass.log("Error in insert vehicle into database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return vehicle;
    }

    /**
     * function to get a vehicle from the database
     * @param plate The vehicle plate
     * @return The vehicle from the database if exists, null otherwise
     */
    public static @Nullable Vehicle getVehicleFromDb(String plate) {
        String selectQuery = "SELECT * FROM vehicle WHERE plate = '" + plate + "'";

        try (ResultSet resultSet = db.query(selectQuery)) {
            if (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                String userEmail = resultSet.getString("user_email");

                TutorStation tutorStation = new TutorStation();
                return new Vehicle(tutorStation, plate, brand, model, userEmail);
            }

        } catch (Exception e) {
            LoggerClass.log("Error in getting vehicle from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return null;
    }

    /**
     * function to get all vehicle from the database
     * @return The vehicles from the database
     */
    public static @NotNull ArrayList<Vehicle> getVehiclesFromDb() {
        String selectQuery = "SELECT * FROM vehicle";

        ArrayList<Vehicle> vehicles = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String plate = resultSet.getString("plate");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                String userEmail = resultSet.getString("user_email");

                vehicles.add(new Vehicle(new TutorStation(), plate, brand, model, userEmail));
            }

        } catch (Exception e) {
            LoggerClass.log("Error in getting vehicles from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return vehicles;
    }

    /**
     * function to get all the user vehicles from the database
     * @return The vehicles from the database
     */
    public static @NotNull ArrayList<Vehicle> getUserVehiclesFromDb(@NotNull User user) {
        String selectQuery = "SELECT * FROM vehicle WHERE user_email = '" + user.getEmail() + "'";

        ArrayList<Vehicle> vehicles = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String plate = resultSet.getString("plate");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                String userEmail = resultSet.getString("user_email");

                vehicles.add(new Vehicle(new TutorStation(), plate, brand, model, userEmail));
            }

        } catch (Exception e) {
            LoggerClass.log("Error in getting user vehicles from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return vehicles;
    }

    /**
     * function to update a vehicle in the database
     * @param vehicle The vehicle to update
     * @param vehiclePlate The new vehicle plate
     * @param vehicleBrand The new vehicle brand
     * @param vehicleModel The new vehicle model
     * @return The vehicle updated
     */
    public static @NotNull Vehicle updateVehicleInDb(@NotNull Vehicle vehicle, @Nullable String vehiclePlate, @Nullable String vehicleBrand, @Nullable String vehicleModel) {
        String updateQuery = "UPDATE vehicle SET plate = '" + vehiclePlate + "', brand = '" + vehicleBrand + "', model = '" + vehicleModel + "' WHERE plate = '" + vehicle.getPlate() + "'";

        try {
            db.updateOrDelete(updateQuery);
            showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Success", "Vehicle " + vehicle.getPlate() + " updated successfully");
            System.out.println("[" + new Date() + "] Vehicle " + vehiclePlate + " updated in the database");
        } catch (Exception e) {
            showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Cannot update vehicle");
            LoggerClass.log("Error in update vehicle in database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return vehicle;
    }

    /**
     * function to delete a vehicle from the database
     * @param vehicle The vehicle to delete
     */
    public static void deleteVehicleFromDb(@NotNull Vehicle vehicle) {
        String deleteQuery = "DELETE FROM vehicle WHERE plate = '" + vehicle.getPlate() + "'";

        try {
            db.updateOrDelete(deleteQuery);
            showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Success", "Vehicle " + vehicle.getPlate() + " deleted successfully!");
            LoggerClass.log("Vehicle " + vehicle.getPlate() + " deleted from the database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete vehicle");
            LoggerClass.log("Error deleting vehicle " + vehicle.getPlate() + " from the database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }
    }
}
