package com.ucoruh.projectmanagement.models;

/**
 * Represents a task that belongs to a project.
 *
 * File format (tasks.txt) — one task per line:
 *   id|projectId|title|description|assignedTo|status|createdAt
 *   e.g.  T001|P001|Design Homepage|Create wireframes|alice|TODO|2026-03-10
 *
 * Status values: TODO | IN_PROGRESS | DONE
 * IDs are generated as T + zero-padded sequential number (T001, T002 …).
 */
public class Task {

    // -------------------------------------------------------
    //  Status constants — avoids magic strings throughout code
    // -------------------------------------------------------

    /** Task has not been started yet. */
    public static final String STATUS_TODO        = "TODO";

    /** Task is currently being worked on. */
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";

    /** Task has been completed. */
    public static final String STATUS_DONE        = "DONE";

    // -------------------------------------------------------
    //  Fields
    // -------------------------------------------------------

    /** Auto-generated unique identifier (e.g. "T001"). */
    private String id;

    /** ID of the project this task belongs to. */
    private String projectId;

    /** Short title/name of the task. */
    private String title;

    /** Detailed description of what needs to be done. */
    private String description;

    /** Username of the team member this task is assigned to. */
    private String assignedTo;

    /** Current workflow status: TODO, IN_PROGRESS, or DONE. */
    private String status;

    /** Date the task was created (stored as yyyy-MM-dd string). */
    private String createdAt;

    // -------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------

    /**
     * Full constructor used when creating or loading a task.
     *
     * @param id          unique task identifier
     * @param projectId   parent project identifier
     * @param title       task title
     * @param description what needs to be done
     * @param assignedTo  username of assignee
     * @param status      current status (TODO / IN_PROGRESS / DONE)
     * @param createdAt   creation date (yyyy-MM-dd)
     */
    public Task(String id, String projectId, String title,
                String description, String assignedTo,
                String status, String createdAt) {
        this.id          = id;
        this.projectId   = projectId;
        this.title       = title;
        this.description = description;
        this.assignedTo  = assignedTo;
        this.status      = status;
        this.createdAt   = createdAt;
    }

    // -------------------------------------------------------
    //  Getters and Setters
    // -------------------------------------------------------

    public String getId()          { return id; }
    public void   setId(String id) { this.id = id; }

    public String getProjectId()               { return projectId; }
    public void   setProjectId(String pid)     { this.projectId = pid; }

    public String getTitle()              { return title; }
    public void   setTitle(String title)  { this.title = title; }

    public String getDescription()               { return description; }
    public void   setDescription(String desc)    { this.description = desc; }

    public String getAssignedTo()                { return assignedTo; }
    public void   setAssignedTo(String username) { this.assignedTo = username; }

    public String getStatus()              { return status; }
    public void   setStatus(String status) { this.status = status; }

    public String getCreatedAt()                 { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // -------------------------------------------------------
    //  Serialization / Deserialization helpers
    // -------------------------------------------------------

    /**
     * Converts this Task into a pipe-delimited string for file storage.
     * @return "id|projectId|title|description|assignedTo|status|createdAt"
     */
    public String toFileString() {
        return id + "|" + projectId + "|" + title + "|" + description
                + "|" + assignedTo + "|" + status + "|" + createdAt;
    }

    /**
     * Parses a pipe-delimited line from tasks.txt back into a Task object.
     *
     * @param line a single line from the tasks file
     * @return a Task object, or null if the line is malformed
     */
    public static Task fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.trim().split("\\|");
        if (parts.length != 7) return null;
        return new Task(parts[0], parts[1], parts[2],
                        parts[3], parts[4], parts[5], parts[6]);
    }

    // -------------------------------------------------------
    //  toString
    // -------------------------------------------------------

    @Override
    public String toString() {
        return "Task{id='" + id + "', title='" + title + "', status='" + status
                + "', assignedTo='" + assignedTo + "'}";
    }
}
