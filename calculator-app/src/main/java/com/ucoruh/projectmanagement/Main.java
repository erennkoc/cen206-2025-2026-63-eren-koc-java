package com.ucoruh.projectmanagement;

import com.ucoruh.projectmanagement.models.User;
import com.ucoruh.projectmanagement.services.UserService;
import com.ucoruh.projectmanagement.services.ProjectService;
import com.ucoruh.projectmanagement.services.TaskService;
import com.ucoruh.projectmanagement.services.ReportService;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.util.Scanner;

/**
 * Main entry point for the Simple Project Management Tool.
 *
 * Flow:
 *   1. Show authentication menu (Register / Login / Guest)
 *   2. After login → show main menu
 *   3. Main menu branches into Project, Task, Progress and Report sections
 */
public class Main {

    // Shared Scanner used across all menu methods
    private static final Scanner scanner = new Scanner(System.in);

    // Service layer singletons
    private static final UserService    userService    = new UserService();
    private static final ProjectService projectService = new ProjectService();
    private static final TaskService    taskService    = new TaskService();
    private static final ReportService  reportService  = new ReportService();

    public static void main(String[] args) {
        // Make sure data directory and files exist before we start
        FileHelper.initializeDataFiles();

        System.out.println("=========================================");
        System.out.println("  Simple Project Management Tool v1.0   ");
        System.out.println("=========================================");

        // Currently logged-in user (null = guest)
        User currentUser = showAuthMenu();

        // After auth, go to the main application menu
        showMainMenu(currentUser);

        System.out.println("\nGoodbye!");
        scanner.close();
    }

    // -------------------------------------------------------
    //  AUTHENTICATION MENU
    // -------------------------------------------------------

    /**
     * Displays the authentication menu and returns the authenticated User.
     * If the user chooses Guest mode, returns null.
     */
    private static User showAuthMenu() {
        while (true) {
            System.out.println("\n--- Authentication ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Continue as Guest");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleRegister();
                    break;
                case "2":
                    User user = handleLogin();
                    if (user != null) return user;
                    break;
                case "3":
                    System.out.println("Continuing as Guest. Some features may be limited.");
                    return null;   // guest – no User object
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /** Reads credentials from console and delegates to UserService. */
    private static void handleRegister() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        boolean success = userService.register(username, password);
        if (success) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Username already exists. Try a different one.");
        }
    }

    /** Returns the logged-in User or null if credentials are wrong. */
    private static User handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = userService.login(username, password);
        if (user != null) {
            System.out.println("Welcome, " + user.getUsername() + "!");
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
        return user;
    }

    // -------------------------------------------------------
    //  MAIN MENU
    // -------------------------------------------------------

    /**
     * The primary application loop after authentication.
     * @param currentUser the logged-in user; null if guest
     */
    private static void showMainMenu(User currentUser) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= MAIN MENU =========");
            if (currentUser != null) {
                System.out.println("Logged in as: " + currentUser.getUsername());
            } else {
                System.out.println("Mode: Guest");
            }
            System.out.println("1. Project Management");
            System.out.println("2. Task Management");
            System.out.println("3. Progress Tracking");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showProjectMenu(currentUser);
                    break;
                case "2":
                    showTaskMenu(currentUser);
                    break;
                case "3":
                    showProgressMenu();
                    break;
                case "4":
                    showReportMenu();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // -------------------------------------------------------
    //  PROJECT MENU
    // -------------------------------------------------------

    private static void showProjectMenu(User currentUser) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Project Management ---");
            System.out.println("1. Create Project");
            System.out.println("2. List All Projects");
            System.out.println("3. View Project Details");
            System.out.println("4. Delete Project");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (currentUser == null) {
                        System.out.println("Please login to create a project.");
                        break;
                    }
                    System.out.print("Enter project name: ");
                    String projectName = scanner.nextLine().trim();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine().trim();
                    projectService.createProject(projectName, description, currentUser.getUsername());
                    break;
                case "2":
                    projectService.listAllProjects();
                    break;
                case "3":
                    System.out.print("Enter project ID: ");
                    String pid = scanner.nextLine().trim();
                    projectService.viewProjectDetails(pid);
                    break;
                case "4":
                    if (currentUser == null) {
                        System.out.println("Please login to delete a project.");
                        break;
                    }
                    System.out.print("Enter project ID to delete: ");
                    String delId = scanner.nextLine().trim();
                    projectService.deleteProject(delId, currentUser.getUsername());
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // -------------------------------------------------------
    //  TASK MENU
    // -------------------------------------------------------

    private static void showTaskMenu(User currentUser) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Task Management ---");
            System.out.println("1. Assign Task to Project");
            System.out.println("2. List Tasks for a Project");
            System.out.println("3. Update Task Status");
            System.out.println("4. Delete Task");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (currentUser == null) {
                        System.out.println("Please login to assign tasks.");
                        break;
                    }
                    System.out.print("Enter project ID: ");
                    String projId = scanner.nextLine().trim();
                    System.out.print("Enter task title: ");
                    String title = scanner.nextLine().trim();
                    System.out.print("Enter task description: ");
                    String desc = scanner.nextLine().trim();
                    System.out.print("Assign to (username): ");
                    String assignee = scanner.nextLine().trim();
                    taskService.assignTask(projId, title, desc, assignee);
                    break;
                case "2":
                    System.out.print("Enter project ID: ");
                    String listProjId = scanner.nextLine().trim();
                    taskService.listTasksForProject(listProjId);
                    break;
                case "3":
                    System.out.print("Enter task ID: ");
                    String taskId = scanner.nextLine().trim();
                    System.out.println("New status options: TODO | IN_PROGRESS | DONE");
                    System.out.print("Enter new status: ");
                    String status = scanner.nextLine().trim().toUpperCase();
                    taskService.updateTaskStatus(taskId, status);
                    break;
                case "4":
                    if (currentUser == null) {
                        System.out.println("Please login to delete tasks.");
                        break;
                    }
                    System.out.print("Enter task ID to delete: ");
                    String delTaskId = scanner.nextLine().trim();
                    taskService.deleteTask(delTaskId);
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // -------------------------------------------------------
    //  PROGRESS TRACKING MENU
    // -------------------------------------------------------

    private static void showProgressMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Progress Tracking ---");
            System.out.println("1. View Progress for a Project");
            System.out.println("2. View Timeline (all tasks sorted by status)");
            System.out.println("3. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter project ID: ");
                    String pid = scanner.nextLine().trim();
                    taskService.viewProgressForProject(pid);
                    break;
                case "2":
                    taskService.viewTimeline();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // -------------------------------------------------------
    //  REPORT MENU
    // -------------------------------------------------------

    private static void showReportMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Generate Project Summary Report");
            System.out.println("2. Generate Full Report (all projects)");
            System.out.println("3. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter project ID: ");
                    String pid = scanner.nextLine().trim();
                    reportService.generateProjectReport(pid);
                    break;
                case "2":
                    reportService.generateFullReport();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
