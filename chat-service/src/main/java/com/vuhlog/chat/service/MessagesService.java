package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessagesService {
    MessagesResponse saveMessage(MessagesRequest request);

    MessagesResponse getMessageById(String messageId);

    Page<MessagesResponse> getMessagesByConversationIdOrderByTimeSentDesc(Integer pageNumber, Integer pageSize, String sort, String text, String conversationId);

    MessagesResponse MessageResponseWS(String messageId);

    void deleteMessageById(String messageId);
}
