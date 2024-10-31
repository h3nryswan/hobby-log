package com.example.demoplswork.model;
/**
 * Blog class represents a blog entry in the application.
 * It contains information about the blog such as the user ID, title, description, image path, and tag.
 * It has constructors to initialize a blog with or without an ID.
 * It provides getter methods to access the blog's properties.
 */
public class Blog {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String imagePath;
    private String tag;  // New field for the tag

    /**
     * Constructor to initialize a blog without an ID.
     * @param userId The user ID of the blog's author.
     * @param title The title of the blog.
     * @param description The description of the blog.
     * @param imagePath The image path of the blog.
     * @param tag The tag of the blog.
     */
    public Blog(int userId, String title, String description, String imagePath, String tag) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.tag = tag;
    }
    /**
     * Constructor to initialize a blog with an ID.
     * @param id The ID of the blog.
     * @param userId The user ID of the blog's author.
     * @param title The title of the blog.
     * @param description The description of the blog.
     * @param imagePath The image path of the blog.
     * @param tag The tag of the blog.
     */
    public Blog(int id, int userId, String title, String description, String imagePath, String tag) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.tag = tag;
    }
    /**
     * Getter for user ID
     * @return The user ID of the blog's author.
     */
    // Getter for tag
    public String getTag() {
        return tag;
    }

    /**
     * Getter for blog ID
     * @return The ID of the blog.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for user ID
     * @return The user ID of the blog's author.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter for title
     * @return The title of the blog.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for description
     * @return The description of the blog.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for image path
     * @return The image path of the blog.
     */
    public String getImagePath() {
        return imagePath;
    }
}

