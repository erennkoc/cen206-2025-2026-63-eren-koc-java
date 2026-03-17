package com.projectmanagement.app;

import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.services.ProjectService;
import com.projectmanagement.lib.services.TaskService;
import com.projectmanagement.lib.services.UserService;
import com.projectmanagement.lib.storage.RepositoryFactory;
import com.projectmanagement.lib.storage.StorageConfig;
import com.projectmanagement.lib.storage.StorageType;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.UUID;

/**
 * @class Main
 * @brief Main entry point for the Project Management Console UI.
 * @details Implements an interactive JLine 3 terminal interface leveraging raw terminal interaction to process arrow-key navigation, bypassing the java.util.Scanner entirely. Coordinates User Authentication, Service dispatching, and contextual routing like the Dashboard and storage swaps.
 */
public class Main {

    /**
     * @brief Context setting to orient storage location.
     */
    private static StorageConfig storageConfig = new StorageConfig(StorageType.BINARY_FILE);
    
    /**
     * @brief Persistent Factory dictating current repository generation.
     */
    private static RepositoryFactory repoFactory = new RepositoryFactory(storageConfig);
    
    /**
     * @brief Dedicated service governing Users.
     */
    private static UserService userService = new UserService(repoFactory.getUserRepository());
    
    /**
     * @brief Dedicated service governing Projects.
     */
    private static ProjectService projectService = new ProjectService(repoFactory.getProjectRepository());
    
    /**
     * @brief Dedicated service governing Tasks.
     */
    private static TaskService taskService = new TaskService(repoFactory.getTaskRepository());
    
    /**
     * @brief Currently authenticated user token/session.
     */
    private static User loggedInUser = null;
    
    /**
     * @brief Native JLine 3 terminal instance interface.
     */
    private static Terminal terminal;

    /**
     * @brief Primary execution bootstrap method.
     * @param args Array of CLI arguments (ignored).
     */
    public static void main(String[] args) {
        try {
            // Build the JLine terminal capturing system-level integrations
            terminal = TerminalBuilder.builder().system(true).build();
            
            boolean appRunning = true;
            while (appRunning) {
                if (loggedInUser == null) {
                    appRunning = renderMainMenu();
                } else {
                    appRunning = renderDashboard();
                }
            }
            
            terminal.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Reinitializes logical services according to the current StorageConfig enum.
     */
    private static void reloadServices() {
        repoFactory = new RepositoryFactory(storageConfig);
        userService = new UserService(repoFactory.getUserRepository());
        projectService = new ProjectService(repoFactory.getProjectRepository());
        taskService = new TaskService(repoFactory.getTaskRepository());
    }

    /**
     * @brief Handles user-facing input queries reliably through JLine's LineReader.
     * @param prompt Printed query asking the user for input.
     * @return Captured user response payload.
     */
    private static String askInput(String prompt) {
        LineReader lr = LineReaderBuilder.builder().terminal(terminal).build();
        return lr.readLine(prompt);
    }
    
    /**
     * @brief Handles hidden password typing logic securely.
     * @param prompt The query text (e.g., "Password: ").
     * @return Captured plaintext output representing the user's secret key intent.
     */
    private static String askPassword(String prompt) {
        LineReader lr = LineReaderBuilder.builder().terminal(terminal).build();
        return lr.readLine(prompt, '*');
    }

    /**
     * @brief Evaluates an interactive menu leveraging hardware interrupts (Keys: Arrow Up, Arrow Down, Enter).
     * @param title Display string situated at the uppermost margin of the menu overlay.
     * @param options Selectable menu list elements.
     * @return Selected array index.
     * @throws IOException For disrupted streams natively inside JLine raw evaluation.
     */
    private static int interactiveMenu(String title, String[] options) throws IOException {
        int index = 0;
        NonBlockingReader reader = terminal.reader();
        Attributes ogAttributes = terminal.enterRawMode(); // Raw IO Capture disables standard buffering

        try {
            while (true) {
                // Wipe terminal trace gracefully
                terminal.puts(Capability.clear_screen);
                terminal.writer().println("=====================================");
                terminal.writer().println("   " + title);
                terminal.writer().println("=====================================\n");
                
                // Print all options indicating the chosen index
                for (int i = 0; i < options.length; i++) {
                    if (i == index) {
                        terminal.writer().println("  => [ " + options[i] + " ]");
                    } else {
                        terminal.writer().println("     " + options[i]);
                    }
                }
                
                terminal.writer().println("\n(Use UP/DOWN Arrows to move, ENTER to select)");
                terminal.writer().flush();

                // Intercept raw key signals
                int c = reader.read();
                if (c == 27) { // ANSI escape sequence detected (\033)
                    int next1 = reader.read();
                    int next2 = reader.read();
                    if (next1 == 91) { // Sequence bracket [
                        if (next2 == 65) {      // Up Arrow \033[A
                            index = (index - 1 + options.length) % options.length;
                        } else if (next2 == 66) { // Down Arrow \033[B
                            index = (index + 1) % options.length;
                        }
                    }
                } else if (c == 13 || c == 10) { // Enter mapping
                    break;
                }
            }
        } finally {
            // Guarantee terminal attributes recover so prompt functionality survives
            terminal.setAttributes(ogAttributes); 
            terminal.puts(Capability.clear_screen);
            terminal.writer().flush();
        }
        
        return index;
    }

    /**
     * @brief Primary gateway processing Unauthenticated user requests.
     * @return Boolean driving the main execution loop (false triggers program exit).
     * @throws IOException Native Exception propagated back when evaluating menu elements fails.
     */
    private static boolean renderMainMenu() throws IOException {
        String[] menuOptions = {
            "Login",
            "Register",
            "Switch Storage Backend [" + storageConfig.getActiveStorageType().name() + "]",
            "Exit Application"
        };

        int selection = interactiveMenu("MAIN MENU - System Ready", menuOptions);

        switch (selection) {
            case 0:
                handleLogin();
                break;
            case 1:
                handleRegister();
                break;
            case 2:
                handleStorageSwap();
                break;
            case 3:
                return false; // Exit signal interrupts app loop
        }
        return true;
    }

    /**
     * @brief Executes logic enabling a User credentials check.
     * @details Connects input requests against the localized UserService instance.
     */
    private static void handleLogin() {
        System.out.println("--- System Secure Login ---");
        String username = askInput("Username: ");
        String password = askPassword("Password: ");
        
        User user = userService.login(username, password);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Authentication Successful! Welcome, " + user.getUsername() + ".");
        } else {
            System.out.println("Credentials mismatch or User not found.");
        }
        pause();
    }

    /**
     * @brief Captures payload strings required to populate a new User identity.
     */
    private static void handleRegister() {
        System.out.println("--- Register New User Profile ---");
        String uname = askInput("Username: ");
        String email = askInput("Email: ");
        String password = askPassword("Password: ");

        try {
            userService.registerUser(UUID.randomUUID().toString(), uname, email, password);
            System.out.println("Registration complete! You may now return to the login screen.");
        } catch (Exception e) {
            System.out.println("Registration Failed: " + e.getMessage());
        }
        pause();
    }

    /**
     * @brief Interfaces through another interactive component facilitating a seamless StorageType swap.
     * @throws IOException Expected to fall-through seamlessly.
     */
    private static void handleStorageSwap() throws IOException {
        String[] storageChoices = {"BINARY_FILE", "SQLITE", "MYSQL", "Cancel Operation"};
        int choice = interactiveMenu("SELECT STORAGE BACKEND", storageChoices);

        if (choice < 3) {
            StorageType newType = StorageType.values()[choice];
            storageConfig.setActiveStorageType(newType);
            reloadServices(); // Apply Dependency Injection factory
            System.out.println("Subsystem synchronized to leverage: " + newType.name());
            pause();
        }
    }

    /**
     * @brief Generates actionable management components allocated to a validated User.
     * @return Boolean driving application loop iteration constraints.
     * @throws IOException Cascades native menu selection exceptions.
     */
    private static boolean renderDashboard() throws IOException {
        String[] dashboardOptions = {
            "Create New Project",
            "Create New Task",
            "Assign Task to Project",
            "Update Task Status",
            "Log Out",
            "Exit Application"
        };

        int selection = interactiveMenu("USER DASHBOARD | Currently Active: " + loggedInUser.getUsername(), dashboardOptions);

        switch (selection) {
            case 0:
                System.out.println("Action [Create New Project] dispatched..."); pause();
                break;
            case 1:
                System.out.println("Action [Create New Task] dispatched..."); pause();
                break;
            case 2:
                System.out.println("Action [Assign Task to Project] dispatched..."); pause();
                break;
            case 3:
                 System.out.println("Action [Update Task Status] dispatched..."); pause();
                 break;
            case 4:
                loggedInUser = null; // Purge active authentication session
                break;
            case 5:
                return false;
        }
        return true;
    }

    /**
     * @brief Halts the Thread briefly simulating a 'Wait' to view prompts.
     */
    private static void pause() {
        System.out.println("\nPress [ENTER] to continue...");
        try {
            // Bypass scanner mapping entirely
            terminal.reader().read(); 
        } catch (Exception ignored) {}
    }
}
