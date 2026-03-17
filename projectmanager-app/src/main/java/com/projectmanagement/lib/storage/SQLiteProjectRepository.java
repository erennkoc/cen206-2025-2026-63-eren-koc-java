package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class SQLiteProjectRepository
 * @brief SQLite implementation of the Project repository.
 * @details Implements IRepository to persist Project entities to a local SQLite database.
 */
public class SQLiteProjectRepository implements IRepository<Project> {

    /**
     * @brief SQLite database url
     */
    private static final String URL = "jdbc:sqlite:projectmanager.db";

    /**
     * @brief Constructor for SQLiteProjectRepository.
     */
    public SQLiteProjectRepository() {
        createTableIfNotExists();
    }

    /**
     * @brief Creates projects table.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS projects ("
                   + "id TEXT PRIMARY KEY,"
                   + "name TEXT NOT NULL,"
                   + "description TEXT"
                   + ");";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating projects table: " + e.getMessage());
        }
    }

    /**
     * @brief Inserts a new Project record into the SQLite db.
     * @param entity The Project instance to be added.
     */
    @Override
    public void create(Project entity) {
        String sql = "INSERT INTO projects(id, name, description) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating project: " + e.getMessage());
        }
    }

    /**
     * @brief Looks up a Project from the SQLite db based on ID.
     * @param id The sought-after Project ID.
     * @return The distinct Project, or null if undiscovered.
     */
    @Override
    public Project findById(String id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Project(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding project by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * @brief Retrieves the entirety of Project records from the SQLite db.
     * @return Complete list of Project occurrences.
     */
    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projects.add(new Project(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all projects: " + e.getMessage());
        }
        return projects;
    }

    /**
     * @brief Applies modifications to a documented Project in the SQLite db.
     * @param entity Project entity carrying current revisions.
     */
    @Override
    public void update(Project entity) {
        String sql = "UPDATE projects SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());
            pstmt.setString(3, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating project: " + e.getMessage());
        }
    }

    /**
     * @brief Eradicates a Project from the SQLite database stream by ID.
     * @param id Targeted Project identifier.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting project: " + e.getMessage());
        }
    }
}
