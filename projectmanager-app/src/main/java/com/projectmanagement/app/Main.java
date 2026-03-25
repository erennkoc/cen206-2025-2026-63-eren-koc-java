package com.projectmanagement.app;

import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.services.ProjectService;
import com.projectmanagement.lib.services.TaskService;
import com.projectmanagement.lib.services.UserService;
import com.projectmanagement.lib.storage.RepositoryFactory;
import com.projectmanagement.lib.storage.StorageConfig;
import com.projectmanagement.lib.storage.StorageType;
import com.projectmanagement.lib.services.ReportService;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;

import java.util.Scanner;
import java.util.UUID;
/**
 * Main entry point for the Project Management Console App UI.
 * <p> Implements a standard console-based interface using Scanner.
 */
public class Main {

    /** Configuration component specifying current storage details (MySQL, SQLite, etc.). */
    private static StorageConfig storageConfig = new StorageConfig(StorageType.BINARY_FILE);
    
    /** Factory handling the abstraction mechanism for multiple database interfaces. */
    private static RepositoryFactory repoFactory = new RepositoryFactory(storageConfig);
    
    /** System service for dispatching user validations, registrations and queries. */
    private static UserService userService = new UserService(repoFactory.getUserRepository());
    
    /** Core service to construct and retrieve overarching Project structures. */
    private static ProjectService projectService = new ProjectService(repoFactory.getProjectRepository());
    
    /** Auxiliary task service to assign tasks, mutate statuses and extract dependencies. */
    private static TaskService taskService = new TaskService(repoFactory.getTaskRepository());
    
    /** Analytical aggregation service exporting metrics and text-based reports for end-users. */
    private static ReportService reportService = new ReportService(projectService, taskService, userService);
    
    /** Stores the memory instance of the presently authenticated user. */
    private static User loggedInUser = null;
    
    /** Flags whether the application sequence runs under limited Guest capacities. */
    private static boolean isGuest = false;
    
    /** Static reader attached to the standard application input sequence. */
    private static Scanner scanner;
    
    /** System terminal reference employed for raw OS terminal interfacing when present. */
    private static Terminal terminal;

    /**
     * Bootstrapping function serving as application lifecycle container.
     * Starts Terminal integrations and enters the eternal authentication/main-menu loop.
     * @param args Standalone command-line runtime instruction variables.
     */
    public static void main(String[] args) {
        // Initialize scanner freshly. Allows unit tests to intercept System.in cleanly.
        scanner = new Scanner(System.in);
        
        try {
            terminal = TerminalBuilder.builder().jna(true).system(true).build();
        } catch (Exception e) {
            terminal = null;
        }

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
        reportService = new ReportService(projectService, taskService, userService);
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
        if (terminal == null || terminal.getType().equals(Terminal.TYPE_DUMB) || terminal.getType().equals(Terminal.TYPE_DUMB_COLOR)) {
            return fallbackInteractiveMenu(title, options);
        }
        
        try {
            Attributes prevAttrs = terminal.enterRawMode();
            NonBlockingReader reader = terminal.reader();
            
            int selectedIndex = 0;
            while (true) {
                terminal.puts(Capability.clear_screen);
                terminal.writer().println("=====================================\r");
                terminal.writer().println("   " + title + "\r");
                terminal.writer().println("=====================================\r");
                terminal.writer().println("Use Arrow Keys or [W=Up, S=Down]. Enter to select.\r");
                terminal.writer().println("\r");
                
                for (int i = 0; i < options.length; i++) {
                    if (i == selectedIndex) {
                        terminal.writer().println(" -> " + options[i] + "\r");
                    } else {
                        terminal.writer().println("    " + options[i] + "\r");
                    }
                }
                terminal.writer().flush();
                
                int c = reader.read();
                if (c == 27) { // ESC sequence
                    int next = reader.read(50L); 
                    if (next == 91) { // [
                        int arrow = reader.read(50L);
                        if (arrow == 65) { // Up
                            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
                        } else if (arrow == 66) { // Down
                            selectedIndex = (selectedIndex + 1) % options.length;
                        }
                    }
                } else if (c == 'w' || c == 'W') {
                    selectedIndex = (selectedIndex - 1 + options.length) % options.length;
                } else if (c == 's' || c == 'S') {
                    selectedIndex = (selectedIndex + 1) % options.length;
                } else if (c == 13 || c == 10) { // Enter
                    terminal.setAttributes(prevAttrs);
                    // clear screen before exiting so the next output is clean
                    terminal.puts(Capability.clear_screen);
                    terminal.writer().flush();
                    return selectedIndex;
                }
            }
        } catch (Exception e) {
            return fallbackInteractiveMenu(title, options);
        }
    }

    private static int fallbackInteractiveMenu(String title, String[] options) {
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
                "View All Projects",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("PROJECT SETUP", options);
            switch (selection) {
                case 0:
                    System.out.println("\n--- Create New Project ---");
                    String pName = askInput("Project Name: ");
                    String pDesc = askInput("Project Description: ");
                    try {
                        projectService.createProject(UUID.randomUUID().toString(), pName, pDesc);
                        System.out.println("Project created successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    pause();
                    break;
                case 1:
                    System.out.println("\n--- All Projects ---");
                    java.util.List<com.projectmanagement.lib.models.Project> projects = projectService.getAllProjects();
                    if (projects == null || projects.isEmpty()) {
                        System.out.println("No projects found.");
                    } else {
                        for (com.projectmanagement.lib.models.Project p : projects) {
                            p.displayDetails();
                            System.out.println("-");
                        }
                    }
                    pause();
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
                "Create New Task & Add to Project",
                "Assign User to Task",
                "View All Tasks",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("TASK MANAGEMENT", options);
            switch (selection) {
                case 0:
                    System.out.println("\n--- Create Task ---");
                    java.util.List<com.projectmanagement.lib.models.Project> projs = projectService.getAllProjects();
                    if (projs == null || projs.isEmpty()) {
                        System.out.println("Please create a Project first!");
                        pause();
                        break;
                    }
                    System.out.println("Available Projects:");
                    for (int i = 0; i < projs.size(); i++) {
                        System.out.println((i+1) + ". " + projs.get(i).getName());
                    }
                    String pIndexStr = askInput("Select Project Number: ");
                    try {
                        int idx = Integer.parseInt(pIndexStr) - 1;
                        if (idx >= 0 && idx < projs.size()) {
                            String tTitle = askInput("Task Title: ");
                            String tDesc = askInput("Task Description: ");
                            com.projectmanagement.lib.models.Task newTask = new com.projectmanagement.lib.models.Task(UUID.randomUUID().toString(), tTitle, tDesc);
                            taskService.registerTask(newTask.getId(), tTitle, tDesc);
                            projectService.addTaskToProject(projs.get(idx).getId(), newTask);
                            System.out.println("Task created and added to Project.");
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    pause();
                    break;
                case 1:
                    System.out.println("\n--- Assign User to Task ---");
                    java.util.List<com.projectmanagement.lib.models.Task> allTasks = taskService.getAllTasks();
                    java.util.List<User> allUsers = userService.getAllUsers();
                    if (allTasks == null || allTasks.isEmpty() || allUsers == null || allUsers.isEmpty()) {
                        System.out.println("You need both Tasks and Users in the system to assign them.");
                        pause();
                        break;
                    }
                    System.out.println("Tasks:");
                    for (int i=0; i<allTasks.size(); i++) {
                        System.out.println((i+1) + ". " + allTasks.get(i).getTitle() + " (Status: " + allTasks.get(i).getStatus() + ")");
                    }
                    String tIdxStr = askInput("Select Task Number: ");
                    
                    System.out.println("Users:");
                    for (int i=0; i<allUsers.size(); i++) {
                        System.out.println((i+1) + ". " + allUsers.get(i).getUsername());
                    }
                    String uIdxStr = askInput("Select User Number: ");
                    
                    try {
                        int currentTaskIndex = Integer.parseInt(tIdxStr) - 1;
                        int currentUserIndex = Integer.parseInt(uIdxStr) - 1;
                        taskService.assignTaskToUser(allTasks.get(currentTaskIndex).getId(), allUsers.get(currentUserIndex));
                        System.out.println("Task successfully assigned.");
                    } catch (Exception e) {
                        System.out.println("Error assigning task. Check inputs.");
                    }
                    pause();
                    break;
                case 2:
                    System.out.println("\n--- All Tasks ---");
                    java.util.List<com.projectmanagement.lib.models.Task> tasks = taskService.getAllTasks();
                    if (tasks == null || tasks.isEmpty()) {
                        System.out.println("No tasks found.");
                    } else {
                        for (com.projectmanagement.lib.models.Task t : tasks) {
                            t.displayDetails();
                            System.out.println("-");
                        }
                    }
                    pause();
                    break;
                case 3:
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
                "Back to Main Menu"
            };
            int selection = interactiveMenu("PROGRESS TRACKING", options);
            switch (selection) {
                case 0:
                    System.out.println("\n--- Update Task Status ---");
                    java.util.List<com.projectmanagement.lib.models.Task> tasks = taskService.getAllTasks();
                    if(tasks == null || tasks.isEmpty()) {
                        System.out.println("No tasks available.");
                        pause();
                        break;
                    }
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i+1) + ". " + tasks.get(i).getTitle() + " [" + tasks.get(i).getStatus() + "]");
                    }
                    String tIdxStr = askInput("Select Task Number: ");
                    
                    System.out.println("1. TODO\n2. IN_PROGRESS\n3. DONE");
                    String sIdxStr = askInput("Select New Status (1-3): ");
                    
                    try {
                        int tIdx = Integer.parseInt(tIdxStr) - 1;
                        int sIdx = Integer.parseInt(sIdxStr);
                        com.projectmanagement.lib.models.TaskStatus newStatus = com.projectmanagement.lib.models.TaskStatus.TODO;
                        if(sIdx == 2) newStatus = com.projectmanagement.lib.models.TaskStatus.IN_PROGRESS;
                        if(sIdx == 3) newStatus = com.projectmanagement.lib.models.TaskStatus.DONE;
                        
                        taskService.updateTaskStatus(tasks.get(tIdx).getId(), newStatus);
                        System.out.println("Task status updated to " + newStatus.name());
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                    pause();
                    break;
                case 1:
                    back = true;
                    break;
            }
        }
    }

    private static void handleReporting() {
        boolean back = false;
        while (!back) {
            String[] options = {
                "Generate General System Report",
                "Generate Task Status Report",
                "Back to Main Menu"
            };
            int selection = interactiveMenu("REPORTING", options);
            switch (selection) {
                case 0:
                    reportService.generateGeneralSystemReport();
                    pause();
                    break;
                case 1:
                    reportService.generateTaskStatusReport();
                    pause();
                    break;
                case 2:
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
