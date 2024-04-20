package com.bhupender.minerva.service;

import com.bhupender.minerva.model.Task;
import com.bhupender.minerva.repository.TaskRepository;
import com.bhupender.minerva.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize annotated fields
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        List<Task> mockTasks = Collections.singletonList(new Task());
        when(taskRepository.findAll()).thenReturn(mockTasks);

        // Act
        List<Task> allTasks = taskService.getAllTasks();

        // Assert
        assertEquals(1, allTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById_ExistingTask() {
        // Arrange
        Long taskId = 1L;
        Task mockTask = new Task();
        when(taskRepository.findById(taskId)).thenReturn(mockTask);

        // Act
        Optional<Task> taskById = taskService.getTaskById(taskId);

        // Assert
        assertTrue(taskById.isPresent());
        assertEquals(mockTask, taskById.get());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testGetTaskById_NonExistingTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(null);

        // Act
        Optional<Task> taskById = taskService.getTaskById(taskId);

        // Assert
        assertFalse(taskById.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testCreateTask() {
        // Arrange
        Task taskToCreate = new Task();
        Task createdTask = new Task(); // Assuming the repository returns the created task
        when(taskRepository.create(taskToCreate)).thenReturn(createdTask);

        // Act
        Task returnedTask = taskService.createTask(taskToCreate);

        // Assert
        assertNotNull(returnedTask);
        assertEquals(createdTask, returnedTask);
        verify(taskRepository, times(1)).create(taskToCreate);
    }

    @Test
    void testUpdateTask_ExistingTask() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        Task updatedTask = new Task(); // Assuming the repository returns the updated task
        when(taskRepository.findById(taskId)).thenReturn(existingTask);
        when(taskRepository.update(existingTask)).thenReturn(updatedTask);

        // Act
        Task returnedTask = taskService.updateTask(taskId, existingTask);

        // Assert
        assertNotNull(returnedTask);
        assertEquals(updatedTask, returnedTask);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).update(existingTask);
    }

    @Test
    void testUpdateTask_NonExistingTask() {
        // Arrange
        Long taskId = 1L;
        Task nonExistingTask = new Task();
        when(taskRepository.findById(taskId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> taskService.updateTask(taskId, nonExistingTask));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).update(nonExistingTask);
    }

    @Test
    void testDeleteTask_ExistingTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.deleteById(taskId)).thenReturn(1);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testDeleteTask_NonExistingTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.deleteById(taskId)).thenReturn(0);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> taskService.deleteTask(taskId));
        verify(taskRepository, times(1)).deleteById(taskId);
    }
}
