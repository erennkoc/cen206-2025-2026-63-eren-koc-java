package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.Task;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskService — manages all task-related operations.
 *
 * Responsibilities:
 *  - Assign (create) tasks within a project
 *  - List tasks for a specific project
 *  - Update a task's status (TODO → IN_PROGRESS → DONE)
 *  - Delete a task
 *  - Show progress summary for a project
 *  - Show the full timeline of all tasks sorted by status
 *
 * Teammate suggestion: One teammate can own this service and extend it
 * with features like task priority, due dates, or comments.
 */
public class TaskService {

    // -------------------------------------------------------
    //  Public API — Task CRUD
    // -------------------------------------------------------

    /**
     * Creates a new task and assigns it to a project.
     *
     * @param projectId  the ID of the parent project (must exist)
     * @param title      short task title
     * @param description what needs to be done
     * @param assignedTo username of the team member responsible
     */
    public void assignTask(String projectId, String title,
                           String description, String assignedTo) {
        if (title.isEmpty()) {
            System.out.println("[ERROR] Task title cannot be empty.");
            return;
        }

        List<Task> tasks = loadAllTasks();

        // Generate a new unique task ID
        String newId = generateNextId(tasks);
        String today = LocalDate.now().toString();

        Task task = new Task(newId, projectId, title, description,
                             assignedTo, Task.STATUS_TODO, today);
        FileHelper.appendLine(FileHelper.TASKS_FILE, task.toFileString());

        System.out.println("[SUCCESS] Task assigned:");
        printTaskDetails(task);
    }

    /**
     * Prints all tasks that belong to the given project.
     *
     * @param projectId the project ID to filter by
     */
    public void listTasksForProject(String projectId) {
        List<Task> allTasks = loadAllTasks();
        List<Task> filtered = new ArrayList<>();

        for (Task t : allTasks) {
            if (t.getProjectId().equalsIgnoreCase(projectId)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No tasks found for project: " + projectId);
            return;
        }

        System.out.println("\n--- Tasks for Project " + projectId + " ---");
        System.out.printf("%-6s %-25s %-12s %-15s %-12s%n",
                          "ID", "Title", "Status", "Assigned To", "Created");
        System.out.println("-----------------------------------------------------------------------");
        for (Task t : filtered) {
            System.out.printf("%-6s %-25s %-12s %-15s %-12s%n",
                    t.getId(), t.getTitle(), t.getStatus(),
                    t.getAssignedTo(), t.getCreatedAt());
        }
    }

    /**
     * Changes the status of the specified task.
     * Valid statuses: TODO, IN_PROGRESS, DONE
     *
     * @param taskId    the ID of the task to update
     * @param newStatus the new status string
     */
    public void updateTaskStatus(String taskId, String newStatus) {
        // Validate the status value
        if (!newStatus.equals(Task.STATUS_TODO)
                && !newStatus.equals(Task.STATUS_IN_PROGRESS)
                && !newStatus.equals(Task.STATUS_DONE)) {
            System.out.println("[ERROR] Invalid status. Use: TODO | IN_PROGRESS | DONE");
            return;
        }

        List<Task> tasks = loadAllTasks();
        boolean found = false;

        for (Task t : tasks) {
            if (t.getId().equalsIgnoreCase(taskId)) {
                t.setStatus(newStatus);
                found = true;
                System.out.println("[SUCCESS] Task " + taskId + " status → " + newStatus);
                break;
            }
        }

        if (!found) {
            System.out.println("Task not found: " + taskId);
            return;
        }

        saveAllTasks(tasks);
    }

    /**
     * Deletes the task with the given ID.
     *
     * @param taskId the ID of the task to remove
     */
    public void deleteTask(String taskId) {
        List<Task> tasks = loadAllTasks();
        List<Task> updated = new ArrayList<>();
        boolean found = false;

        for (Task t : tasks) {
            if (t.getId().equalsIgnoreCase(taskId)) {
                found = true;
                System.out.println("[INFO] Deleted task: " + t.getTitle());
                // Skip — effectively deletes it
            } else {
                updated.add(t);
            }
        }

        if (!found) {
            System.out.println("Task not found: " + taskId);
            return;
        }

        saveAllTasks(updated);
    }

    // -------------------------------------------------------
    //  Public API — Progress & Timeline
    // -------------------------------------------------------

    /**
     * Displays a progress summary for a specific project:
     *  - Total tasks
     *  - Tasks per status
     *  - Completion percentage
     *
     * @param projectId the project to summarize
     */
    public void viewProgressForProject(String projectId) {
        List<Task> tasks = loadAllTasks();
        int total = 0, todo = 0, inProgress = 0, done = 0;

        for (Task t : tasks) {
            if (!t.getProjectId().equalsIgnoreCase(projectId)) continue;
            total++;
            switch (t.getStatus()) {
                case Task.STATUS_TODO:        todo++;       break;
                case Task.STATUS_IN_PROGRESS: inProgress++; break;
                case Task.STATUS_DONE:        done++;       break;
            }
        }

        if (total == 0) {
            System.out.println("No tasks found for project: " + projectId);
            return;
        }

        int percentComplete = (int) ((done / (double) total) * 100);

        System.out.println("\n--- Progress for Project " + projectId + " ---");
        System.out.println("  Total tasks  : " + total);
        System.out.println("  TODO         : " + todo);
        System.out.println("  IN_PROGRESS  : " + inProgress);
        System.out.println("  DONE         : " + done);
        System.out.println("  Completion   : " + percentComplete + "%");
        System.out.println("  Progress bar : " + buildProgressBar(percentComplete));
    }

    /**
     * Displays all tasks across all projects sorted by status:
     *  TODO first, then IN_PROGRESS, then DONE.
     */
    public void viewTimeline() {
        List<Task> tasks = loadAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks exist in the system.");
            return;
        }

        // Separate tasks into three groups
        List<Task> todoList   = new ArrayList<>();
        List<Task> inProgList = new ArrayList<>();
        List<Task> doneList   = new ArrayList<>();

        for (Task t : tasks) {
            switch (t.getStatus()) {
                case Task.STATUS_TODO:        todoList.add(t);   break;
                case Task.STATUS_IN_PROGRESS: inProgList.add(t); break;
                case Task.STATUS_DONE:        doneList.add(t);   break;
            }
        }

        System.out.println("\n========= TASK TIMELINE =========");
        printTaskGroup("[ TODO ]",        todoList);
        printTaskGroup("[ IN PROGRESS ]", inProgList);
        printTaskGroup("[ DONE ]",        doneList);
    }

    // -------------------------------------------------------
    //  Private helpers
    // -------------------------------------------------------

    /**
     * Loads all tasks from the data file.
     * @return list of Task objects; empty list if none
     */
    public List<Task> loadAllTasks() {
        List<String> lines = FileHelper.readAllLines(FileHelper.TASKS_FILE);
        List<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            Task t = Task.fromFileString(line);
            if (t != null) tasks.add(t);
        }
        return tasks;
    }

    /**
     * Overwrites the tasks file with the supplied list.
     * @param tasks the complete, updated list of tasks
     */
    private void saveAllTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toFileString());
        }
        FileHelper.writeAllLines(FileHelper.TASKS_FILE, lines);
    }

    /**
     * Generates the next sequential task ID.
     * Format: T + 3-digit zero-padded number (T001, T002, …)
     */
    private String generateNextId(List<Task> existingTasks) {
        int maxNum = 0;
        for (Task t : existingTasks) {
            try {
                int num = Integer.parseInt(t.getId().substring(1));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException ignored) { /* skip malformed IDs */ }
        }
        return String.format("T%03d", maxNum + 1);
    }

    /**
     * Prints a labelled group of tasks (used in timeline display).
     */
    private void printTaskGroup(String label, List<Task> tasks) {
        System.out.println("\n" + label);
        if (tasks.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        for (Task t : tasks) {
            System.out.printf("  %-6s | Project: %-6s | %-25s | Assigned: %s | %s%n",
                    t.getId(), t.getProjectId(), t.getTitle(),
                    t.getAssignedTo(), t.getCreatedAt());
        }
    }

    /** Pretty-prints a single task's details. */
    private void printTaskDetails(Task t) {
        System.out.println("  ID          : " + t.getId());
        System.out.println("  Project     : " + t.getProjectId());
        System.out.println("  Title       : " + t.getTitle());
        System.out.println("  Description : " + t.getDescription());
        System.out.println("  Assigned To : " + t.getAssignedTo());
        System.out.println("  Status      : " + t.getStatus());
        System.out.println("  Created     : " + t.getCreatedAt());
    }

    /**
     * Builds a simple ASCII progress bar.
     *
     * @param percent 0-100
     * @return e.g. "[=========>          ] 45%"
     */
    private String buildProgressBar(int percent) {
        int bars = percent / 5;   // 20 characters wide
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 20; i++) {
            if (i < bars)       sb.append("=");
            else if (i == bars) sb.append(">");
            else                sb.append(" ");
        }
        sb.append("] ").append(percent).append("%");
        return sb.toString();
    }
}
