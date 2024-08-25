package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.chat.dto.Response.MessagesResponse;

public interface MessagesService {
    public MessagesResponse sendMessage(MessagesRequest request, String conversationId);
}
