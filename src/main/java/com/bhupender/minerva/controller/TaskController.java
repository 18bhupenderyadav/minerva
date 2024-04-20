package com.bhupender.minerva.controller;

import com.bhupender.minerva.builder.TaskBuilder;
import com.bhupender.minerva.model.dto.TaskDto;
import com.bhupender.minerva.model.dto.TaskReadDto;
import com.bhupender.minerva.service.TaskService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskBuilder taskBuilder;

    @GetMapping
    public ResponseEntity<List<TaskReadDto>> getAllTasks() {
        try {
            List<TaskReadDto> tasks = taskService.getAllTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskReadDto> getTaskById(@PathVariable Integer taskId) {
        try {
            TaskReadDto task = taskService.getTaskById(taskId);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<TaskReadDto> createTask(@RequestBody TaskDto taskDto) {
        try {
            TaskReadDto createdTaskDto = taskService.createTask(taskDto);
            // Convert the created TaskDto to TaskReadDto before returning
            return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Integer taskId, @RequestBody TaskDto taskDto) {
        try {
            TaskDto updatedTask = taskService.updateTask(taskId, taskDto);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable Integer taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
