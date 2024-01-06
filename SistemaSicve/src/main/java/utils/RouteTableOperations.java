package utils;

import command.pattern.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
        String insertQuery = "INSERT INTO route (name, speed_limit, length) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = db.insert(insertQuery);

            preparedStatement.setString(1, route.getName());
            preparedStatement.setString(2, route.getSpeedLimit().toString());
            preparedStatement.setString(3, route.getLength().toString());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("Route " + route.getName() + " inserted in the database\n");
        } catch (Exception e) {
            System.out.println("Error in insert route into database: " + e.getMessage());
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
                Double speedLimit = resultSet.getDouble("speed_limit");
                Double length = resultSet.getDouble("length");

                return new Route(routeName, speedLimit, length);
            }
        } catch (Exception e) {
            System.out.println("Error in getting route from database: " + e.getMessage());
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
                Double speedLimit = Double.valueOf(resultSet.getString("speed_limit"));
                Double length = Double.valueOf(resultSet.getString("length"));

                routes.add(new Route(routeName, speedLimit, length));
            }
        } catch (Exception e) {
            System.out.println("Error in getting route from database: " + e.getMessage());
        }

        return routes;
    }

    /**
     * function to update a route in the database
     * @param route The route to update
     * @param name The new route name of the route
     * @param speedLimit The new speed limit of the route
     * @param length The new length of the route
     * @return The route updated
     */
    public static @NotNull Route updateRouteInDb(@NotNull Route route, @Nullable String name, @Nullable Double speedLimit, @Nullable Double length) {
        String updateQuery = null;

        if (name != null && speedLimit != null && length != null) {
            updateQuery = "UPDATE route SET name = '" + name + "', speed_limit = '" + speedLimit + "', length = '" + length + "' WHERE name = '" + route.getName() + "'";
            route.setName(name);
            route.setSpeedLimit(speedLimit);
            route.setLength(length);
        }

        if (name == null && speedLimit != null && length != null) {
            updateQuery = "UPDATE route SET speed_limit = '" + speedLimit + "', length = '" + length + "' WHERE name = '" + route.getName() + "'";
            route.setSpeedLimit(speedLimit);
            route.setLength(length);
        }

        if (name != null && speedLimit == null && length != null) {
            updateQuery = "UPDATE route SET name = '" + name + "', length = '" + length + "' WHERE name = '" + route.getName() + "'";
            route.setName(name);
            route.setLength(length);
        }

        if (name != null && speedLimit != null && length == null) {
            updateQuery = "UPDATE route SET name = '" + name + "', speed_limit = '" + speedLimit + "' WHERE name = '" + route.getName() + "'";
            route.setName(name);
            route.setSpeedLimit(speedLimit);
        }

        if (name == null && speedLimit == null && length != null) {
            updateQuery = "UPDATE route SET length = '" + length + "' WHERE name = '" + route.getName() + "'";
            route.setLength(length);
        }

        if (name == null && speedLimit != null && length == null) {
            updateQuery = "UPDATE route SET speed_limit = '" + speedLimit + "' WHERE name = '" + route.getName() + "'";
            route.setSpeedLimit(speedLimit);
        }

        if (name != null && speedLimit == null && length == null) {
            updateQuery = "UPDATE route SET name = '" + name + "' WHERE name = '" + route.getName() + "'";
            route.setName(name);
        }

        try {
            db.updateOrDelete(updateQuery);

            System.out.println("Route " + route.getName() + " updated in the database\n");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot update route: " + e.getMessage());
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

            System.out.println("Route " + route.getName() + " deleted from the database\n");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete route: " + e.getMessage());
        }
    }
}
