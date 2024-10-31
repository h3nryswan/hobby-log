package com.example.demoplswork;

import com.example.demoplswork.controller.*;
import com.example.demoplswork.model.Logs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.IOException;
import java.sql.SQLException;

/**
 * HelloApplication class is the main entry point for the HobbyLog application.
 * It extends the JavaFX Application class to create a graphical user interface.
 * It has a field to store the primary stage of the application.
 * It has a field to store the logged-in user's ID.
 * It has methods to get and set the logged-in user's ID.
 * It overrides the start method to set up the initial view and stage properties.
 * It has methods to show different views of the application, such as login, home, explore, logs, logs update, create account, and account views.
 * It has a main method to launch the application.
 */
public class    HelloApplication extends Application {
    public static final String TITLE = "Address Book";
    private Stage primaryStage;
    private int loggedInUserID;  // Store the logged-in user's ID

    /**
     * Gets the logged-in user's ID.
     *
     * @return the logged-in user's ID
     */
    public int getLoggedInUserID() {
        return loggedInUserID;
    }

    /**
     * Sets the logged-in user's ID.
     *
     * @param userID the ID of the logged-in user
     */
    public void setLoggedInUserID(int userID) {
        this.loggedInUserID = userID;
    }

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // Get screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the initial view to login-view.fxml
        showLoginView();

        // Set the stage to the full size of the screen, but not in fullscreen mode
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        primaryStage.show();
    }

    /**
     * Shows the Explore view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showExploreView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("explore-view.fxml"));
        Parent root = fxmlLoader.load();
        ExploreView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the Home view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showHomeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Parent root = fxmlLoader.load();
        HomeView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the Logs view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showLogsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("my-logs.fxml"));
        Parent root = fxmlLoader.load();
        LogsView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the Logs Update view.
     *
     * @param id the ID of the log to update
     * @param log the log object to update
     * @throws IOException if an I/O error occurs
     */
    public void showLogsUpdateView(int id, Logs log) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("my-logs-view.fxml"));
        Parent root = fxmlLoader.load();
        LogsUpdateView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        controller.setLogId(id);  // Set the log ID in the controller
        controller.setLog(log);
    }

    /**
     * Shows the Login view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showLoginView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the Create Account view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showCreateAccountView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("create-account-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the Account view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showAccountView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Main method to launch the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}