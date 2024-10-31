package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.LogEvent;
import com.example.demoplswork.events.LogEventDAO;
import com.example.demoplswork.events.ProgressLog;
import com.example.demoplswork.events.StartEvent;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



/**
 * LogsView class is the controller for managing the Logs View.
 * It handles the display and interaction with logs in the application.
 * It has methods to set the application instance, load logs for the current user, and initialize the view.
 * It has methods to navigate to different views such as Home, Explore, Logs Update, and Account.
 * It has methods to handle user interactions such as adding new projects, renaming logs, and deleting logs.
 * It has methods to create log modules from the database and rearrange the grid after log deletion.
 * It has a createNewLog method to create a new log and insert it into the database.
 * It has an addEventToProgressLog method to add events to the progress log.
 * It has a getProgressLog method to retrieve the progress log.
 */
public class LogsView {
    private HelloApplication app;

    @FXML
    private GridPane projectsGrid;  // The GridPane where logs will be added

    @FXML
    private Label logNameText;

    private int currentRow = 0;  // Track the current row in the grid
    private int currentColumn = 0;  // Track the current column in the grid

    private static final int LOGS_PER_ROW = 3;  // Number of logs per row

    private ContextMenu accountMenu;

    @FXML
    private Button accountButton;

    private LogsDAO logsDAO;

    private ProgressLog progressLog;
    private LogEventDAO logEventDAO;

    /**
     * Constructor for LogsView.
     * Initializes the ProgressLog, LogsDAO, and LogEventDAO.
     *
     * @throws SQLException if a database access error occurs
     */
    public LogsView() throws SQLException {
        this.progressLog = new ProgressLog();
        logsDAO = new LogsDAO();
        logEventDAO = new LogEventDAO();
    }

    /**
     * Sets the application instance and loads logs for the current user.
     *
     * @param app the HelloApplication instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;
        loadLogsForUser();
    }

    /**
     * Initializes the LogsView.
     * Sets up the account menu with profile and logout options.
     */
    @FXML
    public void initialize() {
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
     * Navigates to the Home view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    /**
     * Navigates to the Explore view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
        }
    }

    /**
     * Navigates to the Logs Update view.
     *
     * @param id the log ID
     * @param log the Logs object
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToUpdateLogs(int id, Logs log) throws IOException {
        if (app != null) {
            app.showLogsUpdateView(id, log);
        }
    }

    /**
     * Shows the account menu.
     *
     * @param event the ActionEvent
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
     * Handles the creation of a new log.
     */
    @FXML
    public void handleAddNewProject() {
        createNewLog();
    }

    /**
     * Loads logs for the current user from the database and displays them in the GridPane.
     */
    private void loadLogsForUser() {
        int userID = app.getLoggedInUserID();
        List<Object[]> logsList = logsDAO.getLogsForUser(userID);

        for (Object[] logData : logsList) {
            int logID = (int) logData[0];
            Logs log = (Logs) logData[1];
            VBox logModule = createLogModuleFromDatabase(log, logID);
            projectsGrid.add(logModule, currentColumn, currentRow);

            currentColumn++;
            if (currentColumn >= LOGS_PER_ROW) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }

    /**
     * Creates a log module from the database and returns it as a VBox.
     *
     * @param log the Logs object
     * @param logID the log ID
     * @return the VBox containing the log module
     */
    private VBox createLogModuleFromDatabase(Logs log, int logID) {
        VBox logModule = new VBox();
        logModule.setSpacing(0);

        StackPane imageContainer = new StackPane();
        Rectangle imageBackground = new Rectangle(250, 200);
        imageBackground.setFill(javafx.scene.paint.Color.WHITE);
        imageBackground.setStroke(javafx.scene.paint.Color.BLACK);

        String imageUrl = log.getImages().getFirst();  // Assuming this is a URL

        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(false);

        try {
            // Download the image to a temporary file
            Path tempFile = Files.createTempFile("tempImage", ".jpg");
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Load the image from the local temporary file
            Image image = new Image(tempFile.toUri().toString());
            imageView.setImage(image);

            // Delete the temp file on application exit
            tempFile.toFile().deleteOnExit();

        } catch (IOException e) {
            System.out.println("Failed to load image from URL: " + e.getMessage());
            imageView.setImage(null); // Clear image view if loading fails
        }

        imageContainer.getChildren().addAll(imageBackground, imageView);

        StackPane titleContainer = new StackPane();
        Rectangle titleBackground = new Rectangle(250, 40);
        titleBackground.setFill(javafx.scene.paint.Color.web("#ffee00"));
        titleBackground.setStroke(javafx.scene.paint.Color.BLACK);

        logNameText = new Label(log.getLogName());
        logNameText.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-text-fill: black;");
        titleContainer.getChildren().addAll(titleBackground, logNameText);

        ProgressBar progressBar = new ProgressBar(log.getProgress() / 100.0);
        progressBar.setPrefWidth(250);
        progressBar.setPrefHeight(40);
        progressBar.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        StackPane buttonContainer = new StackPane();
        Rectangle buttonBackground = new Rectangle(250, 100);
        buttonBackground.setFill(javafx.scene.paint.Color.BLACK);
        buttonBackground.setStroke(javafx.scene.paint.Color.BLACK);

        VBox buttonsVBox = new VBox(10);
        buttonsVBox.setAlignment(Pos.CENTER);
        Button viewButton = new Button("View Log");
        viewButton.setPrefWidth(190);
        viewButton.setPrefHeight(35);
        viewButton.setStyle("-fx-background-color: #d3d3d3;");
        viewButton.setOnAction(actionEvent -> {
            try {
                goToUpdateLogs(logID, log);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        Button editButton = new Button("Rename Log");
        editButton.setOnAction(event -> renameLog(logID, log));
        editButton.setPrefWidth(90);
        editButton.setPrefHeight(35);
        editButton.setStyle("-fx-background-color: #ffffff;");

        Button deleteButton = new Button("Delete Log");
        deleteButton.setPrefWidth(90);
        deleteButton.setPrefHeight(35);
        deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");

        buttonsHBox.getChildren().addAll(editButton, deleteButton);
        buttonsVBox.getChildren().addAll(viewButton, buttonsHBox);
        buttonContainer.getChildren().addAll(buttonBackground, buttonsVBox);
        logModule.getChildren().addAll(imageContainer, titleContainer, progressBar, buttonContainer);

        deleteButton.setOnAction(event -> handleDeleteLog(logID, log, logModule));

        return logModule;
    }

    /**
     * Renames the specified log.
     *
     * @param currentLogId the current log ID
     * @param log the Logs object
     */
    private void renameLog(int currentLogId, Logs log) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Log");
        dialog.setHeaderText("Rename Log");
        dialog.setContentText("New log name:");
        dialog.getEditor().setText(log.getLogName());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newLogName = result.get().trim();

            if (newLogName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Log name cannot be empty!");
                alert.showAndWait();
                return;
            }

            try {
                logsDAO.updateLogName(currentLogId, newLogName);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Log name updated successfully!");
                logNameText.setText(newLogName);
                log.setLogName(newLogName);
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update log name. Please try again.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the deletion of a log.
     *
     * @param logID the log ID
     * @param selectedLog the selected Logs object
     * @param logModule the VBox containing the log module
     */
    private void handleDeleteLog(int logID, Logs selectedLog, VBox logModule) {
        if (selectedLog != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Log");
            confirmationAlert.setHeaderText("Are you sure you want to delete this log?");
            confirmationAlert.setContentText("Log: " + selectedLog.getLogName());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteLogFromDatabase(logID);
                projectsGrid.getChildren().remove(logModule);
                rearrangeGrid();
            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No Log Selected");
            warningAlert.setContentText("Please select a log to delete.");
            warningAlert.showAndWait();
        }
    }

    /**
     * Deletes the specified log from the database.
     *
     * @param logId the log ID
     */
    private void deleteLogFromDatabase(int logId) {
        LogsDAO logsDAO = new LogsDAO();
        logsDAO.deleteLog(logId);
    }

    /**
     * Rearranges the GridPane after a log is deleted.
     */
    private void rearrangeGrid() {
        List<Node> remainingLogs = new ArrayList<>(projectsGrid.getChildren());
        projectsGrid.getChildren().clear();
        currentRow = 0;
        currentColumn = 0;

        for (Node logNode : remainingLogs) {
            projectsGrid.add(logNode, currentColumn, currentRow);
            currentColumn++;
            if (currentColumn >= LOGS_PER_ROW) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }

    /**
     * Creates a new log.
     * Opens a dialog to get the project name and inserts the new log into the database.
     */
    private void createNewLog() {
        TextInputDialog dialog = new TextInputDialog("New Project");
        dialog.setTitle("Create New Project");
        dialog.setHeaderText("Enter the name of your new project:");
        dialog.setContentText("Project Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(projectName -> {
            int userID = app.getLoggedInUserID();
            Logs newLog = new Logs(projectName, List.of(), List.of(), List.of());

            int logID;
            try {
                logID = logsDAO.insertLog(userID, newLog);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                LogEvent startEvent = new StartEvent(0, userID, logID, projectName, new ArrayList<>(), new ArrayList<>());
                addEventToProgressLog(startEvent);
                goToUpdateLogs(logID, newLog);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Adds an event to the progress log.
     *
     * @param event the LogEvent to add
     */
    public void addEventToProgressLog(LogEvent event) {
        progressLog.addEvent(event);
        try {
            logEventDAO.insertLogEvent(event);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String log = progressLog.getLog();
        System.out.println(log);
    }

    /**
     * Retrieves the progress log.
     *
     * @return the progress log as a String
     */
    public String getProgressLog() {
        return progressLog.getLog();
    }
}
