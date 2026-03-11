package com.ucoruh.projectmanagement.models;

import java.io.Serializable;

/**
 * @class Project
 * @brief Represents a project in the project management system.
 * @details Holds all metadata for a project including name, description, and
 *          date range. Implements Serializable so it can be persisted via Java
 *          File I/O (object serialization to a binary file).
 */
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique auto-incremented identifier for this project. */
    private int projectId;

    /** Human-readable name of the project. */
    private String projectName;

    /** Optional detailed description of the project's goal or scope. */
    private String description;

    /** Project start date stored as a string in "YYYY-MM-DD" format. */
    private String startDate;

    /** Project end (deadline) date stored as a string in "YYYY-MM-DD" format. */
    private String endDate;

    /**
     * @brief Default no-arg constructor required for serialization.
     */
    public Project() {}

    /**
     * @brief Constructs a Project with all fields set.
     *
     * @param projectId   unique project identifier
     * @param projectName human-readable project name
     * @param description optional description / scope
     * @param startDate   start date in "YYYY-MM-DD" format
     * @param endDate     end date in "YYYY-MM-DD" format
     */
    public Project(int projectId, String projectName, String description,
                   String startDate, String endDate) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    /** @return the project ID */
    public int getProjectId() { return projectId; }

    /** @param projectId the project ID to set */
    public void setProjectId(int projectId) { this.projectId = projectId; }

    /** @return the project name */
    public String getProjectName() { return projectName; }

    /** @param projectName the project name to set */
    public void setProjectName(String projectName) { this.projectName = projectName; }

    /** @return the description */
    public String getDescription() { return description; }

    /** @param description the description to set */
    public void setDescription(String description) { this.description = description; }

    /** @return the start date string */
    public String getStartDate() { return startDate; }

    /** @param startDate the start date to set (YYYY-MM-DD) */
    public void setStartDate(String startDate) { this.startDate = startDate; }

    /** @return the end date string */
    public String getEndDate() { return endDate; }

    /** @param endDate the end date to set (YYYY-MM-DD) */
    public void setEndDate(String endDate) { this.endDate = endDate; }

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    /**
     * @brief Returns a formatted multi-line summary of the project.
     * @return formatted project details string
     */
    public String toDisplayString() {
        return String.format(
            "  Project ID  : %d%n" +
            "  Name        : %s%n" +
            "  Description : %s%n" +
            "  Start Date  : %s%n" +
            "  End Date    : %s",
            projectId, projectName, description, startDate, endDate);
    }

    @Override
    public String toString() {
        return String.format("Project{id=%d, name='%s', start='%s', end='%s'}",
                projectId, projectName, startDate, endDate);
    }
}
