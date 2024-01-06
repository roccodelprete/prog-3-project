package strategy.pattern;

import javafx.collections.ObservableList;

/**
 * Class to represent Table type.
 * @param <E> the type parameter
 * @see Table
 * @author Rocco Del Prete
 */
public class TableType<E> {
    /**
     * The Table.
     */
    private Table<E> table;

    /**
     * Constructor for TableType
     * @param table the table
     */
    public TableType(Table<E> table) {
        this.table = table;
    }

    /**
     * function to get the observable list of the table.
     * @return the observable list
     */
    public ObservableList<E> getTableElements() {
        return table.create();
    }
}
