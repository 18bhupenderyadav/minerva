package com.bhupender.minerva.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageToRabbitMQ(com.bhupender.minerva.model.Task task) {
        rabbitTemplate.convertAndSend("taskQueue", task);
    }
}
