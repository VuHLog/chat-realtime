package com.vuhlog.chat.consumer;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.service.MessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQJsonConsumer {
    private final MessagesService messagesService;

    public RabbitMQJsonConsumer(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
    public void consume(MessagesRequest message) {
        log.info("Received json message: {}", message.toString());
        messagesService.saveMessage(message);
    }
}
