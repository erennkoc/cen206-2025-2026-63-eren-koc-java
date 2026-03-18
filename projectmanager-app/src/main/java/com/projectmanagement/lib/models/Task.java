package com.projectmanagement.lib.models;

/**
 * Represents a task within a project.
 * <p> Inherits from BaseEntity. Encapsulates task data such as title, status, and the assigned user.
 */
public class Task extends BaseEntity {
    /**
     * The title of the task.
     */
    private String title;

    /**
     * The detailed description of the task.
     */
    private String description;

    /**
     * The current status of the task.
     */
    private TaskStatus status;

    /**
     * The user assigned to complete the task.
     */
    private User assignedUser;

    /**
     * Default constructor for Task.
     * <p> Sets the default status to TODO.
     */
    public Task() {
        super();
        this.status = TaskStatus.TODO;
    }

    /**
     * Parameterized constructor for Task.
     * @param id The unique identifier of the task.
     * @param title The title of the task.
     * @param description The task description.
     */
    public Task(String id, String title, String description) {
        super(id);
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
    }

    /**
     * Gets the title of the task.
     * @return The task title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     * @param title The new task title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the task.
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * @param description The new task description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the current status of the task.
     * @return The task status.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the task.
     * @param status The new task status.
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Gets the user assigned to the task.
     * @return The assigned user, or null if unassigned.
     */
    public User getAssignedUser() {
        return assignedUser;
    }

    /**
     * Assigns a user to the task.
     * @param assignedUser The user to be assigned to this task.
     */
    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    /**
     * Displays the details of the task.
     * <p> Implementation of the abstract method from BaseEntity.
     */
    @Override
    public void displayDetails() {
        System.out.println("Task ID: " + getId() + ", Title: " + title);
        System.out.println("Status: " + status);
        if (assignedUser != null) {
            System.out.println("Assigned To: " + assignedUser.getUsername());
        } else {
            System.out.println("Assigned To: Unassigned");
        }
    }
}
