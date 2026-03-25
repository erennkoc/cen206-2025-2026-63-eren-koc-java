package com.projectmanagement.lib.services;

import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.storage.IRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Provides business logic and validation for User operations.
 * <p> Implements functionalities such as User Registration, Login and Password Hashing using SHA-256. Follows the Dependency Injection principle by accepting an IRepository instance.
 */
public class UserService {

    /**
     * The User repository used for data persistence.
     * <p> Abstracted via IRepository to allow flexible storage backends.
     */
    private final IRepository<User> userRepository;

    /**
     * Constructs a new UserService with the designated User repository.
     * @param userRepository The storage implementation (dependency) to be injected.
     */
    public UserService(IRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user in the system.
     * <p> Securely hashes the provided password before storing the User object.
     * @param id The unique identifier for the new user.
     * @param username The desired username.
     * @param email The user's email address.
     * @param plainPassword The unencrypted password to be hashed and stored.
     * @throws IllegalArgumentException If any required parameter is null or empty.
     */
    public void registerUser(String id, String username, String email, String plainPassword) {
        if (id == null || username == null || email == null || plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Registration fields cannot be null or empty.");
        }

        String hashedPassword = hashPassword(plainPassword);
        User newUser = new User(id, username, email, hashedPassword);
        userRepository.save(newUser);
    }

    /**
     * Authenticates a user attempting to log in.
     * <p> Retrieves all active users and verifies if the username and securely hashed password match a system record.
     * @param username The username attempting login.
     * @param plainPassword The unencrypted password provided during login.
     * @return The authenticated User object if credentials are correct, null otherwise.
     */
    public User login(String username, String plainPassword) {
        if (username == null || plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }

        String targetHash = hashPassword(plainPassword);
        List<User> allUsers = userRepository.findAll();
        
        // This is a basic implementation; realistic systems might query the repository directly via username
        if (allUsers != null) {
            for (User user : allUsers) {
                if (user.getUsername().equals(username) && user.getPassword().equals(targetHash)) {
                    return user; // Successful login
                }
            }
        }
        
        return null; // Login failed
    }

    /**
     * Retrieves all registered users from the repository.
     * @return List of all User instances in storage.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Utility method to securely hash a plain text password.
     * <p> Implements the "Data Security" constraint using the SHA-256 cryptographic hash function.
     * @param password The plaintext string to encompass via hashing.
     * @return The hexadecimal string representation of the hashed payload.
     * @throws RuntimeException If the underlying JVM lacks the SHA-256 algorithm.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }

    /**
     * Helper function to convert a byte array to a hex string.
     * @param hash The byte array returned from MessageDigest.
     * @return The formatted hexadecimal String.
     */
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
