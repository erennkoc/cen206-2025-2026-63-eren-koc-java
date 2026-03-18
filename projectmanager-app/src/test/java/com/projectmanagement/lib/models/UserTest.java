package com.projectmanagement.lib.models;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

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
        User user = new User();
        assertNull(user.getId());
        assertNotNull(user.getCreatedDate());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User("u1", "johndoe", "john@example.com", "pass123");
        assertEquals("u1", user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("pass123", user.getPassword());
        assertNotNull(user.getCreatedDate());
    }

    @Test
    public void testSetters() {
        User user = new User();
        user.setId("u2");
        user.setUsername("janedoe");
        user.setEmail("jane@example.com");
        user.setPassword("newpass");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedDate(now);

        assertEquals("u2", user.getId());
        assertEquals("janedoe", user.getUsername());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
        assertEquals(now, user.getCreatedDate());
    }

    @Test
    public void testDisplayDetails() {
        User user = new User("u3", "alice", "alice@example.com", "pass");
        user.displayDetails();
        String output = outContent.toString();
        assertTrue(output.contains("User ID: u3"));
        assertTrue(output.contains("Username: alice"));
        assertTrue(output.contains("Email: alice@example.com"));
    }
}
