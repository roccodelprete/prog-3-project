package utils;

import observer_memento.pattern.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Class to represent the database operations on the vehicle table
 */
public class VehicleTableOperations {
    /**
     * The database connection
     */
    private final Database db = new Database();

    /**
     * function to insert a vehicle in the database
     * @param vehicle The vehicle to insert
     * @return The vehicle inserted
     */
    public @NotNull Vehicle insertVehicleIntoDb(@NotNull Vehicle vehicle) {
        String insertQuery = "INSERT INTO vehicle (plate, brand, model) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery)) {
            preparedStatement.setString(1, vehicle.getPlate());
            preparedStatement.setString(2, vehicle.getBrand());
            preparedStatement.setString(3, vehicle.getModel());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("Vehicle " + vehicle.getPlate() + " inserted in the database");
        } catch (Exception e) {
            System.out.println("Error in insert vehicle into database: " + e.getMessage());
        }

        return vehicle;
    }

    /**
     * function to get a vehicle from the database
     * @return The vehicle from the database if exists, null otherwise
     */
    public @Nullable Vehicle getVehicleFromDb(String plate) {
        String selectQuery = "SELECT * FROM vehicle WHERE plate = '" + plate + "'";

        try (ResultSet resultSet = db.query(selectQuery)) {
            if (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");

                return new Vehicle(plate, brand, model);
            }

        } catch (Exception e) {
            System.out.println("Error in getting vehicle from database: " + e.getMessage());
        }

        return null;
    }
}
