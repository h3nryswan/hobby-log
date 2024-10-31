package com.example.demoplswork.model;

import javafx.util.Pair;

import java.util.List;
/**
 * A simple model class representing a Log with a log name, list of to do items, list of image paths, and list of materials.
 */
public class Logs {
    private int id;
    private String logName;
    private List<Pair<String, Boolean>> toDoItems;
    private double progress; // Progress (in percentage)
    private List<String> images; // List of image file paths
    private List<Material> materials; // List of materials

    /**
     * Constructs a new Contact with the specified first name, last name, bio, and profile photo.
     * @param logName The title or name of the log
     * @param toDoItems A list of to-do items with a boolean value and to-do string
     * @param images A list of image paths
     * @param materials A list of materials
     */
    // Constructor
    public Logs(String logName, List<Pair<String, Boolean>> toDoItems, List<String> images, List<Material> materials) {
        this.logName = logName;
        this.toDoItems = toDoItems;
        this.images = images;
        this.materials = materials;
    }
    /*
    * Initialize all to-do items as not completed (false)
    * */
    // Initialize all to-do items as not completed (false)
    private List<Boolean> initializeCompletionStatus(int size) {
        List<Boolean> completionStatus = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            completionStatus.add(false);
        }
        return completionStatus;
    }


    /*
    * Method to update the stats of a to-do item
    * */
    // Method to update the status of a to-do item
    public void updateToDoItemStatus(String task, boolean isChecked) {
        for (Pair<String, Boolean> toDoItem : toDoItems) {
            if (toDoItem.getKey().equals(task)) {
                // Update the checked state
                toDoItem = new Pair<>(task, isChecked);
                break;
            }
        }
    }

    /*
    * Method to add a material to the materials list
    * */
    // Method to add a material to the materials list
    public void addMaterial(Material material) {
        if (material != null) {
            materials.add(material); // Add the material to the list
        } else {
            System.err.println("Cannot add null material.");
        }
    }

    /*
    * Method to return the list of to-do items
    * */
    public List<Pair<String, Boolean>> getToDoItems() {
        return toDoItems;
    }
    /*
    * Method to set the list of to-do items
    * */
    public void setToDoItems(List<Pair<String, Boolean>> toDoItems) {
        this.toDoItems = toDoItems;
    }

    /*
    * Method to get the name of the log
    * */
    // Getters
    public String getLogName() {
        return logName;
    }


    /*
    * Method to get the list of images
    * */
    public List<String> getImages() {
        return images;
    }

    /*
    * Get the list of materials
    * */
    public List<Material> getMaterials() {
        return materials;
    }

    /*
    * Method to get the progress of the log
    * */
    public double getProgress() {
        return progress;
    }
    /*
     * Method to set the name of the log
     * */
    // Setters
    public void setLogName(String logName) {
        this.logName = logName;
    }


    /*
     * Method to get the list of images
     * */
    public void setImages(List<String> images) {
        this.images = images;
    }
    /*
     * Get the list of materials
     * */
    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    /*
     * Method to set the progress of the log
     * */
    public void setProgress(double progress) {
        this.progress = progress;
    }
}
