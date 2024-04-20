package com.bhupender.minerva.dto;

import com.bhupender.minerva.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TaskTest {

    @Test
    void testTaskCreation() {
        // Test task creation with all fields set
        Task task = new Task(1L, "Task 1", "*/5 * * * *", true, LocalDateTime.now(), "command", true, LocalDateTime.now());
        Assertions.assertEquals(1L, task.getTaskId());
        Assertions.assertEquals("Task 1", task.getName());
        Assertions.assertEquals("*/5 * * * *", task.getCronTime());
        Assertions.assertTrue(task.getExecuteOnce());
        Assertions.assertNotNull(task.getDateTime());
        Assertions.assertEquals("command", task.getCommand());
        Assertions.assertTrue(task.getImmediate());
        Assertions.assertNotNull(task.getCreateTime());
    }

    @Test
    void testTaskEquality() {
        // Test task equality
        Task task1 = new Task(1L, "Task 1", "*/5 * * * *", true, LocalDateTime.now(), "command", true, LocalDateTime.now());
        Task task2 = new Task(1L, "Task 1", "*/5 * * * *", true, LocalDateTime.now(), "command", true, LocalDateTime.now());
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void testTaskInequality() {
        // Test task inequality
        Task task1 = new Task(1L, "Task 1", "*/5 * * * *", true, LocalDateTime.now(), "command", true, LocalDateTime.now());
        Task task2 = new Task(2L, "Task 2", "0 * * * *", false, null, "command2", false, null);
        Assertions.assertNotEquals(task1, task2);
    }

    @Test
    void testTaskHashCode() {
        // Test task hash code
        Task task1 = new Task(1L, "Task 1", "*/5 * * * *", true, LocalDateTime.now(), "command", true, LocalDateTime.now());
        Task task2 = new Task(1L, "Task 1", "*/5 * * * *", true, LocalDateTime.now(), "command", true, LocalDateTime.now());
        Assertions.assertEquals(task1.hashCode(), task2.hashCode());
    }

    // Add more test cases as needed
}
