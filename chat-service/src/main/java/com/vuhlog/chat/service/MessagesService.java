package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessagesService {
    public MessagesResponse sendMessage(MessagesRequest request, String conversationId);

    public MessagesResponse getMessageById(String messageId);

    public Page<MessagesResponse> getMessagesByConversationIdOrderByTimeSentDesc(String conversationId, Pageable pageable);

    public MessagesResponse MessageResponseWS(String messageId);
}
