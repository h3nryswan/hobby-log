package com.example.demoplswork.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The DAO class responsible for managing contacts in the database for login/register pages.
 * It provides methods to create a new account, authenticate a user, update user details, delete a user, and retrieve all contacts.
 * It also includes methods to get a user ID by email and to get a contact by user ID.
 * It uses prepared statements to prevent SQL injection attacks and handles SQL exceptions.
 */
public class ContactDAO extends BaseDAO implements IContactDAO  {


    /**
     * Creates a new account with the given first name, last name, email, and password.
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the account is created successfully, otherwise false
     */
    @Override
    public boolean createAccount(String firstName, String lastName, String email, String password) {
        String query = "INSERT INTO users (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);  // Password should be hashed in production
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if account creation fails (e.g., email already exists)
        }
    }

    /**
     * Authenticates a user with the given email and password.
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the user is authenticated successfully, otherwise false
     */
    @Override
    public boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Returns true if a match is found, otherwise false
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the user details with the given ID.
     * @param id the ID of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the user details are updated successfully, otherwise false
     */
    @Override
    public boolean updateUser(int id, String firstName, String lastName, String email, String password) {
        String query = "UPDATE users SET firstName = ?, lastName = ?, email = ?, password = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);  // Password should be hashed
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes the user with the given ID.
     * @param id the ID of the user
     * @return true if the user is deleted successfully, otherwise false
     */
    @Override
    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all contacts from the database.
     * @return a list of contacts
     */
    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String password = rs.getString("password"); // You might want to omit password here
                contacts.add(new Contact(firstName, lastName, email, password)); // Adjust according to your Contact class
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    /**
     * Retrieves the user ID with the given email.
     * @param email the email of the user
     * @return the ID of the user if found, otherwise -1
     */
    public int getUserIDByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1; // No user found with the given email
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves the contact with the given user ID.
     * @param userId the ID of the user
     * @return the contact if found, otherwise null
     */
    public Contact getContactById(int userId) {
        Contact contact = null;
        String query = "SELECT firstName, lastName FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                contact = new Contact(firstName, lastName, " ", " ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }



}

