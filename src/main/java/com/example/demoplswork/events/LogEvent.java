package com.example.demoplswork.events;

import com.example.demoplswork.model.LogsDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * LogEvent class is an abstract class representing a log event.
 * It has fields for the timestamp, user ID, log ID, comments, likes, and description.
 * It has a constructor to initialize these fields.
 * It has getter and setter methods for the timestamp, user ID, log ID, comments, likes, and ID.
 * It has an abstract method getDescription to be implemented by subclasses.
 * It has a toString method to return a string representation of the log event.
 * It has a getLogName method to get the log name by log ID.
 * It has a getCommentsFromDatabase method to retrieve comments for the event from the database.
 */
public abstract class LogEvent {
    protected String timestamp;
    protected int userId;
    protected int logId;
    private List<String> comments; // New field to store comments
    private List<Integer> likes; // New field to store user IDs who liked the event
    private LogsDAO logsDAO;
    private LogEventDAO logEventDAO;
    private int id;
    private String description;

    /**
     * Constructor for LogEvent class.
     * @param id The ID of the log event.
     * @param logId The ID of the log.
     * @param userId The ID of the user.
     * @param description The description of the event.
     * @param comments The comments for the event.
     * @param likes The likes for the event.
     */
    public LogEvent(int id, int logId, int userId, String description, List<String> comments, List<Integer> likes) {
        this.id = id;
        this.logId = logId;
        this.userId = userId;
        this.description = description;
        this.comments = comments != null ? comments : new ArrayList<>();
        this.likes = likes != null ? likes : new ArrayList<>();
        this.timestamp = java.time.LocalDate.now().toString();
        this.logsDAO = new LogsDAO();
        try {
            this.logEventDAO = new LogEventDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Getter method for the timestamp.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Setter method for the timestamp.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Getter method for the user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter method for the log ID.
     */
    public int getLogId() {
        return logId;
    }

    /*
     * Getter method for the description
     * */
    // Abstract method for event description, to be implemented by subclasses
    public abstract String getDescription();

    /**
     * toString method to return a string representation of the log event.
     */
    @Override
    public String toString() {
        return timestamp + " - User" + userId + " " + setDescription() + " (Log ID: " + logId + ")";
    }

    /**
     * set description method to set the description of the event
     */
    public abstract String setDescription();

    /**
     * getLogName method to get the log name by log ID.
     */
    public String getLogName(int logID) {
        return logsDAO.getLogNameById(logID);
    }

    /**
     * Getter method for the comments.
     */
    public List<String> getComments() {
        return comments;
    }

    /**
     * Setter method for the comments.
     */
    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    /**
     * Getter method for the likes.
     */
    public List<Integer> getLikes() {
        return likes;
    }

    /**
     * Setter method for the likes.
     */
    public void setLikes(List<Integer> likes) {
        this.likes = likes;
    }

    /**
     * Getter method for the ID.
     */
    public int getId (){
        return id;
    }

    /**
     * Setter method for the ID.
     */
    public void setId (int id){
        this.id = id;
    }

    /**
     * getCommentsFromDatabase method to retrieve comments for the event from the database.
     */
    // Method to fetch comments from the database
    public List<String> getCommentsFromDatabase() {
        return new ArrayList<>(logEventDAO.getCommentsForEvent(this.id));
    }


}
