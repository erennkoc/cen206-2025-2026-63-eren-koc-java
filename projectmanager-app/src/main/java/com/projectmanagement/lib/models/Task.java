package com.projectmanagement.lib.models;

/**
 * @class Task
 * @brief Represents a task within a project.
 * @details Inherits from BaseEntity. Encapsulates task data such as title, status, and the assigned user.
 */
public class Task extends BaseEntity {
    /**
     * @brief The title of the task.
     */
    private String title;

    /**
     * @brief The detailed description of the task.
     */
    private String description;

    /**
     * @brief The current status of the task.
     */
    private TaskStatus status;

    /**
     * @brief The user assigned to complete the task.
     */
    private User assignedUser;

    /**
     * @brief Default constructor for Task.
     * @details Sets the default status to TODO.
     */
    public Task() {
        super();
        this.status = TaskStatus.TODO;
    }

    /**
     * @brief Parameterized constructor for Task.
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
     * @brief Gets the title of the task.
     * @return The task title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @brief Sets the title of the task.
     * @param title The new task title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @brief Gets the description of the task.
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @brief Sets the description of the task.
     * @param description The new task description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @brief Gets the current status of the task.
     * @return The task status.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @brief Sets the status of the task.
     * @param status The new task status.
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * @brief Gets the user assigned to the task.
     * @return The assigned user, or null if unassigned.
     */
    public User getAssignedUser() {
        return assignedUser;
    }

    /**
     * @brief Assigns a user to the task.
     * @param assignedUser The user to be assigned to this task.
     */
    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    /**
     * @brief Displays the details of the task.
     * @details Implementation of the abstract method from BaseEntity.
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
