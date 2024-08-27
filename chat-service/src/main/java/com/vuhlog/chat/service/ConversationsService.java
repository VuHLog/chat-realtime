package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversationsService {
    ConversationsResponse getPrivateMessageByMemberIdAndType(String userId, String receiverId, int type);

    ConversationsResponse addConversation(ConversationsRequest request);

    ConversationsResponse getConversationById(String conversationId);

    Page<ConversationsResponse> getMyConversations(Pageable pageable);
}
