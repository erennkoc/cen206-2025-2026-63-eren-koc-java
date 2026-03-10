package com.ucoruh.projectmanagement.models;

/**
 * Represents a registered user of the application.
 *
 * File format (users.txt):
 *   username|password
 *   e.g.  john|pass123
 *
 * NOTE: Passwords are stored in plain text for simplicity (university assignment).
 *       In production, always hash passwords (e.g., BCrypt).
 */
public class User {

    // -------------------------------------------------------
    //  Fields
    // -------------------------------------------------------

    /** The unique login name of the user. */
    private String username;

    /** The plain-text password of the user. */
    private String password;

    // -------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------

    /**
     * Creates a new User with the given credentials.
     *
     * @param username the unique login name
     * @param password the plain-text password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // -------------------------------------------------------
    //  Getters and Setters
    // -------------------------------------------------------

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @param username new username value */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the password */
    public String getPassword() {
        return password;
    }

    /** @param password new password value */
    public void setPassword(String password) {
        this.password = password;
    }

    // -------------------------------------------------------
    //  Serialization / Deserialization helpers
    // -------------------------------------------------------

    /**
     * Converts this User into a pipe-delimited string for file storage.
     * @return "username|password"
     */
    public String toFileString() {
        return username + "|" + password;
    }

    /**
     * Parses a pipe-delimited line from users.txt back into a User object.
     *
     * @param line a single line from the users file
     * @return a User object, or null if the line is malformed
     */
    public static User fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.trim().split("\\|");
        if (parts.length != 2) return null;
        return new User(parts[0], parts[1]);
    }

    // -------------------------------------------------------
    //  toString
    // -------------------------------------------------------

    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }
}
