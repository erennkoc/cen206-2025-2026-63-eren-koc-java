package com.ucoruh.projectmanagement;

import com.ucoruh.projectmanagement.models.User;
import com.ucoruh.projectmanagement.services.ProjectService;
import com.ucoruh.projectmanagement.services.ReportService;
import com.ucoruh.projectmanagement.services.TaskService;
import com.ucoruh.projectmanagement.services.UserService;

import java.util.Scanner;

/**
 * @class Main
 * @brief Entry point for the Simple Project Management Tool.
 * @details
 *   Application flow:
 *   <ol>
 *     <li>Authentication menu – Login / Register / Guest</li>
 *     <li>Main menu – Project Setup / Task Management / Reports / Logout</li>
 *   </ol>
 *
 *   Data is persisted via serialized binary files in the "data/" directory:
 *   <ul>
 *     <li>data/users.dat    – registered users</li>
 *     <li>data/projects.dat – projects</li>
 *     <li>data/tasks.dat    – tasks</li>
 *     <li>data/reports/     – generated report text files</li>
 *   </ul>
 *
 * @author Eren Koc
 */
public class Main {

    // =========================================================================
    // Entry point
    // =========================================================================

    /**
     * @brief Starts the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        UserService    userService    = new UserService();
        ProjectService projectService = new ProjectService();
        TaskService    taskService    = new TaskService();
        ReportService  reportService  = new ReportService();

        printBanner();

        // ---- Authentication loop ----
        User currentUser = null;
        while (currentUser == null) {
            currentUser = showAuthMenu(scanner, userService);
        }

        // ---- Main menu loop ----
        boolean running = true;
        while (running) {
            showMainMenu(currentUser);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Project Setup Module
                    projectService.showMenu(scanner);
                    break;

                case "2":
                    // Task Management Module
                    taskService.showMenu(scanner, currentUser.getUsername());
                    break;

                case "3":
                    // Reporting Module
                    reportService.generateReport();
                    break;

                case "4":
                    // Logout – go back to auth menu
                    System.out.println("\n[INFO] Logging out...");
                    currentUser = null;
                    while (currentUser == null) {
                        currentUser = showAuthMenu(scanner, userService);
                    }
                    break;

                case "0":
                    // Exit
                    System.out.println("\nGoodbye! Thank you for using the Project Management Tool.");
                    running = false;
                    break;

                default:
                    System.out.println("[ERROR] Invalid option. Please enter 0–4.");
            }
        }

        scanner.close();
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * @brief Prints the application banner on startup.
     */
    private static void printBanner() {
        System.out.println("============================================");
        System.out.println("   Simple Project Management Tool v1.0");
        System.out.println("   CEN206 Object-Oriented Programming");
        System.out.println("============================================");
    }

    /**
     * @brief Displays the authentication menu and returns a logged-in User.
     *
     * @param scanner     the shared {@link Scanner}
     * @param userService the authentication service
     * @return the authenticated (or guest) {@link User}, or null on failure
     */
    private static User showAuthMenu(Scanner scanner, UserService userService) {
        System.out.println("\n============================================");
        System.out.println("            AUTHENTICATION");
        System.out.println("============================================");
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  3. Continue as Guest");
        System.out.println("  0. Exit");
        System.out.println("============================================");
        System.out.print("Select an option: ");

        String input = scanner.nextLine().trim();
        switch (input) {
            case "1":
                return userService.login(scanner);
            case "2":
                return userService.register(scanner);
            case "3":
                return userService.loginAsGuest();
            case "0":
                System.out.println("\nGoodbye!");
                System.exit(0);
                return null;  // unreachable, satisfies compiler
            default:
                System.out.println("[ERROR] Invalid option. Please try again.");
                return null;
        }
    }

    /**
     * @brief Displays the main application menu.
     *
     * @param user the currently logged-in {@link User}
     */
    private static void showMainMenu(User user) {
        System.out.println("\n============================================");
        System.out.printf("   MAIN MENU  (Logged in as: %s [%s])%n",
                user.getUsername(), user.getRole());
        System.out.println("============================================");
        System.out.println("  1. Project Setup");
        System.out.println("  2. Task Management");
        System.out.println("  3. Generate Report");
        System.out.println("  4. Logout");
        System.out.println("  0. Exit");
        System.out.println("============================================");
        System.out.print("Select an option: ");
    }
}
