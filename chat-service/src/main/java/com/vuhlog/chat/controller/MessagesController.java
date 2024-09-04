package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.ApiResponse;
import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    @Autowired
    private MessagesService messagesService;

    @PostMapping("")
    public ApiResponse<MessagesResponse> sendMessage(@RequestBody MessagesRequest request) {
        String conversationId = request.getConversationId();
        return ApiResponse.<MessagesResponse>builder()
                .result(messagesService.sendMessage(request, conversationId))
                .build();
    }

    @GetMapping("/conversation")
    public Page<MessagesResponse> getMessagesByConversationId(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "conversationId", required = true, defaultValue = "0") String conversationId
    ) {

        Sort sortable = Sort.by("timeSent").descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortable);
        return messagesService.getMessagesByConversationIdOrderByTimeSentDesc(conversationId, pageable);
    }

    @GetMapping("/{messageId}")
    public ApiResponse<MessagesResponse> getMessageById(@PathVariable("messageId") String messageId) {
        return ApiResponse.<MessagesResponse>builder()
                .result(messagesService.getMessageById(messageId))
                .build();
    }

    @DeleteMapping("/{messageId}")
    public ApiResponse<Void> deleteMessageById(@PathVariable("messageId") String messageId) {
        messagesService.deleteMessageById(messageId);
        return ApiResponse.<Void>builder()
                .build();
    }
}
