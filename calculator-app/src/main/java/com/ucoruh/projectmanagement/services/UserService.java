package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.User;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.util.List;
import java.util.Scanner;

/**
 * @class UserService
 * @brief Handles user authentication: registration, login, and guest mode.
 * @details Users are persisted in a serialized binary file under "data/users.dat".
 *          Passwords are stored in plain text (suitable for a university assignment).
 */
public class UserService {

    /** Path to the serialized users data file. */
    private static final String FILE_PATH = "data/users.dat";

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * @brief Prompts the user to register a new account.
     *
     * @param scanner the shared Scanner for console input
     * @return the newly created User, or null if the username already exists
     */
    public User register(Scanner scanner) {
        System.out.println("\n=== REGISTER ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("[ERROR] Username cannot be empty.");
            return null;
        }

        List<User> users = FileHelper.loadList(FILE_PATH);
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("[ERROR] Username already exists. Please choose another.");
                return null;
            }
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("[ERROR] Password cannot be empty.");
            return null;
        }

        User newUser = new User(username, password, "USER");
        users.add(newUser);
        FileHelper.saveList(FILE_PATH, users);

        System.out.println("[OK] Registration successful! Welcome, " + username + ".");
        return newUser;
    }

    /**
     * @brief Prompts the user to log in with existing credentials.
     *
     * @param scanner the shared Scanner for console input
     * @return the authenticated User, or null on failure
     */
    public User login(Scanner scanner) {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        List<User> users = FileHelper.loadList(FILE_PATH);
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                System.out.println("[OK] Login successful! Welcome back, " + username + ".");
                return u;
            }
        }

        System.out.println("[ERROR] Invalid username or password.");
        return null;
    }

    /**
     * @brief Returns a guest User without requiring credentials.
     *
     * @return a transient User with role "GUEST"
     */
    public User loginAsGuest() {
        System.out.println("[INFO] Continuing as Guest. Some features may be restricted.");
        return new User("Guest", "", "GUEST");
    }
}
