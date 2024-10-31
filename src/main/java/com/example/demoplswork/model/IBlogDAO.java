package com.example.demoplswork.model;


import java.sql.SQLException;
import java.util.List;
/**
 * The IBlogDAO interface defines the methods for performing CRUD operations on the Blog table in the database.
 * It includes methods to insert a new blog and retrieve all blogs.
 * Implementing classes should handle SQL exceptions and ensure the database connection is properly managed.
 */
public interface IBlogDAO {
    /**
     * Inserts a new blog into the database.
     * @param blog The blog object to insert
     * @throws SQLException If an SQL exception occurs while inserting the blog
     */
    void insertBlog(Blog blog) throws SQLException;
    /**
     * Retrieves all blogs from the database.
     * @return A list of all blogs
     * @throws SQLException If an SQL exception occurs while retrieving the blogs
     */
    List<Blog> getAllBlogs() throws SQLException;
}
