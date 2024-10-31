package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.*;
import com.example.demoplswork.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.Pair;


import java.io.File;
import java.io.IOException;
/**
 * LogsUpdateView class is the controller for viewing and updating logs.
 * It handles the UI for viewing and updating logs.
 * It has methods to set the application instance, set the log ID, set the log, and initialize the view.
 * It has methods to navigate to different views such as Home, Explore, Logs, and Account.
 * It has methods to handle user interactions such as adding to-do items, adding media, and adding materials.
 * It has methods to populate log details, load images from the log, and display log events.
 * It has methods to handle next and previous media navigation.
 * It has a showAlert method to display alerts with specified titles and messages.
 * It has a getTotalCost method to calculate the total cost of materials.
 * It has a getContactForUserId method to get the contact information for a user ID.
 */
public class LogsUpdateView {

    private HelloApplication app;
    private ContextMenu accountMenu;
    private int logId;  // The ID of the log being updated
    private LogsDAO logsDAO = new LogsDAO();
    private LogEventDAO logEventDAO;
    // VBox or ListView to display events
    @FXML
    private VBox logEventsBox;
    @FXML
    private ListView<LogEvent> logEventsListView;
    private ContactDAO contactDAO;

    @FXML
    private Button accountButton;

    @FXML
    private Button addMaterialButton;

    @FXML
    private Button addMediaButton;

    @FXML
    private Button addToDoButton;

    @FXML
    private VBox toDoListVBox;

    @FXML
    private ImageView mediaImageView;

    @FXML
    private Button nextButton;  // Button to go to the next image
    @FXML
    private Button backButton;  // Button to go to the previous image

    @FXML
    private Label logTitleLabel;

    @FXML
    private Label totalCostLabel; // Link to your Label

    @FXML
    private ProgressBar progressBar;

    private List<Image> images = new ArrayList<>();  // List to store the images
    private int currentIndex = -1;  // Track the current image index

    @FXML
    private TableView<Material> materialsTable;  // The TableView for materials

    @FXML
    private TableColumn<Material, String> materialNameCol;  // Material name column
    @FXML
    private TableColumn<Material, Integer> quantityCol;  // Quantity column
    @FXML
    private TableColumn<Material, Double> priceCol;  // Cost column
    private Logs log;

    /**
     * Constructor for the LogsUpdateView class.
     * @throws SQLException
     */
    public LogsUpdateView() throws SQLException {
        logEventDAO = new LogEventDAO();
        logEventsBox = new VBox();
        logEventsListView = new ListView<>();
    }

    /**
     * Method to set the application instance.
     * @param app The HelloApplication instance
     */
    @FXML
    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    /**
     * Method to set the log ID.
     * @param logId The ID of the log being updated
     */
    public void setLogId(int logId) {
        this.logId = logId;
    }

    /**
     * Method to set the log object.
     * @param log The log object
     */
    public void setLog(Logs log) {
        this.log = log;  // Set the log object

        // Populate the page with log details
        populateLogDetails();
    }

    /**
     * Initialize the LogsUpdateView controller.
     */
    @FXML
    public void initialize() {
        // init columns for materials table
        materialNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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
     * Method to navigate to the Home view.
     * @throws IOException
     */
    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();  // Navigate to Home view
        }
    }

    /**
     * Method to navigate to the Explore view.
     * @throws IOException
     */
    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
        }
    }

    /**
     * Method to navigate to the Logs view.
     * @throws IOException
     */
    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();  // Navigate to Explore view
        }
    }
    /*Method to show the account menu.
     *
     */
    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }

    /**
     * Method to navigate to the Account view.
     * @throws IOException
     */
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }
    /**
     * Method to log out of the account.
     *
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
     * Method to populate the log details.
     */
    // Populate the log details on the page
    private void populateLogDetails() {
        // Set the log title in the label
        logTitleLabel.setText(log.getLogName());
        progressBar.setProgress(log.getProgress() / 100);

        // Clear existing items in the VBox and Table to prevent duplication
        toDoListVBox.getChildren().clear();
        materialsTable.getItems().clear();

        if(logsDAO.getUserIdForLog(logId)!= app.getLoggedInUserID()){
            addMediaButton.setVisible(false);
            addMaterialButton.setVisible(false);
            addToDoButton.setVisible(false);
        }

        // Populate To-Do items in the VBox
        for (Pair<String, Boolean> toDoItem : log.getToDoItems()) {
            String task = toDoItem.getKey();      // The task description
            Boolean isChecked = toDoItem.getValue();  // The task checked state

            CheckBox checkBox = new CheckBox(task);
            checkBox.setSelected(isChecked);      // Set checkbox to the stored checked state
            toDoListVBox.getChildren().add(checkBox);

            if(logsDAO.getUserIdForLog(logId)!= app.getLoggedInUserID()){
                checkBox.setDisable(true);
            }

            // Event listener to update the log when a checkbox is checked/unchecked
            checkBox.setOnAction(event -> {
                boolean wasChecked = checkBox.isSelected();  // Capture the new state of the checkbox

                // Update the task's checked state in the log
                log.updateToDoItemStatus(task, checkBox.isSelected());  // Custom method to update the task's checked state
                double progress = logsDAO.updateToDoItemStatus(logId, task, checkBox.isSelected());

                LogsView logsView;
                try {
                    logsView = new LogsView();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Add a ToDoEvent only if the box goes from unchecked to checked
                if (wasChecked) {
                    LogEvent newEvent = new ToDoEvent(0,app.getLoggedInUserID(), logId, task, new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(newEvent);
                }

                // Update the progress bar
                progressBar.setProgress(progress / 100);

                // If the progress reaches 100%, log an EndEvent
                if (progress / 100 == 1) {
                    LogEvent endEvent = new EndEvent(0, app.getLoggedInUserID(), logId, log.getLogName(), new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(endEvent);
                }
            });

        }

        // Populate the materials in the table
        materialsTable.getItems().addAll(log.getMaterials());

        getTotalCost(log.getMaterials());

        displayLogEvents(logId);

        // Check if there are images before continuing
        List<String> images = log.getImages();
        System.out.println("Images list retrieved: " + images); // Check if it’s null or contains items
        if (images == null || images.isEmpty()) {
            System.out.println("No images associated with this log.");
            return;  // Exit method if no images
        }

        // Only reaches this point if there are images
        loadImagesFromLog(images);


    }

    /**
     * Method to calculate the total cost of materials and update the label.
     *
     * @param materials the list of materials
     */
    // Method to calculate total cost and update the label
    public void getTotalCost(List<Material> materials) {
        double totalCost = materials.stream()
                .mapToDouble(material -> material.getPrice() * material.getQuantity()) // Multiply price and quantity
                .sum(); // Calculate total cost

        // Update the label with the formatted total cost
        totalCostLabel.setText(String.format("Total Cost: $%.2f", totalCost));
    }



    /**
     * Handles the addition of a new to-do item.
     * This method creates a dialog to capture user input for a new task,
     * adds the task to the to-do list, and updates the database and progress bar.
     */
    public void handleAddToDo() {
        // Create a TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add To-Do Item");
        dialog.setHeaderText("Add a new task");
        dialog.setContentText("Enter your task:");

        // Show the dialog and capture the user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(task -> {
            // Add the new task to your to-do list (e.g., a VBox or ListView)
            CheckBox newTask = new CheckBox(task);

            // Store the checkbox state (unchecked by default) along with the task
            boolean isChecked = newTask.isSelected();  // This will be false by default

            // Add to VBox
            toDoListVBox.getChildren().add(newTask);

            // Store the to-do item and its state in the database for the specific log
            try {
                logsDAO.addToDoItem(logId, task, isChecked);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            double newProgress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());
            progressBar.setProgress(newProgress / 100);

            // Event listener to update the log when a checkbox is checked/unchecked
            newTask.setOnAction(event -> {
                boolean wasChecked = newTask.isSelected();  // Capture the new state of the checkbox

                // Update the task's checked state in the log
                log.updateToDoItemStatus(task, newTask.isSelected());  // Custom method to update the task's checked state
                double progress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());

                LogsView logsView;
                try {
                    logsView = new LogsView();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Add a ToDoEvent only if the box goes from unchecked to checked
                if (wasChecked) {
                    LogEvent newEvent = new ToDoEvent(0, app.getLoggedInUserID(), logId, task, new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(newEvent);
                }

                // Update the progress bar
                progressBar.setProgress(progress / 100);

                // If the progress reaches 100%, log an EndEvent
                if (progress / 100 == 1) {
                    LogEvent endEvent = new EndEvent(0, app.getLoggedInUserID(), logId, log.getLogName(), new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(endEvent);
                }
            });

        });
    }

    /**
     * Method to load images from the log.
     */
    // Method to load images and videos from the log
    private void loadImagesFromLog(List<String> imageUrls) {
        images.clear(); // Clear any previously loaded images

        for (String imageUrl : imageUrls) {
            try {
                // Load the image from the S3 URL
                Image image = new Image(imageUrl);
                images.add(image); // Add the image to the list for your slideshow or image view

            } catch (Exception e) {
                System.err.println("Failed to load image from S3 URL: " + imageUrl);
                e.printStackTrace();
            }
        }

        // Display the first image or handle the empty case
        currentIndex = images.isEmpty() ? -1 : 0;
        updateMediaView();
    }



    @FXML
    private VBox mediaContainer;  // Reference to the VBox containing the media

    /**
     * Method to update the media being displayed.
     */
    private void updateMediaView() {
        if (currentIndex >= 0 && currentIndex < images.size()) {  // Use images.size() for consistency
            String currentUrl = log.getImages().get(currentIndex);  // Get the S3 URL

            // Clear the mediaContainer to remove the current media (image/video)
            mediaContainer.getChildren().removeIf(node -> node instanceof ImageView || node instanceof MediaView);

            if (currentUrl.endsWith(".jpg") || currentUrl.endsWith(".png")) {
                System.out.println("Displaying image: " + currentUrl);

                ImageView newImageView = new ImageView();
                newImageView.setFitHeight(200);
                newImageView.setFitWidth(300);
                newImageView.setPreserveRatio(true);

                try {
                    // Download the image to a temporary file
                    Path tempFile = Files.createTempFile("tempImage", ".jpg");
                    try (InputStream in = new URL(currentUrl).openStream()) {
                        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    }

                    newImageView.setImage(new Image(tempFile.toUri().toString()));
                    mediaContainer.getChildren().add(1, newImageView);

                    tempFile.toFile().deleteOnExit();
                } catch (IOException e) {
                    System.out.println("Failed to load image from S3 URL: " + e.getMessage());
                }

            } else if (currentUrl.endsWith(".mp4")) {
                System.out.println("Displaying video: " + currentUrl);

                Media media = new Media(currentUrl);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitHeight(200);
                mediaView.setFitWidth(300);
                mediaView.setPreserveRatio(true);

                mediaView.setOnMouseClicked(e -> {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.play();
                    }
                });

                mediaContainer.getChildren().add(1, mediaView);
                mediaPlayer.play();
            } else {
                System.out.println("Unknown file type: " + currentUrl);
            }

            updateButtonState();  // Update button state after media change
        }
    }

    /**
     * Handles the addition of images to the log.
     */
    @FXML
    public void handleAddImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.png", "*.jpg", "*.mp4")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                S3ImageUploader s3Uploader = new S3ImageUploader();
                String s3Url = s3Uploader.uploadImage(selectedFile.getAbsolutePath(), selectedFile.getName());

                logsDAO.addImage(logId, s3Url);
                List<String> imageList = new ArrayList<>(log.getImages());
                imageList.add(s3Url);
                log.setImages(imageList);

                Image image = new Image(s3Url);
                images.add(image);  // Add to cached images list

                currentIndex = log.getImages().size() - 1;
                updateMediaView();  // Update the view with the newly added media
                updateButtonState();  // Update button states after adding

                LogsView logsView = new LogsView();
                LogEvent event = new ImageEvent(0, app.getLoggedInUserID(), logId, selectedFile.getName(), new ArrayList<>(), new ArrayList<>());
                logsView.addEventToProgressLog(event);

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to add media");
            }
        }
    }

    /**
     * Method to handle the navigation to the next image.
     */
    @FXML
    public void handleNext() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            updateMediaView();
            updateButtonState();
        }
    }

    /**
     * Method to handle the navigation to the previous image.
     */
    @FXML
    public void handleBack() {
        if (currentIndex > 0) {
            currentIndex--;
            updateMediaView();
            updateButtonState();
        }
    }

    /**
     * Method to update the button state based on the current index.
     */
    private void updateButtonState() {
        backButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == images.size() - 1);
    }



    /**
     * Method to handle the addition of a new material.
     */
    // Method to handle adding a new material
    @FXML
    public void handleAddMaterial() {
        // Create a custom dialog for adding materials
        Dialog<Pair<String, Pair<Integer, Double>>> dialog = new Dialog<>();
        dialog.setTitle("Add Material");
        dialog.setHeaderText("Enter material details");

        // Set the button types for OK and Cancel
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create input fields for Material Name, Quantity, and Cost
        TextField materialNameField = new TextField();
        materialNameField.setPromptText("Material Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField costField = new TextField();
        costField.setPromptText("Price (Each)");

        // Create a layout and add the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Material Name:"), 0, 0);
        grid.add(materialNameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Price (Each):"), 0, 2);
        grid.add(costField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Enable/Disable the Add button depending on user input
        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Add listeners to ensure all fields are filled in before enabling Add button
        materialNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || quantityField.getText().trim().isEmpty() || costField.getText().trim().isEmpty());
        });
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || materialNameField.getText().trim().isEmpty() || costField.getText().trim().isEmpty());
        });
        costField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || materialNameField.getText().trim().isEmpty() || quantityField.getText().trim().isEmpty());
        });

        // Convert the result when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String materialName = materialNameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    return new Pair<>(materialName, new Pair<>(quantity, cost));
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numeric values for Quantity and Price.");
                    return null;
                }
            }
            return null;
        });

        // Display the dialog and wait for the user to input values
        Optional<Pair<String, Pair<Integer, Double>>> result = dialog.showAndWait();

        // If the user entered valid data, add it to the table and database
        result.ifPresent(materialData -> {
            String materialName = materialData.getKey();
            int quantity = materialData.getValue().getKey();
            double cost = materialData.getValue().getValue();

            // Add the material to the table
            Material material = new Material(materialName, quantity, cost);
            materialsTable.getItems().add(material);
            log.addMaterial(material);

            // Add the material to the database (replace logId with the actual log ID you're working with)
            logsDAO.addMaterial(logId, material);

            getTotalCost(log.getMaterials());

            LogsView logsView;
            try {
                logsView = new LogsView();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            LogEvent event = new MaterialEvent(0, app.getLoggedInUserID(), logId, materialName, new ArrayList<>(), new ArrayList<>());
            logsView.addEventToProgressLog(event);
        });
    }

    /**
     * Method to show alert dialogs.
     */
    // Helper method to show alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Method to display log events.
     */
    public void displayLogEvents(int logId) {
        try {
            // Fetch log events from the database
            List<LogEvent> logEvents = logEventDAO.getLogEventsForLog(logId);
            // Create an observable list to populate the ListView
            ObservableList<LogEvent> eventItems = FXCollections.observableArrayList(logEvents);
            // Set the items in the ListView
            logEventsListView.setItems(eventItems);
            // Set a custom cell factory for the ListView to display LogEventCell
            logEventsListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(LogEvent event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setGraphic(null);
                    } else {
                        // Fetch user details
                        Contact contact = getContactForUserId(event.getUserId());
                        // Create a custom LogEventCell
                        LogEventCell eventCell = new LogEventCell(event, contact.getFirstName(), contact.getPhoto(), event.getLikes().size(), event.getComments().size());
                        setGraphic(eventCell);  // Set the custom cell graphic
                    }
                }
            });
            // **Clear existing content before adding new content**
            logEventsBox.getChildren().clear();
            // Add the ListView to the VBox or the container where you want to display it
            logEventsBox.getChildren().add(logEventsListView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
* Method to get the contact for the user
* */
    private Contact getContactForUserId(int userId) {
        contactDAO = new ContactDAO();

        // Fetch the contact information for the logged-in user
        Contact contact = contactDAO.getContactById(userId);

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
}





