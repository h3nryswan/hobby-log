package com.example.demoplswork.model;

import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * The ILogsDAO interface defines the methods for performing CRUD operations on the Logs table in the database.
 * It includes methods to insert a new log, add to-do items, add images, add materials, retrieve logs for a user, update to-do item status, update log name, and delete a log.
 * Implementing classes should handle SQL exceptions and ensure the database connection is properly managed.
 */
public class LogsDAO extends BaseDAO implements ILogsDAO{

    /**
    * Insert a new log into the database
    * @param userId the ID of the user who owns the log
    * @param log the Logs object to insert
    * @return the ID of the newly inserted log
    * */
    // Insert a new log into the database
    @Override
    public int insertLog(int userId, Logs log) throws SQLException {
        if (log == null) {
            throw new SQLException("Log cannot be null.");
        }
        if (userId <= 0) {
            throw new SQLException("Invalid user ID.");
        }

        String query = "INSERT INTO logs(user_id, log_name, to_do_items, images, progress, materials) VALUES(?, ?, ?, ?, ?, ?)";
        ResultSet rs = null;
        int logId = -1;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setString(2, log.getLogName());

            // Serialize the to-do items (task:checked format)
            String serializedToDoItems = serializeToDoItems(log.getToDoItems());
            pstmt.setString(3, serializedToDoItems);

            // Serialize images into a comma-separated string
            pstmt.setString(4, String.join(",", log.getImages()));

            // Set progress value
            pstmt.setDouble(5, log.getProgress());

            // Serialize materials
            pstmt.setString(6, serializeMaterials(log.getMaterials()));

            // Execute the update
            pstmt.executeUpdate();

            // Retrieve the generated log ID
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                logId = rs.getInt(1);  // This will be the generated ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return logId;
    }

    /**
    * Serialize to-do items as task:isChecked format
    * */
    // Serialize to-do items as task:isChecked format
    private String serializeToDoItems(List<Pair<String, Boolean>> toDoItems) {
        StringBuilder sb = new StringBuilder();

        for (Pair<String, Boolean> toDoItem : toDoItems) {
            sb.append(toDoItem.getKey())  // Task description
                    .append(":")
                    .append(toDoItem.getValue())  // Checked state
                    .append(",");
        }

        // Remove the trailing comma if any
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
    * Method to check if a log exists in the database
    * */
    public boolean doesLogExist(int logId) {
        String query = "SELECT COUNT(1) FROM logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, logId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, the log exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Log does not exist or an error occurred
    }



    // Method to add a to-do item to a specific log
    /**
     * Add a to-do item to a specific log
     * @param logId the ID of the log to add the to-do item to
     * @param toDoItem the description of the to-do item
     * @param isChecked the status of the to-do item (checked or unchecked)
     * @throws SQLException if the log ID is invalid
     */
    @Override
    public void addToDoItem(int logId, String toDoItem, boolean isChecked) throws SQLException {
        if (logId <= 0) {
            throw new SQLException("Invalid log ID.");
        }
        String query = "UPDATE logs SET to_do_items = CASE " +
                "WHEN to_do_items IS NULL OR to_do_items = '' THEN ? " +
                "ELSE to_do_items || ',' || ? " +
                "END WHERE id = ?";

        // Prepare the to-do item with its state
        String toDoWithState = toDoItem + ":" + (isChecked ? "true" : "false");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, toDoWithState);  // If empty, just add the to-do item
            pstmt.setString(2, toDoWithState);  // Append the new to-do item with its state
            pstmt.setInt(3, logId);  // Specify which log to update
            pstmt.executeUpdate();
            System.out.println("To-do item added to log " + logId + " with state: " + isChecked);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add an image path to a specific log
    /**
     * Add an image path to a specific log
     * @param logId the ID of the log to add the image to
     * @param imagePath the path of the image to add
     * @throws SQLException if the log ID is invalid
     */
    @Override
    public void addImage(int logId, String imagePath) throws SQLException {
        if (logId <= 0) {
            throw new SQLException("Invalid log ID.");
        }
        String query = "UPDATE logs SET images = CASE " +
                "WHEN images IS NULL OR images = '' THEN ? " +
                "ELSE images || ',' || ? " +
                "END WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, imagePath);  // If empty, just add the image path
            pstmt.setString(2, imagePath);  // Append the new image path
            pstmt.setInt(3, logId);  // Specify which log to update
            pstmt.executeUpdate();
            System.out.println("Image added to log " + logId + ": " + imagePath);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a material to a specific log
    /**
     * Add a material to a specific log
     * @param logId the ID of the log to add the material to
     * @param material the Material object to add
     */
    @Override
    public void addMaterial(int logId, Material material) {
        String query = "UPDATE logs SET materials = CASE " +
                "WHEN materials IS NULL OR materials = '' THEN ? " +
                "ELSE materials || ',' || ? " +
                "END WHERE id = ?";

        String serializedMaterial = material.getName() + ":" + material.getQuantity() + ":" + material.getPrice();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, serializedMaterial);  // If empty, just add the material
            pstmt.setString(2, serializedMaterial);  // Append the new material
            pstmt.setInt(3, logId);  // Specify which log to update
            pstmt.executeUpdate();
            System.out.println("Material added to log " + logId + ": " + serializedMaterial);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve logs for a specific user
     * @param userId the ID of the user to retrieve logs for
     * @return a list of Object arrays containing the log ID and Logs object
     */
    @Override
    public List<Object[]> getLogsForUser(int userId) {
        String query = "SELECT id, log_name, to_do_items, images, materials, progress FROM logs WHERE user_id = ?";
        List<Object[]> logsList = new ArrayList<>();

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId);
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                // Extract the log details, including the log ID
                int logID = rs.getInt("id");
                String logName = rs.getString("log_name");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(rs.getString("to_do_items"));
                List<String> images = parseImages(rs.getString("images")); // Deserialize images
                List<Material> materials = parseMaterials(rs.getString("materials")); // Deserialize materials
                double progress = rs.getDouble("progress");

                // Create a Logs object without the ID
                Logs log = new Logs(logName, toDoItems, images, materials);
                log.setProgress(progress);

                System.out.println("Log retrieved: " + logName + ". Progress: " + progress);

                // Add the log and its ID to the list
                logsList.add(new Object[] { logID, log });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logsList;
    }

    /**
     * Retrieve a specific log by its ID
     * @param logID the ID of the log to retrieve
     * @return the Logs object corresponding to the ID
     */
    public Logs getLogById(int logID) {
        String query = "SELECT log_name, to_do_items, images, materials, progress FROM logs WHERE id = ?";
        Logs log = null;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, logID);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                // Extract log details from the result set
                String logName = rs.getString("log_name");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(rs.getString("to_do_items")); // Deserialize to-do items
                List<String> images = parseImages(rs.getString("images")); // Deserialize images
                List<Material> materials = parseMaterials(rs.getString("materials")); // Deserialize materials
                double progress = rs.getDouble("progress");

                // Create the Logs object
                log = new Logs(logName, toDoItems, images, materials);
                log.setProgress(progress);

                System.out.println("Log retrieved by ID: " + logName + ". Progress: " + progress);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return log;
    }

    /**
     * Retrieve the user ID associated with a specific log
     * @param logID the ID of the log to retrieve the user ID for
     * @return the user ID associated with the log
     */
    public int getUserIdForLog(int logID) {
        String query = "SELECT user_id FROM logs WHERE id = ?";
        int userID = -1; // Default value if no user is found

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, logID);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                userID = rs.getInt("user_id"); // Get the user ID from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userID;
    }


    /**
     * Retrieve the log name associated with a specific log ID
     * @param logId the ID of the log to retrieve the name for
     * @return the log name associated with the log ID
     */
    public String getLogNameById(int logId) {
        String query = "SELECT log_name FROM logs WHERE id = ?";
        String logName = null;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, logId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                logName = rs.getString("log_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logName;
    }


    /**
     * Retrieve the to-do items associated with a specific log ID
     * @param logId the ID of the log to retrieve the to-do items for
     * @return a list of Pair objects containing the task description and checked state
     */
    // Helper method to serialize materials into a string format (e.g., name:quantity:cost)
    private String serializeMaterials(List<Material> materials) {
        StringBuilder sb = new StringBuilder();
        for (Material material : materials) {
            sb.append(material.getName()).append(":")
                    .append(material.getQuantity()).append(":")
                    .append(material.getPrice()).append(",");
        }
        return sb.toString();
    }

    // Helper method to deserialize materials from a string format
    /**
     * Deserialize materials from a string format
     * @param materialsStr the string containing the serialized materials
     * @return a list of Material objects
     */
    private List<Material> parseMaterials(String materialsStr) {
        List<Material> materials = new ArrayList<>();

        // Check if materialsStr is null or empty
        if (materialsStr == null || materialsStr.isEmpty()) {
            return materials;  // Return an empty list if there's no material data
        }

        // Split the string by commas to get each material
        String[] materialArr = materialsStr.split(",");

        for (String mat : materialArr) {
            // Split each material string by colon to get details (name, quantity, price)
            String[] details = mat.split(":");

            // Check if the details array has the correct number of parts (3 parts: name, quantity, price)
            if (details.length == 3) {
                try {
                    String name = details[0];
                    int quantity = Integer.parseInt(details[1]);  // Parse the quantity as an integer
                    double price = Double.parseDouble(details[2]);  // Parse the price as a double

                    // Add the material to the list
                    materials.add(new Material(name, quantity, price));
                } catch (NumberFormatException e) {
                    // Handle any parsing errors gracefully (e.g., log the error)
                    System.err.println("Error parsing material: " + mat);
                }
            } else {
                // Handle the case where details do not have exactly 3 parts
                System.err.println("Invalid material format: " + mat);
            }
        }

        return materials;
    }

    // Update a specific to-do item's status in the database and recalculate progress
    /**
     * Update a specific to-do item's status in the database and recalculate progress
     * @param logId the ID of the log containing the to-do item
     * @param task the description of the to-do item to update
     * @param isChecked the new status of the to-do item
     * @return the updated progress value
     */
    @Override
    public double updateToDoItemStatus(int logId, String task, boolean isChecked) {
        String querySelect = "SELECT to_do_items, progress FROM logs WHERE id = ?";
        String queryUpdate = "UPDATE logs SET to_do_items = ?, progress = ? WHERE id = ?";

        try {
            // Step 1: Retrieve the current to-do items and progress
            PreparedStatement pstmtSelect = connection.prepareStatement(querySelect);
            pstmtSelect.setInt(1, logId);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                String toDoItemsStr = rs.getString("to_do_items");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(toDoItemsStr);

                // Step 2: Update the status of the specific task
                for (int i = 0; i < toDoItems.size(); i++) {
                    if (toDoItems.get(i).getKey().equals(task)) {
                        toDoItems.set(i, new Pair<>(task, isChecked));
                        break;
                    }
                }

                // Step 3: Calculate the new progress
                double progress = calculateProgress(toDoItems);

                // Step 4: Serialize the updated to-do items back into the string format
                String updatedToDoItemsStr = serializeToDoItems(toDoItems);

                // Step 5: Update the database with the new to-do list and progress
                PreparedStatement pstmtUpdate = connection.prepareStatement(queryUpdate);
                pstmtUpdate.setString(1, updatedToDoItemsStr);
                pstmtUpdate.setDouble(2, progress);  // Update progress
                pstmtUpdate.setInt(3, logId);
                pstmtUpdate.executeUpdate();

                System.out.println("Updated to-do item: " + task + " to " + isChecked + ". Progress: " + progress);
                return progress;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Update the name of a specific log
     * @param logId the ID of the log to update
     * @param newLogName the new name for the log
     * @throws SQLException if the log ID is invalid
     */
    @Override
    public void updateLogName(int logId, String newLogName) throws SQLException {
        String query = "UPDATE logs SET log_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newLogName);
            pstmt.setInt(2, logId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Delete a specific log from the database
     * @param logId the ID of the log to delete
     */
    @Override
    public void deleteLog(int logId) {
        String sql = "DELETE FROM logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, logId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to calculate progress as a percentage of completed tasks
    /**
     * Calculate the progress as a percentage of completed tasks
     * @param toDoItems the list of to-do items to calculate progress for
     * @return the progress percentage
     */
    private double calculateProgress(List<Pair<String, Boolean>> toDoItems) {
        int completedCount = 0;
        for (Pair<String, Boolean> item : toDoItems) {
            if (item.getValue()) {
                completedCount++;
            }
        }
        if (toDoItems.isEmpty()) {
            return 0.0;
        }
        return (double) completedCount / toDoItems.size() * 100;
    }

    // Helper method to deserialize to-do items from a comma-separated string
    /**
     * Deserialize to-do items from a comma-separated string
     * @param toDoItemsStr the string containing the serialized to-do items
     * @return a list of Pair objects containing the task description and checked state
     */
    private List<Pair<String, Boolean>> parseToDoItems(String toDoItemsStr) {
        List<Pair<String, Boolean>> toDoItems = new ArrayList<>();

        if (toDoItemsStr == null || toDoItemsStr.isEmpty()) {
            return toDoItems;  // Return an empty list if there's no data
        }

        // Split the string into individual tasks
        String[] itemsArray = toDoItemsStr.split(",");

        for (String item : itemsArray) {
            String[] taskDetails = item.split(":");
            if (taskDetails.length == 2) {
                String taskDescription = taskDetails[0].trim();
                boolean isChecked = Boolean.parseBoolean(taskDetails[1].trim());
                toDoItems.add(new Pair<>(taskDescription, isChecked));  // Store task and its checked state
            }
        }

        return toDoItems;
    }


    // Helper method to deserialize images from a comma-separated string
    /**
     * Deserialize images from a comma-separated string
     * @param imagesStr the string containing the serialized image paths
     * @return a list of image paths
     */
    private List<String> parseImages(String imagesStr) {
        if (imagesStr == null || imagesStr.trim().isEmpty()) {
            return Collections.emptyList();  // Returns an immutable empty list
        }
        return List.of(imagesStr.split(","));
    }



}


