package com.vuhlog.chat.service;

import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.notification.dto.Request.NotificationsRequest;

public interface ChatService {
    void sendMessage(MessagesRequest request);
    void sendNotification(NotificationsRequest request);
}
