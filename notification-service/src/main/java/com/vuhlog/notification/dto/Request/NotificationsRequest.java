package com.vuhlog.notification.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationsRequest {
    private String senderId;

    private String receiverId;

    private String content;

    private String notificationType;

    private String href;
}
