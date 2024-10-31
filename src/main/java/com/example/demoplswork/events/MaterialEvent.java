package com.example.demoplswork.events;

import java.util.List;
/**
 * MaterialEvent class represents a log event where a new material is added.
 * It extends the LogEvent class to inherit common log event properties.
 * It has fields for the material name, comments, and likes.
 * It has a constructor to initialize these fields along with the inherited fields.
 * It overrides the getDescription method to return the material name.
 * It overrides the getComments method to return the list of comments.
 * It overrides the setDescription method to return a formatted description of the event.
 */
public class MaterialEvent extends LogEvent {
    private String materialName;
    private List<String> comments;
    private List<Integer> likes;
    /**
     * Constructor to initialize the fields of the MaterialEvent object.
     * @param id The unique identifier of the event.
     * @param userId The unique identifier of the user who created the event.
     * @param logId The unique identifier of the log where the event is created.
     * @param description The name of the material.
     * @param comments The list of comments on the event.
     * @param likes The list of user ids who liked the event.
     */
    public MaterialEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.materialName = description;
        this.comments = comments;
        this.likes = likes;
    }
    /**
     * Returns the description of the event.
     * @return The name of the material.
     */
    @Override
    public String getDescription() {
        return materialName;
    }
    /**
     * Returns the list of comments on the event.
     * @return The list of comments.
     */
    @Override
    public List<String> getComments(){
        return comments;
    }

    /**
     * Returns a formatted description of the event.
     * @return The formatted description.
     */
    @Override
    public String setDescription() {
        return "added new material: \"" + materialName + "\"";
    }
}

