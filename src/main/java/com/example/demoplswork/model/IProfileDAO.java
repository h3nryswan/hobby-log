package com.example.demoplswork.model;

import java.sql.SQLException;
/**
 * The IProfileDAO interface defines the methods for performing CRUD operations on the Profile table in the database.
 * It includes methods to insert a new profile, retrieve a profile by user ID, and update a profile.
 * Implementing classes should handle SQL exceptions and ensure the database connection is properly managed.
 */
public interface IProfileDAO {
    /**
     * Inserts a new profile into the database with the specified user ID, bio, and photo.
     * @param userId The user ID associated with the profile
     * @param bio The bio or description of the user
     * @param photo The file path or URL of the user's profile photo
     * @throws SQLException If an SQL exception occurs while inserting the profile
     */
    void insertProfile(int userId, String bio, String photo) throws SQLException;
    /**
     * Retrieves a profile from the database with the specified user ID and populates the provided Contact object with the profile information.
     * @param contact The Contact object to populate with the profile information
     * @param userId The user ID associated with the profile to retrieve
     */
    void getProfileByUserId(Contact contact, int userId);
    /**
     * Updates the profile in the database with the specified user ID, bio, and photo.
     * @param userId The user ID associated with the profile to update
     * @param bio The updated bio or description of the user
     * @param photo The updated file path or URL of the user's profile photo
     * @throws SQLException If an SQL exception occurs while updating the profile
     */
    void updateProfile(int userId, String bio, String photo) throws SQLException;

}

