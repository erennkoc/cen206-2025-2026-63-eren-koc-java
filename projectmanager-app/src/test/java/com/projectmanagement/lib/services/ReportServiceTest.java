package com.projectmanagement.lib.services;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ReportServiceTest {

    private ProjectService mockProjectService;
    private TaskService mockTaskService;
    private UserService mockUserService;
    private ReportService reportService;

    @BeforeEach
    public void setUp() {
        mockProjectService = mock(ProjectService.class);
        mockTaskService = mock(TaskService.class);
        mockUserService = mock(UserService.class);
        reportService = new ReportService(mockProjectService, mockTaskService, mockUserService);
    }

    @Test
    public void testGenerateGeneralSystemReport() {
        // Arrange
        User u1 = new User("u1", "U1", "u1@test.com", "pass");
        User u2 = new User("u2", "U2", "u2@test.com", "pass");
        
        Project p1 = new Project("p1", "Project 1", "Desc");
        Task t1 = new Task("t1", "Task 1", "Desc");
        p1.addTask(t1);

        when(mockUserService.getAllUsers()).thenReturn(Arrays.asList(u1, u2));
        when(mockProjectService.getAllProjects()).thenReturn(Arrays.asList(p1));
        when(mockTaskService.getAllTasks()).thenReturn(Arrays.asList(t1));

        // Act & Assert (mostly to ensure no exceptions are thrown and it covers the lines)
        reportService.generateGeneralSystemReport();
    }

    @Test
    public void testGenerateGeneralSystemReport_Empty() {
        when(mockUserService.getAllUsers()).thenReturn(null);
        when(mockProjectService.getAllProjects()).thenReturn(null);
        when(mockTaskService.getAllTasks()).thenReturn(null);

        reportService.generateGeneralSystemReport();
    }

    @Test
    public void testGenerateTaskStatusReport() {
        Task t1 = new Task("t1", "T1", "Desc");
        t1.setStatus(TaskStatus.TODO);
        
        Task t2 = new Task("t2", "T2", "Desc");
        t2.setStatus(TaskStatus.IN_PROGRESS);

        Task t3 = new Task("t3", "T3", "Desc");
        t3.setStatus(TaskStatus.DONE);

        when(mockTaskService.getAllTasks()).thenReturn(Arrays.asList(t1, t2, t3));

        reportService.generateTaskStatusReport();
    }

    @Test
    public void testGenerateTaskStatusReport_Empty() {
        when(mockTaskService.getAllTasks()).thenReturn(new ArrayList<>());
        reportService.generateTaskStatusReport();
        
        when(mockTaskService.getAllTasks()).thenReturn(null);
        reportService.generateTaskStatusReport();
    }
}
