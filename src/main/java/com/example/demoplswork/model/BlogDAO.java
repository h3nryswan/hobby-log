package com.example.demoplswork.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * BlogDAO class is responsible for performing CRUD operations on the Blog table in the database.
 * It extends the BaseDAO class to utilize the database connection and table creation methods.
 * It implements the IBlogDAO interface to ensure the implementation of required methods.
 * It has methods to insert a new blog, retrieve all blogs, and retrieve a blog by its ID.
 * It uses prepared statements to prevent SQL injection attacks.
 * It handles SQL exceptions and ensures the database connection is properly closed after operations.
 */
public class BlogDAO extends BaseDAO implements IBlogDAO {

    /*
        * Method to insert a new blog into the database.
     */
    @Override
    public void insertBlog(Blog blog) throws SQLException {
        String query = "INSERT INTO blogs (user_id, title, description, image_path, tag) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blog.getUserId());
            statement.setString(2, blog.getTitle());
            statement.setString(3, blog.getDescription());
            statement.setString(4, blog.getImagePath());
            statement.setString(5, blog.getTag());  // Insert the tag
            statement.executeUpdate();
        }
    }

    /*
        * Method to retrieve all blogs by from the database.
     */
    @Override
    public List<Blog> getAllBlogs() throws SQLException {
        String query = "SELECT * FROM blogs";
        List<Blog> blogs = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Blog blog = new Blog(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getString("tag")  // Retrieve the tag
                );
                blogs.add(blog);
            }
        }
        return blogs;
    }

}

