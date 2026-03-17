package com.projectmanagement.lib.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Project
 * @brief Represents a project in the system.
 * @details Inherits from BaseEntity. Contains a collection of tasks and assigned users, demonstrating object relationships and encapsulation.
 */
public class Project extends BaseEntity {
    /**
     * @brief The name of the project.
     */
    private String name;

    /**
     * @brief A brief description of the project.
     */
    private String description;

    /**
     * @brief The list of tasks associated with this project.
     */
    private List<Task> tasks;

    /**
     * @brief Default constructor for Project.
     * @details Initializes the tasks list.
     */
    public Project() {
        super();
        this.tasks = new ArrayList<>();
    }

    /**
     * @brief Parameterized constructor for Project.
     * @param id The unique identifier for the project.
     * @param name The name of the project.
     * @param description The description of the project.
     */
    public Project(String id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
        this.tasks = new ArrayList<>();
    }

    /**
     * @brief Gets the name of the project.
     * @return The project name.
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Sets the name of the project.
     * @param name The new project name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief Gets the description of the project.
     * @return The project description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @brief Sets the description of the project.
     * @param description The new project description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @brief Gets the list of tasks in the project.
     * @return The list of tasks.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * @brief Sets the list of tasks in the project.
     * @param tasks The new list of tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * @brief Adds a task to the project.
     * @param task The task to add.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * @brief Removes a task from the project.
     * @param task The task to remove.
     */
    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    /**
     * @brief Displays the details of the project.
     * @details Implementation of the abstract method from BaseEntity. Shows polymorphism.
     */
    @Override
    public void displayDetails() {
        System.out.println("Project ID: " + getId() + ", Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Total Tasks: " + tasks.size());
    }
}
