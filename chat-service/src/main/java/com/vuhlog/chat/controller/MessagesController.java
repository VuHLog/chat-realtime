package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.ApiResponse;
import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.exception.AppException;
import com.vuhlog.chat.exception.ErrorCode;
import com.vuhlog.chat.service.ConversationsService;
import com.vuhlog.chat.service.MessagesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private final MessagesService messagesService;

    private final ConversationsService conversationsService;

    public MessagesController(MessagesService messagesService, ConversationsService conversationsService) {
        this.messagesService = messagesService;
        this.conversationsService = conversationsService;
    }

    @PostMapping("")
    public ApiResponse<MessagesResponse> sendMessage(@RequestBody MessagesRequest request) {
        return ApiResponse.<MessagesResponse>builder()
                .result(messagesService.saveMessage(request))
                .build();
    }

    @GetMapping("/conversation")
    public Page<MessagesResponse> getMessagesByConversationId(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "conversationId", required = true, defaultValue = "0") String conversationId,
            @RequestParam(name = "text", required = false, defaultValue = "0") String text
    ) {
        //check permission
        if(!conversationsService.isInConversation(conversationId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return messagesService.getMessagesByConversationIdOrderByTimeSentDesc(pageNumber, pageSize, sort, text, conversationId);
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
