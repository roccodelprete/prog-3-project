package strategy.pattern;

import javafx.collections.ObservableList;
import observer_memento.pattern.LoggedUser;
import observer_memento.pattern.Vehicle;

import static javafx.collections.FXCollections.observableArrayList;
import static utils.VehicleTableOperations.getUserVehiclesFromDb;

/**
 * Class to create a vehicle table
 * @author Rocco Del Prete
 * @see Table
 */
public class VehicleTable implements Table<Vehicle> {
    /**
     * Vehicles list to be displayed
     */
    private ObservableList<Vehicle> data;

    /**
     * Constructor for VehicleTable
     */
    public VehicleTable() {
        data = observableArrayList();
    }

    /**
     * function to create an observable list.
     * @return the observable list
     */
    @Override
    public ObservableList<Vehicle> create() {
        data.addAll(getUserVehiclesFromDb(LoggedUser.getInstance().getUser()));

        return data;
    }
}
