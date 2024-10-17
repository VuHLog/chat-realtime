package com.vuhlog.notification.kafka;

import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationsConsumer {
    private final NotificationService notificationService;
    public NotificationsConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(NotificationsRequest request) {
        log.info("Notifications kafka consumer received: {}", request.getContent());
        notificationService.addNotification(request);
    }
}
