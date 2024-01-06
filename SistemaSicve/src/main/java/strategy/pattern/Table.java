package strategy.pattern;

import javafx.collections.ObservableList;

/**
 * The interface Table.
 * @param <E> the type parameter
 * @author Rocco Del Prete
 */
public interface Table<E> {
    /**
     * function to create an observable list.
     * @return the observable list
     */
    public ObservableList<E> create();
}
