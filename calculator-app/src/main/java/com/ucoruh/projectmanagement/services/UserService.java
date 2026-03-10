package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.User;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * UserService — handles all user account operations.
 *
 * Responsibilities:
 *  - Register new users (check for duplicate usernames)
 *  - Authenticate users (login)
 *  - Load users from file / save to file via FileHelper
 *
 * Teammate suggestion: This class is self-contained.
 * One teammate can work on this service independently.
 */
public class UserService {

    // -------------------------------------------------------
    //  Public API
    // -------------------------------------------------------

    /**
     * Registers a new user.
     * Returns false if the username is already taken.
     *
     * @param username the desired username
     * @param password the plain-text password
     * @return true if registration succeeded, false otherwise
     */
    public boolean register(String username, String password) {
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("[ERROR] Username and password cannot be empty.");
            return false;
        }

        // Check for duplicate usernames
        List<User> users = loadAllUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return false;  // duplicate
            }
        }

        // Save new user to file
        User newUser = new User(username, password);
        FileHelper.appendLine(FileHelper.USERS_FILE, newUser.toFileString());
        System.out.println("[INFO] User registered: " + username);
        return true;
    }

    /**
     * Attempts to log in a user with the given credentials.
     *
     * @param username the username to look up
     * @param password the password to verify
     * @return the matching User object if credentials are correct, or null
     */
    public User login(String username, String password) {
        List<User> users = loadAllUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;  // credentials don't match
    }

    /**
     * Checks whether a given username exists in the system.
     * Useful for validating task assignees.
     *
     * @param username the username to check
     * @return true if the user exists
     */
    public boolean userExists(String username) {
        List<User> users = loadAllUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------
    //  Private helpers
    // -------------------------------------------------------

    /**
     * Loads all users from the data file.
     * @return list of User objects; empty list if file is empty or missing
     */
    public List<User> loadAllUsers() {
        List<String> lines = FileHelper.readAllLines(FileHelper.USERS_FILE);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            User u = User.fromFileString(line);
            if (u != null) users.add(u);
        }
        return users;
    }
}
