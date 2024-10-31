package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

/**
 * ProfileView class is the controller for managing the Profile View.
 * It handles the display and interaction with the user's profile in the application.
 * It has methods to set the application instance, load user data, and initialize the view.
 * It has methods to handle user interactions such as changing the profile photo and editing the bio.
 * It has methods to load and display the user's logs, likes, and comments.
 * It has methods to navigate to different views such as Home, Explore, Logs, and Account.
 * It has a method to log out the user.
 */
public class ProfileView {
    private HelloApplication app;
    private ContextMenu accountMenu;
    private ContactDAO contactDAO;
    private LogsDAO logsDAO;
    private Analytics analytics;

    /**
     * Constructor for ProfileView.
     * Initializes the LogsDAO.
     *
     * @throws SQLException if a database access error occurs
     */
    public ProfileView() throws SQLException {
        logsDAO = new LogsDAO();
        analytics = new Analytics();
    }

    @FXML
    private Button accountButton;

    @FXML
    private Button photoButton;

    @FXML
    private Button bioButton;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox logsContainer;

    @FXML
    private Text bioText;

    @FXML
    private ImageView profileImageView;

    private String userName;
    private String userBio;

    @FXML
    private Label profileLikes;  // Label for Total Likes
    @FXML
    private Label profileComments;  // Label for Total Comments

    /**
     * Sets the application instance and loads the user data.
     *
     * @param app the HelloApplication instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;
        int userID = app.getLoggedInUserID();
        loadUser(userID);
        loadLikesComments(userID);
    }

    /**
     * Initializes the ProfileView.
     * Sets up the account menu with options to view profile and log out.
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
     * Loads the user data and updates the profile view.
     */
    public void loadUser(int userID) {

        contactDAO = new ContactDAO();

        Contact contact = contactDAO.getContactById(userID);

        ProfileDAO profileDAO = new ProfileDAO();
        try {
            profileDAO.insertProfile(userID, " ", " ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        profileDAO.getProfileByUserId(contact, userID);

        if(userID != app.getLoggedInUserID()){
            bioButton.setVisible(false);
            photoButton.setVisible(false);
        }
        else {
            bioButton.setVisible(true);
            photoButton.setVisible(true);
        }

        if (contact != null) {
            nameLabel.setText(contact.getFirstName() + " " + contact.getLastName());
            bioText.setText(contact.getBio());
            String imageUrl = contact.getPhoto();  // Assuming this is the S3 URL

            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    Path tempFile = Files.createTempFile("tempUserImage", ".jpg");
                    try (InputStream in = new URL(imageUrl).openStream()) {
                        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                    profileImageView.setImage(new Image(tempFile.toUri().toString()));
                    tempFile.toFile().deleteOnExit();
                } catch (IOException e) {
                    System.out.println("Failed to load user image from URL: " + e.getMessage());
                    profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/account_circle.png")));  // Fallback
                }
            } else {
                profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/account_circle.png")));  // Fallback
            }
        }

        loadUserLogs(userID);

    }

    /**
     * Loads and displays the user's logs.
     */
    public void loadUserLogs(int userID) {
        logsContainer.getChildren().clear();
        List<Object[]> logsList = logsDAO.getLogsForUser(userID);

        for (Object[] logData : logsList) {
            int logID = (int) logData[0];
            Logs log = (Logs) logData[1];
            HBox logEntry = createLogEntry(log, logID);
            logsContainer.getChildren().add(logEntry);
        }
    }

    public void loadLikesComments(int userID) {

        // Calculate and display Total Likes
        int totalLikes = analytics.calculateTotalLikes(userID);
        profileLikes.setText(STR."\{String.valueOf(totalLikes)} likes");

        // Calculate and display Total Comments
        int totalComments = analytics.calculateTotalComments(userID);
        profileComments.setText(STR."\{String.valueOf(totalComments)} comments");
    }

    /**
     * Creates a dynamic HBox for each log entry.
     *
     * @param log the Logs object
     * @param logID the log ID
     * @return the HBox containing the log entry
     */
    private HBox createLogEntry(Logs log, int logID) {
        HBox logEntry = new HBox();
        logEntry.setSpacing(30);
        logEntry.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");

        Label logNameLabel = new Label(log.getLogName());
        logNameLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Roboto';");
        logNameLabel.setPrefWidth(250);

        ProgressBar progressBar = new ProgressBar(log.getProgress() / 100.0);
        progressBar.setPrefWidth(400);

        HBox likesComments = new HBox();
        likesComments.setSpacing(10);

        // Fetch the real likes and comments for this log
        int totalLikes = analytics.calculateTotalLikesForLog(logID);  // Fetch total likes for this log
        int totalComments = analytics.calculateTotalCommentsForLog(logID);  // Fetch total comments for this log

        // Update likes and comments labels with actual values
        Label likesLabel = new Label(String.valueOf(totalLikes));
        InputStream imageStream = getClass().getResourceAsStream("/images/like-icon.png");
        ImageView likeIcon = new ImageView(new Image(imageStream));
        likeIcon.setFitHeight(20);
        likeIcon.setFitWidth(20);

        Label commentsLabel = new Label(String.valueOf(totalComments));
        InputStream imgStream = getClass().getResourceAsStream("/images/comment-icon.png");
        ImageView commentIcon = new ImageView(new Image(imgStream));
        commentIcon.setFitHeight(20);
        commentIcon.setFitWidth(20);

        likesComments.getChildren().addAll(likesLabel, likeIcon, commentsLabel, commentIcon);

        Button viewLogButton = new Button("View Log");
        viewLogButton.setPrefWidth(120);
        viewLogButton.setStyle("-fx-background-color: #d3d3d3;");
        viewLogButton.setOnAction(e -> {
            try {
                goToUpdateLogs(logID, log);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        logEntry.getChildren().addAll(logNameLabel, progressBar, likesComments, viewLogButton);

        return logEntry;
    }

    /**
     * Navigates to the log update view.
     *
     * @param id the log ID
     * @param log the Logs object
     * @throws IOException if an I/O error occurs
     */
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
     * Navigates to the account view.
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
     * Shows the edit profile dialog.
     */
    @FXML
    private void showEditProfileDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Bio");
        dialog.setHeaderText("Edit your profile information");

        VBox vbox = new VBox(10);
        vbox.setPrefSize(300, 200);

        TextArea bioArea = new TextArea(userBio);
        bioArea.setPrefHeight(100);
        bioArea.setWrapText(true);
        bioArea.setText(bioText.getText());

        vbox.getChildren().addAll(new Label("Bio:"), bioArea);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                userBio = bioArea.getText();
                bioText.setText(userBio);

                int userID = app.getLoggedInUserID();
                ProfileDAO profileDAO = new ProfileDAO();
                try {
                    profileDAO.updateProfile(userID, userBio, null);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Handles the change of profile photo.
     *
     * @param event the ActionEvent
     */
    @FXML
    private void handleChangeProfilePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                S3ImageUploader s3Uploader = new S3ImageUploader();
                String s3Url = s3Uploader.uploadImage(selectedFile.getAbsolutePath(), selectedFile.getName());

                // Set the profile image from S3 URL
                profileImageView.setImage(new Image(s3Url));

                int userID = app.getLoggedInUserID();
                ProfileDAO profileDAO = new ProfileDAO();
                profileDAO.updateProfile(userID, null, s3Url);  // Save S3 URL in the database
            } catch ( SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to upload and save profile image.");
            }
        }
    }


    /**
     * Navigates to the home view.
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
     * Navigates to the logs view.
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
     * Navigates to the explore view.
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
     * Logs out the user and navigates to the login view.
     */
    @FXML
    private void onLogout() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}