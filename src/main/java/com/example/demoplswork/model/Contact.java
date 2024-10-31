package com.example.demoplswork.model;

import java.util.Objects;
/**
 * A simple model class representing a contact with a first name, last name, bio, and profile photo.
 * It includes methods to get and set these properties, as well as a method to get the full name of the contact.
 * It overrides the equals and hashCode methods to compare contacts based on their properties.
 */
public class Contact {
    private int id;
    private String firstName;
    private String lastName;
    private String bio;
    private String photo;

    /**
     * Constructs a new Contact with the specified first name, last name, bio, and profile photo.
     * @param firstName The first name of the contact
     * @param lastName The last name of the contact
     * @param bio The bio of the contact
     * @param photo The profile photo of the contact
     */
    public Contact(String firstName, String lastName, String bio, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.photo = photo;
    }

    /**
    * getter method for the id of the contact
    * @return The id of the contact
    * */
    public int getId() {
        return id;
    }

    /** setter method for the id of the contact
    */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter method for the first name of the contact
     * @return The first name of the contact
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter method for the first name of the contact
     * @param firstName The first name of the contact
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter method for the last name of the contact
     * @return The last name of the contact
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * setter method for the last name of the contact
     * @param lastName The last name of the contact
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * getter method for the bio of the contact
     * @return The bio of the contact
     */
    public String getBio() {
        return bio;
    }
    /**
     * setter method for the bio of the contact
     * @param bio The bio of the contact
     */
    public void setBio(String bio) {
        this.bio = bio;
    }
    /**
     * getter method for the profile photo of the contact
     * @return The profile photo of the contact
     */
    public String getPhoto() {
        return photo;
    }
    /**
     * setter method for the profile photo of the contact
     * @param photo The profile photo of the contact
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    /**
     * Returns the full name of the contact, which is the first name followed by the last name.
     * @return The full name of the contact
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    /**
     * Returns a string representation of the contact, including the full name, bio, and profile photo.
     * @return A string representation of the contact
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return firstName.equals(contact.firstName) &&
                lastName.equals(contact.lastName) &&
                bio.equals(contact.bio) &&
                photo.equals(contact.photo);
    }
    /**
     * Returns a hash code value for the contact based on its properties.
     * @return A hash code value for the contact
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, bio, photo);
    }
}
