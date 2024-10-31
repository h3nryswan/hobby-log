package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.ContactDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * LoginController class is the controller for handling user login and account creation.
 * It has fields for the user's email, password, first name, and last name.
 * It has methods to handle user login, account creation, and navigation between views.
 * It has a setApplication method to set the application instance.
 * It has a goToHome method to navigate to the Home view.
 * It has an onLogin method to handle user login.
 * It has an onCreateAccount method to switch to the account creation view.
 * It has an onCancelCreateAccount method to cancel account creation and navigate back to the login view.
 * It has an onCreateAccountSubmit method to handle account creation.
 * It has a showAlert method to show an alert with a specified title and message.
 */
public class LoginController {

    private HelloApplication app;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    @FXML
    private VBox rootVBox;

    private ContactDAO contactDAO;

    /**
     * Constructor for LoginController.
     */
    public LoginController() {
        contactDAO = new ContactDAO();
    }

    /**
     * Sets the application instance.
     *
     * @param app the application instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;

    }

    /**
     * Navigates to the Home view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> rootVBox.requestFocus());
        rootVBox.requestFocus();  // Set the focus on the root node when the view is first opened
    }

    /**
     * Handles user login.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void onLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty.");
            return;
        }

        if (contactDAO.authenticateUser(email, password)) {
            int userID = contactDAO.getUserIDByEmail(email);

            if (userID != -1) {
                app.setLoggedInUserID(userID);
                app.showHomeView();
            } else {
                showAlert("Error", "Unable to retrieve user information.");
            }
        } else {
            showAlert("Error", "Invalid email or password.");
        }
    }

    /**
     * Switches to the account creation view.
     */
    @FXML
    private void onCreateAccount() {
        try {
            app.showCreateAccountView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels account creation and navigates back to the login view.
     */
    @FXML
    private void onCancelCreateAccount() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles account creation.
     */
    @FXML
    private void onCreateAccountSubmit() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
            return;
        }

        if (contactDAO.createAccount(firstName, lastName, email, password)) {
            try {
                app.showLoginView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showAlert("Success", "Account created successfully. You can now log in.");
        } else {
            showAlert("Error", "Account creation failed. Email may already be in use.");
        }
    }

    /**
     * Shows an alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message of the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}