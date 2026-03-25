package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the User repository.
 * <p> Implements IRepository. Connects to a remote MySQL database for User CRUD operations.
 */
public class MySQLUserRepository implements IRepository<User> {

    private static final String URL = "jdbc:mysql://localhost:3306/projectmanager?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER_DB = "root";
    private static final String PASS_DB = "root";

    /**
     * Constructor for MySQLUserRepository.
     */
    public MySQLUserRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                   + "id VARCHAR(255) PRIMARY KEY,"
                   + "username VARCHAR(255) NOT NULL,"
                   + "email VARCHAR(255) NOT NULL,"
                   + "password VARCHAR(255) NOT NULL"
                   + ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating users table (MySQL): " + e.getMessage());
        }
    }

    /**
     * Persists a novel User entity to the MySQL db.
     * @param entity The User to save.
     */
    @Override
    public void save(User entity) {
        String sql = "INSERT INTO users(id, username, email, password) VALUES(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getUsername());
            pstmt.setString(3, entity.getEmail());
            pstmt.setString(4, entity.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating user (MySQL): " + e.getMessage());
        }
    }

    /**
     * Searches the MySQL db for a given User ID.
     * @param id The target User identifier.
     * @return The matched User, or null if unlocated.
     */
    @Override
    public User findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
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
            System.err.println("Error finding user by id (MySQL): " + e.getMessage());
        }
        return null;
    }

    /**
     * Pulls all Users from the MySQL tables.
     * @return Full list of User entities.
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
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
            System.err.println("Error finding all users (MySQL): " + e.getMessage());
        }
        return users;
    }

    /**
     * Merges an updated User entity into the MySQL db.
     * @param entity User object sporting recent updates.
     */
    @Override
    public void update(User entity) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getUsername());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getPassword());
            pstmt.setString(4, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user (MySQL): " + e.getMessage());
        }
    }

    /**
     * Executes a DELETE statement on a User in the MySQL db.
     * @param id Target ID to drop.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user (MySQL): " + e.getMessage());
        }
    }

    protected Connection getConnection() throws SQLException { return DriverManager.getConnection(URL, USER_DB, PASS_DB); }
}
