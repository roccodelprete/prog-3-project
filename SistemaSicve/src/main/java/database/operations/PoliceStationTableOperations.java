package database.operations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;
import utils.Alert;
import utils.LoggerClass;
import utils.PoliceStation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Class to perform operations on the police_station table
 */
public class PoliceStationTableOperations {
    /**
     * Database connection
     */
    private static final Database db = new Database();

    /**
     * Function to insert a police station into the database
     * @param policeStation The police station to insert
     * @return The police station inserted
     */
    public static @NotNull PoliceStation insertPoliceStationIntoDb(@NotNull PoliceStation policeStation) {
        String insertQuery = "INSERT INTO police_station (name) VALUES (?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery);) {
            preparedStatement.setString(1, policeStation.getName());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            Alert.showAlert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Police station added", "Police station added successfully!");
            LoggerClass.log("Police station inserted into database", LoggerClass.LogType.INFO);
        } catch (Exception e) {
            LoggerClass.log("Error in insert police station into database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return policeStation;
    }

    /**
     * Function to get a police station from the database
     * @param name The name of the police station to get
     * @return The police station from the database if exists, null otherwise
     */
    public static @Nullable PoliceStation getPoliceStationFromDb(String name) {
        String selectQuery = "SELECT * FROM police_station WHERE name = '" + name + "'";

        try (ResultSet resultSet = db.query(selectQuery)) {
            if (resultSet.next()) {
                String policeStationName = resultSet.getString("name");

                return new PoliceStation(policeStationName);
            }

        } catch (Exception e) {
            LoggerClass.log("Error in getting police station from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return null;
    }

    /**
     * Function to gt all the police stations from the database
     * @return The list of police stations from the database
     */
    public static @NotNull ArrayList<PoliceStation> getAllPoliceStationsFromDb() {
        String selectQuery = "SELECT * FROM police_station";

        ArrayList<PoliceStation> policeStations = new ArrayList<>();

        try (ResultSet resultSet = db.query(selectQuery)) {
            while (resultSet.next()) {
                String policeStationName = resultSet.getString("name");

                PoliceStation policeStation = new PoliceStation(policeStationName);

                policeStations.add(policeStation);
            }
        } catch (Exception e) {
            LoggerClass.log("Error in getting police stations from database: " + e.getMessage(), LoggerClass.LogType.ERROR);
        }

        return policeStations;
    }
}
