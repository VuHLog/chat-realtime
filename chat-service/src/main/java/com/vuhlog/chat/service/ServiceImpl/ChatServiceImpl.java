package com.vuhlog.chat.service.ServiceImpl;

import com.vuhlog.chat.Utils.TimestampUtil;
import com.vuhlog.chat.dto.Request.MessagesRequest;
import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.chat.publisher.RabbitMQJsonProducer;
import com.vuhlog.chat.repository.httpClients.IdentityClient;
import com.vuhlog.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitMQJsonProducer rabbitMQJsonProducer;

    @Autowired
    private IdentityClient identityClient;

    public ChatServiceImpl(SimpMessagingTemplate messagingTemplate, RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.messagingTemplate = messagingTemplate;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @Override
    public void sendMessage(MessagesRequest request) {
        String conversationId = request.getConversationId();

        String timeSent = request.getTimeSent();
        Timestamp timestamp = TimestampUtil.stringToTimestamp(timeSent);
        request.setTimeSent(TimestampUtil.timestampToString(timestamp));
        String destination = "/topic";
        messagingTemplate.convertAndSend(destination + "/conversations/" + conversationId, request);

        request.setTimeSent(timeSent);
        rabbitMQJsonProducer.sendJsonMessage(request);
    }

    @Override
    public void sendNotification(NotificationsRequest request) {
        String destination = "/topic/notifications/receiver/"+request.getReceiverId();
        messagingTemplate.convertAndSend(destination, request);
    }
}
