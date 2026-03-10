package com.ucoruh.projectmanagement.services;

import com.ucoruh.projectmanagement.models.Project;
import com.ucoruh.projectmanagement.utils.FileHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectService — manages all project-related operations.
 *
 * Responsibilities:
 *  - Create new projects (auto-generate IDs)
 *  - List all projects
 *  - View details of a single project
 *  - Delete a project (only by its owner)
 *  - Load/save projects via FileHelper
 *
 * Teammate suggestion: One teammate can own this service and
 * extend it with features like edit-project or archive-project.
 */
public class ProjectService {

    // -------------------------------------------------------
    //  Public API
    // -------------------------------------------------------

    /**
     * Creates a new project and persists it to file.
     *
     * @param name          project name
     * @param description   short description
     * @param ownerUsername username of the creator
     */
    public void createProject(String name, String description, String ownerUsername) {
        if (name.isEmpty()) {
            System.out.println("[ERROR] Project name cannot be empty.");
            return;
        }

        List<Project> projects = loadAllProjects();

        // Generate next ID: P001, P002, …
        String newId = generateNextId(projects);
        String today = LocalDate.now().toString();   // yields "yyyy-MM-dd"

        Project project = new Project(newId, name, description, ownerUsername, today);
        FileHelper.appendLine(FileHelper.PROJECTS_FILE, project.toFileString());

        System.out.println("[SUCCESS] Project created:");
        printProjectDetails(project);
    }

    /**
     * Prints a table of all projects in the system.
     */
    public void listAllProjects() {
        List<Project> projects = loadAllProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        System.out.println("\n--- All Projects ---");
        System.out.printf("%-6s %-25s %-20s %-12s%n", "ID", "Name", "Owner", "Created");
        System.out.println("-----------------------------------------------------------");
        for (Project p : projects) {
            System.out.printf("%-6s %-25s %-20s %-12s%n",
                    p.getId(), p.getName(), p.getOwnerUsername(), p.getCreatedAt());
        }
    }

    /**
     * Prints the full details of the project with the given ID.
     *
     * @param projectId the ID to look up (e.g. "P001")
     */
    public void viewProjectDetails(String projectId) {
        Project project = findProjectById(projectId);
        if (project == null) {
            System.out.println("Project not found: " + projectId);
            return;
        }
        System.out.println("\n--- Project Details ---");
        printProjectDetails(project);
    }

    /**
     * Deletes the specified project — only the owner may delete it.
     *
     * @param projectId     the ID of the project to remove
     * @param callerUsername the currently logged-in user
     */
    public void deleteProject(String projectId, String callerUsername) {
        List<Project> projects = loadAllProjects();
        boolean found = false;

        List<Project> updated = new ArrayList<>();
        for (Project p : projects) {
            if (p.getId().equalsIgnoreCase(projectId)) {
                found = true;
                if (!p.getOwnerUsername().equalsIgnoreCase(callerUsername)) {
                    System.out.println("[ERROR] Only the project owner can delete it.");
                    return;
                }
                System.out.println("[INFO] Deleted project: " + p.getName());
                // Skip this project — effectively deleting it
            } else {
                updated.add(p);
            }
        }

        if (!found) {
            System.out.println("Project not found: " + projectId);
            return;
        }

        // Persist the updated list (without the deleted project)
        saveAllProjects(updated);
    }

    // -------------------------------------------------------
    //  Lookup helpers used by other services
    // -------------------------------------------------------

    /**
     * Finds and returns a Project by its ID.
     * @return the Project, or null if not found
     */
    public Project findProjectById(String projectId) {
        for (Project p : loadAllProjects()) {
            if (p.getId().equalsIgnoreCase(projectId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Checks whether a project exists with the given ID.
     */
    public boolean projectExists(String projectId) {
        return findProjectById(projectId) != null;
    }

    // -------------------------------------------------------
    //  Private helpers
    // -------------------------------------------------------

    /**
     * Loads all projects from the data file.
     * @return list of Project objects; empty list if none
     */
    public List<Project> loadAllProjects() {
        List<String> lines = FileHelper.readAllLines(FileHelper.PROJECTS_FILE);
        List<Project> projects = new ArrayList<>();
        for (String line : lines) {
            Project p = Project.fromFileString(line);
            if (p != null) projects.add(p);
        }
        return projects;
    }

    /**
     * Overwrites the projects file with the supplied list.
     * @param projects the complete, updated list of projects
     */
    private void saveAllProjects(List<Project> projects) {
        List<String> lines = new ArrayList<>();
        for (Project p : projects) {
            lines.add(p.toFileString());
        }
        FileHelper.writeAllLines(FileHelper.PROJECTS_FILE, lines);
    }

    /**
     * Generates the next sequential project ID.
     * Format: P + 3-digit zero-padded number (P001, P002, …)
     */
    private String generateNextId(List<Project> existingProjects) {
        int maxNum = 0;
        for (Project p : existingProjects) {
            try {
                int num = Integer.parseInt(p.getId().substring(1));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException ignored) { /* skip malformed IDs */ }
        }
        return String.format("P%03d", maxNum + 1);
    }

    /** Pretty-prints a single project's details. */
    private void printProjectDetails(Project p) {
        System.out.println("  ID          : " + p.getId());
        System.out.println("  Name        : " + p.getName());
        System.out.println("  Description : " + p.getDescription());
        System.out.println("  Owner       : " + p.getOwnerUsername());
        System.out.println("  Created     : " + p.getCreatedAt());
    }
}
