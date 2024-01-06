package utils;

import command.pattern.Infraction;
import command.pattern.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Class to perform operations on the infraction table
 */
public class CommitInfractionTableOperations {
    /**
     * Database connection
     */
    private static final Database db = new Database();

    /**
     * Function to insert an infraction into the database
     * @param infraction The infraction to insert
     */
    public static void insertCommitInfractionIntoDb(@NotNull Infraction infraction) {
        String insertQuery = "INSERT INTO commit_infraction (vehicle_plate, route_name, infraction_description, vehicle_speed) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery);) {
            preparedStatement.setString(1, infraction.getVehiclePlate());
            preparedStatement.setString(2, infraction.getRoute().getName());
            preparedStatement.setString(3, infraction.getMessage());
            preparedStatement.setDouble(4, infraction.getSpeed());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("Infraction inserted in the database\n");
        } catch (Exception e) {
            System.out.println("Error in insert infraction into database: " + e.getMessage());
        }

    }


    /**
     * Function to get an infraction from the database by vehicle and route
     * @param vehiclePlate The vehicle which committed the infraction
     * @param route The route where the infraction was committed
     * @return The infraction from the database if exists, null otherwise
     */
    public static @Nullable ArrayList<Infraction> getCommitInfractionFromDbByVehicleAndRoute(String vehiclePlate, Route route) {
        String selectQuery = "SELECT * FROM commit_infraction WHERE vehicle_plate = ? AND route_name = ?";

        ArrayList<Infraction> infractions = new ArrayList<>();

        try (PreparedStatement preparedStatement = db.insert(selectQuery)) {
            preparedStatement.setString(1, vehiclePlate);
            preparedStatement.setString(2, route.getName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String plate = resultSet.getString("vehicle_plate");
                    String description = resultSet.getString("infraction_description");
                    Double speed = resultSet.getDouble("vehicle_speed");

                    infractions.add(new Infraction(plate, speed, description, route));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getting infraction by vehicle and route from database: " + e.getMessage());
        }

        return infractions.isEmpty() ? null : infractions;
    }
}
