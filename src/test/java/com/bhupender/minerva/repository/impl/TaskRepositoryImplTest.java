package com.bhupender.minerva.repository.impl;

import com.bhupender.minerva.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class) // Add this annotation to enable Mockito extension
public class TaskRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        reset(jdbcTemplate); // Using Mockito.reset to clear any interactions
    }

    @Test
    void testFindAll() {
        // Mocking behavior
        Mockito.when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(new Task()));

        // Perform the test
        List<Task> tasks = taskRepository.findAll();

        // Assertions
        Assertions.assertNotNull(tasks);
        Assertions.assertFalse(tasks.isEmpty());
    }

    @Test
    void testFindById() {
        // Mocking behavior
        Mockito.when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(new Task());

        // Perform the test
        Task task = taskRepository.findById(1L);

        // Assertions
        Assertions.assertNotNull(task);
    }

    @Test
    void testCreate() {
        // Mocking behavior
        Mockito.when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenReturn(1);

        // Prepare a task
        Task task = new Task();
        task.setName("Test Task");
        task.setCronTime("*/5 * * * *");
        task.setExecuteOnce(true);
        task.setDateTime(LocalDateTime.now());
        task.setCommand("command");
        task.setImmediate(true);
        task.setCreateTime(LocalDateTime.now());

        // Perform the test
        Task createdTask = taskRepository.create(task);

        // Assertions
        Assertions.assertNotNull(createdTask);
    }

    @Test
    void testUpdate() {
        // Mocking behavior
        Mockito.when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Prepare a task
        Task task = new Task();
        task.setTaskId(1L);
        task.setName("Test Task");
        task.setCronTime("*/5 * * * *");
        task.setExecuteOnce(true);
        task.setDateTime(LocalDateTime.now());
        task.setCommand("command");
        task.setImmediate(true);
        task.setCreateTime(LocalDateTime.now());

        // Perform the test
        Task updatedTask = taskRepository.update(task);

        // Assertions
        Assertions.assertNotNull(updatedTask);
    }

    @Test
    void testDeleteById() {
        // Mocking behavior
        Mockito.when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Perform the test
        int result = taskRepository.deleteById(1L);

        // Assertions
        Assertions.assertEquals(1, result);
    }
}
