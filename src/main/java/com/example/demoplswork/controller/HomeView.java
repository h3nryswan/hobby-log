package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.Analytics;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * HomeView class is the controller for the Home view.
 * It handles the logic for the Home page of the application.
 * It has methods to initialize the view, load logs for the user, and handle user interactions.
 * It has a setApplication method to set the application instance.
 * It has a goToExplore method to navigate to the Explore view.
 * It has a goToLogs method to navigate to the Logs view.
 * It has a showAccountMenu method to display the account menu.
 * It has a goToAccount method to navigate to the Account view.
 * It has an onLogout method to log out the user.
 * It has a goToUpdateLogs method to navigate to the Update Logs view.
 * It has a loadLogsForUser method to load logs for the logged-in user.
 * It has a getRandomLog method to get a random log from the list of logs.
 * It has a getFirstIncompleteToDoItem method to get the first incomplete to-do item from the log.
 * It has a displayFeaturedLog method to display the log in the featured log section.
 */
public class HomeView {
    private HelloApplication app;
    private ContextMenu accountMenu;
    private LogsDAO logsDAO;
    private Analytics analytics;

    @FXML
    private Button accountButton;

    @FXML
    private ImageView featuredLogImageView;
    @FXML
    private Label featuredLogTitle;
    @FXML
    private ProgressBar featuredLogProgress;
    @FXML
    private CheckBox featuredLogToDo;
    @FXML
    private Button featuredLogButton;

    @FXML
    private Label totalSpentLabel;  // Label for Total Spent
    @FXML
    private Label tasksCompletedLabel;  // Label for Tasks Completed
    @FXML
    private Label projectsCompletedLabel;  // Label for Projects Completed
    @FXML
    private Label materialsUsedLabel;  // Label for Materials Used
    @FXML
    private Label totalLikesLabel;  // Label for Total Likes
    @FXML
    private Label totalCommentsLabel;  // Label for Total Comments

    /**
     * Sets the application instance.
     *
     * @param app the application instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;
        loadLogsForUser();
        loadAnalyticsForUser();
    }

    /**
     * Constructor for HomeView.
     *
     * @throws SQLException if a database access error occurs
     */
    public HomeView() throws SQLException {
        logsDAO = new LogsDAO();
        analytics = new Analytics();
    }

    /**
     * Initializes the Home view.
     */
    @FXML
    public void initialize() {
        // Create the dropdown menu
        accountMenu = new ContextMenu();

        MenuItem viewProfile = new MenuItem("View Profile");
        viewProfile.setOnAction(event -> {
            try {
                goToAccount();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem logout = new MenuItem("Log Out");
        logout.setOnAction(event -> onLogout());

        accountMenu.getItems().addAll(viewProfile, logout);


    }

    /**
     * Load and display analytics data for the logged-in user.
     */
    private void loadAnalyticsForUser() {
        int userID = app.getLoggedInUserID();  // Get the logged-in user ID

        // Calculate and display Total Spend
        double totalSpent = analytics.calculateTotalSpend(userID);
        totalSpentLabel.setText(String.format("$%.2f", totalSpent));

        // Calculate and display Tasks Completed
        int tasksCompleted = analytics.calculateTasksCompleted(userID);
        tasksCompletedLabel.setText(String.valueOf(tasksCompleted));

        // Calculate and display Projects Completed
        int projectsCompleted = analytics.calculateProjectsCompleted(userID);
        projectsCompletedLabel.setText(String.valueOf(projectsCompleted));

        // Calculate and display Materials Used
        int materialsUsed = analytics.calculateMaterialsUsed(userID);
        materialsUsedLabel.setText(String.valueOf(materialsUsed));

        // Calculate and display Total Likes
        int totalLikes = analytics.calculateTotalLikes(userID);
        totalLikesLabel.setText(String.valueOf(totalLikes));

        // Calculate and display Total Comments
        int totalComments = analytics.calculateTotalComments(userID);
        totalCommentsLabel.setText(String.valueOf(totalComments));
    }

    /**
     * Loads logs for the logged-in user.
     */
    private void loadLogsForUser() {
        int userID = app.getLoggedInUserID();

        // Fetch logs from the database for the specific user
        List<Object[]> logs = logsDAO.getLogsForUser(userID);

        if (!logs.isEmpty()) {
            // Pick a random log
            Object[] randomLog = getRandomLog(logs);

            // Display the selected log in the featured log section
            displayFeaturedLog(randomLog);
        } else {
            // If there are no logs, show a message indicating no logs exist
            featuredLogTitle.setText("You don't have any logs.");
            featuredLogProgress.setProgress(0.0); // Set progress to 0
            featuredLogImageView.setScaleX(0.4); // Scale to 50% of the original size
            featuredLogImageView.setScaleY(0.4); // Scale to 50% of the original size
            featuredLogImageView.setPreserveRatio(true);

            // Update the button text and action to navigate to the LogsView
            featuredLogButton.setText("My Logs");
            featuredLogButton.setOnAction(event -> {
                try {
                    goToLogs(); // Navigate to LogsView
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
    }

    /**
     * Gets a random log from the list of logs.
     *
     * @param logs the list of logs
     * @return a random log
     */
    private Object[] getRandomLog(List<Object[]> logs) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(logs.size());
        return logs.get(randomIndex);
    }

    /**
     * Gets the first incomplete to-do item from the log.
     *
     * @param log the log
     * @return the first incomplete to-do item, or null if all items are completed
     */
    private String getFirstIncompleteToDoItem(Logs log) {
        List<Pair<String, Boolean>> toDoItems = log.getToDoItems(); // Assuming you have this method
        for (Pair<String, Boolean> item : toDoItems) {
            if (!item.getValue()) { // Check if it's not completed
                return item.getKey(); // Return the first incomplete item
            }
        }
        return null; // or handle the case where all items are completed
    }

    /**
     * Displays the log in the featured log section.
     *
     * @param logData the log data
     */
    private void displayFeaturedLog(Object[] logData) {
        int logID = (int) logData[0]; // Extract the log ID
        Logs log = (Logs) logData[1]; // Extract the Logs object

        // Set log title
        featuredLogTitle.setText(log.getLogName());

        // Set log progress
        featuredLogProgress.setProgress(log.getProgress() / 100.0);

        // Set the image
        if (!log.getImages().isEmpty()) {
            String imageUrl = log.getImages().getFirst();  // Assuming this is a URL

            try {
                // Download the image to a temporary file
                Path tempFile = Files.createTempFile("tempImage", ".jpg");
                try (InputStream in = new URL(imageUrl).openStream()) {
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }

                // Load the image from the local temporary file
                Image image = new Image(tempFile.toUri().toString());
                featuredLogImageView.setImage(image);

                // Delete the temp file on application exit
                tempFile.toFile().deleteOnExit();

            } catch (IOException e) {
                System.out.println("Failed to load image from URL: " + e.getMessage());
                featuredLogImageView.setImage(null); // Clear image view if loading fails
            }
        } else {
            featuredLogImageView.setImage(null); // No images in list
        }

        // Find the first incomplete to-do item
        String incompleteToDo = getFirstIncompleteToDoItem(log);
        if (incompleteToDo != null) {
            featuredLogToDo.setText(incompleteToDo);
        }

        // Set up the "View Log" button
        featuredLogButton.setOnAction(event -> {
            try {
                goToUpdateLogs(logID, log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Navigates to the Explore view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();  // Navigate to Explore view
        }
    }

    /**
     * Navigates to the Logs view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    /**
     * Shows the account menu.
     *
     * @param event the action event
     */
    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }

    /**
     * Navigates to the Account view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }

    /**
     * Logs out the user and navigates to the Login view.
     */
    @FXML
    private void onLogout() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the Update Logs view.
     *
     * @param id  the log ID
     * @param log the log
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToUpdateLogs(int id, Logs log) throws IOException {
        if (app != null) {
            app.showLogsUpdateView(id, log);  // Navigate to Explore view
        }
    }
}