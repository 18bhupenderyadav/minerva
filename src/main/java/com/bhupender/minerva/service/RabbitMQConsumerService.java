package com.bhupender.minerva.service;

import com.bhupender.minerva.model.Task;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerService {

    @Autowired
    private TaskRedisService taskRedisService;

    @Autowired
    private TaskExecutorService taskExecutorService;

    @RabbitListener(queues = "executionQueue")
    public void receiveMessageFromExecutionQueue(Task task) {

        // Process the incoming message
        System.out.println("Received message from TaskExecutionQueue: " + task);
        System.out.println(task.getTaskId());

        // Validate the contents of the message to be sane!
        // TODO: The validation
        // Execute the task using the executor service
        taskExecutorService.executeTask(task);
    }

    @RabbitListener(queues = "taskQueue")
    public void receiveMessageFromRabbitMQ(Task task) {
        // Process the incoming message
        System.out.println("Received message from RabbitMQ: " + task);
        System.out.println(task.getTaskId());

        // Validate the contents of the message to be sane!
        // TODO: The validation


        // Store the task in Redis using TaskRedisService
        taskRedisService.saveTaskToRedis(task);


        // Add your business logic here to handle the received task

    }
}
