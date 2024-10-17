package com.vuhlog.notification.service.serviceImpl;

import com.vuhlog.notification.constant.NotificationStatus;
import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.notification.dto.Response.NotificationStatusResponse;
import com.vuhlog.notification.dto.Response.NotificationsResponse;
import com.vuhlog.notification.entity.Notifications;
import com.vuhlog.notification.exception.AppException;
import com.vuhlog.notification.exception.ErrorCode;
import com.vuhlog.notification.mapper.NotificationsMapper;
import com.vuhlog.notification.repository.NotificationRepository;
import com.vuhlog.notification.repository.httpClients.IdentityClient;
import com.vuhlog.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationsMapper notificationsMapper;
    private final IdentityClient identityClient;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationsMapper notificationsMapper, IdentityClient identityClient, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.notificationsMapper = notificationsMapper;
        this.identityClient = identityClient;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Page<NotificationsResponse> getMyNotifications(Pageable pageable) {
        return notificationRepository.findByReceiverId(getMyUserId(),pageable).map(notificationsMapper::toNotificationResponse);
    }

    @Override
    public NotificationsResponse addNotification(NotificationsRequest request) {
        Notifications notification = notificationsMapper.toNotification(request);
        notification.setSenderId(request.getSenderId());
        notification.setStatus(NotificationStatus.UNREAD.getStatus());

        return notificationsMapper.toNotificationResponse(notificationRepository.save(notification));
    }

    @Override
    public NotificationStatusResponse updateNotificationStatus(String notificationId) {
        Notifications notification = notificationRepository.findById(notificationId).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        notification.setStatus(NotificationStatus.READ.getStatus());
        return notificationsMapper.toNotificationStatusResponse(notificationRepository.save(notification));
    }

    @Override
    public void deleteNotification(String notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private String getMyUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return identityClient.getUserByUsername(username).getResult().getId();
    }
}
