package com.vuhlog.chat.kafka;

import com.vuhlog.notification.dto.Request.NotificationsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducer {
    private final NewTopic topic;

    private final KafkaTemplate<String, NotificationsRequest> kafkaTemplate;

    public NotificationProducer(NewTopic topic, KafkaTemplate<String, NotificationsRequest> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotifications(NotificationsRequest request) {
        log.info("Sending notifications to {}: {}", topic.name(), request.getContent());

        //create message
        Message<NotificationsRequest> mesage = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(mesage);
    }
}
