package utils;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

/**
 * Class used to set the cursor style of a button
 * @author Rocco Del Prete
 */
public class CursorStyle {
    public static void setCursorStyleOnHover(@NotNull Button button, Cursor cursor) {
        button.setOnMouseEntered(e -> button.getScene().setCursor(cursor));
        button.setOnMouseExited(e -> button.getScene().setCursor(Cursor.DEFAULT));
    }
}
