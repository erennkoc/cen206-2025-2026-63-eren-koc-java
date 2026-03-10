package com.ucoruh.projectmanagement.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHelper — centralised utility for all File I/O operations.
 *
 * Responsibilities:
 *  - Define file paths for each data entity
 *  - Create data directory and files on first run
 *  - Read all lines from a file
 *  - Append a single line to a file
 *  - Overwrite a file with a new list of lines
 *
 * All services use this class; they never touch files directly.
 * This makes it easy to switch storage format later without touching business logic.
 */
public class FileHelper {

    // -------------------------------------------------------
    //  File paths — change here if you want to move data files
    // -------------------------------------------------------

    /** Directory where all data files are stored. */
    public static final String DATA_DIR = "data";

    /** Stores registered user accounts. */
    public static final String USERS_FILE    = DATA_DIR + "/users.txt";

    /** Stores project records. */
    public static final String PROJECTS_FILE = DATA_DIR + "/projects.txt";

    /** Stores task records. */
    public static final String TASKS_FILE    = DATA_DIR + "/tasks.txt";

    // -------------------------------------------------------
    //  Initialization
    // -------------------------------------------------------

    /**
     * Creates the data directory and empty data files if they do not exist.
     * Called once at application startup from Main.
     */
    public static void initializeDataFiles() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("[INFO] Created data directory: " + DATA_DIR);
        }

        createFileIfNotExists(USERS_FILE);
        createFileIfNotExists(PROJECTS_FILE);
        createFileIfNotExists(TASKS_FILE);
    }

    /**
     * Creates an empty file at the given path if it does not already exist.
     * @param filePath relative or absolute path to the target file
     */
    private static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("[INFO] Created file: " + filePath);
            } catch (IOException e) {
                System.out.println("[ERROR] Could not create file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    // -------------------------------------------------------
    //  Read operations
    // -------------------------------------------------------

    /**
     * Reads all non-blank lines from the specified file.
     *
     * @param filePath path to the file to read
     * @return a List of non-empty, trimmed lines; empty list on error
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return lines;   // file not created yet → return empty

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not read file: " + filePath);
            e.printStackTrace();
        }

        return lines;
    }

    // -------------------------------------------------------
    //  Write operations
    // -------------------------------------------------------

    /**
     * Appends a single line to the end of the specified file.
     * Creates the file if it doesn't exist.
     *
     * @param filePath relative or absolute path to the target file
     * @param line     the text to append (a newline is added automatically)
     */
    public static void appendLine(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(new File(filePath), true))) {  // true = append mode
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[ERROR] Could not write to file: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Overwrites the entire file with the provided list of lines.
     * Use this for update and delete operations.
     *
     * @param filePath relative or absolute path to the target file
     * @param lines    the complete new content for the file
     */
    public static void writeAllLines(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(new File(filePath), false))) { // false = overwrite mode
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not write to file: " + filePath);
            e.printStackTrace();
        }
    }
}
