package com.vuhlog.notification.constant;

import lombok.Getter;

@Getter
public enum NotificationType {
    NEW_MESSAGE("new message"),
    FRIEND_REQUEST("friend request"),;
    NotificationType(String type) {
        this.type = type;
    }

    private final String type;
}
