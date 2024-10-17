package com.vuhlog.notification.dto.Response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationsResponse {
    private String id;

    private String senderId;

    private String receiverId;

    private String content;

    private String notificationType;

    private String timestamp;

    private String status;

    private String href;
}
