package com.bhupender.minerva.mapper;

import com.bhupender.minerva.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TaskRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        // Mock ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getLong("taskId")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("Task 1");
        Mockito.when(resultSet.getString("cronTime")).thenReturn("*/5 * * * *");
        Mockito.when(resultSet.getBoolean("executeOnce")).thenReturn(true);
        Mockito.when(resultSet.getTimestamp("dateTime")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        Mockito.when(resultSet.getString("command")).thenReturn("command");
        Mockito.when(resultSet.getBoolean("immediate")).thenReturn(true);
        Mockito.when(resultSet.getTimestamp("createTime")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        // Create TaskRowMapper instance
        RowMapper<Task> taskRowMapper = new TaskRowMapper();

        // Map ResultSet to Task object
        Task task = taskRowMapper.mapRow(resultSet, 1);

        // Verify mapping
        Assertions.assertNotNull(task);
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
    void testMapRow_NullValues() throws SQLException {
        // Mock ResultSet with null values for certain columns
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getLong("taskId")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn(null); // Null value for name
        Mockito.when(resultSet.getString("cronTime")).thenReturn("*/5 * * * *");
        Mockito.when(resultSet.getBoolean("executeOnce")).thenReturn(true);
        Mockito.when(resultSet.getTimestamp("dateTime")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        Mockito.when(resultSet.getString("command")).thenReturn("command");
        Mockito.when(resultSet.getBoolean("immediate")).thenReturn(true);
        Mockito.when(resultSet.getTimestamp("createTime")).thenReturn(Timestamp.valueOf(LocalDateTime.now())); // Return LocalDateTime for createTime

        // Create TaskRowMapper instance
        RowMapper<Task> taskRowMapper = new TaskRowMapper();

        // Map ResultSet to Task object
        Task task = taskRowMapper.mapRow(resultSet, 1);

        // Verify mapping
        Assertions.assertNotNull(task);
        Assertions.assertNull(task.getName()); // Null value should be retained
        Assertions.assertNotNull(task.getCreateTime()); // LocalDateTime object should not be null
    }

    // Add more edge cases as needed
}
