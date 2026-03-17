package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class SQLiteUserRepository
 * @brief SQLite implementation of the User repository.
 * @details Implements IRepository to persist User data to a local SQLite database.
 */
public class SQLiteUserRepository implements IRepository<User> {

    /**
     * @brief The connection string for the SQLite database.
     */
    private static final String URL = "jdbc:sqlite:projectmanager.db";

    /**
     * @brief Constructor for SQLiteUserRepository.
     * @details Ensures that the users table exists when the repository is created.
     */
    public SQLiteUserRepository() {
        createTableIfNotExists();
    }

    /**
     * @brief Creates the users table if it does not already exist.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                   + "id TEXT PRIMARY KEY,"
                   + "username TEXT NOT NULL,"
                   + "email TEXT NOT NULL,"
                   + "password TEXT NOT NULL"
                   + ");";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating users table: " + e.getMessage());
        }
    }

    /**
     * @brief Inserts a new User into the SQLite db.
     * @param entity The User object to insert.
     */
    @Override
    public void create(User entity) {
        String sql = "INSERT INTO users(id, username, email, password) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getUsername());
            pstmt.setString(3, entity.getEmail());
            pstmt.setString(4, entity.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    /**
     * @brief Queries for a User by identifier from the SQLite db.
     * @param id The unique identifier.
     * @return The matched User, or null if missing.
     */
    @Override
    public User findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * @brief Retrieves all Users stored in the SQLite db.
     * @return A list of all User entities.
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * @brief Updates an existing User's attributes in the SQLite db.
     * @param entity The User instance containing modified data.
     */
    @Override
    public void update(User entity) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getUsername());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getPassword());
            pstmt.setString(4, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    /**
     * @brief Evicts a User from the SQLite database by ID.
     * @param id The ID of the User to remove.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }
}
