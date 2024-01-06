package org.sistemasicve;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import observer_memento.pattern.LoggedUser;
import observer_memento.pattern.User;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

import static utils.Alert.showAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.UserTableOperations.getUserFromDb;
import static utils.UserTableOperations.insertUserIntoDb;

public class HomepageController {
    /**
     * The email text field into login tab
     */
    @FXML
    private TextField loginEmail;

    /**
     * The password text field into sign up tab
     */
    @FXML
    private TextField signUpEmail;

    /**
     * The name text field into sign up tab
     */
    @FXML
    private TextField signUpName;

    /**
     * The login button
     */
    @FXML
    private Button loginButton;

    /**
     * The password text field into sign up tab
     */
    @FXML
    private PasswordField signUpPassword;

    /**
     * The password text field into login tab
     */
    @FXML
    private PasswordField loginPassword;

    /**
     * The sign-up button
     */
    @FXML
    private Button signUpButton;

    /**
     * The surname text field into sign up tab
     */
    @FXML
    private TextField signUpSurname;

    /**
     * function to perform actions when the view is initialized
     */
    @FXML
    void initialize() {
        setCursorStyleOnHover(loginButton, Cursor.HAND);
        setCursorStyleOnHover(signUpButton, Cursor.HAND);
    }

    /**
     * Function to handle the login button
     * @param event The event to handle
     */
    @FXML
    void handleLogin(ActionEvent event) {
        try {
            User user = getUserFromDb(loginEmail.getText());

            if (user != null) {
                try {
                    if (BCrypt.checkpw(loginPassword.getText(), user.getPassword())) {
                        if (user.getEmail().equals("admin@admin.com")) {
                            handleOpenAllRoutesTable(event);
                        } else {
                            LoggedUser.getInstance().setUser(user);
                            System.out.println("[" + new Date() + "] User " + LoggedUser.getInstance().getUser().getEmail() + " logged in");
                            handleOpenUserView(event);
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Wrong password");
                    }
                } catch (Exception e) {
                    System.out.println("[" + new Date() + "] Error in login: ");
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User not found");
            }
        } catch (Exception e) {
            System.out.println("[" + new Date() + "] Error in sign up: ");
            e.printStackTrace();
        }
    }

    /**
     * Function to handle the sign-up button
     * @param event The event to handle
     */
    @FXML
    void handleSignUp(ActionEvent event) {
        try {
            User user = getUserFromDb(loginEmail.getText());

            if (user == null) {
               try {
                   User newUser = insertUserIntoDb(new User(
                      signUpName.getText(),
                      signUpSurname.getText(),
                      signUpEmail.getText(),
                      signUpPassword.getText(),
                       false
                   ));

                   System.out.println("[" + new Date() + "] User " + newUser.getEmail() + " inserted in the database");
                   showAlert(Alert.AlertType.CONFIRMATION, "User created", "User created successfully!");
                   handleOpenUserView(event);
               } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error in creating user: " + e.getMessage());
                    System.out.println("[" + new Date() + "] Error in creating user: " + e.getMessage());
               } finally {
                    signUpEmail.setText("");
                    signUpName.setText("");
                    signUpSurname.setText("");
                    signUpPassword.setText("");
               }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User already exists");
                System.out.println("[" + new Date() + "] User " + user.getEmail() + " already exists in the database");
                signUpEmail.clear();
                signUpName.clear();
                signUpSurname.clear();
                signUpPassword.clear();
            }
        } catch (Exception e) {
            System.out.println("[" + new Date() + "] Error in sign up: ");
            e.printStackTrace();
        }
    }

    /**
     * function to handle the view all routes button
     * @param event The event to handle
     * @throws Exception The exception to throw
     */
    private void handleOpenAllRoutesTable(@NotNull ActionEvent event) throws Exception {
        Parent allRoutesView = FXMLLoader.load(getClass().getResource("/org/sistemasicve/all-routes-view.fxml"));
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setTitle("SICVE System - All Routes");
        stage.setScene(new Scene(allRoutesView));
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Function to open the user view
     * @param event The event to handle
     */
    private void handleOpenUserView(@NotNull ActionEvent event) throws Exception {
        Parent userView = FXMLLoader.load(getClass().getResource("/org/sistemasicve/user-view.fxml"));
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setTitle("SICVE System - User");
        stage.setScene(new Scene(userView));
        stage.setResizable(false);

        stage.show();
    }
}
