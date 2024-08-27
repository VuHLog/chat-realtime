package com.vuhlog.chat.controller;

import com.vuhlog.chat.dto.ApiResponse;
import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import com.vuhlog.chat.entity.Conversations;
import com.vuhlog.chat.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("")
    public Page<ConversationsResponse> getMyConversations(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search
    ) {

        Sort sortable = Sort.by("updatedAt").descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortable);
        return conversationsService.getMyConversations(pageable);
    }
}
