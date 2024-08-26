package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.ApiResponse;
import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversations")
public class ConversationsController {
    @Autowired
    private ConversationsService conversationsService;

    @PostMapping
    public ApiResponse<ConversationsResponse> createConversation(@RequestBody ConversationsRequest request){
        return ApiResponse.<ConversationsResponse>builder()
                .result(conversationsService.addConversation(request))
                .build();
    }

    @GetMapping("/private-message")
    public ApiResponse<ConversationsResponse> getPrivateMessage(
            @RequestParam(required = true) String userId,
            @RequestParam(required = true) String receiverId,
            @RequestParam(required = true) int type){
        return ApiResponse.<ConversationsResponse>builder()
                .result(conversationsService.getPrivateMessageByMemberIdAndType(userId, receiverId, type))
                .build();
    }

    @GetMapping("/{conversationId}")
    public ApiResponse<ConversationsResponse> getConversationById(@PathVariable String conversationId){
        return ApiResponse.<ConversationsResponse>builder()
                .result(conversationsService.getConversationById(conversationId))
                .build();
    }
}
