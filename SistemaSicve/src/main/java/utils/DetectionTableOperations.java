package utils;

import command.pattern.Detection;
import org.jetbrains.annotations.NotNull;
import singleton.pattern.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DetectionTableOperations {
    /**
     * The database connection
     */
    private static final Database db = new Database();

    /**
     * function to insert a detection in the database
     * @param detection The detection to insert
     */
    public static void insertDetectionIntoDb(@NotNull Detection detection) {
        String insertQuery = "INSERT INTO detection (vehicle_plate, route_name, detected_speed) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = db.insert(insertQuery);

            preparedStatement.setString(1, detection.getPlate());
            preparedStatement.setString(2, detection.getRouteName());
            preparedStatement.setInt(3, detection.getSpeed());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            LoggerClass.log("Detection inserted into database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            LoggerClass.log("Error in inserting detection into database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

    }

    /**
     * function to get the detections from the database by vehicle and route
     * @param plate The vehicle plate
     * @param routeName The route name
     * @return The detections from the database
     */
    public static @NotNull ArrayList<Detection> getDetectionByRouteAndVehicleFromDb(String plate, String routeName) {
        String selectQuery = "SELECT * FROM detection WHERE vehicle_plate = '" + plate + "'" +
                " AND route_name = '" + routeName + "'";

        ArrayList<Detection> detections = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                detections.add(new Detection(plate, routeName, resultSet.getInt("detected_speed")));
            }
        } catch (Exception e) {
            LoggerClass.log("Error in getting detection by route and vehicle from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return detections;
    }


    /**
     * function to get all the detections from the database by vehicle
     * @param plate The vehicle plate
     * @return The detections from the database
     */
    public static @NotNull ArrayList<Detection> getAllDetectionsByVehicleFromDb(String plate) {
        String selectQuery = "SELECT * FROM detection WHERE vehicle_plate = '" + plate + "'";

        ArrayList<Detection> detections = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String routeName = resultSet.getString("route_name");

                detections.add(new Detection(plate, routeName, resultSet.getInt("detected_speed")));
            }
        } catch (Exception e) {
            LoggerClass.log("Error in getting detections by vehicle from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return detections;
    }

    /**
     * function to get all the detections from the database by route
     * @param routeName The route name
     * @return The detections from the database
     */
    public static @NotNull ArrayList<Detection> getAllDetectionsByRouteFromDb(String routeName) {
        String selectQuery = "SELECT * FROM detection WHERE route_name = '" + routeName + "'";

        ArrayList<Detection> detections = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String plate = resultSet.getString("vehicle_plate");

                detections.add(new Detection(plate, routeName, resultSet.getInt("detected_speed")));
            }
        } catch (Exception e) {
            LoggerClass.log("Error in getting detections by route from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return detections;
    }
}
