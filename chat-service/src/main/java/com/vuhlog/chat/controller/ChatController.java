package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.chat.kafka.NotificationProducer;
import com.vuhlog.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatService chatService;
    private final NotificationProducer notificationProducer;

    public ChatController(ChatService chatService, NotificationProducer notificationProducer) {
        this.chatService = chatService;
        this.notificationProducer = notificationProducer;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessagesRequest request) {
        chatService.sendMessage(request);
    }

    @MessageMapping("/sendNotification")
    public void sendNotification(@Payload NotificationsRequest request) {
        //send notification to notification-service
        notificationProducer.sendNotifications(request);

        // send notification realtime
        chatService.sendNotification(request);
    }

}
