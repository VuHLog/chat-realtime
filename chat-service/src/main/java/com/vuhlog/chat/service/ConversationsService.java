package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.ConversationsRequest;
import com.vuhlog.chat.dto.Response.ConversationsResponse;

public interface ConversationsService {
    ConversationsResponse getPrivateMessageByMemberIdAndType(String userId, String receiverId, int type);

    ConversationsResponse addConversation(ConversationsRequest request);

    ConversationsResponse getConversationById(String conversationId);
}
