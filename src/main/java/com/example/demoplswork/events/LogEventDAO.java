package com.example.demoplswork.events;

import com.example.demoplswork.model.BaseDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * LogEventDAO class is responsible for interacting with the database to perform CRUD operations on log events.
 * It extends the BaseDAO class to inherit database connection handling.
 * It has a constructor to initialize the database connection.
 * It has a method to insert a LogEvent into the database.
 * It has a method to retrieve all LogEvents for a specific log ID.
 * It has a method to retrieve LogEvents for other users excluding the logged-in user.
 * It has a method to update likes for a specific log event.
 * It has a method to get comments for a specific event.
 * It has a method to save a comment for a specific log event.
 * It has a helper method to create a LogEvent object based on the event type.
 */
public class LogEventDAO extends BaseDAO implements ILogEventDAO {

    /**
     * Constructor to initialize the database connection.
     * @throws SQLException
     */
    // Constructor
    public LogEventDAO() throws SQLException {
        super(); // Call the BaseDAO constructor
    }

    /**
     * Method to insert a LogEvent into the database.
     * @param event The LogEvent object to be inserted.
     * @throws SQLException
     */
    // Method to insert a LogEvent into the database
    @Override
    public void insertLogEvent(LogEvent event) throws SQLException {
        String sql = "INSERT INTO log_events (timestamp, description, user_id, log_id, event_type) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getTimestamp());
            stmt.setString(2, event.setDescription());
            stmt.setInt(3, event.getUserId());
            stmt.setInt(4, event.getLogId());
            stmt.setString(5, event.getClass().getSimpleName()); // Store event type as a string
            stmt.executeUpdate();
        }
    }

    /**
     * Method to retrieve all LogEvents for a specific log ID.
     * @param logId The ID of the log for which to retrieve LogEvents.
     * @return A list of LogEvent objects.
     * @throws SQLException
     */
    // Method to retrieve all LogEvents for a specific logId
    @Override
    public List<LogEvent> getLogEventsForLog(int logId) throws SQLException {
        List<LogEvent> events = new ArrayList<>();
        String sql = "SELECT * FROM log_events WHERE log_id = ? ORDER BY timestamp ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, logId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                List<String> commentsList = new ArrayList<>();
                List<Integer> likesList = new ArrayList<>();
                String eventType = rs.getString("event_type");
                String timestamp = rs.getString("timestamp");
                String description = rs.getString("description");
                int userId = rs.getInt("user_id");
                String commentsData = rs.getString("comments");
                commentsList = LogEventUtils.deserializeComments(commentsData);
                String likesData = rs.getString("likes");
                likesList = LogEventUtils.deserializeLikes(likesData);
                int Id = rs.getInt("id");

                // Based on the event type, instantiate the correct subclass
                LogEvent event = createLogEventFromType(eventType, userId, logId, description, commentsList, likesList);
                event.setId(Id);
                event.setTimestamp(timestamp);
                System.out.println("LogEvent timestamp: " + timestamp);
                events.add(event);
            }
        }
        return events;
    }
    /**
     * Method to retrieve LogEvents for other users excluding the logged-in user.
     * @param loggedInUserId The ID of the logged-in user.
     * @return A list of LogEvent objects.
     * @throws SQLException
     */
    public List<LogEvent> getLogEventsForOtherUsers(int loggedInUserId) throws SQLException {
        List<LogEvent> events = new ArrayList<>();

        String sql = "SELECT * FROM log_events WHERE user_id != ? ORDER BY timestamp ASC"; ;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                List<String> commentsList;
                List<Integer> likesList;
                String eventType = rs.getString("event_type");
                String timestamp = rs.getString("timestamp");
                String description = rs.getString("description");
                int userId = rs.getInt("user_id");
                int logId = rs.getInt("log_id");
                String commentsData = rs.getString("comments");
                commentsList = LogEventUtils.deserializeComments(commentsData);
                String likesData = rs.getString("likes");
                likesList = LogEventUtils.deserializeLikes(likesData);
                int Id = rs.getInt("id");

                System.out.println(commentsList);

                // Instantiate the correct subclass based on the event type
                LogEvent event = createLogEventFromType(eventType, userId, logId, description, commentsList, likesList);
                event.setTimestamp(timestamp);
                System.out.println("LogEvent timestamp: " + timestamp);
                event.setId(Id);
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Method to retrieve LogEvents for a specific user.
     * @param userId The ID of the user for which to retrieve LogEvents.
     * @return A list of LogEvent objects.
     * @throws SQLException
     */
    public List<LogEvent> getLogEventsForUser(int userId) throws SQLException {
        List<LogEvent> events = new ArrayList<>();

        String sql = "SELECT * FROM log_events WHERE user_id = ? ORDER BY timestamp ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                List<String> commentsList;
                List<Integer> likesList;
                String eventType = rs.getString("event_type");
                String timestamp = rs.getString("timestamp");
                String description = rs.getString("description");
                int logId = rs.getInt("log_id");
                String commentsData = rs.getString("comments");
                commentsList = LogEventUtils.deserializeComments(commentsData);
                String likesData = rs.getString("likes");
                likesList = LogEventUtils.deserializeLikes(likesData);
                int eventId = rs.getInt("id");

                LogEvent event = createLogEventFromType(eventType, userId, logId, description, commentsList, likesList);
                event.setTimestamp(timestamp);
                event.setId(eventId);
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Method to update likes for a specific log event.
     * @param eventId The ID of the log event for which to update likes.
     * @param likes A list of user IDs who liked the event.
     */
    public void updateLikesForLogEvent(int eventId, List<Integer> likes) {
        String query = "UPDATE log_events SET likes = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String serializedLikes = LogEventUtils.serializeLikes(likes);
            pstmt.setString(1, serializedLikes);
            pstmt.setInt(2, eventId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get comments for a specific event.
     * @param eventId The ID of the event for which to retrieve comments.
     * @return A list of comments.
     */
    public List<String> getCommentsForEvent(int eventId) {
        List<String> commentsList = new ArrayList<>();
        String query = "SELECT comments FROM log_events WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String commentsData = rs.getString("comments");
                commentsList = new ArrayList<>(LogEventUtils.deserializeComments(commentsData));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commentsList;
    }


    /**
     * Method to save a comment for a specific log event.
     * @param eventId The ID of the event to save the comment for.
     * @param comment The comment to save.
     */
    public void saveCommentForLog(int eventId, String comment) {
        String query = "UPDATE log_events SET comments = CASE " +
                "WHEN comments IS NULL OR comments = '' THEN ? " +
                "ELSE comments || '||' || ? " + // Use "||" as delimiter to separate comments
                "END WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, comment);  // If no existing comments, set it to the new comment
            pstmt.setString(2, comment);  // Append the new comment to existing comments
            pstmt.setInt(3, eventId);     // Specify which event to update
            pstmt.executeUpdate();
            System.out.println("Comment added to event " + eventId + ": " + comment);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * Method to create a LogEvent object based on the event type.
     * @param eventId The ID of the log event to delete.
     */
    // Helper method to create a LogEvent based on the event type
    private LogEvent createLogEventFromType(String eventType, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        switch (eventType) {
            case "StartEvent":
                return new StartEvent(0, userId, logId, description, comments, likes);
            case "EndEvent":
                return new EndEvent(0, userId, logId, description, comments, likes);
            case "ToDoEvent":
                return new ToDoEvent(0, userId, logId, description, comments, likes);
            case "MaterialEvent":
                return new MaterialEvent(0, userId, logId, description, comments, likes);
            case "ImageEvent":
                return new ImageEvent(0, userId, logId, description, comments, likes);
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
}

