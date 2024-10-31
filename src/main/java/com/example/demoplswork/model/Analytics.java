package com.example.demoplswork.model;


import com.example.demoplswork.events.LogEvent;
import com.example.demoplswork.events.LogEventDAO;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;

/**
 * Analytics class to calculate various user statistics for the MyAnalytics section.
 * It has methods to calculate the total amount of money spent, the number of tasks completed,
 * the number of projects completed, the total number of materials used, the total number of likes,
 * and the total number of comments for a user.
 * It also has methods to calculate the total number of likes and comments for a specific log.
 */
public class Analytics {

    private LogsDAO logsDAO;
    private LogEventDAO logEventDAO;

    /**
     * Constructor to initialize the LogsDAO and LogEventDAO objects.
     *
     * @throws SQLException If there is an error connecting to the database.
     */
    public Analytics() throws SQLException {
        logsDAO = new LogsDAO();
        logEventDAO = new LogEventDAO();
    }


    /**
     * Calculates the total amount of money spent by the user across all logs.
     *
     * @param userId The ID of the user.
     * @return The total amount spent.
     */
    public double calculateTotalSpend(int userId) {
        double totalSpend = 0.0;

        // Get the logs for the user
        List<Object[]> userLogs = logsDAO.getLogsForUser(userId);

        // Iterate over each log
        for (Object[] logData : userLogs) {
            Logs log = (Logs) logData[1];  // The log object is the second element in the array

            // Get the materials for each log
            List<Material> materials = log.getMaterials();

            // Sum up the cost for each material
            for (Material material : materials) {
                totalSpend += material.getPrice() * material.getQuantity(); // Assuming there's a getQuantity() method as well
            }
        }

        return totalSpend;
    }

    /**
     * Calculates the number of tasks completed by the user.
     *
     * @param userId The ID of the user.
     * @return The number of completed tasks.
     */
    public int calculateTasksCompleted(int userId) {
        int tasksCompleted = 0;

        // Get the logs for the user
        List<Object[]> userLogs = logsDAO.getLogsForUser(userId);

        // Iterate over each log
        for (Object[] logData : userLogs) {
            Logs log = (Logs) logData[1];  // The log object is the second element in the array

            // Get the to-do items for each log
            List<Pair<String, Boolean>> toDoItems = log.getToDoItems();

            // Count tasks that are completed (Boolean is true)
            for (Pair<String, Boolean> toDoItem : toDoItems) {
                if (toDoItem.getValue()) {
                    tasksCompleted++;
                }
            }
        }

        return tasksCompleted;
    }

    /**
     * Calculate the total number of projects completed for a user.
     * A project is considered complete if all to-do items are completed.
     *
     * @param userId the ID of the user
     * @return the number of completed projects (logs)
     */
    public int calculateProjectsCompleted(int userId) {
        int projectsCompleted = 0;

        // Get the logs for the user
        List<Object[]> userLogs = logsDAO.getLogsForUser(userId);

        // Iterate over each log
        for (Object[] logData : userLogs) {
            Logs log = (Logs) logData[1];

            // Get the to-do items for the log
            List<Pair<String, Boolean>> toDoItems = log.getToDoItems();

            // Check if all to-do items are completed
            boolean allTasksCompleted = true;
            for (Pair<String, Boolean> toDoItem : toDoItems) {
                if (!toDoItem.getValue()) {
                    allTasksCompleted = false;
                    break;  // No need to check further if a task is not completed
                }
            }

            // If all tasks are completed, count this log as a completed project
            if (allTasksCompleted && !toDoItems.isEmpty()) {
                projectsCompleted++;
            }
        }

        return projectsCompleted;
    }

    /**
     * Calculates the total number of materials used by the user across all projects.
     *
     * @param userId The ID of the user.
     * @return The number of materials used.
     */
    public int calculateMaterialsUsed(int userId) {
        int totalMaterialsUsed = 0;

        // Get the logs for the user
        List<Object[]> userLogs = logsDAO.getLogsForUser(userId);

        // Iterate over each log
        for (Object[] logData : userLogs) {
            Logs log = (Logs) logData[1];

            // Get the list of materials for each log
            List<Material> materials = log.getMaterials();

            // Add the number of materials in this log to the total count
            totalMaterialsUsed += materials.size();
        }

        return totalMaterialsUsed;
    }

    /**
     * Calculates the total number of likes received by the user across all projects.
     *
     * @param userId The ID of the user.
     * @return The total number of likes.
     */
    public int calculateTotalLikes(int userId) {
        int totalLikes = 0;

        try {
            // Retrieve log events for the user
            List<LogEvent> userEvents = logEventDAO.getLogEventsForUser(userId);

            // Iterate through the events and sum up the likes
            for (LogEvent event : userEvents) {
                totalLikes += event.getLikes().size(); // Assumes getLikes() returns a list of likes
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalLikes;
    }

    /**
     * Calculates the total number of comments received by the user across all projects.
     *
     * @param userId The ID of the user.
     * @return The total number of comments.
     */
    public int calculateTotalComments(int userId) {
        int totalComments = 0;

        try {
            // Retrieve log events for the user
            List<LogEvent> userEvents = logEventDAO.getLogEventsForUser(userId);

            // Iterate through the events and sum up the comments
            for (LogEvent event : userEvents) {
                totalComments += event.getComments().size(); // Assumes getComments() returns a list of comments
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalComments;
    }

    /**
     * Calculates the total number of likes for a specific log.
     *
     * @param logId The ID of the log.
     * @return The total number of likes.
     */
    public int calculateTotalLikesForLog(int logId) {
        int totalLikes = 0;

        try {
            // Retrieve log events for the specific log
            List<LogEvent> logEvents = logEventDAO.getLogEventsForLog(logId);

            // Sum up the likes for each event in the log
            for (LogEvent event : logEvents) {
                totalLikes += event.getLikes().size(); // Assumes getLikes() returns a list of likes
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalLikes;
    }

    /**
     * Calculates the total number of comments for a specific log.
     *
     * @param logId The ID of the log.
     * @return The total number of comments.
     */
    public int calculateTotalCommentsForLog(int logId) {
        int totalComments = 0;

        try {
            // Retrieve log events for the specific log
            List<LogEvent> logEvents = logEventDAO.getLogEventsForLog(logId);

            // Sum up the comments for each event in the log
            for (LogEvent event : logEvents) {
                totalComments += event.getComments().size(); // Assumes getComments() returns a list of comments
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalComments;
    }
}

