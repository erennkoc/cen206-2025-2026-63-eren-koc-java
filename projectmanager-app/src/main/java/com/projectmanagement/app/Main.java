package com.projectmanagement.app;

import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.services.ProjectService;
import com.projectmanagement.lib.services.TaskService;
import com.projectmanagement.lib.services.UserService;
import com.projectmanagement.lib.storage.RepositoryFactory;
import com.projectmanagement.lib.storage.StorageConfig;
import com.projectmanagement.lib.storage.StorageType;
import java.util.Scanner;
import java.util.UUID;
/**
 * @class Main
 * @brief Main entry point for the Project Management Console UI.
 * @details Implements a standard console-based interface using Scanner.
 */
public class Main {

    private static StorageConfig storageConfig = new StorageConfig(StorageType.BINARY_FILE);
    private static RepositoryFactory repoFactory = new RepositoryFactory(storageConfig);
    private static UserService userService = new UserService(repoFactory.getUserRepository());
    private static ProjectService projectService = new ProjectService(repoFactory.getProjectRepository());
    private static TaskService taskService = new TaskService(repoFactory.getTaskRepository());
    
    private static User loggedInUser = null;
    private static boolean isGuest = false;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean appRunning = true;
        while (appRunning) {
            if (loggedInUser == null && !isGuest) {
                appRunning = renderAuthMenu();
            } else {
                appRunning = renderMainMenu();
            }
        }
        System.out.println("Exiting application. Goodbye!");
        scanner.close();
    }

    private static void reloadServices() {
        repoFactory = new RepositoryFactory(storageConfig);
        userService = new UserService(repoFactory.getUserRepository());
        projectService = new ProjectService(repoFactory.getProjectRepository());
        taskService = new TaskService(repoFactory.getTaskRepository());
    }

    private static String askInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static String askPassword(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int interactiveMenu(String title, String[] options) {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("   " + title);
            System.out.println("=====================================");
            
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            
            System.out.print("\nPlease select an option (1-" + options.length + "): ");
            String input = scanner.nextLine().trim();
            
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= options.length) {
                    return choice - 1; // 0-based index
                } else {
                    System.out.println("Invalid input! Please enter a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid character! Please enter numbers only.");
            }
        }
    }

    private static boolean renderAuthMenu() {
        String[] options = {
            "Login",
            "Register",
            "Guest Mode",
            "Change Storage Backend [" + storageConfig.getActiveStorageType().name() + "]",
            "Exit Application"
        };
        int selection = interactiveMenu("USER AUTHENTICATION", options);
        switch (selection) {
            case 0:
                handleLogin();
                break;
            case 1:
                handleRegister();
                break;
            case 2:
                isGuest = true;
                loggedInUser = null;
                System.out.println("Guest Mode activated.");
                pause();
                break;
            case 3:
                handleStorageSwap();
                break;
            case 4:
                return false;
        }
        return true;
    }

    private static boolean renderMainMenu() {
        String status = "Not Logged In";
        if (loggedInUser != null) status = "Logged in as: " + loggedInUser.getUsername();
        else if (isGuest) status = "Guest Mode Active";

        String[] menuOptions = {
            "Project Setup",
            "Task Assignment",
            "Progress Tracking",
            "Reporting",
            "Logout",
            "Exit Application"
        };
        int selection = interactiveMenu("MAIN MENU | " + status, menuOptions);
        switch (selection) {
            case 0:
                handleProjectSetup();
                break;
            case 1:
                handleTaskAssignment();
                break;
            case 2:
                 handleProgressTracking();
                 break;
            case 3:
                 handleReporting();
                 break;
            case 4:
                loggedInUser = null;
                isGuest = false;
                System.out.println("Logged out successfully.");
                pause();
                break;
            case 5:
                return false; 
        }
        return true;
    }

    private static void handleLogin() {
        System.out.println("--- User Login ---");
        String username = askInput("Username: ");
        String password = askPassword("Password: ");
        
        User user = userService.login(username, password);
        if (user != null) {
            loggedInUser = user;
            isGuest = false;
            System.out.println("Login Successful! Welcome, " + user.getUsername() + ".");
        } else {
            System.out.println("Invalid username or password!");
        }
        pause();
    }

    private static void handleRegister() {
        System.out.println("--- New User Registration ---");
        String uname = askInput("Username: ");
        String email = askInput("Email: ");
        String password = askPassword("Password: ");

        try {
            userService.registerUser(UUID.randomUUID().toString(), uname, email, password);
            System.out.println("Registration successful! You can now login.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
        pause();
    }

    private static void handleStorageSwap() {
        String[] storageChoices = {"BINARY_FILE", "SQLITE", "MYSQL", "Cancel"};
        int choice = interactiveMenu("STORAGE BACKEND SELECTION", storageChoices);

        if (choice < 3) {
            StorageType newType = StorageType.values()[choice];
            storageConfig.setActiveStorageType(newType);
            reloadServices();
            System.out.println("Storage backend changed successfully: " + newType.name());
            pause();
        }
    }

    private static void handleProjectSetup() {
        boolean back = false;
        while (!back) {
            String[] options = {
                "Create Project",
                "Manage Projects",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("PROJECT SETUP", options);
            switch (selection) {
                case 0:
                    System.out.println("Action: [Create Project]..."); pause();
                    break;
                case 1:
                    System.out.println("Action: [Manage Projects]..."); pause();
                    break;
                case 2:
                    back = true;
                    break;
            }
        }
    }

    private static void handleTaskAssignment() {
        boolean back = false;
        while (!back) {
            String[] options = {
                "Assign Tasks",
                "Manage Tasks",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("TASK ASSIGNMENT", options);
            switch (selection) {
                case 0:
                    System.out.println("Action: [Assign Tasks]..."); pause();
                    break;
                case 1:
                    System.out.println("Action: [Manage Tasks]..."); pause();
                    break;
                case 2:
                    back = true;
                    break;
            }
        }
    }

    private static void handleProgressTracking() {
        boolean back = false;
        while (!back) {
            String[] options = {
                "Update Task Status",
                "View Progress Timeline",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("PROGRESS TRACKING", options);
            switch (selection) {
                case 0:
                    System.out.println("Action: [Update Task Status]..."); pause();
                    break;
                case 1:
                    System.out.println("Action: [View Progress Timeline]..."); pause();
                    break;
                case 2:
                    back = true;
                    break;
            }
        }
    }

    private static void handleReporting() {
        boolean back = false;
        while (!back) {
            String[] options = {
                "Generate Project Reports",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("REPORTING", options);
            switch (selection) {
                case 0:
                    System.out.println("Action: [Generate Project Reports]..."); pause();
                    break;
                case 1:
                    back = true;
                    break;
            }
        }
    }

    private static void pause() {
        System.out.println("\nPress [ENTER] to continue...");
        scanner.nextLine();
    }
}
