package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Request.MessagesWSRequest;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatControlller {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessagesService messagesService;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessagesWSRequest request) throws Exception {
        String conversationId = request.getConversationId();
        String destination = "/topic/conversations/" + conversationId;
        messagingTemplate.convertAndSend(destination, messagesService.MessageResponseWS(request.getMessagesId()));
    }

}
