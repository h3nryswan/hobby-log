package com.example.demoplswork.model;

import java.sql.SQLException;
import java.util.List;
/**
 * The ILogsDAO interface defines the methods for performing CRUD operations on the Logs table in the database.
 * It includes methods to insert a new log, add to-do items, add images, add materials, retrieve logs for a user, update to-do item status, update log name, and delete a log.
 * Implementing classes should handle SQL exceptions and ensure the database connection is properly managed.
 */
public interface ILogsDAO {
    /**
     * Inserts a new log for the specified user with the given log details.
     * @param userId The ID of the user associated with the log
     * @param log The log object containing log name, to-do items, images, and materials
     * @return The ID of the newly inserted log
     * @throws SQLException If an SQL exception occurs while inserting the log
     */
    int insertLog(int userId, Logs log) throws SQLException;
    /**
     * Adds a to-do item to the specified log with the given details.
     * @param logId The ID of the log to add the to-do item to
     * @param toDoItem The description of the to-do item
     * @param isChecked The completion status of the to-do item
     * @throws SQLException If an SQL exception occurs while adding the to-do item
     */
    void addToDoItem(int logId, String toDoItem, boolean isChecked) throws SQLException;
    /**
     * Adds an image to the specified log with the given image path.
     * @param logId The ID of the log to add the image to
     * @param imagePath The path of the image to add
     * @throws SQLException If an SQL exception occurs while adding the image
     */
    void addImage(int logId, String imagePath) throws SQLException;
    /**
     * Adds a material to the specified log with the given material details.
     * @param logId The ID of the log to add the material to
     * @param material The material object containing material name, quantity, and unit
     */
    void addMaterial(int logId, Material material);
    /**
     * Retrieves a list of logs for the specified user.
     * @param userId The ID of the user to retrieve logs for
     * @return A list of log objects associated with the user
     */
    List<Object[]> getLogsForUser(int userId);
    /**
     * Updates the status of a to-do item in the specified log.
     * @param logId The ID of the log containing the to-do item
     * @param task The description of the to-do item to update
     * @param isChecked The new completion status of the to-do item
     * @return The number of rows affected by the update operation
     */
    double updateToDoItemStatus(int logId, String task, boolean isChecked);

    /**
     * Updates the name of the specified log.
     * @param logId The ID of the log to update
     * @param newLogName The new name for the log
     * @throws SQLException If an SQL exception occurs while updating the log name
     */
    void updateLogName(int logId, String newLogName) throws SQLException;
       /**
     * Deletes the specified log.
     * @param logId The ID of the log to delete
     */
    void deleteLog(int logId);
}

