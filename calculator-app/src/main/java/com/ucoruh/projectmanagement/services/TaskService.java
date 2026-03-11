package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.Task;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @class TaskService
 * @brief Manages CRUD operations and status updates for {@link Task} objects.
 * @details Tasks are persisted in "data/tasks.dat" as a serialized Java List.
 *          Supports assigning tasks to users, updating task status, and
 *          viewing tasks filtered by project or assignee.
 */
public class TaskService {

    /** Path to the serialized tasks data file. */
    private static final String FILE_PATH = "data/tasks.dat";

    // =========================================================================
    // Menu entry point
    // =========================================================================

    /**
     * @brief Displays the Task Management sub-menu and delegates to the
     *        appropriate method based on user input.
     *
     * @param scanner  the shared {@link Scanner} for console I/O
     * @param username the logged-in user's username (used for assignment)
     */
    public void showMenu(Scanner scanner, String username) {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("        TASK MANAGEMENT MENU");
            System.out.println("========================================");
            System.out.println("  1. Assign New Task");
            System.out.println("  2. View All Tasks");
            System.out.println("  3. View My Tasks");
            System.out.println("  4. Update Task Status");
            System.out.println("  5. Delete Task");
            System.out.println("  0. Back to Main Menu");
            System.out.println("========================================");
            System.out.print("Select an option: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    assignTask(scanner);
                    break;
                case "2":
                    listAllTasks();
                    break;
                case "3":
                    listMyTasks(username);
                    break;
                case "4":
                    updateTaskStatus(scanner);
                    break;
                case "5":
                    deleteTask(scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid option. Please enter 0–5.");
            }
        }
    }

    // =========================================================================
    // Task Operations
    // =========================================================================

    /**
     * @brief Prompts for task details and saves the new task.
     *
     * @param scanner the shared {@link Scanner}
     */
    public void assignTask(Scanner scanner) {
        System.out.println("\n--- Assign New Task ---");

        System.out.print("Task Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[ERROR] Task name cannot be empty.");
            return;
        }

        System.out.print("Description (press Enter to skip): ");
        String desc = scanner.nextLine().trim();

        System.out.print("Assign To (username): ");
        String assignedTo = scanner.nextLine().trim();

        System.out.print("Project ID (enter 0 if not linked): ");
        int projectId = readInt(scanner);

        List<Task> tasks = FileHelper.loadList(FILE_PATH);

        int newId = 1;
        for (Task t : tasks) {
            if (t.getTaskId() >= newId) newId = t.getTaskId() + 1;
        }

        Task task = new Task(newId, name, desc, assignedTo, "PENDING", projectId);
        tasks.add(task);
        FileHelper.saveList(FILE_PATH, tasks);

        System.out.println("[OK] Task assigned successfully! Task ID: " + newId);
    }

    /**
     * @brief Lists all tasks in the system.
     */
    public void listAllTasks() {
        System.out.println("\n--- All Tasks ---");
        List<Task> tasks = FileHelper.loadList(FILE_PATH);
        if (tasks.isEmpty()) {
            System.out.println("  (No tasks found.)");
            return;
        }
        printTasks(tasks);
    }

    /**
     * @brief Lists tasks assigned to a specific user.
     *
     * @param username the username to filter by
     */
    public void listMyTasks(String username) {
        System.out.println("\n--- Tasks Assigned to: " + username + " ---");
        List<Task> all = FileHelper.loadList(FILE_PATH);
        List<Task> mine = new ArrayList<>();
        for (Task t : all) {
            if (t.getAssignedTo().equalsIgnoreCase(username)) {
                mine.add(t);
            }
        }
        if (mine.isEmpty()) {
            System.out.println("  (No tasks assigned to you.)");
            return;
        }
        printTasks(mine);
    }

    /**
     * @brief Updates the status of a task selected by ID.
     *
     * @param scanner the shared {@link Scanner}
     */
    public void updateTaskStatus(Scanner scanner) {
        System.out.println("\n--- Update Task Status ---");
        List<Task> tasks = FileHelper.loadList(FILE_PATH);
        if (tasks.isEmpty()) {
            System.out.println("  (No tasks available.)");
            return;
        }
        printTasks(tasks);

        System.out.print("Enter Task ID to update: ");
        int id = readInt(scanner);

        Task target = findById(tasks, id);
        if (target == null) {
            System.out.println("[ERROR] No task found with ID " + id + ".");
            return;
        }

        System.out.println("Current status: " + target.getStatus());
        System.out.println("Available statuses: PENDING, IN_PROGRESS, DONE");
        System.out.print("New Status: ");
        String status = scanner.nextLine().trim().toUpperCase();

        if (!status.equals("PENDING") && !status.equals("IN_PROGRESS") && !status.equals("DONE")) {
            System.out.println("[ERROR] Invalid status. Use PENDING, IN_PROGRESS, or DONE.");
            return;
        }

        target.setStatus(status);
        FileHelper.saveList(FILE_PATH, tasks);
        System.out.println("[OK] Task status updated to: " + status);
    }

    /**
     * @brief Deletes a task selected by ID.
     *
     * @param scanner the shared {@link Scanner}
     */
    public void deleteTask(Scanner scanner) {
        System.out.println("\n--- Delete Task ---");
        List<Task> tasks = FileHelper.loadList(FILE_PATH);
        if (tasks.isEmpty()) {
            System.out.println("  (No tasks to delete.)");
            return;
        }
        printTasks(tasks);

        System.out.print("Enter Task ID to delete: ");
        int id = readInt(scanner);

        Task target = findById(tasks, id);
        if (target == null) {
            System.out.println("[ERROR] No task found with ID " + id + ".");
            return;
        }

        tasks.remove(target);
        FileHelper.saveList(FILE_PATH, tasks);
        System.out.println("[OK] Task deleted successfully.");
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private void printTasks(List<Task> tasks) {
        for (Task t : tasks) {
            System.out.println("----------------------------------------");
            System.out.printf("  Task ID    : %d%n", t.getTaskId());
            System.out.printf("  Name       : %s%n", t.getTaskName());
            System.out.printf("  Description: %s%n", t.getDescription());
            System.out.printf("  Assigned To: %s%n", t.getAssignedTo());
            System.out.printf("  Status     : %s%n", t.getStatus());
            System.out.printf("  Project ID : %d%n", t.getProjectId());
        }
        System.out.println("----------------------------------------");
    }

    private Task findById(List<Task> tasks, int id) {
        for (Task t : tasks) {
            if (t.getTaskId() == id) return t;
        }
        return null;
    }

    private int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
