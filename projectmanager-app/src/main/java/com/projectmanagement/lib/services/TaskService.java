package com.projectmanagement.lib.services;

import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.storage.IRepository;
import java.util.List;

/**
 * Interfaces business constraints and operations related precisely to Tasks.
 * <p> Executes core routines spanning Task Assignment, detail configuration, and Status tracking. Accepts an {@code IRepository<Task>} parameter compliant with the core Dependency Injection rule.
 */
public class TaskService {

    /**
     * The data storage implementation explicitly for Task persistence.
     */
    private final IRepository<Task> taskRepository;

    /**
     * Formulates a TaskService requiring an explicit storage implementation.
     * @param taskRepository The storage backend (dependency string) connected for CRUD.
     */
    public TaskService(IRepository<Task> taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Instantiates a blank Task form and inserts it immediately into the operational repository.
     * @param id The unique Task string identifier.
     * @param title Distinct name characterizing the Task objective.
     * @param description Narrative outlining the core deliverables of this Task.
     * @throws IllegalArgumentException Raised when any critical task elements are effectively nonexistent.
     */
    public void registerTask(String id, String title, String description) {
        if (id == null || title == null || title.isEmpty() || description == null) {
            throw new IllegalArgumentException("Task attributes ID, Title, and Description are explicitly required.");
        }

        Task newTask = new Task(id, title, description);
        taskRepository.create(newTask);
    }

    /**
     * Allocates an established User entity onto a registered target Task.
     * @param taskId Key string pointing to the persistent Task.
     * @param assignedUser Complete User object structure inheriting responsibility for this Task.
     * @throws IllegalArgumentException Triggers when either parameter is essentially unallocated or empty.
     */
    public void assignTaskToUser(String taskId, User assignedUser) {
        if (taskId == null || assignedUser == null) {
            throw new IllegalArgumentException("Target Task ID and the target User assignment cannot be vacant sequences.");
        }

        Task trackingTask = taskRepository.findById(taskId);
        
        if (trackingTask != null) {
            trackingTask.setAssignedUser(assignedUser);
            taskRepository.update(trackingTask);
        } else {
             throw new IllegalArgumentException("Target Task does not exist.");
        }
    }

    /**
     * Pushes a status transition onto an existing Task.
     * @param taskId Reference string locating the Task in progress.
     * @param newStatus Enum value directing the new tracking state (e.g., TODO, IN_PROGRESS, DONE).
     * @throws IllegalArgumentException Returns if Task ID or Target Status params are completely unbound.
     */
    public void updateTaskStatus(String taskId, TaskStatus newStatus) {
        if (taskId == null || newStatus == null) {
             throw new IllegalArgumentException("Task ID and new transition state are required constraints.");
        }

        Task operatingTask = taskRepository.findById(taskId);

        if (operatingTask != null) {
            operatingTask.setStatus(newStatus);
            taskRepository.update(operatingTask);
        } else {
             throw new IllegalArgumentException("Could not identify the selected Task.");
        }
    }

    /**
     * Retrieves all registered tasks.
     * @return A list of all stored Task objects.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
