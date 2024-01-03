package utils;

import command.pattern.PoliceStation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import singleton.pattern.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Class to perform operations on the police_station table
 */
public class PoliceStationTableOperations {
    /**
     * Database connection
     */
    private final Database db = new Database();

    /**
     * Function to insert a police station into the database
     * @param policeStation The police station to insert
     * @return The police station inserted
     */
    public @NotNull PoliceStation insertPoliceStationIntoDb(@NotNull PoliceStation policeStation) {
        String insertQuery = "INSERT INTO police_station (name) VALUES (?)";

        try (PreparedStatement preparedStatement = db.insert(insertQuery);) {
            preparedStatement.setString(1, policeStation.getName());

            preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("Police station " + policeStation.getName() + " inserted in the database\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return policeStation;
    }

    /**
     * Function to get a police station from the database
     * @param name The name of the police station to get
     * @return The police station from the database if exists, null otherwise
     */
    public @Nullable PoliceStation getPoliceStationFromDb(String name) {
        String selectQuery = "SELECT * FROM police_station WHERE name = '" + name + "'";

        try (ResultSet resultSet = db.query(selectQuery)) {
            if (resultSet.next()) {
                String policeStationName = resultSet.getString("name");

                return new PoliceStation(policeStationName);
            }

        } catch (Exception e) {
            System.out.println("Error in getting police station from database: " + e.getMessage());
        }

        return null;
    }
}
