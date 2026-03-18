package com.projectmanagement.lib.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testDefaultConstructor() {
        Task task = new Task();
        assertNull(task.getId());
        assertNotNull(task.getCreatedDate());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getAssignedUser());
    }

    @Test
    public void testParameterizedConstructor() {
        Task task = new Task("t1", "Fix bug", "Fix the critical issue");
        assertEquals("t1", task.getId());
        assertEquals("Fix bug", task.getTitle());
        assertEquals("Fix the critical issue", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNull(task.getAssignedUser());
    }

    @Test
    public void testSettersAndGetters() {
        Task task = new Task();
        User user = new User("u1", "bob", "bob@example.com", "pass");
        task.setId("t2");
        task.setTitle("Develop feature");
        task.setDescription("Code the feature");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setAssignedUser(user);

        assertEquals("t2", task.getId());
        assertEquals("Develop feature", task.getTitle());
        assertEquals("Code the feature", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(user, task.getAssignedUser());
    }

    @Test
    public void testDisplayDetailsUnassigned() {
        Task task = new Task("t3", "Review code", "Review PR #42");
        task.displayDetails();
        String output = outContent.toString();
        assertTrue(output.contains("Task ID: t3"));
        assertTrue(output.contains("Title: Review code"));
        assertTrue(output.contains("Status: TODO"));
        assertTrue(output.contains("Assigned To: Unassigned"));
    }

    @Test
    public void testDisplayDetailsAssigned() {
        Task task = new Task("t4", "Write tests", "Increase coverage");
        task.setStatus(TaskStatus.DONE);
        User user = new User("u1", "charlie", "charlie@m.com", "pass");
        task.setAssignedUser(user);
        task.displayDetails();
        String output = outContent.toString();
        assertTrue(output.contains("Task ID: t4"));
        assertTrue(output.contains("Title: Write tests"));
        assertTrue(output.contains("Status: DONE"));
        assertTrue(output.contains("Assigned To: charlie"));
    }
}
