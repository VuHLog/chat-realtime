package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.ApiResponse;
import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    @Autowired
    private MessagesService messagesService;

    @PostMapping("")
    public ApiResponse<MessagesResponse> sendMessage(@RequestBody MessagesRequest request) throws Exception {
        String conversationId = request.getConversationId();
        return ApiResponse.<MessagesResponse>builder()
                .result(messagesService.sendMessage(request, conversationId))
                .build();
    }
}
