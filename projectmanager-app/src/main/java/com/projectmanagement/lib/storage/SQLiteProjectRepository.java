package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of the Project repository.
 * <p> Implements IRepository to persist Project entities to a local SQLite database.
 */
public class SQLiteProjectRepository implements IRepository<Project> {

    /**
     * SQLite database url
     */
    private static final String URL = "jdbc:sqlite:projectmanager.db";

    /**
     * Constructor for SQLiteProjectRepository.
     */
    public SQLiteProjectRepository() {
        createTableIfNotExists();
    }

    /**
     * Creates projects table.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS projects ("
                   + "id TEXT PRIMARY KEY,"
                   + "name TEXT NOT NULL,"
                   + "description TEXT"
                   + ");";
        String mappingSql = "CREATE TABLE IF NOT EXISTS project_tasks ("
                   + "project_id TEXT,"
                   + "task_id TEXT,"
                   + "PRIMARY KEY (project_id, task_id)"
                   + ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(mappingSql);
        } catch (SQLException e) {
            System.err.println("Error creating projects table: " + e.getMessage());
        }
    }

    /**
     * Inserts a new Project record into the SQLite db.
     * @param entity The Project instance to be added.
     */
    @Override
    public void create(Project entity) {
        String sql = "INSERT INTO projects(id, name, description) VALUES(?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getDescription());
            pstmt.executeUpdate();
            
            saveProjectTasks(conn, entity);
        } catch (SQLException e) {
            System.err.println("Error creating project: " + e.getMessage());
        }
    }

    private void saveProjectTasks(Connection conn, Project entity) throws SQLException {
        String delSql = "DELETE FROM project_tasks WHERE project_id = ?";
        try (PreparedStatement delStmt = conn.prepareStatement(delSql)) {
            delStmt.setString(1, entity.getId());
            delStmt.executeUpdate();
        }

        if (entity.getTasks() != null && !entity.getTasks().isEmpty()) {
            String insSql = "INSERT INTO project_tasks (project_id, task_id) VALUES (?, ?)";
            try (PreparedStatement insStmt = conn.prepareStatement(insSql)) {
                for (com.projectmanagement.lib.models.Task task : entity.getTasks()) {
                    insStmt.setString(1, entity.getId());
                    insStmt.setString(2, task.getId());
                    insStmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Looks up a Project from the SQLite db based on ID.
     * @param id The sought-after Project ID.
     * @return The distinct Project, or null if undiscovered.
     */
    @Override
    public Project findById(String id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Project project = new Project(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                loadProjectTasks(conn, project);
                return project;
            }
        } catch (SQLException e) {
            System.err.println("Error finding project by id: " + e.getMessage());
        }
        return null;
    }

    private void loadProjectTasks(Connection conn, Project project) throws SQLException {
        String taskSql = "SELECT task_id FROM project_tasks WHERE project_id = ?";
        try (PreparedStatement tpstmt = conn.prepareStatement(taskSql)) {
            tpstmt.setString(1, project.getId());
            ResultSet trs = tpstmt.executeQuery();
            SQLiteTaskRepository taskRepo = new SQLiteTaskRepository();
            while (trs.next()) {
                com.projectmanagement.lib.models.Task task = taskRepo.findById(trs.getString("task_id"));
                if (task != null) {
                    project.addTask(task);
                }
            }
        }
    }

    /**
     * Retrieves the entirety of Project records from the SQLite db.
     * @return Complete list of Project occurrences.
     */
    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Project project = new Project(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                loadProjectTasks(conn, project);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all projects: " + e.getMessage());
        }
        return projects;
    }

    /**
     * Applies modifications to a documented Project in the SQLite db.
     * @param entity Project entity carrying current revisions.
     */
    @Override
    public void update(Project entity) {
        String sql = "UPDATE projects SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());
            pstmt.setString(3, entity.getId());
            pstmt.executeUpdate();
            
            saveProjectTasks(conn, entity);
        } catch (SQLException e) {
            System.err.println("Error updating project: " + e.getMessage());
        }
    }

    /**
     * Eradicates a Project from the SQLite database stream by ID.
     * @param id Targeted Project identifier.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            
            String delSql = "DELETE FROM project_tasks WHERE project_id = ?";
            try (PreparedStatement delStmt = conn.prepareStatement(delSql)) {
                delStmt.setString(1, id);
                delStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting project: " + e.getMessage());
        }
    }

    protected Connection getConnection() throws SQLException { return DriverManager.getConnection(URL); }
}
