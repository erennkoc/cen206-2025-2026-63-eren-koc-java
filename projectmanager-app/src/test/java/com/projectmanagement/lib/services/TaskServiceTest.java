package com.projectmanagement.lib.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.storage.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private IRepository<Task> taskRepositoryMock;

    private TaskService taskService;

    @BeforeEach
    public void setup() {
        taskService = new TaskService(taskRepositoryMock);
    }

    @Test
    public void testRegisterTask_Success() {
        String id = "task-1";
        String title = "Complete Documentation";
        String desc = "Write all the missing Doxygen comments";

        taskService.registerTask(id, title, desc);
        verify(taskRepositoryMock, times(1)).save(any(Task.class));
    }

    @Test
    public void testRegisterTask_NullFields_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.registerTask(null, "Title", "Desc");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.registerTask("ID", null, "Desc");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.registerTask("ID", "", "Desc"); // Empty title
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.registerTask("ID", "Title", null);
        });

        verify(taskRepositoryMock, never()).save(any(Task.class));
    }

    @Test
    public void testAssignTaskToUser_Success() {
        String taskId = "task-1";
        Task trackingTask = new Task(taskId, "Title", "Desc");
        User assignedUser = new User("user-1", "Bob", "bob@user.com", "hash");

        when(taskRepositoryMock.findById(taskId)).thenReturn(trackingTask);

        taskService.assignTaskToUser(taskId, assignedUser);

        assertEquals(assignedUser, trackingTask.getAssignedUser());
        verify(taskRepositoryMock, times(1)).update(trackingTask);
    }

    @Test
    public void testAssignTaskToUser_InvalidInputs_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToUser(null, new User());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToUser("task-1", null);
        });

        when(taskRepositoryMock.findById("missing-task")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToUser("missing-task", new User());
        });

        verify(taskRepositoryMock, never()).update(any(Task.class));
    }

    @Test
    public void testUpdateTaskStatus_Success() {
        String taskId = "task-1";
        Task operatingTask = new Task(taskId, "Title", "Desc");

        when(taskRepositoryMock.findById(taskId)).thenReturn(operatingTask);

        taskService.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, operatingTask.getStatus());
        verify(taskRepositoryMock, times(1)).update(operatingTask);
    }

    @Test
    public void testUpdateTaskStatus_InvalidInputs_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTaskStatus(null, TaskStatus.DONE);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTaskStatus("task-1", null);
        });

        when(taskRepositoryMock.findById("missing-task")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
             taskService.updateTaskStatus("missing-task", TaskStatus.IN_PROGRESS);
        });

        verify(taskRepositoryMock, never()).update(any(Task.class));
    }

    @Test
    public void testGetAllTasks() {
        java.util.List<Task> mockList = java.util.Arrays.asList(new Task("task-1", "T", "D"));
        when(taskRepositoryMock.findAll()).thenReturn(mockList);
        java.util.List<Task> result = taskService.getAllTasks();
        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findAll();
    }
}
