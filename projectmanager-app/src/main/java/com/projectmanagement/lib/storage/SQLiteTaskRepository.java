package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class SQLiteTaskRepository
 * @brief SQLite implementation of the Task repository.
 * @details Implements IRepository to persist Task entities securely within an SQLite local db.
 */
public class SQLiteTaskRepository implements IRepository<Task> {

    /**
     * @brief SQLite database connection string.
     */
    private static final String URL = "jdbc:sqlite:projectmanager.db";

    /**
     * @brief Constructor for SQLiteTaskRepository.
     */
    public SQLiteTaskRepository() {
        createTableIfNotExists();
    }

    /**
     * @brief Creates tasks table if missing.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks ("
                   + "id TEXT PRIMARY KEY,"
                   + "title TEXT NOT NULL,"
                   + "description TEXT,"
                   + "status TEXT NOT NULL,"
                   + "assigned_user_id TEXT"
                   + ");";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating tasks table: " + e.getMessage());
        }
    }

    /**
     * @brief Initiates a Task entity registry within the SQLite db.
     * @param entity The Task implementation object.
     */
    @Override
    public void create(Task entity) {
        String sql = "INSERT INTO tasks(id, title, description, status, assigned_user_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getTitle());
            pstmt.setString(3, entity.getDescription());
            pstmt.setString(4, entity.getStatus().name());
            pstmt.setString(5, entity.getAssignedUser() != null ? entity.getAssignedUser().getId() : null);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
    }

    /**
     * @brief Reads a specific Task from the SQLite db using its ID.
     * @param id Tracking index of the desired Task.
     * @return Single Task reference, or null when undiscovered.
     */
    @Override
    public Task findById(String id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Task task = new Task(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("description")
                );
                task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                String userId = rs.getString("assigned_user_id");
                if (userId != null) {
                    User assignee = new SQLiteUserRepository().findById(userId);
                    task.setAssignedUser(assignee);
                }
                return task;
            }
        } catch (SQLException e) {
            System.err.println("Error finding task by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * @brief Discovers and yields all Task forms positioned in the SQLite db.
     * @return Exhaustive list collection of Tasks.
     */
    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            SQLiteUserRepository userRepo = new SQLiteUserRepository();
            while (rs.next()) {
                Task task = new Task(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("description")
                );
                task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                String userId = rs.getString("assigned_user_id");
                if (userId != null) {
                    task.setAssignedUser(userRepo.findById(userId));
                }
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * @brief Re-registers an associated Task inside SQLite with incoming tweaks.
     * @param entity Modified Task form.
     */
    @Override
    public void update(Task entity) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, assigned_user_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getDescription());
            pstmt.setString(3, entity.getStatus().name());
            pstmt.setString(4, entity.getAssignedUser() != null ? entity.getAssignedUser().getId() : null);
            pstmt.setString(5, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }

    /**
     * @brief Withdraws a given Task from the associated SQLite db using ID.
     * @param id The corresponding Task id string.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }
}
