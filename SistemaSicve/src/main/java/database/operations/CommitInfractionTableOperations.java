package database.operations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;
import utils.Infraction;
import utils.LoggerClass;
import utils.Route;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static database.operations.RouteTableOperations.getRouteFromDb;

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

            LoggerClass.log("Infraction inserted into database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            LoggerClass.log("Error in inserting infraction into database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }
    }

    /**
     * Function to get the most serious infraction committed by a vehicle on a route
     * @param vehiclePlate The vehicle plate which committed the infraction
     * @param routeName The route name which the infraction was committed on
     * @return The most serious infraction committed by a vehicle on a route
     */
    public static @Nullable Infraction getMostSeriousInfractionFromDb(@NotNull String vehiclePlate, @NotNull String routeName) {
        String query = "SELECT * FROM commit_infraction WHERE vehicle_plate = ? AND route_name = ? AND date = ? ORDER BY vehicle_speed DESC LIMIT 1";

        try (PreparedStatement preparedStatement = db.insert(query)) {
            preparedStatement.setString(1, vehiclePlate);
            preparedStatement.setString(2, routeName);
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Route route = getRouteFromDb(resultSet.getString("route_name"));

                Infraction infraction = new Infraction(
                        resultSet.getString("vehicle_plate"),
                        resultSet.getInt("vehicle_speed"),
                        resultSet.getString("infraction_description"),
                        route
                );

                resultSet.close();
                preparedStatement.close();

                return infraction;
            }

            resultSet.close();
            preparedStatement.close();

            return null;
        } catch (Exception e) {
            LoggerClass.log("Error in getting most serious infraction from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
            return null;
        }
    }
}
