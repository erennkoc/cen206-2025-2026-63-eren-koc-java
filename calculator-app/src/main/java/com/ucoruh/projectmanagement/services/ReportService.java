package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.Project;
import com.ucoruh.projectmanagement.models.Task;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @class ReportService
 * @brief Generates summary reports for projects and tasks.
 * @details Reports are printed to the console and also saved as plain-text
 *          files in the "data/reports/" directory, timestamped so multiple
 *          reports do not overwrite each other.
 */
public class ReportService {

    /** Directory where report text files are saved. */
    private static final String REPORT_DIR = "data/reports/";

    private static final String PROJECTS_FILE = "data/projects.dat";
    private static final String TASKS_FILE    = "data/tasks.dat";

    // =========================================================================
    // Menu entry point
    // =========================================================================

    /**
     * @brief Generates and displays the full project & task summary report.
     * @details Writes the same content to a timestamped file in "data/reports/".
     */
    public void generateReport() {
        System.out.println("\n========================================");
        System.out.println("         PROJECT & TASK REPORT");
        System.out.println("========================================");

        List<Project> projects = FileHelper.loadList(PROJECTS_FILE);
        List<Task>    tasks    = FileHelper.loadList(TASKS_FILE);

        StringBuilder sb = new StringBuilder();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        sb.append("Report Generated: ").append(timestamp).append("\n");
        sb.append("========================================\n");
        sb.append("PROJECTS (").append(projects.size()).append(")\n");
        sb.append("========================================\n");

        if (projects.isEmpty()) {
            sb.append("  (No projects found.)\n");
        } else {
            for (Project p : projects) {
                sb.append(p.toDisplayString()).append("\n\n");
            }
        }

        sb.append("========================================\n");
        sb.append("TASKS (").append(tasks.size()).append(")\n");
        sb.append("========================================\n");

        if (tasks.isEmpty()) {
            sb.append("  (No tasks found.)\n");
        } else {
            long pending    = 0, inProgress = 0, done = 0;
            for (Task t : tasks) {
                sb.append(String.format("  [%d] %s | %s | -> %s%n",
                        t.getTaskId(), t.getTaskName(), t.getAssignedTo(), t.getStatus()));
                switch (t.getStatus()) {
                    case "PENDING":     pending++;     break;
                    case "IN_PROGRESS": inProgress++;  break;
                    case "DONE":        done++;         break;
                }
            }
            sb.append("\nSummary:\n");
            sb.append("  Pending     : ").append(pending).append("\n");
            sb.append("  In Progress : ").append(inProgress).append("\n");
            sb.append("  Done        : ").append(done).append("\n");
        }

        // Print to console
        System.out.println(sb.toString());

        // Save to file
        saveReportToFile(sb.toString(), timestamp);
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * @brief Writes the report content to a timestamped file.
     *
     * @param content   the report text
     * @param timestamp the timestamp string used in the filename
     */
    private void saveReportToFile(String content, String timestamp) {
        new File(REPORT_DIR).mkdirs();
        String fileName = REPORT_DIR + "report_"
                + timestamp.replace(":", "-").replace(" ", "_") + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.print(content);
            System.out.println("[OK] Report saved to: " + fileName);
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save report: " + e.getMessage());
        }
    }
}
