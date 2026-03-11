package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.Project;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.util.List;
import java.util.Scanner;

/**
 * @class ProjectService
 * @brief Manages CRUD operations for {@link Project} objects.
 * @details Projects are persisted in "data/projects.dat" as a serialized Java
 *          List. The class exposes a console-driven menu that integrates directly
 *          with the main application loop via a shared {@link Scanner}.
 *
 *          <p>Responsibilities:
 *          <ul>
 *            <li>Create a new project (with auto-incremented ID)</li>
 *            <li>List all existing projects</li>
 *            <li>Update an existing project's fields</li>
 *            <li>Delete a project by ID</li>
 *          </ul>
 */
public class ProjectService {

    /** Path to the serialized projects data file. */
    private static final String FILE_PATH = "data/projects.dat";

    // =========================================================================
    // Menu entry point
    // =========================================================================

    /**
     * @brief Displays the Project Management sub-menu and delegates to the
     *        appropriate CRUD method based on user input.
     *
     * @param scanner the shared {@link Scanner} for console I/O
     */
    public void showMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("       PROJECT MANAGEMENT MENU");
            System.out.println("========================================");
            System.out.println("  1. Create Project");
            System.out.println("  2. View All Projects");
            System.out.println("  3. Update Project");
            System.out.println("  4. Delete Project");
            System.out.println("  0. Back to Main Menu");
            System.out.println("========================================");
            System.out.print("Select an option: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    createProject(scanner);
                    break;
                case "2":
                    listProjects();
                    break;
                case "3":
                    updateProject(scanner);
                    break;
                case "4":
                    deleteProject(scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid option. Please enter 0–4.");
            }
        }
    }

    // =========================================================================
    // CRUD Operations
    // =========================================================================

    /**
     * @brief Prompts the user for project details and saves the new project.
     * @details The project ID is automatically assigned as max(existing IDs) + 1.
     *
     * @param scanner the shared {@link Scanner} for console I/O
     */
    public void createProject(Scanner scanner) {
        System.out.println("\n--- Create New Project ---");

        System.out.print("Project Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[ERROR] Project name cannot be empty.");
            return;
        }

        System.out.print("Description (press Enter to skip): ");
        String description = scanner.nextLine().trim();

        System.out.print("Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine().trim();

        System.out.print("End Date   (YYYY-MM-DD): ");
        String endDate = scanner.nextLine().trim();

        List<Project> projects = FileHelper.loadList(FILE_PATH);

        // Auto-increment ID
        int newId = 1;
        for (Project p : projects) {
            if (p.getProjectId() >= newId) {
                newId = p.getProjectId() + 1;
            }
        }

        Project project = new Project(newId, name, description, startDate, endDate);
        projects.add(project);
        FileHelper.saveList(FILE_PATH, projects);

        System.out.println("[OK] Project created successfully! Assigned ID: " + newId);
    }

    /**
     * @brief Prints all stored projects to the console.
     * @details If no projects exist, a friendly message is shown instead.
     */
    public void listProjects() {
        System.out.println("\n--- All Projects ---");
        List<Project> projects = FileHelper.loadList(FILE_PATH);

        if (projects.isEmpty()) {
            System.out.println("  (No projects found. Create one first.)");
            return;
        }

        for (Project p : projects) {
            System.out.println("----------------------------------------");
            System.out.println(p.toDisplayString());
        }
        System.out.println("----------------------------------------");
        System.out.println("Total: " + projects.size() + " project(s).");
    }

    /**
     * @brief Prompts the user to select a project by ID and update its fields.
     * @details Pressing Enter on any field keeps the existing value unchanged.
     *
     * @param scanner the shared {@link Scanner} for console I/O
     */
    public void updateProject(Scanner scanner) {
        System.out.println("\n--- Update Project ---");
        List<Project> projects = FileHelper.loadList(FILE_PATH);

        if (projects.isEmpty()) {
            System.out.println("  (No projects to update.)");
            return;
        }

        // Show summary list first
        for (Project p : projects) {
            System.out.printf("  [%d] %s%n", p.getProjectId(), p.getProjectName());
        }

        System.out.print("Enter Project ID to update: ");
        int id = readInt(scanner);

        Project target = findById(projects, id);
        if (target == null) {
            System.out.println("[ERROR] No project found with ID " + id + ".");
            return;
        }

        System.out.println("Editing: " + target.getProjectName());
        System.out.println("(Press Enter to keep the existing value.)");

        System.out.printf("New Name [%s]: ", target.getProjectName());
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) target.setProjectName(name);

        System.out.printf("New Description [%s]: ", target.getDescription());
        String desc = scanner.nextLine().trim();
        if (!desc.isEmpty()) target.setDescription(desc);

        System.out.printf("New Start Date [%s]: ", target.getStartDate());
        String start = scanner.nextLine().trim();
        if (!start.isEmpty()) target.setStartDate(start);

        System.out.printf("New End Date [%s]: ", target.getEndDate());
        String end = scanner.nextLine().trim();
        if (!end.isEmpty()) target.setEndDate(end);

        FileHelper.saveList(FILE_PATH, projects);
        System.out.println("[OK] Project updated successfully.");
    }

    /**
     * @brief Prompts the user to select a project by ID and removes it.
     *
     * @param scanner the shared {@link Scanner} for console I/O
     */
    public void deleteProject(Scanner scanner) {
        System.out.println("\n--- Delete Project ---");
        List<Project> projects = FileHelper.loadList(FILE_PATH);

        if (projects.isEmpty()) {
            System.out.println("  (No projects to delete.)");
            return;
        }

        for (Project p : projects) {
            System.out.printf("  [%d] %s%n", p.getProjectId(), p.getProjectName());
        }

        System.out.print("Enter Project ID to delete: ");
        int id = readInt(scanner);

        Project target = findById(projects, id);
        if (target == null) {
            System.out.println("[ERROR] No project found with ID " + id + ".");
            return;
        }

        System.out.printf("Are you sure you want to delete '%s'? (yes/no): ",
                target.getProjectName());
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            projects.remove(target);
            FileHelper.saveList(FILE_PATH, projects);
            System.out.println("[OK] Project deleted successfully.");
        } else {
            System.out.println("[INFO] Deletion cancelled.");
        }
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * @brief Finds a project by its ID in the given list.
     *
     * @param projects the list to search
     * @param id       the target project ID
     * @return the matching {@link Project}, or null if not found
     */
    private Project findById(List<Project> projects, int id) {
        for (Project p : projects) {
            if (p.getProjectId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * @brief Reads a single integer from the scanner, consuming the rest of
     *        the line. Returns -1 if the input is not a valid integer.
     *
     * @param scanner the shared {@link Scanner}
     * @return parsed integer or -1 on parse error
     */
    private int readInt(Scanner scanner) {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
