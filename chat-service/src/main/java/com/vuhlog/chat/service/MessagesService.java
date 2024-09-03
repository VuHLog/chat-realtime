package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessagesService {
    MessagesResponse sendMessage(MessagesRequest request, String conversationId);

    MessagesResponse getMessageById(String messageId);

    Page<MessagesResponse> getMessagesByConversationIdOrderByTimeSentDesc(String conversationId, Pageable pageable);

    MessagesResponse MessageResponseWS(String messageId);

    void deleteMessageById(String messageId);
}
