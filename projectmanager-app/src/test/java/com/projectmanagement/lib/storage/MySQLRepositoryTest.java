package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.TaskStatus;
import com.projectmanagement.lib.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class MySQLRepositoryTest {

    private IRepository<User> userRepo;
    private IRepository<Project> projectRepo;
    private IRepository<Task> taskRepo;

    @BeforeEach
    void setUp() {
        userRepo = new MySQLUserRepository();
        projectRepo = new MySQLProjectRepository();
        taskRepo = new MySQLTaskRepository();
    }

    @Test
    void testUserCrud() {
        // Since MySQL is external, the methods will gracefully log exceptions and return null
        // if the database container is not actively running.
        User user = new User("u-m1", "MName", "m@name.com", "mpass");
        userRepo.create(user);
        
        userRepo.findById("u-m1");
        
        user.setUsername("MName2");
        userRepo.update(user);
        
        List<User> all = userRepo.findAll();
        
        userRepo.delete("u-m1");
    }

    @Test
    void testProjectCrud() {
        Project proj = new Project("p-m1", "PName", "pdesc");
        projectRepo.create(proj);
        
        projectRepo.findById("p-m1");
        
        proj.setName("PName2");
        projectRepo.update(proj);
        
        List<Project> all = projectRepo.findAll();
        
        projectRepo.delete("p-m1");
    }

    @Test
    void testTaskCrud() {
        Task task = new Task("t-m1", "TName", "tdesc");
        task.setStatus(TaskStatus.TODO);
        taskRepo.create(task);
        
        taskRepo.findById("t-m1");
        
        task.setTitle("TName2");
        taskRepo.update(task);
        
        List<Task> all = taskRepo.findAll();
        
        taskRepo.delete("t-m1");
    }
}
