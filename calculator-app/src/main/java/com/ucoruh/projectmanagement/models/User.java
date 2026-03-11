package com.ucoruh.projectmanagement.models;

import java.io.Serializable;

/**
 * @class User
 * @brief Represents an application user.
 * @details Stores user credentials and role information. Implements Serializable
 *          so it can be saved/loaded via Java File I/O.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique username used to identify the user. */
    private String username;

    /** Hashed or plain-text password of the user. */
    private String password;

    /** Role of the user: "ADMIN", "USER", or "GUEST". */
    private String role;

    /**
     * @brief Default no-arg constructor required for serialization.
     */
    public User() {}

    /**
     * @brief Constructs a User with all fields set.
     *
     * @param username the unique username
     * @param password the user's password
     * @param role     the user's role
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /** @return the username */
    public String getUsername() { return username; }

    /** @param username the username to set */
    public void setUsername(String username) { this.username = username; }

    /** @return the password */
    public String getPassword() { return password; }

    /** @param password the password to set */
    public void setPassword(String password) { this.password = password; }

    /** @return the role */
    public String getRole() { return role; }

    /** @param role the role to set */
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}
