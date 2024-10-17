package com.vuhlog.notification.service;

import com.vuhlog.notification.constant.NotificationStatus;
import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.notification.dto.Response.NotificationStatusResponse;
import com.vuhlog.notification.dto.Response.NotificationsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationsResponse> getMyNotifications(Pageable pageable);

    NotificationsResponse addNotification(NotificationsRequest notificationsRequest);

    NotificationStatusResponse updateNotificationStatus(String notificationId);

    void deleteNotification(String notificationId);
}
