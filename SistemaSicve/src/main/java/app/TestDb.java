package app;

import singleton.pattern.ConnectionClass;
import singleton.pattern.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDb {
    public static void main(String[] args) throws SQLException {
        Database db = new Database();

        ResultSet query = db.query("SELECT * FROM sicve.vehicle");

        while (query.next()) {
            System.out.println(query.getString("plate"));
        }
    }
}
