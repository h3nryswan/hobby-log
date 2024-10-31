package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.EndEvent;
import com.example.demoplswork.events.ImageEvent;
import com.example.demoplswork.events.LogEvent;
import com.example.demoplswork.events.LogEventDAO;
import com.example.demoplswork.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ExploreView class is the controller for the Explore View.
 * It handles the logic for the Explore page of the application.
 * It has methods to initialize the view, load blogs from the database, and handle user interactions.
 * It has a setApplication method to set the application instance.
 * It has a goToHome method to navigate to the home page.
 * It has a goToLogs method to navigate to the logs page.
 * It has a showAccountMenu method to display the account menu.
 * It has a goToAccount method to navigate to the account page.
 * It has an onLogout method to log out the user.
 * It has a viewBlog method to display a blog's content.
 * It has a searchByHobby method to search for blogs by hobby.
 * It has a searchBlogs method to search for blogs based on user input.
 * It has a showCreateBlogDialog method to create a new blog.
 * It has a displayBlog method to display a blog in the UI.
 * It has a loadBlogsFromDatabase method to load blogs from the database.
 * It has a getCurrentUsername method to get the current user's username.
 * It has a saveBlogToDatabase method to save a blog to the database.
 * It has a viewBlogContent method to view the content of a blog.
 * It has a loadMyFeed method to load the user's feed.
 * It has an addEventToFeed method to add an event to the feed.
 * It has a getContactForUserId method to get the contact for a user ID.
 * It has a showCommentsPopup method to show a comments popup.
 * It has a saveCommentForLog method to save a comment for a log.
 * It has a toggleLike method to toggle a like on an event.
 */
public class ExploreView
{

    private HelloApplication app;
    private ContextMenu accountMenu;
    private ContactDAO contactDAO;
    private LogEventDAO logEventDAO;
    private static final String BUCKET_NAME = "hobby-log";
    private static final String REGION = "ap-southeast-2";

    @FXML
    private VBox post2;

    ArrayList<String> hobbys = new ArrayList<>(
            Arrays.asList("Woodworking", "PC Building", "Miniatures", "Music Production", "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art")
    );

    /**
     * Constructor for ExploreView.
     *
     * @throws SQLException if a database access error occurs
     */
    public ExploreView() throws SQLException {
        this.logEventDAO = new LogEventDAO();
    }

    @FXML
    private VBox commentsContainer1;

    @FXML
    private Button accountButton;

    /**
     * Sets the application instance.
     *
     * @param app the application instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;
        int loggedInUserId = app.getLoggedInUserID(); // Assuming you have a way to get this ID
        loadMyFeed(loggedInUserId);
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        accountMenu = new ContextMenu();
        // hobbyChoiceBox.getItems().addAll(hobbys);

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

        // Load existing blogs when the page is initialized
        loadExistingBlogs();
    }

    /**
     * Searches the list of hobbies.
     *
     * @param searchHobby   the hobby to search for
     * @param listOfStrings the list of strings to search in
     * @return the list of matching hobbies
     */
    private List<String> searchList(String searchHobby, List<String> listOfStrings) {
        List<String> searchHobbyArray = Arrays.asList(searchHobby.trim().split(" "));
        return listOfStrings.stream().filter(input -> {
            return searchHobbyArray.stream().allMatch(hobby ->
                    input.toLowerCase().contains(hobby.toLowerCase()));
        }).collect(Collectors.toList());
    }

    /**
     * Shows the home view.
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
     * Shows the logs view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }
    /**
     * Shows the account view.
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
     * Shows the explore view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void onLogout() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label introLine1;

    @FXML
    private Label introLine2;

    /**
     * Shows the blog view.
     *
     * @param title the title of the blog
     * @param description the blog content
     */
    @FXML
    public void viewBlog(String title, String description, String tag) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Blog Content");

        // Create a ScrollPane to handle large content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true); // Fit the content width to the ScrollPane

        // Create a VBox to hold the title and description
        VBox contentBox = new VBox(10); // 10 is the spacing between title and description
        contentBox.setPadding(new Insets(10)); // Add padding for some space around the content

        // Create a Label for the title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true); // Ensure the title wraps if too long

        Label tagLabel = new Label("Tag: " + tag);  // Add the tag below the title
        tagLabel.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-font-style: italic;");

        // Create a TextArea for the description (optional: TextArea allows for selectable and scrollable text)
        TextArea descriptionArea = new TextArea(description);
        descriptionArea.setWrapText(true); // Ensure text wraps
        descriptionArea.setEditable(false); // Make the TextArea read-only
        descriptionArea.setPrefRowCount(10); // Set preferred rows to limit initial height

        // Add the title and description to the VBox
        contentBox.getChildren().addAll(titleLabel, tagLabel, descriptionArea);

        // Set the VBox into the ScrollPane
        scrollPane.setContent(contentBox);

        // Add the ScrollPane to the dialog
        dialog.getDialogPane().setContent(scrollPane);

        // Add a close button
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


    @FXML
    private ComboBox<String> hobbyChoiceBox;

    @FXML
    void searchByHobby(ActionEvent event) {
        /*listView.getItems().clear();

        String selectedHobby = hobbyChoiceBox.getValue();
        if (selectedHobby != null && !selectedHobby.isEmpty()) {
            listView.getItems().addAll(searchList(selectedHobby, hobbys));
        }*/
    }

    /**
     * Shows the create blog dialog.
     */
    @FXML
    private void showCreateBlogDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Blog");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        TextField introField = new TextField();
        introField.setPromptText("Enter Blog Title");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter Blog Description");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Woodworking", "PC Building", "Miniatures", "Music Production", "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art");
        categoryComboBox.setPromptText("Select Category");

        TextField tagField = new TextField();  // New input for tag
        tagField.setPromptText("Enter Blog Tag");

        Button uploadImageButton = new Button("Upload Cover Image");
        Label imagePathLabel = new Label("No image selected");


        uploadImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());

            if (selectedFile != null) {
                // Instantiate S3ImageUploader and upload the image
                S3ImageUploader s3Uploader = new S3ImageUploader();
                String s3Url = s3Uploader.uploadImage(selectedFile.getAbsolutePath(), selectedFile.getName());

                imagePathLabel.setText(s3Url); // Set the image file name to store in the database
            }
        });

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                new Label("Blog Title:"), introField,
                new Label("Description:"), descriptionArea,
                new Label("Category:"), categoryComboBox,
                // new Label("Tag:"), tagField,  // Add the tag input
                uploadImageButton, imagePathLabel
        );
        dialog.getDialogPane().setContent(dialogContent);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                String intro = introField.getText();
                String description = descriptionArea.getText();
                String category = categoryComboBox.getValue();
                //String tag = tagField.getText();  // Capture the tag
                String imagePath = imagePathLabel.getText();

                if (intro.isEmpty() || description.isEmpty() || category == null || imagePath.equals("No image selected")) {
                    Alert errorAlert = new Alert(AlertType.ERROR, "Please fill all fields, select a category, and upload an image.");
                    errorAlert.showAndWait();
                } else {
                    // Create the new blog and add it to the database
                    Blog newBlog = new Blog(app.getLoggedInUserID(), intro, description, imagePath, category);
                    try {
                        BlogDAO blogDAO = new BlogDAO();
                        blogDAO.insertBlog(newBlog);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Create and add the new blog post dynamically in the UI
                    addNewBlogPost(intro, description, imagePath, category, app.getLoggedInUserID());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Displays a blog in the UI.
     *
     */
    private void loadExistingBlogs() {
        try {
            BlogDAO blogDAO = new BlogDAO();
            List<Blog> blogs = blogDAO.getAllBlogs();

            // Loop through the list of blogs and add them to the UI
            for (Blog blog : blogs) {
                addNewBlogPost(blog.getTitle(), blog.getDescription(), blog.getImagePath(), blog.getTag(), blog.getUserId()); // Pass userId
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a new blog post in the UI.
     *
     *
     */
    private void addNewBlogPost(String title, String description, String imagePath, String tag, int userId) {
        // Fetch user details
        Contact contact = getContactForUserId(userId); // Assuming you have this method available
        String username = contact != null ? contact.getFirstName() : "Unknown User";

        StackPane blogPost = new StackPane();
        blogPost.setPrefHeight(200.0);
        blogPost.setPrefWidth(500.0);
        blogPost.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        VBox postContent = new VBox();
        postContent.setPrefHeight(150.0);
        postContent.setPrefWidth(400.0);
        postContent.setSpacing(10);
        postContent.setStyle("-fx-padding: 30;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-family: 'Roboto'; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);
        titleLabel.setPrefWidth(450.0);

        HBox header = new HBox();
        header.setPrefHeight(23.0);
        header.setPrefWidth(373.0);

        String profilePhotoUrl = (contact != null) ? contact.getPhoto() : null;
        ImageView profileImage = new ImageView();
        profileImage.setFitHeight(50.0);
        profileImage.setFitWidth(50.0);

        if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) {
            try {
                // Download the image to a temporary file
                Path tempFile = Files.createTempFile("tempUserImage", ".jpg");
                try (InputStream in = new URL(profilePhotoUrl).openStream()) {
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }
                profileImage.setImage(new Image(tempFile.toUri().toString()));
                tempFile.toFile().deleteOnExit();
            } catch (IOException e) {
                System.out.println("Failed to load profile image from URL: " + e.getMessage());
                profileImage.setImage(new Image(getClass().getResourceAsStream("/images/account_circle.png")));  // Fallback
            }
        } else {
            profileImage.setImage(new Image(getClass().getResourceAsStream("/images/account_circle.png")));  // Fallback if URL is not set
        }

        Label usernameLabel = new Label(username); // Dynamic username
        usernameLabel.setPrefHeight(20.0);
        usernameLabel.setPrefWidth(122.0);
        usernameLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        usernameLabel.setTranslateY(15.0);




        header.getChildren().addAll(profileImage, usernameLabel);
        header.setSpacing(10);

        StackPane coverImageContainer = new StackPane();
        coverImageContainer.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView coverImageView = new ImageView();
        coverImageView.setFitHeight(300.0);
        coverImageView.setFitWidth(330.0);
        coverImageView.setPreserveRatio(true);

        if (imagePath != null && !imagePath.isEmpty()) {
            String imageUrl = imagePath;  // Assuming this is a complete S3 URL

            try {
                // Download the image to a temporary file
                Path tempFile = Files.createTempFile("tempImage", ".jpg");
                try (InputStream in = new URL(imageUrl).openStream()) {
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }

                // Load the image from the local temporary file
                Image image = new Image(tempFile.toUri().toString());
                coverImageView.setImage(image);

                // Delete the temp file on application exit
                tempFile.toFile().deleteOnExit();

            } catch (IOException e) {
                System.out.println("Failed to load image from URL: " + e.getMessage());
                coverImageView.setImage(new Image(getClass().getResourceAsStream("/images/post-ph.jpg"))); // Fallback
            }
        } else {
            coverImageView.setImage(new Image(getClass().getResourceAsStream("/images/post-ph.jpg"))); // Fallback if no image path
        }

        coverImageContainer.getChildren().add(coverImageView);



        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(javafx.geometry.Pos.CENTER);

        Button openArticleButton = new Button("Open article");
        openArticleButton.setPrefHeight(27.0);
        openArticleButton.setPrefWidth(90.0);
        openArticleButton.setStyle("-fx-background-color: #FFD643;");
        openArticleButton.setOnAction(e -> viewBlog(title, description, tag)); // Pass title and description to the viewBlog method

        buttonContainer.getChildren().add(openArticleButton);

        postContent.getChildren().addAll(titleLabel, header, coverImageContainer, buttonContainer);
        blogPost.getChildren().add(postContent);

        // Add the new blog post to the VBox (post2)
        post2.getChildren().addFirst(blogPost);
    }





    /**
     * Loads the feed for the logged-in user.
     *
     * @param loggedInUserId the ID of the logged-in user
     */
    public void loadMyFeed(int loggedInUserId) {
        try {
            LogEventDAO logEventDAO = new LogEventDAO();
            List<LogEvent> events = logEventDAO.getLogEventsForOtherUsers(loggedInUserId);

            // Group the events by date (yyyy-MM-dd format)
            Map<LocalDate, List<LogEvent>> eventsByDate = events.stream()
                    .collect(Collectors.groupingBy(event -> LocalDate.parse(event.getTimestamp().substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

            // Sort the dates in descending order
            List<LocalDate> sortedDates = eventsByDate.keySet().stream()
                    .sorted(Comparator.reverseOrder()) // Sort in descending order
                    .collect(Collectors.toList());

            // Iterate over each sorted date group and add them to the feed
            for (LocalDate date : sortedDates) {
                List<LogEvent> eventsForDate = eventsByDate.get(date);

                // Reverse the list to show the most recent events first
                Collections.reverse(eventsForDate);

                // Create a date header label
                Label dateHeader = new Label(date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
                dateHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 0 10 0;");

                // Add the date header to the feed
                commentsContainer1.getChildren().add(dateHeader);

                // Add all events for that date
                for (LogEvent event : eventsForDate) {
                    if (event instanceof EndEvent || event instanceof ImageEvent) {
                        addEventToFeed(event, true); // true indicates these can have images
                    } else {
                        addEventToFeed(event, false); // Other events without images
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an event to the feed.
     *
     * @param event    the event to add
     * @param hasImage whether the event has an image
     */
    private void addEventToFeed(LogEvent event, boolean hasImage) {
        LogsDAO logsDAO = new LogsDAO();

        // Check if the associated log exists
        if (!logsDAO.doesLogExist(event.getLogId())) {
            System.out.println("Log ID " + event.getLogId() + " does not exist. Skipping event.");
            return; // Skip this event if the log does not exist
        }

        StackPane postContainer = new StackPane();
        postContainer.setPrefHeight(200.0);
        postContainer.setPrefWidth(500.0);
        postContainer.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        VBox postContent = new VBox();
        postContent.setPrefHeight(150.0);
        postContent.setPrefWidth(300.0);
        postContent.setSpacing(10);
        postContent.setStyle("-fx-padding: 30;");

        // Fetch user details
        Contact contact = getContactForUserId(event.getUserId());
        String username = contact != null ? contact.getFirstName() : "Unknown User";
        String profilePhotoPath = contact != null ? contact.getPhoto() : "/images/account_circle.png";

        // Profile Image and Username
        HBox header = new HBox(15); // Added spacing
        ImageView profileImage = new ImageView();
        profileImage.setFitHeight(50.0);
        profileImage.setFitWidth(50.0);

        // Load profile image from S3 URL using temporary file
        try {
            if (profilePhotoPath != null && !profilePhotoPath.isEmpty()) {
                Path tempFile = Files.createTempFile("tempProfileImage", ".jpg");
                try (InputStream in = new URL(profilePhotoPath).openStream()) {
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }
                profileImage.setImage(new Image(tempFile.toUri().toString()));
                tempFile.toFile().deleteOnExit();  // Delete temp file on application exit
            } else {
                System.out.println("Profile image not found at S3 URL: " + profilePhotoPath);
                profileImage.setImage(new Image(getClass().getResourceAsStream("/images/account_circle.png")));  // Fallback image
            }
        } catch (IOException e) {
            System.out.println("Failed to load profile image from URL: " + e.getMessage());
            profileImage.setImage(new Image(getClass().getResourceAsStream("/images/account_circle.png")));
        }


        // Create bold label for the username
        Label boldUser = new Label(username + ": ");
        boldUser.setStyle("-fx-font-weight: bold;");

        // Create an HBox to join bold username and description
        HBox userDescription = new HBox(boldUser, new Label(event.getDescription()));
        userDescription.setSpacing(5);

        // Set onMouseEntered to underline the text on hover
        userDescription.setOnMouseEntered(e -> boldUser.setStyle("-fx-underline: true; -fx-font-weight: bold;"));

        // Set onMouseExited to remove the underline when the mouse leaves
        userDescription.setOnMouseExited(e -> boldUser.setStyle("-fx-underline: false; -fx-font-weight: bold;"));

        userDescription.setOnMouseClicked(event1 -> loadOtherUserProfile(event.getUserId()));

        Label logNameLabel = new Label("Project: " + event.getLogName(event.getLogId())); // Use method to get log name
        logNameLabel.setStyle("-fx-font-style: italic;");
        // Set onMouseEntered to underline the text on hover
        logNameLabel.setOnMouseEntered(e -> logNameLabel.setStyle("-fx-font-style: italic; -fx-underline: true;"));

        // Set onMouseExited to remove the underline when the mouse leaves
        logNameLabel.setOnMouseExited(e -> logNameLabel.setStyle("-fx-font-style: italic;"));
        logNameLabel.setOnMouseClicked(e -> {
            try {
                goToUpdateLogs(event.getLogId(), logsDAO.getLogById(event.getLogId()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        // Adjust Header Layout
        VBox headerText = new VBox(5); // Vertical arrangement for username + description and project name
        headerText.getChildren().addAll(userDescription, logNameLabel);

        // Add components to the header HBox
        header.getChildren().addAll(profileImage, headerText);

        // Optional Image for specific event types
        // Media container (image or video)
        StackPane mediaContainer = new StackPane();

        if (hasImage && event instanceof ImageEvent) {
            ImageEvent imageEvent = (ImageEvent) event;
            String fileName = imageEvent.getImagePath(event.getDescription());  // Get just the filename
            String mediaUrl = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + fileName;  // Rebuild full S3 URL

            if (mediaUrl.endsWith(".mp4")) {
                System.out.println("Loading video: " + mediaUrl);
                Media media = new Media(mediaUrl);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitHeight(150);
                mediaView.setFitWidth(200);
                mediaView.setPreserveRatio(true);

                mediaView.setOnMouseClicked(e -> {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.play();
                    }
                });

                mediaContainer.getChildren().add(mediaView);
            } else {
                System.out.println("Loading image: " + mediaUrl);
                ImageView eventImage = new ImageView();
                eventImage.setFitHeight(150);
                eventImage.setFitWidth(200);
                eventImage.setPreserveRatio(true);

                try {
                    Path tempFile = Files.createTempFile("tempEventImage", ".jpg");
                    try (InputStream in = new URL(mediaUrl).openStream()) {
                        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                    eventImage.setImage(new Image(tempFile.toUri().toString()));
                    tempFile.toFile().deleteOnExit();
                } catch (IOException e) {
                    System.out.println("Failed to load image from URL: " + e.getMessage());
                    eventImage.setImage(new Image(getClass().getResourceAsStream("/images/post-ph.jpg")));  // Fallback
                }

                mediaContainer.getChildren().add(eventImage);
            }
        }



        // Likes and Comments Controls
        Label likeCountLabel = new Label(event.getLikes().size() + " Likes");
        Label commentCountLabel = new Label(event.getComments().size() + " Comments");
        HBox likeCommentControls = new HBox();
        likeCommentControls.setSpacing(10);

        Button likeButton = new Button(event.getLikes().contains(app.getLoggedInUserID()) ? "Unlike" : "Like");
        likeButton.setOnAction(e -> {
            toggleLike(event, likeButton, likeCountLabel);
        });

        HBox countLabels = new HBox(likeCountLabel, commentCountLabel);
        countLabels.setSpacing(10);

        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> showCommentsPopup(event, commentCountLabel)); // Open comments popup

        likeCommentControls.getChildren().addAll(likeButton, commentButton);

        // Add elements to the postContent VBox
        postContent.getChildren().addAll(header, mediaContainer, countLabels, likeCommentControls);

        // Add the post content to the main post container
        postContainer.getChildren().add(postContent);

        // Add the post container to the main feed
        commentsContainer1.getChildren().add(postContainer);
    }

    /**
     * Loads the profile of another user.
     *
     * @param userId the ID of the user
     */
    public void loadOtherUserProfile(int userId) {
        try {
            // Load the FXML for ProfileView
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
            Parent profileView = loader.load();

            // Pass the userId to the ProfileView controller
            ProfileView controller = loader.getController();
            controller.setApplication(app);
            controller.loadUser(userId);
            controller.loadLikesComments(userId);


            // Switch to the ProfileView scene
            Stage stage = (Stage) commentsContainer1.getScene().getWindow();  // Replace 'someNodeInCurrentView' with a reference to a node in the current view
            Scene scene = new Scene(profileView);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the contact for a user ID.
     *
     * @param userId the user ID
     * @return the contact for the user ID
     */
    private Contact getContactForUserId(int userId) {
        contactDAO = new ContactDAO();

        // Fetch the contact information for the logged-in user
        Contact contact = contactDAO.getContactById(userId);

        if (contact == null) {
            System.out.println("No contact found for user ID: " + userId);
            return null; // Safely return if no contact found
        }

        // Now update the contact with profile details (bio, photo)
        ProfileDAO profileDAO = new ProfileDAO();
        try {
            profileDAO.insertProfile(userId, " ", " ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        profileDAO.getProfileByUserId(contact, userId);
        return contact;
    }

    /**
     * Shows the comments popup for an event.
     *
     * @param event             the event
     * @param commentCountLabel the label to update the comment count
     */
    private void showCommentsPopup(LogEvent event, Label commentCountLabel) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Comments");

        // Set dialog buttons
        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

        // Main content VBox
        VBox contentBox = new VBox(10);
        contentBox.setPrefWidth(400);
        contentBox.setStyle("-fx-padding: 20;");

        // Fetch and display existing comments
        List<String> comments = new ArrayList<>(event.getCommentsFromDatabase());
        System.out.println(comments);
        VBox commentsBox = new VBox(5);
        if (comments != null) {
            for (String comment : comments) {
                Label commentLabel = new Label(comment);
                commentsBox.getChildren().add(commentLabel);
            }
        }

        // Add commentsBox to a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(commentsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200); // Adjust height as needed

        // Text field for adding a new comment
        TextField newCommentField = new TextField();
        newCommentField.setPromptText("Write a comment...");

        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setOnAction(e -> {
            String newComment = newCommentField.getText().trim();
            if (!newComment.isEmpty()) {
                // Fetch the current user's username
                String currentUsername = getCurrentUsername(); // Implement this method to get the logged-in user's name

                // Format the new comment as "Username: Comment Text"
                String formattedComment = currentUsername + ": " + newComment;
                commentsBox.getChildren().add(new Label(formattedComment));
                newCommentField.clear();

                // Save the new comment to the database (implement save logic)
                saveCommentForLog(event.getId(), formattedComment);

                // Update the comment list and label
                comments.add(formattedComment);
                event.setComments(comments); // Ensure the event's comments list is updated
                commentCountLabel.setText(comments.size() + " Comments");
            }
        });

        // Layout: Add scrollPane and new comment input
        contentBox.getChildren().addAll(scrollPane, newCommentField, addCommentButton);
        dialog.getDialogPane().setContent(contentBox);

        // Show the dialog
        dialog.showAndWait();
    }

    /**
     * Gets the current username.
     *
     * @return the current username
     */
    private String getCurrentUsername() {
        Contact loggedIn = getContactForUserId(app.getLoggedInUserID());
        return loggedIn.getFirstName();
    }

    /**
     * Saves a comment for a specific log event.
     *
     * @param eventId the ID of the event
     * @param comment the comment to save
     */
    private void saveCommentForLog(int eventId, String comment) {
        logEventDAO.saveCommentForLog(eventId, comment);
    }

    /**
     * Toggles the like status for a log event.
     *
     * @param event          the log event
     * @param likeButton     the button to toggle like status
     * @param likeCountLabel the label to update like count
     */
    private void toggleLike(LogEvent event, Button likeButton, Label likeCountLabel) {
        int userId = app.getLoggedInUserID();
        List<Integer> likes = event.getLikes();

        if (likes.contains(userId)) {
            // User already liked the event, so unlike it
            likes.remove(Integer.valueOf(userId));
            likeButton.setText("Like");
        } else {
            // User has not liked the event, so like it
            likes.add(userId);
            likeButton.setText("Unlike");
        }

        // Update like count
        likeCountLabel.setText(likes.size() + " Likes");

        // Save updated likes to the database
        logEventDAO.updateLikesForLogEvent(event.getId(), likes);
    }

    /**
     * Navigates to the update logs view.
     *
     * @param id the ID of the log
     * @param log the log to update
     * @throws IOException if an I/O error occurs
     */
    public void goToUpdateLogs(int id, Logs log) throws IOException {
        if (app != null) {
            app.showLogsUpdateView(id, log);
        }
    }
}