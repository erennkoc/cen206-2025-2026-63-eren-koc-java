package com.projectmanagement.lib.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStatusTest {

    @Test
    public void testEnumValues() {
        TaskStatus[] statuses = TaskStatus.values();
        assertEquals(3, statuses.length);
        assertEquals(TaskStatus.TODO, TaskStatus.valueOf("TODO"));
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.valueOf("IN_PROGRESS"));
        assertEquals(TaskStatus.DONE, TaskStatus.valueOf("DONE"));
    }
}
