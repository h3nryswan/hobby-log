package com.example.demoplswork.model;

import java.util.List;
/**
 * The IContactDAO interface defines the methods for performing CRUD operations on the Contact table in the database.
 * It includes methods to create a new account, authenticate a user, update user details, delete a user, and retrieve all contacts.
 * Implementing classes should handle SQL exceptions and ensure the database connection is properly managed.
 */
public interface IContactDAO {

    /**
     * Creates a new account with the specified first name, last name, email, and password.
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param password The password of the user
     * @return true if the account was successfully created, false otherwise
     */
    boolean createAccount(String firstName, String lastName, String email, String password);

    /**
     * Authenticates a user with the specified email and password.
     * @param email The email of the user
     * @param password The password of the user
     * @return true if the user is authenticated, false otherwise
     */
    boolean authenticateUser(String email, String password);

    /**
     * Updates the details of a user with the specified ID.
     * @param id The ID of the user
     * @param firstName The new first name of the user
     * @param lastName The new last name of the user
     * @param email The new email of the user
     * @param password The new password of the user
     * @return true if the user details were successfully updated, false otherwise
     */
    boolean updateUser(int id, String firstName, String lastName, String email, String password); //implement later

    /**
     * Deletes a user with the specified ID.
     * @param id The ID of the user
     * @return true if the user was successfully deleted, false otherwise
     */
    boolean deleteUser(int id);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts
     */
    List<Contact> getAllContacts();
}