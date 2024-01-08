package utils;

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
     * The user phone number
     */
    private SimpleStringProperty phoneNumber;

    /**
     * The is admin flag
     */
    private SimpleBooleanProperty isAdmin;

    /**
     * Constructor
     * @param name The name of the user
     * @param surname The user surname
     * @param email The user email
     * @param password The user password
     * @param sendMeNotification The user choice to receive notification
     */
    public User(
            String name,
            String surname,
            String email,
            String password,
            boolean sendMeNotification,
            String phoneNumber,
            boolean isAdmin) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.sendMeNotification = new SimpleBooleanProperty(sendMeNotification);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.isAdmin = new SimpleBooleanProperty(isAdmin);
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

    /**
     * Getter for the phoneNumber
     * @return The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    /**
     * Setter for the phoneNumber
     * @param phoneNumber The new phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    /**
     * Getter for the isAdmin
     * @return The isAdmin
     */
    public boolean getIsAdmin() {
        return isAdmin.get();
    }

    /**
     * Setter for the isAdmin
     * @param isAdmin The new isAdmin
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin.set(isAdmin);
    }
}
