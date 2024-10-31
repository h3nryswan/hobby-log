package com.example.demoplswork.events;

import java.util.List;
/**
 * EndEvent class is a subclass of LogEvent class. It is used to create an EndEvent object.
 * It has a constructor that takes in an id, userId, logId, description, comments, and likes.
 * It has a getDescription method that returns the description of the EndEvent object.
 * It has a setDescription method that returns the description of the EndEvent object.
 * It has a getComments method that returns the comments of the EndEvent object.
 */
public class EndEvent extends LogEvent {
    private String projectName;
    private List<String> comments;
    private List<Integer> likes;
    /**
     * Constructor for EndEvent class
     * @param id
     * @param userId
     * @param logId
     * @param description
     * @param comments
     * @param likes
     */
    public EndEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.projectName = description;
        this.comments = comments;
        this.likes = likes;
    }

    /**
     * Gets the comments of the EndEvent object
     * return List of comments
     */
    @Override
    public List<String> getComments(){
        return comments;
    }

    /**
     * Gets the description of the EndEvent object
     * return string description
     */
    @Override
    public String getDescription() {
        return projectName;
    }

    /**
     * Sets the description of the EndEvent object
     * @return String
     */
    @Override
    public String setDescription() {
        return "completed the log! \"" + projectName + "\"";
    }

}

