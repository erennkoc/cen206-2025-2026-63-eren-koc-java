package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SQLiteRepositoryTest {

    private IRepository<User> userRepo;
    private IRepository<Project> projectRepo;
    private IRepository<Task> taskRepo;

    @BeforeEach
    void setUp() {
        userRepo = new SQLiteUserRepository();
        projectRepo = new SQLiteProjectRepository();
        taskRepo = new SQLiteTaskRepository();
    }

    @Test
    void testUserCrud() {
        User user = new User("u-s1", "SName", "s@name.com", "spass");
        userRepo.delete("u-s1"); // Clear if exists
        userRepo.create(user);
        
        User found = userRepo.findById("u-s1");
        assertNotNull(found);
        
        user.setUsername("SName2");
        userRepo.update(user);
        assertEquals("SName2", userRepo.findById("u-s1").getUsername());
        
        List<User> all = userRepo.findAll();
        assertFalse(all.isEmpty());
        
        userRepo.delete("u-s1");
        assertNull(userRepo.findById("u-s1"));
    }

    @Test
    void testProjectCrud() {
        Project proj = new Project("p-s1", "PName", "pdesc");
        projectRepo.delete("p-s1");
        projectRepo.create(proj);
        
        Project found = projectRepo.findById("p-s1");
        assertNotNull(found);
        
        proj.setName("PName2");
        projectRepo.update(proj);
        assertEquals("PName2", projectRepo.findById("p-s1").getName());
        
        List<Project> all = projectRepo.findAll();
        assertFalse(all.isEmpty());
        
        projectRepo.delete("p-s1");
        assertNull(projectRepo.findById("p-s1"));
    }

    @Test
    void testTaskCrud() {
        Task task = new Task("t-s1", "TName", "tdesc");
        task.setStatus(TaskStatus.TODO);
        taskRepo.delete("t-s1");
        taskRepo.create(task);
        
        Task found = taskRepo.findById("t-s1");
        assertNotNull(found);
        
        task.setTitle("TName2");
        taskRepo.update(task);
        assertEquals("TName2", taskRepo.findById("t-s1").getTitle());
        
        List<Task> all = taskRepo.findAll();
        assertFalse(all.isEmpty());
        
        taskRepo.delete("t-s1");
        assertNull(taskRepo.findById("t-s1"));
    }
}
