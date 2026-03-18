package com.projectmanagement.lib.services;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.models.TaskStatus;

import java.util.List;

public class ReportService {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;

    public ReportService(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    public void generateGeneralSystemReport() {
        System.out.println("\n=====================================");
        System.out.println("       GENERAL SYSTEM REPORT         ");
        System.out.println("=====================================");
        
        List<Project> projects = projectService.getAllProjects();
        List<Task> tasks = taskService.getAllTasks();
        List<User> users = userService.getAllUsers();
        
        System.out.println("Total Registered Users  : " + (users != null ? users.size() : 0));
        System.out.println("Total Managed Projects  : " + (projects != null ? projects.size() : 0));
        System.out.println("Total Tracked Tasks     : " + (tasks != null ? tasks.size() : 0));
        
        if (projects != null && !projects.isEmpty()) {
            System.out.println("\n--- Projects Breakdown ---");
            for (Project project : projects) {
                System.out.println(" [Project] " + project.getName() + " (Contains " + project.getTasks().size() + " tasks)");
            }
        }
        System.out.println("=====================================\n");
    }

    public void generateTaskStatusReport() {
        System.out.println("\n=====================================");
        System.out.println("         TASK STATUS REPORT          ");
        System.out.println("=====================================");
        
        List<Task> tasks = taskService.getAllTasks();
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks found in the system.");
            System.out.println("=====================================\n");
            return;
        }

        long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        System.out.println("Total Tasks     : " + tasks.size());
        System.out.println("TODO            : " + todo);
        System.out.println("IN_PROGRESS     : " + inProgress);
        System.out.println("DONE            : " + done);
        System.out.println("=====================================\n");
    }
}
