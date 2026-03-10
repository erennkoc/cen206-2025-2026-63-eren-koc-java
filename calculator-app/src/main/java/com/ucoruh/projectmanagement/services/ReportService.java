package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.Project;
import com.ucoruh.projectmanagement.models.Task;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportService — generates human-readable reports about projects and tasks.
 *
 * Responsibilities:
 *  - Print a summary report for a single project to console and to a .txt file
 *  - Print a full report of all projects
 *
 * Reports are saved to the "data/reports/" directory so they can be
 * reviewed later without reopening the application.
 *
 * Teammate suggestion: One teammate can extend this service with
 * CSV or HTML report formats, email export, etc.
 */
public class ReportService {

    /** Directory where generated report files are saved. */
    private static final String REPORTS_DIR = "data/reports";

    /** Services used to fetch data for reports. */
    private final ProjectService projectService = new ProjectService();
    private final TaskService    taskService    = new TaskService();

    // -------------------------------------------------------
    //  Public API
    // -------------------------------------------------------

    /**
     * Generates a summary report for a single project:
     *  - Project metadata
     *  - Task count per status
     *  - Completion percentage
     *  - Full task list
     *
     * @param projectId the ID of the project to report on
     */
    public void generateProjectReport(String projectId) {
        Project project = projectService.findProjectById(projectId);

        if (project == null) {
            System.out.println("Project not found: " + projectId);
            return;
        }

        // Gather tasks for this project
        List<Task> allTasks = taskService.loadAllTasks();
        List<Task> projectTasks = new ArrayList<>();
        for (Task t : allTasks) {
            if (t.getProjectId().equalsIgnoreCase(projectId)) {
                projectTasks.add(t);
            }
        }

        // Build the report content
        List<String> reportLines = buildProjectReportLines(project, projectTasks);

        // Print to console
        System.out.println();
        for (String line : reportLines) System.out.println(line);

        // Save to file
        String fileName = "report_" + projectId + "_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".txt";
        saveReport(fileName, reportLines);
    }

    /**
     * Generates a full system report across all projects.
     * Each project's summary is included with its task breakdown.
     */
    public void generateFullReport() {
        List<Project> projects = projectService.loadAllProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects found to report on.");
            return;
        }

        List<Task> allTasks = taskService.loadAllTasks();

        List<String> fullReport = new ArrayList<>();
        fullReport.add("=========================================");
        fullReport.add("     FULL SYSTEM REPORT");
        fullReport.add("     Generated: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fullReport.add("     Total projects: " + projects.size());
        fullReport.add("     Total tasks:    " + allTasks.size());
        fullReport.add("=========================================");

        for (Project p : projects) {
            // Filter tasks for this project
            List<Task> projectTasks = new ArrayList<>();
            for (Task t : allTasks) {
                if (t.getProjectId().equalsIgnoreCase(p.getId())) {
                    projectTasks.add(t);
                }
            }
            fullReport.add("");  // blank separator
            fullReport.addAll(buildProjectReportLines(p, projectTasks));
        }

        // Print to console
        System.out.println();
        for (String line : fullReport) System.out.println(line);

        // Save to file
        String fileName = "full_report_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".txt";
        saveReport(fileName, fullReport);
    }

    // -------------------------------------------------------
    //  Private helpers
    // -------------------------------------------------------

    /**
     * Builds the report lines for a single project.
     *
     * @param project      the project metadata
     * @param projectTasks tasks that belong to this project
     * @return list of printable/saveable strings
     */
    private List<String> buildProjectReportLines(Project project, List<Task> projectTasks) {
        int total = projectTasks.size();
        int todo = 0, inProgress = 0, done = 0;

        for (Task t : projectTasks) {
            switch (t.getStatus()) {
                case Task.STATUS_TODO:        todo++;        break;
                case Task.STATUS_IN_PROGRESS: inProgress++;  break;
                case Task.STATUS_DONE:        done++;        break;
            }
        }

        int percentComplete = (total == 0) ? 0 : (int) ((done / (double) total) * 100);

        List<String> lines = new ArrayList<>();
        lines.add("-----------------------------------------");
        lines.add("PROJECT REPORT: " + project.getName());
        lines.add("-----------------------------------------");
        lines.add("  ID          : " + project.getId());
        lines.add("  Description : " + project.getDescription());
        lines.add("  Owner       : " + project.getOwnerUsername());
        lines.add("  Created     : " + project.getCreatedAt());
        lines.add("  --- Task Summary ---");
        lines.add("  Total       : " + total);
        lines.add("  TODO        : " + todo);
        lines.add("  IN_PROGRESS : " + inProgress);
        lines.add("  DONE        : " + done);
        lines.add("  Completion  : " + percentComplete + "%");
        lines.add("");

        if (projectTasks.isEmpty()) {
            lines.add("  No tasks assigned to this project yet.");
        } else {
            lines.add("  --- Task Details ---");
            for (Task t : projectTasks) {
                lines.add(String.format("  [%s] %-6s | %-25s | Assigned: %-15s | Created: %s",
                        t.getStatus(), t.getId(), t.getTitle(),
                        t.getAssignedTo(), t.getCreatedAt()));
            }
        }
        lines.add("-----------------------------------------");
        return lines;
    }

    /**
     * Saves the report lines to a text file in the reports directory.
     *
     * @param fileName  the name of the output file
     * @param lines     the content to write
     */
    private void saveReport(String fileName, List<String> lines) {
        // Make sure the reports directory exists
        File dir = new File(REPORTS_DIR);
        if (!dir.exists()) dir.mkdirs();

        String filePath = REPORTS_DIR + "/" + fileName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("[INFO] Report saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save report: " + filePath);
            e.printStackTrace();
        }
    }
}
