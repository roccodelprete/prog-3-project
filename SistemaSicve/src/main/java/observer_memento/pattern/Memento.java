package observer_memento.pattern;

/**
 * Memento interface
 * Used to restore the state of the walker
 * @author Rocco Del Prete
 */
public interface Memento {
    /**
     * function to restore the state of the walker
     */
    void restore();
}
