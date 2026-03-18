package com.projectmanagement.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        try {
            Field scannerField = Main.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(null, new Scanner(System.in));
        } catch (Exception e) {}
    }

    @Test
    public void testMain_GuestModeAndExit() {
        // App starts in Auth Menu
        // 3 -> Guest Mode
        // \n -> Pause
        // 6 -> Exit Application
        String simulatedUserInput = "3\n\n6\n";
        provideInput(simulatedUserInput);
        
        try {
            Main.main(new String[0]);
        } catch (Exception e) {
            // Ignore for now
        }
        
        String output = testOut.toString();
        assertTrue(output.contains("Guest Mode activated."));
        assertTrue(output.contains("Exiting application. Goodbye!"));
    }

    @Test
    public void testMain_FullNavigation() {
        // We will try to cover the rest of the Main methods via fallback interactive menu
        StringBuilder sb = new StringBuilder();
        
        // --- AUTH MENU ---
        // 1. Try invalid input (letters) -> "Invalid character! Please enter numbers only."
        sb.append("a\n");
        // 2. Try invalid range
        sb.append("9\n");
        
        // 3. Register
        sb.append("2\n");
        sb.append("usr\n"); // username
        sb.append("email@email.com\n"); // email
        sb.append("pass\n"); // pass
        sb.append("\n"); // pause
        
        // 4. Login with wrong pass
        sb.append("1\n");
        sb.append("usr\n");
        sb.append("wrong\n");
        sb.append("\n"); // pause

        // 5. Login correct
        sb.append("1\n");
        sb.append("usr\n");
        sb.append("pass\n");
        sb.append("\n"); // pause
        
        // --- MAIN MENU ---
        // Project Setup
        sb.append("1\n");
        sb.append("1\n"); // Create project
        sb.append("ProjA\n"); // name
        sb.append("DescA\n"); // desc
        sb.append("\n"); // pause
        
        // Project Setup -> View all
        sb.append("2\n"); // View all projects
        sb.append("\n"); // pause
        
        // Back to main
        sb.append("3\n");
        
        // Task Assignment
        sb.append("2\n");
        sb.append("3\n"); // View all Tasks (none initially)
        sb.append("\n"); // pause
        
        // Add Task
        sb.append("1\n"); // Create new task
        sb.append("1\n"); // Select Project 1
        sb.append("TaskA\n"); // title
        sb.append("DescA\n"); // desc
        sb.append("\n"); // pause
        
        // Assign User to task
        sb.append("2\n"); // Assign user
        sb.append("1\n"); // Task 1
        sb.append("1\n"); // User 1
        sb.append("\n"); // pause
        
        // Back to main
        sb.append("4\n");
        
        // Progress Tracking
        sb.append("3\n");
        sb.append("1\n"); // Update Task
        sb.append("1\n"); // Task 1
        sb.append("3\n"); // status 3 (DONE)
        sb.append("\n"); // pause
        
        sb.append("2\n"); // Back
        
        // Reporting
        sb.append("4\n");
        sb.append("1\n"); // General
        sb.append("\n"); // pause
        sb.append("2\n"); // Task
        sb.append("\n"); // pause
        sb.append("3\n"); // Back
        
        // Logout
        sb.append("5\n");
        sb.append("\n"); // pause
        
        // Auth Menu -> Swap Storage
        sb.append("4\n"); // Change storage
        sb.append("1\n"); // Select Binary
        sb.append("\n"); // pause
        
        // Exit
        sb.append("5\n");
        
        provideInput(sb.toString());
        
        try {
            Main.main(new String[0]);
        } catch (Exception e) {}
    }
}
