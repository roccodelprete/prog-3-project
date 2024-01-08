package database.operations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.Alert;
import utils.LoggerClass;
import utils.Route;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.Alert.showAlert;

public class RouteTableOperations {
    /**
     * The database connection
     */
    private static final Database db = new Database();

    /**
     * function to insert a vehicle in the database
     * @param route The route to insert
     */
    public static void insertRouteIntoDb(@NotNull Route route) {
        String insertQuery = "INSERT INTO route (name, speed_limit, length, police_station) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = db.insert(insertQuery);

            preparedStatement.setString(1, route.getName());
            preparedStatement.setString(2, route.getSpeedLimit().toString());
            preparedStatement.setString(3, route.getLength().toString());
            preparedStatement.setString(4, route.getPoliceStation());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Success", "Route added successfully!");
            LoggerClass.log("Route " + route.getName() + " inserted into database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot insert route");
            LoggerClass.log("Error in insert route into database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

    }

    /**
     * function to get a route from the database
     * @param name The name of the route
     * @return The route from the database if exists, null otherwise
     */
    public static @Nullable Route getRouteFromDb(String name) {
        String selectQuery = "SELECT * FROM route WHERE name = '" + name + "'";

        try (ResultSet resultSet = db.query(selectQuery)) {
            if (resultSet.next()) {
                String routeName = resultSet.getString("name");
                int speedLimit = resultSet.getInt("speed_limit");
                int length = resultSet.getInt("length");
                String policeStation = resultSet.getString("police_station");


                return new Route(routeName, speedLimit, length, policeStation);
            }
        } catch (Exception e) {
            LoggerClass.log("Error in getting route from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return null;
    }


    /**
     * function to get all routes from the database
     * @return The routes from the database
     */
    public static @NotNull ArrayList<Route> getAllRoutesFromDb() {
        String selectQuery = "SELECT * FROM route";

        ArrayList<Route> routes = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String routeName = resultSet.getString("name");
                int speedLimit = Integer.parseInt(resultSet.getString("speed_limit"));
                int length = Integer.parseInt(resultSet.getString("length"));
                String policeStation = resultSet.getString("police_station");

                routes.add(new Route(routeName, speedLimit, length, policeStation));
            }
        } catch (Exception e) {
            LoggerClass.log("Error in getting all routes from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return routes;
    }

    /**
     * function to update a route in the database
     * @param route The route to update
     * @param name The new route name of the route
     * @param speedLimit The new speed limit of the route
     * @param length The new length of the route
     * @param policeStation The new police station of the route
     * @return The route updated
     */
    public static @NotNull Route updateRouteInDb(@NotNull Route route, String name, Integer speedLimit, Integer length, String policeStation) {
        String updateQuery =
                "UPDATE route SET name = '" + name + "', speed_limit = " + speedLimit + ", length = " + length + "," +
                "police_station = '" + policeStation + "' WHERE name = '" + route.getName() + "'";

        try {
            db.updateOrDelete(updateQuery);
            showAlert(Alert.AlertType.CONFIRMATION, "Success", "Route updated successfully!");
            LoggerClass.log("Route " + route.getName() + " updated in the database", LoggerClass.LogType.INFO);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot update route");
            LoggerClass.log("Error updating route " + route.getName() + " in the database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return route;
    }


    /**
     * function to delete a route from the database
     * @param route The route to delete
     */
    public static void deleteRouteFromDb(@NotNull Route route) {
        String deleteQuery = "DELETE FROM route WHERE name = '" + route.getName() + "'";

        try {
            db.updateOrDelete(deleteQuery);
            showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Success", "Route deleted successfully!");
            LoggerClass.log("Route " + route.getName() + " deleted from the database", LoggerClass.LogType.INFO);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete the route");
            LoggerClass.log("Error deleting route " + route.getName() + " from the database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }
    }
}
