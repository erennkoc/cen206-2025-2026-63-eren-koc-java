package com.projectmanagement.lib.models;

/**
 * @class User
 * @brief Represents a user in the project management system.
 * @details Inherits from BaseEntity and encapsulates user-specific data like username and email.
 */
public class User extends BaseEntity {
    /**
     * @brief The username of the user.
     */
    private String username;

    /**
     * @brief The email address of the user.
     */
    private String email;

    /**
     * @brief The password of the user.
     */
    private String password;

    /**
     * @brief Default constructor for User.
     */
    public User() {
        super();
    }

    /**
     * @brief Parameterized constructor for User.
     * @param id The unique identifier of the user.
     * @param username The username of the user.
     * @param email The email address of the user.
     * @param password The password for the user account.
     */
    public User(String id, String username, String email, String password) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * @brief Gets the username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @brief Sets the username.
     * @param username The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @brief Gets the email address.
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @brief Sets the email address.
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @brief Gets the user's password.
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @brief Sets the user's password.
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @brief Displays the details of the user.
     * @details Implementation of the abstract method from BaseEntity.
     */
    @Override
    public void displayDetails() {
        System.out.println("User ID: " + getId() + ", Username: " + username + ", Email: " + email);
    }
}
