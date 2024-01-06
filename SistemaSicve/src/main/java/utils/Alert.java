package utils;

import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alert extends javafx.scene.control.Alert {
    public Alert(AlertType alertType, String title, String message) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(null);
        this.setContentText(message);
    }

    /**
     * function to show alert
     * @param alertType The alert type
     * @param title The alert title
     * @param message The alert message
     */
    public static void showAlert(AlertType alertType, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show();
    }

    /**
     * function to show a confirmation alert
     * @param alertType The alert type
     * @param title The alert title
     * @param message The alert message
     */
    public static boolean showConfirmationAlert(AlertType alertType, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
