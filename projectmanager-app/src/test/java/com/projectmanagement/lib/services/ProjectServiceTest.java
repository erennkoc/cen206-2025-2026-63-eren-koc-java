package com.projectmanagement.lib.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.storage.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private IRepository<Project> projectRepositoryMock;

    private ProjectService projectService;

    @BeforeEach
    public void setup() {
        projectService = new ProjectService(projectRepositoryMock);
    }

    @Test
    public void testCreateProject_Success() {
        String id = "proj-1";
        String name = "Test Project";
        String desc = "Testing Project Logic";

        projectService.createProject(id, name, desc);
        verify(projectRepositoryMock, times(1)).save(any(Project.class));
    }

    @Test
    public void testCreateProject_NullInputs_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(null, "Name", "Desc");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject("ID", null, "Desc");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject("ID", "", "Desc"); // Empty string
        });

        assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject("ID", "Name", null); // null desc
        });

        verify(projectRepositoryMock, never()).save(any(Project.class));
    }

    @Test
    public void testAddTaskToProject_Success() {
        String projectId = "proj-1";
        Project mockedProject = new Project(projectId, "Test Project", "Desc");
        Task newTask = new Task("task-1", "A Task", "Doing it");

        when(projectRepositoryMock.findById(projectId)).thenReturn(mockedProject);

        projectService.addTaskToProject(projectId, newTask);

        // Project should now have 1 task
        assertEquals(1, mockedProject.getTasks().size());
        verify(projectRepositoryMock, times(1)).update(mockedProject);
    }

    @Test
    public void testAddTaskToProject_InvalidInputs_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            projectService.addTaskToProject(null, new Task());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            projectService.addTaskToProject("proj-id", null);
        });

        // Test non-existent project
        when(projectRepositoryMock.findById("missing-proj")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            projectService.addTaskToProject("missing-proj", new Task());
        });
        
        verify(projectRepositoryMock, never()).update(any(Project.class));
    }

    @Test
    public void testGetAllProjects() {
        java.util.List<Project> mockList = java.util.Arrays.asList(new Project("proj-1", "T", "D"));
        when(projectRepositoryMock.findAll()).thenReturn(mockList);
        java.util.List<Project> result = projectService.getAllProjects();
        assertEquals(1, result.size());
        verify(projectRepositoryMock, times(1)).findAll();
    }
}
