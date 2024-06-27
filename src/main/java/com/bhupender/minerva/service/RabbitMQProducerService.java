package com.bhupender.minerva.service;

import com.bhupender.minerva.model.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {

    @Value("${spring.rabbitmq.task-queue-name:taskQueue}")
    private String taskQueueName;

    @Value("${spring.rabbitmq.execution-queue-name:executionQueue}")
    private String executionQueueName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageToTaskQueue(Task task) {
        rabbitTemplate.convertAndSend(taskQueueName, task);
    }

    public void sendMessageToExecutionQueue(Task task) {
        rabbitTemplate.convertAndSend(executionQueueName, task);
    }
}
