package strategy.pattern;

import utils.Route;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import static javafx.collections.FXCollections.observableArrayList;
import static database.operations.RouteTableOperations.getAllRoutesFromDb;

public class RouteTable implements Table<Route> {
    /**
     * Routes list to be displayed
     */
    @FXML
    private ObservableList<Route> data;

    /**
     * Constructor for RouteTable
     */
    public RouteTable() {
        data = observableArrayList();
    }

    /**
     * function to create an observable list.
     * @return the observable list
     */
    @Override
    public ObservableList<Route> create() {
        data.addAll(getAllRoutesFromDb());

        return data;
    }
}
