package com.vuhlog.notification.constant;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    READ("Đã đọc"),
    UNREAD("Chưa đọc");
    NotificationStatus(String status) {
        this.status = status;
    }

    private final String status;
}
