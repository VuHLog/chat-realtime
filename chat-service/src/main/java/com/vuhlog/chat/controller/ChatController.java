package com.vuhlog.chat.controller;

import com.vuhlog.chat.Utils.TimestampUtil;
import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.publisher.RabbitMQJsonProducer;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    private final RabbitMQJsonProducer rabbitMQJsonProducer;

    public ChatController(SimpMessagingTemplate messagingTemplate, RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.messagingTemplate = messagingTemplate;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessagesRequest request) throws Exception {
        String conversationId = request.getConversationId();

        String timeSent = request.getTimeSent();
        Timestamp timestamp =TimestampUtil.stringToTimestamp(timeSent);
        request.setTimeSent(TimestampUtil.timestampToString(timestamp));
        String destination = "/topic/conversations/" + conversationId;
        messagingTemplate.convertAndSend(destination, request);

        request.setTimeSent(timeSent);
        rabbitMQJsonProducer.sendJsonMessage(request);
    }

}
