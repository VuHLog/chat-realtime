package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;
import com.vuhlog.chat.dto.Response.LatestConversationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversationsService {
    ConversationsResponse getPrivateMessageByMemberIdAndType(String userId, String receiverId, int type);

    ConversationsResponse getConversationById(String conversationId);

    Page<ConversationsResponse> getMyConversations(Pageable pageable);

    LatestConversationResponse getLatestConversation();

    ConversationsResponse addConversation(ConversationsRequest request);

    void deleteConversation(String conversationId);

    boolean isInConversation(String conversationId);
}
