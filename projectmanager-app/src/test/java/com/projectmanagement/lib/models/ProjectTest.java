package com.projectmanagement.lib.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

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
        Project project = new Project();
        assertNull(project.getId());
        assertNotNull(project.getCreatedDate());
        assertNotNull(project.getTasks());
        assertTrue(project.getTasks().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        Project project = new Project("p1", "Alpha", "Project Alpha description");
        assertEquals("p1", project.getId());
        assertEquals("Alpha", project.getName());
        assertEquals("Project Alpha description", project.getDescription());
        assertNotNull(project.getTasks());
        assertTrue(project.getTasks().isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        Project project = new Project();
        project.setId("p2");
        project.setName("Beta");
        project.setDescription("Project Beta description");

        List<Task> tasks = new ArrayList<>();
        Task task = new Task("t1", "T1", "Desc");
        tasks.add(task);
        project.setTasks(tasks);

        assertEquals("p2", project.getId());
        assertEquals("Beta", project.getName());
        assertEquals("Project Beta description", project.getDescription());
        assertEquals(1, project.getTasks().size());
        assertEquals(task, project.getTasks().get(0));
    }

    @Test
    public void testAddRemoveTask() {
        Project project = new Project();
        Task task1 = new Task("t1", "T1", "Desc1");
        Task task2 = new Task("t2", "T2", "Desc2");

        project.addTask(task1);
        project.addTask(task2);
        assertEquals(2, project.getTasks().size());
        assertTrue(project.getTasks().contains(task1));

        project.removeTask(task1);
        assertEquals(1, project.getTasks().size());
        assertFalse(project.getTasks().contains(task1));
    }

    @Test
    public void testDisplayDetails() {
        Project project = new Project("p3", "Gamma", "Proj Gamma");
        project.addTask(new Task("t1", "T1", "D1"));
        project.displayDetails();
        String output = outContent.toString();
        assertTrue(output.contains("Project ID: p3"));
        assertTrue(output.contains("Name: Gamma"));
        assertTrue(output.contains("Description: Proj Gamma"));
        assertTrue(output.contains("Total Tasks: 1"));
    }
}
