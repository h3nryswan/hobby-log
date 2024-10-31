package com.example.demoplswork.events;

import java.util.List;
/**
 * StartEvent class represents a log event where a new project is started.
 * It extends the LogEvent class to inherit common log event properties.
 * It has fields for the project name, comments, and likes.
 * It has a constructor to initialize these fields along with the inherited fields.
 * It overrides the getDescription method to return the project name.
 * It overrides the getComments method to return the list of comments.
 * It overrides the setDescription method to return a formatted description of the event.
 */
public class StartEvent extends LogEvent {
    private String projectName;
    private List<String> comments;
    private List<Integer> likes;
    /**
     * Constructor to initialize the fields of the StartEvent object.
     * @param id The unique identifier of the event.
     * @param userId The unique identifier of the user who created the event.
     * @param logId The unique identifier of the log that the event belongs to.
     * @param description The name of the project that is being started.
     * @param comments The list of comments on the event.
     * @param likes The list of user IDs who liked the event.
     */
    public StartEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.projectName = description;
        this.comments = comments;
        this.likes = likes;
    }
    /**
     * Returns the project name.
     * @return The project name.
     */
    @Override
    public List<String> getComments(){
        return comments;
    }
    /**
     * Returns the list of comments on the event.
     * @return The list of comments on the event.
     */
    @Override
    public String getDescription() {
        return projectName;
    }
    /**
     * Returns a formatted description of the event.
     * @return A formatted description of the event.
     */
    @Override
    public String setDescription() {
        return "started logging \"" + projectName + "\"";
    }

}
