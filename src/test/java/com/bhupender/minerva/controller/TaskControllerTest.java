package com.bhupender.minerva.controller;

import com.bhupender.minerva.model.Task;
import com.bhupender.minerva.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

//     TODO: Hit catch blocks as well in the unit tests.

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // Mocking behavior for taskService methods
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(
                new Task(1L, "Task 1", "*/5 * * * *", true, null, "command", true, null),
                new Task(2L, "Task 2", "*/10 * * * *", false, null, "command", false, null)
        ));
    }

    @Test
    void testGetAllTasks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Task 2"));
    }

    @Test
    void testGetTaskById() throws Exception {
        Long taskId = 1L;
        Task task = new Task(taskId, "Task 1", "*/5 * * * *", true, null, "command", true, null);

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Task 1"));
    }

    @Test
    void testCreateTask() throws Exception {
        Task task = new Task(1L, "Task 1", "*/5 * * * *", true, null, "command", true, null);

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .content(asJsonString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Task 1"));
    }

    @Test
    void testUpdateTask() throws Exception {
        Long taskId = 1L;
        Task task = new Task(taskId, "Updated Task 1", "*/5 * * * *", true, null, "updated command", true, null);

        when(taskService.updateTask(taskId, task)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{taskId}", taskId)
                        .content(asJsonString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Task 1"));
    }

    @Test
    void testDeleteTask() throws Exception {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{taskId}", taskId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // Helper method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
