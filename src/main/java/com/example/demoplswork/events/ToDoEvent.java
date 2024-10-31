package com.example.demoplswork.events;

import java.util.List;
/**
 * ToDoEvent class represents a log event where a to-do item is completed.
 * It extends the LogEvent class to inherit common log event properties.
 * It has fields for the to-do item, comments, and likes.
 * It has a constructor to initialize these fields along with the inherited fields.
 * It overrides the getDescription method to return the to-do item.
 * It overrides the getComments method to return the list of comments.
 * It overrides the setDescription method to return a formatted description of the event.
 */
public class ToDoEvent extends LogEvent {
    private String toDoItem;
    private List<String> comments;
    private List<Integer> likes;
    /**
     * Constructor to initialize the fields of the ToDoEvent object.
     * @param id The unique identifier of the event.
     * @param userId The unique identifier of the user who created the event.
     * @param logId The unique identifier of the log where the event is created.
     * @param description The description of the event.
     * @param comments The list of comments on the event.
     * @param likes The list of likes on the event.
     */
    public ToDoEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.toDoItem = description;
        this.comments = comments;
        this.likes = likes;
    }
    /**
     * Getter method to return the to-do item.
     * @return The to-do item.
     */
    @Override
    public List<String> getComments(){
        return comments;
    }
    /**
     * Getter method to return the list of comments.
     * @return The list of comments.
     */
    @Override
    public String getDescription() {
        return toDoItem;
    }
    /**
     * Getter method to return the list of likes.
     * @return The list of likes.
     */
    @Override
    public String setDescription() {
        return "completed To-Do \"" + toDoItem + "\"";
    }
}

