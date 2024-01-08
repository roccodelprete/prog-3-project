package observer_memento.pattern;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import utils.LoggerClass;

import java.util.ArrayList;

import static utils.Alert.showAlert;

/**
 * TutorStation (Concrete subject) that notifies observers about events
 * @see VehicleObserver
 * @author Rocco Del Prete
 */
public class TutorStation {
    private ArrayList<VehicleObserver> observers = new ArrayList<>();

    /**
     * function to add an observer
     * @param observer The observer to add
     */
    public void attach(VehicleObserver observer) {
        observers.add(observer);
    }

    /**
     * function to remove an observer
     * @param observer The observer to remove
     */
    public void detach(VehicleObserver observer) {
        observers.remove(observer);
    }

    /**
     * function to notify all observers
     * @param message The message to send to the observers
     */
    public void notifyObservers(String message) {
        for (VehicleObserver observer : observers) {
            observer.update(new SimpleStringProperty(message));
        }
    }

    /**
     * function to get the vehicle observers
     * @return The vehicle observers
     */
    public ArrayList<VehicleObserver> getObservers() {
        return this.observers;
    }

    /**
     * function to get a specific vehicle observers
     * @param observer The vehicle observer to get
     * @return The vehicle observer
     */
    public Vehicle getObserver(VehicleObserver observer) {
        return (Vehicle) this.observers.get(this.observers.indexOf(observer));
    }

    /**
     * function to notify a specific observer
     * @param observer The observer to notify
     * @param message The message to send to the observer
     */
    public void notifyObserver(@NotNull VehicleObserver observer, String message) {
        String twilioSID = "AC2eb490c92c14b104496daf9bbc8f681d";
        String twilioToken = "768f4a6fad548897bf23b30ad20a7c82";

        String fromPhoneNumber = "+12019043530";
        String toPhoneNumber = LoggedUser.getInstance().getUser().getPhoneNumber();

        try {
            Twilio.init(twilioSID, twilioToken);

            Message generatedMessage = Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(fromPhoneNumber),
                            message)
                    .create();

            observer.update(new SimpleStringProperty(message));

            showAlert(Alert.AlertType.CONFIRMATION, "Message sent", "Message sent to " + generatedMessage.getTo());
            LoggerClass.log("Message sent to " + generatedMessage.getTo(), LoggerClass.LogType.INFO);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error sending message to " + toPhoneNumber);
            LoggerClass.log("Error sending message to", LoggerClass.LogType.ERROR);
        }
    }
}
