package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class MySQLRepositoryTest {

    @Mock private Connection mockConn;
    @Mock private PreparedStatement mockPstmt;
    @Mock private Statement mockStmt;
    @Mock private ResultSet mockRs;

    @Mock private PreparedStatement mockTaskPstmt;
    @Mock private ResultSet mockTaskRs;
    
    private AutoCloseable mocks;

    private IRepository<User> userRepo;
    private IRepository<Project> projectRepo;
    private IRepository<Task> taskRepo;

    @BeforeEach
    void setUp() throws SQLException {
        mocks = MockitoAnnotations.openMocks(this);
        
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockConn.createStatement()).thenReturn(mockStmt);
        
        when(mockPstmt.executeQuery()).thenReturn(mockRs);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getString(anyString())).thenReturn("mockData");

        when(mockConn.prepareStatement("SELECT task_id FROM project_tasks WHERE project_id = ?")).thenReturn(mockTaskPstmt);
        when(mockTaskPstmt.executeQuery()).thenReturn(mockTaskRs);
        when(mockTaskRs.next()).thenReturn(true, false);
        when(mockTaskRs.getString("task_id")).thenReturn("mockTask1");
        
        userRepo = new MySQLUserRepository() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConn;
            }
        };
        projectRepo = new MySQLProjectRepository() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConn;
            }
        };
        taskRepo = new MySQLTaskRepository() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConn;
            }
        };
    }
    
    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testUserCrud() throws Exception {
        User user = new User("u-m1", "MName", "m@name.com", "mpass");
        userRepo.save(user);
        
        User foundUser = userRepo.findById("u-m1"); 
        assertNotNull(foundUser);
        assertEquals("mockData", foundUser.getUsername());
        
        user.setUsername("MName2");
        userRepo.update(user);
        
        List<User> all = userRepo.findAll();
        assertEquals(1, all.size());
        
        userRepo.delete("u-m1");
    }

    @Test
    void testProjectCrud() throws Exception {
        Project proj = new Project("p-m1", "PName", "pdesc");
        proj.addTask(new Task("mockTask1", "TName", "TDesc"));
        projectRepo.save(proj);
        
        Project foundProj = projectRepo.findById("p-m1");
        assertNotNull(foundProj);
        assertEquals("mockData", foundProj.getName());
        
        proj.setName("PName2");
        projectRepo.update(proj);
        
        List<Project> all = projectRepo.findAll();
        assertEquals(1, all.size());
        
        projectRepo.delete("p-m1");
    }

    @Test
    void testTaskCrud() throws Exception {
        when(mockRs.getString("status")).thenReturn("TODO"); 
        
        Task task = new Task("t-m1", "TName", "tdesc");
        task.setStatus(TaskStatus.TODO);
        taskRepo.save(task);
        
        Task foundTask = taskRepo.findById("t-m1");
        assertNotNull(foundTask);
        
        task.setTitle("TName2");
        taskRepo.update(task);
        
        List<Task> all = taskRepo.findAll();
        assertEquals(1, all.size());
        
        taskRepo.delete("t-m1");
    }

    @Test
    void testSQLExceptions() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Mocked Exception"));
        when(mockConn.createStatement()).thenThrow(new SQLException("Mocked Exception"));

        User user = new User("id", "name", "email", "pass");
        userRepo.save(user);
        userRepo.findById("id");
        userRepo.findAll();
        userRepo.update(user);
        userRepo.delete("id");

        Project proj = new Project("id", "name", "desc");
        projectRepo.save(proj);
        projectRepo.findById("id");
        projectRepo.findAll();
        projectRepo.update(proj);
        projectRepo.delete("id");

        Task task = new Task("id", "title", "desc");
        taskRepo.save(task);
        taskRepo.findById("id");
        taskRepo.findAll();
        taskRepo.update(task);
        taskRepo.delete("id");
    }

    @Test
    void testConstructorSQLExceptions() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            new MySQLUserRepository() {
                @Override protected Connection getConnection() throws SQLException { throw new SQLException("Mock Constructor Ex"); }
            };
            new MySQLProjectRepository() {
                @Override protected Connection getConnection() throws SQLException { throw new SQLException("Mock Constructor Ex"); }
            };
            new MySQLTaskRepository() {
                @Override protected Connection getConnection() throws SQLException { throw new SQLException("Mock Constructor Ex"); }
            };
        });
    }
}
