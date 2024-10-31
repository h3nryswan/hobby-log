package com.example.demoplswork.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * LogEventUtils class provides utility methods for serializing and deserializing comments and likes.
 * It has a method to serialize a list of comments into a string with "||" as a delimiter.
 * It has a method to deserialize a string of comments separated by "||" into a list.
 * It has a method to serialize a list of likes (integers) into a comma-separated string.
 * It has a method to deserialize a comma-separated string of likes into a list of integers.
 */
public class LogEventUtils {
    /**
     * Serialize a list of comments into a string with "||" as a delimiter.
     * @param comments List of comments
     * @return String with comments separated by "||"
     */
    // Serialize List to a comma-separated string
    public static String serializeComments(List<String> comments) {
        return String.join("||", comments); // Use "||" as a delimiter to handle cases where comments may contain commas
    }
    /**
     * Deserialize a string of comments separated by "||" into a list.
     * @param commentsData String with comments separated by "||"
     * @return List of comments
     */
    // Deserialize a comma-separated string to a List
    public static List<String> deserializeComments(String commentsData) {
        if (commentsData == null || commentsData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(commentsData.split("\\|\\|")); // Split by "||" delimiter
    }
    /**
     * Serialize a list of likes (integers) into a comma-separated string.
     * @param likes List of likes
     * @return String with likes separated by commas
     */
    // Serialize List of Integers to a comma-separated string
    public static String serializeLikes(List<Integer> likes) {
        List<String> likeStrings = new ArrayList<>();
        for (Integer like : likes) {
            likeStrings.add(String.valueOf(like));
        }
        return String.join(",", likeStrings); // Use comma to separate user IDs
    }
    /**
     * Deserialize a comma-separated string of likes into a list of integers.
     * @param likesData String with likes separated by commas
     * @return List of likes
     */
    // Deserialize a comma-separated string to a List of Integers
    public static List<Integer> deserializeLikes(String likesData) {
        List<Integer> likes = new ArrayList<>();
        if (likesData == null || likesData.isEmpty()) {
            return likes;
        }
        String[] likeStrings = likesData.split(",");
        for (String like : likeStrings) {
            try {
                likes.add(Integer.parseInt(like.trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return likes;
    }
}
