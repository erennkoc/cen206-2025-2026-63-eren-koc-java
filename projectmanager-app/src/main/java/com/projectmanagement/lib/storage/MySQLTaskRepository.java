package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the Task repository.
 * <p> Implements IRepository.
 */
public class MySQLTaskRepository implements IRepository<Task> {

    private static final String URL = "jdbc:mysql://localhost:3306/projectmanager?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER_DB = "root";
    private static final String PASS_DB = "root";

    /**
     * Constructor for MySQLTaskRepository.
     */
    public MySQLTaskRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks ("
                   + "id VARCHAR(255) PRIMARY KEY,"
                   + "title VARCHAR(255) NOT NULL,"
                   + "description TEXT,"
                   + "status VARCHAR(50) NOT NULL,"
                   + "assigned_user_id VARCHAR(255)"
                   + ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating tasks table (MySQL): " + e.getMessage());
        }
    }

    /**
     * Initiates a Task entity registry within the MySQL db.
     * @param entity The Task implementation object.
     */
    @Override
    public void save(Task entity) {
        String sql = "INSERT INTO tasks(id, title, description, status, assigned_user_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
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
     * Reads a specific Task from the MySQL db using its ID.
     * @param id Tracking index of the desired Task.
     * @return Single Task reference, or null when undiscovered.
     */
    @Override
    public Task findById(String id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = getConnection();
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
                    User assignee = new MySQLUserRepository().findById(userId);
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
     * Discovers and yields all Task forms positioned in the MySQL db.
     * @return Exhaustive list collection of Tasks.
     */
    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            MySQLUserRepository userRepo = new MySQLUserRepository();
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
     * Re-registers an associated Task inside MySQL with incoming tweaks.
     * @param entity Modified Task form.
     */
    @Override
    public void update(Task entity) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, assigned_user_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
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
     * Withdraws a given Task from the associated MySQL db using ID.
     * @param id The corresponding Task id string.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }

    protected Connection getConnection() throws SQLException { return DriverManager.getConnection(URL, USER_DB, PASS_DB); }
}
