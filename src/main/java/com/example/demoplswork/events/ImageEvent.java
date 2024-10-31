package com.example.demoplswork.events;

import com.example.demoplswork.model.LogsDAO;

import java.util.List;
/**
 * ImageEvent class is a subclass of LogEvent class. It is used to create an ImageEvent object.
 * It has a constructor that takes in an id, userId, logId, description, comments, and likes.
 * It has a getDescription method that returns the description of the ImageEvent object.
 * It has a setDescription method that sets the description of the ImageEvent object.
 * It has a getComments method that returns the comments of the ImageEvent object.
 * It has a getImagePath method to retrieve the image path for the log.
 */
public class ImageEvent extends LogEvent {
    private String imagePath;
    private List<String> comments;
    private List<Integer> likes;

    /**
     * Constructor for ImageEvent class.
     * @param id The id of the ImageEvent object.
     * @param userId The userId of the ImageEvent object.
     * @param logId The logId of the ImageEvent object.
     * @param description The description of the ImageEvent object.
     * @param comments The comments of the ImageEvent object.
     * @param likes The likes of the ImageEvent object.
     */
    public ImageEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.imagePath = description;
        this.comments = comments;
        this.likes = likes;
    }

    /**
     * Method to get the description of the ImageEvent object.
     * @return The description of the ImageEvent object.
     */
    @Override
    public String getDescription() {
        return imagePath;
    }

    /**
     * Method to get the comments of the ImageEvent object.
     * @return The comments of the ImageEvent object.
     */
    @Override
    public List<String> getComments(){
        return comments;
    }

    /**
     * Method to set the description of the ImageEvent object.
     * @return The description of the ImageEvent object.
     */
    @Override
    public String setDescription() {
        return "added new media: " + imagePath;
    }

    /**
     * Method to retrieve the image path for the log.
     * @return The image path for the log.
     */
    public String getImagePath(String description) {
        String imagePath = "";

        // Check if the description contains the expected format
        if (description.startsWith("added new media: ")) {
            // Extract the image file name from the description
            imagePath = description.substring("added new media: ".length()).trim();
        } else {
            System.out.println("Invalid description format. No image found.");
        }

        return imagePath;
    }
}


