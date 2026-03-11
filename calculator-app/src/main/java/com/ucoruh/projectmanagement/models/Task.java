package com.ucoruh.projectmanagement.models;

import java.io.Serializable;

/**
 * @class Task
 * @brief Represents a task assigned within a project.
 * @details Contains task details including assignment, status, and the project
 *          it belongs to. Implements Serializable for File I/O persistence.
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique identifier for this task. */
    private int taskId;

    /** Human-readable name of the task. */
    private String taskName;

    /** Detailed description of what the task involves. */
    private String description;

    /** Username of the user this task is assigned to. */
    private String assignedTo;

    /** Current status: "PENDING", "IN_PROGRESS", or "DONE". */
    private String status;

    /** ID of the project this task belongs to. */
    private int projectId;

    /**
     * @brief Default no-arg constructor required for serialization.
     */
    public Task() {}

    /**
     * @brief Constructs a Task with all fields set.
     *
     * @param taskId     unique task ID
     * @param taskName   name of the task
     * @param description description of the task
     * @param assignedTo username assigned to this task
     * @param status     current task status
     * @param projectId  project this task belongs to
     */
    public Task(int taskId, String taskName, String description,
                String assignedTo, String status, int projectId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.assignedTo = assignedTo;
        this.status = status;
        this.projectId = projectId;
    }

    /** @return the task ID */
    public int getTaskId() { return taskId; }

    /** @param taskId the task ID to set */
    public void setTaskId(int taskId) { this.taskId = taskId; }

    /** @return the task name */
    public String getTaskName() { return taskName; }

    /** @param taskName the task name to set */
    public void setTaskName(String taskName) { this.taskName = taskName; }

    /** @return the description */
    public String getDescription() { return description; }

    /** @param description the description to set */
    public void setDescription(String description) { this.description = description; }

    /** @return the username this task is assigned to */
    public String getAssignedTo() { return assignedTo; }

    /** @param assignedTo the assigned username */
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    /** @return the current status */
    public String getStatus() { return status; }

    /** @param status the status to set */
    public void setStatus(String status) { this.status = status; }

    /** @return the project ID */
    public int getProjectId() { return projectId; }

    /** @param projectId the project ID to set */
    public void setProjectId(int projectId) { this.projectId = projectId; }

    @Override
    public String toString() {
        return String.format("Task{id=%d, name='%s', assignedTo='%s', status='%s', projectId=%d}",
                taskId, taskName, assignedTo, status, projectId);
    }
}
