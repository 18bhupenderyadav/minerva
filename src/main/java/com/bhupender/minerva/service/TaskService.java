package com.bhupender.minerva.service;

import com.bhupender.minerva.builder.TaskBuilder;
import com.bhupender.minerva.model.Task;
import com.bhupender.minerva.model.dto.TaskDto;
import com.bhupender.minerva.model.dto.TaskReadDto;
import com.bhupender.minerva.repository.TaskRepository;
import com.bhupender.minerva.service.exception.NotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<TaskReadDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(TaskBuilder::convertToReadDto)
                .collect(Collectors.toList());
    }

    public TaskReadDto getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new NotFoundException("Task not found with id: " + taskId);
        }
        return TaskBuilder.convertToReadDto(task);
    }

    public TaskReadDto createTask(TaskDto taskDto) {
        Task task = TaskBuilder.convertToEntity(taskDto);

        // TODO: Have a try catch block in case the task is ill formed
        task = taskRepository.create(task);


        // Check if the task needs to be executed today
        if (isTaskScheduledForToday(task)) {
            // Send the task to RabbitMQ
            sendMessageToRabbitMQ(task);
        }

        return TaskBuilder.convertToReadDto(task);
    }

    private void sendMessageToRabbitMQ(Task task) {
        rabbitTemplate.convertAndSend("taskQueue", task);
    }

    private boolean isTaskScheduledForToday(Task task) {

        // TODO: Have logic implemented here. Also make sure to get all
        //  the times the task is supposed to be run today
        return true;
    }

    public TaskDto updateTask(Integer taskId, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(taskId);
        if (existingTask == null) {
            throw new NotFoundException("Task not found with id: " + taskId);
        }
        Task updatedTask = TaskBuilder.convertToEntity(taskDto);
        updatedTask.setTaskId(taskId);
        updatedTask = taskRepository.update(updatedTask);
        return TaskBuilder.convertToDto(updatedTask);
    }

    public void deleteTask(Integer taskId) {
        int result = taskRepository.deleteById(taskId);
        if (result == 0) {
            throw new NotFoundException("Task not found with id: " + taskId);
        }
    }
}
