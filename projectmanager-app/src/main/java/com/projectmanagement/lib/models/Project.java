package com.projectmanagement.lib.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a project in the system.
 * <p> Inherits from BaseEntity. Contains a collection of tasks and assigned users, demonstrating object relationships and encapsulation.
 */
public class Project extends BaseEntity {
    /**
     * The name of the project.
     */
    private String name;

    /**
     * A brief description of the project.
     */
    private String description;

    /**
     * The list of tasks associated with this project.
     */
    private List<Task> tasks;

    /**
     * Default constructor for Project.
     * <p> Initializes the tasks list.
     */
    public Project() {
        super();
        this.tasks = new ArrayList<>();
    }

    /**
     * Parameterized constructor for Project.
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
     * Gets the name of the project.
     * @return The project name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     * @param name The new project name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the project.
     * @return The project description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the project.
     * @param description The new project description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of tasks in the project.
     * @return The list of tasks.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the list of tasks in the project.
     * @param tasks The new list of tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the project.
     * @param task The task to add.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes a task from the project.
     * @param task The task to remove.
     */
    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    /**
     * Displays the details of the project.
     * <p> Implementation of the abstract method from BaseEntity. Shows polymorphism.
     */
    @Override
    public void displayDetails() {
        System.out.println("Project ID: " + getId() + ", Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Total Tasks: " + tasks.size());
    }
}
