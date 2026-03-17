package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BinaryRepositoryTest {

    private IRepository<User> userRepo;
    private IRepository<Project> projectRepo;
    private IRepository<Task> taskRepo;

    @BeforeEach
    void setUp() {
        userRepo = new BinaryUserRepository();
        projectRepo = new BinaryProjectRepository();
        taskRepo = new BinaryTaskRepository();
    }

    @Test
    void testUserCrud() {
        User user = new User("u-b1", "BName", "b@name.com", "bpass");
        userRepo.create(user);
        assertNotNull(userRepo.findById("u-b1"));
        
        user.setUsername("BName2");
        userRepo.update(user);
        assertEquals("BName2", userRepo.findById("u-b1").getUsername());
        
        List<User> all = userRepo.findAll();
        assertFalse(all.isEmpty());
        
        userRepo.delete("u-b1");
        assertNull(userRepo.findById("u-b1"));
    }

    @Test
    void testProjectCrud() {
        Project proj = new Project("p-b1", "PName", "pdesc");
        projectRepo.create(proj);
        assertNotNull(projectRepo.findById("p-b1"));
        
        proj.setName("PName2");
        projectRepo.update(proj);
        assertEquals("PName2", projectRepo.findById("p-b1").getName());
        
        List<Project> all = projectRepo.findAll();
        assertFalse(all.isEmpty());
        
        projectRepo.delete("p-b1");
        assertNull(projectRepo.findById("p-b1"));
    }

    @Test
    void testTaskCrud() {
        Task task = new Task("t-b1", "TName", "tdesc");
        task.setStatus(TaskStatus.TODO);
        taskRepo.create(task);
        assertNotNull(taskRepo.findById("t-b1"));
        
        task.setTitle("TName2");
        taskRepo.update(task);
        assertEquals("TName2", taskRepo.findById("t-b1").getTitle());
        
        List<Task> all = taskRepo.findAll();
        assertFalse(all.isEmpty());
        
        taskRepo.delete("t-b1");
        assertNull(taskRepo.findById("t-b1"));
    }
}
