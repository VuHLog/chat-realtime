package com.vuhlog.notification.controller;

import com.vuhlog.notification.dto.ApiResponse;
import com.vuhlog.notification.dto.Request.NotificationsRequest;
import com.vuhlog.notification.dto.Response.NotificationStatusResponse;
import com.vuhlog.notification.dto.Response.NotificationsResponse;
import com.vuhlog.notification.entity.Notifications;
import com.vuhlog.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("")
    public ApiResponse<NotificationsResponse> createNotification(@RequestBody NotificationsRequest request) {
        return ApiResponse.<NotificationsResponse>builder()
                .result(notificationService.addNotification(request))
                .build();
    }

    @GetMapping("/my-notifications")
    public Page<NotificationsResponse> getMyNotifications(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize
    ) {
        Sort sortable = Sort.by("timestamp").descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortable);
        return notificationService.getMyNotifications(pageable);
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<NotificationStatusResponse> updateNotificationStatus(@PathVariable String notificationId) {
        return ApiResponse.<NotificationStatusResponse>builder()
                .result(notificationService.updateNotificationStatus(notificationId))
                .build();
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<String> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.<String>builder()
                .result("Notification has been deleted")
                .build();
    }
}
