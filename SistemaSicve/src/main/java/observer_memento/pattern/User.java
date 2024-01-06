package observer_memento.pattern;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    /**
     * The name of the user
     */
    private SimpleStringProperty name;

    /**
     * The user surname
     */
    private SimpleStringProperty surname;

    /**
     * The user email
     */
    private SimpleStringProperty email;

    /**
     * The user password
     */
    private SimpleStringProperty password;

    /**
     * The user choice to receive notification
     */
    private SimpleBooleanProperty sendMeNotification;

    /**
     * Constructor
     * @param name The name of the user
     * @param surname The user surname
     * @param email The user email
     * @param password The user password
     * @param sendMeNotification The user choice to receive notification
     */
    public User(String name, String surname, String email, String password, boolean sendMeNotification) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.sendMeNotification = new SimpleBooleanProperty(sendMeNotification);
    }

    /**
     * Getter for the name
     * @return The name
     */
    public String getUserName() {
        return name.get();
    }

    /**
     * Getter for the surname
     * @return The surname
     */
    public String getSurname() {
        return surname.get();
    }

    /**
     * Getter for the email
     * @return The email
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Getter for the password
     * @return The password
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Setter for the password
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * Getter for the sendMeNotification
     * @return The sendMeNotification
     */
    public boolean getSendMeNotification() {
        return sendMeNotification.get();
    }

    /**
     * Setter for the sendMeNotification
     * @param sendMeNotification The new sendMeNotification
     */
    public void setSendMeNotification(boolean sendMeNotification) {
        this.sendMeNotification.set(sendMeNotification);
    }
}